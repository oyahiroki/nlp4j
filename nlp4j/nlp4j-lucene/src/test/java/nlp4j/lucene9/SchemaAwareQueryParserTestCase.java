package nlp4j.lucene9;

import junit.framework.TestCase;
import nlp4j.lucene.LocalSearch;
import nlp4j.lucene.SearchResult;

/**
 * JUnit3 tests for {@link SchemaAwareQueryParser} and {@link TypedFieldQueryFactory}.
 *
 * <p>
 * Verifies that Lucene Query Parser syntax (field:value, field:[lower TO upper])
 * is correctly routed through schema-aware Point queries for numeric/date fields.
 * </p>
 */
public class SchemaAwareQueryParserTestCase extends TestCase {

	// -----------------------------------------------------------------------
	// TypedFieldQueryFactory.resolveFieldType
	// -----------------------------------------------------------------------

	public void testResolveFieldType_explicitSchema() {
		SearchSchema schema = new SearchSchema();
		schema.add("my_field", FieldTypeDef.integer().stored(true));

		FieldTypeDef def = TypedFieldQueryFactory.resolveFieldType("my_field", schema);
		assertEquals(FieldTypeDef.Kind.INTEGER, def.kind());
	}

	public void testResolveFieldType_dynamicSuffix_i() {
		FieldTypeDef def = TypedFieldQueryFactory.resolveFieldType("year_i", null);
		assertEquals(FieldTypeDef.Kind.INTEGER, def.kind());
	}

	public void testResolveFieldType_dynamicSuffix_d() {
		FieldTypeDef def = TypedFieldQueryFactory.resolveFieldType("price_d", null);
		assertEquals(FieldTypeDef.Kind.DOUBLE, def.kind());
	}

	public void testResolveFieldType_dynamicSuffix_dt() {
		FieldTypeDef def = TypedFieldQueryFactory.resolveFieldType("created_dt", null);
		assertEquals(FieldTypeDef.Kind.DATE, def.kind());
	}

	public void testResolveFieldType_noSuffix_keyword() {
		FieldTypeDef def = TypedFieldQueryFactory.resolveFieldType("category", null);
		assertEquals(FieldTypeDef.Kind.KEYWORD, def.kind());
	}

	public void testIsNumericOrDate_integer() {
		assertTrue(TypedFieldQueryFactory.isNumericOrDate("year_i", null));
	}

	public void testIsNumericOrDate_keyword() {
		assertFalse(TypedFieldQueryFactory.isNumericOrDate("category", null));
	}

	// -----------------------------------------------------------------------
	// searchLucene — INTEGER exact (via SchemaAwareQueryParser)
	// -----------------------------------------------------------------------

	public void testSearchLucene_integerExact() throws Exception {
		try (LocalSearch search = LocalSearch.builder("en")
				.autoAnalyze(false)
				.field("year_i", FieldTypeDef.integer().stored(true).aggregatable(true))
				.build()) {

			search.addJson("""
					{"id":"1","text":"Product A","year_i":2024}
					""");
			search.addJson("""
					{"id":"2","text":"Product B","year_i":2025}
					""");
			search.addJson("""
					{"id":"3","text":"Product C","year_i":2026}
					""");
			search.commit();

			SearchResult[] results = search.searchLucene("year_i:2025", 10);
			System.out.println("testSearchLucene_integerExact: hits=" + results.length);
			assertEquals(1, results.length);
			assertEquals("2", results[0].id);
		}
	}

	// -----------------------------------------------------------------------
	// searchLucene — INTEGER range (via SchemaAwareQueryParser)
	// -----------------------------------------------------------------------

	public void testSearchLucene_integerRange() throws Exception {
		try (LocalSearch search = LocalSearch.builder("en")
				.autoAnalyze(false)
				.field("year_i", FieldTypeDef.integer().stored(true).aggregatable(true))
				.build()) {

			search.addJson("""
					{"id":"1","text":"Product A","year_i":2024}
					""");
			search.addJson("""
					{"id":"2","text":"Product B","year_i":2025}
					""");
			search.addJson("""
					{"id":"3","text":"Product C","year_i":2026}
					""");
			search.commit();

			// year_i:[2025 TO 2026] → 2件
			SearchResult[] results = search.searchLucene("year_i:[2025 TO 2026]", 10);
			System.out.println("testSearchLucene_integerRange: hits=" + results.length);
			assertEquals(2, results.length);
		}
	}

	// -----------------------------------------------------------------------
	// searchLucene — DOUBLE range (via SchemaAwareQueryParser)
	// -----------------------------------------------------------------------

	public void testSearchLucene_doubleRange() throws Exception {
		try (LocalSearch search = LocalSearch.builder("en")
				.autoAnalyze(false)
				.field("price_d", FieldTypeDef.doubleNumber().stored(true).aggregatable(true))
				.build()) {

			search.addJson("""
					{"id":"1","text":"Product A","price_d":80.0}
					""");
			search.addJson("""
					{"id":"2","text":"Product B","price_d":150.0}
					""");
			search.addJson("""
					{"id":"3","text":"Product C","price_d":250.0}
					""");
			search.commit();

			// price_d:[100 TO 200] → 1件 (150)
			SearchResult[] results = search.searchLucene("price_d:[100 TO 200]", 10);
			System.out.println("testSearchLucene_doubleRange: hits=" + results.length);
			assertEquals(1, results.length);
			assertEquals("2", results[0].id);
		}
	}

