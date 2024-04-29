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

	static final String ENDPOINT_OPENAI_EMBEDDINGS = "https://api.openai.com/v1/embeddings";

	/**
	 * Creates an embedding vector representing the input text.
	 * 
	 * @param text
	 * @return
	 * @throws IOException
	 * @see https://platform.openai.com/docs/api-reference/embeddings/create
	 */
	public JsonObject embeddings(String text) throws IOException {
		/*
		 * https://platform.openai.com/docs/api-reference/embeddings/create
		 */
		{
			Map<String, String> header = new HashMap<>();
			{
				header.put("Authorization", "Bearer " + this.configuration.getApiKey());
				header.put("Content-Type", "application/json");
			}
			JsonObject requestBody = new JsonObject();
			{
				requestBody.addProperty("input", text);
				requestBody.addProperty("model", "text-embedding-ada-002");
			}
			NlpServiceResponse res = client.post(ENDPOINT_OPENAI_EMBEDDINGS, header, requestBody.toString());

			int code = res.getResponseCode();
			if (code != 200) {
				throw new IOException("url: " + ENDPOINT_OPENAI_EMBEDDINGS + ",response: " + code);
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

	static final String ENDPOINT_OPENAI_MODELS = "https://api.openai.com" + "/v1/models";

	/**
	 * Lists the currently available models, and provides basic information about
	 * each one such as the owner and availability.
	 * 
	 * @return
	 * @throws IOException
	 * @see https://platform.openai.com/docs/api-reference/models/list
	 */
	public JsonObject models() throws IOException {
		/*
		 * https://platform.openai.com/docs/api-reference/models/list
		 */

		Map<String, String> header = new HashMap<>();
		{
			header.put("Authorization", "Bearer " + this.configuration.getApiKey());
		}

		NlpServiceResponse res = client.get(ENDPOINT_OPENAI_MODELS, header, null);

		String json = res.getOriginalResponseBody();

		return (new Gson()).fromJson(json, JsonObject.class);

	}

	static final String ENDPOINT_OPENAI_CHATCOMPLETIONS = "https://api.openai.com/v1/chat/completions";

	/**
	 * (#Legacy) Creates a completion for the provided prompt and parameters.
	 * 
	 * @param requestbody
	 * @return
	 * @throws IOException
	 * @see https://platform.openai.com/docs/api-reference/completions/create
	 */
	public JsonObject chat_completions(JsonObject requestbody) throws IOException {
		/**
		 * https://platform.openai.com/docs/api-reference/completions/create
		 */
		HttpClient5 client = new HttpClient5();

		Map<String, String> header = new HashMap<>();
		header.put("Authorization", "Bearer " + this.configuration.getApiKey());

		NlpServiceResponse res = client.post(ENDPOINT_OPENAI_CHATCOMPLETIONS, header, requestbody.toString());

		String json = res.getOriginalResponseBody();

		client.close();

		return (new Gson()).fromJson(json, JsonObject.class);
	}

	@Override
	public void close() throws Exception {
		client.close();

	}

	public JsonObject chat_completions(String model, Messages messages) throws IOException {
		JsonObject requestbody = new JsonObject();
		requestbody.addProperty("model", model);
		requestbody.add("messages", messages.toJsonArray());
		return chat_completions(requestbody);
	}

}
