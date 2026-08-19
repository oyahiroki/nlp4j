/*
 * Copyright (C) 2026 Hiroki OYA
 *
 * Licensed under the Apache License, Version 2.0
 */
package nlp4j.lucene9;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.StoredFields;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Executor for performing search operations against a Lucene index. Handles
 * pagination and retrieval of matching documents.
 */
public class SearchExecutor {

	/**
	 * Executes a search query and returns paginated results.
	 *
	 * @param searcher the Lucene IndexSearcher to use
	 * @param query    the Lucene Query to execute
	 * @param request  the search request containing pagination parameters
	 * @return a SearchResult containing the total hits and paginated documents
	 * @throws IOException if an I/O error occurs during search
	 */
	public static SearchResult execute(IndexSearcher searcher, Query query, SearchRequest request) throws IOException {
		int from = request.from();
		int size = request.size();

		if (size <= 0) {
			long totalHits = searcher.count(query);
			return new SearchResult(totalHits, List.of());
		}

		int limit = from + size;

		TopDocs topDocs = searcher.search(query, limit);
		StoredFields storedFields = searcher.storedFields();

		List<SearchHit> hits = new ArrayList<>();

		ScoreDoc[] scoreDocs = topDocs.scoreDocs;

		for (int i = request.from(); i < scoreDocs.length; i++) {
			ScoreDoc scoreDoc = scoreDocs[i];

			Document document = storedFields.document(scoreDoc.doc);

			hits.add(new SearchHit(String.valueOf(scoreDoc.doc), scoreDoc.score, document));
		}

		return new SearchResult(topDocs.totalHits.value, hits);
	}
}
