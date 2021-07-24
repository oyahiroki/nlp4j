package nlp4j.azure.search.searcher;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

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

	/**
	 * @param json1date '@@view':'deviations'
	 * @return deviations view
	 * @throws IOException on Error
	 */
	public JsonObject searchDeviation(String json1date) throws IOException {
		Gson gson = new Gson();
		try {
			JsonObject json1 = gson.fromJson(json1date, JsonObject.class);

			if (json1.get("@@view") == null || json1.get("@@view").getAsString().equals("deviations") == false) {
				throw new IOException("invalid parameters");
			}

			JsonObject json2 = gson.fromJson(json1date, JsonObject.class);

			json1.remove("@@view");
			json2.remove("@@view");

			return searchDeviation(json1, json2);
		} catch (JsonSyntaxException e) {
			throw new IOException(e);
		}
	}

	DecimalFormat df = new DecimalFormat("#.####");

	public JsonObject searchDeviation(JsonObject jsonObj1, JsonObject jsonObj2) throws IOException {

		JsonObject res = new JsonObject();

		System.err.println(JsonUtils.prettyPrint(jsonObj1));

		if (jsonObj1.get("facets").getAsJsonArray().size() < 2) {
			throw new IOException("at least 2 facets need to be set for deviation view");
		}

		{
			// date facet
			String facet1Date = jsonObj1.get("facets").getAsJsonArray().get(0).getAsString();
			String facet2 = jsonObj1.get("facets").getAsJsonArray().get(1).getAsString();
			HashMap<String, ArrayList<Integer>> facet2CountMap = new HashMap<>();

			ArrayList<String> facet2Values = new ArrayList<>();

			if (facet1Date.contains(",")) {
				facet1Date = facet1Date.substring(0, facet1Date.indexOf(","));
			}
			if (facet2.contains(",")) {
				facet2 = facet2.substring(0, facet2.indexOf(","));
			}

			System.err.println("facet1: " + facet1Date);
			System.err.println("facet2: " + facet2);

//			logger.info("search");
//			logger.debug(JsonUtils.prettyPrint(jsonObj1));

			AzureSearchClient az = new AzureSearchClient(super.props);

			JsonObject res1 = az.post("search", jsonObj1);

			System.err.println(JsonUtils.prettyPrint(res1));

			res = res1.deepCopy();

			{ // get countTotal and calculate ratio
				long countTotal = res.get("@odata.count").getAsLong();

				JsonObject search_facets = res.get("@search.facets").getAsJsonObject();

				// facet1Date
				if (search_facets.get(facet1Date) != null) {
					JsonArray arr1 = search_facets.get(facet1Date).getAsJsonArray();
					for (int n = 0; n < arr1.size(); n++) {
						JsonObject o = arr1.get(n).getAsJsonObject();
						long c = o.get("count").getAsLong();
						double r = Math.round(((double) c / (double) countTotal) * 1000000) / 1000000.0;
						if (r < 0.001) {
							r = 0.001;
						}
						o.addProperty("ratio", r);
					}
				}

				// facet2
				if (search_facets.get(facet2) != null) {
					JsonArray arr1 = search_facets.get(facet2).getAsJsonArray();
					for (int n = 0; n < arr1.size(); n++) {
						JsonObject o = arr1.get(n).getAsJsonObject();
						String v = o.get("value").getAsString();
						facet2Values.add(v);
						facet2CountMap.put(v, new ArrayList<Integer>());
					}
				}

			}

			JsonArray resDeviaions = new JsonArray();
			JsonArray resDeviaionsValues = new JsonArray();

			JsonArray facetValues = res1.get("@search.facets").getAsJsonObject().get(facet1Date).getAsJsonArray();

			// for each date value
			for (int n = 0; n < facetValues.size(); n++) {

				String v1 = facetValues.get(n).getAsJsonObject().get("value").getAsString();
				long count = facetValues.get(n).getAsJsonObject().get("count").getAsLong();

				// date filter should be set as filter, not in search parameter
				String filterDate;
				{
					if (n < (facetValues.size() - 1)) {
						String v2 = facetValues.get(n + 1).getAsJsonObject().get("value").getAsString();
						filterDate = "(" + facet1Date + " ge " + v1 + ") and (" + facet1Date + " lt " + v2 + ")";
					} else {
						filterDate = "(" + facet1Date + " ge " + v1 + ")";
					}
				}

				jsonObj2.addProperty("filter", filterDate);

				{
					logger.info("search");
					logger.debug(JsonUtils.prettyPrint(jsonObj2));

					// search
					JsonObject res2 = az.post("search", jsonObj2);

//					System.err.println("@search.facets---");
//					System.err.println(JsonUtils.prettyPrint(res2.get("@search.facets").getAsJsonObject()));
					JsonObject deviation = res2.get("@search.facets").getAsJsonObject();
					deviation.addProperty("filter", filterDate);
					resDeviaions.add(deviation);

					resDeviaionsValues.add(res2.get("@search.facets").getAsJsonObject().get(facet1Date).getAsJsonArray()
							.get(0).getAsJsonObject().get("value"));

					HashMap<String, Integer> facet2Map = new HashMap<String, Integer>();

					JsonArray facet2Counts = res2.get("@search.facets").getAsJsonObject().get(facet2).getAsJsonArray();
					for (int x = 0; x < facet2Counts.size(); x++) {
						JsonObject o = facet2Counts.get(x).getAsJsonObject();
						facet2Map.put(o.get("value").getAsString(), o.get("count").getAsInt());
					}

					for (String facet2Value : facet2Values) {
						if (facet2Map.containsKey(facet2Value)) {
							facet2CountMap.get(facet2Value).add(facet2Map.get(facet2Value));
						} else {
							facet2CountMap.get(facet2Value).add(0);
						}
					}

				}
			}

			res.add("@@deviations", resDeviaions);
			res.add("@@deviations.values", resDeviaionsValues);

			JsonObject data = new JsonObject();
			{
				data.add("labels", resDeviaionsValues);

				JsonArray datasets = new JsonArray();

				for (String key : facet2CountMap.keySet()) {
					JsonObject dataset = new JsonObject();

					{ // label
						dataset.addProperty("label", key);
					}

					{ // data
						ArrayList<Integer> ii = facet2CountMap.get(key);
						JsonArray arr = new JsonArray();
						for (int i : ii) {
							arr.add(i);
						}
						dataset.add("data", arr);
					}

					datasets.add(dataset);
				}
				data.add("datasets", datasets);
			}
			res.add("@@deviations.data", data);

		}

		return res;
	}

}
