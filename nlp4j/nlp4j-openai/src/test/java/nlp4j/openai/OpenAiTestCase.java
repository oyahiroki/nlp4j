package nlp4j.openai;

import com.google.gson.JsonObject;

import junit.framework.TestCase;
import nlp4j.openai.Configuration;
import nlp4j.openai.OpenAI;

public class OpenAiTestCase extends TestCase {

	public void testOpenAI() {
	}

	public void testEmbeddings() throws Exception {
		String apiKey = System.getProperty("openai.api_key");
		String organization = System.getProperty("openai.organization");

		String text = "今日はいい天気です。";

		Configuration configuration = new Configuration(organization, apiKey);

		System.err.println(configuration);

		OpenAI openai = new OpenAI(configuration);

		JsonObject jo = openai.embeddings(text);

//		System.err.println(jo.toString());
//
//		// 1536
//		System.err.println(
//				jo.get("data").getAsJsonArray().get(0).getAsJsonObject().get("embedding").getAsJsonArray().size());

	}

	public void testModels() {
	}

	public void testChat_completions() {
	}

}
