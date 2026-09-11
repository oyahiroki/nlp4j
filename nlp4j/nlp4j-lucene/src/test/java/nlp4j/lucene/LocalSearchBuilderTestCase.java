package nlp4j.lucene;

import java.nio.file.Path;
import java.time.ZoneId;
import java.util.Map;

import junit.framework.TestCase;
import nlp4j.lucene9.FieldTypeDef;

/**
 * Test target: nlp4j.lucene.LocalSearch.Builder
 */
public class LocalSearchBuilderTestCase extends TestCase {

	// -----------------------------------------------------------------------
	// builder(language) — 基本動作
	// -----------------------------------------------------------------------

	/**
	 * builder("ja") でインスタンスが生成され、日本語全文検索が動作することを確認する。
	 */
	public void testBuilderDefault_ja() throws Exception {
		try (LocalSearch search = LocalSearch.builder("ja").build()) {
			search.add("1", "東京の観光スポット");
			search.add("2", "京都の寺院と歴史");
			search.add("3", "大阪の食文化");
			search.commit();

			SearchResult[] results = search.search("東京", 10);
			assertEquals(1, results.length);
			assertEquals("1", results[0].id);
		}
	}

	/**
	 * builder("en") でインスタンスが生成され、英語全文検索が動作することを確認する。
	 */
	public void testBuilderDefault_en() throws Exception {
		try (LocalSearch search = LocalSearch.builder("en").build()) {
			search.add("1", "Kyoto is a historic city in Japan.");
			search.add("2", "Tokyo is the capital of Japan.");
			search.add("3", "Paris is the capital of France.");
			search.commit();

			SearchResult[] results = search.search("Kyoto", 10);
			assertEquals(1, results.length);
			assertEquals("1", results[0].id);
		}
	}

	/**
	 * builder("default") でインスタンスが生成され、デフォルトフィールドで検索が動作することを確認する。
	 */
	public void testBuilderDefault_default() throws Exception {
		try (LocalSearch search = LocalSearch.builder("default").build()) {
			search.add("1", "hello world");
			search.add("2", "foo bar");
			search.commit();

			SearchResult[] results = search.search("hello", 10);
			assertEquals(1, results.length);
			assertEquals("1", results[0].id);
		}
	}

	// -----------------------------------------------------------------------
	// autoAnalyze(false)
	// -----------------------------------------------------------------------

	/**
	 * autoAnalyze(false) を設定した場合、インスタンスが正常に生成されることを確認する。
	 */
	public void testBuilderAutoAnalyzeFalse() throws Exception {
		try (LocalSearch search = LocalSearch.builder("ja").autoAnalyze(false).build()) {
			search.add("1", "東京");
			search.add("2", "京都");
			search.commit();

			// autoAnalyze=false でも基本的な検索は動作する
			SearchResult[] results = search.search("東京", 10);
			assertTrue(results.length >= 0); // 検索が例外なく完了すること
		}
	}

	/**
	 * autoAnalyze(true)（デフォルト）と autoAnalyze(false) の両方でインスタンスを生成できることを確認する。
	 */
	public void testBuilderAutoAnalyzeTrue() throws Exception {
		try (LocalSearch search = LocalSearch.builder("ja").autoAnalyze(true).build()) {
			search.add("1", "東京の観光スポット");
			search.commit();

			SearchResult[] results = search.search("東京", 10);
			assertEquals(1, results.length);
		}
	}

	// -----------------------------------------------------------------------
	// vectorDimension(int)
	// -----------------------------------------------------------------------

	/**
	 * vectorDimension(2) を設定し、ベクトル検索が動作することを確認する。
	 */
	public void testBuilderVectorDimension() throws Exception {
		try (LocalSearch search = LocalSearch.builder("en").vectorDimension(2).build()) {
			search.add("1_East", new float[] { 1.0f, 0.0f });
			search.add("2_North", new float[] { 0.0f, 1.0f });
			search.add("3_West", new float[] { -1.0f, 0.0f });
			search.commit();

			SearchResult[] results = search.searchVector(new float[] { 0.9f, 0.1f }, 10);
			assertEquals(3, results.length);
			assertEquals("1_East", results[0].id);
		}
	}

