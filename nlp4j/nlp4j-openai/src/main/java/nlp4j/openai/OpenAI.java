package nlp4j.openai;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import nlp4j.NlpServiceResponse;
import nlp4j.http.HttpClient5;

/**
 * @author Hiroki Oya
 *
 */
public class OpenAI implements AutoCloseable {

	Configuration configuration;

	HttpClient5 client = new HttpClient5();

	public OpenAI(Configuration configuration) {
		super();
		this.configuration = configuration;
	}

	public OpenAI(String organization, String apiKey) {
		configuration = new Configuration(organization, apiKey);
	}

	public JsonObject embeddings(String text) throws IOException {

		{
			Map<String, String> header = new HashMap<>();
			{
				header.put("Authorization", "Bearer " + this.configuration.getApiKey());
				header.put("Content-Type", "application/json");
			}
			String url = "https://api.openai.com/v1/embeddings";
			JsonObject requestBody = new JsonObject();
			{
				requestBody.addProperty("input", text);
				requestBody.addProperty("model", "text-embedding-ada-002");
			}
			NlpServiceResponse res = client.post(url, header, requestBody.toString());

			int code = res.getResponseCode();
			if (code != 200) {
				throw new IOException("response: " + code);
			}

			Gson gson = new Gson();
			JsonObject jo = gson.fromJson(res.getOriginalResponseBody(), JsonObject.class);
//			System.err.println(res.getOriginalResponseBody());
//			// 1536
//			System.err.println(
//					jo.get("data").getAsJsonArray().get(0).getAsJsonObject().get("embedding").getAsJsonArray().size());
			return jo;
		}

	}

	/**
	 * see https://platform.openai.com/docs/api-reference/models/list
	 * 
	 * @return
	 * @throws IOException
	 */
	public JsonObject models() throws IOException {

		Map<String, String> header = new HashMap<>();
		{
			header.put("Authorization", "Bearer " + this.configuration.getApiKey());
		}

		String url = "https://api.openai.com" + "/v1/models";

		NlpServiceResponse res = client.get(url, header, null);

		String json = res.getOriginalResponseBody();

		return (new Gson()).fromJson(json, JsonObject.class);

	}

	/**
	 * see https://platform.openai.com/docs/api-reference/completions/create
	 * 
	 * @param requestbody
	 * @return
	 * @throws IOException
	 */
	public JsonObject chat_completions(JsonObject requestbody) throws IOException {
		HttpClient5 client = new HttpClient5();

		Map<String, String> header = new HashMap<>();
		header.put("Authorization", "Bearer " + this.configuration.getApiKey());

		String url = "https://api.openai.com" + "/v1/chat/completions";

		NlpServiceResponse res = client.post(url, header, requestbody.toString());

		String json = res.getOriginalResponseBody();

		client.close();

		return (new Gson()).fromJson(json, JsonObject.class);
	}

	@Override
	public void close() throws Exception {
		client.close();

	}

}
