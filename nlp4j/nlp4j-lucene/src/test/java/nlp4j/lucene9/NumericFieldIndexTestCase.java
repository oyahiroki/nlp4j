package nlp4j.lucene9;

import junit.framework.TestCase;
import nlp4j.json.JsonNode;

/**
 * JUnit3 integration test for INTEGER, DOUBLE, DATE field indexing and querying
 * via {@link SearchSchema}, {@link SearchDocumentBuilder}, and
 * {@link LuceneQueryBuilder}.
 */
public class NumericFieldIndexTestCase extends TestCase {

	// -----------------------------------------------------------------------
	// INTEGER field
	// -----------------------------------------------------------------------

	public void testIntegerField_termQuery() throws Exception {
		SearchSchema schema = new SearchSchema();
		schema.add("id", FieldTypeDef.keyword().stored(true));
		schema.add("year_i", FieldTypeDef.integer().stored(true).aggregatable(true));

		try (LuceneIndex index = new LuceneIndex()) {
			index.add(schema.document().put("id", "1").put("year_i", 2024).build());
			index.add(schema.document().put("id", "2").put("year_i", 2025).build());
			index.add(schema.document().put("id", "3").put("year_i", 2026).build());
			index.commit();

			LuceneLocalSearchApi api = new LuceneLocalSearchApi(index, schema);

			// term: year_i = 2025
			JsonNode result = api.search("idx/_search", JsonNode.parse("""
					{
					  "size": 10,
					  "query": {"term": {"year_i": 2025}}
					}
					"""));

			System.out.println("testIntegerField_termQuery: " + result.toJson());
			assertEquals(1, result.get("hits").get("total").get("value").asInt());
			assertEquals("2", result.get("hits").get("hits").get(0).get("_source").get("id").asString());
		}
	}

	public void testIntegerField_rangeQuery() throws Exception {
		SearchSchema schema = new SearchSchema();
		schema.add("id", FieldTypeDef.keyword().stored(true));
		schema.add("year_i", FieldTypeDef.integer().stored(true).aggregatable(true));

		try (LuceneIndex index = new LuceneIndex()) {
			index.add(schema.document().put("id", "1").put("year_i", 2024).build());
			index.add(schema.document().put("id", "2").put("year_i", 2025).build());
			index.add(schema.document().put("id", "3").put("year_i", 2026).build());
			index.commit();

			LuceneLocalSearchApi api = new LuceneLocalSearchApi(index, schema);

			// range: year_i >= 2025 AND year_i <= 2026
			JsonNode result = api.search("idx/_search", JsonNode.parse("""
					{
					  "size": 10,
					  "query": {"range": {"year_i": {"gte": 2025, "lte": 2026}}}
					}
					"""));

			System.out.println("testIntegerField_rangeQuery: " + result.toJson());
			assertEquals(2, result.get("hits").get("total").get("value").asInt());
		}
	}

	// -----------------------------------------------------------------------
	// LONG field  (Integer.MAX_VALUE を超える値で INTEGER/LONG の分離を保証)
	// -----------------------------------------------------------------------

	/**
	 * Integer.MAX_VALUE (2_147_483_647) を明確に超える long 値を term query で
	 * ヒットできることを確認する。
	 * INTEGER フィールドでは表現できない値なので、LONG と INTEGER を別実装した
	 * 意味を回帰テストとして保証する。
	 */
	public void testLongField_termQuery_overIntMax() throws Exception {
		// population_l = 9_000_000_000L  (> Integer.MAX_VALUE = 2_147_483_647)
		final long POP_TOKYO   = 9_000_000_000L;
		final long POP_KYOTO   = 1_400_000L;
		final long POP_OSAKA   = 2_750_000L;

		SearchSchema schema = new SearchSchema();
		schema.add("id",           FieldTypeDef.keyword().stored(true));
		schema.add("population_l", FieldTypeDef.longNumber().stored(true).aggregatable(true));

		try (LuceneIndex index = new LuceneIndex()) {
			index.add(schema.document().put("id", "tokyo").put("population_l", POP_TOKYO).build());
			index.add(schema.document().put("id", "kyoto").put("population_l", POP_KYOTO).build());
			index.add(schema.document().put("id", "osaka").put("population_l", POP_OSAKA).build());
			index.commit();

			LuceneLocalSearchApi api = new LuceneLocalSearchApi(index, schema);

			// term: population_l = 9_000_000_000
			JsonNode result = api.search("idx/_search", JsonNode.parse("""
					{
					  "size": 10,
					  "query": {"term": {"population_l": 9000000000}}
					}
					"""));

			System.out.println("testLongField_termQuery_overIntMax: " + result.toJson());
			assertEquals(1, result.get("hits").get("total").get("value").asInt());
			assertEquals("tokyo",
					result.get("hits").get("hits").get(0).get("_source").get("id").asString());
		}
	}

