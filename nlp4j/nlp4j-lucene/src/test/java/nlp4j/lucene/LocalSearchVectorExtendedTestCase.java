package nlp4j.lucene;

import java.util.Map;

import junit.framework.TestCase;
import nlp4j.lucene9.FieldTypeDef;

/**
 * LocalSearch ベクトル検索拡張機能のテストケース。
 *
 * <p>
 * kaiwa0830-1810.md の会話内容に基づき実装した以下の機能を検証します:
 * </p>
 * <ol>
 * <li>{@code add(String id, String body, float[] vector)}</li>
 * <li>{@code add(String id, String body, float[] vector, Map<String,String> fields)}</li>
 * <li>{@code validateVector()} による共通バリデーション</li>
 * <li>{@code getVectorDimension()} / {@code hasVectorField()}</li>
 * </ol>
 */
public class LocalSearchVectorExtendedTestCase extends TestCase {

	// =========================================================
	// add(id, body, vector) のテスト
	// =========================================================

	/**
	 * add(id, body, vector) でテキストとベクトルを同時に登録し、 searchVector() でベクトル検索できることを確認する。
	 */
	public void testAddBodyVector001() throws Exception {
		float[] v1 = { 1.0f, 0.0f };
		float[] v2 = { 0.0f, 1.0f };
		float[] v3 = { -1.0f, 0.0f };

		try (LocalSearch search = new LocalSearch("en", 2)) {
			search.add("1", "Kyoto is a historic city.", v1);
			search.add("2", "Tokyo is the capital of Japan.", v2);
			search.add("3", "Osaka is a city in Japan.", v3);
			search.commit();

			// クエリベクトル (0.9, 0.1) は v1 (1.0, 0.0) に最近傍 → id=1 が先頭
			SearchResult[] results = search.searchVector(new float[] { 0.9f, 0.1f }, 10);
			System.out.println("testAddBodyVector001 size: " + results.length);
			for (int n = 0; n < results.length; n++) {
				System.out.println("result[" + n + "].id: " + results[n].id + " body: " + results[n].body);
			}

			assertEquals(3, results.length);
			assertEquals("1", results[0].id);
			// body が取得できること
			assertEquals("Kyoto is a historic city.", results[0].body);
		}
	}

	/**
	 * add(id, body, vector) で登録した文書に対して全文検索（search）も動作することを確認する。
	 */
	public void testAddBodyVector002() throws Exception {
		float[] v1 = { 1.0f, 0.0f };
		float[] v2 = { 0.0f, 1.0f };

		try (LocalSearch search = new LocalSearch("en", 2)) {
			search.add("1", "Kyoto is a historic city in Japan.", v1);
			search.add("2", "Tokyo is the capital of Japan.", v2);
			search.commit();

			// 全文検索でも body が検索できること
			SearchResult[] results = search.search("Kyoto", 10);
			System.out.println("testAddBodyVector002 size: " + results.length);
			assertEquals(1, results.length);
			assertEquals("1", results[0].id);
		}
	}

	// =========================================================
	// add(id, body, vector, fields) のテスト
	// =========================================================

	/**
	 * add(id, body, vector, fields) でテキスト・ベクトル・追加フィールドを同時に登録し、 searchVector()
	 * でフィールドフィルターが動作することを確認する。
	 */
	public void testAddBodyVectorFields001() throws Exception {
		float[] v1 = { 1.0f, 0.0f };
		float[] v2 = { 0.9f, 0.1f };
		float[] v3 = { 0.0f, 1.0f };

		try (LocalSearch search = new LocalSearch("en", 2)) {
			search.add("1", "Kyoto is a historic city.", v1, Map.of("category_s", "city"));
			search.add("2", "Nintendo is headquartered in Kyoto.", v2, Map.of("category_s", "company"));
			search.add("3", "Tokyo is the capital city of Japan.", v3, Map.of("category_s", "city"));
			search.commit();

			// category_s=city でフィルター → id=1, id=3 の 2 件
			SearchResult[] results = search.searchVector(new float[] { 0.9f, 0.1f }, 10, Map.of("category_s", "city"));
			System.out.println("testAddBodyVectorFields001 size: " + results.length);
			for (int n = 0; n < results.length; n++) {
				System.out.println("result[" + n + "].id: " + results[n].id);
			}
			assertEquals(2, results.length);
			// 最近傍は id=1（v1 が最も近い）
			assertEquals("1", results[0].id);
		}
	}

