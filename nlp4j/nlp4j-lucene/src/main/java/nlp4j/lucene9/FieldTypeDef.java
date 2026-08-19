/*
 * Copyright (C) 2026 Hiroki OYA
 *
 * Licensed under the Apache License, Version 2.0
 */
package nlp4j.lucene9;

import org.apache.lucene.index.VectorSimilarityFunction;

/**
 * Logical field definition for LuceneLocalSearchApi.
 *
 * This class hides low-level Lucene field choices such as: - StringField -
 * TextField - StoredField - SortedDocValuesField - NumericDocValuesField -
 * LongPoint - KnnFloatVectorField
 */
public class FieldTypeDef {

	public enum Kind {
		KEYWORD, TEXT, INTEGER, LONG, DOUBLE, DATE, KNN_VECTOR, STORED_ONLY
	}

	private final Kind kind;

	private boolean stored;
	private boolean aggregatable;
	private boolean sortable;
	private boolean range;
	private boolean multiValued;

	private int dimension = -1;
	private VectorSimilarityFunction vectorSimilarityFunction = VectorSimilarityFunction.COSINE;

	private FieldTypeDef(Kind kind) {
		this.kind = kind;
	}

	public static FieldTypeDef keyword() {
		return new FieldTypeDef(Kind.KEYWORD);
	}

	public static FieldTypeDef text() {
		return new FieldTypeDef(Kind.TEXT);
	}

	public static FieldTypeDef integer() {
		return new FieldTypeDef(Kind.INTEGER);
	}

	public static FieldTypeDef longNumber() {
		return new FieldTypeDef(Kind.LONG);
	}

	public static FieldTypeDef doubleNumber() {
		return new FieldTypeDef(Kind.DOUBLE);
	}

	public static FieldTypeDef date() {
		return new FieldTypeDef(Kind.DATE);
	}

	public static FieldTypeDef knnVector(int dimension) {
		if (dimension <= 0) {
			throw new IllegalArgumentException("dimension must be > 0");
		}

		FieldTypeDef def = new FieldTypeDef(Kind.KNN_VECTOR);
		def.dimension = dimension;
		return def;
	}

	/**
	 * Creates a stored-only field definition.
	 * This field is not searchable, sortable, or aggregatable.
	 * It only stores data for retrieval in search results.
	 * Useful for storing raw JSON, metadata, or display-only data.
	 *
	 * @return a new FieldTypeDef for stored-only fields
	 */
	public static FieldTypeDef storedOnly() {
		FieldTypeDef def = new FieldTypeDef(Kind.STORED_ONLY);
		def.stored = true; // Always stored
		return def;
	}

	public FieldTypeDef stored() {
		return stored(true);
	}

	public FieldTypeDef stored(boolean stored) {
		this.stored = stored;
		return this;
	}

	public FieldTypeDef aggregatable() {
		return aggregatable(true);
	}

	public FieldTypeDef aggregatable(boolean aggregatable) {
		this.aggregatable = aggregatable;
		return this;
	}

	public FieldTypeDef sortable() {
		return sortable(true);
	}

	public FieldTypeDef sortable(boolean sortable) {
		this.sortable = sortable;
		return this;
	}

	public FieldTypeDef range() {
		return range(true);
	}

	public FieldTypeDef range(boolean range) {
		this.range = range;
		return this;
	}

	public FieldTypeDef multiValued() {
		return multiValued(true);
	}

	public FieldTypeDef multiValued(boolean multiValued) {
		this.multiValued = multiValued;
		return this;
	}

	public FieldTypeDef similarity(VectorSimilarityFunction vectorSimilarityFunction) {
		if (vectorSimilarityFunction == null) {
			throw new IllegalArgumentException("vectorSimilarityFunction must not be null");
		}
		this.vectorSimilarityFunction = vectorSimilarityFunction;
		return this;
	}

	public Kind kind() {
		return kind;
	}

	public boolean is_stored() {
		return stored;
	}

	public boolean is_aggregatable() {
		return aggregatable;
	}

	public boolean is_sortable() {
		return sortable;
	}

	public boolean is_range() {
		return range;
	}

	public boolean is_multiValued() {
		return multiValued;
	}

	public int get_dimension() {
		return dimension;
	}

	public VectorSimilarityFunction vectorSimilarityFunction() {
		return vectorSimilarityFunction;
	}
}