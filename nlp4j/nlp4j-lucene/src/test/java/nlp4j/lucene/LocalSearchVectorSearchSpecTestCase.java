/*
 * Copyright (C) 2026 Hiroki OYA
 *
 * Licensed under the Apache License, Version 2.0
 */
package nlp4j.lucene;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import org.apache.lucene.index.VectorSimilarityFunction;

import junit.framework.TestCase;
import nlp4j.analytics.LocalAnalytics;
import nlp4j.lucene9.FieldTypeDef;
import nlp4j.lucene9.FieldValueConverter;
import nlp4j.lucene9.SearchSchema;
import nlp4j.lucene9.SearchSchemaStore;

/**
 * JUnit3 test case verifying all specifications described in kaiwa0924-2105.md.
 */
public class LocalSearchVectorSearchSpecTestCase extends TestCase {

	private Path tempDir;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		tempDir = Files.createTempDirectory("nlp4j-vector-spec-test-");
	}

	@Override
	protected void tearDown() throws Exception {
		if (tempDir != null && Files.exists(tempDir)) {
			// clean up
			try (var s = Files.walk(tempDir)) {
				s.sorted(java.util.Comparator.reverseOrder())
						.map(Path::toFile)
						.forEach(java.io.File::delete);
			}
		}
		super.tearDown();
	}

	// ========================================================================
	// 1 & 2. FieldTypeDef & Vector schema metadata (dimension / similarity / model)
	// ========================================================================

	public void testVectorFieldTypeDefAndMetadata() {
		FieldTypeDef def = FieldTypeDef.knnVector(1024, VectorSimilarityFunction.COSINE, "multilingual-e5-large");
		assertEquals(FieldTypeDef.Kind.KNN_VECTOR, def.kind());
		assertEquals(1024, def.get_dimension());
		assertEquals(VectorSimilarityFunction.COSINE, def.vectorSimilarityFunction());
		assertEquals("multilingual-e5-large", def.get_model());
		assertFalse(def.is_aggregatable());

		FieldTypeDef def2 = FieldTypeDef.knnVector(512);
		assertEquals(FieldTypeDef.Kind.KNN_VECTOR, def2.kind());
		assertEquals(512, def2.get_dimension());
		assertEquals(VectorSimilarityFunction.COSINE, def2.vectorSimilarityFunction());
		assertNull(def2.get_model());
	}

	// ========================================================================
	// 3. Builder vectorField definition
	// ========================================================================

	public void testBuilderVectorField() throws Exception {
		try (LocalSearch search = LocalSearch.builder("ja")
				.vectorField("vector", 1024, VectorSimilarityFunction.COSINE, "multilingual-e5-large")
				.build()) {

			assertEquals(1024, search.getVectorDimension());
			assertTrue(search.hasVectorField());
			assertEquals("multilingual-e5-large", search.getVectorModel("vector"));
			assertEquals(Integer.valueOf(1024), search.getVectorDimension("vector"));
			assertEquals("KNN_VECTOR", search.getFieldType("vector"));

			FieldTypeDef info = search.getFieldInfo("vector");
			assertNotNull(info);
			assertEquals(1024, info.get_dimension());
			assertEquals("multilingual-e5-large", info.get_model());
			assertEquals(VectorSimilarityFunction.COSINE, info.vectorSimilarityFunction());
		}
	}

	// ========================================================================
	// 4 & 5. FieldValueConverter.toFloatVector
	// ========================================================================

	public void testFieldValueConverterToFloatVector_ValidList() {
		List<Number> list = List.of(0.123, -0.456, 0.789);
		float[] vec = FieldValueConverter.toFloatVector(list, 3);
		assertEquals(3, vec.length);
		assertEquals(0.123f, vec[0], 1e-6f);
		assertEquals(-0.456f, vec[1], 1e-6f);
		assertEquals(0.789f, vec[2], 1e-6f);
	}

	public void testFieldValueConverterToFloatVector_ValidFloatArray() {
		float[] input = new float[] { 1.0f, 2.0f };
		float[] vec = FieldValueConverter.toFloatVector(input, 2);
		assertEquals(2, vec.length);
		assertEquals(1.0f, vec[0], 1e-6f);
	}

	public void testFieldValueConverterToFloatVector_DimensionMismatch() {
		List<Number> list = List.of(0.1, 0.2);
		try {
			FieldValueConverter.toFloatVector(list, 3);
			fail("Expected IllegalArgumentException for dimension mismatch");
		} catch (IllegalArgumentException e) {
			assertTrue(e.getMessage().contains("mismatch"));
		}
	}

	public void testFieldValueConverterToFloatVector_NonNumeric() {
		List<Object> list = List.of("abc", 0.2);
		try {
			FieldValueConverter.toFloatVector(list, 2);
			fail("Expected IllegalArgumentException for non-numeric element");
		} catch (IllegalArgumentException e) {
			assertTrue(e.getMessage().contains("numeric"));
		}
	}

	public void testFieldValueConverterToFloatVector_NaN() {
		List<Number> list = List.of(Float.NaN, 0.2f);
		try {
			FieldValueConverter.toFloatVector(list, 2);
			fail("Expected IllegalArgumentException for NaN");
		} catch (IllegalArgumentException e) {
			assertTrue(e.getMessage().contains("NaN"));
		}
	}

	public void testFieldValueConverterToFloatVector_Infinite() {
		List<Number> list = List.of(Float.POSITIVE_INFINITY, 0.2f);
		try {
			FieldValueConverter.toFloatVector(list, 2);
			fail("Expected IllegalArgumentException for Infinite");
		} catch (IllegalArgumentException e) {
			assertTrue(e.getMessage().contains("Infinite"));
		}
	}

	// ========================================================================
	// 6. DynamicFieldResolver does not infer VECTOR from array
	// ========================================================================

	public void testDynamicFieldResolverDoesNotInferVector() {
		nlp4j.lucene9.DynamicFieldResolver resolver = new nlp4j.lucene9.DynamicFieldResolver();
		FieldTypeDef def = resolver.resolve("unknown_field");
		assertEquals(FieldTypeDef.Kind.KEYWORD, def.kind());
	}

	// ========================================================================
	// 7, 8, 9 & 11. LocalSearch.searchVector(field, vector, k) & filter
	// ========================================================================

	public void testSearchVectorWithFieldAndFilter() throws Exception {
		// doc1: maker=AAA, vector=[1, 0]
		// doc2: maker=BBB, vector=[0.99, 0.01]
		// doc3: maker=AAA, vector=[0, 1]
		try (LocalSearch search = LocalSearch.builder("ja")
				.vectorField("vector", 2, VectorSimilarityFunction.COSINE, "multilingual-e5-large")
				.field("maker_s", FieldTypeDef.keyword().stored(true))
				.build()) {

			search.add("doc1", (String) null, new float[] { 1.0f, 0.0f }, Map.of("maker_s", "AAA"));
			search.add("doc2", (String) null, new float[] { 0.99f, 0.01f }, Map.of("maker_s", "BBB"));
			search.add("doc3", (String) null, new float[] { 0.0f, 1.0f }, Map.of("maker_s", "AAA"));
			search.commit();

			// 1. searchVector(field, vector, k) without filter
			SearchResult[] resultsAll = search.searchVector("vector", new float[] { 1.0f, 0.0f }, 10);
			assertEquals(3, resultsAll.length);
			assertEquals("doc1", resultsAll[0].id);
			assertEquals("doc2", resultsAll[1].id);
			assertEquals("doc3", resultsAll[2].id);
			assertTrue(resultsAll[0].score > 0);
			assertTrue(resultsAll[0].score >= resultsAll[1].score);

			// 2. searchVector(field, vector, k, filterLuceneQuery)
			SearchResult[] resultsFiltered = search.searchVector("vector", new float[] { 1.0f, 0.0f }, 10, "maker_s:\"AAA\"");
			assertEquals(2, resultsFiltered.length);
			assertEquals("doc1", resultsFiltered[0].id);
			assertEquals("doc3", resultsFiltered[1].id);

			// 3. limit k respected
			SearchResult[] resultsK1 = search.searchVector("vector", new float[] { 1.0f, 0.0f }, 1, "maker_s:\"AAA\"");
			assertEquals(1, resultsK1.length);
			assertEquals("doc1", resultsK1[0].id);
		}
	}

	// ========================================================================
	// 12. Schema API public access from Java / Python
	// ========================================================================

	public void testSchemaApiPublic() throws Exception {
		try (LocalSearch search = LocalSearch.builder("ja")
				.vectorField("vector", 1024, VectorSimilarityFunction.COSINE, "multilingual-e5-large")
				.build()) {

			SearchSchema schema = search.getSchema();
			assertNotNull(schema);
			assertTrue(schema.contains("vector"));

			assertEquals("multilingual-e5-large", search.getVectorModel("vector"));
			assertEquals(Integer.valueOf(1024), search.getVectorDimension("vector"));
			assertEquals("KNN_VECTOR", search.getFieldType("vector"));
			assertNotNull(search.getField("vector"));
			assertNotNull(search.getFieldInfo("vector"));
		}
	}

	// ========================================================================
	// 13. SearchSchemaStore persistence of vector metadata (dimension, similarity, model)
	// ========================================================================

	public void testSchemaPersistenceWithVectorMetadata() throws Exception {
		try (LocalSearch search = LocalSearch.builder("ja")
				.vectorField("vector", 1024, VectorSimilarityFunction.DOT_PRODUCT, "multilingual-e5-large")
				.build()) {

			search.add("1", (String) null, new float[1024]);
			search.commit();
			search.saveIndexTo(tempDir);
		}

		// Reopen from disk
		try (LocalSearch search = LocalSearch.builder("ja")
				.loadIndexFrom(tempDir)
				.build()) {

			assertEquals(1024, search.getVectorDimension());
			assertEquals("multilingual-e5-large", search.getVectorModel("vector"));
			assertEquals(Integer.valueOf(1024), search.getVectorDimension("vector"));

			FieldTypeDef def = search.getField("vector");
			assertNotNull(def);
			assertEquals(FieldTypeDef.Kind.KNN_VECTOR, def.kind());
			assertEquals(1024, def.get_dimension());
			assertEquals(VectorSimilarityFunction.DOT_PRODUCT, def.vectorSimilarityFunction());
			assertEquals("multilingual-e5-large", def.get_model());
		}
	}

	// ========================================================================
	// 14. LocalAnalytics rejection on VECTOR field
	// ========================================================================

	public void testLocalAnalyticsRejectionOnVectorField() throws Exception {
		try (LocalSearch search = LocalSearch.builder("ja")
				.vectorField("vector", 2, VectorSimilarityFunction.COSINE, "model-a")
				.field("category", FieldTypeDef.keyword().stored(true).aggregatable(true))
				.build()) {

			search.add("1", "body1", new float[] { 1.0f, 0.0f }, Map.of("category", "A"));
			search.commit();

			LocalAnalytics analytics = new LocalAnalytics(search);
			try {
				// terms aggregation on vector field should fail
				search.aggregate("vector", 10);
				fail("Expected LocalSearchException when aggregating on non-aggregatable VECTOR field");
			} catch (LocalSearchException e) {
				assertTrue(e.getMessage().contains("not aggregatable") || e.getMessage().contains("vector"));
			}

			// FieldsSummary check: VECTOR should have aggregatable = false
			FieldsSummary summary = search.getFieldsSummary();
			FieldSummary vectorSummary = summary.getFields().stream()
					.filter(f -> "vector".equals(f.getField()))
					.findFirst()
					.orElse(null);
			assertNotNull(vectorSummary);
			assertFalse(vectorSummary.isAggregatable());
			assertEquals(FieldTypeDef.Kind.KNN_VECTOR, vectorSummary.getKind());
		}
	}

	// ========================================================================
	// kaiwa0924-2134: Arbitrary vector field in JSON, hasVectorField, multiValued check
	// ========================================================================

	public void testAddJsonArbitraryVectorField() throws Exception {
		try (LocalSearch search = LocalSearch.builder("en")
				.vectorField("custom_vec", 3, VectorSimilarityFunction.COSINE, "model-custom")
				.build()) {

			assertTrue(search.hasVectorField());
			assertEquals("model-custom", search.getVectorModel("custom_vec"));
			assertEquals(Integer.valueOf(3), search.getVectorDimension("custom_vec"));

			search.addJson("{\"id\":\"1\",\"body\":\"doc1\",\"custom_vec\":[1.0, 0.0, 0.0]}");
			search.addJson("{\"id\":\"2\",\"body\":\"doc2\",\"custom_vec\":[0.0, 1.0, 0.0]}");
			search.commit();

			SearchResult[] results = search.searchVector("custom_vec", new float[] { 1.0f, 0.0f, 0.0f }, 10);
			assertEquals(2, results.length);
			assertEquals("1", results[0].id);

			// stored data json should not contain custom_vec
			SearchResult r1 = search.search("doc1", 1)[0];
			assertNotNull(r1.data);
			assertFalse(r1.data.contains("custom_vec"));
		}
	}

	public void testKnnVectorMultiValuedRejected() {
		try {
			FieldTypeDef.knnVector(128).multiValued(true);
			fail("Expected IllegalArgumentException when setting multiValued on KNN_VECTOR");
		} catch (IllegalArgumentException e) {
			assertTrue(e.getMessage().contains("KNN_VECTOR"));
		}
	}

	public void testAddJsonInvalidVectorElementThrows() throws Exception {
		try (LocalSearch search = LocalSearch.builder("en").vectorDimension(2).build()) {
			try {
				search.addJson("{\"id\":\"1\",\"body\":\"doc1\",\"vector\":[\"abc\", 0.0]}");
				fail("Expected LocalSearchException for non-numeric vector element");
			} catch (LocalSearchException e) {
				assertTrue(e.getMessage().contains("numeric"));
			}
		}
	}
}
