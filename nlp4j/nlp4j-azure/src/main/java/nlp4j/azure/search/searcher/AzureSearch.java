package nlp4j.azure.search.searcher;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import nlp4j.AbstractDocumentSearcher;
import nlp4j.DocumentSearcher;
import nlp4j.azure.search.AzureSearchClient;
import nlp4j.util.JsonUtils;

/**
 * @author Hiroki Oya
 * @since 1.3
 *
 */
public class AzureSearch extends AbstractDocumentSearcher implements DocumentSearcher {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	/**
	 * @param requestObj リクエストオブジェクト
	 * @return response from Azure Search
	 * @throws IOException 例外発生時
	 */
	public JsonObject search(JsonObject requestObj) throws IOException {
		logger.info("search");
		logger.debug(JsonUtils.prettyPrint(requestObj));
		AzureSearchClient az = new AzureSearchClient(super.props);
		JsonObject res = az.post("search", requestObj);
		logger.debug(JsonUtils.prettyPrint(res));
		return res;
	}

	/**
	 * @param json リクエストJSOｎ
	 * @return response from Azure Search
	 * @throws IOException 例外発生時
	 */
	public JsonObject search(String json) throws IOException {
		Gson gson = new Gson();
		try {
			JsonObject requestObj = gson.fromJson(json, JsonObject.class);
			return search(requestObj);
		} catch (JsonSyntaxException e) {
			throw new IOException(e);
		}
	}

	public JsonObject searchDeviation(String json1date, String json2facet) throws IOException {
		Gson gson = new Gson();
		try {
			JsonObject json1 = gson.fromJson(json1date, JsonObject.class);
			JsonObject json2 = gson.fromJson(json2facet, JsonObject.class);
			return searchDeviation(json1, json2);
		} catch (JsonSyntaxException e) {
			throw new IOException(e);
		}
	}

	public JsonObject searchDeviation(JsonObject jsonObj1, JsonObject jsonObj2) throws IOException {

		JsonObject res = new JsonObject();

		System.err.println(JsonUtils.prettyPrint(jsonObj1));
		{
			String facet1 = jsonObj1.get("facets").getAsJsonArray().get(0).getAsString();
			if (facet1.contains(",")) {
				facet1 = facet1.substring(0, facet1.indexOf(","));
			}
			System.err.println(facet1);

			logger.info("search");
			logger.debug(JsonUtils.prettyPrint(jsonObj1));

			AzureSearchClient az = new AzureSearchClient(super.props);
			JsonObject res1 = az.post("search", jsonObj1);

			System.err.println(JsonUtils.prettyPrint(res1));

			JsonArray facetValues = res1.get("@search.facets").getAsJsonObject().get(facet1).getAsJsonArray();

			for (int n = 0; n < facetValues.size(); n++) {
				String v1 = facetValues.get(n).getAsJsonObject().get("value").getAsString();

				if (n < (facetValues.size() - 1)) {
					String v2 = facetValues.get(n + 1).getAsJsonObject().get("value").getAsString();
					System.err.println(v1 + " - " + v2);
				} else {
					System.err.println(v1);
				}
			}

		}

		{
			logger.info("search");
			logger.debug(JsonUtils.prettyPrint(jsonObj2));
			AzureSearchClient az = new AzureSearchClient(super.props);
			JsonObject res2 = az.post("search", jsonObj2);
			System.err.println(JsonUtils.prettyPrint(res2));
		}

		return res;
	}

}
