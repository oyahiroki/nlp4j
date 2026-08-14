package nlp4j.analytics;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;
import nlp4j.lucene.LocalSearch;

public class LocalAnalyticsTestCase extends TestCase {

	// =========================================================
	// Constructor
	// =========================================================

	/**
	 * LocalAnalytics のコンストラクタに null を渡した場合、 NullPointerException がスローされることを確認する。
	 */
	public void testConstructor001() throws Exception {

		try {
			new LocalAnalytics(null);
			fail("LocalSearch が null の場合は NullPointerException がスローされること");
		} catch (NullPointerException e) {
			assertEquals("search must not be null", e.getMessage());
		}
	}

	// =========================================================
	// relativeRate()
	// =========================================================

	/**
	 * relativeRate() の基本動作を確認する。
	 *
	 * <pre>
	 * Documents:
	 *
	 * id=1 group=A feature=[x,y]
	 * id=2 group=A feature=[x]
	 * id=3 group=B feature=[y]
	 * id=4 group=B feature=[y,z]
	 * id=5 group=B feature=[z]
	 *
	 * 全文書数 = 5
	 * group=A = 2文書
	 *
	 * feature=x
	 *   group=A : 2 / 2 = 1.0
	 *   all     : 2 / 5 = 0.4
	 *   relativeRate = 1.0 / 0.4 = 2.5
	 *
	 * feature=y
	 *   group=A : 1 / 2 = 0.5
	 *   all     : 3 / 5 = 0.6
	 *   relativeRate = 0.5 / 0.6 = 0.833333...
	 * </pre>
	 */
	public void testRelativeRate001() throws Exception {

		try (LocalSearch search = createStandardSearch()) {

			LocalAnalytics analytics = new LocalAnalytics(search);

			Map<String, Double> result = analytics.relativeRate("group", "A", "feature", 100);

			System.out.println("testRelativeRate001: " + result);

			assertEquals(2, result.size());

			assertEquals(2.5, result.get("x").doubleValue(), 0.000001);

			assertEquals(0.8333333333, result.get("y").doubleValue(), 0.000001);

			assertFalse(result.containsKey("z"));
		}
	}

	/**
	 * relativeRate() の結果が relativeRate の降順になっていることを確認する。
	 *
	 * x = 2.5 y = 0.8333...
	 *
	 * したがって x → y の順になること。
	 */
	public void testRelativeRate002_Sorted() throws Exception {

		try (LocalSearch search = createStandardSearch()) {

			LocalAnalytics analytics = new LocalAnalytics(search);

			Map<String, Double> result = analytics.relativeRate("group", "A", "feature", 100);

			List<String> keys = new ArrayList<>(result.keySet());

			assertEquals(2, keys.size());

			assertEquals("x", keys.get(0));
			assertEquals("y", keys.get(1));

			assertTrue(result.get("x") > result.get("y"));
		}
	}

	/**
	 * group=B の relativeRate を確認する。
	 *
	 * group=B = 3文書
	 *
	 * feature=y: targetRate = 2/3 allRate = 3/5 relativeRate = 1.111111...
	 *
	 * feature=z: targetRate = 2/3 allRate = 2/5 relativeRate = 1.666666...
	 *
	 * 結果は z → y の順になる。
	 */
	public void testRelativeRate003_GroupB() throws Exception {

		try (LocalSearch search = createStandardSearch()) {

			LocalAnalytics analytics = new LocalAnalytics(search);

			Map<String, Double> result = analytics.relativeRate("group", "B", "feature", 100);

			System.out.println("testRelativeRate003_GroupB: " + result);

			assertEquals(2, result.size());

			assertEquals(1.6666666667, result.get("z").doubleValue(), 0.000001);

			assertEquals(1.1111111111, result.get("y").doubleValue(), 0.000001);

			List<String> keys = new ArrayList<>(result.keySet());

			assertEquals("z", keys.get(0));
			assertEquals("y", keys.get(1));
		}
	}

	/**
	 * queryField と aggregationField が同じ場合も relativeRate を計算できることを確認する。
	 *
	 * group=A の文書では group=A が 100%。
	 *
	 * targetRate = 2/2 = 1.0 allRate = 2/5 = 0.4 relativeRate = 2.5
	 */
	public void testRelativeRate004_SameField() throws Exception {

		try (LocalSearch search = createStandardSearch()) {

			LocalAnalytics analytics = new LocalAnalytics(search);

			Map<String, Double> result = analytics.relativeRate("group", "A", "group", 100);

			System.out.println("testRelativeRate004_SameField: " + result);

			assertEquals(1, result.size());

			assertEquals(2.5, result.get("A").doubleValue(), 0.000001);
		}
	}

