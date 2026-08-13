package nlp4j.lucene;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

import org.apache.lucene.document.Document;

import nlp4j.impl.DefaultDocument;
import nlp4j.json.JsonNode;
import nlp4j.krmj.annotator.KuromojiAnnotator;
import nlp4j.lucene9.FieldTypeDef;
import nlp4j.lucene9.LuceneIndex;
import nlp4j.lucene9.LuceneLocalSearchApi;
import nlp4j.lucene9.SearchSchema;

/**
 * Simple local search engine wrapper for Lucene. Provides a simplified API for
 * adding documents and performing text searches with language-specific field
 * support (Japanese, English, or default).
 *
 * <p>
 * This class automatically manages the Lucene index lifecycle and provides a
 * high-level interface for common search operations.
 * </p>
 *
 * <p>
 * Builder を使った標準的な利用方法:
 * </p>
 *
 * <pre>
 * // オンメモリ、日本語、自動解析あり（デフォルト設定）
 * try (LocalSearch search = LocalSearch.builder("ja").build()) {
 * 	search.add("doc1", "東京の観光スポット");
 * 	search.commit();
 * 	SearchResult[] results = search.search("東京", 10);
 * }
 *
 * // 自動解析を無効化
 * try (LocalSearch search = LocalSearch.builder("ja")
 * 		.autoAnalyze(false)
 * 		.build()) {
 * 	// ...
 * }
 *
 * // ディスクインデックス + ベクトル検索
 * try (LocalSearch search = LocalSearch.builder("ja")
 * 		.vectorDimension(1024)
 * 		.indexDirectory(Path.of("./index"))
 * 		.build()) {
 * 	// ...
 * }
 * </pre>
 *
 * <p>
 * 従来の new LocalSearch("ja") も引き続き利用できます。
 * </p>
 */
public class LocalSearch implements AutoCloseable {

	// -----------------------------------------------------------------------
	// Builder
	// -----------------------------------------------------------------------

	/**
	 * Returns a new {@link Builder} for the specified language.
	 *
	 * @param language the language code ("ja" for Japanese, "en" for English, or
	 *                 any other value for default text field)
	 * @return a new Builder
	 */
	public static Builder builder(String language) {
		return new Builder(language);
	}

	/**
	 * Builder for {@link LocalSearch}.
	 *
	 * <pre>
	 * LocalSearch search = LocalSearch.builder("ja")
	 *         .autoAnalyze(true)
	 *         .vectorDimension(1024)
	 *         .indexDirectory(Path.of("./index"))
	 *         .build();
	 * </pre>
	 */
	public static class Builder {

		private final String language;
		private boolean autoAnalyze = true;
		private int vectorDimension = 0;
		private Path indexDir = null;

		private Builder(String language) {
			this.language = language;
		}

		/**
		 * KuromojiAnnotator による形態素解析（自動エンリッチ）を有効／無効にします。
		 * デフォルトは {@code true}（有効）。language が "ja" 以外の場合は
		 * この設定によらず解析は実行されません。
		 *
		 * @param autoAnalyze true で自動解析を有効化
		 * @return this Builder
		 */
		public Builder autoAnalyze(boolean autoAnalyze) {
			this.autoAnalyze = autoAnalyze;
			return this;
		}

		/**
		 * KNN ベクトル検索に使用するベクトルの次元数を設定します。
		 * 0（デフォルト）の場合はベクトルフィールドを作成しません。
		 *
		 * @param vectorDimension ベクトルの次元数（0 以上）
		 * @return this Builder
		 */
		public Builder vectorDimension(int vectorDimension) {
			this.vectorDimension = vectorDimension;
			return this;
		}

		/**
		 * ディスク上の Lucene インデックスディレクトリを指定します。
		 * 指定しない場合はオンメモリインデックスを使用します。
		 *
		 * @param indexDir インデックスを格納するディレクトリパス
		 * @return this Builder
		 */
		public Builder indexDirectory(Path indexDir) {
			this.indexDir = indexDir;
			return this;
		}

		/**
		 * 設定した内容で {@link LocalSearch} インスタンスを生成します。
		 *
		 * @return 新しい LocalSearch インスタンス
		 * @throws LocalSearchException if initialization fails
		 */
		public LocalSearch build() {
			return new LocalSearch(this);
		}
	}

	// -----------------------------------------------------------------------
	// Fields
	// -----------------------------------------------------------------------

	private String language;
	private boolean autoAnalyze;

	private String default_field_name;
	SearchSchema schema;
	LuceneIndex index;

	LuceneLocalSearchApi api;

	// -----------------------------------------------------------------------
	// Constructors
	// -----------------------------------------------------------------------

	/**
	 * Builder から LocalSearch を生成するプライベートコンストラクタ。
	 * すべての公開コンストラクタはここに委譲します。
	 */
	private LocalSearch(Builder builder) {
		if (builder.vectorDimension < 0) {
			throw new LocalSearchException("vectorDimension must be >= 0",
					new IllegalArgumentException("vectorDimension must be >= 0"));
		}

		this.language = builder.language;
		this.autoAnalyze = builder.autoAnalyze;

		if (builder.indexDir == null) {
			initIndex();
		} else {
			initIndex(builder.indexDir);
		}

		this.schema = createSchema(builder.vectorDimension);
		this.default_field_name = resolveDefaultFieldName(builder.language);
	}

	/**
	 * Constructs a new LocalSearch instance with the specified language.
	 *
	 * @param language the language code ("ja" for Japanese, "en" for English, or
	 *                 any other value for default text field)
	 * @throws LocalSearchException if index initialization fails
	 */
	public LocalSearch(String language) {
		this(new Builder(language));
	}

	public LocalSearch(String language, int vectorDimension) {
		this(new Builder(language).vectorDimension(vectorDimension));
	}

