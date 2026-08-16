package nlp4j.lucene;

import junit.framework.TestCase;

/**
 * LocalSearch.searchLucene(String, int) のテストケース。
 *
 * <p>
 * Lucene Query Parser syntax を使用した検索について、
 * AND / OR / NOT / 括弧 / フレーズ / フィールド指定 / limit
 * の動作を確認する。
 * </p>
 */
public class LocalSearchLuceneQueryTestCase extends TestCase {

	// =========================================================
	// searchLucene()
	// =========================================================

	/**
	 * 単一キーワードによる基本検索。
	 *
	 * Kyoto を含む文書:
	 *
	 * id=1 Kyoto is a historic city in Japan.
	 * id=2 Nintendo is a video game company headquartered in Kyoto.
	 * id=4 Kyoto and Tokyo are major cities in Japan.
	 *
	 * → 3件
	 */
	public void testSearchLucene001_Basic() throws Exception {

		try (LocalSearch search = createStandardSearch()) {

			SearchResult[] results = search.searchLucene("Kyoto", 10);

			printResults("testSearchLucene001_Basic", results);

			assertEquals(3, results.length);

			assertTrue(containsId(results, "1"));
			assertTrue(containsId(results, "2"));
			assertTrue(containsId(results, "4"));

			assertFalse(containsId(results, "3"));
			assertFalse(containsId(results, "5"));
		}
	}

	/**
	 * AND 演算子が機能することを確認する。
	 *
	 * Kyoto AND Nintendo
	 *
	 * → id=2 のみ。
	 */
	public void testSearchLucene002_And() throws Exception {

		try (LocalSearch search = createStandardSearch()) {

			SearchResult[] results =
					search.searchLucene("Kyoto AND Nintendo", 10);

			printResults("testSearchLucene002_And", results);

			assertEquals(1, results.length);
			assertEquals("2", results[0].id);
		}
	}

	/**
	 * OR 演算子が機能することを確認する。
	 *
	 * Kyoto OR Tokyo
	 *
	 * Kyoto:
	 *   id=1,2,4
	 *
	 * Tokyo:
	 *   id=3,4
	 *
	 * → id=1,2,3,4 の4件。
	 */
	public void testSearchLucene003_Or() throws Exception {

		try (LocalSearch search = createStandardSearch()) {

			SearchResult[] results =
					search.searchLucene("Kyoto OR Tokyo", 10);

			printResults("testSearchLucene003_Or", results);

			assertEquals(4, results.length);

			assertTrue(containsId(results, "1"));
			assertTrue(containsId(results, "2"));
			assertTrue(containsId(results, "3"));
			assertTrue(containsId(results, "4"));

			assertFalse(containsId(results, "5"));
		}
	}

	/**
	 * NOT 演算子が機能することを確認する。
	 *
	 * Kyoto AND NOT Nintendo
	 *
	 * Kyoto を含む id=1,2,4 から
	 * Nintendo を含む id=2 が除外される。
	 *
	 * → id=1,4
	 */
	public void testSearchLucene004_Not() throws Exception {

		try (LocalSearch search = createStandardSearch()) {

			SearchResult[] results =
					search.searchLucene(
							"Kyoto AND NOT Nintendo",
							10);

			printResults("testSearchLucene004_Not", results);

			assertEquals(2, results.length);

			assertTrue(containsId(results, "1"));
			assertTrue(containsId(results, "4"));

			assertFalse(containsId(results, "2"));
		}
	}

	/**
	 * 括弧を使った論理式が機能することを確認する。
	 *
	 * Kyoto AND (Nintendo OR historic)
	 *
	 * id=1 : Kyoto + historic
	 * id=2 : Kyoto + Nintendo
	 * id=4 : Kyoto のみ
	 *
	 * → id=1,2
	 */
	public void testSearchLucene005_Parentheses() throws Exception {

		try (LocalSearch search = createStandardSearch()) {

			SearchResult[] results =
					search.searchLucene(
							"Kyoto AND (Nintendo OR historic)",
							10);

			printResults(
					"testSearchLucene005_Parentheses",
					results);

			assertEquals(2, results.length);

			assertTrue(containsId(results, "1"));
			assertTrue(containsId(results, "2"));

			assertFalse(containsId(results, "4"));
		}
	}

