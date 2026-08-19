/*
 * Copyright (C) 2026 Hiroki OYA
 *
 * Licensed under the Apache License, Version 2.0
 */
package nlp4j.lucene9;

import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.KnnFloatVectorField;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.KnnFloatVectorQuery;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;

import nlp4j.json.JsonNode;

/**
 * Builder for converting OpenSearch-style JSON queries into Lucene Query
 * objects. Supports match_all, term, match, query_string, and knn query types.
 */
public class LuceneQueryBuilder {

	/**
	 * Parses a Lucene query string.
	 *
	 * <p>
	 * This method only parses the query and does not execute a search.
	 * </p>
	 *
	 * @param query        Lucene query string
	 * @param defaultField default field used by QueryParser
	 * @param analyzer     analyzer used for query parsing
	 * @return parsed Lucene Query
	 * @throws Exception if parsing fails
	 */
	public static Query parseQueryString(String query, String defaultField, Analyzer analyzer) throws Exception {
		return parseQueryString(query, defaultField, analyzer, null);
	}

	/**
	 * Parses a Lucene query string using a schema-aware parser when schema is provided.
	 *
	 * @param query        Lucene query string
	 * @param defaultField default field used by QueryParser
	 * @param analyzer     analyzer used for query parsing
	 * @param schema       the SearchSchema for field type resolution (may be null)
	 * @return parsed Lucene Query
	 * @throws Exception if parsing fails
	 */
	public static Query parseQueryString(
			String query,
			String defaultField,
			Analyzer analyzer,
			SearchSchema schema) throws Exception {

		if (query == null || query.isBlank()) {
			throw new IllegalArgumentException("query must not be null or empty");
		}

		if (defaultField == null || defaultField.isBlank()) {
			throw new IllegalArgumentException("defaultField must not be null or empty");
		}

		QueryParser parser = (schema == null)
				? new QueryParser(defaultField, analyzer)
				: new SchemaAwareQueryParser(defaultField, analyzer, schema);

		return parser.parse(query);
	}

	/**
	 * Builds a Lucene Query from a SearchRequest.
	 *
	 * @param request  the search request containing the query definition
	 * @param analyzer the analyzer to use for query parsing
	 * @return a Lucene Query object
	 * @throws RuntimeException if the query type is unsupported or parsing fails
	 */
	/**
	 * Builds a Lucene Query from a SearchRequest (schema-aware).
	 *
	 * @param request  the search request containing the query definition
	 * @param analyzer the analyzer to use for query parsing
	 * @param schema   the SearchSchema for field type resolution
	 * @return a Lucene Query object
	 * @throws RuntimeException if the query type is unsupported or parsing fails
	 */
	public static Query build(SearchRequest request, Analyzer analyzer, SearchSchema schema) {
		JsonNode queryJson = request.query();
		SearchSchema effectiveSchema = (schema != null) ? schema : new SearchSchema();

		try {
			if (queryJson.has("match_all")) {
				return new MatchAllDocsQuery();
			}

			if (queryJson.has("term")) {
				return buildTermQuery(queryJson.get("term"), effectiveSchema);
			}

			if (queryJson.has("match")) {
				return buildMatchQuery(queryJson.get("match"), analyzer);
			}

			if (queryJson.has("query_string")) {
				return buildQueryStringQuery(queryJson.get("query_string"), analyzer, effectiveSchema);
			}

			if (queryJson.has("knn")) {
				return buildKnnQuery(queryJson.get("knn"), analyzer, effectiveSchema);
			}

			if (queryJson.has("bool")) {
				return buildBoolQuery(queryJson.get("bool"), analyzer, effectiveSchema);
			}

			if (queryJson.has("range")) {
				return buildRangeQuery(queryJson.get("range"), effectiveSchema);
			}

			throw new IllegalArgumentException("Unsupported query: " + queryJson);

		} catch (Exception e) {
			throw new RuntimeException("Failed to build Lucene query: " + queryJson, e);
		}
	}

