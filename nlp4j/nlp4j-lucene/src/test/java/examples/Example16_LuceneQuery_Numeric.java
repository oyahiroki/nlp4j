package examples;

import nlp4j.lucene.LocalSearch;
import nlp4j.lucene.SearchResult;
import nlp4j.lucene9.FieldTypeDef;

/**
 * Example: Lucene Query syntax for numeric fields (INTEGER and DOUBLE).
 *
 * <p>
 * Demonstrates that Lucene Query Parser syntax such as
 * {@code year_i:2025} and {@code year_i:[2025 TO 2026]}
 * works correctly with schema-aware query parsing.
 * </p>
 *
 * <p>
 * Expected output:
 * </p>
 *
 * <pre>
 * === year_i:2025 ===
2 : Product B

=== year_i:[2025 TO 2026] ===
2 : Product B
3 : Product C

=== year_i:{2025 TO 2026] (not include 2025) ===
3 : Product C

=== year_i:[2025 TO 2026} (not include 2026) ===
2 : Product B

=== price_d:[100 TO 200] ===
2 : Product B

 * </pre>
 */
public class Example16_LuceneQuery_Numeric {

	public static void main(String[] args) throws Exception {

		try (LocalSearch search = LocalSearch.builder("en")
				.autoAnalyze(false)
				.field("year_i",  FieldTypeDef.integer().stored(true).aggregatable(true))
				.field("price_d", FieldTypeDef.doubleNumber().stored(true).aggregatable(true))
				.build()) {

			// Add documents
			search.addJson("""
					{"id":"1","text":"Product A","year_i":2024,"price_d":80.0}
					""");
			search.addJson("""
					{"id":"2","text":"Product B","year_i":2025,"price_d":150.0}
					""");
			search.addJson("""
					{"id":"3","text":"Product C","year_i":2026,"price_d":250.0}
					""");
			search.commit();

			// -----------------------------------------------------------
			// 1. Exact match: year_i = 2025
			// -----------------------------------------------------------
			System.out.println("=== year_i:2025 ===");
			{
				SearchResult[] r1 = search.searchLucene("year_i:2025", 10);
				for (SearchResult r : r1) {
					System.out.println(r.id + " : " + r.body);
				}
			}

			// -----------------------------------------------------------
			// 2. Range: year_i in [2025, 2026]
			// -----------------------------------------------------------
			System.out.println();
			{
				System.out.println("=== year_i:[2025 TO 2026] ===");
				SearchResult[] r2 = search.searchLucene("year_i:[2025 TO 2026]", 10);
				for (SearchResult r : r2) {
					System.out.println(r.id + " : " + r.body);
				}
			}
			System.out.println();
			{
				System.out.println("=== year_i:{2025 TO 2026] (not include 2025) ===");
				SearchResult[] r2 = search.searchLucene("year_i:{2025 TO 2026]", 10);
				for (SearchResult r : r2) {
					System.out.println(r.id + " : " + r.body);
				}
			}
			System.out.println();
			{
				System.out.println("=== year_i:[2025 TO 2026} (not include 2026) ===");
				SearchResult[] r2 = search.searchLucene("year_i:[2025 TO 2026}", 10);
				for (SearchResult r : r2) {
					System.out.println(r.id + " : " + r.body);
				}
			}
			

			// -----------------------------------------------------------
			// 3. Double range: price_d in [100, 200]
			// -----------------------------------------------------------
			System.out.println();
			System.out.println("=== price_d:[100 TO 200] ===");
			SearchResult[] r3 = search.searchLucene("price_d:[100 TO 200]", 10);
			for (SearchResult r : r3) {
				System.out.println(r.id + " : " + r.body);
			}
		}
	}
}
// Expected Output
//=== year_i:2025 ===
//2 : Product B
//
//=== year_i:[2025 TO 2026] ===
//2 : Product B
//3 : Product C
//
//=== year_i:{2025 TO 2026] (not include 2025) ===
//3 : Product C
//
//=== year_i:[2025 TO 2026} (not include 2026) ===
//2 : Product B
//
//=== price_d:[100 TO 200] ===
//2 : Product B
