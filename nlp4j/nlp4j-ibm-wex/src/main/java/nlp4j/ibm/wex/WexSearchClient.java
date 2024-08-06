package nlp4j.ibm.wex;

import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import nlp4j.Document;
import nlp4j.DocumentBuilder;
import nlp4j.http.HttpClient;
import nlp4j.http.HttpClientBuilder;
import nlp4j.util.JsonObjectUtils;
import nlp4j.util.JsonUtils;
import nlp4j.util.MapBuilder;

public class WexSearchClient {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	private String collection = "sandbox";
	private String language = Locale.getDefault().toString();
	private String locale = Locale.getDefault().getLanguage();
	private String endPoint = "http://localhost:8393";

	public JsonObject search(String query, int start, int results) throws IOException {

		String url = this.endPoint + "/api/v10/search";

		Map<String, String> params = (new MapBuilder<String, String>()) //
				.put("collection", this.collection)//
				.put("output", "application/json")// xmi
				.put("enableHref", "false") //
				.put("query", query) //
				.put("queryLang", this.language) //
				.put("linguistic", "engine") //
				.put("synonymExpansion", "automatic") //
				.put("nearDuplication", "shingle") //
				.put("documentPart", "aggregation") //
				.put("summaryLengthRatio", "100") //
				.put("sortKey", "relevance") //
				.put("sortOrder", "desc") //
				.put("start", "" + start) //
				.put("results", "" + results) //
				.build();//
		try ( //
				HttpClient client = (new HttpClientBuilder()).build(); //
				InputStream is = client.getInputStream(url, params); //
		) {
//			System.out.println(JsonUtils.prettyPrint(org.apache.commons.io.IOUtils.toString(is)));
//			System.out.println(org.apache.commons.io.IOUtils.toString(is));

			String responseBody = org.apache.commons.io.IOUtils.toString(is);

			JsonObject jo = JsonObjectUtils.fromJson(responseBody);

			System.err.println(JsonUtils.prettyPrint(jo));

			return jo;
		}

	}

	public List<Document> searchDocuments(String query, int start, int results) throws IOException {

		List<Document> docs = new ArrayList<>();

		JsonObject jo = search(query, start, results);

		JsonArray es_result = jo //
				.get("es_apiResponse").getAsJsonObject() //
				.get("es_result").getAsJsonArray();

		for (int n = 0; n < es_result.size(); n++) {
			JsonObject jo_es_result = es_result.get(n).getAsJsonObject();
			double es_relevance = jo_es_result.get("es_relevance").getAsDouble();
			String es_summary = jo_es_result.get("es_summary").getAsString();
			Document doc = (new DocumentBuilder()) //
					.text(es_summary) //
					.put("relevance", es_relevance) //
					.build();
			docs.add(doc);
		}

		return docs;
	}

	static public void main(String[] args) throws Exception {

		String query = "*:*";

		query = "タイヤ";

		int start = 0;
		int results = 10;

		WexSearchClient wex = new WexSearchClient();
		List<Document> docs = wex.searchDocuments(query, start, results);
		docs.forEach(doc -> {
			System.err.println(doc);
		});

	}

}
