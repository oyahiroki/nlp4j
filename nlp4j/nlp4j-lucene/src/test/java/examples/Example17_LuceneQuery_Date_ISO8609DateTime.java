package examples;

import nlp4j.lucene.LocalSearch;
import nlp4j.lucene.SearchResult;
import nlp4j.lucene9.FieldTypeDef;

/**
 * @since 1.5
 * Example: Lucene Query syntax for date fields (DATE / *_dt suffix).
 *
 * <p>
 * Demonstrates that Lucene Query Parser syntax such as
 * {@code created_dt:[2026-08-01T00:00:00Z TO 2026-09-01T00:00:00Z}} works
 * correctly with schema-aware query parsing.
 * </p>
 *
 * <p>
 * Expected output:
 * </p>
 *
 * <pre>
 * === created_dt:[2026-08-01T00:00:00Z TO 2026-09-01T00:00:00Z} ===
2 : Event B
=== created_dt:[2026-07-01T00:00:00Z TO 2026-09-30T00:00:00Z] ===
1 : Event A
2 : Event B
3 : Event C
=== created_dt:{2026-07-01T00:00:00Z TO 2026-09-30T00:00:00Z] (not include 2026-07-01) ===
2 : Event B
3 : Event C

=== created_dt:[2026-07-01T00:00:00Z TO *] ===
1 : Event A
2 : Event B
3 : Event C

=== created_dt:[* TO 2026-07-31T23:59:59Z] ===
1 : Event A

 *
 * ...
 * </pre>
 */
public class Example17_LuceneQuery_Date_ISO8609DateTime {

	public static void main(String[] args) throws Exception {

		try (LocalSearch search = LocalSearch.builder("en").autoAnalyze(false)
				.field("created_dt", FieldTypeDef.date().stored(true).aggregatable(true)).build()) {

			// Add documents
			search.addJson("""
					{"id":"1","text":"Event A","created_dt":"2026-07-01T00:00:00Z"}
					""");
			search.addJson("""
					{"id":"2","text":"Event B","created_dt":"2026-08-19T10:30:00Z"}
					""");
			search.addJson("""
					{"id":"3","text":"Event C","created_dt":"2026-09-30T00:00:00Z"}
					""");
			search.commit();

			// -----------------------------------------------------------
			// 1. Date range: August 2026 only
			// -----------------------------------------------------------
			{
				System.out.println("=== created_dt:[2026-08-01T00:00:00Z TO 2026-09-01T00:00:00Z} ===");
				SearchResult[] r1 = search.searchLucene("created_dt:[2026-08-01T00:00:00Z TO 2026-09-01T00:00:00Z}",
						10);
				for (SearchResult r : r1) {
					System.out.println(r.id + " : " + r.body);
				}
			}

			{
				System.out.println("=== created_dt:[2026-07-01T00:00:00Z TO 2026-09-30T00:00:00Z] ===");
				SearchResult[] r1 = search.searchLucene("created_dt:[2026-07-01T00:00:00Z TO 2026-09-30T00:00:00Z]",
						10);
				for (SearchResult r : r1) {
					System.out.println(r.id + " : " + r.body);
				}
			}

			{
				System.out.println(
						"=== created_dt:{2026-07-01T00:00:00Z TO 2026-09-30T00:00:00Z] (not include 2026-07-01) ===");
				SearchResult[] r1 = search.searchLucene("created_dt:{2026-07-01T00:00:00Z TO 2026-09-30T00:00:00Z]",
						10);
				for (SearchResult r : r1) {
					System.out.println(r.id + " : " + r.body);
				}
			}

			// -----------------------------------------------------------
			// 2. Open upper bound: on or after 2026-07-01
			// -----------------------------------------------------------
			System.out.println();
			{
				System.out.println("=== created_dt:[2026-07-01T00:00:00Z TO *] ===");
				SearchResult[] r2 = search.searchLucene("created_dt:[2026-07-01T00:00:00Z TO *]", 10);
				for (SearchResult r : r2) {
					System.out.println(r.id + " : " + r.body);
				}
			}

			// -----------------------------------------------------------
			// 3. Open lower bound: up to 2026-07-31
			// -----------------------------------------------------------
			System.out.println();
			{
				System.out.println("=== created_dt:[* TO 2026-07-31T23:59:59Z] ===");
				SearchResult[] r3 = search.searchLucene("created_dt:[* TO 2026-07-31T23:59:59Z]", 10);
				for (SearchResult r : r3) {
					System.out.println(r.id + " : " + r.body);
				}

			}
		}
	}
}
// Expected Output
//=== created_dt:[2026-08-01T00:00:00Z TO 2026-09-01T00:00:00Z} ===
//2 : Event B
//=== created_dt:[2026-07-01T00:00:00Z TO 2026-09-30T00:00:00Z] ===
//1 : Event A
//2 : Event B
//3 : Event C
//=== created_dt:{2026-07-01T00:00:00Z TO 2026-09-30T00:00:00Z] (not include 2026-07-01) ===
//2 : Event B
//3 : Event C
//
//=== created_dt:[2026-07-01T00:00:00Z TO *] ===
//1 : Event A
//2 : Event B
//3 : Event C
//
//=== created_dt:[* TO 2026-07-31T23:59:59Z] ===
//1 : Event A