	public LocalSearch(String language, int vectorDimension, File indexDir) {
		this(new Builder(language).vectorDimension(vectorDimension).indexDirectory(indexDir.toPath()));
	}

	public LocalSearch(String language, int vectorDimension, Path indexDir) {
		this(new Builder(language).vectorDimension(vectorDimension).indexDirectory(indexDir));
	}

	// -----------------------------------------------------------------------
	// Static factory
	// -----------------------------------------------------------------------

	public static LocalSearch open(String language, int vectorDimension, Path indexDir) {
		return new Builder(language).vectorDimension(vectorDimension).indexDirectory(indexDir).build();
	}

	public void add(String id, float[] vector) {
		Document doc1 = schema.document() //
				.put("id", id) //
				.putVector("vector", vector) //
				.build();
		try {
			this.index.add(doc1);
		} catch (IOException e) {
			throw new LocalSearchException(e.getMessage(), e);
		}
	}

	/**
	 * ベクトルと追加フィールドを同一 Document に登録します。
	 * ベクトル検索時のフィールドフィルターを利用するには、このメソッドで文書を追加してください。
	 *
	 * <p>例:</p>
	 * <pre>
	 * search.add(
	 *     "1",
	 *     new float[] { 1.0f, 0.0f },
	 *     java.util.Map.of("category", "technology", "country", "Japan")
	 * );
	 * </pre>
	 *
	 * @param id      ドキュメントの一意識別子
	 * @param vector  ベクトル
	 * @param fields  keyword フィールドの追加値（フィールド名 → 値）
	 * @throws LocalSearchException if adding the document fails
	 */
	public void add(String id, float[] vector, java.util.Map<String, String> fields) {
		try {
			var builder = schema.document()
					.put("id", id)
					.putVector("vector", vector);

			if (fields != null) {
				for (java.util.Map.Entry<String, String> entry : fields.entrySet()) {
					String fieldName = entry.getKey();
					String value = entry.getValue();
					if (fieldName == null || value == null) {
						continue;
					}
					ensureKeywordField(fieldName);
					builder.put(fieldName, value);
				}
			}

			this.index.add(builder.build());

		} catch (IOException e) {
			throw new LocalSearchException(e.getMessage(), e);
		}
	}

	/**
	 * Adds a document to the search index.
	 *
	 * <p>language が "ja" の場合は KuromojiAnnotator による形態素解析を自動実行し、
	 * word.* フィールドに原形（見出し語）を登録します。</p>
	 *
	 * @param id   the unique identifier for the document
	 * @param body the text content to be indexed
	 * @throws LocalSearchException if adding the document fails
	 */
	public void add(String id, String body) {
		SearchRecord record = new SearchRecord(id, body);
		add(record);
	}

	/**
	 * SearchRecord をそのまま登録します。
	 *
	 * <p>language が "ja" の場合は KuromojiAnnotator による形態素解析を自動実行し、
	 * word.* フィールドに原形（見出し語）を登録します。</p>
	 *
	 * @param record 登録するドキュメントレコード
	 * @throws LocalSearchException if adding the document fails
	 */
	public void add(SearchRecord record) {
		try {
			enrich(record);

			var builder = schema.document()
					.put("id", record.getId())
					.put(default_field_name, record.getBody());

			// word.* フィールドへキーワードを登録
			for (SearchKeyword kw : record.getKeywords()) {
				builder.put(kw.getPos(), kw.getLex());
			}

			// 追加フィールドを登録
			for (String fieldName : record.dataKeys()) {
				for (String value : record.getDataValues(fieldName)) {
					builder.put(fieldName, value);
				}
			}

			this.index.add(builder.build());
		} catch (IOException e) {
			throw new LocalSearchException(e.getMessage(), e);
		}
	}

	/**
	 * Adds a document from a JSON string. The JSON must contain "id" and either
	 * "body" or "text" field. Additional fields are indexed as keyword fields.
	 * JSON array values are indexed as multi-valued keyword fields.
	 *
	 * <p>
	 * Example JSON format:
	 * </p>
	 *
	 * <pre>
	 * // body field (traditional)
	 * {
	 *   "id": "doc1",
	 *   "body": "Document text content",
	 *   "category": "tech"
	 * }
	 *
	 * // text field + array values
	 * {
	 *   "id": "doc2",
	 *   "text": "Document text content",
	 *   "keywords": ["java", "lucene", "search"]
	 * }
	 * </pre>
	 *
	 * @param json_string the JSON string containing document data
	 * @throws LocalSearchException if JSON parsing or document addition fails
	 */
	public void addJson(String json_string) {
		try {
			JsonNode json = JsonNode.parse(json_string);

			String id = getRequiredString(json, "id");
			String body = getDocumentText(json);

			SearchRecord record = new SearchRecord(id, body);

			// JSON の追加フィールドを SearchRecord に転写
			for (String fieldName : json.keys()) {
				if ("id".equals(fieldName)
						|| "body".equals(fieldName)
						|| "text".equals(fieldName)) {
					continue;
				}

				JsonNode valueNode = json.get(fieldName);
				if (valueNode == null || valueNode.isNull()) {
					continue;
				}

				if (valueNode.isArray()) {
					for (JsonNode itemNode : valueNode.asList()) {
						if (itemNode == null || itemNode.isNull()) {
							continue;
						}
						String value = itemNode.asString(null);
						if (value != null) {
							record.addData(fieldName, value);
						}
					}
				} else {
					String value = valueNode.asString(null);
					if (value != null) {
						record.addData(fieldName, value);
					}
				}
			}

			enrich(record);

			var builder = schema.document()
					.put("id", record.getId())
					.put(default_field_name, record.getBody())
					.put("data", json_string);

			// word.* フィールドへキーワードを登録
			for (SearchKeyword kw : record.getKeywords()) {
				builder.put(kw.getPos(), kw.getLex());
			}

			// 追加フィールドを登録
			for (String fieldName : record.dataKeys()) {
				List<String> values = record.getDataValues(fieldName);
				if (values.size() > 1) {
					ensureMultiValuedKeywordField(fieldName);
				} else {
					ensureKeywordField(fieldName);
				}
				for (String value : values) {
					builder.put(fieldName, value);
				}
			}

			this.index.add(builder.build());

		} catch (Throwable th) {
			throw new LocalSearchException(th.getMessage(), th);
		}
	}

