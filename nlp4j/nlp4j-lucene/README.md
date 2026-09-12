# nlp4j-lucene

`nlp4j-lucene` provides an intuitive, easy-to-use search and analytics API built on top of Apache Lucene. It enables you to integrate full-text search, field filtering, vector search, morphological analysis, and real-time aggregations into Java applications with minimal setup.

---

## Features

- **Simple & Expressive API**: Create indices, index documents, and query in just a few lines of code.
- **Multilingual Full-Text Search**: Out-of-the-box support for Japanese (Kuromoji analyzer) and English (English analyzer), with automatic multi-field default search across `text_ja`, `text_en`, `text`, and `body`.
- **JSON & Structured Document Indexing**: Index documents directly from JSON strings, JSON objects (`JsonObject`), or key-value maps without manual schema mapping.
- **Dynamic & Strongly-Typed Schema**: Supports text, keyword, integer, long, double, and date fields with automatic type inference and range queries.
- **Vector / Semantic Search**: Built-in vector search and hybrid search (text + vector with filters) powered by Lucene's HNSW vector index.
- **Integrated Morphological Analysis**: Automatic part-of-speech extraction and indexing (`word`, `word.noun`, `word.verb`, `word.propn`, etc.) powered by Kuromoji or custom NLP annotators.
- **Real-Time Aggregations & Analytics**: Elasticsearch-like facet and term aggregations, relative rate calculations, and JSON-based request/response APIs.
- **In-Memory & Persistent Storage**: Work seamlessly with in-memory indexes (ideal for unit testing and fast processing) or disk-based directories.

---

## Getting Started

### 1. Basic Full-Text Search

```java
import nlp4j.SearchRecord;
import nlp4j.SearchResult;
import nlp4j.lucene.LocalSearch;

// Create an in-memory index for Japanese
try (LocalSearch search = LocalSearch.builder()
        .language("ja")
        .build()) {

    // Index documents
    search.add("1", "東京都は日本の首都です。");
    search.add("2", "京都は日本の歴史的な古都です。");
    search.add("3", "大阪は西日本の主要都市です。");
    search.commit();

    // Full-text search
    List<SearchResult> results = search.search("日本");
    for (SearchResult result : results) {
        System.out.printf("ID: %s, Score: %.4f, Body: %s%n",
                result.getId(), result.getScore(), result.getBody());
    }
}
```

---

### 2. English Search with Field Filtering

```java
try (LocalSearch search = LocalSearch.builder()
        .language("en")
        .build()) {

    // Index documents with fields
    search.add("1", "Kyoto is a historic city.", Map.of("category", "city", "country", "Japan"));
    search.add("2", "Nintendo is headquartered in Kyoto.", Map.of("category", "company", "country", "Japan"));
    search.commit();

    // Query with term filters
    List<SearchResult> results = search.search("Kyoto", Map.of("category", "company"));
    // Returns document 2
}
```

---

### 3. JSON Indexing & Search DSL

Index arbitrary JSON documents with automatic field type mapping:

```java
try (LocalSearch search = LocalSearch.builder().build()) {
    // Index raw JSON string
    search.addJson("""
        {
          "id": "item1",
          "body": "Apache Lucene is a high-performance text search engine library.",
          "category": "software",
          "tags": ["java", "search", "open-source"],
          "stars_i": 5
        }
        """);
    search.commit();

    // Search using Elasticsearch-style JSON DSL
    String jsonQuery = """
        {
          "query": {
            "term": { "category": "software" }
          }
        }
        """;
    String responseJson = search.searchJson(jsonQuery);
    System.out.println(responseJson);
}
```

---

### 4. Vector / Semantic Search

Combine dense vector embeddings with metadata filters:

```java
try (LocalSearch search = LocalSearch.builder()
        .vectorDimension(3)
        .build()) {

    search.add("1", new float[]{1.0f, 0.0f, 0.0f});
    search.add("2", new float[]{0.0f, 1.0f, 0.0f}, Map.of("category", "tech"));
    search.commit();

    // K-Nearest Neighbor (k-NN) search
    float[] targetVector = new float[]{0.9f, 0.1f, 0.0f};
    List<SearchResult> results = search.search(targetVector, 10);

    // Vector search with filter
    List<SearchResult> filteredResults = search.search(targetVector, 10, Map.of("category", "tech"));
}
```

---

### 5. Facet & Keyword Aggregations

Perform instant bucket aggregations:

```java
try (LocalSearch search = LocalSearch.builder().language("ja").build()) {
    search.add("1", "京都観光と寺院巡り", Map.of("category", "観光"));
    search.add("2", "京都の最新IT技術開発", Map.of("category", "技術"));
    search.add("3", "東京のITスタートアップ", Map.of("category", "技術"));
    search.commit();

    // Aggregate category field counts
    Map<String, Integer> counts = search.aggregate("category", 10);
    // counts -> {"技術": 2, "観光": 1}

    // Aggregate extracted nouns (via morphological analysis)
    Map<String, Integer> nouns = search.aggregate("word.noun", 10);
}
```

---

### 6. Lucene Query Syntax Support

Execute rich Lucene queries with AND/OR, phrase matches, range queries, and wildcards:

```java
try (LocalSearch search = LocalSearch.builder().language("en").build()) {
    search.add("1", "Kyoto is a historic city in Japan.");
    search.add("2", "Tokyo and Kyoto are major cities in Japan.");
    search.commit();

    // Lucene query string
    List<SearchResult> results = search.searchLucene("Kyoto AND Japan NOT Tokyo");
    // Returns document 1
}
```

---

## Storage & Configuration Options

[`LocalSearch.builder()`](src/main/java/nlp4j/lucene/LocalSearch.java:1) provides flexible options:

```java
LocalSearch search = LocalSearch.builder()
    .language("ja")                     // Analyzer language: "ja", "en", etc.
    .directory(new File("./my-index"))   // Disk-based persistence (omit for in-memory)
    .vectorDimension(128)               // Enable dense vector search
    .schema(schema)                     // Custom field definitions
    .nlp(true)                          // Enable morphological analysis annotator
    .build();
```

---

## Requirements

- **Java**: 17 or higher
- **Apache Lucene**: 9.x
