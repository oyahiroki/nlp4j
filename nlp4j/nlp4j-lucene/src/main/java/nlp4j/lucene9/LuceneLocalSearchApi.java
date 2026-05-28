package nlp4j.lucene9;

import java.io.IOException;

import org.apache.lucene.search.Query;

import nlp4j.json.JsonNode;
import nlp4j.lucene9.LuceneIndex.SearchSession;

/**
 * Local search API for Lucene that provides OpenSearch-compatible REST API
 * interface. This class accepts OpenSearch-style JSON requests and returns JSON
 * responses. Uses LuceneIndex for managing the search infrastructure.
 */
public class LuceneLocalSearchApi {

	private final LuceneIndex index;

	/**
	 * Constructs a new LuceneLocalSearchApi instance.
	 *
	 * @param index the LuceneIndex to use for searching
	 */
	public LuceneLocalSearchApi(LuceneIndex index) {
		this.index = index;
	}

	/**
	 * Executes a search request using OpenSearch-compatible JSON format.
	 *
	 * @param path        the search path (e.g., "myindex/_search")
	 * @param requestBody the search request body in JSON format
	 * @return the search response in JSON format
	 * @throws IOException if an I/O error occurs during search
	 */
	public JsonNode search(String path, JsonNode requestBody) throws IOException {
		SearchRequest request = SearchRequestParser.parse(path, requestBody);

		try (SearchSession session = index.acquireSearcher()) {
			Query query = LuceneQueryBuilder.build(request, session.getAnalyzer());

			SearchResult hits = SearchExecutor.execute(session.getSearcher(), query, request);

			JsonNode aggregations = AggregationExecutor.execute(session.getSearcher(), query, request);

			JsonNode searchResult = SearchResponseBuilder.build(hits, aggregations);

			return searchResult;
		}
	}
}
