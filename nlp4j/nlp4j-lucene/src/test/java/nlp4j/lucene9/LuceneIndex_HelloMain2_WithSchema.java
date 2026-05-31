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
public class LuceneIndex_HelloMain2_WithSchema {

	public static void main(String[] args) throws Exception {

		SearchSchema schema = new SearchSchema();
		{
			schema.add("id", FieldTypeDef.keyword().stored(true));
			schema.add("category", FieldTypeDef.keyword().stored(true).aggregatable(true).sortable(true));
			schema.add("content", FieldTypeDef.text().stored(true));
			schema.add("text_ja", FieldTypeDef.text().stored(true));
			schema.add("created_at", FieldTypeDef.longNumber().stored(true).range(true).sortable(true));
			schema.add("vector", FieldTypeDef.knnVector(384));
		}

		// Create a new in-memory Lucene index
		try (LuceneIndex index = new LuceneIndex()) {

			// --------------------
			// Add sample documents
			// --------------------

			// Document 1
			Document doc1 = schema.document() //
					.put("id", "1") //
					.put("category", "greeting") //
					.put("text_ja", "東京都の人口は多いです。") //
					.build();
			{
				index.add(doc1);
			}

			// Create the search API
			LuceneLocalSearchApi api = new LuceneLocalSearchApi(index);

			System.out.println("=".repeat(80));
			System.out.println("Lucene Local Search API Demo");
			System.out.println("=".repeat(80));

			// --------------------
			// Example 3: Match Query
			// --------------------
			System.out.println("\n[Example 3] Match Query");
			System.out.println("-".repeat(80));

			JsonNode matchRequest = JsonNode.object();
			{
				JsonNode matchQuery = JsonNode.object();
				matchQuery.put("match", JsonNode.object().put("text_ja", "東京都"));
				matchRequest.put("query", matchQuery);
				matchRequest.put("size", 10);
			}

			System.out.println("```");
			System.out.println(matchRequest);
			System.out.println("```");

			JsonNode result3 = api.search("myindex/_search", matchRequest);
			System.out.println(result3.toJson());

		}
	}
}


