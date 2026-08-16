package examples;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import nlp4j.json.JsonNode;
import nlp4j.lucene.LocalSearch;

public class Example09_TextMining_Ja2 {
	public static void main(String[] args) throws Exception {

		try (LocalSearch search = new LocalSearch("ja")) {

			// Just add natural language text.
			// Morphological analysis is performed automatically.
			search.add("1", "ニッサン ドアミラーが破損");
			search.add("2", "ニッサン ドアミラーが動かない");
			search.add("3", "トヨタ ドアミラーが外れた");
			search.add("4", "トヨタ ブレーキの効きが悪い");
			search.add("5", "トヨタ ドアから水が入った");

			search.commit();

			int count_all = 5;

			Map<String, Integer> agg_all;

			{
				// Count nouns
				String nouns = search.aggregateJson("""
						{
						  "field": "word.noun",
						  "size": 10
						}
						""");
				System.out.println("=== Nouns All ===");

				agg_all = getAsMap(nouns);

				printAggregation(nouns);
			}

			Map<String, Integer> agg_query;
			int count_nissan = 2;
			{
				// Count nouns
				String nouns = search.aggregateJson("""
						{
						  "query":"ニッサン",
						  "name":"values",
						  "field": "word.noun",
						  "size": 10
						}
						""");
				System.out.println("=== Nouns ===");
				System.out.println("---");
				System.out.println(nouns);
				agg_query = getAsMap(nouns);
				System.out.println("---");
				printAggregation(nouns);
//				ニッサン : 2
//				ミラー : 2
//				ドア : 2
//				破損 : 1

			}

			System.out.println("---");

			Map<String, Double> agg_collocation = new HashMap<String, Double>();

			for (Map.Entry<String, Integer> entry : agg_query.entrySet()) {
				String key = entry.getKey();
				int value2 = (int) entry.getValue();
				int value1 = (int) agg_all.get(key);
				double coll = ((double) value2 / ((double) count_nissan)) / ((double) value1 / (double) count_all);
				agg_collocation.put(key, coll);
			}

			{
				Map<String, Double> sortedMap = agg_collocation.entrySet().stream()
						.sorted(Map.Entry.<String, Double>comparingByValue().reversed())
						.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1,
								LinkedHashMap<String, Double>::new));
				for (Map.Entry<String, Double> entry : sortedMap.entrySet()) {
					String key = entry.getKey();
					double collocation = entry.getValue();
					System.out.println(key + " : " + collocation);
//					破損 : 2.5
//					ニッサン : 2.5
//					ミラー : 1.6666666666666667
//					ドア : 1.25

				}
			}

		}
	}

	private static Map<String, Integer> getAsMap(String json) {
		JsonNode result = JsonNode.parse(json);
		JsonNode buckets = result.get("aggregations").get("values").get("buckets");

		Map<String, Integer> agg = new HashMap<String, Integer>();

		for (JsonNode bucket : buckets.asList()) {
//			System.out.println(bucket.get("key").asString() + " : " + bucket.get("doc_count").asInt());
			String key = bucket.get("key").asString();
			int count = bucket.get("doc_count").asInt();
			agg.put(key, count);
		}
		return agg;
	}

	private static void printAggregation(String json) throws Exception {

		JsonNode result = JsonNode.parse(json);
		JsonNode buckets = result.get("aggregations").get("values").get("buckets");

		for (JsonNode bucket : buckets.asList()) {
			System.out.println(bucket.get("key").asString() + " : " + bucket.get("doc_count").asInt());
		}
	}
}