	/**
	 * queryField=queryValue に該当する文書が存在しない場合、 空 Map が返ることを確認する。
	 */
	public void testRelativeRate005_NoQueryDocuments() throws Exception {

		try (LocalSearch search = createStandardSearch()) {

			LocalAnalytics analytics = new LocalAnalytics(search);

			Map<String, Double> result = analytics.relativeRate("group", "NOT_FOUND", "feature", 100);

			assertNotNull(result);
			assertTrue(result.isEmpty());
		}
	}

	/**
	 * インデックスが空の場合、空 Map が返ることを確認する。
	 */
	public void testRelativeRate006_EmptyIndex() throws Exception {

		try (LocalSearch search = LocalSearch.builder("en").autoAnalyze(false).build()) {

			search.commit();

			LocalAnalytics analytics = new LocalAnalytics(search);

			Map<String, Double> result = analytics.relativeRate("group", "A", "feature", 100);

			assertNotNull(result);
			assertTrue(result.isEmpty());
		}
	}

	/**
	 * aggregationAll に対象値が含まれない場合、 LocalAnalytics が count(aggregationField, key)
	 * を使って allCount を取得できることを確認する。
	 *
	 * size=1 のため全体 aggregation の最大値 "y" だけが返るが、 group=A 内では "x" が最大。
	 *
	 * Documents:
	 *
	 * A -> x B -> y B -> y
	 *
	 * x: targetRate = 1/1 = 1.0 allRate = 1/3 relativeRate = 3.0
	 *
	 * aggregationAll(size=1) には x がないため、 fallback の count("feature", "x") が必要。
	 */
	public void testRelativeRate007_AggregationSizeFallback() throws Exception {

		try (LocalSearch search = LocalSearch.builder("en").autoAnalyze(false).build()) {

			search.addJson("""
					{
					  "id":"1",
					  "body":"document 1",
					  "group":"A",
					  "feature":"x"
					}
					""");

			search.addJson("""
					{
					  "id":"2",
					  "body":"document 2",
					  "group":"B",
					  "feature":"y"
					}
					""");

			search.addJson("""
					{
					  "id":"3",
					  "body":"document 3",
					  "group":"B",
					  "feature":"y"
					}
					""");

			search.commit();

			LocalAnalytics analytics = new LocalAnalytics(search);

			Map<String, Double> result = analytics.relativeRate("group", "A", "feature", 1);

			System.out.println("testRelativeRate007_AggregationSizeFallback: " + result);

			assertEquals(1, result.size());

			assertEquals(3.0, result.get("x").doubleValue(), 0.000001);
		}
	}

	// =========================================================
	// relativeRates()
	// =========================================================

	/**
	 * queryField に存在するすべての値について relativeRate が計算されることを確認する。
	 *
	 * group=A, group=B の両方が返ること。
	 */
	public void testRelativeRates001() throws Exception {

		try (LocalSearch search = createStandardSearch()) {

			LocalAnalytics analytics = new LocalAnalytics(search);

			Map<String, Map<String, Double>> result = analytics.relativeRates("group", "feature", 100);

			System.out.println("testRelativeRates001: " + result);

			assertEquals(2, result.size());

			assertTrue(result.containsKey("A"));
			assertTrue(result.containsKey("B"));

			Map<String, Double> groupA = result.get("A");

			Map<String, Double> groupB = result.get("B");

			assertEquals(2.5, groupA.get("x").doubleValue(), 0.000001);

			assertEquals(0.8333333333, groupA.get("y").doubleValue(), 0.000001);

			assertEquals(1.6666666667, groupB.get("z").doubleValue(), 0.000001);

			assertEquals(1.1111111111, groupB.get("y").doubleValue(), 0.000001);
		}
	}

	/**
	 * relativeRates() の各内部 Map が relativeRate 降順になっていることを確認する。
	 */
	public void testRelativeRates002_Sorted() throws Exception {

		try (LocalSearch search = createStandardSearch()) {

			LocalAnalytics analytics = new LocalAnalytics(search);

			Map<String, Map<String, Double>> result = analytics.relativeRates("group", "feature", 100);

			{
				List<String> keys = new ArrayList<>(result.get("A").keySet());

				assertEquals("x", keys.get(0));
				assertEquals("y", keys.get(1));
			}

			{
				List<String> keys = new ArrayList<>(result.get("B").keySet());

				assertEquals("z", keys.get(0));
				assertEquals("y", keys.get(1));
			}
		}
	}

	/**
	 * 空インデックスで relativeRates() を呼び出した場合、 空 Map が返ることを確認する。
	 */
	public void testRelativeRates003_EmptyIndex() throws Exception {

		try (LocalSearch search = LocalSearch.builder("en").autoAnalyze(false).build()) {

			search.commit();

			LocalAnalytics analytics = new LocalAnalytics(search);

			Map<String, Map<String, Double>> result = analytics.relativeRates("group", "feature", 100);

			assertNotNull(result);
			assertTrue(result.isEmpty());
		}
	}

