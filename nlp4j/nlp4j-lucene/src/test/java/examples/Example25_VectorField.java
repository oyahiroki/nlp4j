/*
 * Copyright (C) 2026 Hiroki OYA
 *
 * Licensed under the Apache License, Version 2.0
 */
package examples;

import nlp4j.lucene.LocalSearch;
import nlp4j.lucene.SearchResult;
import nlp4j.lucene.VectorSimilarity;

/**
 * Example25_VectorField
 *
 * <p>
 * Demonstrates:
 * </p>
 *
 * <ol>
 * <li>Defining a named KNN vector field</li>
 * <li>Adding vector values from JSON</li>
 * <li>Reading vector field metadata</li>
 * <li>Running KNN vector search</li>
 * <li>Running KNN vector search with a Lucene query filter</li>
 * </ol>
 *
 * <p>
 * This example uses simple 3-dimensional vectors so that the behavior is easy
 * to understand without an embedding model.
 * </p>
 */
public class Example25_VectorField {

	public static void main(String[] args) throws Exception {

		try (LocalSearch search = LocalSearch.builder("en") //
				.autoAnalyze(false) //
				.vectorField( //
						"vector3", //
						3, //
						VectorSimilarity.COSINE, //
						"demo-3d") //
				.build()) {

			// ------------------------------------------------------------
			// Add documents
			//
			// vector3 is explicitly defined as a KNN_VECTOR field.
			// category_s is dynamically resolved as a KEYWORD field.
			// ------------------------------------------------------------

			search.addJson("""
					{
					  "id": "1",
					  "text_en": "Electric vehicle battery",
					  "category_s": "vehicle",
					  "vector3": [1.0, 0.0, 0.0]
					}
					""");

			search.addJson("""
					{
					  "id": "2",
					  "text_en": "Hybrid vehicle system",
					  "category_s": "vehicle",
					  "vector3": [0.9, 0.1, 0.0]
					}
					""");

			search.addJson("""
					{
					  "id": "3",
					  "text_en": "Computer software",
					  "category_s": "software",
					  "vector3": [0.0, 1.0, 0.0]
					}
					""");

			search.addJson("""
					{
					  "id": "4",
					  "text_en": "Cloud computing service",
					  "category_s": "software",
					  "vector3": [0.0, 0.9, 0.1]
					}
					""");

			search.commit();

			// ------------------------------------------------------------
			// 1. Vector field metadata
			// ------------------------------------------------------------

			System.out.println("=== Vector field metadata ===");

			System.out.println(
					"Type      : "
							+ search.getFieldType("vector3"));

			System.out.println(
					"Dimension : "
							+ search.getVectorDimension("vector3"));

			System.out.println(
					"Model     : "
							+ search.getVectorModel("vector3"));

			System.out.println();

			// ------------------------------------------------------------
			// 2. Vector search
			//
			// This query vector is closest to documents 1 and 2.
			// ------------------------------------------------------------

			float[] queryVector = {
					1.0f,
					0.0f,
					0.0f
			};

			System.out.println(
					"=== Vector search: vector3 ===");

			SearchResult[] results = search.searchVector(
					"vector3",
					queryVector,
					10);

			printResults(results);

			System.out.println();

			// ------------------------------------------------------------
			// 3. Vector search with Lucene query filter
			//
			// Only documents with category_s = vehicle are candidates.
			// ------------------------------------------------------------

			System.out.println(
					"=== Vector search: category_s:\"vehicle\" ===");

			results = search.searchVector(
					"vector3",
					queryVector,
					10,
					"category_s:\"vehicle\"");

			printResults(results);

			System.out.println();

			// ------------------------------------------------------------
			// 4. Another query vector
			//
			// This query vector is closest to documents 3 and 4.
			// ------------------------------------------------------------

			float[] softwareVector = {
					0.0f,
					1.0f,
					0.0f
			};

			System.out.println(
					"=== Vector search: software direction ===");

			results = search.searchVector(
					"vector3",
					softwareVector,
					10);

			printResults(results);
		}
	}

	private static void printResults(
			SearchResult[] results) {

		for (SearchResult result : results) {

			System.out.printf(
					"id=%s score=%.4f text=%s%n",
					result.id,
					result.score,
					result.body);
		}
	}
}