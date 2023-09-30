package nlp4j.llm.embeddings;

import java.lang.invoke.MethodHandles;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import nlp4j.Document;
import nlp4j.util.JsonUtils;

/**
 * Parse GiNZA response
 * 
 * @author Hiroki Oya
 *
 */
public class EmbeddingsResponseParser {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	public Document parseResponse(String json) {

		{
			Gson gson = new Gson();

			// GiNZA のレスポンスは JsonObject
			JsonObject responseDoc = gson.fromJson(json, JsonObject.class);

			JsonUtils.prettyPrint(responseDoc);

			// message
			// time
			// text
			// embeddings

			JsonArray embeddings = responseDoc.get("embeddings").getAsJsonArray();
			System.err.println(embeddings.size());
			Double[] dd = new Double[embeddings.size()];
			for (int n = 0; n < embeddings.size(); n++) {
				dd[n] = embeddings.get(n).getAsDouble();
			}
		}

		return null;

	}

}
