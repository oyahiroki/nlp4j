/*
 * Copyright (C) 2026 Hiroki OYA
 *
 * Licensed under the Apache License, Version 2.0
 */
package nlp4j.lucene;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.document.Document;

import nlp4j.KeywordBuilder;
import nlp4j.impl.DefaultDocument;
import nlp4j.json.JsonNode;
import nlp4j.krmj.annotator.KuromojiAnnotator;
import nlp4j.lucene9.FieldTypeDef;
import nlp4j.lucene9.LuceneIndex;
import nlp4j.lucene9.LuceneLocalSearchApi;
import nlp4j.lucene9.SearchSchema;
import nlp4j.util.StringUtils;

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
 * try (LocalSearch search = LocalSearch.builder("ja").autoAnalyze(false).build()) {
 * 	// ...
 * }
 *
 * // ディスクインデックス + ベクトル検索
 * try (LocalSearch search = LocalSearch.builder("ja").vectorDimension(1024).indexDirectory(Path.of("./index"))
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
	 * LocalSearch search = LocalSearch.builder("ja").autoAnalyze(true).vectorDimension(1024)
	 * 		.indexDirectory(Path.of("./index")).build();
	 * </pre>
	 */
	public static class Builder {

		private final String language;
		private boolean autoAnalyze = true;
		private int vectorDimension = 0;
		private Path indexDir = null;

		private SearchRecordEnricher enricher;

		private final java.util.Map<String, FieldTypeDef> fields = new java.util.LinkedHashMap<>();

		private Builder(String language) {
			this.language = language;
		}

		/**
		 * KuromojiAnnotator による形態素解析（自動エンリッチ）を有効／無効にします。 デフォルトは
		 * {@code true}（有効）。language が "ja" 以外の場合は この設定によらず解析は実行されません。
		 *
		 * @param autoAnalyze true で自動解析を有効化
		 * @return this Builder
		 */
		public Builder autoAnalyze(boolean autoAnalyze) {
			this.autoAnalyze = autoAnalyze;
			return this;
		}

		/**
		 * KNN ベクトル検索に使用するベクトルの次元数を設定します。 0（デフォルト）の場合はベクトルフィールドを作成しません。
		 *
		 * @param vectorDimension ベクトルの次元数（0 以上）
		 * @return this Builder
		 */
		public Builder vectorDimension(int vectorDimension) {
			this.vectorDimension = vectorDimension;
			return this;
		}

		/**
		 * ディスク上の Lucene インデックスディレクトリを指定します。 指定しない場合はオンメモリインデックスを使用します。
		 *
		 * @param indexDir インデックスを格納するディレクトリパス
		 * @return this Builder
		 */
		public Builder indexDirectory(Path indexDir) {
			this.indexDir = indexDir;
			return this;
		}

		public Builder enricher(SearchRecordEnricher enricher) {
			this.enricher = enricher;
			return this;
		}

		/**
		 * 明示フィールド定義を追加します。
		 * suffix パターンより優先されます。
		 *
		 * @param fieldName    フィールド名
		 * @param fieldTypeDef フィールド型定義
		 * @return this Builder
		 */
		public Builder field(String fieldName, FieldTypeDef fieldTypeDef) {
			fields.put(fieldName, fieldTypeDef);
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
	private SearchRecordEnricher enricher;

	private String default_field_name;
	SearchSchema schema;
	LuceneIndex index;

	LuceneLocalSearchApi api;

	private final nlp4j.lucene9.DynamicFieldResolver dynamicFieldResolver = new nlp4j.lucene9.DynamicFieldResolver();

	// -----------------------------------------------------------------------
	// Constructors
	// -----------------------------------------------------------------------

	/**
	 * Builder から LocalSearch を生成するプライベートコンストラクタ。 すべての公開コンストラクタはここに委譲します。
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

		this.schema = createSchema(builder);
		this.api = new LuceneLocalSearchApi(index, this.schema);
		this.default_field_name = resolveDefaultFieldName(builder.language);

		if (builder.enricher != null) {
			this.enricher = builder.enricher;
		} else {
			this.enricher = SearchRecordEnrichers.forLanguage(builder.language);
		}

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
	 * <p>
	 * 例:
	 * </p>
	 * 
	 * <pre>
	 * search.add("1", new float[] { 1.0f, 0.0f }, java.util.Map.of("category", "technology", "country", "Japan"));
	 * </pre>
	 *
	 * @param id     ドキュメントの一意識別子
	 * @param vector ベクトル
	 * @param fields keyword フィールドの追加値（フィールド名 → 値）
	 * @throws LocalSearchException if adding the document fails
	 */
	public void add(String id, float[] vector, java.util.Map<String, String> fields) {
		try {
			var builder = schema.document().put("id", id).putVector("vector", vector);

			if (fields != null) {
				for (java.util.Map.Entry<String, String> entry : fields.entrySet()) {
					String fieldName = entry.getKey();
					String value = entry.getValue();
					if (fieldName == null || value == null) {
						continue;
					}
					ensureField(fieldName, false);
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
	 * <p>
	 * language に対応する SearchRecordEnricher により 言語固有のテキスト解析を自動実行します。
	 * </p>
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
	 * <p>
	 * language が "ja" の場合は KuromojiAnnotator による形態素解析を自動実行し、 word.*
	 * フィールドに原形（見出し語）を登録します。
	 * </p>
	 *
	 * @param record 登録するドキュメントレコード
	 * @throws LocalSearchException if adding the document fails
	 */
	public void add(SearchRecord record) {
		try {
			enrich(record);

			var builder = schema.document().put("id", record.getId()).put(default_field_name, record.getBody());

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
	 * "body" or "text" field. Additional fields are indexed as keyword fields. JSON
	 * array values are indexed as multi-valued keyword fields.
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
				if ("id".equals(fieldName) || "body".equals(fieldName) || "text".equals(fieldName)) {
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

			var builder = schema.document().put("id", record.getId()).put(default_field_name, record.getBody())
					.put("data", json_string);

			// word.* フィールドへキーワードを登録
			for (SearchKeyword kw : record.getKeywords()) {
				builder.put(kw.getPos(), kw.getLex());
			}

			// 追加フィールドを登録
			for (String fieldName : record.dataKeys()) {
				List<String> values = record.getDataValues(fieldName);
				ensureField(fieldName, values.size() > 1);
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
	 * "body" フィールドを優先し、なければ "text" フィールドを返します。 どちらも存在しない場合は
	 * IllegalArgumentException をスローします。
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
	 * 指定フィールドが未登録の場合、DynamicFieldResolver で型を解決してスキーマに登録します。
	 * 明示 schema 済みのフィールドは変更しません。
	 *
	 * @param fieldName   フィールド名
	 * @param multiValued 複数値フィールドの場合 true
	 */
	private void ensureField(String fieldName, boolean multiValued) {
		if (schema.contains(fieldName)) {
			return;
		}
		nlp4j.lucene9.FieldTypeDef type = dynamicFieldResolver.resolve(fieldName);
		if (multiValued) {
			type = type.multiValued(true);
		}
		schema.addIfAbsent(fieldName, type);
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


	private static final Set<String> DEFAULT_WORD_FIELDS = Set.of("word", "word.noun", "word.verb", "word.adj",
			"word.adp", "word.aux", "word.sym", "word.propn", "word.num", "word.adv");

	private static void addDefaultWordFields(SearchSchema schema) {
		for (String fieldName : DEFAULT_WORD_FIELDS) {
			schema.add(fieldName, FieldTypeDef.keyword().stored(true).aggregatable(true).multiValued(true));
		}
	}

	private SearchSchema createSchema(Builder builder) {
		SearchSchema schema = new SearchSchema();

		schema.add("id", FieldTypeDef.keyword().stored(true));
		schema.add("text", FieldTypeDef.text().stored(true));
		schema.add("text_en", FieldTypeDef.text().stored(true));
		schema.add("text_ja", FieldTypeDef.text().stored(true));
		schema.add("data", FieldTypeDef.storedOnly());

		// 形態素解析結果の word.* フィールド（multiValued keyword）
		addDefaultWordFields(schema);

		if (builder.vectorDimension > 0) {
			schema.add("vector", FieldTypeDef.knnVector(builder.vectorDimension));
		}

		// 明示フィールド定義（suffix patternより優先）
		for (java.util.Map.Entry<String, FieldTypeDef> entry : builder.fields.entrySet()) {
			schema.add(entry.getKey(), entry.getValue());
		}

		return schema;
	}

	/**
	 * SearchRecord に形態素解析結果を付与します（エンリッチ処理）。
	 *
	 * <p>
	 * language が "ja" の場合のみ KuromojiAnnotator を実行します。 word.* フィールドへ登録するのは NOUN /
	 * PROPN / VERB / ADJ のみ（テキストマイニング向け）。 全品詞は word.{pos} フィールドにも登録します。
	 * </p>
	 *
	 * @param record エンリッチ対象のレコード
	 */
	private void enrich(SearchRecord record) {

		if (!this.autoAnalyze) {
			return;
		}

		if (record == null || record.getBody() == null || record.getBody().isEmpty()) {
			return;
		}

		try {
			enricher.enrich(record);
		} catch (Exception e) {
			throw new LocalSearchException("Text enrichment failed: " + e.getMessage(), e);
		}

	}

	/**
	 * 簡易形式のリクエスト JSON（query, limit, filters）から OpenSearch 形式のリクエストを生成します。
	 *
	 * <p>
	 * 入力 JSON 形式:
	 * </p>
	 * 
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
			must.add(JsonNode.object().put("match", JsonNode.object().put(this.default_field_name, query)));
		}

		JsonNode filter = JsonNode.array();
		for (String fieldName : filters.keys()) {
			String value = filters.get(fieldName).asString(null);
			if (value == null) {
				continue;
			}
			filter.add(JsonNode.object().put("term", JsonNode.object().put(fieldName, value)));
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
		boolean isKeyword = schema.contains(field) && schema.get(field).kind() == FieldTypeDef.Kind.KEYWORD;

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
	private JsonNode createVectorSearchRequest(float[] vector, int limit, java.util.Map<String, String> filters) {
		JsonNode request = JsonNode.object();
		request.put("size", limit);

		JsonNode knn = JsonNode.object();
		knn.put("field", "vector");
		knn.put("query_vector", vector);
		knn.put("k", limit);

		if (filters != null && !filters.isEmpty()) {
			JsonNode filterQueries = JsonNode.array();

			for (java.util.Map.Entry<String, String> entry : filters.entrySet()) {
				filterQueries
						.add(JsonNode.object().put("term", JsonNode.object().put(entry.getKey(), entry.getValue())));
			}

			JsonNode boolFilter = JsonNode.object();
			boolFilter.put("must", filterQueries);

			knn.put("filter", JsonNode.object().put("bool", boolFilter));
		}

		request.put("knn", knn);

		return request;
	}

	private JsonNode executeRequest(JsonNode request) {
		try {
			return api.search("myindex/_search", request);
		} catch (IOException e) {
			throw new LocalSearchException(e.getMessage(), e);
		}
	}

	private SearchResult[] executeSearch(JsonNode request) {
		return toSearchResults(executeRequest(request));
	}

	private void initIndex() {
		try {
			index = new LuceneIndex();
			// api は schema 生成後に再設定される（reinitApi()）
		} catch (IOException e) {
			throw new LocalSearchException(e.getMessage(), e);
		}
	}

	private void initIndex(Path indexDir) {
		try {
			index = new LuceneIndex(indexDir);
			// api は schema 生成後に再設定される（reinitApi()）
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
	 * 指定フィールドに対してテキスト検索を行います。 addJson() で追加した category, country, source
	 * などの追加フィールドを対象に検索できます。
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
	 * フィールドフィルター付きベクトル検索を行います。 フィルター対象フィールドを持つ文書は
	 * {@link #add(String, float[], java.util.Map)} で 登録してください。
	 *
	 * <p>
	 * 例:
	 * </p>
	 * 
	 * <pre>
	 * SearchResult[] results = search.search(new float[] { 0.9f, 0.1f }, 10,
	 * 		java.util.Map.of("category", "technology", "country", "Japan"));
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
	 * <p>
	 * 例:
	 * </p>
	 * 
	 * <pre>
	 * SearchResult[] results = search.search("Kyoto", 10, java.util.Map.of("category", "company"));
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
	 * JSON 文字列で検索条件を指定して検索を実行します。Python (JPype) など外部から 複雑な検索条件を渡す場合に使用します。
	 *
	 * <p>
	 * OpenSearch 形式の JSON をそのまま渡せます。
	 * </p>
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
	 * {@link #searchJson(String)} は結果を {@link SearchResult}[] に変換するため、 レスポンスに含まれる
	 * {@code aggregations} などの情報が失われます。 このメソッドはレスポンス全体を JSON 文字列として返すため、
	 * aggregations や hits のメタ情報も含めて取得できます。
	 * </p>
	 *
	 * <pre>
	 * // hits + aggregations を同時に取得する例
	 * String response = search.searchResponseJson("""
	 * 		{
	 * 		  "size": 10,
	 * 		  "query": {"match": {"text_en": "Kyoto"}},
	 * 		  "aggs": {
	 * 		    "values": {"terms": {"field": "category", "size": 10}}
	 * 		  }
	 * 		}
	 * 		""");
	 * </pre>
	 *
	 * @param requestJson OpenSearch Query DSL 形式の検索リクエスト JSON 文字列
	 * @return OpenSearch 形式のレスポンス JSON 文字列
	 * @throws LocalSearchException if JSON parsing or search fails
	 */
	public String searchResponseJson(String requestJson) {
		try {
			JsonNode request = JsonNode.parse(requestJson);
			JsonNode response = executeRequest(request);
			return response.toJson();
		} catch (Throwable th) {
			throw new LocalSearchException(th.getMessage(), th);
		}
	}

	/**
	 * 簡易形式の JSON 文字列で全文検索＋フィールド絞り込みを実行します。 Python (JPype) など外部から絞り込み条件を渡す場合に便利です。
	 *
	 * <p>
	 * 入力 JSON 形式（searchJson の OpenSearch 形式とは異なる簡易形式です）:
	 * </p>
	 * 
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

	public SearchResult[] searchLucene(String query, int limit) {

		JsonNode queryString = JsonNode.object();
		queryString.put("query", query);
		queryString.put("default_field", this.default_field_name);

		JsonNode request = JsonNode.object();
		request.put("query", JsonNode.object().put("query_string", queryString));
		request.put("size", limit);

		return executeSearch(request);
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
	 * 簡易形式のaggregationリクエストを実行し、 OpenSearch互換形式のaggregationレスポンスを返します。
	 *
	 * <p>
	 * 入力例:
	 * </p>
	 * 
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
	 * <p>
	 * 出力例:
	 * </p>
	 * 
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

			String field = getRequiredString(request, "field");

			String aggregationName = getOptionalString(request, "name", "values");

			String query = getOptionalString(request, "query", null);

			int size = getOptionalInt(request, "size", 10);

			if (size < 1) {
				throw new IllegalArgumentException("size must be greater than 0");
			}

			JsonNode filters = request.get("filters");

			JsonNode searchRequest = createAggregationRequest(aggregationName, field, query, size, filters);

			JsonNode luceneResponse = executeRequest(searchRequest);

			JsonNode openSearchResponse = toOpenSearchAggregationResponse(aggregationName, luceneResponse);

			return openSearchResponse.toJson();

		} catch (Exception e) {
			throw new LocalSearchException(e.getMessage(), e);
		}
	}

	// -----------------------------------------------------------------------
	// Java API: count()
	// -----------------------------------------------------------------------

	/**
	 * インデックス内の全ドキュメント件数を返します。
	 *
	 * @return ドキュメント件数
	 * @throws LocalSearchException if search fails
	 */
	public long count() {
		return count(null, (Map<String, String>) null);
	}

	/**
	 * 全文検索クエリにマッチするドキュメント件数を返します。
	 *
	 * @param query 全文検索クエリ（null または空文字の場合は全件）
	 * @return マッチするドキュメント件数
	 * @throws LocalSearchException if search fails
	 */
	public long count(String query) {
		return count(query, (Map<String, String>) null);
	}

	/**
	 * 全文検索クエリ＋フィールド絞り込みにマッチするドキュメント件数を返します。
	 *
	 * @param query   全文検索クエリ（null または空文字の場合は match_all）
	 * @param filters keyword フィールドの絞り込み条件（フィールド名 → 値）
	 * @return マッチするドキュメント件数
	 * @throws LocalSearchException if search fails
	 */
	public long count(String query, Map<String, String> filters) {
		JsonNode searchRequest = createCountRequest(query, toFilterNode(filters));
		return toTotalHits(executeRequest(searchRequest));
	}

	/**
	 * 指定フィールドの値が一致するドキュメント件数を返します。
	 *
	 * <p>
	 * 全文検索ではなく、keyword フィールドの完全一致で絞り込みます。 形態素解析で生成された word.*
	 * フィールドなど、分析フィールドを条件に使う場合に便利です。
	 * </p>
	 *
	 * <p>
	 * 例:
	 * </p>
	 * 
	 * <pre>
	 * // word.noun=ニッサン が出現する文書の件数
	 * long count = search.count("word.noun", "ニッサン");
	 *
	 * // category=technology の文書の件数
	 * long count = search.count("category", "technology");
	 * </pre>
	 *
	 * @param filterField 絞り込み対象のフィールド名
	 * @param filterValue 絞り込み対象のフィールド値
	 * @return マッチするドキュメント件数
	 * @throws LocalSearchException if search fails
	 */
	public long count(String filterField, String filterValue) {
		return count(null, java.util.Map.of(filterField, filterValue));
	}

	// -----------------------------------------------------------------------
	// Java API: aggregate()
	// -----------------------------------------------------------------------

	/**
	 * 全ドキュメントを対象に、指定フィールドの terms aggregation を実行します。
	 *
	 * @param field 集計対象フィールド名
	 * @param size  返すバケット数の上限
	 * @return フィールド値 → ドキュメント件数のマップ（件数降順）
	 * @throws LocalSearchException if aggregation fails
	 */
	public Map<String, Long> aggregate(String field, int size) {
		return aggregate(field, null, size, null);
	}

	/**
	 * 全文検索クエリで絞り込んだ上で、指定フィールドの terms aggregation を実行します。
	 *
	 * @param field 集計対象フィールド名
	 * @param query 全文検索クエリ（null または空文字の場合は全件）
	 * @param size  返すバケット数の上限
	 * @return フィールド値 → ドキュメント件数のマップ（件数降順）
	 * @throws LocalSearchException if aggregation fails
	 */
	public Map<String, Long> aggregate(String field, String query, int size) {
		return aggregate(field, query, size, null);
	}

	/**
	 * 全文検索クエリ＋フィールド絞り込みで絞り込んだ上で、指定フィールドの terms aggregation を実行します。
	 *
	 * @param field   集計対象フィールド名
	 * @param query   全文検索クエリ（null または空文字の場合は全件）
	 * @param size    返すバケット数の上限
	 * @param filters keyword フィールドの絞り込み条件（null または空の場合はスキップ）
	 * @return フィールド値 → ドキュメント件数のマップ（件数降順）
	 * @throws LocalSearchException if aggregation fails
	 */
	public Map<String, Long> aggregate(String field, String query, int size, Map<String, String> filters) {
		JsonNode searchRequest = createAggregationRequest("values", field, query, size, toFilterNode(filters));
		JsonNode response = executeRequest(searchRequest);
		return toAggregationMap("values", response);
	}

	/**
	 * 指定フィールドの値で絞り込んだ上で、別フィールドの terms aggregation を実行します。
	 *
	 * <p>
	 * 全文検索ではなく、keyword フィールドの完全一致で絞り込みます。 形態素解析で生成された word.* フィールドなどを条件に使う場合に便利です。
	 * </p>
	 *
	 * <p>
	 * 例:
	 * </p>
	 * 
	 * <pre>
	 * // word.noun=ニッサン が出現する文書の中で word.noun を集計
	 * Map&lt;String, Long&gt; result = search.aggregate("word.noun", "word.noun", "ニッサン", 1000);
	 *
	 * // word.noun=ニッサン が出現する文書の中で word.verb を集計
	 * Map&lt;String, Long&gt; result = search.aggregate("word.verb", "word.noun", "ニッサン", 1000);
	 *
	 * // category=car が設定された文書の中で word.noun を集計
	 * Map&lt;String, Long&gt; result = search.aggregate("word.noun", "category", "car", 1000);
	 * </pre>
	 *
	 * @param aggregationField 集計対象フィールド名
	 * @param filterField      絞り込み対象のフィールド名
	 * @param filterValue      絞り込み対象のフィールド値
	 * @param size             返すバケット数の上限
	 * @return フィールド値 → ドキュメント件数のマップ（件数降順）
	 * @throws LocalSearchException if aggregation fails
	 */
	public Map<String, Long> aggregate(String aggregationField, String filterField, String filterValue, int size) {
		return aggregate(aggregationField, null, size, java.util.Map.of(filterField, filterValue));
	}

	/**
	 * Lucene Query Parser syntax を検証します。
	 *
	 * <p>
	 * このメソッドはクエリを実行せず、 Lucene QueryParser で正常に解析できるかどうかだけを確認します。
	 * </p>
	 *
	 * <pre>
	 * LuceneQueryValidationResult result = search.validateLuceneQuery("京都 AND (寺院 OR 神社)");
	 *
	 * if (!result.isValid()) {
	 * 	System.out.println(result.getMessage());
	 * }
	 * </pre>
	 *
	 * @param query Lucene Query Parser syntax のクエリ文字列
	 * @return validation result
	 */
	public LuceneQueryValidationResult validateLuceneQuery(String query) {

		try (nlp4j.lucene9.SearchSession session = index.acquireSearcher()) {

			nlp4j.lucene9.LuceneQueryBuilder.parseQueryString(
					query, this.default_field_name, session.getAnalyzer(), this.schema);

			return LuceneQueryValidationResult.valid();

		} catch (Exception e) {

			return LuceneQueryValidationResult.invalid(e.getMessage());
		}
	}

	// -----------------------------------------------------------------------
	// Private helpers
	// -----------------------------------------------------------------------

	/**
	 * count() 用のリクエストを生成します（aggs なし、size=0）。 query が null または空文字の場合は match_all
	 * になります。
	 *
	 * @param query   全文検索クエリ（null または空文字の場合は match_all）
	 * @param filters keyword フィールドの絞り込み条件（null の場合はスキップ）
	 * @return OpenSearch 形式のリクエスト JsonNode
	 */
	private JsonNode createCountRequest(String query, JsonNode filters) {
		JsonNode root = JsonNode.object();
		root.put("size", 0);

		boolean hasQuery = query != null && !query.isEmpty();
		boolean hasFilters = filters != null && !filters.isNull() && filters.size() > 0;

		if (hasQuery || hasFilters) {
			JsonNode boolQuery = JsonNode.object();

			if (hasQuery) {
				JsonNode must = JsonNode.array();
				must.add(JsonNode.object().put("match", JsonNode.object().put(this.default_field_name, query)));
				boolQuery.put("must", must);
			}

			if (hasFilters) {
				JsonNode filter = JsonNode.array();
				for (String fieldName : filters.keys()) {
					String value = filters.get(fieldName).asString(null);
					if (value == null) {
						continue;
					}
					filter.add(JsonNode.object().put("term", JsonNode.object().put(fieldName, value)));
				}
				if (filter.size() > 0) {
					boolQuery.put("filter", filter);
				}
			}

			root.put("query", JsonNode.object().put("bool", boolQuery));
		}

		return root;
	}

	/**
	 * レスポンスの hits.total.value を返します。
	 *
	 * @param response api.search() からのレスポンス
	 * @return ヒット件数
	 */
	private long toTotalHits(JsonNode response) {
		return response.get("hits").get("total").get("value").asLong(0);
	}

	/**
	 * aggregation レスポンスから {@code Map<String, Long>} を生成します。 LinkedHashMap
	 * を使用して件数降順を保持します。
	 *
	 * @param aggregationName aggregation 名
	 * @param response        api.search() からのレスポンス
	 * @return フィールド値 → ドキュメント件数のマップ
	 */
	private Map<String, Long> toAggregationMap(String aggregationName, JsonNode response) {

		JsonNode buckets = response.get("aggregations").get(aggregationName).get("buckets");

		Map<String, Long> result = new LinkedHashMap<>();

		for (JsonNode bucket : buckets.asList()) {
			String key = bucket.get("key").asString();
			long docCount = bucket.get("doc_count").asLong(0);
			result.put(key, docCount);
		}

		return result;
	}

	/**
	 * {@code Map<String, String>} を filters 用の JsonNode に変換します。 null または空の場合は null
	 * を返します。
	 *
	 * @param filters フィールド名 → 値のマップ
	 * @return filters 用 JsonNode（null の場合あり）
	 */
	private JsonNode toFilterNode(Map<String, String> filters) {
		if (filters == null || filters.isEmpty()) {
			return null;
		}
		JsonNode node = JsonNode.object();
		for (Map.Entry<String, String> entry : filters.entrySet()) {
			if (entry.getKey() == null || entry.getValue() == null) {
				continue;
			}
			node.put(entry.getKey(), entry.getValue());
		}
		return node;
	}

	private JsonNode createAggregationRequest(String aggregationName, String field, String query, int size,
			JsonNode filters) {

		JsonNode root = JsonNode.object();

		// 検索ヒット本文は不要
		root.put("size", 0);

		boolean hasQuery = query != null && !query.isEmpty();

		boolean hasFilters = filters != null && !filters.isNull() && filters.size() > 0;

		if (hasQuery || hasFilters) {
			JsonNode boolQuery = JsonNode.object();

			if (hasQuery) {
				JsonNode must = JsonNode.array();

				must.add(JsonNode.object().put("match", JsonNode.object().put(this.default_field_name, query)));

				boolQuery.put("must", must);
			}

			if (hasFilters) {
				JsonNode filter = JsonNode.array();

				for (String fieldName : filters.keys()) {
					JsonNode valueNode = filters.get(fieldName);

					if (valueNode == null || valueNode.isNull()) {
						continue;
					}

					String value = valueNode.asString(null);

					if (value == null) {
						continue;
					}

					filter.add(JsonNode.object().put("term", JsonNode.object().put(fieldName, value)));
				}

				if (filter.size() > 0) {
					boolQuery.put("filter", filter);
				}
			}

			root.put("query", JsonNode.object().put("bool", boolQuery));
		}

		JsonNode terms = JsonNode.object();
		terms.put("field", field);
		terms.put("size", size);

		JsonNode aggregation = JsonNode.object();
		aggregation.put("terms", terms);

		JsonNode aggregations = JsonNode.object();
		aggregations.put(aggregationName, aggregation);

		root.put("aggs", aggregations);

		return root;
	}

	/**
	 * LuceneLocalSearchApiのaggregation結果を、 OpenSearch互換形式に変換します。
	 *
	 * @param aggregationName aggregation名
	 * @param luceneResponse  LuceneLocalSearchApiのレスポンス
	 * @return OpenSearch互換形式のレスポンス
	 */
	private JsonNode toOpenSearchAggregationResponse(String aggregationName, JsonNode luceneResponse) {

		JsonNode rawAggregations = requireNode(luceneResponse, "aggregations", "Lucene response");

		JsonNode rawAggregation = requireNode(rawAggregations, aggregationName, "aggregations");

		JsonNode rawBuckets = requireNode(rawAggregation, "buckets", "aggregation '" + aggregationName + "'");

		JsonNode openSearchBuckets = JsonNode.array();

		for (JsonNode rawBucket : rawBuckets.asList()) {

			JsonNode rawKey = requireNode(rawBucket, "key", "aggregation bucket");

			JsonNode rawDocCount = rawBucket.get("doc_count");

			/*
			 * Lucene側で count という名前の場合にも 対応できるようにする。
			 */
			if (rawDocCount == null || rawDocCount.isNull()) {
				rawDocCount = rawBucket.get("count");
			}

			if (rawDocCount == null || rawDocCount.isNull()) {
				throw new IllegalStateException("Aggregation bucket does not " + "contain doc_count or count");
			}

			JsonNode bucket = JsonNode.object();

			/*
			 * keyを文字列化せず、そのJSON型を維持する。 keywordでは文字列、将来の数値集計では数値になる。
			 */
			bucket.put("key", rawKey);

			JsonNode keyAsString = rawBucket.get("key_as_string");

			if (keyAsString != null && !keyAsString.isNull()) {
				bucket.put("key_as_string", keyAsString);
			}

			bucket.put("doc_count", (Number) rawDocCount.asLong(0));

			openSearchBuckets.add(bucket);
		}

		JsonNode openSearchAggregation = JsonNode.object();

		copyIfPresent(rawAggregation, openSearchAggregation, "doc_count_error_upper_bound");

		copyIfPresent(rawAggregation, openSearchAggregation, "sum_other_doc_count");

		openSearchAggregation.put("buckets", openSearchBuckets);

		JsonNode openSearchAggregations = JsonNode.object();

		openSearchAggregations.put(aggregationName, openSearchAggregation);

		JsonNode result = JsonNode.object();

		result.put("aggregations", openSearchAggregations);

		return result;
	}

	/**
	 * @param parent
	 * @param fieldName
	 * @param context
	 * @return
	 */
	private JsonNode requireNode(JsonNode parent, String fieldName, String context) {

		if (parent == null || parent.isNull()) {
			throw new IllegalStateException(context + " is null");
		}

		JsonNode value = parent.get(fieldName);

		if (value == null || value.isNull()) {
			throw new IllegalStateException(context + " does not contain '" + fieldName + "'");
		}

		return value;
	}

	private void copyIfPresent(JsonNode source, JsonNode target, String fieldName) {

		JsonNode value = source.get(fieldName);

		if (value != null && !value.isNull()) {
			target.put(fieldName, value);
		}
	}

	private String getRequiredString(JsonNode object, String fieldName) {

		JsonNode value = object.get(fieldName);

		if (value == null || value.isNull()) {
			throw new IllegalArgumentException("Required field is missing: " + fieldName);
		}

		String result = value.asString(null);

		if (result == null || result.isBlank()) {
			throw new IllegalArgumentException("Field must not be empty: " + fieldName);
		}

		return result;
	}

	private String getOptionalString(JsonNode object, String fieldName, String defaultValue) {

		JsonNode value = object.get(fieldName);

		if (value == null || value.isNull()) {
			return defaultValue;
		}

		return value.asString(defaultValue);
	}

	private int getOptionalInt(JsonNode object, String fieldName, int defaultValue) {

		JsonNode value = object.get(fieldName);

		if (value == null || value.isNull()) {
			return defaultValue;
		}

		return value.asInt(defaultValue);
	}

	@Override
	public String toString() {
		return "LocalSearch [language=" + language + ", autoAnalyze=" + autoAnalyze + ", default_field_name="
				+ default_field_name + ", schema=" + schema + ", index=" + index + "]";
	}
}
