package examples;

import nlp4j.json.JsonNode;
import nlp4j.lucene.LocalSearch;

public class Example08_TextMining_Ja1 {
	public static void main(String[] args) throws Exception {

		try (LocalSearch search = new LocalSearch("ja")) {

			// Just add natural language text.
			// Morphological analysis is performed automatically.
			search.add("1", "私は歩いて学校に行きました。");
			search.add("2", "彼女は走って学校に行きました。");
			search.add("3", "彼は歩いて会社に行きました。");

			search.commit();

			// Count nouns
			String nouns = search.aggregateJson("""
					{
					  "field": "word.noun",
					  "size": 10
					}
					""");

			System.out.println("=== Nouns ===");
			printAggregation(nouns);

			// Count verbs
			String verbs = search.aggregateJson("""
					{
					  "field": "word.verb",
					  "size": 10
					}
					""");

			System.out.println("=== Verbs ===");
			printAggregation(verbs);
		}
	}

	private static void printAggregation(String json) throws Exception {

		JsonNode result = JsonNode.parse(json);
		JsonNode buckets = result.get("aggregations").get("values").get("buckets");

		for (JsonNode bucket : buckets.asList()) {
			System.out.println(bucket.get("key").asString() + " : " + bucket.get("doc_count").asInt());
		}
	}
}
