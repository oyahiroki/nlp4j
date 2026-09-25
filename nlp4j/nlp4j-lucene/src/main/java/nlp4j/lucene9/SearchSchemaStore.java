/*
 * Copyright (C) 2026 Hiroki OYA
 *
 * Licensed under the Apache License, Version 2.0
 */
package nlp4j.lucene9;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.lucene.index.VectorSimilarityFunction;

import nlp4j.json.JsonNode;

/**
 * Persists and restores a {@link SearchSchema} as a JSON file alongside a Lucene index.
 *
 * <pre>
 * SearchSchemaStore.save(dir, schema);   // writes nlp4j-schema.json
 * SearchSchema schema = SearchSchemaStore.load(dir); // reads it back
 * </pre>
 */
public final class SearchSchemaStore {

    public static final String FILE_NAME = "nlp4j-schema.json";
    public static final int FORMAT_VERSION = 1;

    private SearchSchemaStore() {
    }

    /**
     * Returns {@code true} if {@code nlp4j-schema.json} exists in the given directory.
     */
    public static boolean exists(Path indexDir) {
        return Files.exists(indexDir.resolve(FILE_NAME));
    }

    /**
     * Serialises {@code schema} to {@code indexDir/nlp4j-schema.json}.
     *
     * @param indexDir target directory (must already exist)
     * @param schema   schema to save
     * @throws IOException on I/O error
     */
    public static void save(Path indexDir, SearchSchema schema) throws IOException {
        JsonNode root = JsonNode.object();
        root.put("formatVersion", FORMAT_VERSION);

        JsonNode fields = JsonNode.array();

        for (java.util.Map.Entry<String, FieldTypeDef> e : schema.asMap().entrySet()) {
            String name = e.getKey();
            FieldTypeDef def = e.getValue();

            JsonNode field = JsonNode.object();
            field.put("name", name);
            field.put("kind", def.kind().name());
            field.put("stored", def.is_stored());
            field.put("aggregatable", def.is_aggregatable());
            field.put("sortable", def.is_sortable());
            field.put("range", def.is_range());
            field.put("multiValued", def.is_multiValued());

            if (def.kind() == FieldTypeDef.Kind.KNN_VECTOR) {
                field.put("dimension", def.get_dimension());
                field.put("vectorSimilarityFunction", def.vectorSimilarityFunction().name());
                if (def.get_model() != null) {
                    field.put("model", def.get_model());
                }
            }

            fields.add(field);
        }

        root.put("fields", fields);

        Path target = indexDir.resolve(FILE_NAME);
        Files.writeString(target, root.toJson(), StandardCharsets.UTF_8);
    }

    /**
     * Deserialises {@code indexDir/nlp4j-schema.json} back to a {@link SearchSchema}.
     *
     * @param indexDir source directory
     * @return restored schema
     * @throws IOException                   on I/O error
     * @throws IllegalArgumentException      if the format version is unsupported
     */
    public static SearchSchema load(Path indexDir) throws IOException {
        Path source = indexDir.resolve(FILE_NAME);
        String json = Files.readString(source, StandardCharsets.UTF_8);
        JsonNode root = JsonNode.parse(json);

        int version = root.get("formatVersion").asInt(0);
        if (version != FORMAT_VERSION) {
            throw new IllegalArgumentException(
                    "Unsupported nlp4j-schema.json formatVersion: " + version);
        }

        SearchSchema schema = new SearchSchema();

        JsonNode fields = root.get("fields");
        for (JsonNode field : fields.asList()) {
            String name      = field.get("name").asString();
            String kindStr   = field.get("kind").asString();
            boolean stored   = field.get("stored").asBoolean(false);
            boolean aggregatable = field.get("aggregatable").asBoolean(false);
            boolean sortable = field.get("sortable").asBoolean(false);
            boolean range    = field.get("range").asBoolean(false);
            boolean multiValued = field.get("multiValued").asBoolean(false);

            FieldTypeDef.Kind kind = FieldTypeDef.Kind.valueOf(kindStr);

            FieldTypeDef def;
            switch (kind) {
                case KEYWORD:
                    def = FieldTypeDef.keyword();
                    break;
                case TEXT:
                    def = FieldTypeDef.text();
                    break;
                case INTEGER:
                    def = FieldTypeDef.integer();
                    break;
                case LONG:
                    def = FieldTypeDef.longNumber();
                    break;
                case DOUBLE:
                    def = FieldTypeDef.doubleNumber();
                    break;
                case DATE:
                    def = FieldTypeDef.date();
                    break;
                case KNN_VECTOR: {
                    int dimension = field.get("dimension") != null ? field.get("dimension").asInt(0) : 0;
                    def = FieldTypeDef.knnVector(dimension);
                    String simStr = field.get("vectorSimilarityFunction") != null ? field.get("vectorSimilarityFunction").asString(null) : null;
                    if (simStr != null) {
                        def.similarity(VectorSimilarityFunction.valueOf(simStr));
                    }
                    String modelStr = field.get("model") != null ? field.get("model").asString(null) : null;
                    if (modelStr != null) {
                        def.model(modelStr);
                    }
                    break;
                }
                case STORED_ONLY:
                    def = FieldTypeDef.storedOnly();
                    break;
                default:
                    throw new IllegalArgumentException("Unknown field kind: " + kind);
            }

            def.stored(stored).aggregatable(aggregatable).sortable(sortable)
               .range(range).multiValued(multiValued);

            schema.add(name, def);
        }

        return schema;
    }
}
