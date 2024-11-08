package nlp4j.openai;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import nlp4j.NlpServiceResponse;
import nlp4j.http.HttpClient;
import nlp4j.http.HttpClientBuilder;

/**
 * @author Hiroki Oya
 *
 */
public class OpenAI implements AutoCloseable {

	/**
	 * https://api.openai.com/v1/embeddings
	 */
	static public final String ENDPOINT_OPENAI_EMBEDDINGS = "https://api.openai.com/v1/embeddings";

	/**
	 * https://api.openai.com/v1/models
	 */
	static public final String ENDPOINT_OPENAI_MODELS = "https://api.openai.com/v1/models";

	/**
	 * https://api.openai.com/v1/chat/completions
	 */
	static public final String ENDPOINT_OPENAI_CHATCOMPLETIONS = "https://api.openai.com/v1/chat/completions";

	Config configuration;

	HttpClient client = (new HttpClientBuilder()).build();

	public OpenAI(Config configuration) {
		super();
		this.configuration = configuration;
	}

	public OpenAI(String organization, String apiKey) {
		configuration = new Config(organization, apiKey);
	}

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
		HttpClient client = (new HttpClientBuilder()).build();

		Map<String, String> header = new HashMap<>();
		header.put("Authorization", "Bearer " + this.configuration.getApiKey());

		NlpServiceResponse res = client.post(ENDPOINT_OPENAI_CHATCOMPLETIONS, header, requestbody.toString());

		String json = res.getOriginalResponseBody();

		client.close();

		return (new Gson()).fromJson(json, JsonObject.class);
	}

	public JsonObject chat_completions(String model, Messages messages) throws IOException {
		JsonObject requestbody = new JsonObject();
		{
			requestbody.addProperty("model", model);
			requestbody.add("messages", messages.toJsonArray());
		}
		return chat_completions(requestbody);
	}

	public InputStream chat_completions_stream(String model, Messages messages) throws IOException {
		JsonObject requestbody = new JsonObject();
		{
			requestbody.addProperty("model", model);
			requestbody.add("messages", messages.toJsonArray());
			requestbody.addProperty("stream", true);
		}

		return chat_completions_stream(requestbody);
	}

	public InputStream chat_completions_stream(JsonObject requestbody) throws IOException {
		/**
		 * https://platform.openai.com/docs/api-reference/completions/create
		 */
		HttpClient client = (new HttpClientBuilder()).build();

		Map<String, String> header = new HashMap<>();
		header.put("Authorization", "Bearer " + this.configuration.getApiKey());
		header.put("Content-Type", "application/json");

		Map<String, String> params = null;
		InputStream is = client.getInputStreamPost(ENDPOINT_OPENAI_CHATCOMPLETIONS, params, header, requestbody);
		return is;
	}

	@Override
	public void close() throws Exception {
		client.close();

	}

	/**
	 * Creates an embedding vector representing the input text.
	 * 
	 * @param text ("text-embedding-ada-002","text-embedding-3-small","text-embedding-3-large")
	 * @param model
	 * @return
	 * @throws IOException
	 * @see https://platform.openai.com/docs/api-reference/embeddings/create
	 */
	public JsonObject embeddings(String text, String model) throws IOException {
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
				requestBody.addProperty("model", model); // "text-embedding-ada-002","text-embedding-3-small","text-embedding-3-large"
				requestBody.addProperty("encoding_format", "float"); // 2024-08-08
			}
			// throws IOException
			NlpServiceResponse res = client.post(ENDPOINT_OPENAI_EMBEDDINGS, header, requestBody.toString());

			int code = res.getResponseCode();
			if (code != 200 //
					&& code != -1 // 2024-08-08
			) {
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

	/**
	 * Creates an embedding vector representing the input text.
	 * 
	 * @param text
	 * @return
	 * @throws IOException
	 * @see https://platform.openai.com/docs/api-reference/embeddings/create
	 * @deprecated
	 */
	public JsonObject embeddings(String text) throws IOException {
		throw new UnsupportedOperationException("This method is deprecated and should not be used.");
	}

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

}
