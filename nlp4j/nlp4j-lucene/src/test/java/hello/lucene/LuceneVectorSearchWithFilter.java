package hello.lucene;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.KnnFloatVectorField;
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

public class LuceneVectorSearchWithFilter {
	private static final String FIELD_ID = "id";
	private static final String FIELD_TEXT = "text";
	private static final String FIELD_CATEGORY = "category";
	private static final String FIELD_VECTOR = "vector";

	public static void main(String[] args) throws Exception {

		Analyzer analyzer = new StandardAnalyzer();

		try (Directory directory = new ByteBuffersDirectory()) {

			// =========================================================
			// 1. Index 作成
			// =========================================================
			try (IndexWriter writer = new IndexWriter(directory, new IndexWriterConfig(analyzer))) {

				addDocument(writer, "1", "Lucene is a search library written in Java.", "search",
						new float[] { 0.90f, 0.10f, 0.10f });

				addDocument(writer, "2", "Python is often used for natural language processing.", "nlp",
						new float[] { 0.10f, 0.90f, 0.10f });

				addDocument(writer, "3", "Lucene supports vector search and keyword search.", "search",
						new float[] { 0.85f, 0.20f, 0.10f });

				addDocument(writer, "4", "Neural embeddings are useful for semantic search.", "embedding",
						new float[] { 0.30f, 0.80f, 0.10f });

				writer.commit();
			}

			// =========================================================
			// 2. Searcher 作成
			// =========================================================
			try (DirectoryReader reader = DirectoryReader.open(directory)) {

				IndexSearcher searcher = new IndexSearcher(reader);

				// 検索したいベクトル
				float[] queryVector = new float[] { 0.88f, 0.15f, 0.10f };

				// 近傍件数
				int k = 3;

				// =====================================================
				// 3. Lucene Query 形式の filter を作成
				// =====================================================
				QueryParser parser = new QueryParser(FIELD_TEXT, analyzer);

				// Lucene Query Parser 形式
				// text フィールドに Lucene または search が含まれる
				// かつ category が search
				String luceneQueryString = "(Lucene OR search) AND category:search";

				Query filterQuery = parser.parse(luceneQueryString);

				// =====================================================
				// 4. filter 付きベクトル検索
				// =====================================================
				Query vectorQuery = new KnnFloatVectorQuery(FIELD_VECTOR, queryVector, k, filterQuery);

				TopDocs topDocs = searcher.search(vectorQuery, k);

				// =====================================================
				// 5. 結果表示
				// =====================================================
				System.out.println("Lucene Query filter:");
				System.out.println(luceneQueryString);
				System.out.println();

				for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
					Document doc = searcher.doc(scoreDoc.doc);

					System.out.println("score=" + scoreDoc.score + ", id=" + doc.get(FIELD_ID) + ", category="
							+ doc.get(FIELD_CATEGORY) + ", text=" + doc.get(FIELD_TEXT));
				}
			}
		}
	}

	private static void addDocument(IndexWriter writer, String id, String text, String category, float[] vector)
			throws IOException {

		Document doc = new Document();

		// ID。完全一致検索・更新用
		doc.add(new StringField(FIELD_ID, id, Field.Store.YES));

		// キーワード検索用
		doc.add(new TextField(FIELD_TEXT, text, Field.Store.YES));

		// category。完全一致検索用
		doc.add(new StringField(FIELD_CATEGORY, category, Field.Store.YES));

		// ベクトル検索用
		doc.add(new KnnFloatVectorField(FIELD_VECTOR, vector, VectorSimilarityFunction.COSINE));

		writer.addDocument(doc);
	}
}