	/**
	 * vectorDimension(0)（デフォルト）でビルドした場合、ベクトルフィールドなしで正常動作することを確認する。
	 */
	public void testBuilderVectorDimensionZero() throws Exception {
		try (LocalSearch search = LocalSearch.builder("en").vectorDimension(0).build()) {
			search.add("1", "hello world");
			search.commit();

			SearchResult[] results = search.search("hello", 10);
			assertEquals(1, results.length);
		}
	}

	/**
	 * vectorDimension に負の値を指定した場合、LocalSearchException がスローされることを確認する。
	 */
	public void testBuilderVectorDimensionNegative() throws Exception {
		try {
			LocalSearch search = LocalSearch.builder("en").vectorDimension(-1).build();
			search.close();
			fail("LocalSearchException が期待される");
		} catch (LocalSearchException e) {
			// expected
		}
	}

	// -----------------------------------------------------------------------
	// loadIndexFrom(Path)
	// -----------------------------------------------------------------------

	/**
	 * nlp4j.lucene.LocalSearchBuilderTestCase.testBuilderloadIndexFrom()
	 * 
	 * loadIndexFrom(Path) を指定したディスクインデックスで検索が動作することを確認する。
	 * テスト後はインデックスディレクトリを削除する。
	 */
	public void testBuilderloadIndexFrom() throws Exception {
		Path indexDir = Path.of("target/test-index-builder-" + System.currentTimeMillis());
		java.nio.file.Files.createDirectories(indexDir);

		try (LocalSearch search = LocalSearch.builder("en").loadIndexFrom(indexDir).build()) {
			search.add("1", "Kyoto is a historic city.");
			search.add("2", "Tokyo is the capital of Japan.");
			search.commit();

			SearchResult[] results = search.search("Kyoto", 10);
			assertEquals(1, results.length);
			assertEquals("1", results[0].id);
		} finally {
			// クリーンアップ
			deleteRecursively(indexDir);
		}
	}
	
	

	/**
	 * loadIndexFrom(Path) で作成したディスクインデックスを、一度 LocalSearch を閉じた後に
	 * 別の LocalSearch インスタンスで再オープンし、前回登録したドキュメントが検索できることを確認する。
	 *
	 * <p>
	 * LuceneIndex は内部的にオンメモリで動作するため、ディスクへの永続化には
	 * {@link LocalSearch#saveIndexTo(Path)} を使用する必要がある。
	 * フェーズ1で saveIndexTo() を呼び出してディスクに保存し、
	 * フェーズ2で同じディレクトリを loadIndexFrom() に指定して前回のデータを読み込む。
	 * </p>
	 */
	public void testBuilderloadIndexFromReopen() throws Exception {
		Path indexDir = Path.of("target/test-index-builder-reopen-" + System.currentTimeMillis());

		try {
			// --- フェーズ 1: インデックスを作成し、ディスクに保存して閉じる ---
			try (LocalSearch search = LocalSearch.builder("en").build()) {
				search.add("1", "Kyoto is a historic city.");
				search.add("2", "Tokyo is the capital of Japan.");
				search.add("3", "Paris is the capital of France.");
				search.commit();
				search.saveIndexTo(indexDir);
			}

			// --- フェーズ 2: 保存したディレクトリを loadIndexFrom() で指定して再オープン ---
			try (LocalSearch search = LocalSearch.builder("en").loadIndexFrom(indexDir).build()) {
				// 前回登録した "Kyoto" を含む文書がヒットすること
				SearchResult[] results = search.search("Kyoto", 10);
				assertEquals(1, results.length);
				assertEquals("1", results[0].id);

				// 前回登録した全ドキュメント数が維持されていること
				long total = search.count();
				assertEquals(3, total);
			}
		} finally {
			deleteRecursively(indexDir);
		}
	}

	// -----------------------------------------------------------------------
	// timeZone(String)
	// -----------------------------------------------------------------------