	/**
	 * "body" フィールドを優先し、なければ "text" フィールドを返します。
	 * どちらも存在しない場合は IllegalArgumentException をスローします。
	 */
	private String getDocumentText(JsonNode json) {
		JsonNode bodyNode = json.get("body");
		if (bodyNode != null && !bodyNode.isNull()) {
			String body = bodyNode.asString(null);
			if (body != null) {
				return body;
			}
		}
		JsonNode textNode = json.get("text");
		if (textNode != null && !textNode.isNull()) {
			String text = textNode.asString(null);
			if (text != null) {
				return text;
			}
		}
		throw new IllegalArgumentException("Required field is missing: body or text");
	}

	/**
	 * 指定フィールドが未登録の場合、複数値を持つ keyword フィールドとして登録します。
	 * addJson() でのJSON配列フィールド登録に使用します。
	 *
	 * @param fieldName the field name to ensure
	 */
	private void ensureMultiValuedKeywordField(String fieldName) {
		schema.addIfAbsent(fieldName,
				FieldTypeDef.keyword().stored(true).aggregatable(true).multiValued(true));
	}

	/**
	 * Closes the search index and releases all resources. This method is
	 * automatically called when using try-with-resources.
	 *
	 * @throws LocalSearchException if closing the index fails
	 */
	@Override
	public void close() {
		if (this.index != null) {
			try {
				this.index.close();
			} catch (IOException e) {
				throw new LocalSearchException(e.getMessage(), e);
			}
		}
	}

	/**
	 * Commits all pending changes to the index. This method should be called after
	 * adding documents to make them searchable.
	 *
	 * @throws LocalSearchException if commit fails
	 */
	public void commit() {
		try {
			this.index.commit();
		} catch (IOException e) {
			throw new LocalSearchException(e.getMessage(), e);
		}
	}

	/**
	 * 指定フィールドがスキーマに未登録の場合のみ keyword フィールドとして追加します。
	 * addJson() から動的フィールド登録のために使用します。
	 *
	 * @param fieldName the field name to ensure
	 */
	private void ensureKeywordField(String fieldName) {
		// aggregatable() を付けることで SortedDocValuesField がインデックスされ、
		// terms aggregation (TermsAggregation) でも使用可能になる
		schema.addIfAbsent(fieldName, FieldTypeDef.keyword().stored(true).aggregatable(true));
	}

	private SearchSchema createSchema(int vectorDimension) {
		SearchSchema schema = new SearchSchema();

		schema.add("id", FieldTypeDef.keyword().stored(true));
		schema.add("text", FieldTypeDef.text().stored(true));
		schema.add("text_en", FieldTypeDef.text().stored(true));
		schema.add("text_ja", FieldTypeDef.text().stored(true));
		schema.add("data", FieldTypeDef.storedOnly());

		// 形態素解析結果の word.* フィールド（multiValued keyword）
		schema.add("word",      FieldTypeDef.keyword().stored(true).aggregatable(true).multiValued(true));
		schema.add("word.noun", FieldTypeDef.keyword().stored(true).aggregatable(true).multiValued(true));
		schema.add("word.verb", FieldTypeDef.keyword().stored(true).aggregatable(true).multiValued(true));
		schema.add("word.adj",  FieldTypeDef.keyword().stored(true).aggregatable(true).multiValued(true));
		schema.add("word.adp",  FieldTypeDef.keyword().stored(true).aggregatable(true).multiValued(true));
		schema.add("word.aux",  FieldTypeDef.keyword().stored(true).aggregatable(true).multiValued(true));
		schema.add("word.sym",  FieldTypeDef.keyword().stored(true).aggregatable(true).multiValued(true));
		schema.add("word.propn",FieldTypeDef.keyword().stored(true).aggregatable(true).multiValued(true));
		schema.add("word.num",  FieldTypeDef.keyword().stored(true).aggregatable(true).multiValued(true));

		if (vectorDimension > 0) {
			schema.add("vector", FieldTypeDef.knnVector(vectorDimension));
		}

		return schema;
	}

	/**
	 * SearchRecord に形態素解析結果を付与します（エンリッチ処理）。
	 *
	 * <p>language が "ja" の場合のみ KuromojiAnnotator を実行します。
	 * word.* フィールドへ登録するのは NOUN / PROPN / VERB / ADJ のみ（テキストマイニング向け）。
	 * 全品詞は word.{pos} フィールドにも登録します。</p>
	 *
	 * @param record エンリッチ対象のレコード
	 */
	private void enrich(SearchRecord record) {
		if (!this.autoAnalyze) {
			return;
		}
		if (!"ja".equals(this.language)) {
			return;
		}
		if (record.getBody() == null || record.getBody().isEmpty()) {
			return;
		}
		try {
			nlp4j.Document doc = new DefaultDocument();
			doc.setText(record.getBody());

			KuromojiAnnotator annotator = new KuromojiAnnotator();
			annotator.setProperty("target", "text");
			annotator.annotate(doc);

			// 重複登録を防ぐために登録済みの (pos, lex) ペアを管理
			java.util.Set<String> registered = new java.util.HashSet<>();

			for (nlp4j.Keyword kw : doc.getKeywords()) {
				String upos = kw.getUPos();
				String lex = kw.getLex();
				String str = kw.getStr();
				int begin = kw.getBegin();
				int end = kw.getEnd();

				if (upos == null || lex == null) {
					continue;
				}

				// word.{pos} フィールドへ全品詞を登録
				String wordField = toWordField(upos);
				String wordKey = wordField + "\t" + lex;
				if (registered.add(wordKey)) {
					record.addKeyword(wordField, lex, str, begin, end);
				}

				// word フィールドへは NOUN / PROPN / VERB / ADJ のみ登録
				if (isContentWord(upos)) {
					String mainKey = "word\t" + lex;
					if (registered.add(mainKey)) {
						record.addKeyword("word", lex, str, begin, end);
					}
				}
			}
		} catch (Exception e) {
			throw new LocalSearchException("形態素解析に失敗しました: " + e.getMessage(), e);
		}
	}

