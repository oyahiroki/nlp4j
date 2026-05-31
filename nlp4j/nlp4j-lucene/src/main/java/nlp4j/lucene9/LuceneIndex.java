package nlp4j.lucene9;

import java.io.Closeable;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.ja.JapaneseAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
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

		@Override
		public void close() throws IOException {
			manager.release(searcher);
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
		 * Returns the IndexSearcher for this session.
		 *
		 * @return the IndexSearcher
		 */
		public IndexSearcher getSearcher() {
			return searcher;
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
	}

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	private final Directory directory;

	/**
	 * default analyzer
	 */
	private final Analyzer analyzer;

	private final IndexWriter writer;

	private final SearcherManager searcherManager;
	private int count_added = 0;
	private int count_committed = 0;

	private int count_searched = 0;

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
		{
			// text_en -> English
			fieldAnalyzers.put("text_en", new EnglishAnalyzer());
			// text_ja -> kuromoji
			fieldAnalyzers.put("text_ja", new JapaneseAnalyzer());
		}

		// ----------------------------
		// per field analyzer
		// ----------------------------

		this.analyzer = new PerFieldAnalyzerWrapper(defaultAnalyzer, fieldAnalyzers);

		IndexWriterConfig config = new IndexWriterConfig(analyzer);

		this.writer = new IndexWriter(directory, config);

		this.searcherManager = new SearcherManager(writer, null);
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
	 * add document
	 */
	public void add(Document doc) throws IOException {
		count_added++;
		String id = doc.get("id");
		if (id != null) {
			writer.updateDocument(new Term("id", id), doc);
		} else {
			writer.addDocument(doc);
		}
	}

	@Override
	public void close() throws IOException {

		searcherManager.close();

		writer.close();

		directory.close();

		analyzer.close();
	}

	public void commit() throws IOException {
		count_committed++;
		this.writer.commit();
	}

	/**
	 * easy search
	 */
	public List<Document> search(String queryString, int size) throws Exception {
		count_searched++;
		try (SearchSession session = acquireSearcher()) {
			return session.search(queryString, size);
		}
	}

	@Override
	public String toString() {
		return "LuceneIndex [count_added=" + count_added + ", count_committed=" + count_committed + ", count_searched="
				+ count_searched + "]";
	}
}