package nlp4j.wordpress;

import java.io.IOException;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import nlp4j.Document;
import nlp4j.NlpServiceResponse;
import nlp4j.http.HttpClient5;
import nlp4j.impl.DefaultNlpServiceResponse;

public class WordPressClient {
	private String endpoint;
	private String token;

	public WordPressClient(String endpoint, String user_name, String application_password) {
		this.endpoint = endpoint;
		this.token = Base64.getEncoder().encodeToString((user_name + ":" + application_password).getBytes());
	}

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

		try (HttpClient5 client = new HttpClient5();) {
			DefaultNlpServiceResponse response = client.post(url, headers, request_body.toString());
			return response;
		}

	}

}
