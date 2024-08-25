package nlp4j.opensearch;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import junit.framework.TestCase;

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
				String method = "_search";
				String jsonBody = "{\r\n" //
						+ "  \"query\" : {\r\n" //
						+ "    \"term\": { \"text_txt_ja\": \"天気\" }\r\n" //
						+ "  }\r\n" //
						+ "}";
				JsonObject jo = (new Gson()).fromJson(jsonBody, JsonObject.class);
				client.get(method, jo);
			}

		}
	}

}