	// =========================================================
	// Validation
	// =========================================================

	/**
	 * relativeRate() の queryField が null の場合。
	 */
	public void testRelativeRateValidation001() throws Exception {

		try (LocalSearch search = createStandardSearch()) {

			LocalAnalytics analytics = new LocalAnalytics(search);

			try {
				analytics.relativeRate(null, "A", "feature", 100);

				fail("IllegalArgumentException が必要");

			} catch (IllegalArgumentException e) {

				assertEquals("queryField must not be empty", e.getMessage());
			}
		}
	}

	/**
	 * relativeRate() の queryField が空文字の場合。
	 */
	public void testRelativeRateValidation002() throws Exception {

		try (LocalSearch search = createStandardSearch()) {

			LocalAnalytics analytics = new LocalAnalytics(search);

			try {
				analytics.relativeRate("", "A", "feature", 100);

				fail("IllegalArgumentException が必要");

			} catch (IllegalArgumentException e) {

				assertEquals("queryField must not be empty", e.getMessage());
			}
		}
	}

	/**
	 * relativeRate() の aggregationField が空文字の場合。
	 */
	public void testRelativeRateValidation003() throws Exception {

		try (LocalSearch search = createStandardSearch()) {

			LocalAnalytics analytics = new LocalAnalytics(search);

			try {
				analytics.relativeRate("group", "A", "", 100);

				fail("IllegalArgumentException が必要");

			} catch (IllegalArgumentException e) {

				assertEquals("aggregationField must not be empty", e.getMessage());
			}
		}
	}

	/**
	 * relativeRate() の queryValue が null の場合。
	 */
	public void testRelativeRateValidation004() throws Exception {

		try (LocalSearch search = createStandardSearch()) {

			LocalAnalytics analytics = new LocalAnalytics(search);

			try {
				analytics.relativeRate("group", null, "feature", 100);

				fail("IllegalArgumentException が必要");

			} catch (IllegalArgumentException e) {

				assertEquals("queryValue must not be null", e.getMessage());
			}
		}
	}

	/**
	 * relativeRate() の size が 0 の場合。
	 */
	public void testRelativeRateValidation005() throws Exception {

		try (LocalSearch search = createStandardSearch()) {

			LocalAnalytics analytics = new LocalAnalytics(search);

			try {
				analytics.relativeRate("group", "A", "feature", 0);

				fail("IllegalArgumentException が必要");

			} catch (IllegalArgumentException e) {

				assertEquals("size must be greater than 0", e.getMessage());
			}
		}
	}

	/**
	 * relativeRates() の size が 0 の場合。
	 */
	public void testRelativeRatesValidation001() throws Exception {

		try (LocalSearch search = createStandardSearch()) {

			LocalAnalytics analytics = new LocalAnalytics(search);

			try {
				analytics.relativeRates("group", "feature", 0);

				fail("IllegalArgumentException が必要");

			} catch (IllegalArgumentException e) {

				assertEquals("size must be greater than 0", e.getMessage());
			}
		}
	}

	// =========================================================
	// Test data
	// =========================================================

	/**
	 * テスト用の LocalSearch を生成する。
	 *
	 * <pre>
	 * id=1 group=A feature=[x,y]
	 * id=2 group=A feature=[x]
	 * id=3 group=B feature=[y]
	 * id=4 group=B feature=[y,z]
	 * id=5 group=B feature=[z]
	 *
	 * Document count:
	 *
	 * group
	 *   A = 2
	 *   B = 3
	 *
	 * feature
	 *   x = 2
	 *   y = 3
	 *   z = 2
	 * </pre>
	 */
	private LocalSearch createStandardSearch() throws Exception {

		LocalSearch search = LocalSearch.builder("en").autoAnalyze(false).build();

		try {
			search.addJson("""
					{
					  "id":"1",
					  "body":"document 1",
					  "group":"A",
					  "feature":["x","y"]
					}
					""");

			search.addJson("""
					{
					  "id":"2",
					  "body":"document 2",
					  "group":"A",
					  "feature":["x"]
					}
					""");

			search.addJson("""
					{
					  "id":"3",
					  "body":"document 3",
					  "group":"B",
					  "feature":["y"]
					}
					""");

			search.addJson("""
					{
					  "id":"4",
					  "body":"document 4",
					  "group":"B",
					  "feature":["y","z"]
					}
					""");

			search.addJson("""
					{
					  "id":"5",
					  "body":"document 5",
					  "group":"B",
					  "feature":["z"]
					}
					""");

			search.commit();

			return search;

		} catch (Exception e) {
			search.close();
			throw e;
		}
	}
}