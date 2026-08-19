/*
 * Copyright (C) 2026 Hiroki OYA
 *
 * Licensed under the Apache License, Version 2.0
 */
package nlp4j.lucene9;

import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;

import nlp4j.json.JsonNode;

import java.io.IOException;

/**
 * Executor for performing aggregations on search results.
 * Currently supports terms aggregations.
 */
public class AggregationExecutor {
	
	/**
	 * Executes all aggregations defined in the search request.
	 *
	 * @param searcher the Lucene IndexSearcher to use
	 * @param query the Lucene Query to filter documents
	 * @param request the search request containing aggregation definitions
	 * @return a JsonNode containing aggregation results, or an empty object if no aggregations
	 * @throws IOException if an I/O error occurs during aggregation
	 * @throws IllegalArgumentException if an unsupported aggregation type is encountered
	 */
	public static JsonNode execute(IndexSearcher searcher, Query query, SearchRequest request) throws IOException {

		JsonNode aggs = request.aggregations();

		if (aggs == null || aggs.isNull() || aggs.size() == 0) {
			return JsonNode.object();
		}

		JsonNode result = JsonNode.object();

		for (String aggName : aggs.keys()) {
			JsonNode aggBody = aggs.get(aggName);

			if (aggBody.has("terms")) {
				JsonNode terms = aggBody.get("terms");

				TermsAggregation aggregation = new TermsAggregation(aggName, terms.get("field").asString(),
						terms.has("size") ? terms.get("size").asInt(10) : 10);

				result.put(aggName, aggregation.execute(searcher, query));
			} else {
				throw new IllegalArgumentException("Unsupported aggregation: " + aggBody);
			}
		}

		return result;
	}
}