	/**
	 * add(id, body, vector, fields) で登録した文書に対して全文検索＋フィールドフィルターも動作することを確認する。
	 */
	public void testAddBodyVectorFields002() throws Exception {
		float[] v1 = { 1.0f, 0.0f };
		float[] v2 = { 0.9f, 0.1f };
		float[] v3 = { 0.0f, 1.0f };

		try (LocalSearch search = new LocalSearch("en", 2)) {
			search.add("1", "Kyoto is a historic city in Japan.", v1, Map.of("category", "city", "country", "Japan"));
			search.add("2", "Nintendo is headquartered in Kyoto.", v2,
					Map.of("category", "company", "country", "Japan"));
			search.add("3", "Paris is the capital city of France.", v3,
					Map.of("category", "city", "country", "France"));
			search.commit();

			// "Kyoto" + category=company → id=2 の 1 件
			SearchResult[] results = search.search("Kyoto", 10, Map.of("category", "company"));
			System.out.println("testAddBodyVectorFields002 size: " + results.length);
			assertEquals(1, results.length);
			assertEquals("2", results[0].id);
		}
	}

	/**
	 * add(id, body, vector, fields=null) で fields が null の場合も正常に動作することを確認する。
	 */
	public void testAddBodyVectorFieldsNullFields() throws Exception {
		float[] v1 = { 1.0f, 0.0f };

		try (LocalSearch search = new LocalSearch("en", 2)) {
			search.add("1", "Kyoto is a historic city.", v1, null);
			search.commit();

			SearchResult[] results = search.searchVector(new float[] { 1.0f, 0.0f }, 10);
			assertEquals(1, results.length);
			assertEquals("1", results[0].id);
		}
	}

	// =========================================================
	// validateVector() のテスト
	// =========================================================

	/**
	 * add(id, body, vector) で null ベクトルを渡した場合 LocalSearchException がスローされることを確認する。
	 */
	public void testValidateVectorNull() throws Exception {
		try (LocalSearch search = new LocalSearch("en", 2)) {
			try {
				search.add("1", "body", null);
				fail("Expected LocalSearchException for null vector");
			} catch (LocalSearchException e) {
				System.out.println("testValidateVectorNull: " + e.getMessage());
				assertTrue(e.getMessage().contains("null"));
			}
		}
	}

	/**
	 * add(id, body, vector) で次元数不一致のベクトルを渡した場合 LocalSearchException がスローされることを確認する。
	 */
	public void testValidateVectorDimensionMismatch() throws Exception {
		try (LocalSearch search = new LocalSearch("en", 2)) {
			try {
				// 3次元ベクトルを渡す（vectorDimension=2 と不一致）
				search.add("1", "body", new float[] { 1.0f, 0.0f, 0.0f });
				fail("Expected LocalSearchException for dimension mismatch");
			} catch (LocalSearchException e) {
				System.out.println("testValidateVectorDimensionMismatch: " + e.getMessage());
				assertTrue(e.getMessage().contains("mismatch") || e.getMessage().contains("dimension"));
			}
		}
	}

	/**
	 * vectorDimension を設定せずに add(id, body, vector) を呼んだ場合 LocalSearchException
	 * がスローされることを確認する。
	 */
	public void testValidateVectorNoVectorField() throws Exception {
		try (LocalSearch search = new LocalSearch("en")) { // vectorDimension=0
			try {
				search.add("1", "body", new float[] { 1.0f, 0.0f });
				fail("Expected LocalSearchException when vector field not enabled");
			} catch (LocalSearchException e) {
				System.out.println("testValidateVectorNoVectorField: " + e.getMessage());
				assertTrue(e.getMessage().contains("Vector field is not defined"));
			}
		}
	}

	public void testValidateVectorUnknownField() throws Exception {

		try (LocalSearch search = LocalSearch.builder("en").vectorField("vector3", 3).build()) {

			try {
				search.searchVector("unknown_vector", new float[] { 1.0f, 0.0f, 0.0f }, 10);

				fail("Expected LocalSearchException");

			} catch (LocalSearchException e) {

				assertTrue(e.getMessage().contains("Vector field is not defined"));
			}
		}
	}

	public void testValidateVectorWrongFieldType() throws Exception {

		try (LocalSearch search = LocalSearch.builder("en").field("category_s", FieldTypeDef.keyword().stored(true))
				.build()) {

			try {
				search.searchVector("category_s", new float[] { 1.0f, 0.0f }, 10);

				fail("Expected LocalSearchException");

			} catch (LocalSearchException e) {

				assertTrue(e.getMessage().contains("is not a KNN_VECTOR field"));
			}
		}
	}

	/**
	 * searchVector() で次元数不一致のベクトルを渡した場合 LocalSearchException がスローされることを確認する。
	 */
	public void testValidateVectorOnSearch() throws Exception {
		try (LocalSearch search = new LocalSearch("en", 2)) {
			search.add("1", new float[] { 1.0f, 0.0f });
			search.commit();
			try {
				// 3次元ベクトルで検索（vectorDimension=2 と不一致）
				search.searchVector(new float[] { 1.0f, 0.0f, 0.0f }, 10);
				fail("Expected LocalSearchException for dimension mismatch on search");
			} catch (LocalSearchException e) {
				System.out.println("testValidateVectorOnSearch: " + e.getMessage());
				assertTrue(e.getMessage().contains("mismatch") || e.getMessage().contains("dimension"));
			}
		}
	}