	/**
	 * UPOS タグ名を word.* フィールド名に変換します。
	 *
	 * <p>例: "NOUN" → "word.noun"</p>
	 *
	 * @param upos KuromojiAnnotator が返す UPOS 文字列
	 * @return word.* フィールド名
	 */
	private String toWordField(String upos) {
		return "word." + upos.toLowerCase(Locale.ROOT);
	}

	/**
	 * テキストマイニング向けの主要品詞（内容語）かどうかを判定します。
	 *
	 * <p>NOUN / PROPN / VERB / ADJ を内容語として扱います。</p>
	 *
	 * @param upos UPOS 文字列
	 * @return 内容語なら true
	 */
	private boolean isContentWord(String upos) {
		return "NOUN".equals(upos)
				|| "PROPN".equals(upos)
				|| "VERB".equals(upos)
				|| "ADJ".equals(upos);
	}

	/**
	 * 簡易形式のリクエスト JSON（query, limit, filters）から OpenSearch 形式のリクエストを生成します。
	 *
	 * <p>入力 JSON 形式:</p>
	 * <pre>
	 * {
	 *   "query": "検索キーワード",   // 全文検索クエリ（省略時は match_all）
	 *   "limit": 10,                // 取得件数（省略時は 10）
	 *   "filters": {                // keyword フィールドの絞り込み条件（省略可）
	 *     "category": "技術",
	 *     "country": "Japan"
	 *   }
	 * }
	 * </pre>
	 *
	 * @param request 簡易形式のリクエスト JsonNode
	 * @return OpenSearch 形式のリクエスト JsonNode
	 */
	private JsonNode createSearchRequest(JsonNode request) {
		String query = request.get("query").asString("");
		int limit = request.get("limit").asInt(10);

		JsonNode filters = request.get("filters");
		boolean hasFilters = filters != null && !filters.isNull() && filters.size() > 0;

		if (!hasFilters) {
			return createTextSearchRequest(query, limit);
		}

		// bool クエリ: must（全文検索） + filter（keyword 絞り込み）
		JsonNode root = JsonNode.object();
		root.put("size", limit);

		JsonNode must = JsonNode.array();
		if (query == null || query.isEmpty()) {
			must.add(JsonNode.object().put("match_all", JsonNode.object()));
		} else {
			must.add(JsonNode.object().put(
					"match",
					JsonNode.object().put(this.default_field_name, query)));
		}

		JsonNode filter = JsonNode.array();
		for (String fieldName : filters.keys()) {
			String value = filters.get(fieldName).asString(null);
			if (value == null) {
				continue;
			}
			filter.add(JsonNode.object().put(
					"term",
					JsonNode.object().put(fieldName, value)));
		}

		JsonNode boolQuery = JsonNode.object();
		boolQuery.put("must", must);
		boolQuery.put("filter", filter);

		root.put("query", JsonNode.object().put("bool", boolQuery));

		return root;
	}

	private JsonNode createTextSearchRequest(String query, int limit) {
		return createTextSearchRequest(this.default_field_name, query, limit);
	}

	private JsonNode createTextSearchRequest(String field, String query, int limit) {
		JsonNode request = JsonNode.object();

		// keyword フィールドは term クエリ（完全一致）、text フィールドは match クエリ（全文検索）
		boolean isKeyword = schema.contains(field)
				&& schema.get(field).kind() == FieldTypeDef.Kind.KEYWORD;

		JsonNode innerQuery;
		if (isKeyword) {
			innerQuery = JsonNode.object();
			innerQuery.put("term", JsonNode.object().put(field, query));
		} else {
			innerQuery = JsonNode.object();
			innerQuery.put("match", JsonNode.object().put(field, query));
		}

		request.put("query", innerQuery);
		request.put("size", limit);

		return request;
	}

	private JsonNode createVectorSearchRequest(float[] vector, int limit) {
		return createVectorSearchRequest(vector, limit, null);
	}

	/**
	 * フィルター付きベクトル検索リクエストを生成します。
	 *
	 * @param vector  クエリベクトル
	 * @param limit   取得件数の上限
	 * @param filters keyword フィールドの絞り込み条件（null または空の場合はフィルターなし）
	 * @return KNN 検索リクエスト JsonNode
	 */
	private JsonNode createVectorSearchRequest(float[] vector, int limit,
			java.util.Map<String, String> filters) {
		JsonNode request = JsonNode.object();
		request.put("size", limit);

		JsonNode knn = JsonNode.object();
		knn.put("field", "vector");
		knn.put("query_vector", vector);
		knn.put("k", limit);

		if (filters != null && !filters.isEmpty()) {
			JsonNode filterQueries = JsonNode.array();

			for (java.util.Map.Entry<String, String> entry : filters.entrySet()) {
				filterQueries.add(
						JsonNode.object().put(
								"term",
								JsonNode.object().put(entry.getKey(), entry.getValue())));
			}

			JsonNode boolFilter = JsonNode.object();
			boolFilter.put("must", filterQueries);

			knn.put("filter", JsonNode.object().put("bool", boolFilter));
		}

		request.put("knn", knn);

		return request;
	}

