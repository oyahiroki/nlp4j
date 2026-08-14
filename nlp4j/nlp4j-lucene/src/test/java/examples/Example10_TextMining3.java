package examples;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import nlp4j.lucene.LocalSearch;

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

			// ------------------------------------------------------------
			// Aggregation for all documents
			// ------------------------------------------------------------

			Map<String, Long> nounAll = search.aggregate("word.noun", 1000);
			Map<String, Long> verbAll = search.aggregate("word.verb", 1000);

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

				long countQuery = search.count(queryWord);

				if (countQuery == 0) {
					continue;
				}

				calculateAndPrintRelativeRate(search, queryWord, countQuery, "word.noun", nounAll);

				calculateAndPrintRelativeRate(search, queryWord, countQuery, "word.verb", verbAll);
			}
		}
	}

	/**
	 * Calculate relativeRate for one query word and one aggregation field.
	 */
	private static void calculateAndPrintRelativeRate(LocalSearch search, String queryWord, long countQuery,
			String aggregationField, Map<String, Long> aggregationAll) throws Exception {
		
		long countAll = search.count();
		
		Map<String, Long> aggregationQuery = search.aggregate(aggregationField, queryWord, 1000);

		Map<String, Double> relativeRates = new LinkedHashMap<>();

		for (Map.Entry<String, Long> entry : aggregationQuery.entrySet()) {

			String key = entry.getKey();

			long targetCount = entry.getValue();

			Long allCount = aggregationAll.get(key);

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

	private static void printAggregation(Map<String, Long> aggregation) {

		aggregation.entrySet().stream().sorted(Map.Entry.<String, Long>comparingByValue().reversed())
				.forEach(entry -> System.out.println(entry.getKey() + " : " + entry.getValue()));
	}
}
