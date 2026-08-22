/*
 * Copyright (C) 2026 Hiroki OYA
 *
 * Licensed under the Apache License, Version 2.0
 */
package nlp4j.lucene9;

import java.io.IOException;
import java.time.ZoneId;

import org.apache.lucene.search.Query;

import nlp4j.json.JsonNode;

/**
 * Local search API for Lucene that provides OpenSearch-compatible REST API
 * interface. This class accepts OpenSearch-style JSON requests and returns JSON
 * responses. Uses LuceneIndex for managing the search infrastructure.
 */
public class LuceneLocalSearchApi {

	private final LuceneIndex index;
	private final SearchSchema schema;
	private final ZoneId zoneId;

	/**
	 * Constructs a new LuceneLocalSearchApi instance.
	 *
	 * @param index the LuceneIndex to use for searching
	 */
	public LuceneLocalSearchApi(LuceneIndex index) {
		this(index, new SearchSchema(), ZoneId.systemDefault());
	}

	/**
	 * Constructs a new LuceneLocalSearchApi instance with schema for typed queries.
	 *
	 * @param index  the LuceneIndex to use for searching
	 * @param schema the SearchSchema used to resolve field types for queries
	 */
	public LuceneLocalSearchApi(LuceneIndex index, SearchSchema schema) {
		this(index, schema, ZoneId.systemDefault());
	}

	/**
	 * Constructs a new LuceneLocalSearchApi instance with schema and timezone.
	 *
	 * @param index  the LuceneIndex to use for searching
	 * @param schema the SearchSchema used to resolve field types for queries
	 * @param zoneId timezone for DATE fields without offset
	 */
	public LuceneLocalSearchApi(LuceneIndex index, SearchSchema schema, ZoneId zoneId) {
		this.index = index;
		this.schema = (schema != null) ? schema : new SearchSchema();
		this.zoneId = (zoneId != null) ? zoneId : ZoneId.systemDefault();
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

			Query query = LuceneQueryBuilder.build(request, session.getAnalyzer(), schema, zoneId);

			SearchResult hits = SearchExecutor.execute(session.getSearcher(), query, request);

			JsonNode aggregations = AggregationExecutor.execute(session.getSearcher(), query, request);

			JsonNode searchResult = SearchResponseBuilder.build(hits, aggregations);

			return searchResult;
		}
	}
}
