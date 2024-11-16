package nlp4j.openai;

import java.lang.invoke.MethodHandles;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import nlp4j.AbstractDocumentAnnotator;
import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.util.DocumentUtil;
import nlp4j.util.PropertyUtils;

public class OpenAIEmbeddingAnnotator extends AbstractDocumentAnnotator implements DocumentAnnotator, AutoCloseable {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	private OpenAI openai;

	public OpenAIEmbeddingAnnotator() {
		super();
		String apiKey = PropertyUtils.getProperty("openai.api_key");
		String organization = PropertyUtils.getProperty("openai.organization");
		if (apiKey == null) {
			throw new RuntimeException("NOT_SET: openai.api_key");
		}
		openai = new OpenAI(organization, apiKey);
	}

	@Override
	public void annotate(Document doc) throws Exception {
		for (String target : super.targets) {
			String text = doc.getAttributeAsString(target);
			{
				JsonObject jo = openai.embeddings(text, "text-embedding-3-small");

				JsonArray vector_arr = jo.get("data").getAsJsonArray().get(0).getAsJsonObject().get("embedding")
						.getAsJsonArray();
				Double[] v = new Double[vector_arr.size()];

				for (int n = 0; n < vector_arr.size(); n++) {
					v[n] = vector_arr.get(n).getAsDouble();
				}

				doc.putAttribute("vector", v);
				logger.info("vector_size: " + v.length);
			}

		}

//		System.err.println(DocumentUtil.toJsonPrettyString(doc));

	}

	@Override
	public void close() throws Exception {
		openai.close();
	}

}