	/**
	 * Integer.MAX_VALUE を超える long 値を含む range query が正しく動作することを確認する。
	 */
	public void testLongField_rangeQuery_overIntMax() throws Exception {
		final long POP_TOKYO = 9_000_000_000L;  // > Integer.MAX_VALUE
		final long POP_KYOTO = 1_400_000L;
		final long POP_OSAKA = 2_750_000L;

		SearchSchema schema = new SearchSchema();
		schema.add("id",           FieldTypeDef.keyword().stored(true));
		schema.add("population_l", FieldTypeDef.longNumber().stored(true).aggregatable(true));

		try (LuceneIndex index = new LuceneIndex()) {
			index.add(schema.document().put("id", "tokyo").put("population_l", POP_TOKYO).build());
			index.add(schema.document().put("id", "kyoto").put("population_l", POP_KYOTO).build());
			index.add(schema.document().put("id", "osaka").put("population_l", POP_OSAKA).build());
			index.commit();

			LuceneLocalSearchApi api = new LuceneLocalSearchApi(index, schema);

			// range: population_l >= 2_000_000_000 (> Integer.MAX_VALUE の閾値付近)
			// → tokyo (9_000_000_000) のみヒット
			JsonNode result = api.search("idx/_search", JsonNode.parse("""
					{
					  "size": 10,
					  "query": {"range": {"population_l": {"gte": 2000000000}}}
					}
					"""));

			System.out.println("testLongField_rangeQuery_overIntMax: " + result.toJson());
			assertEquals(1, result.get("hits").get("total").get("value").asInt());
			assertEquals("tokyo",
					result.get("hits").get("hits").get(0).get("_source").get("id").asString());
		}
	}

	// -----------------------------------------------------------------------
	// DOUBLE field
	// -----------------------------------------------------------------------

	public void testDoubleField_rangeQuery() throws Exception {
		SearchSchema schema = new SearchSchema();
		schema.add("id", FieldTypeDef.keyword().stored(true));
		schema.add("price_d", FieldTypeDef.doubleNumber().stored(true).aggregatable(true));

		try (LuceneIndex index = new LuceneIndex()) {
			index.add(schema.document().put("id", "a").put("price_d", 99.9).build());
			index.add(schema.document().put("id", "b").put("price_d", 150.0).build());
			index.add(schema.document().put("id", "c").put("price_d", 200.5).build());
			index.commit();

			LuceneLocalSearchApi api = new LuceneLocalSearchApi(index, schema);

			// range: price_d >= 100 AND price_d <= 200
			JsonNode result = api.search("idx/_search", JsonNode.parse("""
					{
					  "size": 10,
					  "query": {"range": {"price_d": {"gte": 100.0, "lte": 200.0}}}
					}
					"""));

			System.out.println("testDoubleField_rangeQuery: " + result.toJson());
			assertEquals(1, result.get("hits").get("total").get("value").asInt());
			assertEquals("b", result.get("hits").get("hits").get(0).get("_source").get("id").asString());
		}
	}

	// -----------------------------------------------------------------------
	// DATE field
	// -----------------------------------------------------------------------

	public void testDateField_indexAndRetrieve() throws Exception {
		SearchSchema schema = new SearchSchema();
		schema.add("id", FieldTypeDef.keyword().stored(true));
		schema.add("created_dt", FieldTypeDef.date().stored(true));

		try (LuceneIndex index = new LuceneIndex()) {
			index.add(schema.document().put("id", "1").put("created_dt", "2026-08-19T10:30:00Z").build());
			index.commit();

			LuceneLocalSearchApi api = new LuceneLocalSearchApi(index, schema);

			JsonNode result = api.search("idx/_search", JsonNode.parse("""
					{"size": 10, "query": {"match_all": {}}}
					"""));

			System.out.println("testDateField_indexAndRetrieve: " + result.toJson());
			assertEquals(1, result.get("hits").get("total").get("value").asInt());
		}
	}

