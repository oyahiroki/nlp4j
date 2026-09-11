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

	// =========================================================
	// relativeRateLucene()
	// =========================================================

	/**
	 * relativeRateLucene() の基本動作を確認する。
	 *
	 * <p>
	 * group:A という Lucene Query で relativeRate を計算し、
	 * relativeRate(field, value, ...) と数学的に同じ結果になることを確認する。
	 * </p>
	 *
	 * <pre>
	 * Documents: createStandardSearch() を使用
	 *
	 * relativeRate("group","A","feature",100) と
	 * relativeRateLucene("group:A","feature",100) の結果が一致すること。
	 * </pre>
	 */
	public void testRelativeRateLucene001() throws Exception {

		try (LocalSearch search = createStandardSearch()) {

			LocalAnalytics analytics = new LocalAnalytics(search);

			AnalyticsResult r1 = analytics.relativeRate("group", "A", "feature", 100);

			AnalyticsResult r2 = analytics.relativeRateLucene("group:A", "feature", 100);

			System.out.println("testRelativeRateLucene001 r1: " + r1);
			System.out.println("testRelativeRateLucene001 r2: " + r2);

			// count, totalCount が一致すること
			assertEquals(r1.getCount(), r2.getCount());
			assertEquals(r1.getTotalCount(), r2.getTotalCount());

			// bucket 数が一致すること
			assertEquals(r1.getBuckets().size(), r2.getBuckets().size());

			// 各 bucket の count, allCount, relativeRate が一致すること
			AnalyticsAggregationBucket x1 = findBucket(r1, "x");
			AnalyticsAggregationBucket x2 = findBucket(r2, "x");

			assertNotNull(x1);
			assertNotNull(x2);

			assertEquals(x1.getCount(), x2.getCount());
			assertEquals(x1.getAllCount(), x2.getAllCount());
			assertEquals(x1.getRelativeRate(), x2.getRelativeRate(), 0.000001);

			AnalyticsAggregationBucket y1 = findBucket(r1, "y");
			AnalyticsAggregationBucket y2 = findBucket(r2, "y");

			assertNotNull(y1);
			assertNotNull(y2);

			assertEquals(y1.getCount(), y2.getCount());
			assertEquals(y1.getAllCount(), y2.getAllCount());
			assertEquals(y1.getRelativeRate(), y2.getRelativeRate(), 0.000001);

			// r2 の query が LUCENE 種別であること
			assertEquals(AnalyticsQuery.Kind.LUCENE, r2.getQuery().getKind());
			assertEquals("group:A", r2.getLuceneQuery());
			assertNull(r2.getQueryField());
			assertNull(r2.getQueryValue());
		}
	}

	/**
	 * relativeRateLucene() の結果が relativeRate 降順になっていることを確認する。
	 */
	public void testRelativeRateLuceneSorted001() throws Exception {

		try (LocalSearch search = createStandardSearch()) {

			LocalAnalytics analytics = new LocalAnalytics(search);

			AnalyticsResult result = analytics.relativeRateLucene("group:A", "feature", 100);

			List<AnalyticsAggregationBucket> buckets = result.getBuckets();

			assertEquals(2, buckets.size());

			// x(2.5) > y(0.833...) の順になること
			assertEquals("x", buckets.get(0).getKey());
			assertEquals("y", buckets.get(1).getKey());
			assertTrue(buckets.get(0).getRelativeRate() > buckets.get(1).getRelativeRate());
		}
	}

	/**
	 * relativeRateLucene() で条件に一致する文書がない場合、bucket が空の AnalyticsResult が返ることを確認する。
	 */
	public void testRelativeRateLuceneNoMatch001() throws Exception {

		try (LocalSearch search = createStandardSearch()) {

			LocalAnalytics analytics = new LocalAnalytics(search);

			AnalyticsResult result = analytics.relativeRateLucene("group:NOTFOUND", "feature", 100);

			assertNotNull(result);

			assertEquals(AnalyticsQuery.Kind.LUCENE, result.getQuery().getKind());
			assertEquals("feature", result.getField());

			assertEquals(0, result.getCount());
			assertEquals(5, result.getTotalCount());

			assertTrue(result.getBuckets().isEmpty());
		}
	}

	/**
	 * relativeRateLucene() で空インデックスの場合、bucket が空の AnalyticsResult が返ることを確認する。
	 */
	public void testRelativeRateLuceneEmptyIndex001() throws Exception {

		try (LocalSearch search = LocalSearch.builder("en").autoAnalyze(false).build()) {

			search.commit();

			LocalAnalytics analytics = new LocalAnalytics(search);

			AnalyticsResult result = analytics.relativeRateLucene("group:A", "feature", 100);

			assertNotNull(result);

			assertEquals(AnalyticsQuery.Kind.LUCENE, result.getQuery().getKind());
			assertEquals("feature", result.getField());

			assertEquals(0, result.getCount());
			assertEquals(0, result.getTotalCount());

			assertTrue(result.getBuckets().isEmpty());
		}
	}

	/**
	 * relativeRateLucene() で aggregation size が小さい場合、fallback count が動作することを確認する。
	 *
	 * <pre>
	 * Documents:
	 *   id=1 group=A feature=x
	 *   id=2 group=B feature=y
	 *   id=3 group=B feature=y
	 *
	 * relativeRateLucene("group:A", "feature", 1):
	 *   aggregationAll(size=1) には y のみ含まれるが、
	 *   group:A 内では x が唯一の feature。
	 *   fallback の count("feature","x") によって allCount=1 が取得できること。
	 * </pre>
	 */
	public void testRelativeRateLuceneAggregationSizeFallback001() throws Exception {

		try (LocalSearch search = LocalSearch.builder("en").autoAnalyze(false).build()) {

			search.addJson("""
					{"id":"1","body":"doc1","group":"A","feature":"x"}
					""");
			search.addJson("""
					{"id":"2","body":"doc2","group":"B","feature":"y"}
					""");
			search.addJson("""
					{"id":"3","body":"doc3","group":"B","feature":"y"}
					""");
			search.commit();

			LocalAnalytics analytics = new LocalAnalytics(search);

			AnalyticsResult result = analytics.relativeRateLucene("group:A", "feature", 1);

			System.out.println("testRelativeRateLuceneAggregationSizeFallback001: " + result);

			assertEquals(1, result.getCount());
			assertEquals(3, result.getTotalCount());
			assertEquals(1, result.getBuckets().size());

			AnalyticsAggregationBucket x = result.getBuckets().get(0);
			assertEquals("x", x.getKey());
			assertEquals(1, x.getCount());
			assertEquals(1, x.getAllCount());
			assertEquals(3.0, x.getRelativeRate(), 0.000001);
		}
	}

	/**
	 * size（candidateSize）が小さい場合、count が少ないが relativeRate が高い項目は
	 * aggregateLucene() の返す候補から外れることを確認する。
	 *
	 * <p>
	 * これは設計上の注意点です。size は「表示件数」ではなく「relativeRate 計算候補数」です。
	 * </p>
	 *
	 * <pre>
	 * Documents:
	 *   group=Query: A=10, B=8, C=5, Z=2  (countQuery=25)
	 *   group=Other: A=10, B=12, C=15     (37件)
	 *   countAll=62, feature allCount: A=20, B=20, C=20, Z=2
	 *
	 * relativeRate 計算:
	 *   Z: targetRate = 2/25 = 0.08, allRate = 2/62 ≈ 0.032, relativeRate ≈ 2.5
	 *   A: targetRate = 10/25 = 0.4,  allRate = 20/62 ≈ 0.32,  relativeRate ≈ 1.25
	 *   B: targetRate = 8/25 = 0.32,  allRate = 20/62 ≈ 0.32,  relativeRate ≈ 1.0
	 *   C: targetRate = 5/25 = 0.2,   allRate = 20/62 ≈ 0.32,  relativeRate ≈ 0.625
	 *   → Z の relativeRate が最高
	 *
	 * size=3 の場合:
	 *   aggregateLucene(size=3) → count上位3件 A,B,C のみ返す → Z は候補に入らない
	 *   結果として、relativeRate が最高の Z が欠落する
	 *
	 * size=10 の場合:
	 *   全件返す → Z も候補に含まれ、relativeRate最高として先頭に来る
	 * </pre>
	 */
	public void testRelativeRateLuceneCandidateSize001() throws Exception {

		try (LocalSearch search = LocalSearch.builder("en").autoAnalyze(false).build()) {

			// group=Query の文書（countQuery = 25）
			// feature=A: 10件
			for (int i = 0; i < 10; i++) {
				search.addJson("{\"id\":\"qa" + i + "\",\"body\":\"doc\",\"group\":\"Query\",\"feature\":\"A\"}");
			}
			// feature=B: 8件
			for (int i = 0; i < 8; i++) {
				search.addJson("{\"id\":\"qb" + i + "\",\"body\":\"doc\",\"group\":\"Query\",\"feature\":\"B\"}");
			}
			// feature=C: 5件
			for (int i = 0; i < 5; i++) {
				search.addJson("{\"id\":\"qc" + i + "\",\"body\":\"doc\",\"group\":\"Query\",\"feature\":\"C\"}");
			}
			// feature=Z: 2件（count少 → aggregation size=3 では候補から外れる）
			for (int i = 0; i < 2; i++) {
				search.addJson("{\"id\":\"qz" + i + "\",\"body\":\"doc\",\"group\":\"Query\",\"feature\":\"Z\"}");
			}
			// group=Other の文書（全体 allCount 用）
			// feature=A: 追加10件 → allCount=20
			for (int i = 0; i < 10; i++) {
				search.addJson("{\"id\":\"oa" + i + "\",\"body\":\"doc\",\"group\":\"Other\",\"feature\":\"A\"}");
			}
			// feature=B: 追加12件 → allCount=20
			for (int i = 0; i < 12; i++) {
				search.addJson("{\"id\":\"ob" + i + "\",\"body\":\"doc\",\"group\":\"Other\",\"feature\":\"B\"}");
			}
			// feature=C: 追加15件 → allCount=20
			for (int i = 0; i < 15; i++) {
				search.addJson("{\"id\":\"oc" + i + "\",\"body\":\"doc\",\"group\":\"Other\",\"feature\":\"C\"}");
			}
			// feature=Z: 追加なし → allCount=2（全体でも2件のみ）

			search.commit();

			LocalAnalytics analytics = new LocalAnalytics(search);

			// -----------------------------------------------------------
			// size=3 の場合: A,B,C のみ候補 → Z は欠落
			// -----------------------------------------------------------
			AnalyticsResult small = analytics.relativeRateLucene("group:Query", "feature", 3);

			System.out.println("testRelativeRateLuceneCandidateSize001 size=3: " + small);

			// Z は candidates に入らないため bucket に存在しない
			assertNull("size=3 では Z は候補から外れること",
					findBucket(small, "Z"));

			// A,B,C は含まれる（最大3件）
			assertNotNull(findBucket(small, "A"));
			assertNotNull(findBucket(small, "B"));
			assertNotNull(findBucket(small, "C"));

			// -----------------------------------------------------------
			// size=10 の場合: Z も候補に入り、relativeRate 最高で先頭になる
			// -----------------------------------------------------------
			AnalyticsResult large = analytics.relativeRateLucene("group:Query", "feature", 10);

			System.out.println("testRelativeRateLuceneCandidateSize001 size=10: " + large);

			// Z は含まれること
			AnalyticsAggregationBucket z = findBucket(large, "Z");
			assertNotNull("size=10 では Z が候補に含まれること", z);

			// Z の relativeRate が最高（先頭）であること
			// Z:
			// targetRate = 2 / 25 = 0.08
			// allRate    = 2 / 62 ≈ 0.03226
			// relativeRate ≈ 2.48
			//
			// A:
			// targetRate = 10 / 25 = 0.4
			// allRate    = 20 / 62 ≈ 0.32258
			// relativeRate ≈ 1.24
			assertEquals("Z は relativeRate 最高のため先頭であること",
					"Z", large.getBuckets().get(0).getKey());

			// Z の relativeRate は A,B,C より高いこと
			AnalyticsAggregationBucket a = findBucket(large, "A");
			assertNotNull(a);
			assertTrue("Z の relativeRate が A より高いこと",
					z.getRelativeRate() > a.getRelativeRate());

			// countAll（全文書数）は 62 であること
			// group=Query: A=10, B=8, C=5, Z=2 → 25件
			// group=Other: A=10, B=12, C=15     → 37件
			// 合計: 62件
			assertEquals(62, large.getTotalCount());
			// countQuery（group=Query の文書数）は 25 であること
			assertEquals(25, large.getCount());
		}
	}

	/**
	 * size（candidateSize）の役割を relativeRate() の既存 API でも確認する。
	 *
	 * <p>
	 * size が候補数を制御することは relativeRate() と relativeRateLucene() で共通の挙動です。
	 * このテストは relativeRate() の同等ケースを確認します。
	 * </p>
	 *
	 * <pre>
	 * 同じデータセットに対して:
	 *   size=3: Z は候補から外れる
	 *   size=10: Z は候補に含まれ relativeRate 最高
	 * </pre>
	 */
	public void testRelativeRateCandidateSize001() throws Exception {

		try (LocalSearch search = LocalSearch.builder("en").autoAnalyze(false).build()) {

			// group=Query: A=10, B=8, C=5, Z=2
			for (int i = 0; i < 10; i++) {
				search.addJson("{\"id\":\"qa" + i + "\",\"body\":\"doc\",\"group\":\"Query\",\"feature\":\"A\"}");
			}
			for (int i = 0; i < 8; i++) {
				search.addJson("{\"id\":\"qb" + i + "\",\"body\":\"doc\",\"group\":\"Query\",\"feature\":\"B\"}");
			}
			for (int i = 0; i < 5; i++) {
				search.addJson("{\"id\":\"qc" + i + "\",\"body\":\"doc\",\"group\":\"Query\",\"feature\":\"C\"}");
			}
			for (int i = 0; i < 2; i++) {
				search.addJson("{\"id\":\"qz" + i + "\",\"body\":\"doc\",\"group\":\"Query\",\"feature\":\"Z\"}");
			}
			// group=Other: A=10, B=12, C=15, Z なし
			for (int i = 0; i < 10; i++) {
				search.addJson("{\"id\":\"oa" + i + "\",\"body\":\"doc\",\"group\":\"Other\",\"feature\":\"A\"}");
			}
			for (int i = 0; i < 12; i++) {
				search.addJson("{\"id\":\"ob" + i + "\",\"body\":\"doc\",\"group\":\"Other\",\"feature\":\"B\"}");
			}
			for (int i = 0; i < 15; i++) {
				search.addJson("{\"id\":\"oc" + i + "\",\"body\":\"doc\",\"group\":\"Other\",\"feature\":\"C\"}");
			}
			search.commit();

			LocalAnalytics analytics = new LocalAnalytics(search);

			// size=3: Z は候補から外れる
			AnalyticsResult small = analytics.relativeRate("group", "Query", "feature", 3);
			System.out.println("testRelativeRateCandidateSize001 size=3: " + small);
			assertNull("size=3 では Z は候補から外れること", findBucket(small, "Z"));

			// size=10: Z が含まれて relativeRate 最高
			AnalyticsResult large = analytics.relativeRate("group", "Query", "feature", 10);
			System.out.println("testRelativeRateCandidateSize001 size=10: " + large);

			AnalyticsAggregationBucket z = findBucket(large, "Z");
			assertNotNull("size=10 では Z が候補に含まれること", z);
			assertEquals("Z は relativeRate 最高のため先頭であること",
					"Z", large.getBuckets().get(0).getKey());
		}
	}

	// =========================================================
	// relativeRate() / relativeRates() - 特殊文字値
	// =========================================================

	/**
	 * queryValue にスペースを含む値（例: "Nissan Motor"）を渡した場合でも
	 * relativeRate() が正常に動作することを確認する。
	 *
	 * <p>
	 * 以前の実装では "maker:Nissan Motor" という不正な Lucene Query 文字列を生成していたが、
	 * filters パラメータを使う実装では正確に一致する。
	 * </p>
	 *
	 * <pre>
	 * Documents:
	 *   id=1 maker="Nissan Motor" feature=x
	 *   id=2 maker="Nissan Motor" feature=y
	 *   id=3 maker=Toyota         feature=y
	 *
	 * relativeRate("maker", "Nissan Motor", "feature", 100):
	 *   countQuery = 2 (maker="Nissan Motor")
	 *   countAll   = 3
	 *
	 *   feature=x: targetRate=1/2=0.5, allRate=1/3≈0.333, relativeRate≈1.5
	 *   feature=y: targetRate=1/2=0.5, allRate=2/3≈0.667, relativeRate≈0.75
	 * </pre>
	 */
	public void testRelativeRateSpecialValue_Space() throws Exception {

		try (LocalSearch search = LocalSearch.builder("en").autoAnalyze(false).build()) {

			search.addJson("{\"id\":\"1\",\"body\":\"doc1\",\"maker\":\"Nissan Motor\",\"feature\":\"x\"}");
			search.addJson("{\"id\":\"2\",\"body\":\"doc2\",\"maker\":\"Nissan Motor\",\"feature\":\"y\"}");
			search.addJson("{\"id\":\"3\",\"body\":\"doc3\",\"maker\":\"Toyota\",\"feature\":\"y\"}");
			search.commit();

			LocalAnalytics analytics = new LocalAnalytics(search);

			AnalyticsResult result = analytics.relativeRate("maker", "Nissan Motor", "feature", 100);

			System.out.println("testRelativeRateSpecialValue_Space: " + result);

			assertEquals("Nissan Motor", result.getQueryValue());
			assertEquals(2, result.getCount());
			assertEquals(3, result.getTotalCount());
			assertEquals(2, result.getBuckets().size());

			AnalyticsAggregationBucket x = findBucket(result, "x");
			assertNotNull("feature=x が結果に含まれること", x);
			assertEquals(1, x.getCount());
			assertEquals(1, x.getAllCount());
			assertEquals(1.5, x.getRelativeRate(), 0.000001);

			AnalyticsAggregationBucket y = findBucket(result, "y");
			assertNotNull("feature=y が結果に含まれること", y);
			assertEquals(1, y.getCount());
			assertEquals(2, y.getAllCount());
			assertEquals(0.75, y.getRelativeRate(), 0.000001);
		}
	}

	/**
	 * queryValue に Lucene 特殊文字（コロン）を含む値（例: "A:B"）を渡した場合でも
	 * relativeRate() が正常に動作することを確認する。
	 *
	 * <p>
	 * 以前の実装では "maker:A:B" という不正な Lucene Query 文字列を生成していた。
	 * filters パラメータを使う実装では正確に一致する。
	 * </p>
	 */
	public void testRelativeRateSpecialValue_Colon() throws Exception {

		try (LocalSearch search = LocalSearch.builder("en").autoAnalyze(false).build()) {

			search.addJson("{\"id\":\"1\",\"body\":\"doc1\",\"maker\":\"A:B\",\"feature\":\"x\"}");
			search.addJson("{\"id\":\"2\",\"body\":\"doc2\",\"maker\":\"A:B\",\"feature\":\"y\"}");
			search.addJson("{\"id\":\"3\",\"body\":\"doc3\",\"maker\":\"Toyota\",\"feature\":\"y\"}");
			search.commit();

			LocalAnalytics analytics = new LocalAnalytics(search);

			AnalyticsResult result = analytics.relativeRate("maker", "A:B", "feature", 100);

			System.out.println("testRelativeRateSpecialValue_Colon: " + result);

			assertEquals("A:B", result.getQueryValue());
			assertEquals(2, result.getCount());
			assertEquals(3, result.getTotalCount());
			assertEquals(2, result.getBuckets().size());

			assertNotNull("feature=x が結果に含まれること", findBucket(result, "x"));
			assertNotNull("feature=y が結果に含まれること", findBucket(result, "y"));
		}
	}

	// =========================================================
	// relativeRates() - 4引数オーバーロード
	// =========================================================

	/**
	 * relativeRates(queryField, aggregationField, queryValueSize, candidateSize) の基本動作を確認する。
	 *
	 * <p>
	 * 3引数版（size, size）と4引数版（size, size）の結果が同じになることを確認する。
	 * </p>
	 */
	public void testRelativeRates_FourArgs_Basic() throws Exception {

		try (LocalSearch search = createStandardSearch()) {

			LocalAnalytics analytics = new LocalAnalytics(search);

			Map<String, AnalyticsResult> r3 = analytics.relativeRates("group", "feature", 100);
			Map<String, AnalyticsResult> r4 = analytics.relativeRates("group", "feature", 100, 100);

			assertEquals("4引数版と3引数版のキー数が一致すること", r3.size(), r4.size());
			assertTrue(r4.containsKey("A"));
			assertTrue(r4.containsKey("B"));

			AnalyticsResult a3 = r3.get("A");
			AnalyticsResult a4 = r4.get("A");

			assertEquals(a3.getCount(), a4.getCount());
			assertEquals(a3.getTotalCount(), a4.getTotalCount());
			assertEquals(a3.getBuckets().size(), a4.getBuckets().size());
		}
	}

	/**
	 * relativeRates() の 4引数版で queryValueSize を小さくした場合、
	 * queryField の処理される値の種類が制限されることを確認する。
	 *
	 * <pre>
	 * group=A=2, group=B=3 の文書がある。
	 * aggregate(queryField, queryValueSize=1) は doc_count 最大の B のみ返す。
	 * → 結果は B のみ含まれ、A は含まれないこと。
	 * </pre>
	 */
	public void testRelativeRates_FourArgs_QueryValueSize() throws Exception {

		try (LocalSearch search = createStandardSearch()) {

			LocalAnalytics analytics = new LocalAnalytics(search);

			// queryValueSize=1 のため、doc_count 最大の group(B=3) のみ処理される
			Map<String, AnalyticsResult> result = analytics.relativeRates("group", "feature", 1, 100);

			System.out.println("testRelativeRates_FourArgs_QueryValueSize: " + result);

			assertEquals("queryValueSize=1 のため結果は1件", 1, result.size());
			assertTrue("B のみ含まれること", result.containsKey("B"));
			assertFalse("A は含まれないこと", result.containsKey("A"));
		}
	}

	/**
	 * relativeRates() の 4引数版で candidateSize に 0 を渡した場合、
	 * IllegalArgumentException がスローされることを確認する。
	 */
	public void testRelativeRates_FourArgs_Validation_CandidateSize() throws Exception {

		try (LocalSearch search = createStandardSearch()) {

			LocalAnalytics analytics = new LocalAnalytics(search);

			try {
				analytics.relativeRates("group", "feature", 100, 0);
				fail("candidateSize=0 は IllegalArgumentException がスローされること");
			} catch (IllegalArgumentException e) {
				assertEquals("candidateSize must be greater than 0", e.getMessage());
			}
		}
	}

	// =========================================================
	// AnalyticsResult - toString
	// =========================================================

	/**
	 * AnalyticsResult.toString() が "AnalyticsResult [" で始まることを確認する。
	 */
	public void testAnalyticsResultToString001() throws Exception {

		AnalyticsQuery query = AnalyticsQuery.fieldValue("maker", "Nissan");
		AnalyticsResult result = new AnalyticsResult(query, "word.noun", 10, 100);

		String str = result.toString();
		System.out.println("testAnalyticsResultToString001: " + str);

		assertTrue("toString は 'AnalyticsResult [' で始まること", str.startsWith("AnalyticsResult ["));
	}

	// =========================================================
	// ソート安定化
	// =========================================================

	/**
	 * relativeRate が同一の bucket が複数ある場合、count 降順 → key 昇順 でソートされることを確認する。
	 *
	 * <pre>
	 * Documents:
	 *   group=A: feature=p x3, feature=q x3, feature=r x1
	 *   group=B: feature=r x1   ← r の全体 allCount を 2 にするための追加
	 *
	 *   countAll=8, countQuery=7 (group=A)
	 *
	 *   feature=p: targetRate=3/7, allRate=3/8, relativeRate=(3/7)/(3/8)=8/7≈1.1428
	 *   feature=q: targetRate=3/7, allRate=3/8, relativeRate=8/7≈1.1428  （p と同値）
	 *   feature=r: targetRate=1/7, allRate=2/8=1/4, relativeRate=(1/7)/(1/4)=4/7≈0.5714
	 *
	 * relativeRate が同じ p と q は count 降順(同数)→ key 昇順 で p → q の順になること。
	 * </pre>
	 */
	public void testSortStability001() throws Exception {

		try (LocalSearch search = LocalSearch.builder("en").autoAnalyze(false).build()) {

			// group=A: p=3, q=3, r=1
			for (int i = 0; i < 3; i++) {
				search.addJson("{\"id\":\"p" + i + "\",\"body\":\"doc\",\"group\":\"A\",\"feature\":\"p\"}");
			}
			for (int i = 0; i < 3; i++) {
				search.addJson("{\"id\":\"q" + i + "\",\"body\":\"doc\",\"group\":\"A\",\"feature\":\"q\"}");
			}
			search.addJson("{\"id\":\"r0\",\"body\":\"doc\",\"group\":\"A\",\"feature\":\"r\"}");
			// 全体で r=2 にするため別グループの r を追加
			search.addJson("{\"id\":\"r1\",\"body\":\"doc\",\"group\":\"B\",\"feature\":\"r\"}");
			search.commit();

			LocalAnalytics analytics = new LocalAnalytics(search);

			AnalyticsResult result = analytics.relativeRate("group", "A", "feature", 100);

			System.out.println("testSortStability001: " + result);

			List<AnalyticsAggregationBucket> buckets = result.getBuckets();
			assertEquals(3, buckets.size());

			// relativeRate=1.0 の p と q は key 昇順で p → q
			assertEquals("p", buckets.get(0).getKey());
			assertEquals("q", buckets.get(1).getKey());
			// r は relativeRate=0.5 で末尾
			assertEquals("r", buckets.get(2).getKey());

			// p, q: (3/7) / (3/8) = 8/7 ≈ 1.1428571428571428
			assertEquals(8.0 / 7.0, buckets.get(0).getRelativeRate(), 0.000001);
			assertEquals(8.0 / 7.0, buckets.get(1).getRelativeRate(), 0.000001);
			// r: (1/7) / (2/8) = (1/7) / (1/4) = 4/7 ≈ 0.5714285714285714
			assertEquals(4.0 / 7.0, buckets.get(2).getRelativeRate(), 0.000001);
		}
	}

	// =========================================================
	// relativeRates() - 空インデックスは mutable Map を返す
	// =========================================================

	/**
	 * relativeRates() が空インデックスの場合に返す Map が mutable であることを確認する。
	 *
	 * <p>
	 * 空インデックス時の戻り値と通常時の戻り値が同様に mutable Map であること。
	 * </p>
	 */
	public void testRelativeRates_EmptyReturnsLinkedHashMap() throws Exception {

		try (LocalSearch search = LocalSearch.builder("en").autoAnalyze(false).build()) {

			search.commit();

			LocalAnalytics analytics = new LocalAnalytics(search);

			Map<String, AnalyticsResult> result = analytics.relativeRates("group", "feature", 100);

			assertNotNull(result);
			assertTrue(result.isEmpty());

			// UnsupportedOperationException が投げられないこと
			try {
				result.put("test", null);
			} catch (UnsupportedOperationException e) {
				fail("空インデックス時の戻り値 Map が mutable であること（UnsupportedOperationException は不可）");
			}
		}
	}

	// =========================================================
	// AnalyticsResult - count <= totalCount バリデーション
	// =========================================================

	/**
	 * AnalyticsResult のコンストラクタで count > totalCount の場合、
	 * IllegalArgumentException がスローされることを確認する。
	 */
	public void testAnalyticsResultCountExceedsTotalCount() throws Exception {

		AnalyticsQuery query = AnalyticsQuery.fieldValue("maker", "Nissan");

		try {
			new AnalyticsResult(query, "word.noun", 10, 5); // count(10) > totalCount(5)
			fail("count > totalCount では IllegalArgumentException が期待される");
		} catch (IllegalArgumentException e) {
			assertTrue("例外メッセージに 'count must be <= totalCount' が含まれること",
					e.getMessage().contains("count must be <= totalCount"));
		}
	}

	/**
	 * count == totalCount は許可されることを確認する。
	 */
	public void testAnalyticsResultCountEqualsTotalCount() throws Exception {

		AnalyticsQuery query = AnalyticsQuery.fieldValue("maker", "Nissan");

		// count == totalCount は正常ケース
		AnalyticsResult result = new AnalyticsResult(query, "word.noun", 5, 5);
		assertEquals(5, result.getCount());
		assertEquals(5, result.getTotalCount());
	}

	/**
	 * count = 0, totalCount = 0 は許可されることを確認する（空インデックスケース）。
	 */
	public void testAnalyticsResultCountZeroTotalCountZero() throws Exception {

		AnalyticsQuery query = AnalyticsQuery.fieldValue("maker", "Nissan");

		AnalyticsResult result = new AnalyticsResult(query, "word.noun", 0, 0);
		assertEquals(0, result.getCount());
		assertEquals(0, result.getTotalCount());
	}

	public void testValidateLuceneQuery001() throws Exception {
		try (LocalSearch search = LocalSearch.builder("ja").build()) {
			String q = "京都 AND (寺院 OR 神社)";
			LuceneQueryValidationResult result = search.validateQuery(q);
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
			LuceneQueryValidationResult result = search.validateQuery(q);
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