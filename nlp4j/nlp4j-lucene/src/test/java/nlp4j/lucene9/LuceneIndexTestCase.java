package nlp4j.lucene9;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchEvent.Modifier;

import junit.framework.TestCase;
import nlp4j.json.JsonNode;

public class LuceneIndexTestCase extends TestCase {

	public void testSearch001() throws Exception {
		SearchSchema schema = new SearchSchema();
		{
			schema.add("id", FieldTypeDef.keyword().stored(true));
			schema.add("category", FieldTypeDef.keyword().stored(true).aggregatable(true).sortable(true));
			schema.add("text_ja", FieldTypeDef.text().stored(true));
			schema.add("vector", FieldTypeDef.knnVector(2));
		}

		// Create a new in-memory Lucene index
		try (LuceneIndex index = new LuceneIndex()) {

			// --------------------
			// Add sample documents
			// --------------------

			// Document 1
			{
				index.add(schema.document() //
						.put("id", "1") //
						.put("category", "greeting") //
						.put("text_ja", "東京都の人口は多いです。") //
						.putVector("vector", new float[] { 1.0f, 0.0f }) //
						.build());
			}
			// Document 2
			{
				index.add(schema.document() //
						.put("id", "2") //
						.put("category", "greeting") //
						.put("text_ja", "京都の人口は多いです。") //
						.putVector("vector", new float[] { 0.0f, 1.0f }) //
						.build());
			}

			// Create the search API
			LuceneLocalSearchApi api = new LuceneLocalSearchApi(index);

			System.out.println("=".repeat(80));
			System.out.println("Vector Search Demo with LuceneLocalSearchApi");
			System.out.println("=".repeat(80));

			// --------------------
			// Example 1: Basic Vector Search
			// --------------------
			System.out.println("\n[Example 1] Basic Vector Search");
			System.out.println("-".repeat(80));

			JsonNode result1 = api.search("myindex/_search", JsonNode.parse("""
					{
						"size": 10,
						"knn": {
							"field": "vector",
							"query_vector": [0.8, 0.1],
							"k": 10
						}
					}
					"""));

			System.out.println(result1.toJson());
			assertTrue(result1.get("hits") != null);
			assertTrue(result1.get("hits").get("total").get("value").asInt() == 2);
			assertTrue(result1.get("hits").get("hits").get(0).get("_source").get("id").asString().equals("1"));
		}
	}

	public void testSearch002() throws Exception {
		SearchSchema schema = new SearchSchema();
		{
			schema.add("id", FieldTypeDef.keyword().stored(true));
			schema.add("category", FieldTypeDef.keyword().stored(true).aggregatable(true).sortable(true));
			schema.add("text_ja", FieldTypeDef.text().stored(true));
			schema.add("data", FieldTypeDef.storedOnly());
		}

		// Create a new in-memory Lucene index
		try (LuceneIndex index = new LuceneIndex()) {

			// --------------------
			// Add sample documents
			// --------------------

			// Document 1
			{
				index.add(schema.document() //
						.put("id", "1") //
						.put("category", "greeting") //
						.put("text_ja", "東京都の人口は多いです。") //
						.put("data", "THIS IS DATA1") //
						.build());
			}
			// Document 2
			{
				index.add(schema.document() //
						.put("id", "2") //
						.put("category", "greeting") //
						.put("text_ja", "京都の人口は多いです。") //
						.put("data", "THIS IS DATA2") //
						.build());
			}

			// Create the search API
			LuceneLocalSearchApi api = new LuceneLocalSearchApi(index);

			// --------------------
			// Example 1: Basic Vector Search
			// --------------------
			System.out.println("\n[Example 1] Basic Vector Search");
			System.out.println("-".repeat(80));

			JsonNode result1 = api.search("myindex/_search", JsonNode.parse("""
					{
						"size": 10,
						"query": {
							"match_all": {}
						}
					}
					"""));

			System.out.println(result1.toJson());
			assertTrue(result1.get("hits") != null);
		}
	}

	public void testSearch100() throws Exception {
		SearchSchema schema = new SearchSchema();
		{
			schema.add("id", FieldTypeDef.keyword().stored(true));
			schema.add("category", FieldTypeDef.keyword().stored(true).aggregatable(true).sortable(true));
			schema.add("text_ja", FieldTypeDef.text().stored(true));
			schema.add("data", FieldTypeDef.storedOnly());
		}

		// Create a new in-memory Lucene index
		try (LuceneIndex index = new LuceneIndex()) {

			// --------------------
			// Add sample documents
			// --------------------

			// Document 1
			{
				index.add(schema.document() //
						.put("id", "1") //
						.put("category", "greeting") //
						.put("text_ja", "東京都の人口は多いです。") //
						.put("data", "THIS IS DATA1") //
						.build());
			}
			// Document 2
			{
				index.add(schema.document() //
						.put("id", "2") //
						.put("category", "greeting") //
						.put("text_ja", "京都の人口は多いです。") //
						.put("data", "THIS IS DATA2") //
						.build());
			}

			// Create the search API
			LuceneLocalSearchApi api = new LuceneLocalSearchApi(index);

			// --------------------
			// Example 1: Basic Vector Search
			// --------------------
			System.out.println("\n[Example 1] Basic Vector Search");
			System.out.println("-".repeat(80));

			JsonNode result1 = api.search("myindex/_search", JsonNode.parse("""
					{
						"size": 10,
						"query": {
							"match_all": {}
						}
					}
					"""));

			System.out.println(result1.toJson());
			assertTrue(result1.get("hits") != null);

			Path tempDir = Files.createTempDirectory("temp_");

			index.writeToAndClose(tempDir);

			index.close();

			System.err.println(tempDir);

		}

	}

	public void testWriteToAndClosePreflightCheckFailsOnNonEmptyDir() throws Exception {
		Path tempDir = Files.createTempDirectory("temp_nonempty_");
		try {
			// ディレクトリ内にダミーファイルを作成
			Files.writeString(tempDir.resolve("dummy.txt"), "dummy content");

			LuceneIndex index = new LuceneIndex();
			org.apache.lucene.document.Document doc = new org.apache.lucene.document.Document();
			doc.add(new org.apache.lucene.document.TextField("text", "hello world", org.apache.lucene.document.Field.Store.YES));
			index.add(doc);
			index.commit();

			try {
				index.writeToAndClose(tempDir);
				fail("Expected IOException because output dir is not empty");
			} catch (IOException e) {
				assertTrue(e.getMessage().contains("Output directory is not empty"));
			}

			// 事前チェックで失敗した場合、index はまだ closed にならず利用可能（あるいは明示的に close 可能）
			// 検索やクローズが正常に行えることを確認
			java.util.List<org.apache.lucene.document.Document> docs = index.search("text:hello", 10);
			assertEquals(1, docs.size());
			index.close();
		} finally {
			Files.walk(tempDir)
					.sorted(java.util.Comparator.reverseOrder())
					.map(Path::toFile)
					.forEach(java.io.File::delete);
		}
	}
}