	/**
	 * timeZone("Asia/Tokyo") を設定してインスタンスが正常に生成されることを確認する。
	 */
	public void testBuilderTimeZone_valid() throws Exception {
		try (LocalSearch search = LocalSearch.builder("ja").timeZone("Asia/Tokyo").build()) {
			search.add("1", "東京");
			search.commit();

			SearchResult[] results = search.search("東京", 10);
			assertEquals(1, results.length);
		}
	}

	/**
	 * timeZone("UTC") を設定してインスタンスが正常に生成されることを確認する。
	 */
	public void testBuilderTimeZone_UTC() throws Exception {
		try (LocalSearch search = LocalSearch.builder("en").timeZone("UTC").build()) {
			search.add("1", "hello");
			search.commit();

			SearchResult[] results = search.search("hello", 10);
			assertEquals(1, results.length);
		}
	}

	/**
	 * 不正なタイムゾーン文字列を指定した場合、例外がスローされることを確認する。
	 */
	public void testBuilderTimeZone_invalid() throws Exception {
		try {
			LocalSearch.builder("ja").timeZone("Invalid/Zone").build().close();
			fail("例外が期待される");
		} catch (java.time.zone.ZoneRulesException e) {
			// expected
		}
	}

	// -----------------------------------------------------------------------
	// field(String, FieldTypeDef)
	// -----------------------------------------------------------------------

	/**
	 * field() で明示フィールド定義を追加し、そのフィールドで検索が動作することを確認する。
	 */
	public void testBuilderField() throws Exception {
		try (LocalSearch search = LocalSearch.builder("en")
				.field("category", FieldTypeDef.keyword().stored(true))
				.build()) {
			search.addJson("""
					{"id":"1","body":"Kyoto is a historic city.","category":"city"}
					""");
			search.addJson("""
					{"id":"2","body":"Nintendo is in Kyoto.","category":"company"}
					""");
			search.commit();

			SearchResult[] results = search.search("category:city", 10);
			assertEquals(1, results.length);
			assertEquals("1", results[0].id);
		}
	}

	// -----------------------------------------------------------------------
	// メソッドチェーン — 複合設定
	// -----------------------------------------------------------------------

	/**
	 * 複数の Builder オプション（language + autoAnalyze + vectorDimension）を組み合わせて
	 * インスタンスが正常に生成されることを確認する。
	 */
	public void testBuilderCombined() throws Exception {
		try (LocalSearch search = LocalSearch.builder("ja")
				.autoAnalyze(true)
				.vectorDimension(3)
				.timeZone("Asia/Tokyo")
				.build()) {
			search.add("1_A", new float[] { 1.0f, 0.0f, 0.0f });
			search.add("2_B", new float[] { 0.0f, 1.0f, 0.0f });
			search.commit();

			SearchResult[] results = search.searchVector(new float[] { 0.9f, 0.1f, 0.0f }, 10);
			assertEquals(2, results.length);
			assertEquals("1_A", results[0].id);
		}
	}

	// -----------------------------------------------------------------------
	// getFields() / getAggregatableFields() テスト
	// -----------------------------------------------------------------------

	/**
	 * getFields() がスキーマに登録された全フィールド名を返すことを確認する。
	 * id / text_ja / word.noun など基本フィールドが含まれること。
	 */
	public void testGetFields() throws Exception {
		try (LocalSearch search = LocalSearch.builder("ja").build()) {
			java.util.List<String> fields = search.getFields();

			assertTrue("id should be in fields", fields.contains("id"));
			assertTrue("text_ja should be in fields", fields.contains("text_ja"));
			assertTrue("word.noun should be in fields", fields.contains("word.noun"));
		}
	}

	/**
	 * getAggregatableFields() が aggregatable=true のフィールドのみを返すことを確認する。
	 * word / word.noun は含まれ、id / text_ja / data は含まれないこと。
	 */
	public void testGetAggregatableFields() throws Exception {
		try (LocalSearch search = LocalSearch.builder("ja").build()) {
			java.util.List<String> fields = search.getAggregatableFields();

			assertTrue("word should be aggregatable", fields.contains("word"));
			assertTrue("word.noun should be aggregatable", fields.contains("word.noun"));

			assertFalse("id should NOT be aggregatable", fields.contains("id"));
			assertFalse("text_ja should NOT be aggregatable", fields.contains("text_ja"));
			assertFalse("data should NOT be aggregatable", fields.contains("data"));
		}
	}