	/**
	 * NaN を含むベクトルを渡した場合 LocalSearchException がスローされることを確認する。
	 */
	public void testValidateVectorNaN() throws Exception {
		try (LocalSearch search = new LocalSearch("en", 2)) {
			try {
				search.add("1", "body", new float[] { Float.NaN, 0.0f });
				fail("Expected LocalSearchException for NaN vector");
			} catch (LocalSearchException e) {
				System.out.println("testValidateVectorNaN: " + e.getMessage());
				assertTrue(e.getMessage().contains("NaN") || e.getMessage().contains("invalid"));
			}
		}
	}

	// =========================================================
	// getVectorDimension() / hasVectorField() のテスト
	// =========================================================

	/**
	 * vectorDimension=1024 で構築した場合 getVectorDimension() が 1024 を返すことを確認する。
	 */
	public void testGetVectorDimension001() throws Exception {
		try (LocalSearch search = new LocalSearch("en", 1024)) {
			assertEquals(1024, search.getVectorDimension());
			assertTrue(search.hasVectorField());
		}
	}

	/**
	 * vectorDimension 未設定（=0）の場合 getVectorDimension() が 0 を返し、 hasVectorField() が
	 * false を返すことを確認する。
	 */
	public void testGetVectorDimension002() throws Exception {
		try (LocalSearch search = new LocalSearch("en")) {
			assertEquals(0, search.getVectorDimension());
			assertFalse(search.hasVectorField());
		}
	}

	/**
	 * Builder で vectorDimension を指定した場合も getVectorDimension() が正しい値を返すことを確認する。
	 */
	public void testGetVectorDimensionBuilder() throws Exception {
		try (LocalSearch search = LocalSearch.builder("en").vectorDimension(384).build()) {
			assertEquals(384, search.getVectorDimension());
			assertTrue(search.hasVectorField());
		}
	}

	// =========================================================
	// add(id, body, vector) と searchVector フィルターの統合テスト
	// =========================================================

	/**
	 * Wikipedia サンプルパターンの確認: add(id, body, vector, fields) で
	 * id/body/category/vector を同時登録し、 searchVector + filter で category
	 * 絞り込みが動作することを確認する。
	 */
	public void testWikipediaSamplePattern() throws Exception {
		float[] kyotoVec = { 0.9f, 0.1f, 0.0f };
		float[] tokyoVec = { 0.8f, 0.2f, 0.1f };
		float[] nintendoVec = { 0.1f, 0.9f, 0.0f };
		float[] queryVec = { 0.85f, 0.15f, 0.05f };

		try (LocalSearch search = new LocalSearch("en", 3)) {
			search.add("Kyoto", "Kyoto is a historic city in Japan.", kyotoVec, Map.of("category_s", "city"));
			search.add("Tokyo", "Tokyo is the capital city of Japan.", tokyoVec, Map.of("category_s", "city"));
			search.add("Nintendo", "Nintendo is a video game company headquartered in Kyoto.", nintendoVec,
					Map.of("category_s", "company"));
			search.commit();

			// category_s=city でフィルター → city カテゴリの 2 件のみ
			SearchResult[] cityResults = search.searchVector(queryVec, 10, Map.of("category_s", "city"));
			System.out.println("testWikipediaSamplePattern city size: " + cityResults.length);
			assertEquals(2, cityResults.length);

			// フィルターなし → 全 3 件
			SearchResult[] allResults = search.searchVector(queryVec, 10);
			System.out.println("testWikipediaSamplePattern all size: " + allResults.length);
			assertEquals(3, allResults.length);
		}
	}

	// =========================================================
	// テキスト検索＋ベクトル検索が同一 Document を返す統合テスト
	// (kaiwa0830-1832 推奨テスト)
	// =========================================================