	private SearchResult[] executeSearch(JsonNode request) {
		try {
			JsonNode response = api.search("myindex/_search", request);
			return toSearchResults(response);
		} catch (IOException e) {
			throw new LocalSearchException(e.getMessage(), e);
		}
	}

	private void initIndex() {
		try {
			index = new LuceneIndex();
			api = new LuceneLocalSearchApi(index);
		} catch (IOException e) {
			throw new LocalSearchException(e.getMessage(), e);
		}
	}

	private void initIndex(Path indexDir) {
		try {
			index = new LuceneIndex(indexDir);
			api = new LuceneLocalSearchApi(index);
		} catch (IOException e) {
			throw new LocalSearchException(e.getMessage(), e);
		}
	}

	private String resolveDefaultFieldName(String language) {
		if ("ja".equals(language)) {
			return "text_ja";
		} else if ("en".equals(language)) {
			return "text_en";
		} else {
			return "text";
		}
	}

	/**
	 * 指定フィールドに対してテキスト検索を行います。
	 * addJson() で追加した category, country, source などの追加フィールドを対象に検索できます。
	 *
	 * @param field the field name to search
	 * @param query the search query string
	 * @param limit the maximum number of results to return
	 * @return an array of SearchResult objects, ordered by relevance score
	 * @throws LocalSearchException if search fails
	 */
	public SearchResult[] search(String field, String query, int limit) {
		return executeSearch(createTextSearchRequest(field, query, limit));
	}

	/**
	 * フィールドの値ごとのドキュメント件数を集計します（terms aggregation）。
	 *
	 * <p>入力 JSON 形式:</p>
	 * <pre>
	 * // フィールド全体を集計
	 * {"field":"category","size":10}
	 *
	 * // 全文検索で絞り込んだ上で集計
	 * {"field":"category","query":"東京","size":10}
	 *
	 * // 全文検索 + filters（keyword 完全一致）で絞り込んだ上で集計
	 * {"field":"category","query":"東京","size":10,"filters":{"country":"Japan","source":"news"}}
	 * </pre>
	 *
	 * <p>戻り値 JSON 形式:</p>
	 * <pre>
	 * {
	 *   "field": "category",
	 *   "buckets": [
	 *     {"key": "technology", "count": 1}
	 *   ]
	 * }
	 * </pre>
	 *
	 * @param requestJson 集計リクエスト JSON 文字列（field, query?, size?, filters?）
	 * @return 集計結果の独自形式 JSON 文字列
	 * @throws LocalSearchException if JSON parsing or aggregation fails
	 */
	public String aggregateJson_bak(String requestJson) {
		try {
			JsonNode request = JsonNode.parse(requestJson);

			String field = request.get("field").asString();
			String query = request.get("query").asString(null);
			int size = request.get("size").asInt(10);
			JsonNode filters = request.get("filters");

			JsonNode searchRequest = createAggregationRequest(field, query, size, filters);

			JsonNode response = api.search("myindex/_search", searchRequest);

			return toAggregationResult(field, response).toJson();

		} catch (Throwable th) {
			throw new LocalSearchException(th.getMessage(), th);
		}
	}

	/**
	 * aggregateJson() 用の OpenSearch 形式リクエストを生成します。
	 * query / filters が両方ない場合は match_all と等価（query 節なし）になります。
	 *
	 * @param field   集計対象フィールド名
	 * @param query   全文検索クエリ（null または空文字の場合はスキップ）
	 * @param size    返すバケット数の上限
	 * @param filters keyword フィールドの絞り込み条件（null または空の場合はスキップ）
	 * @return OpenSearch 形式のリクエスト JsonNode
	 */
	private JsonNode createAggregationRequest(String field, String query, int size, JsonNode filters) {
		JsonNode root = JsonNode.object();

		// ヒット本文は不要なので size=0
		root.put("size", 0);

		boolean hasQuery = query != null && !query.isEmpty();
		boolean hasFilters = filters != null && !filters.isNull() && filters.size() > 0;

		if (hasQuery || hasFilters) {
			JsonNode boolQuery = JsonNode.object();

			if (hasQuery) {
				JsonNode must = JsonNode.array();
				must.add(JsonNode.object().put(
						"match", JsonNode.object().put(this.default_field_name, query)));
				boolQuery.put("must", must);
			}

			if (hasFilters) {
				JsonNode filter = JsonNode.array();
				for (String fieldName : filters.keys()) {
					String value = filters.get(fieldName).asString(null);
					if (value == null) {
						continue;
					}
					filter.add(JsonNode.object().put(
							"term", JsonNode.object().put(fieldName, value)));
				}
				boolQuery.put("filter", filter);
			}

			root.put("query", JsonNode.object().put("bool", boolQuery));
		}

		JsonNode terms = JsonNode.object();
		terms.put("field", field);
		terms.put("size", size);

		JsonNode aggs = JsonNode.object();
		aggs.put("values", JsonNode.object().put("terms", terms));

		root.put("aggs", aggs);

		return root;
	}

	/**
	 * OpenSearch 形式の aggregation レスポンスを独自形式に変換します。
	 *
	 * <pre>
	 * {"field":"category","buckets":[{"key":"観光","count":5},{"key":"技術","count":3}]}
	 * </pre>
	 *
	 * @param field    集計対象フィールド名
	 * @param response api.search() からのレスポンス
	 * @return 独自形式の JsonNode
	 */
	private JsonNode toAggregationResult(String field, JsonNode response) {
		JsonNode result = JsonNode.object();
		result.put("field", field);

		JsonNode buckets = JsonNode.array();

		JsonNode rawBuckets = response.get("aggregations").get("values").get("buckets");
		for (JsonNode raw : rawBuckets.asList()) {
			JsonNode bucket = JsonNode.object();
			bucket.put("key", raw.get("key").asString());
			bucket.put("count", (Number) raw.get("doc_count").asLong(0));
			buckets.add(bucket);
		}

		result.put("buckets", buckets);

		return result;
	}

