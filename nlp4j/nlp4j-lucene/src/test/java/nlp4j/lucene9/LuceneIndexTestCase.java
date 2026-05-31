package nlp4j.lucene9;

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
}
