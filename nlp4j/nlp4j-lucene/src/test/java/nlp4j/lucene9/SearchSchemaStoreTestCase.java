/*
 * Copyright (C) 2026 Hiroki OYA
 *
 * Licensed under the Apache License, Version 2.0
 */
package nlp4j.lucene9;

import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.lucene.index.VectorSimilarityFunction;

import junit.framework.TestCase;

/**
 * JUnit3 tests for {@link SearchSchemaStore} round-trip serialisation.
 */
public class SearchSchemaStoreTestCase extends TestCase {

    // ------------------------------------------------------------------
    // Helpers
    // ------------------------------------------------------------------

    private Path tempDir;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        tempDir = Files.createTempDirectory("nlp4j-schema-test-");
    }

    // ------------------------------------------------------------------
    // Tests
    // ------------------------------------------------------------------

    /**
     * KEYWORD field round-trip: stored=true, aggregatable=true.
     */
    public void testSaveAndLoadKeywordField() throws Exception {
        SearchSchema original = new SearchSchema();
        original.add("id", FieldTypeDef.keyword().stored(true));
        original.add("category", FieldTypeDef.keyword().stored(true).aggregatable(true));

        SearchSchemaStore.save(tempDir, original);
        assertTrue("Schema file should exist", SearchSchemaStore.exists(tempDir));

        SearchSchema loaded = SearchSchemaStore.load(tempDir);

        assertEquals(original.asMap().size(), loaded.asMap().size());

        FieldTypeDef idDef = loaded.get("id");
        assertEquals(FieldTypeDef.Kind.KEYWORD, idDef.kind());
        assertTrue(idDef.is_stored());
        assertFalse(idDef.is_aggregatable());

        FieldTypeDef catDef = loaded.get("category");
        assertEquals(FieldTypeDef.Kind.KEYWORD, catDef.kind());
        assertTrue(catDef.is_stored());
        assertTrue(catDef.is_aggregatable());
    }

    /**
     * Numeric fields (INTEGER / LONG / DOUBLE) round-trip.
     */
    public void testSaveAndLoadNumericField() throws Exception {
        SearchSchema original = new SearchSchema();
        original.add("year_i",  FieldTypeDef.integer().stored(true).aggregatable(true).range(true));
        original.add("count_l", FieldTypeDef.longNumber().stored(true).range(true));
        original.add("score_d", FieldTypeDef.doubleNumber().stored(true));

        SearchSchemaStore.save(tempDir, original);
        SearchSchema loaded = SearchSchemaStore.load(tempDir);

        FieldTypeDef year = loaded.get("year_i");
        assertEquals(FieldTypeDef.Kind.INTEGER, year.kind());
        assertTrue(year.is_stored());
        assertTrue(year.is_aggregatable());
        assertTrue(year.is_range());

        FieldTypeDef count = loaded.get("count_l");
        assertEquals(FieldTypeDef.Kind.LONG, count.kind());
        assertTrue(count.is_range());

        FieldTypeDef score = loaded.get("score_d");
        assertEquals(FieldTypeDef.Kind.DOUBLE, score.kind());
        assertTrue(score.is_stored());
    }

    /**
     * DATE field round-trip.
     */
    public void testSaveAndLoadDateField() throws Exception {
        SearchSchema original = new SearchSchema();
        original.add("published_at", FieldTypeDef.date().stored(true).range(true));

        SearchSchemaStore.save(tempDir, original);
        SearchSchema loaded = SearchSchemaStore.load(tempDir);

        FieldTypeDef def = loaded.get("published_at");
        assertEquals(FieldTypeDef.Kind.DATE, def.kind());
        assertTrue(def.is_stored());
        assertTrue(def.is_range());
    }

    /**
     * KNN_VECTOR field round-trip including dimension and similarity function.
     */
    public void testSaveAndLoadVectorField() throws Exception {
        SearchSchema original = new SearchSchema();
        original.add("vector",
                FieldTypeDef.knnVector(1024).similarity(VectorSimilarityFunction.DOT_PRODUCT));

        SearchSchemaStore.save(tempDir, original);
        SearchSchema loaded = SearchSchemaStore.load(tempDir);

        FieldTypeDef def = loaded.get("vector");
        assertEquals(FieldTypeDef.Kind.KNN_VECTOR, def.kind());
        assertEquals(1024, def.get_dimension());
        assertEquals(VectorSimilarityFunction.DOT_PRODUCT, def.vectorSimilarityFunction());
    }

    /**
     * Default similarity (COSINE) for KNN_VECTOR is preserved.
     */
    public void testSaveAndLoadVectorFieldCosineSimilarity() throws Exception {
        SearchSchema original = new SearchSchema();
        original.add("vector", FieldTypeDef.knnVector(512));
        // no explicit similarity → defaults to COSINE

        SearchSchemaStore.save(tempDir, original);
        SearchSchema loaded = SearchSchemaStore.load(tempDir);

        FieldTypeDef def = loaded.get("vector");
        assertEquals(512, def.get_dimension());
        assertEquals(VectorSimilarityFunction.COSINE, def.vectorSimilarityFunction());
    }

    /**
     * Field insertion order is preserved after round-trip (LinkedHashMap guarantee).
     */
    public void testPreserveFieldOrder() throws Exception {
        SearchSchema original = new SearchSchema();
        original.add("id",       FieldTypeDef.keyword().stored(true));
        original.add("title",    FieldTypeDef.text().stored(true));
        original.add("year_i",   FieldTypeDef.integer().stored(true));
        original.add("category", FieldTypeDef.keyword().stored(true).aggregatable(true));

        SearchSchemaStore.save(tempDir, original);
        SearchSchema loaded = SearchSchemaStore.load(tempDir);

        String[] expected = {"id", "title", "year_i", "category"};
        String[] actual = loaded.fieldNames().toArray(new String[0]);

        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals("Field order mismatch at index " + i, expected[i], actual[i]);
        }
    }

    /**
     * Loading a JSON with an unsupported formatVersion throws IllegalArgumentException.
     */
    public void testLoadUnknownFormatVersion() throws Exception {
        String badJson = "{\"formatVersion\":999,\"fields\":[]}";
        Path schemaFile = tempDir.resolve(SearchSchemaStore.FILE_NAME);
        java.nio.file.Files.writeString(schemaFile, badJson, java.nio.charset.StandardCharsets.UTF_8);

        try {
            SearchSchemaStore.load(tempDir);
            fail("Expected IllegalArgumentException for unknown format version");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("999"));
        }
    }

    /**
     * exists() returns false when no schema file is present.
     */
    public void testExistsReturnsFalseWhenMissing() throws Exception {
        assertFalse(SearchSchemaStore.exists(tempDir));
    }

    /**
     * exists() returns true after save().
     */
    public void testExistsReturnsTrueAfterSave() throws Exception {
        SearchSchema schema = new SearchSchema();
        schema.add("id", FieldTypeDef.keyword().stored(true));
        SearchSchemaStore.save(tempDir, schema);
        assertTrue(SearchSchemaStore.exists(tempDir));
    }

    /**
     * STORED_ONLY field round-trip.
     */
    public void testSaveAndLoadStoredOnlyField() throws Exception {
        SearchSchema original = new SearchSchema();
        original.add("data", FieldTypeDef.storedOnly());

        SearchSchemaStore.save(tempDir, original);
        SearchSchema loaded = SearchSchemaStore.load(tempDir);

        FieldTypeDef def = loaded.get("data");
        assertEquals(FieldTypeDef.Kind.STORED_ONLY, def.kind());
        assertTrue(def.is_stored());
    }

    /**
     * MultiValued KEYWORD field round-trip.
     */
    public void testSaveAndLoadMultiValuedKeywordField() throws Exception {
        SearchSchema original = new SearchSchema();
        original.add("tags", FieldTypeDef.keyword().stored(true).aggregatable(true).multiValued(true));

        SearchSchemaStore.save(tempDir, original);
        SearchSchema loaded = SearchSchemaStore.load(tempDir);

        FieldTypeDef def = loaded.get("tags");
        assertEquals(FieldTypeDef.Kind.KEYWORD, def.kind());
        assertTrue(def.is_multiValued());
        assertTrue(def.is_aggregatable());
    }

    /**
     * equals() / hashCode() consistency for round-tripped FieldTypeDef.
     */
    public void testFieldTypeDefEqualsAfterRoundTrip() throws Exception {
        FieldTypeDef original = FieldTypeDef.knnVector(256).similarity(VectorSimilarityFunction.EUCLIDEAN);
        SearchSchema schema = new SearchSchema();
        schema.add("vec", original);

        SearchSchemaStore.save(tempDir, schema);
        SearchSchema loaded = SearchSchemaStore.load(tempDir);

        FieldTypeDef restored = loaded.get("vec");
        assertEquals(original, restored);
        assertEquals(original.hashCode(), restored.hashCode());
    }
}
