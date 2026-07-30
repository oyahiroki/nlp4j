package nlp4j.lucene9;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.KnnFloatVectorField;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.KnnFloatVectorQuery;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;

import nlp4j.json.JsonNode;

/**
 * Builder for converting OpenSearch-style JSON queries into Lucene Query objects.
 * Supports match_all, term, match, query_string, and knn query types.
 */
public class LuceneQueryBuilder {
	
	/**
	 * Builds a Lucene Query from a SearchRequest.
	 *
	 * @param request the search request containing the query definition
	 * @param analyzer the analyzer to use for query parsing
	 * @return a Lucene Query object
	 * @throws RuntimeException if the query type is unsupported or parsing fails
	 */
	public static Query build(SearchRequest request, Analyzer analyzer) {
		JsonNode queryJson = request.query();

		try {
			if (queryJson.has("match_all")) {
				return new MatchAllDocsQuery();
			}

			if (queryJson.has("term")) {
				return buildTermQuery(queryJson.get("term"));
			}

			if (queryJson.has("match")) {
				return buildMatchQuery(queryJson.get("match"), analyzer);
			}

			if (queryJson.has("query_string")) {
				return buildQueryStringQuery(queryJson.get("query_string"), analyzer);
			}
			
			if (queryJson.has("knn")) {
				return buildKnnQuery(queryJson.get("knn"), analyzer);
			}
	
			if (queryJson.has("bool")) {
				return buildBoolQuery(queryJson.get("bool"), analyzer);
			}
	
			throw new IllegalArgumentException("Unsupported query: " + queryJson);

		} catch (Exception e) {
			throw new RuntimeException("Failed to build Lucene query: " + queryJson, e);
		}
	}

	/**
	 * Builds a term query for exact matching.
	 *
	 * @param termJson the term query definition in JSON format
	 * @return a TermQuery object
	 */
	private static Query buildTermQuery(JsonNode termJson) {
		String field = termJson.keys().iterator().next();
		String value = termJson.get(field).asString();
		return new TermQuery(new Term(field, value));
	}

	/**
	 * Builds a match query with text analysis.
	 *
	 * @param matchJson the match query definition in JSON format
	 * @param analyzer the analyzer to use for text analysis
	 * @return a Query object
	 * @throws Exception if query parsing fails
	 */
	private static Query buildMatchQuery(JsonNode matchJson, Analyzer analyzer) throws Exception {

		String field = matchJson.keys().iterator().next();
		String text = matchJson.get(field).asString();

		QueryParser parser = new QueryParser(field, analyzer);
		return parser.parse(QueryParser.escape(text));
	}

	/**
	 * Builds a query_string query for advanced query syntax.
	 *
	 * @param qsJson the query_string definition in JSON format
	 * @param analyzer the analyzer to use for query parsing
	 * @return a Query object
	 * @throws Exception if query parsing fails
	 */
	private static Query buildQueryStringQuery(JsonNode qsJson, Analyzer analyzer) throws Exception {

		String q = qsJson.get("query").asString();

		String defaultField = qsJson.has("default_field") ? qsJson.get("default_field").asString() : "text";

		QueryParser parser = new QueryParser(defaultField, analyzer);
		return parser.parse(q);
	}

	/**
	 * Builds a bool query supporting must and filter clauses.
	 * must clauses are scored (MUST occurrence), filter clauses are unscored (FILTER occurrence).
	 *
	 * @param boolJson the bool query definition in JSON format
	 * @param analyzer the analyzer to use for inner query parsing
	 * @return a BooleanQuery object
	 * @throws Exception if inner query parsing fails
	 */
	private static Query buildBoolQuery(JsonNode boolJson, Analyzer analyzer) throws Exception {
		BooleanQuery.Builder builder = new BooleanQuery.Builder();

		if (boolJson.has("must")) {
			JsonNode mustNode = boolJson.get("must");
			List<JsonNode> clauses = mustNode.isArray() ? mustNode.asList()
					: List.of(mustNode);
			for (JsonNode clause : clauses) {
				SearchRequest dummy = new SearchRequest(null, 0, 0, clause, null);
				builder.add(build(dummy, analyzer), BooleanClause.Occur.MUST);
			}
		}

		if (boolJson.has("filter")) {
			JsonNode filterNode = boolJson.get("filter");
			List<JsonNode> clauses = filterNode.isArray() ? filterNode.asList()
					: List.of(filterNode);
			for (JsonNode clause : clauses) {
				SearchRequest dummy = new SearchRequest(null, 0, 0, clause, null);
				builder.add(build(dummy, analyzer), BooleanClause.Occur.FILTER);
			}
		}

		if (boolJson.has("should")) {
			JsonNode shouldNode = boolJson.get("should");
			List<JsonNode> clauses = shouldNode.isArray() ? shouldNode.asList()
					: List.of(shouldNode);
			for (JsonNode clause : clauses) {
				SearchRequest dummy = new SearchRequest(null, 0, 0, clause, null);
				builder.add(build(dummy, analyzer), BooleanClause.Occur.SHOULD);
			}
		}

		if (boolJson.has("must_not")) {
			JsonNode mustNotNode = boolJson.get("must_not");
			List<JsonNode> clauses = mustNotNode.isArray() ? mustNotNode.asList()
					: List.of(mustNotNode);
			for (JsonNode clause : clauses) {
				SearchRequest dummy = new SearchRequest(null, 0, 0, clause, null);
				builder.add(build(dummy, analyzer), BooleanClause.Occur.MUST_NOT);
			}
		}

		return builder.build();
	}

	/**
		* Builds a KNN (k-nearest neighbors) vector search query.
		*
		* @param knnJson the knn query definition in JSON format
		* @param analyzer the analyzer to use for filter query parsing
		* @return a Query object for vector search
		* @throws Exception if query parsing fails
		*/
	private static Query buildKnnQuery(JsonNode knnJson, Analyzer analyzer) throws Exception {
		String field = knnJson.get("field").asString();
		JsonNode vectorNode = knnJson.get("query_vector");
		int k = knnJson.get("k").asInt();
		
		// Convert query_vector to float[]
		float[] queryVector = parseFloatArray(vectorNode);
		
		// Check if filter exists
		if (knnJson.has("filter")) {
			// Build filter query recursively
			SearchRequest dummyRequest = new SearchRequest(null, 0, 0, knnJson.get("filter"), null);
			Query filterQuery = build(dummyRequest, analyzer);
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
