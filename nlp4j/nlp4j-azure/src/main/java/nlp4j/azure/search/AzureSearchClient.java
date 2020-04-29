package nlp4j.azure.search;

import java.io.IOException;
import java.util.Properties;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import nlp4j.util.JsonUtils;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author Hiroki Oya
 * @since 1.3
 */
public class AzureSearchClient {

	private String endPoint;
	private String admin_key;
	static private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

	/**
	 * @param props contains "endpoint","admin_key"
	 */
	public AzureSearchClient(Properties props) {
		endPoint = props.getProperty("endpoint");
		admin_key = props.getProperty("admin_key");

		if (endPoint == null) {
			throw new RuntimeException("ENDPOINT is not set");
		}
		if (admin_key == null) {
			throw new RuntimeException("ADMIN_KEY is not set");
		}

	}

	public JsonObject post(JsonObject requestDocs) throws IOException {
		OkHttpClient client = new OkHttpClient();
		String url = endPoint;

		String json = requestDocs.toString();

		RequestBody body = RequestBody.create(JSON, json); // new

		Request request = new Request.Builder() //
				.url(url) //
				.addHeader("api-key", admin_key) // 忘れると403
				.post(body) //
				.build();

		Response response = client.newCall(request).execute();

		System.err.println("Response");
		System.err.println(response.code());
		System.err.println(response.message());

		String responsebody = response.body().string();

		System.err.println(JsonUtils.prettyPrint(responsebody));

		Gson gson = new Gson();

		JsonObject res = gson.fromJson(responsebody, JsonObject.class);

		return res;
	}

}
