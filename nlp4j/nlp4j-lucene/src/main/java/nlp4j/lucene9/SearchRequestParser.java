package nlp4j.lucene9;

import nlp4j.json.JsonNode;

/**
 * Parser for converting OpenSearch-style search requests into SearchRequest
 * objects. Handles parsing of path, query parameters, knn parameters, and
 * aggregations from JSON format.
 */
public class SearchRequestParser {

	/**
	 * Parses a search request from path and JSON body. Supports both traditional
	 * query and knn (vector search) parameters. If knn parameter exists, it takes
	 * precedence over query parameter.
	 *
	 * @param path the search path (e.g., "myindex/_search")
	 * @param body the request body in JSON format
	 * @return a SearchRequest object containing parsed parameters
	 * @throws IllegalArgumentException if the path is invalid
	 */
	public static SearchRequest parse(String path, JsonNode body) {
		String indexName = parseIndexName(path);

		int from = body.has("from") ? body.get("from").asInt(0) : 0;
		int size = body.has("size") ? body.get("size").asInt(10) : 10;

		JsonNode query;

		// knn parameter takes precedence over query parameter
		if (body.has("knn")) {
			// Wrap knn as a query type
			query = JsonNode.object();
			query.put("knn", body.get("knn"));
		} //
		else if (body.has("query")) {
			query = body.get("query");
		} //
		else {
			query = defaultMatchAllQuery();
		}

		JsonNode aggs = null;

		if (body.has("aggs")) {
			aggs = body.get("aggs");
		} //
		else if (body.has("aggregations")) {
			aggs = body.get("aggregations");
		}

		return new SearchRequest(indexName, from, size, query, aggs);
	}

	/**
	 * Extracts the index name from the search path.
	 *
	 * @param path the search path (e.g., "myindex/_search")
	 * @return the index name
	 * @throws IllegalArgumentException if the path is invalid or empty
	 */
	private static String parseIndexName(String path) {
		// example: "myindex/_search"
		String[] parts = path.split("/");
		if (parts.length == 0 || parts[0].isBlank()) {
			throw new IllegalArgumentException("Invalid path: " + path);
		}
		return parts[0];
	}

	/**
	 * Creates a default match_all query.
	 *
	 * @return a JsonNode representing a match_all query
	 */
	private static JsonNode defaultMatchAllQuery() {
		JsonNode query = JsonNode.object();
		query.put("match_all", JsonNode.object());
		return query;
	}
}
