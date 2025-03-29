package nlp4j.wordpress;

import java.io.IOException;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import nlp4j.Document;
import nlp4j.NlpServiceResponse;
import nlp4j.http.HttpClient;
import nlp4j.http.HttpClientBuilder;

/**
 * @author Hiroki Oya
 * @since 1.0.0.0
 */
public class WordPressClient {
	private String endpoint;
	private String token;

	public WordPressClient(String endpoint, String user_name, String application_password) {
		this.endpoint = endpoint;
		this.token = Base64.getEncoder().encodeToString((user_name + ":" + application_password).getBytes());
	}

	/**
	 * https://developer.wordpress.org/rest-api/reference/posts/#create-a-post
	 */
	public NlpServiceResponse post(Document doc) throws IOException {

		JsonObject request_body = new JsonObject();
		request_body.addProperty("title", doc.getAttributeAsString("title"));
		request_body.addProperty("status", doc.getAttributeAsString("status")); // draft, publish
		request_body.addProperty("content", doc.getAttributeAsString("content")); //

		if (doc.getAttributeAsListNumbers("categories") != null) {
			JsonArray categories = new JsonArray();
			for (Number n : doc.getAttributeAsListNumbers("categories")) {
				categories.add(n);
			}
			request_body.add("categories", categories);
		}

//		JsonArray categories = new JsonArray();
//		categories.add(41);
//		request_body.add("categories", categories);
		// categories: [n]
		// tags: [n]
		request_body.addProperty("slug", "pre_open");

		String url = endpoint + "/wp-json/wp/v2/posts/";

		Map<String, String> headers = new LinkedHashMap<>();
		headers.put("Authorization", "Basic " + token);

		try (HttpClient client = (new HttpClientBuilder()).build();) {
			NlpServiceResponse response = client.post(url, headers, request_body.toString());
			return response;
		}

	}

	/**
	 * https://developer.wordpress.org/rest-api/reference/categories/
	 */
	public void getCategories() throws IOException {
		String url = endpoint + "/wp-json/wp/v2/categories/?per_page=100&order=asc&orderby=id";
		try (HttpClient client = (new HttpClientBuilder()).build();) {
			NlpServiceResponse response = client.get(url);
			JsonElement jo = response.getAsJson();
			JsonArray arr = jo.getAsJsonArray();
			for (int n = 0; n < arr.size(); n++) {
				System.out.println(arr.get(n).getAsJsonObject().get("id").getAsInt() + "\t"
						+ arr.get(n).getAsJsonObject().get("name").getAsString());
			}
		}
	}

}
