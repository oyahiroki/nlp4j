/*
 * Copyright (C) 2026 Hiroki OYA
 *
 * Licensed under the Apache License, Version 2.0
 */
package nlp4j.lucene;

import java.util.List;

/**
 * Summary statistics for all fields in the index.
 *
 * <p>
 * Returned by {@link LocalSearch#getFieldsSummary()}. Fields are returned in
 * schema registration order, and only fields that have at least one indexed
 * value are included.
 * </p>
 *
 * @since 1.7.2.0
 */
public final class FieldsSummary {

	private final long documentCount;
	private final List<FieldSummary> fields;

	FieldsSummary(long documentCount, List<FieldSummary> fields) {
		this.documentCount = documentCount;
		this.fields = List.copyOf(fields);
	}

	/**
	 * Total number of live documents in the index.
	 *
	 * @return live document count
	 */
	public long getDocumentCount() {
		return documentCount;
	}

	/**
	 * Per-field summaries in schema registration order. Only fields with at least
	 * one indexed value are included.
	 *
	 * @return unmodifiable list of field summaries
	 */
	public List<FieldSummary> getFields() {
		return fields; // already immutable via List.copyOf()
	}
}
