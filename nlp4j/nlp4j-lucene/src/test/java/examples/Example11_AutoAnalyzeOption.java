package examples;

import nlp4j.lucene.LocalSearch;
import nlp4j.lucene.SearchResult;

/**
 * @since 1.5
 */
public class Example11_AutoAnalyzeOption {

	public static void main(String[] args) throws Exception {

		// autoAnalyze = true
		try (LocalSearch search = LocalSearch.builder("ja").autoAnalyze(true).build()) {
			search.add("1", "私は歩いて学校に行きました。");
			search.commit();

			// word.verb に "行く" が集計されること
			String json = search.aggregateJson("""
					{"field":"word.verb","size":10}
					""");
			nlp4j.json.JsonNode buckets = nlp4j.json.JsonNode.parse(json).get("aggregations").get("values")
					.get("buckets");

			boolean foundIku = false;
			for (nlp4j.json.JsonNode bucket : buckets.asList()) {
				if ("行く".equals(bucket.get("key").asString())) {
					foundIku = true;
				}
			}
			// true
			System.err.println("word.verb '行く' が出現: " + foundIku);
		}

		// autoAnalyze = false
		try (LocalSearch search = LocalSearch.builder("ja").autoAnalyze(false).build()) {
			search.add("1", "私は歩いて学校に行きました。");
			search.commit();

			// word.verb に "行く" が集計されないこと
			String json = search.aggregateJson("""
					{"field":"word.verb","size":10}
					""");
			nlp4j.json.JsonNode buckets = nlp4j.json.JsonNode.parse(json).get("aggregations").get("values")
					.get("buckets");

			boolean foundIku = false;
			for (nlp4j.json.JsonNode bucket : buckets.asList()) {
				if ("行く".equals(bucket.get("key").asString())) {
					foundIku = true;
				}
			}
			// false
			System.err.println("word.verb '行く' が出現: " + foundIku);
		}

	}

}
// Expected output
//word.verb '行く' が出現: true
//word.verb '行く' が出現: false
