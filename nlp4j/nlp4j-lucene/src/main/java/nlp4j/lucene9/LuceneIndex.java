package nlp4j.lucene9;

import java.io.Closeable;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;
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
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.IOContext;

public class LuceneIndex implements Closeable {

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	private boolean closed = false;

	private static Analyzer createAnalyzer() {

		StandardAnalyzer defaultAnalyzer = new StandardAnalyzer();

		Map<String, Analyzer> fieldAnalyzers = new HashMap<>();
		{
			fieldAnalyzers.put("text_en", new EnglishAnalyzer());
			fieldAnalyzers.put("text_ja", new JapaneseAnalyzer());
		}

		return new PerFieldAnalyzerWrapper(defaultAnalyzer, fieldAnalyzers);
	}

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

		// Index設定
		{
			this.directory = new ByteBuffersDirectory();
		}

		this.analyzer = createAnalyzer();

		IndexWriterConfig config = new IndexWriterConfig(analyzer);

		this.writer = new IndexWriter(directory, config);

		this.searcherManager = new SearcherManager(writer, null);
	}

	/**
	 * constructor
	 *
	 * Creates an in-memory Lucene index from an existing filesystem index.
	 *
	 * @param inputDir existing Lucene index directory
	 * @throws IOException if an I/O error occurs
	 */
	public LuceneIndex(Path inputDir) throws IOException {

		// Index設定
		{
			if (Files.notExists(inputDir)) {
				throw new IOException("Input directory does not exist: " + inputDir);
			}
			this.directory = new ByteBuffersDirectory();
			try (Directory inputDirectory = FSDirectory.open(inputDir)) {
				for (String fileName : inputDirectory.listAll()) {
					this.directory.copyFrom(inputDirectory, fileName, fileName, IOContext.DEFAULT);
				}
			}
		}

		this.analyzer = createAnalyzer();

		IndexWriterConfig config = new IndexWriterConfig(analyzer);

		// 既存インデックスを使うことを明示
		config.setOpenMode(OpenMode.CREATE_OR_APPEND);

		this.writer = new IndexWriter(directory, config);

		this.searcherManager = new SearcherManager(writer, null);
	}

	/**
	 * acquire searcher
	 */
	public SearchSession acquireSearcher() throws IOException {
		ensureOpen();
		searcherManager.maybeRefresh();

		IndexSearcher searcher = searcherManager.acquire();

		return new SearchSession(searcher, searcherManager, analyzer);
	}

	/**
	 * add document
	 */
	public void add(Document doc) throws IOException {
		ensureOpen();
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

		if (searcherManager != null) {
			searcherManager.close();
		}
		if (writer != null) {
			writer.close();
		}

		if (directory != null) {
			directory.close();
		}
		if (analyzer != null) {
			analyzer.close();
		}
	}

	public void commit() throws IOException {
		ensureOpen();
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

	/**
	 * Writes this in-memory Lucene index to a filesystem directory.
	 *
	 * @param outputDir output directory
	 * @throws IOException if an I/O error occurs
	 * 
	 * @deprecated Use writeToAndClose(Path) instead.
	 */
	public void writeTo(Path outputDir) throws IOException {

		// 未コミットの変更を保存対象に含める
		commit();

		Files.createDirectories(outputDir);

		try (Directory outputDirectory = FSDirectory.open(outputDir)) {

			// copyFrom はコピー先ファイルが既に存在すると失敗するため、
			// 既存ファイルがある場合は明示的にエラーにする。
			String[] existingFiles = outputDirectory.listAll();
			if (existingFiles.length > 0) {
				throw new IOException("Output directory is not empty: " + outputDir);
			}

			for (String fileName : directory.listAll()) {
				outputDirectory.copyFrom(directory, fileName, fileName, IOContext.DEFAULT);
			}
		}
	}

	private void ensureOpen() throws IOException {
		if (closed) {
			throw new IOException("LuceneIndex is already closed.");
		}
	}

	/**
	 * Writes this in-memory Lucene index to a filesystem directory, then closes
	 * this LuceneIndex.
	 *
	 * After calling this method, this LuceneIndex instance must not be used.
	 *
	 * @param outputDir output directory
	 * @throws IOException if an I/O error occurs
	 */
	public synchronized void writeToAndClose(Path outputDir) throws IOException {

		if (closed) {
			throw new IOException("LuceneIndex is already closed.");
		}

		Files.createDirectories(outputDir);

		IOException thrown = null;

		try {
			// SearcherManager は writer を参照しているため先に閉じる
			searcherManager.close();

			// 保存用なので、時間がかかってもよい前提なら実行してよい
			// doWait=true なので merge 完了まで待つ
			writer.forceMerge(1, true);

			// commitOnClose=true の場合:
			// 変更を書き出し、実行中 merge を待ち、commit して close
			writer.close();

			try (Directory outputDirectory = FSDirectory.open(outputDir)) {

				String[] existingFiles = outputDirectory.listAll();
				if (existingFiles.length > 0) {
					throw new IOException("Output directory is not empty: " + outputDir);
				}

				for (String fileName : directory.listAll()) {

					// write.lock はコピー不要
					if (IndexWriter.WRITE_LOCK_NAME.equals(fileName)) {
						continue;
					}

					outputDirectory.copyFrom(directory, fileName, fileName, IOContext.DEFAULT);
				}
			}

		} catch (IOException e) {
			thrown = e;
			throw e;

		} finally {
			try {
				directory.close();
			} catch (IOException e) {
				if (thrown != null) {
					thrown.addSuppressed(e);
				} else {
					thrown = e;
				}
			}

			analyzer.close();

			closed = true;
		}
	}

	@Override
	public String toString() {
		return "LuceneIndex [count_added=" + count_added + ", count_committed=" + count_committed + ", count_searched="
				+ count_searched + "]";
	}
}