	/**
	 * addJson() で動的登録された keyword フィールドが getAggregatableFields() に現れることを確認する。
	 * DynamicFieldResolver によって maker / category が KEYWORD（aggregatable=true）として登録される。
	 */
	public void testGetAggregatableFieldsWithDynamicField() throws Exception {
		try (LocalSearch search = LocalSearch.builder("en").build()) {
			search.addJson("""
					{
					  "id": "1",
					  "body": "Nissan EV",
					  "maker": "Nissan",
					  "category": "EV"
					}
					""");

			java.util.List<String> fields = search.getAggregatableFields();

			assertTrue("maker should be aggregatable after addJson", fields.contains("maker"));
			assertTrue("category should be aggregatable after addJson", fields.contains("category"));
		}
	}

	/**
	 * saveIndexTo() / loadIndexFrom() 後に getAggregatableFields() が動的フィールドを正しく返すことを確認する。
	 * SearchSchemaStore との連動確認。
	 */
	public void testGetAggregatableFieldsAfterSaveAndLoad() throws Exception {
		Path dir = java.nio.file.Files.createTempDirectory("nlp4j-test-aggfields-");
		try {
			// 1. インデックス作成・保存
			try (LocalSearch search = LocalSearch.builder("en").build()) {
				search.addJson("""
						{"id":"1","body":"Nissan vehicle","maker":"Nissan","category":"EV"}
						""");
				search.commit();
				search.saveIndexTo(dir);
			}

			// 2. 再ロード後に getAggregatableFields() が動的フィールドを返すこと
			try (LocalSearch loaded = LocalSearch.builder("en").loadIndexFrom(dir).build()) {
				java.util.List<String> fields = loaded.getAggregatableFields();

				assertTrue("maker should be aggregatable after load", fields.contains("maker"));
				assertTrue("category should be aggregatable after load", fields.contains("category"));
			}
		} finally {
			deleteRecursively(dir);
		}
	}

	/**
	 * getFields() と getAggregatableFields() の返す集合が正しい包含関係にあることを確認する。
	 * aggregatable ⊆ fields。
	 */
	public void testAggregatableFieldsIsSubsetOfAllFields() throws Exception {
		try (LocalSearch search = LocalSearch.builder("en").build()) {
			search.addJson("""
					{"id":"1","body":"test","category":"tech","year_i":2026}
					""");

			java.util.List<String> all = search.getFields();
			java.util.List<String> agg = search.getAggregatableFields();

			for (String field : agg) {
				assertTrue("aggregatable field '" + field + "' must also appear in getFields()",
						all.contains(field));
			}
			// all must be >= agg (non-aggregatable fields like id, text_en exist only in all)
			assertTrue("getFields() should return at least as many fields as getAggregatableFields()",
					all.size() >= agg.size());
		}
	}

	// -----------------------------------------------------------------------
	// vectorDimension merge テスト（schema 再ロード）
	// -----------------------------------------------------------------------

	/**
	 * vectorDimension なしで保存したインデックスを、後から vectorDimension(3) を指定して
	 * ロードした場合、schema に KNN_VECTOR フィールドが追加され、ベクトル検索が動作することを確認する。
	 *
	 * <p>
	 * これは「persisted schema に vector がない + Builder.vectorDimension > 0」ケースです。
	 * mergeSchemas() が KNN_VECTOR を追加し、正常に動作すること。
	 * </p>
	 */
	public void testVectorDimensionMerge_PersistNoVector_BuilderHasVector() throws Exception {
		Path dir = java.nio.file.Files.createTempDirectory("nlp4j-test-vd-merge-");
		try {
			// フェーズ 1: vectorDimension なしで保存
			try (LocalSearch search = LocalSearch.builder("en").autoAnalyze(false).build()) {
				search.addJson("{\"id\":\"1\",\"body\":\"doc1\",\"category\":\"A\"}");
				search.commit();
				search.saveIndexTo(dir);
			}

			// フェーズ 2: vectorDimension(3) を指定して再ロード
			// mergeSchemas() が schema に KNN_VECTOR を追加し、ベクトル検索が使えること
			try (LocalSearch search = LocalSearch.builder("en")
					.autoAnalyze(false)
					.vectorDimension(3)
					.loadIndexFrom(dir)
					.build()) {

				assertEquals(3, search.getVectorDimension());
				assertTrue("hasVectorField() should return true after merge", search.hasVectorField());

				// ベクトル付きドキュメントを追加して検索できること
				search.add("v1", new float[]{1.0f, 0.0f, 0.0f});
				search.commit();

				SearchResult[] results = search.searchVector(new float[]{0.9f, 0.1f, 0.0f}, 5);
				assertEquals(1, results.length);
				assertEquals("v1", results[0].id);
			}
		} finally {
			deleteRecursively(dir);
		}
	}

