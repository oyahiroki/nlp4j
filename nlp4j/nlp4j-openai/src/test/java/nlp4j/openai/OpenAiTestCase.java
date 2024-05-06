package nlp4j.openai;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import junit.framework.TestCase;
import nlp4j.util.JsonObjectUtils;

public class OpenAiTestCase extends TestCase {

	public void testOpenAI() {
	}

	public void testEmbeddings() throws Exception {
		String apiKey = System.getProperty("openai.api_key");
		String organization = System.getProperty("openai.organization");

		String text = "今日はいい天気です。";

		Configuration configuration = new Configuration(organization, apiKey);

		System.err.println(configuration);

		try (OpenAI openai = new OpenAI(organization, apiKey);) {
			JsonObject jo = openai.embeddings(text);

//
//		// 1536
//		System.err.println(
//				jo.get("data").getAsJsonArray().get(0).getAsJsonObject().get("embedding").getAsJsonArray().size());

			System.err.println(jo.toString());

			JsonArray vector = JsonObjectUtils.query(jo, JsonArray.class, "/data[0]/embedding");

			System.err.println(vector.size()); // -> 1536
		}

	}

	public void testModels() {
	}

	public void testChat_completions() {
	}

}