	/**
	 * Builds a Lucene Query from a SearchRequest (schema-unaware, backward compatible).
	 *
	 * @param request  the search request containing the query definition
	 * @param analyzer the analyzer to use for query parsing
	 * @return a Lucene Query object
	 * @throws RuntimeException if the query type is unsupported or parsing fails
	 */
	public static Query build(SearchRequest request, Analyzer analyzer) {
		return build(request, analyzer, null);
	}

	/**
	 * Builds a term query for exact matching. Handles Numeric/Date types when schema is provided.
	 *
	 * @param termJson the term query definition in JSON format
	 * @param schema   the SearchSchema for field type resolution
	 * @return a Query object (TermQuery for KEYWORD/TEXT, numeric exact for others)
	 */
	private static Query buildTermQuery(JsonNode termJson, SearchSchema schema) {
		String field = termJson.keys().iterator().next();
		String value = termJson.get(field).asString();
		return TypedFieldQueryFactory.newExactQuery(field, value, schema);
	}

	/**
	 * Builds a range query for Numeric/Date fields.
	 *
	 * <p>
	 * Supported operators: gte, gt, lte, lt.
	 * </p>
	 *
	 * <pre>
	 * {"range": {"year_i": {"gte": 2020, "lte": 2026}}}
	 * {"range": {"created_dt": {"gte": "2026-08-01T00:00:00Z", "lt": "2026-09-01T00:00:00Z"}}}
	 * </pre>
	 *
	 * @param rangeJson the range query definition in JSON format
	 * @param schema    the SearchSchema for field type resolution
	 * @return a Lucene range Query
	 */
	private static Query buildRangeQuery(JsonNode rangeJson, SearchSchema schema) {
		String field = rangeJson.keys().iterator().next();
		JsonNode conditions = rangeJson.get(field);

		String lower = null;
		String upper = null;
		boolean lowerInclusive = true;
		boolean upperInclusive = true;

		if (conditions.has("gte")) { lower = conditions.get("gte").asString(); lowerInclusive = true; }
		if (conditions.has("gt"))  { lower = conditions.get("gt").asString();  lowerInclusive = false; }
		if (conditions.has("lte")) { upper = conditions.get("lte").asString(); upperInclusive = true; }
		if (conditions.has("lt"))  { upper = conditions.get("lt").asString();  upperInclusive = false; }

		return TypedFieldQueryFactory.newRangeQuery(field, lower, upper, lowerInclusive, upperInclusive, schema);
	}

	/**
	 * Builds a match query with text analysis.
	 *
	 * @param matchJson the match query definition in JSON format
	 * @param analyzer  the analyzer to use for text analysis
	 * @return a Query object
	 * @throws Exception if query parsing fails
	 */
	private static Query buildMatchQuery(JsonNode matchJson, Analyzer analyzer) throws Exception {

		String field = matchJson.keys().iterator().next();
		String text = matchJson.get(field).asString();

		QueryParser parser = new QueryParser(field, analyzer);
		return parser.parse(QueryParser.escape(text)); // throws ParseException
	}

	/**
	 * Builds a query_string query for advanced query syntax.
	 *
	 * @param qsJson   the query_string definition in JSON format
	 * @param analyzer the analyzer to use for query parsing
	 * @return a Query object
	 * @throws Exception if query parsing fails
	 */
	private static Query buildQueryStringQuery(JsonNode qsJson, Analyzer analyzer, SearchSchema schema)
			throws Exception {

		if (qsJson == null || qsJson.isNull()) {
			throw new IllegalArgumentException("query_string must not be null");
		}

		JsonNode queryNode = qsJson.get("query");

		if (queryNode == null || queryNode.isNull()) {
			throw new IllegalArgumentException("query_string.query is required");
		}

		String query = queryNode.asString(null);

		if (query == null || query.isBlank()) {
			throw new IllegalArgumentException("query_string.query must not be empty");
		}

		String defaultField = "text";

		JsonNode defaultFieldNode = qsJson.get("default_field");

		if (defaultFieldNode != null && !defaultFieldNode.isNull()) {
			String value = defaultFieldNode.asString(null);
			if (value != null && !value.isBlank()) {
				defaultField = value;
			}
		}

		return parseQueryString(query, defaultField, analyzer, schema);
	}

