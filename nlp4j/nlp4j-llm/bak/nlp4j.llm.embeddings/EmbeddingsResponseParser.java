package nlp4j.llm.embeddings;

import java.lang.invoke.MethodHandles;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import nlp4j.Document;
import nlp4j.util.JsonUtils;

/**
 * Parse embedding response
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
			JsonElement je = responseDoc.get("embeddings");

			if (je != null) {
				JsonArray embeddings = je.getAsJsonArray();
				if (logger.isDebugEnabled()) {
					logger.debug(embeddings.size());
				}
				Double[] dd = new Double[embeddings.size()];
				for (int n = 0; n < embeddings.size(); n++) {
					dd[n] = embeddings.get(n).getAsDouble();
				}

			} else {
				logger.error("SERVER RESPONSE_INVALID: (embedding response is null");
			}
		}

		return null;

	}

}