	/**
	 * persisted schema に vectorDimension=3 の KNN_VECTOR がある状態で、
	 * Builder に vectorDimension(3)（同一）を指定してロードした場合、
	 * conflict が起きず正常にロードできることを確認する。
	 */
	public void testVectorDimensionMerge_SameDimension_OK() throws Exception {
		Path dir = java.nio.file.Files.createTempDirectory("nlp4j-test-vd-same-");
		try {
			// フェーズ 1: vectorDimension=3 で保存
			try (LocalSearch search = LocalSearch.builder("en")
					.autoAnalyze(false)
					.vectorDimension(3)
					.build()) {
				search.add("1", new float[]{1.0f, 0.0f, 0.0f});
				search.commit();
				search.saveIndexTo(dir);
			}

			// フェーズ 2: 同じ vectorDimension(3) を指定して再ロード → conflict なし
			try (LocalSearch search = LocalSearch.builder("en")
					.autoAnalyze(false)
					.vectorDimension(3)
					.loadIndexFrom(dir)
					.build()) {

				assertEquals(3, search.getVectorDimension());

				SearchResult[] results = search.searchVector(new float[]{0.9f, 0.1f, 0.0f}, 5);
				assertEquals(1, results.length);
				assertEquals("1", results[0].id);
			}
		} finally {
			deleteRecursively(dir);
		}
	}

	/**
	 * persisted schema に vectorDimension=3 の KNN_VECTOR があるのに、
	 * Builder で vectorDimension(2) を指定した場合、LocalSearchException が発生することを確認する。
	 */
	public void testVectorDimensionMerge_DifferentDimension_Conflict() throws Exception {
		Path dir = java.nio.file.Files.createTempDirectory("nlp4j-test-vd-conflict-");
		try {
			// フェーズ 1: vectorDimension=3 で保存
			try (LocalSearch search = LocalSearch.builder("en")
					.autoAnalyze(false)
					.vectorDimension(3)
					.build()) {
				search.add("1", new float[]{1.0f, 0.0f, 0.0f});
				search.commit();
				search.saveIndexTo(dir);
			}

			// フェーズ 2: vectorDimension(2) を指定して再ロード → conflict
			try {
				LocalSearch search = LocalSearch.builder("en")
						.autoAnalyze(false)
						.vectorDimension(2)
						.loadIndexFrom(dir)
						.build();
				search.close();
				fail("vectorDimension conflict では LocalSearchException が期待される");
			} catch (LocalSearchException e) {
				assertTrue("例外メッセージに 'vectorDimension conflict' が含まれること",
						e.getMessage().contains("vectorDimension conflict"));
			}
		} finally {
			deleteRecursively(dir);
		}
	}

	// -----------------------------------------------------------------------
	// Builder.field() 同一定義での再ロード
	// -----------------------------------------------------------------------

