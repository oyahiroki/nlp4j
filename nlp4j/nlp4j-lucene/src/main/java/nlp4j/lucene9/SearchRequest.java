package nlp4j.lucene9;

import nlp4j.json.JsonNode;

/**
 * Represents a search request containing query parameters and aggregations.
 * This class encapsulates all the information needed to execute a search operation.
 */
public class SearchRequest {
	private final String indexName;
	private final int from;
	private final int size;
	private final JsonNode query;
	private final JsonNode aggregations;

	/**
	 * Constructs a new SearchRequest.
	 *
	 * @param indexName the name of the index to search
	 * @param from the starting offset for pagination
	 * @param size the maximum number of results to return
	 * @param query the query definition in JSON format
	 * @param aggregations the aggregations definition in JSON format (can be null)
	 */
	public SearchRequest(String indexName, int from, int size, JsonNode query, JsonNode aggregations) {

		this.indexName = indexName;
		this.from = from;
		this.size = size;
		this.query = query;
		this.aggregations = aggregations;
	}

	/**
	 * Returns the index name.
	 *
	 * @return the index name
	 */
	public String indexName() {
		return indexName;
	}

	/**
	 * Returns the starting offset for pagination.
	 *
	 * @return the from offset
	 */
	public int from() {
		return from;
	}

	/**
	 * Returns the maximum number of results to return.
	 *
	 * @return the size limit
	 */
	public int size() {
		return size;
	}

	/**
	 * Returns the query definition.
	 *
	 * @return the query in JSON format
	 */
	public JsonNode query() {
		return query;
	}

	/**
	 * Returns the aggregations definition.
	 *
	 * @return the aggregations in JSON format, or null if no aggregations
	 */
	public JsonNode aggregations() {
		return aggregations;
	}
}
