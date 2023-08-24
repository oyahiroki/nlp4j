package nlp4j.openai;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import nlp4j.AbstractDocumentAnnotator;
import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.util.DocumentUtil;

public class OpenAIEmbeddingAnnotator extends AbstractDocumentAnnotator implements DocumentAnnotator, AutoCloseable {

	OpenAI openai;

	public OpenAIEmbeddingAnnotator() {
		super();
		String apiKey = System.getProperty("openai.api_key");
		String organization = System.getProperty("openai.organization");
		if (apiKey == null) {
			throw new RuntimeException("NOT_SET: openai.api_key");
		}
		openai = new OpenAI(organization, apiKey);
	}

	@Override
	public void annotate(Document doc) throws Exception {
		String text = doc.getText();
		{
			JsonObject jo = openai.embeddings(text);

			JsonArray vector_arr = jo.get("data").getAsJsonArray().get(0).getAsJsonObject().get("embedding")
					.getAsJsonArray();
			Double[] v = new Double[vector_arr.size()];

			for (int n = 0; n < vector_arr.size(); n++) {
				v[n] = vector_arr.get(n).getAsDouble();
			}

			doc.putAttribute("vector", v);

		}

		System.err.println(DocumentUtil.toJsonPrettyString(doc));

	}

	@Override
	public void close() throws Exception {
		openai.close();
	}

}
