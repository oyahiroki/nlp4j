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
import java.time.ZoneId;

/**
 * Executor for performing aggregations on search results.
 * Supports terms aggregations and date histogram aggregations.
 */
public class AggregationExecutor {

	/**
	 * Executes all aggregations defined in the search request.
	 *
	 * @param searcher the Lucene IndexSearcher to use
	 * @param query    the Lucene Query to filter documents
	 * @param request  the search request containing aggregation definitions
	 * @param schema   the SearchSchema used for field type resolution
	 * @param zoneId   the ZoneId used for date histogram bucket calculation
	 * @return a JsonNode containing aggregation results, or an empty object if no aggregations
	 * @throws IOException if an I/O error occurs during aggregation
	 * @throws IllegalArgumentException if an unsupported aggregation type is encountered
	 */
	public static JsonNode execute(IndexSearcher searcher, Query query, SearchRequest request,
			SearchSchema schema, ZoneId zoneId) throws IOException {

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

			} else if (aggBody.has("date_histogram")) {
				JsonNode histogram = aggBody.get("date_histogram");

				String field = histogram.get("field").asString();

				// Resolve time_zone from JSON, fall back to builder zoneId
				ZoneId effectiveZoneId = zoneId;
				if (histogram.has("time_zone")) {
					effectiveZoneId = ZoneId.of(histogram.get("time_zone").asString());
				}

				// Field type validation
				FieldTypeDef type = TypedFieldQueryFactory.resolveFieldType(field, schema);

				if (type.kind() != FieldTypeDef.Kind.DATE) {
					throw new IllegalArgumentException(
							"date_histogram requires DATE field: " + field);
				}

				if (!type.is_aggregatable()) {
					throw new IllegalArgumentException(
							"DATE field is not aggregatable: " + field);
				}

				DateHistogramInterval interval = DateHistogramInterval.of(
						histogram.get("calendar_interval").asString());

				DateHistogramAggregation aggregation =
						new DateHistogramAggregation(field, interval, effectiveZoneId);

				result.put(aggName, aggregation.execute(searcher, query));

			} else {
				throw new IllegalArgumentException("Unsupported aggregation: " + aggBody);
			}
		}

		return result;
	}
}
