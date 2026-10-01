/*
 * Copyright (C) 2026 Hiroki OYA
 *
 * Licensed under the Apache License, Version 2.0
 */
package nlp4j.lucene;

/**
 * Vector similarity function for kNN vector search in NLP4J.
 *
 * @since 1.8.0.0
 */
public enum VectorSimilarity {

	COSINE,

	DOT_PRODUCT,

	EUCLIDEAN,

	MAXIMUM_INNER_PRODUCT

}