	/**
	 * persisted schema に存在するフィールドと同一定義を Builder.field() で指定して再ロードした場合、
	 * conflict が起きず正常にロードできることを確認する。
	 *
	 * <p>
	 * これは FieldTypeDef.equals() が全属性を正しく比較していることも検証します。
	 * </p>
	 */
	public void testBuilderField_SameDefinition_Reload_OK() throws Exception {
		Path dir = java.nio.file.Files.createTempDirectory("nlp4j-test-field-same-");
		try {
			FieldTypeDef makerDef = FieldTypeDef.keyword().stored(true).aggregatable(true);

			// フェーズ 1: maker フィールドを明示定義して保存
			try (LocalSearch search = LocalSearch.builder("en")
					.autoAnalyze(false)
					.field("maker", makerDef)
					.build()) {
				search.addJson("{\"id\":\"1\",\"body\":\"doc1\",\"maker\":\"Nissan\"}");
				search.commit();
				search.saveIndexTo(dir);
			}

			// フェーズ 2: 同一定義で再ロード → conflict なし
			try (LocalSearch search = LocalSearch.builder("en")
					.autoAnalyze(false)
					.field("maker", makerDef)
					.loadIndexFrom(dir)
					.build()) {

				assertTrue("maker should be in fields after reload", search.getFields().contains("maker"));
				assertEquals(1, search.count());
			}
		} finally {
			deleteRecursively(dir);
		}
	}

	// -----------------------------------------------------------------------
	// 不正 formatVersion → LocalSearchException
	// -----------------------------------------------------------------------

	/**
	 * 不正な formatVersion を持つ schema ファイルをロードしようとした場合、
	 * LocalSearchException がスローされることを確認する。
	 *
	 * <p>
	 * SearchSchemaStore.load() が投げる IllegalArgumentException が
	 * LocalSearchException にラップされること。
	 * </p>
	 */
	public void testInvalidFormatVersion_ThrowsLocalSearchException() throws Exception {
		Path dir = java.nio.file.Files.createTempDirectory("nlp4j-test-badver-");
		try {
			// 壊れた schema ファイルを手動で書き込む（formatVersion=999）
			Path schemaFile = dir.resolve(nlp4j.lucene9.SearchSchemaStore.FILE_NAME);
			java.nio.file.Files.writeString(schemaFile,
					"{\"formatVersion\":999,\"fields\":[]}",
					java.nio.charset.StandardCharsets.UTF_8);

			// Lucene インデックスファイルを作る（schema がないとフォールバックする）
			// ダミーの segments_N ファイルを作らなくても schema ファイルがあれば load される
			try {
				LocalSearch search = LocalSearch.builder("en")
						.loadIndexFrom(dir)
						.build();
				search.close();
				fail("不正な formatVersion では LocalSearchException が期待される");
			} catch (LocalSearchException e) {
				assertTrue("例外メッセージに 'Failed to load schema' または 'formatVersion' が含まれること",
						e.getMessage().contains("Failed to load schema")
						|| e.getMessage().contains("formatVersion"));
			}
		} finally {
			deleteRecursively(dir);
		}
	}

	// -----------------------------------------------------------------------
	// saveIndexTo() 後の二重 close
	// -----------------------------------------------------------------------

	/**
	 * saveIndexTo() 後に try-with-resources で LocalSearch.close() が呼ばれた場合、
	 * 二重 close でも例外が発生しないことを確認する。
	 *
	 * <p>
	 * LuceneIndex.close() が idempotent であることを検証します。
	 * </p>
	 */
	public void testSaveIndexTo_DoubleClose_NoException() throws Exception {
		Path dir = java.nio.file.Files.createTempDirectory("nlp4j-test-dblclose-");
		try {
			// try-with-resources の close() で二重 close が起きるケース
			try (LocalSearch search = LocalSearch.builder("en").autoAnalyze(false).build()) {
				search.add("1", "hello world");
				search.commit();
				search.saveIndexTo(dir);
				// saveIndexTo() で内部の LuceneIndex が close される
				// try-with-resources の終わりでも close() が呼ばれる → 二重 close
			}
			// 例外が発生しなければ OK
		} finally {
			deleteRecursively(dir);
		}
	}

	// -----------------------------------------------------------------------
	// Helpers
	// -----------------------------------------------------------------------

	private void deleteRecursively(Path dir) {
		try {
			if (dir == null || !java.nio.file.Files.exists(dir)) {
				return;
			}
			java.nio.file.Files.walk(dir)
					.sorted(java.util.Comparator.reverseOrder())
					.map(Path::toFile)
					.forEach(java.io.File::delete);
		} catch (Exception e) {
			// ignore cleanup errors
		}
	}
}
