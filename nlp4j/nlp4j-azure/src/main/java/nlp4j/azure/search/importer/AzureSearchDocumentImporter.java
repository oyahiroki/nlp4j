package nlp4j.azure.search.importer;

import java.io.IOException;

import com.azure.core.util.Configuration;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import nlp4j.AbstractDocumentImporter;
import nlp4j.Document;
import nlp4j.DocumentImporter;
import nlp4j.util.DocumentUtil;
import nlp4j.util.JsonUtils;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AzureSearchDocumentImporter extends AbstractDocumentImporter implements DocumentImporter {

	private static final String ENDPOINT = Configuration.getGlobalConfiguration()
			.get("AZURE_COGNITIVE_SEARCH_ENDPOINT");
	private static final String ADMIN_KEY = Configuration.getGlobalConfiguration()
			.get("AZURE_COGNITIVE_SEARCH_ADMIN_KEY");

//	private static final String INDEX_NAME = "hotels-sample-index";

	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

	@Override
	public void importDocument(Document doc) throws IOException {

//		System.err.println(JsonUtils.prettyPrint(DocumentUtil.toJsonObjectForIndex(doc)));

//		System.err.println(ENDPOINT);
//		System.err.println(ADMIN_KEY);

		// document
		// https://docs.microsoft.com/en-us/rest/api/searchservice/addupdate-or-delete-documents

//		String serviceName = "";
//		String indexName = "";
//		String api_version = "";

		OkHttpClient client = new OkHttpClient();

		String url = ENDPOINT;

		if (ENDPOINT == null) {
			throw new RuntimeException("ENDPOINT is not set");
		}
		if (ADMIN_KEY == null) {
			throw new RuntimeException("ADMIN_KEY is not set");
		}

		// {'value':[{'@search.action':'upload','id':'1','field1':'Nissan','text':'This
		// is test'}]}

		JsonObject requestDocs = new JsonObject();

		JsonArray requestValue = new JsonArray();

		{
			JsonObject requestDoc = DocumentUtil.toJsonObjectForIndex(doc);
			requestDoc.addProperty("@search.action", "upload");
			requestValue.add(requestDoc);
		}

		requestDocs.add("value", requestValue);

		System.err.println("To be imported.");
		System.err.println(JsonUtils.prettyPrint(requestDocs));

		String json = requestDocs.toString();

//		System.err.println(json);

//		Gson gson = new Gson();
//		JsonObject jsonObj = gson.fromJson(json, JsonObject.class);
//		System.err.println(jsonObj);

		RequestBody body = RequestBody.create(JSON, json); // new

		Request request = new Request.Builder() //
				.url(url) //
				.addHeader("api-key", ADMIN_KEY) // 忘れると403
				.post(body) //
				.build();

		Response response = client.newCall(request).execute();

		System.err.println("Response");
		System.err.println(response.code());
		System.err.println(response.message());

		String responsebody = response.body().string();

		System.err.println(JsonUtils.prettyPrint(responsebody));

	}

	@Override
	public void commit() throws IOException {

	}

	@Override
	public void close() {

	}

}
