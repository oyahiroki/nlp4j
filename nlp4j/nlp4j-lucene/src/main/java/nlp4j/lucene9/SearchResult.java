/*
 * Copyright (C) 2026 Hiroki OYA
 *
 * Licensed under the Apache License, Version 2.0
 */
package nlp4j.lucene9;

import java.util.List;

/**
 * Represents the result of a search operation.
 * Contains the total number of matching documents and a list of search hits.
 */
public class SearchResult {
	private final long total;
    private final List<SearchHit> hits;

    /**
     * Constructs a new SearchResult.
     *
     * @param total the total number of matching documents
     * @param hits the list of search hits (paginated)
     */
    public SearchResult(long total, List<SearchHit> hits) {
        this.total = total;
        this.hits = hits;
    }

    /**
     * Returns the total number of matching documents.
     *
     * @return the total count
     */
    public long total() {
        return total;
    }

    /**
     * Returns the list of search hits.
     *
     * @return the list of SearchHit objects
     */
    public List<SearchHit> hits() {
        return hits;
    }
}
