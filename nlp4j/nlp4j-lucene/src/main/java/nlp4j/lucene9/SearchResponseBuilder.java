/*
 * Copyright (C) 2026 Hiroki OYA
 *
 * Licensed under the Apache License, Version 2.0
 */
package nlp4j.lucene9;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexableField;

import nlp4j.json.JsonNode;

/**
 * Builder for constructing OpenSearch-compatible JSON responses from search results.
 * Formats search hits and aggregations into the expected JSON structure.
 */
public class SearchResponseBuilder {
	
	/**
	 * Builds a complete search response in OpenSearch-compatible JSON format.
	 *
	 * @param result the search result containing hits and total count
	 * @param aggregations the aggregation results (can be null or empty)
	 * @return a JsonNode containing the formatted response
	 */
	public static JsonNode build(SearchResult result, JsonNode aggregations) {
		JsonNode root = JsonNode.object();

		JsonNode hitsObject = JsonNode.object();

		JsonNode total = JsonNode.object();
		total.put("value", result.total());
		total.put("relation", "eq");

		hitsObject.put("total", total);

		JsonNode hitsArray = JsonNode.array();

		for (SearchHit hit : result.hits()) {
			JsonNode hitJson = JsonNode.object();

			hitJson.put("_id", hit.id());
			hitJson.put("_score", hit.score());

			hitJson.put("_source", toJson(hit.document()));

			hitsArray.add(hitJson);
		}

		hitsObject.put("hits", hitsArray);
		root.put("hits", hitsObject);

		if (aggregations != null && !aggregations.isNull() && aggregations.size() > 0) {
			root.put("aggregations", aggregations);
		}

		return root;
	}

	/**
	 * Converts a Lucene Document to a JsonNode representation.
	 * Handles multi-valued fields by creating arrays.
	 *
	 * @param document the Lucene Document to convert
	 * @return a JsonNode containing the document fields
	 */
	private static JsonNode toJson(Document document) {
		JsonNode source = JsonNode.object();

		for (IndexableField field : document.getFields()) {
			String name = field.name();
			String value = field.stringValue();

			if (value == null) {
				continue;
			}

			if (source.has(name)) {
				JsonNode existing = source.get(name);
				if (existing.isArray()) {
					existing.add(value);
				} else {
					JsonNode array = JsonNode.array();
					array.add(existing.asString());
					array.add(value);
					source.put(name, array);
				}
			} else {
				source.put(name, value);
			}
		}

		return source;
	}
}
