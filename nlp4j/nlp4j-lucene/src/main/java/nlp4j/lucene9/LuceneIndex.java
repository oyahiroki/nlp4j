package nlp4j.lucene9;

import java.io.Closeable;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.ja.JapaneseAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;

public class LuceneIndex implements Closeable {

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
	
	private final Directory directory;

	/**
	 * default analyzer
	 */
	private final Analyzer analyzer;

	private final IndexWriter writer;

	private final SearcherManager searcherManager;

	/**
	 * constructor
	 */
	public LuceneIndex() throws IOException {

		this.directory = new ByteBuffersDirectory();

		// ----------------------------
		// default analyzer
		// ----------------------------

		StandardAnalyzer defaultAnalyzer = new StandardAnalyzer();

		// ----------------------------
		// field analyzers
		// ----------------------------

		Map<String, Analyzer> fieldAnalyzers = new HashMap<>();

		// text_ja -> kuromoji
		fieldAnalyzers.put("text_ja", new JapaneseAnalyzer());

		// ----------------------------
		// per field analyzer
		// ----------------------------

		this.analyzer = new PerFieldAnalyzerWrapper(defaultAnalyzer, fieldAnalyzers);

		IndexWriterConfig config = new IndexWriterConfig(analyzer);

		this.writer = new IndexWriter(directory, config);

		this.searcherManager = new SearcherManager(writer, null);
	}

	/**
	 * add document
	 */
	public void add(Document doc) throws IOException {
		writer.addDocument(doc);
	}

	/**
	 * acquire searcher
	 */
	public SearchSession acquireSearcher() throws IOException {

		searcherManager.maybeRefresh();

		IndexSearcher searcher = searcherManager.acquire();

		return new SearchSession(searcher, searcherManager, analyzer);
	}

	/**
	 * easy search
	 */
	public List<Document> search(String queryString, int size) throws Exception {

		try (SearchSession session = acquireSearcher()) {

			return session.search(queryString, size);
		}
	}

	@Override
	public void close() throws IOException {

		searcherManager.close();

		writer.close();

		directory.close();

		analyzer.close();
	}

	/**
	 * Search Session
	 */
	public static class SearchSession implements Closeable {

		private final IndexSearcher searcher;

		private final SearcherManager manager;

		private final Analyzer analyzer;

		public SearchSession(IndexSearcher searcher, SearcherManager manager, Analyzer analyzer) {

			this.searcher = searcher;
			this.manager = manager;
			this.analyzer = analyzer;
		}

		/**
		 * Returns the IndexSearcher for this session.
		 *
		 * @return the IndexSearcher
		 */
		public IndexSearcher getSearcher() {
			return searcher;
		}

		/**
		 * Returns the Analyzer for this session.
		 *
		 * @return the Analyzer
		 */
		public Analyzer getAnalyzer() {
			return analyzer;
		}

		/**
		 * search
		 */
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

		@Override
		public void close() throws IOException {
			manager.release(searcher);
		}
	}
}