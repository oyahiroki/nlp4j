package hello.lucene;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.KnnFloatVectorField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.VectorSimilarityFunction;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.KnnFloatVectorQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;

public class LuceneVectorSearchExample {
	private static final String FIELD_ID = "id";
	private static final String FIELD_TITLE = "title";
	private static final String FIELD_TEXT = "text";
	private static final String FIELD_VECTOR = "vector";

	public static void main(String[] args) throws Exception {

		Analyzer analyzer = new StandardAnalyzer();

		try (Directory directory = new ByteBuffersDirectory()) {

			// ----------------------------
			// 1. Index 作成
			// ----------------------------
			IndexWriterConfig config = new IndexWriterConfig(analyzer);

			try (IndexWriter writer = new IndexWriter(directory, config)) {

				addDocument(writer, "1", "Java and Lucene", "Lucene is a search library written in Java.",
						new float[] { 0.90f, 0.10f, 0.10f });

				addDocument(writer, "2", "Python and NLP", "Python is often used for natural language processing.",
						new float[] { 0.10f, 0.90f, 0.10f });

				addDocument(writer, "3", "Lucene Vector Search",
						"Lucene supports nearest neighbor search using vectors.", new float[] { 0.80f, 0.20f, 0.10f });

				writer.commit();
			}

			// ----------------------------
			// 2. Searcher 作成
			// ----------------------------
			try (DirectoryReader reader = DirectoryReader.open(directory)) {

				IndexSearcher searcher = new IndexSearcher(reader);

				// ----------------------------
				// 3. ベクトル検索
				// ----------------------------
				float[] queryVector = new float[] { 0.85f, 0.15f, 0.10f };
				int k = 2;

				Query vectorQuery = KnnFloatVectorField.newVectorQuery(FIELD_VECTOR, queryVector, k);

				TopDocs topDocs = searcher.search(vectorQuery, k);

				System.out.println("=== Vector Search ===");
				printResults(searcher, topDocs);

				// ----------------------------
				// 4. キーワード条件で filter したベクトル検索
				// ----------------------------
				QueryParser parser = new QueryParser(FIELD_TEXT, analyzer);

				// Lucene Query Parser 形式
				Query filterQuery = parser.parse("Lucene OR Java");

				Query filteredVectorQuery = new KnnFloatVectorQuery(FIELD_VECTOR, queryVector, k, filterQuery);

				TopDocs filteredTopDocs = searcher.search(filteredVectorQuery, k);

				System.out.println();
				System.out.println("=== Filtered Vector Search ===");
				printResults(searcher, filteredTopDocs);
			}
		}
	}

	private static void addDocument(IndexWriter writer, String id, String title, String text, float[] vector)
			throws IOException {

		Document doc = new Document();

		// 検索・更新用のID
		doc.add(new StringField(FIELD_ID, id, Field.Store.YES));

		// 結果表示用
		doc.add(new StoredField(FIELD_TITLE, title));

		// キーワード検索用
		doc.add(new TextField(FIELD_TEXT, text, Field.Store.YES));

		// ベクトル検索用
		// COSINE を使う例
		doc.add(new KnnFloatVectorField(FIELD_VECTOR, vector, VectorSimilarityFunction.COSINE));

		writer.addDocument(doc);
	}

	private static void printResults(IndexSearcher searcher, TopDocs topDocs) throws IOException {

		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			Document doc = searcher.doc(scoreDoc.doc);

			System.out.println("score=" + scoreDoc.score + ", id=" + doc.get(FIELD_ID) + ", title="
					+ doc.get(FIELD_TITLE) + ", text=" + doc.get(FIELD_TEXT));
		}
	}
}
