package examples;

import nlp4j.lucene.LocalSearch;

public class Example22_AggregateJsonLuceneQuery {

	public static void main(String[] args) throws Exception {
		try (LocalSearch search = new LocalSearch("en")) {
			search.addJson("""
					{
					  "id": "1",
					  "body": "Kyoto is a historic city in Japan.",
					  "category": "city",
					  "country": "Japan"
					}
					""");
			search.addJson("""
					{
					  "id": "2",
					  "body": "Nintendo is a historic video game company headquartered in Kyoto.",
					  "category": "company",
					  "country": "Japan"
					}
					""");
			search.addJson("""
					{
					  "id": "3",
					  "body": "Paris is the capital city of France.",
					  "category": "city",
					  "country": "France"
					}
					""");
			search.commit();

			String json = search.aggregateJson("""
					{
					  "field": "category",
					  "query": "text_en:Kyoto AND text_en:historic",
					  "size": 10
					}
					""");
			System.out.println("testAggregateJsonLuceneQuery001: " + json);

			nlp4j.json.JsonNode result = nlp4j.json.JsonNode.parse(json);
			nlp4j.json.JsonNode buckets = result.get("aggregations").get("values").get("buckets");

			// Kyoto を含む id=1, id=2 → city:1, company:1
//			assertEquals(2, buckets.size());
//			assertEquals(1L, buckets.get(0).get("doc_count").asLong(0));
		}
	}
}
// Expected output
//testAggregateJsonLuceneQuery001: {
//	  "aggregations": {
//	    "values": {
//	      "doc_count_error_upper_bound": 0,
//	      "sum_other_doc_count": 0,
//	      "buckets": [
//	        {
//	          "key": "city",
//	          "doc_count": 1
//	        },
//	        {
//	          "key": "company",
//	          "doc_count": 1
//	        }
//	      ]
//	    }
//	  }
//	}