	public void saveIndexTo(Path dir) throws IOException {
		if (index != null) {
			this.index.writeToAndClose(dir);
		}
	}

	public void saveIndexTo(File dir) throws IOException {
		saveIndexTo(dir.toPath());
	}

	public SearchResult[] search(float[] vector, int limit) {
		return executeSearch(createVectorSearchRequest(vector, limit));
	}

	/**
	 * フィールドフィルター付きベクトル検索を行います。
	 * フィルター対象フィールドを持つ文書は {@link #add(String, float[], java.util.Map)} で
	 * 登録してください。
	 *
	 * <p>例:</p>
	 * <pre>
	 * SearchResult[] results = search.search(
	 *     new float[] { 0.9f, 0.1f }, 10,
	 *     java.util.Map.of("category", "technology", "country", "Japan")
	 * );
	 * </pre>
	 *
	 * @param vector  クエリベクトル
	 * @param limit   取得件数の上限
	 * @param filters keyword フィールドの絞り込み条件（フィールド名 → 値）
	 * @return an array of SearchResult objects, ordered by similarity score
	 * @throws LocalSearchException if search fails
	 */
	public SearchResult[] search(float[] vector, int limit, java.util.Map<String, String> filters) {
		return executeSearch(createVectorSearchRequest(vector, limit, filters));
	}

	/**
	 * Performs a text search on the indexed documents.
	 *
	 * @param query the search query string
	 * @param limit the maximum number of results to return
	 * @return an array of SearchResult objects, ordered by relevance score
	 * @throws LocalSearchException if search fails
	 */
	public SearchResult[] search(String query, int limit) {
		return executeSearch(createTextSearchRequest(query, limit));
	}

	/**
	 * 全文検索＋フィールド絞り込みを行います（Java 利用者向けオーバーロード）。
	 *
	 * <p>例:</p>
	 * <pre>
	 * SearchResult[] results = search.search(
	 *     "Kyoto", 10,
	 *     java.util.Map.of("category", "company")
	 * );
	 * </pre>
	 *
	 * @param query   全文検索クエリ（空文字列の場合は match_all）
	 * @param limit   取得件数の上限
	 * @param filters keyword フィールドの絞り込み条件（フィールド名 → 値）
	 * @return an array of SearchResult objects, ordered by relevance score
	 * @throws LocalSearchException if search fails
	 */
	public SearchResult[] search(String query, int limit, java.util.Map<String, String> filters) {
		JsonNode request = JsonNode.object();
		request.put("query", query);
		request.put("limit", limit);

		JsonNode filterNode = JsonNode.object();
		for (java.util.Map.Entry<String, String> entry : filters.entrySet()) {
			filterNode.put(entry.getKey(), entry.getValue());
		}
		request.put("filters", filterNode);

		return executeSearch(createSearchRequest(request));
	}

	/**
	 * JSON 文字列で検索条件を指定して検索を実行します。Python (JPype) など外部から
	 * 複雑な検索条件を渡す場合に使用します。
	 *
	 * <p>OpenSearch 形式の JSON をそのまま渡せます。</p>
	 *
	 * <pre>
	 * // term クエリ（完全一致）
	 * search.searchJson("{\"query\":{\"term\":{\"category\":\"技術\"}},\"size\":10}")
	 *
	 * // match クエリ（全文検索）
	 * search.searchJson("{\"query\":{\"match\":{\"text_ja\":\"東京\"}},\"size\":5}")
	 *
	 * // bool クエリ（must + filter）
	 * search.searchJson("{\"query\":{\"bool\":{\"must\":[{\"match\":{\"text_ja\":\"東京\"}}],\"filter\":[{\"term\":{\"category\":\"技術\"}}]}},\"size\":10}")
	 * </pre>
	 *
	 * @param requestJson OpenSearch 形式の検索リクエスト JSON 文字列
	 * @return an array of SearchResult objects, ordered by relevance score
	 * @throws LocalSearchException if JSON parsing or search fails
	 */
	public SearchResult[] searchJson(String requestJson) {
		try {
			JsonNode request = JsonNode.parse(requestJson);
			return executeSearch(request);
		} catch (Throwable th) {
			throw new LocalSearchException(th.getMessage(), th);
		}
	}

	/**
	 * OpenSearch Query DSL 形式のリクエストを実行し、OpenSearch 形式のレスポンス JSON をそのまま返します。
	 *
	 * <p>
	 * {@link #searchJson(String)} は結果を {@link SearchResult}[] に変換するため、
	 * レスポンスに含まれる {@code aggregations} などの情報が失われます。
	 * このメソッドはレスポンス全体を JSON 文字列として返すため、
	 * aggregations や hits のメタ情報も含めて取得できます。
	 * </p>
	 *
	 * <pre>
	 * // hits + aggregations を同時に取得する例
	 * String response = search.searchResponseJson("""
	 *     {
	 *       "size": 10,
	 *       "query": {"match": {"text_en": "Kyoto"}},
	 *       "aggs": {
	 *         "values": {"terms": {"field": "category", "size": 10}}
	 *       }
	 *     }
	 *     """);
	 * </pre>
	 *
	 * @param requestJson OpenSearch Query DSL 形式の検索リクエスト JSON 文字列
	 * @return OpenSearch 形式のレスポンス JSON 文字列
	 * @throws LocalSearchException if JSON parsing or search fails
	 */
	public String searchResponseJson(String requestJson) {
		try {
			JsonNode request = JsonNode.parse(requestJson);
			JsonNode response = api.search("myindex/_search", request);
			return response.toJson();
		} catch (Throwable th) {
			throw new LocalSearchException(th.getMessage(), th);
		}
	}

