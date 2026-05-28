package nlp4j.lucene9;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;

import nlp4j.json.JsonNode;

/**
 * Builder for converting OpenSearch-style JSON queries into Lucene Query objects.
 * Supports match_all, term, match, and query_string query types.
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
}
