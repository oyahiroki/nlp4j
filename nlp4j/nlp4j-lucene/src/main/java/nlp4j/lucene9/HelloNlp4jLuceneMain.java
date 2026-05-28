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
public class HelloNlp4jLuceneMain {

	public static void main(String[] args) throws Exception {

		// Create a new in-memory Lucene index
		try (LuceneIndex index = new LuceneIndex()) {

			// --------------------
			// Add sample documents
			// --------------------

			// Document 1
			Document doc1 = new Document();
			{
				// StringField
				doc1.add(new StringField("id", "1", Field.Store.YES));
				// StringField + SortedDocValuesField
				doc1.add(new StringField("category", "greeting", Field.Store.YES));
				doc1.add(new SortedDocValuesField("category", new BytesRef("greeting")));
				// TextField
				doc1.add(new TextField("content", "Hello Lucene", Field.Store.YES));
				index.add(doc1);
			}

			// Document 2
			Document doc2 = new Document();
			{
				doc2.add(new StringField("id", "2", Field.Store.YES));
				doc2.add(new StringField("category", "technology", Field.Store.YES));
				doc2.add(new SortedDocValuesField("category", new BytesRef("technology")));
				doc2.add(new TextField("content", "Apache Lucene is a search library", Field.Store.YES));
				index.add(doc2);
			}

			// Document 3
			Document doc3 = new Document();
			{
				doc3.add(new StringField("id", "3", Field.Store.YES));
				doc3.add(new StringField("category", "technology", Field.Store.YES));
				doc3.add(new SortedDocValuesField("category", new BytesRef("technology")));
				doc3.add(new TextField("content", "Search engines use Lucene", Field.Store.YES));
				index.add(doc3);
			}

			// Document 4 (Japanese text)
			Document doc4 = new Document();
			{
				doc4.add(new StringField("id", "4", Field.Store.YES));
				doc4.add(new StringField("category", "greeting", Field.Store.YES));
				doc4.add(new SortedDocValuesField("category", new BytesRef("greeting")));
				doc4.add(new TextField("text_ja", "こんにちは、Luceneの世界へようこそ", Field.Store.YES));
				index.add(doc4);
			}

			// Create the search API
			LuceneLocalSearchApi api = new LuceneLocalSearchApi(index);

			System.out.println("=".repeat(80));
			System.out.println("Lucene Local Search API Demo");
			System.out.println("=".repeat(80));

			// --------------------
			// Example 1: Match All Query
			// --------------------
			System.out.println("\n[Example 1] Match All Query");
			System.out.println("-".repeat(80));

			JsonNode matchAllRequest = JsonNode.object();
			{
				matchAllRequest.put("query", JsonNode.object().put("match_all", JsonNode.object()));
				matchAllRequest.put("size", 10);

			}
//			{
//			  "query": {
//			    "match_all": {}
//			  },
//			  "size": 10
//			}

			System.out.println("```");
			System.out.println(matchAllRequest.toJson());
			System.out.println("```");

			JsonNode result1 = api.search("myindex/_search", matchAllRequest);
			System.out.println(result1.toJson());

			// --------------------
			// Example 2: Term Query
			// --------------------
			System.out.println("\n[Example 2] Term Query (category:technology)");
			System.out.println("-".repeat(80));

			JsonNode termRequest = JsonNode.object();
			{
				JsonNode termQuery = JsonNode.object();
				termQuery.put("term", JsonNode.object().put("category", "technology"));
				termRequest.put("query", termQuery);
				termRequest.put("size", 10);
			}

//			{
//			  "query": {
//			    "term": {
//			      "category": "technology"
//			    }
//			  },
//			  "size": 10
//			}
			System.out.println("```");
			System.out.println(termRequest.toString());
			System.out.println("```");

			JsonNode result2 = api.search("myindex/_search", termRequest);
			System.out.println(result2.toJson());

			// --------------------
			// Example 3: Match Query
			// --------------------
			System.out.println("\n[Example 3] Match Query (content:Lucene)");
			System.out.println("-".repeat(80));

			JsonNode matchRequest = JsonNode.object();
			{
				JsonNode matchQuery = JsonNode.object();
				matchQuery.put("match", JsonNode.object().put("content", "Lucene"));
				matchRequest.put("query", matchQuery);
				matchRequest.put("size", 10);
			}

			System.out.println("```");
			System.out.println(matchRequest);
			System.out.println("```");

			JsonNode result3 = api.search("myindex/_search", matchRequest);
			System.out.println(result3.toJson());

			// --------------------
			// Example 4: Query String
			// --------------------
			System.out.println("\n[Example 4] Query String (content:search OR content:library)");
			System.out.println("-".repeat(80));

			JsonNode qsRequest = JsonNode.object();
			{
				JsonNode qsQuery = JsonNode.object();
				JsonNode qsBody = JsonNode.object();
				qsBody.put("query", "search OR library");
				qsBody.put("default_field", "content");
				qsQuery.put("query_string", qsBody);
				qsRequest.put("query", qsQuery);
				qsRequest.put("size", 10);
			}

			System.out.println("```");
			System.out.println(qsRequest.toString());
			System.out.println("```");

//			{
//			  "query": {
//			    "query_string": {
//			      "query": "search OR library",
//			      "default_field": "content"
//			    }
//			  },
//			  "size": 10
//			}

			JsonNode result4 = api.search("myindex/_search", qsRequest);
			System.out.println(result4.toJson());

			// --------------------
			// Example 5: Aggregation (Terms)
			// --------------------
			System.out.println("\n[Example 5] Terms Aggregation (category field)");
			System.out.println("-".repeat(80));

			JsonNode aggRequest = JsonNode.object();
			{
				aggRequest.put("query", JsonNode.object().put("match_all", JsonNode.object()));
				aggRequest.put("size", 0); // Only return aggregations, no hits
				JsonNode aggs = JsonNode.object();
				JsonNode categoryAgg = JsonNode.object();
				JsonNode termsAgg = JsonNode.object();
				termsAgg.put("field", "category");
				termsAgg.put("size", 10);
				categoryAgg.put("terms", termsAgg);
				aggs.put("categories", categoryAgg);
				aggRequest.put("aggs", aggs);

			}

//			{
//			  "query": {
//			    "query_string": {
//			      "query": "search OR library",
//			      "default_field": "content"
//			    }
//			  },
//			  "size": 10
//			}

			System.out.println("```");
			System.out.println(aggRequest.toString());
			System.out.println("```");

			JsonNode result5 = api.search("myindex/_search", aggRequest);
			System.out.println(result5.toJson());

			// --------------------
			// Example 6: Pagination
			// --------------------
			System.out.println("\n[Example 6] Pagination (from:1, size:2)");
			System.out.println("-".repeat(80));

			JsonNode pageRequest = JsonNode.object();
			pageRequest.put("query", JsonNode.object().put("match_all", JsonNode.object()));
			pageRequest.put("from", 1);
			pageRequest.put("size", 2);

			JsonNode result6 = api.search("myindex/_search", pageRequest);
			System.out.println(result6.toJson());

			System.out.println("\n" + "=".repeat(80));
			System.out.println("Demo completed successfully!");
			System.out.println("=".repeat(80));
		}
	}
}

// Made with Bob