	// -----------------------------------------------------------------------
	// searchLucene — DATE range (via SchemaAwareQueryParser)
	// -----------------------------------------------------------------------

	public void testSearchLucene_dateRange() throws Exception {
		try (LocalSearch search = LocalSearch.builder("en")
				.autoAnalyze(false)
				.field("created_dt", FieldTypeDef.date().stored(true).aggregatable(true))
				.build()) {

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

			// August 2026 only → id=2
			SearchResult[] results = search.searchLucene(
					"created_dt:[2026-08-01T00:00:00Z TO 2026-09-01T00:00:00Z]", 10);
			System.out.println("testSearchLucene_dateRange: hits=" + results.length);
			assertEquals(1, results.length);
			assertEquals("2", results[0].id);
		}
	}

	// -----------------------------------------------------------------------
	// searchLucene — DATE open-ended range (via SchemaAwareQueryParser)
	// -----------------------------------------------------------------------

	public void testSearchLucene_dateRange_openUpper() throws Exception {
		try (LocalSearch search = LocalSearch.builder("en")
				.autoAnalyze(false)
				.field("created_dt", FieldTypeDef.date().stored(true).aggregatable(true))
				.build()) {

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

			// On or after 2026-07-01 → 3件すべて
			SearchResult[] results = search.searchLucene(
					"created_dt:[2026-07-01T00:00:00Z TO *]", 10);
			System.out.println("testSearchLucene_dateRange_openUpper: hits=" + results.length);
			assertEquals(3, results.length);
		}
	}

	public void testSearchLucene_dateRange_openLower() throws Exception {
		try (LocalSearch search = LocalSearch.builder("en")
				.autoAnalyze(false)
				.field("created_dt", FieldTypeDef.date().stored(true).aggregatable(true))
				.build()) {

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

			// Up to 2026-07-31 → id=1 のみ
			SearchResult[] results = search.searchLucene(
					"created_dt:[* TO 2026-07-31T23:59:59Z]", 10);
			System.out.println("testSearchLucene_dateRange_openLower: hits=" + results.length);
			assertEquals(1, results.length);
			assertEquals("1", results[0].id);
		}
	}

	// -----------------------------------------------------------------------
	// searchLucene — Dynamic suffix (without explicit Builder.field())
	// -----------------------------------------------------------------------

	public void testSearchLucene_dynamicIntegerSuffix() throws Exception {
		try (LocalSearch search = LocalSearch.builder("en").autoAnalyze(false).build()) {

			search.addJson("""
					{"id":"1","text":"hello","year_i":2025}
					""");
			search.addJson("""
					{"id":"2","text":"world","year_i":2023}
					""");
			search.commit();

			// suffix *_i → INTEGER (dynamic resolution)
			SearchResult[] results = search.searchLucene("year_i:2025", 10);
			System.out.println("testSearchLucene_dynamicIntegerSuffix: hits=" + results.length);
			assertEquals(1, results.length);
			assertEquals("1", results[0].id);
		}
	}

	// -----------------------------------------------------------------------
	// validateLuceneQuery — schema-aware validation
	// -----------------------------------------------------------------------

	public void testValidateLuceneQuery_numericRange_valid() throws Exception {
		try (LocalSearch search = LocalSearch.builder("en")
				.autoAnalyze(false)
				.field("year_i", FieldTypeDef.integer().stored(true))
				.build()) {

			nlp4j.lucene.LuceneQueryValidationResult result =
					search.validateLuceneQuery("year_i:[2025 TO 2026]");
			System.out.println("testValidateLuceneQuery_numericRange_valid: " + result.isValid());
			assertTrue(result.isValid());
		}
	}

	public void testValidateLuceneQuery_dateRange_valid() throws Exception {
		try (LocalSearch search = LocalSearch.builder("en")
				.autoAnalyze(false)
				.field("created_dt", FieldTypeDef.date().stored(true))
				.build()) {

			nlp4j.lucene.LuceneQueryValidationResult result =
					search.validateLuceneQuery("created_dt:[2026-08-01T00:00:00Z TO 2026-09-01T00:00:00Z]");
			System.out.println("testValidateLuceneQuery_dateRange_valid: " + result.isValid());
			assertTrue(result.isValid());
		}
	}

	public void testValidateLuceneQuery_noTimeZone() throws Exception {
		try (LocalSearch search = LocalSearch.builder("en")
				.autoAnalyze(false)
				.field("created_dt", FieldTypeDef.date().stored(true))
				.build()) {

			nlp4j.lucene.LuceneQueryValidationResult result =
					search.validateLuceneQuery("created_dt:[2026-08-01 TO 2026-09-01]");
			System.out.println("testValidateLuceneQuery_invalidDate_invalid: " + result.isValid()
					+ " msg=" + result.getMessage());
		}
	}
}
