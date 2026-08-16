package nlp4j.analytics;

import java.util.List;
import java.util.Map;

import junit.framework.TestCase;
import nlp4j.lucene.LocalSearch;
import nlp4j.lucene.LuceneQueryValidationResult;

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

			AnalyticsResult result = analytics.relativeRate("group", "A", "feature", 100);

			System.out.println("testRelativeRate001: " + result);

			/*
			 * Result 全体。
			 */
			assertEquals("group", result.getQueryField());
			assertEquals("A", result.getQueryValue());
			assertEquals("feature", result.getField());

			assertEquals(2, result.getCount());
			assertEquals(5, result.getTotalCount());

			assertEquals(2, result.getBuckets().size());

			/*
			 * feature=x
			 */
			AnalyticsAggregationBucket x = findBucket(result, "x");

			assertNotNull(x);

			assertEquals("x", x.getKey());
			assertEquals("feature", x.getField());

			assertNotNull(x.getKeyword());
			assertEquals("feature", x.getKeyword().getField());
			assertEquals("x", x.getKeyword().getLex());

			assertEquals(2, x.getCount());
			assertEquals(2, x.getAllCount());

			assertEquals(2.5, x.getRelativeRate(), 0.000001);

			/*
			 * feature=y
			 */
			AnalyticsAggregationBucket y = findBucket(result, "y");

			assertNotNull(y);

			assertEquals("y", y.getKey());
			assertEquals(1, y.getCount());
			assertEquals(3, y.getAllCount());

			assertEquals(0.8333333333, y.getRelativeRate(), 0.000001);

			/*
			 * group=A には z は存在しない。
			 */
			assertNull(findBucket(result, "z"));
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

			AnalyticsResult result = analytics.relativeRate("group", "A", "feature", 100);

			List<AnalyticsAggregationBucket> buckets = result.getBuckets();

			assertEquals(2, buckets.size());

			assertEquals("x", buckets.get(0).getKey());

			assertEquals("y", buckets.get(1).getKey());

			assertTrue(buckets.get(0).getRelativeRate() > buckets.get(1).getRelativeRate());
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

			AnalyticsResult result = analytics.relativeRate("group", "B", "feature", 100);

			System.out.println("testRelativeRate003_GroupB: " + result);

			assertEquals("group", result.getQueryField());
			assertEquals("B", result.getQueryValue());
			assertEquals("feature", result.getField());

			assertEquals(3, result.getCount());
			assertEquals(5, result.getTotalCount());

			assertEquals(2, result.getBuckets().size());

			AnalyticsAggregationBucket z = findBucket(result, "z");

			assertNotNull(z);

			assertEquals(2, z.getCount());
			assertEquals(2, z.getAllCount());

			assertEquals(1.6666666667, z.getRelativeRate(), 0.000001);

			AnalyticsAggregationBucket y = findBucket(result, "y");

			assertNotNull(y);

			assertEquals(2, y.getCount());
			assertEquals(3, y.getAllCount());

			assertEquals(1.1111111111, y.getRelativeRate(), 0.000001);

			/*
			 * relativeRate 降順。
			 */
			assertEquals("z", result.getBuckets().get(0).getKey());

			assertEquals("y", result.getBuckets().get(1).getKey());
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

			AnalyticsResult result = analytics.relativeRate("group", "A", "group", 100);

			System.out.println("testRelativeRate004_SameField: " + result);

			assertEquals("group", result.getField());

			assertEquals(2, result.getCount());
			assertEquals(5, result.getTotalCount());

			assertEquals(1, result.getBuckets().size());

			AnalyticsAggregationBucket bucket = result.getBuckets().get(0);

			assertEquals("A", bucket.getKey());

			assertEquals("group", bucket.getKeyword().getField());

			assertEquals("A", bucket.getKeyword().getLex());

			assertEquals(2, bucket.getCount());
			assertEquals(2, bucket.getAllCount());

			assertEquals(2.5, bucket.getRelativeRate(), 0.000001);
		}
	}

	/**
	 * queryField=queryValue に該当する文書が存在しない場合、 bucket が空の AnalyticsAggregationResult
	 * が返ることを確認する。
	 */
	public void testRelativeRate005_NoQueryDocuments() throws Exception {

		try (LocalSearch search = createStandardSearch()) {

			LocalAnalytics analytics = new LocalAnalytics(search);

			AnalyticsResult result = analytics.relativeRate("group", "NOT_FOUND", "feature", 100);

			assertNotNull(result);

			assertEquals("group", result.getQueryField());

			assertEquals("NOT_FOUND", result.getQueryValue());

			assertEquals("feature", result.getField());

			assertEquals(0, result.getCount());

			assertEquals(5, result.getTotalCount());

			assertTrue(result.getBuckets().isEmpty());
		}
	}

	/**
	 * インデックスが空の場合、 bucket が空の AnalyticsAggregationResult が返ることを確認する。
	 */
	public void testRelativeRate006_EmptyIndex() throws Exception {

		try (LocalSearch search = LocalSearch.builder("en").autoAnalyze(false).build()) {

			search.commit();

			LocalAnalytics analytics = new LocalAnalytics(search);

			AnalyticsResult result = analytics.relativeRate("group", "A", "feature", 100);

			assertNotNull(result);

			assertEquals("group", result.getQueryField());
			assertEquals("A", result.getQueryValue());
			assertEquals("feature", result.getField());

			assertEquals(0, result.getCount());
			assertEquals(0, result.getTotalCount());

			assertTrue(result.getBuckets().isEmpty());
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

			AnalyticsResult result = analytics.relativeRate("group", "A", "feature", 1);

			System.out.println("testRelativeRate007_AggregationSizeFallback: " + result);

			assertEquals(1, result.getCount());
			assertEquals(3, result.getTotalCount());

			assertEquals(1, result.getBuckets().size());

			AnalyticsAggregationBucket x = result.getBuckets().get(0);

			assertEquals("x", x.getKey());

			assertEquals(1, x.getCount());

			/*
			 * aggregationAll(size=1) に x は含まれないが、 fallback の count("feature", "x") によって
			 * allCount=1 が取得できること。
			 */
			assertEquals(1, x.getAllCount());

			assertEquals(3.0, x.getRelativeRate(), 0.000001);
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

			Map<String, AnalyticsResult> result = analytics.relativeRates("group", "feature", 100);

			System.out.println("testRelativeRates001: " + result);

			assertEquals(2, result.size());

			assertTrue(result.containsKey("A"));
			assertTrue(result.containsKey("B"));

			/*
			 * group=A
			 */
			AnalyticsResult groupA = result.get("A");

			assertNotNull(groupA);

			assertEquals("group", groupA.getQueryField());
			assertEquals("A", groupA.getQueryValue());
			assertEquals("feature", groupA.getField());

			assertEquals(2, groupA.getCount());
			assertEquals(5, groupA.getTotalCount());

			AnalyticsAggregationBucket ax = findBucket(groupA, "x");

			AnalyticsAggregationBucket ay = findBucket(groupA, "y");

			assertNotNull(ax);
			assertNotNull(ay);

			assertEquals(2, ax.getCount());
			assertEquals(2, ax.getAllCount());

			assertEquals(2.5, ax.getRelativeRate(), 0.000001);

			assertEquals(1, ay.getCount());
			assertEquals(3, ay.getAllCount());

			assertEquals(0.8333333333, ay.getRelativeRate(), 0.000001);

			/*
			 * group=B
			 */
			AnalyticsResult groupB = result.get("B");

			assertNotNull(groupB);

			assertEquals("group", groupB.getQueryField());
			assertEquals("B", groupB.getQueryValue());
			assertEquals("feature", groupB.getField());

			assertEquals(3, groupB.getCount());
			assertEquals(5, groupB.getTotalCount());

			AnalyticsAggregationBucket bz = findBucket(groupB, "z");

			AnalyticsAggregationBucket by = findBucket(groupB, "y");

			assertNotNull(bz);
			assertNotNull(by);

			assertEquals(2, bz.getCount());
			assertEquals(2, bz.getAllCount());

			assertEquals(1.6666666667, bz.getRelativeRate(), 0.000001);

			assertEquals(2, by.getCount());
			assertEquals(3, by.getAllCount());

			assertEquals(1.1111111111, by.getRelativeRate(), 0.000001);
		}
	}

	/**
	 * relativeRates() の各 AnalyticsAggregationResult の bucket が relativeRate
	 * 降順になっていることを確認する。
	 */
	public void testRelativeRates002_Sorted() throws Exception {

		try (LocalSearch search = createStandardSearch()) {

			LocalAnalytics analytics = new LocalAnalytics(search);

			Map<String, AnalyticsResult> result = analytics.relativeRates("group", "feature", 100);

			{
				List<AnalyticsAggregationBucket> buckets = result.get("A").getBuckets();

				assertEquals(2, buckets.size());

				assertEquals("x", buckets.get(0).getKey());

				assertEquals("y", buckets.get(1).getKey());

				assertTrue(buckets.get(0).getRelativeRate() > buckets.get(1).getRelativeRate());
			}

			{
				List<AnalyticsAggregationBucket> buckets = result.get("B").getBuckets();

				assertEquals(2, buckets.size());

				assertEquals("z", buckets.get(0).getKey());

				assertEquals("y", buckets.get(1).getKey());

				assertTrue(buckets.get(0).getRelativeRate() > buckets.get(1).getRelativeRate());
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

			Map<String, AnalyticsResult> result = analytics.relativeRates("group", "feature", 100);

			assertNotNull(result);

			assertTrue(result.isEmpty());
		}
	}

	// =========================================================
	// AnalyticsKeyword
	// =========================================================

	/**
	 * relativeRate() で生成される AnalyticsKeyword に aggregationField と lex
	 * が正しく設定されることを確認する。
	 */
	public void testAnalyticsKeyword001() throws Exception {

		try (LocalSearch search = createStandardSearch()) {

			LocalAnalytics analytics = new LocalAnalytics(search);

			AnalyticsResult result = analytics.relativeRate("group", "A", "feature", 100);

			AnalyticsAggregationBucket bucket = findBucket(result, "x");

			assertNotNull(bucket);

			AnalyticsKeyword keyword = bucket.getKeyword();

			assertNotNull(keyword);

			assertEquals("feature", keyword.getField());

			assertEquals("x", keyword.getLex());

			/*
			 * Bucket の shortcut API と同じ値になること。
			 */
			assertEquals(keyword.getField(), bucket.getField());

			assertEquals(keyword.getLex(), bucket.getKey());
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

	public void testValidateLuceneQuery001() throws Exception {
		try (LocalSearch search = LocalSearch.builder("ja").build()) {
			String q = "京都 AND (寺院 OR 神社)";
			LuceneQueryValidationResult result = search.validateLuceneQuery(q);
			if (result.isValid()) {
				System.out.println("Valid query");
			} else {
				System.out.println("Invalid query: " + result.getMessage());
				fail();
			}
		}
	}

	public void testValidateLuceneQuery002() throws Exception {
		try (LocalSearch search = LocalSearch.builder("ja").build()) {
			String q = "京都 AND (寺院 OR 神社";
			LuceneQueryValidationResult result = search.validateLuceneQuery(q);
			if (result.isValid()) {
				System.out.println("Valid query");
				fail();
			} else {
				System.out.println("Invalid query: " + result.getMessage());
			}
		}
	}

	// =========================================================
	// Helper
	// =========================================================

	/**
	 * AnalyticsAggregationResult から 指定された key の bucket を取得する。
	 *
	 * @param result aggregation result
	 * @param key    bucket key
	 * @return bucket。存在しない場合は null
	 */
	private AnalyticsAggregationBucket findBucket(AnalyticsResult result, String key) {

		for (AnalyticsAggregationBucket bucket : result.getBuckets()) {

			if (key.equals(bucket.getKey())) {
				return bucket;
			}
		}

		return null;
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