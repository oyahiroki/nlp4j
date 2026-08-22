package examples;

import nlp4j.lucene.LocalSearch;
import nlp4j.lucene.SearchResult;
import nlp4j.lucene9.FieldTypeDef;

/**
 * @since 1.5
 */
public class Example18_LuceneQuery_DateSimple {

	public static void main(String[] args) throws Exception {

		try (LocalSearch search = LocalSearch.builder("en").autoAnalyze(false)
				.field("created_dt", FieldTypeDef.date().stored(true).aggregatable(true)).build()) {

			// Add documents
			search.addJson("""
					{"id":"1","text":"Event A 07-01","created_dt":"2026-07-01"}
					""");
			search.addJson("""
					{"id":"2","text":"Event B 08-19","created_dt":"2026-08-19"}
					""");
			search.addJson("""
					{"id":"3","text":"Event C 09-30","created_dt":"2026-09-30"}
					""");
			search.commit();

			// -----------------------------------------------------------
			// 1. Date range: August 2026 only
			// -----------------------------------------------------------
			{
				System.out.println("=== created_dt:[2026-08-01 TO 2026-09-01} ===");
				SearchResult[] r1 = search.searchLucene("created_dt:[2026-08-01 TO 2026-09-01}",
						10);
				for (SearchResult r : r1) {
					System.out.println(r.id + " : " + r.body);
				}
			}

			{
				System.out.println("=== created_dt:[2026-07-01 TO 2026-09-30] ===");
				SearchResult[] r1 = search.searchLucene("created_dt:[2026-07-01 TO 2026-09-30]",
						10);
				for (SearchResult r : r1) {
					System.out.println(r.id + " : " + r.body);
				}
			}

			{
				System.out.println(
						"=== created_dt:{2026-07-01 TO 2026-09-30] (not include 2026-07-01) ===");
				SearchResult[] r1 = search.searchLucene("created_dt:{2026-07-01 TO 2026-09-30]",
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
				System.out.println("=== created_dt:[2026-07-01 TO *] ===");
				SearchResult[] r2 = search.searchLucene("created_dt:[2026-07-01 TO *]", 10);
				for (SearchResult r : r2) {
					System.out.println(r.id + " : " + r.body);
				}
			}
			System.out.println();
			{
				System.out.println("=== created_dt:{2026-07-01 TO *] ===");
				SearchResult[] r2 = search.searchLucene("created_dt:{2026-07-01 TO *]", 10);
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
