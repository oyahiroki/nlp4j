package nlp4j.lucene9;

import nlp4j.json.JsonNode;

/**
 * Example demonstrating the use of STORED_ONLY fields.
 * 
 * STORED_ONLY fields are useful for:
 * - Storing raw JSON data for display purposes
 * - Keeping metadata that doesn't need to be searched
 * - Storing computed values or denormalized data
 * - Maintaining original data alongside indexed versions
 */
public class StoredOnlyFieldExample {

	public static void main(String[] args) throws Exception {

		// Define schema with STORED_ONLY field
		SearchSchema schema = new SearchSchema();
		{
			schema.add("id", FieldTypeDef.keyword().stored(true));
			schema.add("title", FieldTypeDef.text().stored(true));
			schema.add("category", FieldTypeDef.keyword().stored(true).aggregatable(true));
			
			// STORED_ONLY field - not searchable, only for retrieval
			schema.add("raw_json", FieldTypeDef.storedOnly());
			schema.add("display_data", FieldTypeDef.storedOnly());
		}

		// Create index
		try (LuceneIndex index = new LuceneIndex()) {

			// Add documents with STORED_ONLY fields
			{
				String rawJson = "{\"id\":\"001\",\"title\":\"表示用タイトル\",\"metadata\":{\"author\":\"太郎\"}}";
				
				index.add(schema.document()
						.put("id", "001")
						.put("title", "Tokyo Travel Guide")
						.put("category", "travel")
						.put("raw_json", rawJson)
						.put("display_data", "Additional display information")
						.build());
			}

			{
				String rawJson = "{\"id\":\"002\",\"title\":\"京都観光ガイド\",\"metadata\":{\"author\":\"花子\"}}";
				
				index.add(schema.document()
						.put("id", "002")
						.put("title", "Kyoto Sightseeing")
						.put("category", "travel")
						.put("raw_json", rawJson)
						.put("display_data", "More display data")
						.build());
			}

			// Create search API
			LuceneLocalSearchApi api = new LuceneLocalSearchApi(index);

			System.out.println("=".repeat(80));
			System.out.println("STORED_ONLY Field Example");
			System.out.println("=".repeat(80));

			// Search by title (STORED_ONLY fields are not searchable)
			System.out.println("\n[Example 1] Search by title");
			System.out.println("-".repeat(80));
			
			JsonNode result1 = api.search("myindex/_search", JsonNode.parse("""
					{
						"size": 10,
						"query": {
							"match": {
								"title": "Tokyo"
							}
						}
					}
					"""));
			
			System.out.println(result1.toJson());

			// Match all - STORED_ONLY fields are returned in results
			System.out.println("\n[Example 2] Match all - STORED_ONLY fields in results");
			System.out.println("-".repeat(80));
			
			JsonNode result2 = api.search("myindex/_search", JsonNode.parse("""
					{
						"size": 10,
						"query": {
							"match_all": {}
						}
					}
					"""));
			
			System.out.println(result2.toJson());

			// Aggregate by category (STORED_ONLY fields cannot be aggregated)
			System.out.println("\n[Example 3] Aggregation by category");
			System.out.println("-".repeat(80));
			
			JsonNode result3 = api.search("myindex/_search", JsonNode.parse("""
					{
						"size": 0,
						"query": {
							"match_all": {}
						},
						"aggs": {
							"categories": {
								"terms": {
									"field": "category",
									"size": 10
								}
							}
						}
					}
					"""));
			
			System.out.println(result3.toJson());

			System.out.println("\n" + "=".repeat(80));
			System.out.println("Key Points:");
			System.out.println("- raw_json and display_data are STORED_ONLY fields");
			System.out.println("- They appear in search results (_source)");
			System.out.println("- They cannot be searched, sorted, or aggregated");
			System.out.println("- Useful for storing display data, metadata, or raw JSON");
			System.out.println("=".repeat(80));
		}
	}
}

// Made with Bob
