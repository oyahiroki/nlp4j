package examples;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import nlp4j.lucene.LocalSearch;

public class Example15_TextMining_En1 {

	private static final double MIN_RELATIVE_RATE = -1.0;

	public static void main(String[] args) throws Exception {

		try (LocalSearch search = new LocalSearch("en")) {

			search.add("1", "Nissan side mirror is damaged");
			search.add("2", "Nissan side mirror does not move");
			search.add("3", "Toyota mirror came off");
			search.add("4", "Toyota brakes are not working well");
			search.add("5", "Water leaked in through the Toyota left side door");

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

			queryWords.add("Toyota");
			queryWords.add("Nissan");
//			queryWords.addAll(nounAll.keySet());
//			queryWords.addAll(verbAll.keySet());

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

				System.out.println("");
				System.out.println("" + queryWord + " -> " + countQuery);

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
			System.out.println();
			System.out.println("=== query=[" + queryWord + "]" + " aggregation=[" + aggregationField + "] ===");
			System.out.println("[NOT FOUND]");
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

		if (aggregation.size() == 0) {
			System.out.println("[NOT FOUND]");
		}
	}
}
// Expected results
//=== All Nouns ===
//side : 3
//mirror : 3
// side mirror : 3
//door : 1
//brake : 1
//
//=== All Verbs ===
//leak : 1
//move : 1
//work : 1
//come : 1
//
//=== query=[ side mirror] aggregation=[word.noun] ===
//[NOT FOUND]
//
//=== query=[ side mirror] aggregation=[word.verb] ===
//[NOT FOUND]
//
//=== query=[Nissan] aggregation=[word.noun] ===
//[NOT FOUND]
//
//=== query=[Nissan] aggregation=[word.verb] ===
//move : 2.5000
//
//=== query=[Toyota] aggregation=[word.noun] ===
//[NOT FOUND]
//
//=== query=[Toyota] aggregation=[word.verb] ===
//[NOT FOUND]
//
//=== query=[brake] aggregation=[word.noun] ===
//brake : 5.0000
//
//=== query=[brake] aggregation=[word.verb] ===
//work : 5.0000
//
//=== query=[door] aggregation=[word.noun] ===
//door : 5.0000
//
//=== query=[door] aggregation=[word.verb] ===
//leak : 5.0000
//
//=== query=[leak] aggregation=[word.noun] ===
//door : 5.0000
//
//=== query=[leak] aggregation=[word.verb] ===
//leak : 5.0000
//
//=== query=[mirror] aggregation=[word.noun] ===
//[NOT FOUND]
//
//=== query=[mirror] aggregation=[word.verb] ===
//[NOT FOUND]
//
//=== query=[move] aggregation=[word.noun] ===
//[NOT FOUND]
//
//=== query=[move] aggregation=[word.verb] ===
//move : 5.0000
//
//=== query=[side] aggregation=[word.noun] ===
//[NOT FOUND]
//
//=== query=[side] aggregation=[word.verb] ===
//[NOT FOUND]
//
//=== query=[work] aggregation=[word.noun] ===
//brake : 5.0000
//
//=== query=[work] aggregation=[word.verb] ===
//work : 5.0000