	/**
	 * 簡易形式の JSON 文字列で全文検索＋フィールド絞り込みを実行します。
	 * Python (JPype) など外部から絞り込み条件を渡す場合に便利です。
	 *
	 * <p>入力 JSON 形式（searchJson の OpenSearch 形式とは異なる簡易形式です）:</p>
	 * <pre>
	 * // 全文検索のみ
	 * search.searchByQuery("{\"query\":\"東京\",\"limit\":10}")
	 *
	 * // 全文検索 + filters による keyword 絞り込み
	 * search.searchByQuery("{\"query\":\"東京\",\"limit\":5,\"filters\":{\"category\":\"技術\"}}")
	 *
	 * // filters のみ（query 省略 → match_all）
	 * search.searchByQuery("{\"filters\":{\"category\":\"観光\"},\"limit\":10}")
	 * </pre>
	 *
	 * @param requestJson 簡易形式の検索リクエスト JSON 文字列
	 * @return an array of SearchResult objects, ordered by relevance score
	 * @throws LocalSearchException if JSON parsing or search fails
	 */
	public SearchResult[] searchByQuery(String requestJson) {
		try {
			JsonNode request = JsonNode.parse(requestJson);
			return executeSearch(createSearchRequest(request));
		} catch (Throwable th) {
			throw new LocalSearchException(th.getMessage(), th);
		}
	}

	private SearchResult[] toSearchResults(JsonNode response) {
		JsonNode hits = response.get("hits").get("hits");

		int size = hits.size();

		if (size < 1) {
			return new SearchResult[0];
		}

		SearchResult[] results = new SearchResult[size];

		for (int n = 0; n < size; n++) {
			JsonNode hit = hits.get(n);
			JsonNode source = hit.get("_source");

			SearchResult result = new SearchResult();
			result.score = (float) hit.get("_score").asDouble(-1);
			result.id = source.get("id").asString();

			JsonNode textNode = source.get(default_field_name);
			result.body = (textNode != null) ? textNode.asString() : null;

			JsonNode dataNode = source.get("data");
			result.data = (dataNode != null) ? dataNode.asString() : null;

			results[n] = result;
		}

		return results;
	}
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 簡易形式のaggregationリクエストを実行し、
	 * OpenSearch互換形式のaggregationレスポンスを返します。
	 *
	 * <p>入力例:</p>
	 * <pre>
	 * {
	 *   "name": "categories",
	 *   "field": "category",
	 *   "size": 10,
	 *   "query": "東京",
	 *   "filters": {
	 *     "country": "Japan"
	 *   }
	 * }
	 * </pre>
	 *
	 * <p>出力例:</p>
	 * <pre>
	 * {
	 *   "aggregations": {
	 *     "categories": {
	 *       "buckets": [
	 *         {
	 *           "key": "観光",
	 *           "doc_count": 5
	 *         }
	 *       ]
	 *     }
	 *   }
	 * }
	 * </pre>
	 *
	 * @param requestJson 簡易形式のaggregationリクエスト
	 * @return OpenSearch互換形式のaggregationレスポンス
	 */
	public String aggregateJson(String requestJson) {
	    try {
	        JsonNode request = JsonNode.parse(requestJson);

	        String field = getRequiredString(
	                request,
	                "field"
	        );

	        String aggregationName = getOptionalString(
	                request,
	                "name",
	                "values"
	        );

	        String query = getOptionalString(
	                request,
	                "query",
	                null
	        );

	        int size = getOptionalInt(
	                request,
	                "size",
	                10
	        );

	        if (size < 1) {
	            throw new IllegalArgumentException(
	                    "size must be greater than 0"
	            );
	        }

	        JsonNode filters = request.get("filters");

	        JsonNode searchRequest =
	                createAggregationRequest(
	                        aggregationName,
	                        field,
	                        query,
	                        size,
	                        filters
	                );

	        /*
	         * ここで返るのはLuceneLocalSearchApiの内部レスポンス。
	         * 公開形式としてそのまま返さない。
	         */
	        JsonNode luceneResponse =
	                api.search(
	                        "myindex/_search",
	                        searchRequest
	                );

	        JsonNode openSearchResponse =
	                toOpenSearchAggregationResponse(
	                        aggregationName,
	                        luceneResponse
	                );

	        return openSearchResponse.toJson();

	    } catch (Exception e) {
	        throw new LocalSearchException(
	                e.getMessage(),
	                e
	        );
	    }
	}
	
	
	private JsonNode createAggregationRequest(
	        String aggregationName,
	        String field,
	        String query,
	        int size,
	        JsonNode filters) {

	    JsonNode root = JsonNode.object();

	    // 検索ヒット本文は不要
	    root.put("size", 0);

	    boolean hasQuery =
	            query != null && !query.isEmpty();

	    boolean hasFilters =
	            filters != null
	            && !filters.isNull()
	            && filters.size() > 0;

	    if (hasQuery || hasFilters) {
	        JsonNode boolQuery = JsonNode.object();

	        if (hasQuery) {
	            JsonNode must = JsonNode.array();

	            must.add(
	                    JsonNode.object().put(
	                            "match",
	                            JsonNode.object().put(
	                                    this.default_field_name,
	                                    query
	                            )
	                    )
	            );

	            boolQuery.put("must", must);
	        }

	        if (hasFilters) {
	            JsonNode filter = JsonNode.array();

	            for (String fieldName : filters.keys()) {
	                JsonNode valueNode =
	                        filters.get(fieldName);

	                if (valueNode == null
	                        || valueNode.isNull()) {
	                    continue;
	                }

	                String value =
	                        valueNode.asString(null);

	                if (value == null) {
	                    continue;
	                }

	                filter.add(
	                        JsonNode.object().put(
	                                "term",
	                                JsonNode.object().put(
	                                        fieldName,
	                                        value
	                                )
	                        )
	                );
	            }

	            if (filter.size() > 0) {
	                boolQuery.put("filter", filter);
	            }
	        }

	        root.put(
	                "query",
	                JsonNode.object().put(
	                        "bool",
	                        boolQuery
	                )
	        );
	    }

	    JsonNode terms = JsonNode.object();
	    terms.put("field", field);
	    terms.put("size", size);

	    JsonNode aggregation = JsonNode.object();
	    aggregation.put("terms", terms);

	    JsonNode aggregations = JsonNode.object();
	    aggregations.put(
	            aggregationName,
	            aggregation
	    );

	    root.put("aggs", aggregations);

	    return root;
	}
	
	
	/**
	 * LuceneLocalSearchApiのaggregation結果を、
	 * OpenSearch互換形式に変換します。
	 *
	 * @param aggregationName aggregation名
	 * @param luceneResponse LuceneLocalSearchApiのレスポンス
	 * @return OpenSearch互換形式のレスポンス
	 */
	private JsonNode toOpenSearchAggregationResponse(
	        String aggregationName,
	        JsonNode luceneResponse) {

	    JsonNode rawAggregations =
	            requireNode(
	                    luceneResponse,
	                    "aggregations",
	                    "Lucene response"
	            );

	    JsonNode rawAggregation =
	            requireNode(
	                    rawAggregations,
	                    aggregationName,
	                    "aggregations"
	            );

	    JsonNode rawBuckets =
	            requireNode(
	                    rawAggregation,
	                    "buckets",
	                    "aggregation '" + aggregationName + "'"
	            );

	    JsonNode openSearchBuckets =
	            JsonNode.array();

	    for (JsonNode rawBucket :
	            rawBuckets.asList()) {

	        JsonNode rawKey =
	                requireNode(
	                        rawBucket,
	                        "key",
	                        "aggregation bucket"
	                );

	        JsonNode rawDocCount =
	                rawBucket.get("doc_count");

	        /*
	         * Lucene側で count という名前の場合にも
	         * 対応できるようにする。
	         */
	        if (rawDocCount == null
	                || rawDocCount.isNull()) {
	            rawDocCount =
	                    rawBucket.get("count");
	        }

	        if (rawDocCount == null
	                || rawDocCount.isNull()) {
	            throw new IllegalStateException(
	                    "Aggregation bucket does not "
	                    + "contain doc_count or count"
	            );
	        }

	        JsonNode bucket = JsonNode.object();

	        /*
	         * keyを文字列化せず、そのJSON型を維持する。
	         * keywordでは文字列、将来の数値集計では数値になる。
	         */
	        bucket.put("key", rawKey);

	        JsonNode keyAsString =
	                rawBucket.get("key_as_string");

	        if (keyAsString != null
	                && !keyAsString.isNull()) {
	            bucket.put(
	                    "key_as_string",
	                    keyAsString
	            );
	        }

	        bucket.put(
	                "doc_count",
	                (Number) rawDocCount.asLong(0)
	        );

	        openSearchBuckets.add(bucket);
	    }

	    JsonNode openSearchAggregation =
	            JsonNode.object();

	    copyIfPresent(
	            rawAggregation,
	            openSearchAggregation,
	            "doc_count_error_upper_bound"
	    );

	    copyIfPresent(
	            rawAggregation,
	            openSearchAggregation,
	            "sum_other_doc_count"
	    );

	    openSearchAggregation.put(
	            "buckets",
	            openSearchBuckets
	    );

	    JsonNode openSearchAggregations =
	            JsonNode.object();

	    openSearchAggregations.put(
	            aggregationName,
	            openSearchAggregation
	    );

	    JsonNode result = JsonNode.object();

	    result.put(
	            "aggregations",
	            openSearchAggregations
	    );

	    return result;
	}
	
	
	/**
	 * @param parent
	 * @param fieldName
	 * @param context
	 * @return
	 */
	private JsonNode requireNode(
	        JsonNode parent,
	        String fieldName,
	        String context) {

	    if (parent == null || parent.isNull()) {
	        throw new IllegalStateException(
	                context + " is null"
	        );
	    }

	    JsonNode value =
	            parent.get(fieldName);

	    if (value == null || value.isNull()) {
	        throw new IllegalStateException(
	                context
	                + " does not contain '"
	                + fieldName
	                + "'"
	        );
	    }

	    return value;
	}

