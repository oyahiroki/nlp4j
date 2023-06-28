package nlp4j.chatgpt;

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
public class OpenAI {

	Configuration configuration;

	public OpenAI(Configuration configuration) {
		super();
		this.configuration = configuration;
	}

	/**
	 * see https://platform.openai.com/docs/api-reference/models/list
	 * 
	 * @return
	 * @throws IOException
	 */
	public JsonObject models() throws IOException {

		HttpClient5 client = new HttpClient5();

		Map<String, String> header = new HashMap<>();
		header.put("Authorization", "Bearer " + this.configuration.getApiKey());

		String url = "https://api.openai.com" + "/v1/models";

		NlpServiceResponse res = client.get(url, header, null);

		String json = res.getOriginalResponseBody();

		client.close();

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

}
