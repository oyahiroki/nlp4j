package examples;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import nlp4j.lucene.LocalSearch;

public class Example10_TextMining_Ja3 {

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
// Expected Output
//=== All Nouns ===
//ドア : 4
//トヨタ : 3
//ドアミラー : 3
//ミラー : 3
//ニッサン : 2
//水 : 1
//ブレーキ : 1
//破損 : 1
//
//=== All Verbs ===
//動く : 1
//外れる : 1
//入る : 1
//効く : 1
//
//=== query=[ニッサン] aggregation=[word.noun] ===
//ニッサン : 2.5000
//破損 : 2.5000
//
//=== query=[ニッサン] aggregation=[word.verb] ===
//動く : 2.5000
//
//=== query=[ブレーキ] aggregation=[word.noun] ===
//ブレーキ : 5.0000
//
//=== query=[ブレーキ] aggregation=[word.verb] ===
//効く : 5.0000
//
//=== query=[入る] aggregation=[word.noun] ===
//水 : 5.0000
//
//=== query=[入る] aggregation=[word.verb] ===
//入る : 5.0000
//
//=== query=[効く] aggregation=[word.noun] ===
//ブレーキ : 5.0000
//
//=== query=[効く] aggregation=[word.verb] ===
//効く : 5.0000
//
//=== query=[動く] aggregation=[word.noun] ===
//ニッサン : 2.5000
//
//=== query=[動く] aggregation=[word.verb] ===
//動く : 5.0000
//
//=== query=[外れる] aggregation=[word.verb] ===
//外れる : 5.0000
//
//=== query=[水] aggregation=[word.noun] ===
//水 : 5.0000
//
//=== query=[水] aggregation=[word.verb] ===
//入る : 5.0000
//
//=== query=[破損] aggregation=[word.noun] ===
//破損 : 5.0000
//ニッサン : 2.5000