	/**
	 * Lucene のフレーズ検索が機能することを確認する。
	 *
	 * "historic city"
	 *
	 * → id=1 のみ。
	 */
	public void testSearchLucene006_Phrase() throws Exception {

		try (LocalSearch search = createStandardSearch()) {

			SearchResult[] results =
					search.searchLucene(
							"\"historic city\"",
							10);

			printResults("testSearchLucene006_Phrase", results);

			assertEquals(1, results.length);
			assertEquals("1", results[0].id);
		}
	}

	/**
	 * Lucene Query Parser のフィールド指定が機能することを確認する。
	 *
	 * text_en:Kyoto AND text_en:Nintendo
	 *
	 * → id=2 のみ。
	 */
	public void testSearchLucene007_Field() throws Exception {

		try (LocalSearch search = createStandardSearch()) {

			SearchResult[] results =
					search.searchLucene(
							"text_en:Kyoto AND text_en:Nintendo",
							10);

			printResults("testSearchLucene007_Field", results);

			assertEquals(1, results.length);
			assertEquals("2", results[0].id);
		}
	}

	/**
	 * limit が検索結果件数に反映されることを確認する。
	 *
	 * Kyoto は3件ヒットするが limit=1 のため1件のみ返ること。
	 */
	public void testSearchLucene008_Limit() throws Exception {

		try (LocalSearch search = createStandardSearch()) {

			SearchResult[] results =
					search.searchLucene("Kyoto", 1);

			printResults("testSearchLucene008_Limit", results);

			assertEquals(1, results.length);
		}
	}

	/**
	 * 一致する文書が存在しない場合、
	 * 空の SearchResult[] が返ることを確認する。
	 */
	public void testSearchLucene009_NoMatch() throws Exception {

		try (LocalSearch search = createStandardSearch()) {

			SearchResult[] results =
					search.searchLucene(
							"Kyoto AND Osaka",
							10);

			printResults("testSearchLucene009_NoMatch", results);

			assertNotNull(results);
			assertEquals(0, results.length);
		}
	}

	/**
	 * Lucene Query Parser として不正な構文を指定した場合、
	 * RuntimeException がスローされることを確認する。
	 *
	 * 現在の LuceneQueryBuilder.build() は ParseException 等を
	 * RuntimeException にラップしている。
	 */
	public void testSearchLucene010_InvalidSyntax() throws Exception {

		try (LocalSearch search = createStandardSearch()) {

			try {

				search.searchLucene(
						"Kyoto AND (Nintendo OR",
						10);

				fail("不正な Lucene query の場合は例外が必要");

			} catch (RuntimeException e) {

				System.out.println(
						"testSearchLucene010_InvalidSyntax: "
						+ e.getMessage());

				assertNotNull(e.getMessage());
			}
		}
	}

	// =========================================================
	// Helper
	// =========================================================

	/**
	 * SearchResult[] に指定IDが含まれているか確認する。
	 */
	private boolean containsId(
			SearchResult[] results,
			String id) {

		for (SearchResult result : results) {

			if (id.equals(result.id)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * テスト結果をコンソールへ出力する。
	 */
	private void printResults(
			String testName,
			SearchResult[] results) {

		System.out.println(
				testName + " size: " + results.length);

		for (int i = 0; i < results.length; i++) {

			System.out.println(
					"result[" + i + "].id: "
					+ results[i].id);

			System.out.println(
					"result[" + i + "].body: "
					+ results[i].body);

			System.out.println(
					"result[" + i + "].score: "
					+ results[i].score);
		}
	}

	// =========================================================
	// Test data
	// =========================================================

	/**
	 * Lucene query テスト用の LocalSearch を生成する。
	 *
	 * <pre>
	 * id=1 Kyoto is a historic city in Japan.
	 * id=2 Nintendo is a video game company headquartered in Kyoto.
	 * id=3 Sony is a video game company headquartered in Tokyo.
	 * id=4 Kyoto and Tokyo are major cities in Japan.
	 * id=5 Osaka is a large city in Japan.
	 * </pre>
	 */
	private LocalSearch createStandardSearch()
			throws Exception {

		LocalSearch search =
				LocalSearch.builder("en")
						.autoAnalyze(false)
						.build();

		try {

			search.add(
					"1",
					"Kyoto is a historic city in Japan.");

			search.add(
					"2",
					"Nintendo is a video game company headquartered in Kyoto.");

			search.add(
					"3",
					"Sony is a video game company headquartered in Tokyo.");

			search.add(
					"4",
					"Kyoto and Tokyo are major cities in Japan.");

			search.add(
					"5",
					"Osaka is a large city in Japan.");

			search.commit();

			return search;

		} catch (Exception e) {

			search.close();

			throw e;
		}
	}
}