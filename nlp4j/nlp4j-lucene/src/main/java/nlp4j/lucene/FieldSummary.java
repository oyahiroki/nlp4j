/*
 * Copyright (C) 2026 Hiroki OYA
 *
 * Licensed under the Apache License, Version 2.0
 */
package nlp4j.lucene;

import nlp4j.lucene9.FieldTypeDef;

/**
 * Summary statistics for a single field.
 *
 * <p>
 * Returned by {@link LocalSearch#getFieldsSummary()}.
 * </p>
 *
 * <ul>
 * <li>Coverage = DocumentsWithValue / DocumentCount (aggregatable fields
 * only)</li>
 * <li>Unique = distinct value count (KEYWORD + aggregatable only)</li>
 * <li>Diversity = Unique / DocumentsWithValue (KEYWORD + aggregatable
 * only)</li>
 * <li>Example = first stored value found in live documents (all fields)</li>
 * </ul>
 *
 * <p>
 * Unavailable values are indicated by {@code -1} (long) or {@code -1.0}
 * (double). Use the corresponding {@code has*()} predicates before reading
 * computed values.
 * </p>
 *
 * @since 1.7.2.0
 */
public final class FieldSummary {

	private final String field;
	private final FieldTypeDef.Kind kind;
	private final boolean aggregatable;
	private final long documentCount;

	/**
	 * Number of live documents that have at least one value for this field. -1 =
	 * unavailable.
	 */
	private final long documentsWithValue;

	/**
	 * Distinct value count. Valid only for KEYWORD + aggregatable. -1 =
	 * unavailable.
	 */
	private final long uniqueValueCount;

	/** First stored value found in a live document. null = unavailable. */
	private final String example;

	FieldSummary(String field, FieldTypeDef.Kind kind, boolean aggregatable, long documentCount,
			long documentsWithValue, long uniqueValueCount, String example) {
		this.field = field;
		this.kind = kind;
		this.aggregatable = aggregatable;
		this.documentCount = documentCount;
		this.documentsWithValue = documentsWithValue;
		this.uniqueValueCount = uniqueValueCount;
		this.example = example;
	}

	public String getField() {
		return field;
	}

	public FieldTypeDef.Kind getKind() {
		return kind;
	}

	public boolean isAggregatable() {
		return aggregatable;
	}

	public long getDocumentCount() {
		return documentCount;
	}

	public long getDocumentsWithValue() {
		return documentsWithValue;
	}

	public boolean hasCoverage() {
		return aggregatable && documentsWithValue >= 0 && documentCount >= 0;
	}

	public double getCoverage() {
		if (!hasCoverage()) {
			return -1.0;
		}
		if (documentCount == 0) {
			return 0.0;
		}
		return (double) documentsWithValue / (double) documentCount;
	}

	public boolean hasUniqueValueCount() {
		return aggregatable && kind == FieldTypeDef.Kind.KEYWORD && uniqueValueCount >= 0;
	}

	public long getUniqueValueCount() {
		return uniqueValueCount;
	}

	public boolean hasDiversity() {
		return hasUniqueValueCount() && documentsWithValue >= 0;
	}

	public double getDiversity() {
		if (!hasDiversity()) {
			return -1.0;
		}
		if (documentsWithValue == 0) {
			return 0.0;
		}
		return (double) uniqueValueCount / (double) documentsWithValue;
	}

	public String getExample() {
		return example;
	}
}
