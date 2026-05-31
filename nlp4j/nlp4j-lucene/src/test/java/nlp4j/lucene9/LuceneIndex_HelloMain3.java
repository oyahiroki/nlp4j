package nlp4j.lucene9;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.SortedDocValuesField;
import org.apache.lucene.util.BytesRef;

import nlp4j.json.JsonNode;

/**
 * Sample application demonstrating the usage of LuceneLocalSearchApi. This
 * example shows how to: - Create a Lucene index - Add documents with multiple
 * fields - Perform various types of searches (match_all, term, match,
 * query_string) - Execute aggregations
 */
public class LuceneIndex_HelloMain3 {

	public static void main(String[] args) throws Exception {

		SearchSchema schema = new SearchSchema();
		{
			schema.add("id", FieldTypeDef.keyword().stored(true));
			schema.add("category", FieldTypeDef.keyword().stored(true).aggregatable(true).sortable(true));
			schema.add("content", FieldTypeDef.text().stored(true));
			schema.add("text_ja", FieldTypeDef.text().stored(true));
			schema.add("created_at", FieldTypeDef.longNumber().stored(true).range(true).sortable(true));
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
							"query_vector": [0.8, 0.0],
							"k": 10
						}
					}
					"""));
			
			System.out.println(result1.toJson());

			// --------------------
			// Example 2: Vector Search with Term Filter
			// --------------------
			System.out.println("\n[Example 2] Vector Search with Term Filter (category:greeting)");
			System.out.println("-".repeat(80));
			
			JsonNode result2 = api.search("myindex/_search", JsonNode.parse("""
					{
						"size": 10,
						"knn": {
							"field": "vector",
							"query_vector": [1.0, 0.0],
							"k": 10,
							"filter": {
								"term": {
									"category": "greeting"
								}
							}
						}
					}
					"""));
			
			System.out.println(result2.toJson());

			// --------------------
			// Example 3: Vector Search with Match Filter
			// --------------------
			System.out.println("\n[Example 3] Vector Search with Match Filter (text_ja:東京)");
			System.out.println("-".repeat(80));
			
			JsonNode result3 = api.search("myindex/_search", JsonNode.parse("""
					{
						"size": 10,
						"knn": {
							"field": "vector",
							"query_vector": [0.5, 0.5],
							"k": 10,
							"filter": {
								"match": {
									"text_ja": "東京"
								}
							}
						}
					}
					"""));
			
			System.out.println(result3.toJson());

			System.out.println("\n" + "=".repeat(80));
			System.out.println("Vector Search Demo completed successfully!");
			System.out.println("=".repeat(80));
		}

		
	}
}