	/**
	 * add(id, body, vector) で登録した同一 Document が、 テキスト検索でも KNN ベクトル検索でも取得できることを確認する。
	 *
	 * <p>
	 * Embedding 統合で最も重要なシナリオ:
	 * </p>
	 * 
	 * <pre>
	*            same document
	*                 │
	*       ┌─────────┴─────────┐
	*       ↓                   ↓
	* text search          vector search
	*       ↓                   ↓
	*     id=1               id=1
	*       │                   │
	*       └─────────┬─────────┘
	*                 ↓
	*     "Kyoto is a historic city."
	 * </pre>
	 */
	public void testAddTextAndVector001() throws Exception {
		try (LocalSearch search = LocalSearch.builder("en").vectorDimension(2).build()) {
			search.add("1", "Kyoto is a historic city.", new float[] { 1.0f, 0.0f });
			search.add("2", "Tokyo is the capital of Japan.", new float[] { 0.0f, 1.0f });
			search.commit();

			// テキスト検索: "Kyoto" → id=1 のみ
			SearchResult[] text = search.search("Kyoto", 10);
			System.out.println("testAddTextAndVector001 text size: " + text.length);
			assertEquals(1, text.length);
			assertEquals("1", text[0].id);

			// ベクトル検索: (0.9, 0.1) は id=1 に最近傍
			SearchResult[] vector = search.searchVector(new float[] { 0.9f, 0.1f }, 10);
			System.out.println("testAddTextAndVector001 vector size: " + vector.length);
			assertEquals(2, vector.length);
			assertEquals("1", vector[0].id);
			// body が両方の検索で取得できること
			assertEquals("Kyoto is a historic city.", vector[0].body);
		}
	}

	/**
	 * add(id, body, vector, fields) で登録した同一 Document が、 テキスト検索＋フィールドフィルターでも KNN
	 * ベクトル検索＋フィールドフィルターでも 同じ Document を返すことを確認する。
	 */
	public void testAddTextAndVector002() throws Exception {
		float[] nintendoVec = { 0.1f, 0.9f };
		float[] kyotoVec = { 0.9f, 0.1f };
		float[] queryVec = { 0.85f, 0.15f };

		try (LocalSearch search = LocalSearch.builder("en").vectorDimension(2).build()) {
			search.add("1", "Nintendo is headquartered in Kyoto.", nintendoVec,
					Map.of("category", "company", "country", "Japan"));
			search.add("2", "Kyoto is a historic city in Japan.", kyotoVec,
					Map.of("category", "city", "country", "Japan"));
			search.add("3", "Paris is the capital city of France.", new float[] { 0.5f, 0.5f },
					Map.of("category", "city", "country", "France"));
			search.commit();

			// テキスト検索 "Kyoto" + category=company → id=1 のみ
			SearchResult[] textResults = search.search("Kyoto", 10, Map.of("category", "company"));
			System.out.println("testAddTextAndVector002 text size: " + textResults.length);
			assertEquals(1, textResults.length);
			assertEquals("1", textResults[0].id);

			// ベクトル検索 + category=company → id=1 のみ
			SearchResult[] vectorResults = search.searchVector(queryVec, 10, Map.of("category", "company"));
			System.out.println("testAddTextAndVector002 vector size: " + vectorResults.length);
			assertEquals(1, vectorResults.length);
			assertEquals("1", vectorResults[0].id);

			// 両方の検索が同一 Document (id=1) を返すこと
			assertEquals(textResults[0].id, vectorResults[0].id);
		}
	}

	/**
	 * add(id, body, vector, fields) で登録した同一 Document が、 テキスト検索＋フィールドフィルターと KNN
	 * ベクトル検索＋フィールドフィルターの 両方で同じ id を返すことを確認する。
	 *
	 * <pre>
	* search.add(
	*     "1",
	*     "Nintendo is headquartered in Kyoto.",
	*     new float[] {1.0f, 0.0f},
	*     Map.of("category", "company", "country", "Japan")
	* );
	*
	* search.search("Kyoto", 10, Map.of("category", "company"))
	*   → id=1
	*
	* search.searchVector(queryVector, 10, Map.of("category", "company"))
	*   → id=1
	 * </pre>
	 */
	public void testAddTextVectorAndFields001() throws Exception {
		try (LocalSearch search = LocalSearch.builder("en").vectorDimension(2).build()) {
			search.add("1", "Nintendo is headquartered in Kyoto.", new float[] { 1.0f, 0.0f },
					java.util.Map.of("category", "company", "country", "Japan"));
			search.add("2", "Kyoto is a historic city in Japan.", new float[] { 0.9f, 0.1f },
					java.util.Map.of("category", "city", "country", "Japan"));
			search.add("3", "Paris is the capital city of France.", new float[] { 0.0f, 1.0f },
					java.util.Map.of("category", "city", "country", "France"));
			search.commit();

			// テキスト検索 "Kyoto" + category=company → id=1 のみ
			SearchResult[] textResults = search.search("Kyoto", 10, java.util.Map.of("category", "company"));
			System.out.println("testAddTextVectorAndFields001 text size: " + textResults.length);
			assertEquals(1, textResults.length);
			assertEquals("1", textResults[0].id);

			// ベクトル検索 (0.9, 0.1) + category=company → id=1 のみ
			SearchResult[] vectorResults = search.searchVector(new float[] { 0.9f, 0.1f }, 10,
					java.util.Map.of("category", "company"));
			System.out.println("testAddTextVectorAndFields001 vector size: " + vectorResults.length);
			assertEquals(1, vectorResults.length);
			assertEquals("1", vectorResults[0].id);
		}
	}
}