	public void testDateField_rangeQuery() throws Exception {
		SearchSchema schema = new SearchSchema();
		schema.add("id", FieldTypeDef.keyword().stored(true));
		schema.add("created_dt", FieldTypeDef.date().stored(true).aggregatable(true));

		try (LuceneIndex index = new LuceneIndex()) {
			// 2026-07-01
			index.add(schema.document().put("id", "1").put("created_dt", "2026-07-01T00:00:00Z").build());
			// 2026-08-19
			index.add(schema.document().put("id", "2").put("created_dt", "2026-08-19T10:30:00Z").build());
			// 2026-09-30
			index.add(schema.document().put("id", "3").put("created_dt", "2026-09-30T00:00:00Z").build());
			index.commit();

			LuceneLocalSearchApi api = new LuceneLocalSearchApi(index, schema);

			// range: created_dt >= 2026-08-01 AND created_dt < 2026-09-01
			JsonNode result = api.search("idx/_search", JsonNode.parse("""
					{
					  "size": 10,
					  "query": {
					    "range": {
					      "created_dt": {
					        "gte": "2026-08-01T00:00:00Z",
					        "lt":  "2026-09-01T00:00:00Z"
					      }
					    }
					  }
					}
					"""));

			System.out.println("testDateField_rangeQuery: " + result.toJson());
			assertEquals(1, result.get("hits").get("total").get("value").asInt());
			assertEquals("2", result.get("hits").get("hits").get(0).get("_source").get("id").asString());
		}
	}

	// -----------------------------------------------------------------------
	// DynamicFieldResolver integration: addJson via LocalSearch
	// -----------------------------------------------------------------------

	public void testDynamicField_integerViaSuffix() throws Exception {
		nlp4j.lucene.LocalSearch search = nlp4j.lucene.LocalSearch.builder("en").autoAnalyze(false).build();

		search.addJson("""
				{"id":"1","text":"hello","year_i":2025}
				""");
		search.addJson("""
				{"id":"2","text":"world","year_i":2023}
				""");
		search.commit();

		// range で year_i=2025 のみ取得
		nlp4j.lucene.SearchResult[] results = search.searchJson("""
				{
				  "size": 10,
				  "query": {"range": {"year_i": {"gte": 2025, "lte": 2025}}}
				}
				""");

		System.out.println("testDynamicField_integerViaSuffix: hits=" + results.length);
		assertEquals(1, results.length);
		assertEquals("1", results[0].id);

		search.close();
	}

	public void testDynamicField_dateViaSuffix() throws Exception {
		nlp4j.lucene.LocalSearch search = nlp4j.lucene.LocalSearch.builder("en").autoAnalyze(false).build();

		search.addJson("""
				{"id":"1","text":"early","created_dt":"2026-07-01T00:00:00Z"}
				""");
		search.addJson("""
				{"id":"2","text":"late","created_dt":"2026-10-01T00:00:00Z"}
				""");
		search.commit();

		nlp4j.lucene.SearchResult[] results = search.searchJson("""
				{
				  "size": 10,
				  "query": {
				    "range": {
				      "created_dt": {
				        "gte": "2026-08-01T00:00:00Z",
				        "lt":  "2026-09-01T00:00:00Z"
				      }
				    }
				  }
				}
				""");

		System.out.println("testDynamicField_dateViaSuffix: hits=" + results.length);
		assertEquals(0, results.length);

		search.close();
	}

	public void testExplicitFieldOverride() throws Exception {
		// Builder.field() で明示した型が suffix より優先されることを確認
		nlp4j.lucene.LocalSearch search = nlp4j.lucene.LocalSearch.builder("en")
				.autoAnalyze(false)
				.field("year_i", FieldTypeDef.longNumber().stored(true).aggregatable(true))
				.build();

		search.addJson("""
				{"id":"1","text":"test","year_i":2025}
				""");
		search.commit();

		// LONG として登録されているので term で LONG exact query が使われる
		nlp4j.lucene.SearchResult[] results = search.searchJson("""
				{
				  "size": 10,
				  "query": {"term": {"year_i": 2025}}
				}
				""");

		System.out.println("testExplicitFieldOverride: hits=" + results.length);
		assertEquals(1, results.length);

		search.close();
	}
}
