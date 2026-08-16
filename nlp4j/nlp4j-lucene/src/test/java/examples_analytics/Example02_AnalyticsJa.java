package examples_analytics;

import java.io.File;
import java.util.Map;

import nlp4j.analytics.AnalyticsAggregationBucket;
import nlp4j.analytics.AnalyticsResult;
import nlp4j.analytics.AnalyticsKeyword;
import nlp4j.analytics.LocalAnalytics;
import nlp4j.json.JsonNode;
import nlp4j.lucene.LocalSearch;
import nlp4j.util.CsvUtils;

public class Example02_AnalyticsJa {

	public static void main(String[] args) throws Exception {

		try (LocalSearch search = LocalSearch.builder("ja").autoAnalyze(true).build();) {

			// 正式には Stream から読み出す
			CsvUtils.stream(new File("src/test/resources/examples_analytics/不具合情報一覧_202501-202512_head100.csv"))
					.forEach(d -> {
						System.out.println(d.getAttribute("番号"));
						System.out.println(d.getAttribute("受付日"));
						System.out.println(d.getAttribute("車名"));
						System.out.println(d.getAttribute("型式"));
						System.out.println(d.getAttribute("通称名"));
						System.out.println(d.getAttribute("不具合装置"));
						System.out.println(d.getAttribute("申告内容"));
						System.out.println("---");
						JsonNode json = JsonNode.object() //
								.put("id", "mlit_" + d.getAttributeAsString("番号")) //
								.put("maker", d.getAttributeAsString("車名")) //
								.put("type", d.getAttributeAsString("型式")) //
								.put("name", d.getAttributeAsString("通称名")) //
								.put("malfunction_device", d.getAttributeAsString("不具合装置")) //
								.put("body", d.getAttributeAsString("申告内容")) //
						;
						search.addJson(json.toJson());
					});

			search.commit();

			{

				LocalAnalytics analytics = new LocalAnalytics(search);
				{
					Map<String, Long> agg = search.aggregate("maker", 100);
					agg.forEach((k, v) -> {
						System.out.println(k + "=" + v);
					});
				}
				System.out.println("---");
				{
					AnalyticsResult result = analytics.relativeRate("maker", "ニッサン", "name", 100);

					result.getBuckets().forEach(b -> {
						System.out.println(b.getKeyword().getField() + "," + b.getKeyword().getLex() + ","
								+ b.getRelativeRate() + "," + b.getCount());
					});

				}
				System.out.println("---");
				{
					AnalyticsResult result = analytics.relativeRate("name", "セレナ", "word.noun", 100);
					result.getBuckets().forEach(b -> {
						System.out.println(b.getKeyword().getField() + "," + b.getKeyword().getLex() + ","
								+ b.getRelativeRate() + "," + b.getCount());
					});
				}

			}

		}
	}

}