	private void copyIfPresent(
	        JsonNode source,
	        JsonNode target,
	        String fieldName) {

	    JsonNode value = source.get(fieldName);

	    if (value != null && !value.isNull()) {
	        target.put(fieldName, value);
	    }
	}

	private String getRequiredString(
	        JsonNode object,
	        String fieldName) {

	    JsonNode value = object.get(fieldName);

	    if (value == null || value.isNull()) {
	        throw new IllegalArgumentException(
	                "Required field is missing: "
	                + fieldName
	        );
	    }

	    String result = value.asString(null);

	    if (result == null || result.isBlank()) {
	        throw new IllegalArgumentException(
	                "Field must not be empty: "
	                + fieldName
	        );
	    }

	    return result;
	}

	private String getOptionalString(
	        JsonNode object,
	        String fieldName,
	        String defaultValue) {

	    JsonNode value = object.get(fieldName);

	    if (value == null || value.isNull()) {
	        return defaultValue;
	    }

	    return value.asString(defaultValue);
	}

	private int getOptionalInt(
	        JsonNode object,
	        String fieldName,
	        int defaultValue) {

	    JsonNode value = object.get(fieldName);

	    if (value == null || value.isNull()) {
	        return defaultValue;
	    }

	    return value.asInt(defaultValue);
	}

	@Override
	public String toString() {
		return "LocalSearch [language=" + language + ", autoAnalyze=" + autoAnalyze
				+ ", default_field_name=" + default_field_name + ", schema=" + schema
				+ ", index=" + index + "]";
	}
}
