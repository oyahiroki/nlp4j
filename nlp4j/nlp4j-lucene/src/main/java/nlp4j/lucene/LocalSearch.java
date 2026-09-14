/*
 * Copyright (C) 2026 Hiroki OYA
 *
 * Licensed under the Apache License, Version 2.0
 */
package nlp4j.lucene;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.document.Document;

import nlp4j.json.JsonNode;
import nlp4j.lucene9.FieldTypeDef;
import nlp4j.lucene9.LuceneIndex;
import nlp4j.lucene9.LuceneLocalSearchApi;
import nlp4j.lucene9.SearchSchema;
import nlp4j.lucene9.SearchSchemaStore;

/**
 * Simple local search engine wrapper for Lucene. Provides a simplified API for
 * adding documents and performing text searches with language-specific field
 * support (Japanese, English, or default).
 *
 * <p>
 * すべての文字列クエリは <b>Lucene Query Parser syntax</b> として扱われます。
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
 * try (LocalSearch search = LocalSearch.builder("ja").vectorDimension(1024).loadIndexFrom(Path.of("./index"))
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
	 * 指定した言語の {@link Builder} を返します。
	 *
	 * @param language 言語コード（"ja" で日本語、"en" で英語、それ以外はデフォルトテキストフィールド）
	 * @return 新しい Builder インスタンス
	 */
	public static Builder builder(String language) {
		return new Builder(language);
	}

	/**
	 * Builder for {@link LocalSearch}.
	 *
	 * <pre>
	 * LocalSearch search = LocalSearch.builder("ja").autoAnalyze(true).vectorDimension(1024)
	 * 		.loadIndexFrom(Path.of("./index")).build();
	 * </pre>
	 */
	public static class Builder {

		private final String language;
		private boolean autoAnalyze = true;
		private int vectorDimension = 0;
		private Path indexDir = null;
		private ZoneId zoneId = ZoneId.systemDefault();

		private SearchRecordEnricher enricher;

		private final java.util.Map<String, FieldTypeDef> fields = new java.util.LinkedHashMap<>();

		/**
		 * language は、フィールド名省略時のデフォルトテキストフィールド、 body/text フィールドのAnalyzer、デフォルト検索対象、
		 * および自然言語エンリッチ処理を決定します。
		 * 
		 * @param language LocalSearch で使用する言語コード
		 */
		private Builder(String language) {
			this.language = language;
		}

		/**
		 * 言語固有の自然言語解析（自動エンリッチ）を有効／無効にします。 デフォルトは true（有効）です。
		 * 
		 * <p>
		 * この設定は word.* などの自然言語エンリッチ処理を制御します。 Lucene Analyzer による全文検索用の解析は無効になりません。
		 * </p>
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
		 * ディスク上の Lucene インデックス読み込みディレクトリを指定します。
		 *
		 * @param indexDir インデックスを読み込むディレクトリパス
		 * @return this Builder
		 */
		public Builder loadIndexFrom(Path indexDir) {
			this.indexDir = indexDir;
			return this;
		}

		/**
		 * 自然言語エンリッチ処理に使用する独自の SearchRecordEnricher を設定します。
		 *
		 * <p>
		 * 指定しない場合は language に対応するデフォルトの Enricher が使用されます。
		 * </p>
		 *
		 * @param enricher 使用する SearchRecordEnricher
		 * @return this Builder
		 */
		public Builder enricher(SearchRecordEnricher enricher) {
			this.enricher = enricher;
			return this;
		}

		/**
		 * タイムゾーンを設定します。
		 *
		 * <p>
		 * Date フィールドの値にオフセットが含まれない場合（例: {@code 2026-08-21}、
		 * {@code 2026-08-21T14:30:00}）、このタイムゾーンが使用されます。 オフセット付きの値（例:
		 * {@code 2026-08-21T14:30:00+09:00}、{@code ...Z}）では 指定されたオフセットが優先されます。
		 * </p>
		 *
		 * <p>
		 * 省略時は {@link ZoneId#systemDefault()} が使用されます。
		 * ディスクインデックスを別環境で利用する場合は明示指定を推奨します。
		 * </p>
		 *
		 * <pre>
		 * LocalSearch.builder("ja").timeZone("Asia/Tokyo").build();
		 * </pre>
		 *
		 * @param zoneId タイムゾーン文字列（例: {@code "Asia/Tokyo"}、{@code "UTC"}）
		 * @return this Builder
		 * @throws java.time.zone.ZoneRulesException if the zone ID is unknown
		 */
		public Builder timeZone(String zoneId) {
			this.zoneId = ZoneId.of(zoneId);
			return this;
		}

		/**
		 * 明示フィールド定義を追加します。 suffix パターンより優先されます。
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

	/** ベクトルフィールド名の定数 */
	private static final String VECTOR_FIELD = "vector";

	private String language;
	private boolean autoAnalyze;
	int vectorDimension;
	private ZoneId zoneId;
	private SearchRecordEnricher textEnricher;
	private SearchRecordEnricher dateFieldEnricher;

	private String default_field_name;
	private final SearchSchema schema;
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

		// ------------------------------------------------------------------
		// スキーマ解決（schema / vectorDimension を先に確定させる）
		// initIndex() より先に行うことで、schema conflict が起きても
		// LuceneIndex の resource leak を防ぎます。
		// ------------------------------------------------------------------
		if (builder.indexDir == null) {
			this.schema = createSchema(builder);
		} else {
			if (SearchSchemaStore.exists(builder.indexDir)) {
				try {
					SearchSchema persisted = SearchSchemaStore.load(builder.indexDir);
					this.schema = mergeSchemas(persisted, builder);
				} catch (IOException | IllegalArgumentException e) {
					throw new LocalSearchException("Failed to load schema: " + e.getMessage(), e);
				}
			} else {
				// Backwards compatibility: no schema file found
				this.schema = createSchema(builder);
			}
		}

		this.vectorDimension = resolveVectorDimension(builder, this.schema);

		// ------------------------------------------------------------------
		// スキーマが確定した後に LuceneIndex を開く
		// ------------------------------------------------------------------
		if (builder.indexDir == null) {
			initIndex(builder.language);
		} else {
			initIndex(builder.indexDir, builder.language);
		}
		this.zoneId = builder.zoneId;
		this.api = new LuceneLocalSearchApi(index, this.schema, this.zoneId);
		this.default_field_name = resolveDefaultFieldName(builder.language);

		this.dateFieldEnricher = new DateFieldEnricher(this.schema, builder.zoneId);

		if (builder.enricher != null) {
			this.textEnricher = builder.enricher;
		} else {
			this.textEnricher = SearchRecordEnrichers.forLanguage(builder.language);
		}
	}

	/**
	 * 指定した言語で LocalSearch インスタンスを生成します。
	 *
	 * @param language 言語コード（"ja" で日本語、"en" で英語、それ以外はデフォルトテキストフィールド）
	 * @throws LocalSearchException インデックスの初期化に失敗した場合
	 */
	public LocalSearch(String language) {
		this(new Builder(language));
	}

	/**
	 * 指定した言語とベクトル次元数で LocalSearch インスタンスを生成します。
	 *
	 * @param language        言語コード（"ja" で日本語、"en" で英語、それ以外はデフォルトテキストフィールド）
	 * @param vectorDimension KNN ベクトル検索に使用するベクトルの次元数（0 の場合はベクトルフィールドなし）
	 * @throws LocalSearchException インデックスの初期化に失敗した場合
	 */
	public LocalSearch(String language, int vectorDimension) {
		this(new Builder(language).vectorDimension(vectorDimension));
	}

	/**
	 * 指定した言語・ベクトル次元数・ディスクインデックスディレクトリで LocalSearch インスタンスを生成します。
	 *
	 * @param language        言語コード（"ja" で日本語、"en" で英語、それ以外はデフォルトテキストフィールド）
	 * @param vectorDimension KNN ベクトル検索に使用するベクトルの次元数（0 の場合はベクトルフィールドなし）
	 * @param indexDir        Lucene インデックスを読み書きするディレクトリ
	 * @throws LocalSearchException インデックスの初期化に失敗した場合
	 */
	public LocalSearch(String language, int vectorDimension, File indexDir) {
		this(new Builder(language).vectorDimension(vectorDimension).loadIndexFrom(indexDir.toPath()));
	}

	/**
	 * 指定した言語・ベクトル次元数・ディスクインデックスパスで LocalSearch インスタンスを生成します。
	 *
	 * @param language        言語コード（"ja" で日本語、"en" で英語、それ以外はデフォルトテキストフィールド）
	 * @param vectorDimension KNN ベクトル検索に使用するベクトルの次元数（0 の場合はベクトルフィールドなし）
	 * @param indexDir        Lucene インデックスを読み書きするディレクトリパス
	 * @throws LocalSearchException インデックスの初期化に失敗した場合
	 */
	public LocalSearch(String language, int vectorDimension, Path indexDir) {
		this(new Builder(language).vectorDimension(vectorDimension).loadIndexFrom(indexDir));
	}

	// -----------------------------------------------------------------------
	// Static factory
	// -----------------------------------------------------------------------

	/**
	 * 指定した言語・ベクトル次元数・ディスクインデックスパスで LocalSearch インスタンスを生成するファクトリメソッドです。
	 * {@code new LocalSearch(language, vectorDimension, indexDir)} と同等です。
	 *
	 * @param language        言語コード（"ja" で日本語、"en" で英語、それ以外はデフォルトテキストフィールド）
	 * @param vectorDimension KNN ベクトル検索に使用するベクトルの次元数（0 の場合はベクトルフィールドなし）
	 * @param indexDir        Lucene インデックスを読み書きするディレクトリパス
	 * @return 新しい LocalSearch インスタンス
	 * @throws LocalSearchException インデックスの初期化に失敗した場合
	 */
	public static LocalSearch open(String language, int vectorDimension, Path indexDir) {
		return new Builder(language).vectorDimension(vectorDimension).loadIndexFrom(indexDir).build();
	}

	/**
	 * ベクトルのみを持つドキュメントをインデックスに追加します。 テキストフィールドは登録されません。
	 *
	 * @param id     ドキュメントの一意識別子
	 * @param vector 登録するベクトル
	 * @throws LocalSearchException ドキュメントの追加に失敗した場合
	 */
	public void add(String id, float[] vector) {
		validateVector(vector);
		Document doc1 = schema.document(zoneId) //
				.put("id", id) //
				.putVector(VECTOR_FIELD, vector) //
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
		validateVector(vector);
		try {
			var builder = schema.document(zoneId).put("id", id).putVector(VECTOR_FIELD, vector);

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
	 * フィールド名を指定せずにテキストドキュメントを追加します。
	 *
	 * <p>
	 * テキストは language に対応するデフォルトテキストフィールドに登録されます。
	 * </p>
	 *
	 * <ul>
	 * <li>{@code ja} → {@code text_ja}</li>
	 * <li>{@code en} → {@code text_en}</li>
	 * <li>その他 → {@code text}</li>
	 * </ul>
	 *
	 * <p>
	 * {@code autoAnalyze=true} の場合は、言語に対応する自然言語エンリッチ処理も実行されます。
	 * </p>
	 *
	 * @param id   ドキュメントの一意識別子
	 * @param text 登録するテキスト
	 * @throws LocalSearchException ドキュメントの追加に失敗した場合
	 */
	public void add(String id, String text) {
		SearchRecord record = new SearchRecord(id, text);
		add(record);
	}

	/**
	 * フィールド名を指定せずにテキストドキュメントを追加します。
	 *
	 * <p>
	 * テキストは language に対応するデフォルトテキストフィールドに登録されます。
	 * </p>
	 *
	 * <ul>
	 * <li>{@code ja} → {@code text_ja}</li>
	 * <li>{@code en} → {@code text_en}</li>
	 * <li>その他 → {@code text}</li>
	 * </ul>
	 *
	 * <p>
	 * {@code autoAnalyze=true} の場合は、言語に対応する自然言語エンリッチ処理も実行されます。
	 * </p>
	 *
	 * @param id     ドキュメントの一意識別子
	 * @param text   登録するテキスト
	 * @param vector KNN ベクトル
	 * @throws LocalSearchException ドキュメントの追加に失敗した場合
	 */
	public void add(String id, String text, float[] vector) {
		if (vector == null) {
			throw new LocalSearchException("vector must not be null",
					new IllegalArgumentException("vector must not be null"));
		}
		SearchRecord record = new SearchRecord(id, text);
		record.setVector(vector);
		add(record);
	}

	/**
	 * フィールド名を指定せずにテキストドキュメントを追加します。
	 *
	 * <p>
	 * テキストは language に対応するデフォルトテキストフィールドに登録されます。
	 * </p>
	 *
	 * <ul>
	 * <li>{@code ja} → {@code text_ja}</li>
	 * <li>{@code en} → {@code text_en}</li>
	 * <li>その他 → {@code text}</li>
	 * </ul>
	 *
	 * <p>
	 * {@code autoAnalyze=true} の場合は、言語に対応する自然言語エンリッチ処理も実行されます。
	 * </p>
	 *
	 * @param id     ドキュメントの一意識別子
	 * @param text   登録するテキスト
	 * @param vector KNN ベクトル
	 * @param fields keyword フィールドの追加値（フィールド名 → 値）
	 * @throws LocalSearchException ドキュメントの追加に失敗した場合
	 */
	public void add(String id, String text, float[] vector, java.util.Map<String, String> fields) {
		if (vector == null) {
			throw new LocalSearchException("vector must not be null",
					new IllegalArgumentException("vector must not be null"));
		}
		SearchRecord record = new SearchRecord(id, text);
		record.setVector(vector);
		if (fields != null) {
			for (java.util.Map.Entry<String, String> entry : fields.entrySet()) {
				if (entry.getKey() != null && entry.getValue() != null) {
					record.addData(entry.getKey(), entry.getValue());
				}
			}
		}
		add(record);
	}

	/**
	 * SearchRecord をドキュメントとして登録します。
	 *
	 * <p>
	 * {@link SearchRecord#getBody()} の値は language に対応する デフォルトテキストフィールド
	 * （{@code text_ja}, {@code text_en}, {@code text}）に登録されます。
	 * </p>
	 *
	 * <p>
	 * {@code autoAnalyze=true} の場合は、language に対応する {@link SearchRecordEnricher}
	 * による自然言語エンリッチ処理を実行します。
	 * </p>
	 *
	 * @param record 登録するドキュメントレコード
	 * @throws LocalSearchException ドキュメントの追加に失敗した場合
	 */
	public void add(SearchRecord record) {
		if (record.hasVector()) {
			validateVector(record.getVector());
		}
		try {
			enrich(record);
			Map<String, String> textFields = new LinkedHashMap<>();
			if (record.getBody() != null) {
				textFields.put(this.default_field_name, record.getBody());
			}
			addDocument(record, null, textFields);
		} catch (IOException e) {
			throw new LocalSearchException(e.getMessage(), e);
		}
	}

	/**
	 * JSON 文字列からドキュメントをインデックスに追加します。
	 *
	 * <p>
	 * {@code id} は必須です。 {@code body}, {@code text}, {@code text_ja},
	 * {@code text_en} は 独立した全文検索フィールドとして扱われ、指定されたフィールド名のまま インデックスおよび stored field
	 * に登録されます。
	 * </p>
	 *
	 * <p>
	 * テキストフィールドを複数指定することもできます。 例えば {@code body} と {@code text_en} に異なる値を指定しても、
	 * それぞれ独立したフィールドとして登録されます。
	 * </p>
	 *
	 * <p>
	 * テキストフィールドが存在しない場合でも、 有効な {@code vector} が指定されていれば登録できます。
	 * </p>
	 *
	 * @param json_string 登録するドキュメントの JSON 文字列
	 * @throws LocalSearchException JSON の解析またはドキュメントの登録に失敗した場合
	 */
	public void addJson(String json_string) {
		try {
			JsonNode json = JsonNode.parse(json_string);

			String id = getRequiredString(json, "id"); // may throw IllegalArgumentException

			// vector フィールドを解析
			float[] vector = parseVector(json);

			// テキストフィールドを抽出
			Map<String, String> textFields = new LinkedHashMap<>();
			for (String tf : TEXT_FIELDS) {
				JsonNode node = json.get(tf);
				if (node != null && !node.isNull()) {
					String val = node.asString(null);
					if (val != null) {
						textFields.put(tf, val);
					}
				}
			}

			if (textFields.isEmpty() && vector == null) {
				throw new IllegalArgumentException("Required field is missing: at least one text field (" + TEXT_FIELDS
						+ ") or vector is required");
			}

			String analysisText = resolveAnalysisText(textFields);
			SearchRecord record = new SearchRecord(id, analysisText);

			if (vector != null) {
				record.setVector(vector);
			}

			// JSON の追加フィールドを SearchRecord に転写
			for (String fieldName : json.keys()) {
				if ("id".equals(fieldName) || VECTOR_FIELD.equals(fieldName) || TEXT_FIELDS.contains(fieldName)) {
					continue;
				}

				JsonNode valueNode = json.get(fieldName);
				if (valueNode == null || valueNode.isNull()) {
					continue;
				}

				if (valueNode.isArray()) {

					// JSON array は要素数に関係なく multi-valued として登録する
					ensureField(fieldName, true);

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
			addDocument(record, buildStoredDataJson(json), textFields);

		} catch (Throwable th) {
			throw new LocalSearchException(th.getMessage(), th);
		}
	}

	private void ensureWordField(String fieldName) {
		schema.addIfAbsent( //
				fieldName, //
				FieldTypeDef.keyword() //
						.stored(true) //
						.aggregatable(true) //
						.multiValued(true) //
		);
	}

	/**
	 * SearchRecord を Lucene Document としてインデックスに追加します。 add(SearchRecord) と addJson()
	 * の共通登録経路です。
	 *
	 * @param record     登録するドキュメントレコード（enrich 済み）
	 * @param rawJson    元の JSON 文字列。null の場合は "data" フィールドを登録しません。
	 * @param textFields 登録する全文検索テキストフィールド （body / text / text_ja / text_en）
	 * @throws IOException インデックスへの追加に失敗した場合
	 */
	private void addDocument(SearchRecord record, String rawJson, Map<String, String> textFields) throws IOException {
		var builder = schema.document(zoneId).put("id", record.getId());

		if (textFields != null) {
			for (Map.Entry<String, String> entry : textFields.entrySet()) {
				if (entry.getKey() != null && entry.getValue() != null) {
					builder.put(entry.getKey(), entry.getValue());
				}
			}
		}

		if (rawJson != null) {
			builder.put("data", rawJson);
		}

		// ベクトルを登録
		if (record.hasVector()) {
			builder.putVector(VECTOR_FIELD, record.getVector());
		}

		// word.* フィールドへキーワードを登録
		for (SearchKeyword kw : record.getKeywords()) {
			String fieldName = kw.getPos();

			if (fieldName == null || fieldName.isBlank()) {
				continue;
			}

			ensureWordField(fieldName);

			builder.put(fieldName, kw.getLex());
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
	}

	/**
	 * {@code data} stored field に保存する JSON 文字列を生成します。
	 *
	 * <p>
	 * 入力 JSON から {@code "vector"} フィールドを除外した JSON を返します。 KNN ベクトルは Lucene
	 * の専用フィールドに保存されるため、{@code data} への二重保存は不要です。 これにより大規模インデックス（Wikipedia
	 * 等）でのストレージ肥大化を防ぎます。
	 * </p>
	 *
	 * <p>
	 * 例: 入力 JSON が {@code {"id":"1","body":"...","vector":[...],"category":"city"}}
	 * のとき、 返値は {@code {"id":"1","body":"...","category":"city"}} になります。
	 * </p>
	 *
	 * @param json 元の入力 JsonNode
	 * @return {@code "vector"} フィールドを除いた JSON 文字列
	 */
	private String buildStoredDataJson(JsonNode json) {
		com.google.gson.JsonObject copy = json.rawObject().deepCopy();
		copy.remove(VECTOR_FIELD);
		return copy.toString();
	}

	private static final Set<String> TEXT_FIELDS = Set.of("body", "text", "text_ja", "text_en");

	/**
	 * 自然言語エンリッチ処理に渡すテキストを優先順位に従って解決します。
	 *
	 * <p>
	 * 優先順位は {@code body -> text -> default_field_name} です。 現在の language
	 * に対応しない言語専用フィールドしか存在しない場合は {@code null} を返し、自然言語エンリッチ処理は実行しません。
	 * </p>
	 */
	private String resolveAnalysisText(Map<String, String> textFields) {
		if (textFields == null || textFields.isEmpty()) {
			return null;
		}
		if (textFields.containsKey("body")) {
			return textFields.get("body");
		}
		if (textFields.containsKey("text")) {
			return textFields.get("text");
		}
		if (textFields.containsKey(this.default_field_name)) {
			return textFields.get(this.default_field_name);
		}

		return null;
	}

	/**
	 * ベクトルの妥当性を検証します。
	 *
	 * <p>
	 * 以下の条件を検証します:
	 * </p>
	 * <ul>
	 * <li>vector が null でないこと</li>
	 * <li>vectorDimension が 0 より大きいこと（ベクトルフィールドが有効であること）</li>
	 * <li>vector.length が vectorDimension と一致すること</li>
	 * <li>各要素が NaN / Infinity でないこと</li>
	 * </ul>
	 *
	 * @param vector 検証するベクトル
	 * @throws LocalSearchException 検証失敗時
	 */
	private void validateVector(float[] vector) {
		if (vector == null) {
			throw new LocalSearchException("vector must not be null",
					new IllegalArgumentException("vector must not be null"));
		}
		if (vectorDimension <= 0) {
			throw new LocalSearchException(
					"Vector field is not enabled. Specify vectorDimension when building LocalSearch.",
					new IllegalArgumentException("Vector field is not enabled"));
		}
		if (vector.length != vectorDimension) {
			throw new LocalSearchException(
					"Vector dimension mismatch: expected=" + vectorDimension + ", actual=" + vector.length,
					new IllegalArgumentException("Vector dimension mismatch"));
		}
		for (int i = 0; i < vector.length; i++) {
			if (Float.isNaN(vector[i]) || Float.isInfinite(vector[i])) {
				throw new LocalSearchException("vector[" + i + "] contains invalid value: " + vector[i],
						new IllegalArgumentException("Vector contains NaN or Infinite value"));
			}
		}
	}

	/**
	 * JSON ノードから "vector" フィールドを解析して float[] を返します。 vector フィールドが存在しない場合は null
	 * を返します。
	 *
	 * @param json 対象の JsonNode
	 * @return float[] または null
	 * @throws IllegalArgumentException vector が配列でない場合
	 * @throws LocalSearchException     vectorDimension が未設定の場合、 次元数不一致または不正な値を含む場合
	 */
	private float[] parseVector(JsonNode json) {
		JsonNode node = json.get(VECTOR_FIELD);

		if (node == null || node.isNull()) {
			return null;
		}

		if (!node.isArray()) {
			throw new IllegalArgumentException("vector must be an array");
		}

		float[] vector = new float[node.size()];
		for (int i = 0; i < node.size(); i++) {
			vector[i] = (float) node.get(i).asDouble(0.0);
		}
		validateVector(vector);
		return vector;
	}

	/**
	 * 指定フィールドが未登録の場合、DynamicFieldResolver で型を解決してスキーマに登録します。 明示 schema
	 * 済みのフィールドは変更しません。
	 *
	 * @param fieldName   フィールド名
	 * @param multiValued 複数値フィールドの場合 true
	 */
	private void ensureField(String fieldName, boolean multiValued) {
		nlp4j.lucene9.FieldTypeDef type;

		if (schema.contains(fieldName)) {
			type = schema.get(fieldName);
		} else {
			type = dynamicFieldResolver.resolve(fieldName);
		}

		// DATE fields must always be single-valued
		if (type.kind() == nlp4j.lucene9.FieldTypeDef.Kind.DATE && multiValued) {
			throw new LocalSearchException(
					"DATE field must be single-valued: " + fieldName,
					new IllegalArgumentException(
							"DATE field must be single-valued: " + fieldName));
		}

		if (schema.contains(fieldName)) {
			if (multiValued && !type.is_multiValued()) {
				throw new LocalSearchException(
						"Field '" + fieldName + "' is defined as single-valued, "
								+ "but multiple values were provided.",
						new IllegalArgumentException(
								"Field '" + fieldName + "' is single-valued but received an array"));
			}
			return;
		}

		if (multiValued) {
			type = type.multiValued(true);
		}
		schema.addIfAbsent(fieldName, type);
	}

	/**
	 * 検索インデックスを閉じ、すべてのリソースを解放します。 try-with-resources 構文を使用している場合は自動的に呼び出されます。
	 *
	 * @throws LocalSearchException インデックスのクローズに失敗した場合
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
	 * インデックスへの保留中の変更をすべてコミットします。 ドキュメントを追加した後、検索可能にするためにこのメソッドを呼び出す必要があります。
	 *
	 * @throws LocalSearchException コミットに失敗した場合
	 */
	public void commit() {
		try {
			this.index.commit();
		} catch (IOException e) {
			throw new LocalSearchException(e.getMessage(), e);
		}
	}

	/**
	 * 指定した ID のドキュメントをインデックスから削除します。
	 *
	 * <p>
	 * 指定した ID のドキュメントが存在しない場合は何もしません（冪等）。 変更を反映するには {@link #commit()} を呼び出してください。
	 * </p>
	 *
	 * <pre>
	 * search.delete("doc1");
	 * search.commit();
	 * </pre>
	 *
	 * @param id 削除するドキュメントの識別子
	 * @throws LocalSearchException {@code id} が null の場合、またはインデックス操作に失敗した場合
	 */
	public void delete(String id) {
		if (id == null) {
			throw new LocalSearchException("id must not be null", new IllegalArgumentException("id must not be null"));
		}
		try {
			this.index.delete(id);
		} catch (IOException e) {
			throw new LocalSearchException(e.getMessage(), e);
		}
	}

	private static final Set<String> DEFAULT_WORD_FIELDS = Set.of("word", "word.noun", "word.verb", "word.adj",
			"word.adp", "word.aux", "word.sym", "word.propn", "word.num", "word.adv");

	private static void addDefaultWordFields(SearchSchema schema) {
		for (String fieldName : DEFAULT_WORD_FIELDS) {
			schema.addIfAbsent(fieldName, FieldTypeDef.keyword().stored(true).aggregatable(true).multiValued(true));
		}
	}

	private SearchSchema createSchema(Builder builder) {
		SearchSchema schema = new SearchSchema();

		schema.add("id", FieldTypeDef.keyword().stored(true));
		schema.add("body", FieldTypeDef.text().stored(true));
		schema.add("text", FieldTypeDef.text().stored(true));
		schema.add("text_en", FieldTypeDef.text().stored(true));
		schema.add("text_ja", FieldTypeDef.text().stored(true));
		schema.add("data", FieldTypeDef.storedOnly());

		// 形態素解析結果の word.* フィールド（multiValued keyword）
		addDefaultWordFields(schema);

		if (builder.vectorDimension > 0) {
			schema.add(VECTOR_FIELD, FieldTypeDef.knnVector(builder.vectorDimension));
		}

		// 明示フィールド定義（suffix patternより優先）
		for (java.util.Map.Entry<String, FieldTypeDef> entry : builder.fields.entrySet()) {
			schema.add(entry.getKey(), entry.getValue());
		}

		return schema;
	}

	/**
	 * SearchRecord に追加情報を付与します（エンリッチ処理）。
	 *
	 * <p>
	 * DateFieldEnricher は常に実行されます。 {@code autoAnalyze=true} の場合は、language に対応する
	 * SearchRecordEnricher による自然言語解析も実行されます。
	 * </p>
	 *
	 * @param record エンリッチ対象のレコード
	 */
	private void enrich(SearchRecord record) {

		if (record == null) {
			return;
		}

		// --------------------------------------------------
		// Date field enrichment（autoAnalyze に関係なく常に実行）
		// --------------------------------------------------

		dateFieldEnricher.enrich(record);

		// --------------------------------------------------
		// Natural language enrichment
		// --------------------------------------------------

		if (!this.autoAnalyze) {
			return;
		}

		if (record.getBody() == null || record.getBody().isEmpty()) {
			return;
		}

		try {
			textEnricher.enrich(record);
		} catch (Exception e) {
			throw new LocalSearchException("Text enrichment failed: " + e.getMessage(), e);
		}

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
		knn.put("field", VECTOR_FIELD);
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

	private void initIndex(String language) {
		try {
			index = new LuceneIndex(language);
		} catch (IOException e) {
			throw new LocalSearchException(e.getMessage(), e);
		}
	}

	private void initIndex(Path indexDir, String language) {
		try {
			index = new LuceneIndex(indexDir, language);
		} catch (IOException e) {
			throw new LocalSearchException(e.getMessage(), e);
		}
	}

	/**
	 * LocalSearch が使用する基本フィールドが schema に存在することを保証します。
	 * 既存のフィールド定義は変更せず、存在しないフィールドのみ追加します。
	 */
	private static void ensureCoreFields(SearchSchema schema) {
		schema.addIfAbsent("id", FieldTypeDef.keyword().stored(true));
		schema.addIfAbsent("body", FieldTypeDef.text().stored(true));
		schema.addIfAbsent("text", FieldTypeDef.text().stored(true));
		schema.addIfAbsent("text_en", FieldTypeDef.text().stored(true));
		schema.addIfAbsent("text_ja", FieldTypeDef.text().stored(true));
		schema.addIfAbsent("data", FieldTypeDef.storedOnly());
		addDefaultWordFields(schema);
	}

	/**
	 * Merges a persisted schema with builder-specified fields and vectorDimension.
	 * Rules: - Builder-only fields are added to the schema. - Fields present in
	 * both must have identical definitions; otherwise an exception is thrown. - If
	 * builder.vectorDimension > 0 and the persisted schema has no "vector" field, a
	 * KNN_VECTOR field is added to the persisted schema. - If the persisted schema
	 * already has a "vector" field with a different dimension or a non-KNN_VECTOR
	 * kind, an exception is thrown.
	 */
	private static SearchSchema mergeSchemas(SearchSchema persisted, Builder builder) {
		ensureCoreFields(persisted);

		// --- vectorDimension の merge ---
		if (builder.vectorDimension > 0) {
			if (persisted.contains(VECTOR_FIELD)) {
				FieldTypeDef vectorDef = persisted.get(VECTOR_FIELD);
				if (vectorDef.kind() != FieldTypeDef.Kind.KNN_VECTOR) {
					throw new LocalSearchException("Field '" + VECTOR_FIELD
							+ "' exists in schema but is not KNN_VECTOR " + "(kind=" + vectorDef.kind() + ")",
							new IllegalArgumentException("vectorDimension conflict"));
				}
				if (vectorDef.get_dimension() != builder.vectorDimension) {
					throw new LocalSearchException(
							"vectorDimension conflict: builder=" + builder.vectorDimension + ", schema="
									+ vectorDef.get_dimension(),
							new IllegalArgumentException("vectorDimension conflict"));
				}
				// 既存 vector と builder が一致 → OK
			} else {
				// persisted schema に vector フィールドがない → 追加
				persisted.add(VECTOR_FIELD, FieldTypeDef.knnVector(builder.vectorDimension));
			}
		}

		// --- builder.fields の merge ---
		for (java.util.Map.Entry<String, FieldTypeDef> entry : builder.fields.entrySet()) {
			String fieldName = entry.getKey();
			FieldTypeDef builderDef = entry.getValue();
			if (persisted.contains(fieldName)) {
				FieldTypeDef persistedDef = persisted.get(fieldName);
				if (!persistedDef.equals(builderDef)) {
					throw new LocalSearchException(
							"Field definition conflict for '" + fieldName + "': " + "persisted=" + persistedDef.kind()
									+ ", builder=" + builderDef.kind(),
							new IllegalArgumentException("Field definition conflict"));
				}
			} else {
				persisted.add(fieldName, builderDef);
			}
		}
		return persisted;
	}

	/**
	 * Resolves vectorDimension from the builder and schema. Schema wins when
	 * builder is 0; conflict (different non-zero values) throws.
	 */
	private static int resolveVectorDimension(Builder builder, SearchSchema schema) {
		int builderDim = builder.vectorDimension;

		// Find vector field dimension from schema
		int schemaDim = 0;
		if (schema.contains(VECTOR_FIELD)) {
			FieldTypeDef vectorDef = schema.get(VECTOR_FIELD);
			if (vectorDef.kind() == FieldTypeDef.Kind.KNN_VECTOR) {
				schemaDim = vectorDef.get_dimension();
			}
		}

		if (builderDim == 0) {
			return schemaDim; // use schema dimension (may also be 0 = no vector)
		}
		if (schemaDim == 0) {
			return builderDim;
		}
		if (builderDim != schemaDim) {
			throw new LocalSearchException("vectorDimension conflict: builder=" + builderDim + ", schema=" + schemaDim,
					new IllegalArgumentException("vectorDimension conflict"));
		}
		return builderDim;
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
	 * KNN ベクトルの次元数を返します。
	 *
	 * <p>
	 * ベクトルフィールドが無効な場合は 0 を返します。 Python から {@code engine.vector_dimension}
	 * として取得する場合に使用します。
	 * </p>
	 *
	 * @return ベクトルの次元数（0 の場合はベクトルフィールドなし）
	 */
	public int getVectorDimension() {
		return vectorDimension;
	}

	/**
	 * ベクトルフィールドが有効かどうかを返します。
	 *
	 * @return vectorDimension > 0 の場合 {@code true}
	 */
	public boolean hasVectorField() {
		return vectorDimension > 0;
	}

	/**
	 * スキーマに登録されているすべてのフィールド名を、登録順で返します。
	 *
	 * @return フィールド名のリスト
	 */
	public List<String> getFields() {
		return new ArrayList<>(schema.fieldNames());
	}

	/**
	 * スキーマに登録されている集計可能なフィールド名のみを、登録順で返します。 ファセット・集計 UI の構築に利用できます。
	 *
	 * @return {@link nlp4j.lucene9.FieldTypeDef#is_aggregatable()} が {@code true}
	 *         のフィールド名のリスト
	 */
	public List<String> getAggregatableFields() {
		return schema.aggregatableFieldNames();
	}

	/**
	 * 現在のインデックスを指定したディレクトリに保存します。 保存後はインデックスが閉じられます。 スキーマ情報も同ディレクトリに保存されます。
	 *
	 * @param dir インデックスを保存するディレクトリパス
	 * @throws IOException インデックスまたはスキーマの書き込みに失敗した場合
	 */
	public void saveIndexTo(Path dir) throws IOException {
		if (index != null) {
			this.index.writeToAndClose(dir);
			SearchSchemaStore.save(dir, schema);
		}
	}

	/**
	 * 現在のインデックスを指定したディレクトリに保存します。 保存後はインデックスが閉じられます。 スキーマ情報も同ディレクトリに保存されます。
	 *
	 * @param dir インデックスを保存するディレクトリ
	 * @throws IOException インデックスまたはスキーマの書き込みに失敗した場合
	 */
	public void saveIndexTo(File dir) throws IOException {
		saveIndexTo(dir.toPath());
	}

	/**
	 * ベクトル検索を行います。 指定したクエリベクトルに最も近いドキュメントを類似度スコア順で返します。
	 *
	 * <p>
	 * ベクトル類似度による KNN 検索であり、テキストクエリとは独立した検索方式です。
	 * </p>
	 *
	 * @param vector クエリベクトル
	 * @param limit  返す結果の最大件数
	 * @return 類似度スコア順の SearchResult 配列
	 * @throws LocalSearchException 検索に失敗した場合
	 */
	public SearchResult[] searchVector(float[] vector, int limit) {
		validateVector(vector);
		if (limit < 1) {
			throw new LocalSearchException("limit must be greater than 0",
					new IllegalArgumentException("limit must be greater than 0"));
		}
		return executeSearch(createVectorSearchRequest(vector, limit));
	}

	/**
	 * フィールドフィルター付きベクトル検索を行います。 フィルター対象フィールドを持つ文書は
	 * {@link #add(String, float[], java.util.Map)} で登録してください。
	 *
	 * <p>
	 * ベクトル類似度による KNN 検索であり、テキストクエリとは独立した検索方式です。 絞り込みは {@code filters} による keyword
	 * フィールドの完全一致のみ指定できます。
	 * </p>
	 *
	 * <p>
	 * 例:
	 * </p>
	 *
	 * <pre>
	 * SearchResult[] results = search.searchVector(new float[] { 0.9f, 0.1f }, 10,
	 * 		java.util.Map.of("category", "technology", "country", "Japan"));
	 * </pre>
	 *
	 * @param vector  クエリベクトル
	 * @param limit   取得件数の上限
	 * @param filters keyword フィールドの絞り込み条件（フィールド名 → 値）
	 * @return 類似度スコア順の SearchResult 配列
	 * @throws LocalSearchException 検索に失敗した場合
	 */
	public SearchResult[] searchVector(float[] vector, int limit, java.util.Map<String, String> filters) {
		validateVector(vector);
		if (limit < 1) {
			throw new LocalSearchException("limit must be greater than 0",
					new IllegalArgumentException("limit must be greater than 0"));
		}
		return executeSearch(createVectorSearchRequest(vector, limit, filters));
	}

	/**
	 * Lucene Query Parser syntax でインデックスを検索します。
	 * <p>
	 * フィールド名を指定しない語は {@link #getDefaultSearchFields()} が返す
	 * 複数のデフォルトテキストフィールドを対象に検索されます。
	 * </p>
	 * <p>
	 * クエリ文字列は常に <b>Lucene Query Parser syntax</b> として解釈されます。 シンプルなキーワード検索から AND /
	 * OR / フィールド指定・範囲検索まで利用できます。
	 * </p>
	 *
	 * <p>
	 * 例:
	 * </p>
	 *
	 * <pre>
	 * // キーワード検索
	 * search.search("Kyoto", 10);
	 *
	 * // AND 検索
	 * search.search("Kyoto AND historic", 10);
	 *
	 * // フィールド指定
	 * search.search("category:company AND text_en:Kyoto", 10);
	 *
	 * // 数値範囲検索
	 * search.search("year_i:[2020 TO 2026]", 10);
	 * </pre>
	 *
	 * @param query Lucene Query Parser syntax のクエリ文字列
	 * @param limit 返す結果の最大件数
	 * @return 関連度スコア順の SearchResult 配列
	 * @throws LocalSearchException 検索に失敗した場合
	 */
	public SearchResult[] search(String query, int limit) {
		validateSearchArgs(query, limit);
		JsonNode request = JsonNode.object();
		request.put("query", createQueryNode(query));
		request.put("size", limit);

		return executeSearch(request);
	}

	/**
	 * Lucene Query Parser syntax ＋フィールドフィルターでインデックスを検索します。
	 * <p>
	 * フィールド名を指定しない語は {@link #getDefaultSearchFields()} が返す
	 * 複数のデフォルトテキストフィールドを対象に検索されます。
	 * </p>
	 * <p>
	 * クエリ文字列は <b>Lucene Query Parser syntax</b> として解釈されます。 {@code filters}
	 * はスコアリングに影響しない filter 句として適用されます。
	 * </p>
	 *
	 * <p>
	 * 例:
	 * </p>
	 *
	 * <pre>
	 * search.search("text_en:Kyoto", 10, java.util.Map.of("category", "company"));
	 * </pre>
	 *
	 * @param query   Lucene Query Parser syntax のクエリ文字列
	 * @param limit   取得件数の上限
	 * @param filters keyword フィールドの絞り込み条件（フィールド名 → 値）
	 * @return 関連度スコア順の SearchResult 配列
	 * @throws LocalSearchException 検索に失敗した場合
	 */
	public SearchResult[] search(String query, int limit, java.util.Map<String, String> filters) {
		validateSearchArgs(query, limit);
		JsonNode request = JsonNode.object();
		request.put("size", limit);
		request.put("query", createQueryWithFilters(query, toFilterNode(filters)));
		return executeSearch(request);
	}

	/**
	 * OpenSearch Query DSL 形式の JSON 文字列で検索を実行します（低レベル API）。 Python (JPype)
	 * など外部から複雑な検索条件を渡す場合に使用します。
	 *
	 * <p>
	 * このメソッドは OpenSearch 互換の JSON DSL（term / match / bool など）を受け付けます。
	 * {@code query_string} クエリを JSON に含めることで Lucene Query 構文を間接的に利用できます。
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
	 * @param requestJson OpenSearch Query DSL 形式の検索リクエスト JSON 文字列
	 * @return 関連度スコア順の SearchResult 配列
	 * @throws LocalSearchException JSON の解析または検索に失敗した場合
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
	 * OpenSearch Query DSL 形式のリクエストを実行し、OpenSearch 形式のレスポンス JSON をそのまま返します（低レベル
	 * API）。
	 *
	 * <p>
	 * このメソッドは OpenSearch 互換の JSON DSL を受け付けます。 {@code query_string} クエリを JSON
	 * に含めることで Lucene Query 構文を間接的に利用できます。
	 * </p>
	 *
	 * <p>
	 * {@link #searchJson(String)} は結果を {@link SearchResult}[] に変換するため、レスポンスに含まれる
	 * {@code aggregations} などの情報が失われます。このメソッドはレスポンス全体を JSON 文字列として返すため、
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
	 * @throws LocalSearchException JSON の解析または検索に失敗した場合
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
			result.body = resolveResultText(source);

			JsonNode dataNode = source.get("data");
			result.data = (dataNode != null) ? dataNode.asString() : null;

			results[n] = result;
		}

		return results;
	}

	/**
	 * SearchResult.body として返す代表テキストを解決します。
	 *
	 * <p>
	 * 優先順位は {@code body -> text -> default_field_name -> text_ja -> text_en} です。
	 * </p>
	 *
	 * <p>
	 * これは Lucene の {@code body} フィールドそのものを意味するのではなく、 SearchResult
	 * の互換・簡易APIとして代表テキストを取得するための処理です。
	 * </p>
	 */
	private String resolveResultText(JsonNode source) {
		if (source == null) {
			return null;
		}
		// Priority: body -> text -> default_field_name -> text_ja -> text_en
		String[] priority = new String[] { "body", "text", this.default_field_name, "text_ja", "text_en" };
		for (String f : priority) {
			JsonNode node = source.get(f);
			if (node != null && !node.isNull()) {
				String val = node.asString(null);
				if (val != null) {
					return val;
				}
			}
		}
		return null;
	}

	/**
	 * JSON 文字列で aggregation を実行し、OpenSearch 互換形式のレスポンスを返します。
	 *
	 * <p>
	 * {@code "query"} フィールドは <b>Lucene Query Parser syntax</b> として解釈されます。
	 * </p>
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
	 *   "query": "text_en:Kyoto AND country:Japan",
	 *   "filters": {
	 *     "source": "news"
	 *   }
	 * }
	 * </pre>
	 *
	 * @param requestJson aggregation リクエスト JSON 文字列
	 * @return OpenSearch 互換形式の aggregation レスポンス JSON 文字列
	 * @throws LocalSearchException JSON の解析または集計に失敗した場合
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
	 * @throws LocalSearchException 検索に失敗した場合
	 */
	public long count() {
		return count(null, (Map<String, String>) null);
	}

	/**
	 * Lucene Query Parser syntax にマッチするドキュメント件数を返します。
	 * <p>
	 * フィールド名を指定しない語は {@link #getDefaultSearchFields()} が返す
	 * 複数のデフォルトテキストフィールドを対象に検索されます。
	 * </p>
	 * <p>
	 * クエリ文字列は <b>Lucene Query Parser syntax</b> として解釈されます。 null または空文字の場合は全件を返します。
	 * </p>
	 *
	 * @param query Lucene Query Parser syntax のクエリ文字列（null または空文字の場合は全件）
	 * @return マッチするドキュメント件数
	 * @throws LocalSearchException 検索に失敗した場合
	 */
	public long count(String query) {
		return count(query, (Map<String, String>) null);
	}

	/**
	 * Lucene Query Parser syntax ＋フィールドフィルターにマッチするドキュメント件数を返します。
	 * <p>
	 * フィールド名を指定しない語は {@link #getDefaultSearchFields()} が返す
	 * 複数のデフォルトテキストフィールドを対象に検索されます。
	 * </p>
	 * <p>
	 * クエリ文字列は <b>Lucene Query Parser syntax</b> として解釈されます。 {@code filters}
	 * はスコアリングに影響しない filter 句として適用されます。
	 * </p>
	 *
	 * @param query   Lucene Query Parser syntax のクエリ文字列（null または空文字の場合は全件）
	 * @param filters keyword フィールドの絞り込み条件（フィールド名 → 値）
	 * @return マッチするドキュメント件数
	 * @throws LocalSearchException 検索に失敗した場合
	 */
	public long count(String query, Map<String, String> filters) {
		JsonNode searchRequest = createCountRequest(query, toFilterNode(filters));
		return toTotalHits(executeRequest(searchRequest));
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
	 * @throws LocalSearchException 集計に失敗した場合
	 */
	public Map<String, Long> aggregate(String field, int size) {
		return aggregate(field, null, size, null);
	}

	/**
	 * Lucene Query Parser syntax で絞り込んだ上で、指定フィールドの terms aggregation を実行します。
	 * <p>
	 * フィールド名を指定しない語は {@link #getDefaultSearchFields()} が返す
	 * 複数のデフォルトテキストフィールドを対象に検索されます。
	 * </p>
	 * <p>
	 * クエリ文字列は <b>Lucene Query Parser syntax</b> として解釈されます。
	 * </p>
	 *
	 * @param field 集計対象フィールド名
	 * @param query Lucene Query Parser syntax のクエリ文字列（null または空文字の場合は全件）
	 * @param size  返すバケット数の上限
	 * @return フィールド値 → ドキュメント件数のマップ（件数降順）
	 * @throws LocalSearchException 集計に失敗した場合
	 */
	public Map<String, Long> aggregate(String field, String query, int size) {
		return aggregate(field, query, size, null);
	}

	/**
	 * Lucene Query Parser syntax ＋フィールドフィルターで絞り込んだ上で、指定フィールドの terms aggregation
	 * を実行します。
	 * <p>
	 * フィールド名を指定しない語は {@link #getDefaultSearchFields()} が返す
	 * 複数のデフォルトテキストフィールドを対象に検索されます。
	 * </p>
	 * <p>
	 * クエリ文字列は <b>Lucene Query Parser syntax</b> として解釈されます。 {@code filters}
	 * はスコアリングに影響しない filter 句として適用されます。
	 * </p>
	 *
	 * @param field   集計対象フィールド名
	 * @param query   Lucene Query Parser syntax のクエリ文字列（null または空文字の場合は全件）
	 * @param size    返すバケット数の上限
	 * @param filters keyword フィールドの絞り込み条件（null または空の場合はスキップ）
	 * @return フィールド値 → ドキュメント件数のマップ（件数降順）
	 * @throws LocalSearchException 集計に失敗した場合
	 */
	public Map<String, Long> aggregate(String field, String query, int size, Map<String, String> filters) {
		validateAggregatableField(field);
		JsonNode searchRequest = createAggregationRequest("values", field, query, size, toFilterNode(filters));
		JsonNode response = executeRequest(searchRequest);
		return toAggregationMap("values", response);
	}

	// -----------------------------------------------------------------------
	// Java API: dateHistogram()
	// -----------------------------------------------------------------------

	/**
	 * 全ドキュメントを対象に date histogram aggregation を実行します。
	 *
	 * @param field    集計対象の DATE フィールド名（例: {@code "created_dt"}）
	 * @param interval 集計単位（YEAR / MONTH / HOUR）
	 * @return 時刻昇順の {@link nlp4j.lucene9.DateHistogramBucket} リスト
	 * @throws LocalSearchException 集計に失敗した場合
	 */
	public List<nlp4j.lucene9.DateHistogramBucket> dateHistogram(
			String field,
			nlp4j.lucene9.DateHistogramInterval interval) {
		return dateHistogram(field, interval, null, null);
	}

	/**
	 * Lucene Query Parser syntax で絞り込んだ上で date histogram aggregation を実行します。
	 *
	 * @param field    集計対象の DATE フィールド名
	 * @param interval 集計単位（YEAR / MONTH / HOUR）
	 * @param query    Lucene Query Parser syntax のクエリ文字列（null または空文字の場合は全件）
	 * @return 時刻昇順の {@link nlp4j.lucene9.DateHistogramBucket} リスト
	 * @throws LocalSearchException 集計に失敗した場合
	 */
	public List<nlp4j.lucene9.DateHistogramBucket> dateHistogram(
			String field,
			nlp4j.lucene9.DateHistogramInterval interval,
			String query) {
		return dateHistogram(field, interval, query, null);
	}

	/**
	 * Lucene Query Parser syntax ＋フィールドフィルターで絞り込んだ上で date histogram aggregation を実行します。
	 *
	 * @param field    集計対象の DATE フィールド名
	 * @param interval 集計単位（YEAR / MONTH / HOUR）
	 * @param query    Lucene Query Parser syntax のクエリ文字列（null または空文字の場合は全件）
	 * @param filters  keyword フィールドの絞り込み条件（null または空の場合はスキップ）
	 * @return 時刻昇順の {@link nlp4j.lucene9.DateHistogramBucket} リスト
	 * @throws LocalSearchException 集計に失敗した場合
	 */
	public List<nlp4j.lucene9.DateHistogramBucket> dateHistogram(
			String field,
			nlp4j.lucene9.DateHistogramInterval interval,
			String query,
			Map<String, String> filters) {

		if (field == null || field.isBlank()) {
			throw new LocalSearchException("field must not be blank",
					new IllegalArgumentException("field must not be blank"));
		}
		if (interval == null) {
			throw new LocalSearchException("interval must not be null",
					new IllegalArgumentException("interval must not be null"));
		}

		try {
			JsonNode request = createDateHistogramRequest(field, interval, query, toFilterNode(filters));
			JsonNode response = executeRequest(request);
			return toDateHistogramBuckets("values", response);
		} catch (LocalSearchException e) {
			throw e;
		} catch (Exception e) {
			throw new LocalSearchException(e.getMessage(), e);
		}
	}

	private JsonNode createDateHistogramRequest(
			String field,
			nlp4j.lucene9.DateHistogramInterval interval,
			String query,
			JsonNode filters) {

		JsonNode root = JsonNode.object();
		root.put("size", 0);

		boolean hasQuery = query != null && !query.isEmpty();
		boolean hasFilters = filters != null && !filters.isNull() && filters.size() > 0;

		if (hasQuery || hasFilters) {
			root.put("query", createQueryWithFilters(hasQuery ? query : null, filters));
		}

		// Build date_histogram agg
		JsonNode histogram = JsonNode.object();
		histogram.put("field", field);
		histogram.put("calendar_interval", interval.value());

		JsonNode aggBody = JsonNode.object();
		aggBody.put("date_histogram", histogram);

		JsonNode aggs = JsonNode.object();
		aggs.put("values", aggBody);

		root.put("aggs", aggs);
		return root;
	}

	private List<nlp4j.lucene9.DateHistogramBucket> toDateHistogramBuckets(
			String aggName, JsonNode response) {

		JsonNode bucketsNode = response
				.get("aggregations")
				.get(aggName)
				.get("buckets");

		List<nlp4j.lucene9.DateHistogramBucket> result = new ArrayList<>();

		for (JsonNode b : bucketsNode.asList()) {
			long key = b.get("key").asLong(0);
			String keyAsString = b.get("key_as_string").asString();
			long docCount = b.get("doc_count").asLong(0);
			result.add(new nlp4j.lucene9.DateHistogramBucket(key, keyAsString, docCount));
		}

		return result;
	}


	// -----------------------------------------------------------------------
	// Java API: validateQuery()
	// -----------------------------------------------------------------------

	/**
	 * Lucene Query Parser syntax を検証します。
	 * <p>
	 * フィールド名を指定しない語は {@link #getDefaultSearchFields()} が返す
	 * 複数のデフォルトテキストフィールドを対象に検索されます。
	 * </p>
	 * <p>
	 * このメソッドはクエリを実行せず、Lucene QueryParser で正常に解析できるかどうかだけを確認します。
	 * {@link #search(String, int)} などに渡す前に構文チェックする用途に使用してください。
	 * </p>
	 *
	 * <pre>
	 * LuceneQueryValidationResult result = search.validateQuery("京都 AND (寺院 OR 神社)");
	 *
	 * if (!result.isValid()) {
	 * 	System.out.println(result.getMessage());
	 * }
	 * </pre>
	 *
	 * @param query Lucene Query Parser syntax のクエリ文字列
	 * @return 検証結果
	 */
	public LuceneQueryValidationResult validateQuery(String query) {

		try (nlp4j.lucene9.SearchSession session = index.acquireSearcher()) {

			nlp4j.lucene9.LuceneQueryBuilder.parseQueryString(query, getDefaultSearchFields().toArray(new String[0]),
					session.getAnalyzer(), this.schema, this.zoneId);

			return LuceneQueryValidationResult.valid();

		} catch (Exception e) {

			return LuceneQueryValidationResult.invalid(e.getMessage());
		}
	}

	/**
	 * フィールド指定のないテキストクエリで検索対象となる デフォルトフィールドを返します。
	 *
	 * <ul>
	 * <li>{@code ja}: {@code text_ja}, {@code text}, {@code body}</li>
	 * <li>{@code en}: {@code text_en}, {@code text}, {@code body}</li>
	 * <li>その他: {@code text}, {@code body}</li>
	 * </ul>
	 *
	 * <p>
	 * {@code text_en:Kyoto} のようにフィールド名を明示した場合は、 この一覧に含まれないフィールドも検索できます。
	 * </p>
	 *
	 * @return デフォルト検索フィールドのリスト
	 */
	public List<String> getDefaultSearchFields() {
		if ("ja".equals(this.language)) {
			return List.of("text_ja", "text", "body");
		} else if ("en".equals(this.language)) {
			return List.of("text_en", "text", "body");
		} else {
			return List.of("text", "body");
		}
	}

	// -----------------------------------------------------------------------
	// Private helpers
	// -----------------------------------------------------------------------

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

	/**
	 * aggregation 用の共通フィルタ配列を構築します。
	 */
	private JsonNode buildFilterArray(JsonNode filters) {
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
		return filter;
	}

	/**
	 * aggregation 用の terms 句を構築します。
	 */
	private JsonNode buildAggregationsNode(String aggregationName, String field, int size) {
		JsonNode terms = JsonNode.object();
		terms.put("field", field);
		terms.put("size", size);

		JsonNode aggregation = JsonNode.object();
		aggregation.put("terms", terms);

		JsonNode aggregations = JsonNode.object();
		aggregations.put(aggregationName, aggregation);
		return aggregations;
	}

	/**
	 * query_string クエリ（Lucene Query Parser syntax）の JsonNode を生成します。
	 */
	private JsonNode createQueryNode(String query) {
		JsonNode queryString = JsonNode.object();
		queryString.put("query", query);
		JsonNode fieldsArray = JsonNode.array();
		for (String f : getDefaultSearchFields()) {
			fieldsArray.add(f);
		}
		queryString.put("fields", fieldsArray);
		return JsonNode.object().put("query_string", queryString);
	}

	/**
	 * Lucene Query ＋ filters の query 部分を生成します（search / count / aggregation 共通）。
	 *
	 * @param query   Lucene Query Parser syntax のクエリ文字列
	 * @param filters keyword フィールドの絞り込み条件（null の場合はスキップ）
	 * @return query 部分の JsonNode
	 */
	private JsonNode createQueryWithFilters(String query, JsonNode filters) {
		boolean hasQuery = query != null && !query.isEmpty();
		boolean hasFilters = filters != null && !filters.isNull() && filters.size() > 0;

		if (hasFilters) {
			JsonNode boolQuery = JsonNode.object();

			if (hasQuery) {
				JsonNode must = JsonNode.array();
				must.add(createQueryNode(query));
				boolQuery.put("must", must);
			}

			JsonNode filter = buildFilterArray(filters);
			if (filter.size() > 0) {
				boolQuery.put("filter", filter);
			}

			return JsonNode.object().put("bool", boolQuery);
		} else if (hasQuery) {
			return createQueryNode(query);
		} else {
			// match_all
			return JsonNode.object().put("match_all", JsonNode.object());
		}
	}

	/**
	 * count() 用のリクエストを生成します（aggs なし、size=0）。 query が null または空文字の場合は match_all
	 * になります。
	 *
	 * @param query   Lucene Query Parser syntax のクエリ文字列（null または空文字の場合は match_all）
	 * @param filters keyword フィールドの絞り込み条件（null の場合はスキップ）
	 * @return count 用リクエスト JsonNode
	 */
	private JsonNode createCountRequest(String query, JsonNode filters) {
		JsonNode root = JsonNode.object();
		root.put("size", 0);
		boolean hasQuery = query != null && !query.isEmpty();
		if (hasQuery || (filters != null && !filters.isNull() && filters.size() > 0)) {
			root.put("query", createQueryWithFilters(hasQuery ? query : null, filters));
		}
		return root;
	}

	/**
	 * aggregation リクエストを生成します。 query は Lucene Query Parser syntax として解釈されます。
	 */
	private JsonNode createAggregationRequest(String aggregationName, String field, String query, int size,
			JsonNode filters) {

		JsonNode root = JsonNode.object();
		root.put("size", 0);

		boolean hasQuery = query != null && !query.isEmpty();
		boolean hasFilters = filters != null && !filters.isNull() && filters.size() > 0;

		if (hasQuery || hasFilters) {
			root.put("query", createQueryWithFilters(hasQuery ? query : null, filters));
		}

		root.put("aggs", buildAggregationsNode(aggregationName, field, size));

		return root;
	}

	/**
	 * search() 系メソッドの入口で query と limit を検証します。
	 */
	private void validateSearchArgs(String query, int limit) {
		if (query == null || query.isBlank()) {
			throw new LocalSearchException("query must not be blank",
					new IllegalArgumentException("query must not be blank"));
		}
		if (limit < 1) {
			throw new LocalSearchException("limit must be greater than 0",
					new IllegalArgumentException("limit must be greater than 0"));
		}
	}

	/**
	 * aggregate() 系メソッドの入口で field を検証します。 aggregatable
	 * でないフィールドを指定した場合に明快なエラーメッセージを返します。
	 */
	private void validateAggregatableField(String field) {
		if (field == null || field.isBlank()) {
			throw new LocalSearchException("field must not be blank",
					new IllegalArgumentException("field must not be blank"));
		}
		if (!schema.contains(field)) {
			// 動的フィールドは未登録の場合があるため、存在しないフィールドは警告せず通過させる
			return;
		}
		if (!schema.get(field).is_aggregatable()) {
			throw new LocalSearchException("Field is not aggregatable: " + field,
					new IllegalArgumentException("Field is not aggregatable: " + field));
		}
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

	SearchSchema getSchema() {
		return schema;
	}

	@Override
	public String toString() {
		return "LocalSearch [language=" + language + ", autoAnalyze=" + autoAnalyze + ", default_field_name="
				+ default_field_name + ", schema=" + schema + ", index=" + index + "]";
	}
}