	/**
	 * Builds a bool query supporting must and filter clauses. must clauses are
	 * scored (MUST occurrence), filter clauses are unscored (FILTER occurrence).
	 *
	 * @param boolJson the bool query definition in JSON format
	 * @param analyzer the analyzer to use for inner query parsing
	 * @return a BooleanQuery object
	 * @throws Exception if inner query parsing fails
	 */
	private static Query buildBoolQuery(JsonNode boolJson, Analyzer analyzer, SearchSchema schema) throws Exception {
		BooleanQuery.Builder builder = new BooleanQuery.Builder();

		if (boolJson.has("must")) {
			JsonNode mustNode = boolJson.get("must");
			List<JsonNode> clauses = mustNode.isArray() ? mustNode.asList() : List.of(mustNode);
			for (JsonNode clause : clauses) {
				SearchRequest dummy = new SearchRequest(null, 0, 0, clause, null);
				builder.add(build(dummy, analyzer, schema), BooleanClause.Occur.MUST);
			}
		}

		if (boolJson.has("filter")) {
			JsonNode filterNode = boolJson.get("filter");
			List<JsonNode> clauses = filterNode.isArray() ? filterNode.asList() : List.of(filterNode);
			for (JsonNode clause : clauses) {
				SearchRequest dummy = new SearchRequest(null, 0, 0, clause, null);
				builder.add(build(dummy, analyzer, schema), BooleanClause.Occur.FILTER);
			}
		}

		if (boolJson.has("should")) {
			JsonNode shouldNode = boolJson.get("should");
			List<JsonNode> clauses = shouldNode.isArray() ? shouldNode.asList() : List.of(shouldNode);
			for (JsonNode clause : clauses) {
				SearchRequest dummy = new SearchRequest(null, 0, 0, clause, null);
				builder.add(build(dummy, analyzer, schema), BooleanClause.Occur.SHOULD);
			}
		}

		if (boolJson.has("must_not")) {
			JsonNode mustNotNode = boolJson.get("must_not");
			List<JsonNode> clauses = mustNotNode.isArray() ? mustNotNode.asList() : List.of(mustNotNode);
			for (JsonNode clause : clauses) {
				SearchRequest dummy = new SearchRequest(null, 0, 0, clause, null);
				builder.add(build(dummy, analyzer, schema), BooleanClause.Occur.MUST_NOT);
			}
		}

		return builder.build();
	}

	/**
	 * Builds a KNN (k-nearest neighbors) vector search query.
	 *
	 * @param knnJson  the knn query definition in JSON format
	 * @param analyzer the analyzer to use for filter query parsing
	 * @param schema   the SearchSchema for field type resolution
	 * @return a Query object for vector search
	 * @throws Exception if query parsing fails
	 */
	private static Query buildKnnQuery(JsonNode knnJson, Analyzer analyzer, SearchSchema schema) throws Exception {
		String field = knnJson.get("field").asString();
		JsonNode vectorNode = knnJson.get("query_vector");
		int k = knnJson.get("k").asInt();

		// Convert query_vector to float[]
		float[] queryVector = parseFloatArray(vectorNode);

		// Check if filter exists
		if (knnJson.has("filter")) {
			// Build filter query recursively
			SearchRequest dummyRequest = new SearchRequest(null, 0, 0, knnJson.get("filter"), null);
			Query filterQuery = build(dummyRequest, analyzer, schema);
			return new KnnFloatVectorQuery(field, queryVector, k, filterQuery);
		}

		// No filter
		return KnnFloatVectorField.newVectorQuery(field, queryVector, k);
	}

	/**
	 * Converts a JsonNode array to a float array.
	 *
	 * @param arrayNode the JsonNode containing an array of numbers
	 * @return a float array
	 * @throws IllegalArgumentException if the node is not an array
	 */
	private static float[] parseFloatArray(JsonNode arrayNode) {
		if (!arrayNode.isArray()) {
			throw new IllegalArgumentException("query_vector must be an array");
		}

		int size = arrayNode.size();
		float[] result = new float[size];

		for (int i = 0; i < size; i++) {
			result[i] = (float) arrayNode.get(i).asDouble(0.0);
		}

		return result;
	}
}
