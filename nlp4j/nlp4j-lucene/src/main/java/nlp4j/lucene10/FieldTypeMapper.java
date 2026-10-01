/*
 * Copyright (C) 2026 Hiroki OYA
 *
 * Licensed under the Apache License, Version 2.0
 */
package nlp4j.lucene10;

import org.apache.lucene.index.VectorSimilarityFunction;

import nlp4j.lucene.VectorSimilarity;

/**
 * Mapper between NLP4J logical field types and Lucene 10 physical types.
 */
final class FieldTypeMapper {

	private FieldTypeMapper() {
	}

	static VectorSimilarityFunction toLucene(VectorSimilarity similarity) {
		if (similarity == null) {
			return VectorSimilarityFunction.COSINE;
		}
		return switch (similarity) {
		case COSINE -> VectorSimilarityFunction.COSINE;
		case DOT_PRODUCT -> VectorSimilarityFunction.DOT_PRODUCT;
		case EUCLIDEAN -> VectorSimilarityFunction.EUCLIDEAN;
		case MAXIMUM_INNER_PRODUCT -> VectorSimilarityFunction.MAXIMUM_INNER_PRODUCT;
		};
	}

}
