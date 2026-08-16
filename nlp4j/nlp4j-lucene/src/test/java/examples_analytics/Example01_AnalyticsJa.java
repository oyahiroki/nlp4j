package examples_analytics;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nlp4j.analytics.AnalyticsAggregationBucket;
import nlp4j.analytics.AnalyticsResult;
import nlp4j.analytics.LocalAnalytics;
import nlp4j.lucene.LocalSearch;

public class Example01_AnalyticsJa {

	public static void main(String[] args) throws Exception {
		try (LocalSearch search = new LocalSearch("ja")) {
//			search.add("1", "ニッサン ドアが破損した");
//			search.add("2", "ニッサン ドアが動かない");
//			search.add("3", "ニッサン ミラーが動かない");
//			search.add("4", "トヨタ ドアが外れた");
//			search.add("5", "トヨタ ブレーキが効かない");
//			search.add("6", "トヨタ ドアから水が入った");
			{
				search.addJson("""
						{
						  "id":"1",
						  "body":"ミラーが動かない",
						  "maker":"ニッサン"
						}
						""");

				search.addJson("""
						{
						  "id":"2",
						  "body":"ドアが破損",
						  "maker":"ニッサン"
						}
						""");

				search.addJson("""
						{
						  "id":"3",
						  "body":"ドアが動かない",
						  "maker":"トヨタ"
						}
						""");
			}
			search.commit();

			{

				LocalAnalytics analytics = new LocalAnalytics(search);

				AnalyticsResult result = analytics.relativeRate("maker", "ニッサン", "word.noun", 100);

				for (AnalyticsAggregationBucket b : result.getBuckets()) {
					System.out.println(b.toString());
				}

				assertEquals(3, result.getTotalCount());
				{
					assertEquals("破損", result.getBuckets().get(0).getKeyword().getLex());
					assertEquals(1.5, result.getBuckets().get(0).getRelativeRate());
				}
				{
					assertEquals("ミラー", result.getBuckets().get(1).getKeyword().getLex());
					assertEquals(1.5, result.getBuckets().get(1).getRelativeRate());
				}

			}

		}

	}

	private static void assertEquals(String s1, String s2) {
		if (s1 == null && s2 != null) {
			throw new RuntimeException("" + s1 + " != " + s2);
		} else {
			if (s1.equals(s2) == false) {
				throw new RuntimeException("" + s1 + " != " + s2);
			}
		}

	}

	private static void assertEquals(Number n1, Number n2) {
		if (n1.doubleValue() != n2.doubleValue()) {
			throw new RuntimeException("" + n1 + " != " + n2);
		}

	}

}
