package examples;

import java.util.List;
import java.util.Map;

import nlp4j.analytics.AnalyticsAggregationBucket;
import nlp4j.analytics.AnalyticsResult;
import nlp4j.analytics.LocalAnalytics;
import nlp4j.lucene.LocalSearch;

public class Example23_View {

	public static void main(String[] args) throws Exception {

		try (LocalSearch search = LocalSearch.builder("en").autoAnalyze(false).build()) {

			// -------------------------------------------------
			// Add documents
			// -------------------------------------------------

			search.addJson("""
					{
					  "id": "1",
					  "body": "Nissan reported a broken door mirror.",
					  "maker": "Nissan",
					  "category": "body",
					  "part": "door mirror"
					}
					""");

			search.addJson("""
					{
					  "id": "2",
					  "body": "Nissan reported a door mirror failure.",
					  "maker": "Nissan",
					  "category": "body",
					  "part": "door mirror"
					}
					""");

			search.addJson("""
					{
					  "id": "3",
					  "body": "Nissan reported a battery problem.",
					  "maker": "Nissan",
					  "category": "electrical",
					  "part": "battery"
					}
					""");

			search.addJson("""
					{
					  "id": "4",
					  "body": "Toyota reported a brake problem.",
					  "maker": "Toyota",
					  "category": "brake",
					  "part": "brake"
					}
					""");

			search.addJson("""
					{
					  "id": "5",
					  "body": "Toyota reported a battery problem.",
					  "maker": "Toyota",
					  "category": "electrical",
					  "part": "battery"
					}
					""");

			search.addJson("""
					{
					  "id": "6",
					  "body": "Honda reported a brake problem.",
					  "maker": "Honda",
					  "category": "brake",
					  "part": "brake"
					}
					""");

			search.commit();

			// =================================================
			// 1. Equivalent to engine.view()
			// =================================================

			System.out.println("=== View: aggregatable fields ===");
			System.out.println("Format: field | value (document count)");
			System.out.println();

			List<String> fields = search.getAggregatableFields();

			for (String field : fields) {

				// engine.view() では上位3件程度
				Map<String, Long> values = search.aggregate(field, 3);

				// 値が存在しないフィールドは表示しない
				if (values.isEmpty()) {
					continue;
				}

				System.out.print(field + " | ");

				boolean first = true;

				for (Map.Entry<String, Long> entry : values.entrySet()) {

					if (!first) {
						System.out.print(", ");
					}

					System.out.print(entry.getKey() + " (" + entry.getValue() + ")");

					first = false;
				}

				System.out.println();
			}

			// =================================================
			// 2. Equivalent to engine.view("category")
			// =================================================

			System.out.println();
			System.out.println("=== View: category ===");
			System.out.println("Values are ordered by document count.");
			System.out.println();

			Map<String, Long> categories = search.aggregate("category", 10);

			System.out.printf("%-20s %8s%n", "Value", "Count");

			System.out.printf("%-20s %8s%n", "--------------------", "--------");

			for (Map.Entry<String, Long> entry : categories.entrySet()) {

				System.out.printf("%-20s %8d%n", entry.getKey(), entry.getValue());
			}

			// =================================================
			// 3. Equivalent to
			// engine.view("part", "maker:Nissan")
			// =================================================

			String luceneQuery = "maker:Nissan";
			int candidateSize = 1000;

			LocalAnalytics analytics = new LocalAnalytics(search);

			AnalyticsResult result = analytics.relativeRateLucene(luceneQuery, "part", candidateSize);

			System.out.println();
			System.out.println("=== View: part ===");
			System.out.println("Lucene query: " + luceneQuery);

			System.out.println("Matched documents: " + result.getCount() + " / " + result.getTotalCount());

			System.out.println("Values are ordered by relative rate.");

			System.out.println();

			System.out.printf("%-20s %8s %10s %14s%n", "Value", "Count", "All Count", "Relative Rate");

			System.out.printf("%-20s %8s %10s %14s%n", "--------------------", "--------", "----------",
					"-------------");

			for (AnalyticsAggregationBucket bucket : result.getBuckets()) {

				System.out.printf("%-20s %8d %10d %13.2fx%n", bucket.getKey(), bucket.getCount(), bucket.getAllCount(),
						bucket.getRelativeRate());
			}
		}
	}
}
// Expected output
//=== View: aggregatable fields ===
//Format: field | value (document count)
//
//maker | Nissan (3), Toyota (2), Honda (1)
//category | brake (2), electrical (2), body (2)
//part | brake (2), door mirror (2), battery (2)
//
//=== View: category ===
//Values are ordered by document count.
//
//Value                   Count
//-------------------- --------
//brake                       2
//electrical                  2
//body                        2
//
//=== View: part ===
//Lucene query: maker:Nissan
//Matched documents: 3 / 6
//Values are ordered by relative rate.
//
//Value                   Count  All Count  Relative Rate
//-------------------- -------- ----------  -------------
//door mirror                 2          2          2.00x
//battery                     1          2          1.00x
