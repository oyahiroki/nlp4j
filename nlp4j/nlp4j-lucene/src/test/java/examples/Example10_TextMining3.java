package examples;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import nlp4j.json.JsonNode;
import nlp4j.lucene.LocalSearch;
import nlp4j.lucene.SearchResult;

public class Example10_TextMining3 {

	private static final double MIN_RELATIVE_RATE = 2.0;

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

			int countAll = 5;

			// ------------------------------------------------------------
			// Aggregation for all documents
			// ------------------------------------------------------------

			Map<String, Integer> nounAll = aggregate(search, null, "word.noun");
			Map<String, Integer> verbAll = aggregate(search, null, "word.verb");

			System.out.println("=== All Nouns ===");
			printAggregation(nounAll);

			System.out.println();
			System.out.println("=== All Verbs ===");
			printAggregation(verbAll);

			// ------------------------------------------------------------
			// Query words
			//
			// Use every value appearing in word.noun or word.verb
			// as a query.
			// ------------------------------------------------------------

			Set<String> queryWords = new TreeSet<>();

			queryWords.addAll(nounAll.keySet());
			queryWords.addAll(verbAll.keySet());

			// ------------------------------------------------------------
			// Calculate relativeRate
			//
			// Query words:
			// word.noun + word.verb
			//
			// Aggregation fields:
			// word.noun
			// word.verb
			//
			// relativeRate =
			//
			// target document rate
			// --------------------
			// all document rate
			//
			// ------------------------------------------------------------

			for (String queryWord : queryWords) {

				int countQuery = searchCount(search, queryWord);

				if (countQuery == 0) {
					continue;
				}

				calculateAndPrintRelativeRate(search, queryWord, countQuery, countAll, "word.noun", nounAll);

				calculateAndPrintRelativeRate(search, queryWord, countQuery, countAll, "word.verb", verbAll);
			}
		}
	}

	/**
	 * Calculate relativeRate for one query word and one aggregation field.
	 */
	private static void calculateAndPrintRelativeRate(LocalSearch search, String queryWord, int countQuery,
			int countAll, String aggregationField, Map<String, Integer> aggregationAll) throws Exception {

		Map<String, Integer> aggregationQuery = aggregate(search, queryWord, aggregationField);

		Map<String, Double> relativeRates = new HashMap<String, Double>();

		for (Map.Entry<String, Integer> entry : aggregationQuery.entrySet()) {

			String key = entry.getKey();

			int targetCount = entry.getValue();

			Integer allCount = aggregationAll.get(key);

			if (allCount == null || allCount == 0) {
				continue;
			}

			double targetRate = (double) targetCount / (double) countQuery;

			double allRate = (double) allCount / (double) countAll;

			double relativeRate = targetRate / allRate;

			relativeRates.put(key, relativeRate);
		}

		Map<String, Double> sortedMap = relativeRates.entrySet().stream()

				// Do not display values less than 2.0
				.filter(e -> e.getValue() >= MIN_RELATIVE_RATE)

				.sorted(Map.Entry.<String, Double>comparingByValue().reversed())

				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1,
						LinkedHashMap<String, Double>::new));

		if (sortedMap.isEmpty()) {
			return;
		}

		System.out.println();
		System.out.println("=== query=[" + queryWord + "]" + " aggregation=[" + aggregationField + "] ===");

		for (Map.Entry<String, Double> entry : sortedMap.entrySet()) {

			String key = entry.getKey();
			double relativeRate = entry.getValue();

			System.out.printf("%s : %.4f%n", key, relativeRate);
		}
	}

	/**
	 * Aggregation.
	 *
	 * query == null: aggregation for all documents
	 *
	 * query != null: aggregation for documents matching the query
	 */
	private static Map<String, Integer> aggregate(LocalSearch search, String query, String field) throws Exception {

		String json;

		if (query == null) {

			json = search.aggregateJson("""
					{
					  "name": "values",
					  "field": "%s",
					  "size": 1000
					}
					""".formatted(field));

		} else {

			json = search.aggregateJson("""
					{
					  "query": "%s",
					  "name": "values",
					  "field": "%s",
					  "size": 1000
					}
					""".formatted(escapeJson(query), field));
		}

		return getAsMap(json);
	}

	/**
	 * Count documents matching the query.
	 *
	 * This method assumes that LocalSearch.searchJson() returns hits.total.value in
	 * Lucene/OpenSearch-like JSON.
	 *
	 * Adjust this part if the current LocalSearch API exposes the hit count through
	 * another method.
	 */
	private static int searchCount(LocalSearch search, String query) throws Exception {

		SearchResult[] results =
	            search.search(query, 1000);

	    return results.length;
	}

	private static Map<String, Integer> getAsMap(String json) {

		JsonNode result = JsonNode.parse(json);

		JsonNode buckets = result.get("aggregations").get("values").get("buckets");

		Map<String, Integer> agg = new HashMap<String, Integer>();

		for (JsonNode bucket : buckets.asList()) {

			String key = bucket.get("key").asString();

			int count = bucket.get("doc_count").asInt();

			agg.put(key, count);
		}

		return agg;
	}

	private static void printAggregation(Map<String, Integer> aggregation) {

		aggregation.entrySet().stream().sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
				.forEach(entry -> System.out.println(entry.getKey() + " : " + entry.getValue()));
	}

	private static String escapeJson(String str) {

		return str.replace("\\", "\\\\").replace("\"", "\\\"");
	}
}
