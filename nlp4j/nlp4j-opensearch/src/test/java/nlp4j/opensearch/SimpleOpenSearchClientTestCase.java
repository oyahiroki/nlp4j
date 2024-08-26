package nlp4j.opensearch;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import junit.framework.TestCase;
import nlp4j.NlpServiceResponse;
import nlp4j.opensearch.data.SearchResult;

public class SimpleOpenSearchClientTestCase extends TestCase {

	public void testSimpleOpenSearchClient() throws Exception {
		String password = System.getProperty("OPENSEARCH_PASSWORD");
		if (password == null) {
			System.err.println("password is not set.");
			return;
		}
		try (SimpleOpenSearchClient client = new SimpleOpenSearchClient("https://localhost:9200", "admin", password,
				"myindex1");) {

			{
//				String method = "_search";
//				String jsonBody = "{\r\n" //
//						+ "  \"query\" : {\r\n" //
//						+ "    \"term\": { \"text_txt_ja\": \"天気\" }\r\n" //
//						+ "  }\r\n" //
//						+ "}";
//				JsonObject jo = (new Gson()).fromJson(jsonBody, JsonObject.class);
//				NlpServiceResponse res = client.get(method, jo);
//				System.err.println(res.getOriginalResponseBody());
			}
			System.err.println("-----");
			{
				String field = "text_txt_ja";
				String value = "天気";
				NlpServiceResponse res = client.search(field, value);
				System.err.println(res.getOriginalResponseBody());
				{
					JsonObject jo = (new Gson()).fromJson(res.getOriginalResponseBody(), JsonObject.class);
					int c = jo.get("hits").getAsJsonObject().get("hits").getAsJsonArray().size();
					if (c == 0) {
						System.err.println("Not Found");
					} else {
						System.err.println("Found");
					}

				}
				{
					Gson gson = new Gson();
					SearchResult result = gson.fromJson(res.getOriginalResponseBody(), SearchResult.class);
					int c = result.getHits().getHits().size();
					if (c == 0) {
						System.err.println("Not Found");
					} else {
						System.err.println("Found");
					}
					result.getHits().getHits().forEach(h -> {
						System.err.println(h);
					});
				}
			}

		}
	}

}
