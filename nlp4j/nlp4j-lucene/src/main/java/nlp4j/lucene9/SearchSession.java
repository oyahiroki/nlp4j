/*
 * Copyright (C) 2026 Hiroki OYA
 *
 * Licensed under the Apache License, Version 2.0
 */
package nlp4j.lucene9;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.search.TopDocs;

/**
 */
public class SearchSession implements Closeable {

	private final IndexSearcher searcher;

	private final SearcherManager manager;

	private final Analyzer analyzer;

	/**
	 * public を付けていません。
	 * 
	 * 理由は、SearchSession は本来 LuceneIndex#acquireSearcher() から取得すべきもので、利用者が直接 new
	 * SearchSession(...) するクラスではないためです。
	 * 
	 * @param searcher
	 * @param manager
	 * @param analyzer
	 */
	SearchSession(IndexSearcher searcher, SearcherManager manager, Analyzer analyzer) {
		this.searcher = searcher;
		this.manager = manager;
		this.analyzer = analyzer;
	}

	@Override
	public void close() throws IOException {
		manager.release(searcher);
	}

	public Analyzer getAnalyzer() {
		return analyzer;
	}

	public IndexSearcher getSearcher() {
		return searcher;
	}

	public List<Document> search(String queryString, int size) throws Exception {

		Query query;

		if ("*:*".equals(queryString)) {
			query = new MatchAllDocsQuery();
		} else {
			QueryParser parser = new QueryParser("text_ja", analyzer);
			query = parser.parse(queryString);
		}

		TopDocs topDocs = searcher.search(query, size);

		List<Document> docs = new ArrayList<>();

		for (ScoreDoc sd : topDocs.scoreDocs) {
			Document doc = searcher.doc(sd.doc);
			docs.add(doc);
		}

		return docs;
	}
}