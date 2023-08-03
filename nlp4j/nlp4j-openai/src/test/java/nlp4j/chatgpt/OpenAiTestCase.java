package nlp4j.chatgpt;

import junit.framework.TestCase;

public class OpenAiTestCase extends TestCase {

	public void testOpenAI() {
	}

	public void testEmbeddings() throws Exception {
		String apiKey = System.getProperty("openai.api_key");
		String organization = System.getProperty("openai.organization");

		Configuration configuration = new Configuration(organization, apiKey);

		System.err.println(configuration);

		OpenAI openai = new OpenAI(configuration);

		openai.embeddings();
	}

	public void testModels() {
	}

	public void testChat_completions() {
	}

}
