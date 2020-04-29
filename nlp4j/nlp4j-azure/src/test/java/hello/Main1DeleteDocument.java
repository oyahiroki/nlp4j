package hello;

import com.azure.core.util.Configuration;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.util.ArrayList;
import java.util.List;

public class Main1DeleteDocument {

	private static final String ENDPOINT = Configuration.getGlobalConfiguration()
			.get("AZURE_COGNITIVE_SEARCH_ENDPOINT");
	private static final String ADMIN_KEY = Configuration.getGlobalConfiguration()
			.get("AZURE_COGNITIVE_SEARCH_ADMIN_KEY");

	private static final String INDEX_NAME = "hotels-sample-index";

	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

	public static void main(String[] args) throws Exception {

		System.err.println(ENDPOINT);
		System.err.println(ADMIN_KEY);

		// document
		// https://docs.microsoft.com/en-us/rest/api/searchservice/addupdate-or-delete-documents

//		String serviceName = "";
//		String indexName = "";
//		String api_version = "";

		OkHttpClient client = new OkHttpClient();

		String url = ENDPOINT;

		String json = "{" //
				+ "'value':[" + "{" //
				+ "'@search.action':'delete'," //
				// + "'key_field_name':'id'," //
				+ "'id':'1'" //
				+ "}" //
				+ "]" //
				+ "}" //
		;

		System.err.println(json);

		Gson gson = new Gson();
		JsonObject jsonObj = gson.fromJson(json, JsonObject.class);
		System.err.println(jsonObj);

		RequestBody body = RequestBody.create(JSON, json); // new
		// RequestBody body = RequestBody.create(JSON, json); // old

		Request request = new Request.Builder() //
				.url(url) //
				.addHeader("api-key", ADMIN_KEY) // 忘れると403
				.post(body)//
				.build();

		Response response = client.newCall(request).execute();

		System.err.println(response.code());
		System.err.println(response.message());

		String responsebody = response.body().string();

		System.err.println(responsebody);

//		POST https://[service name].search.windows.net/indexes/[index name]/docs/index?api-version=[api-version]
//			  Content-Type: application/json   
//			  api-key: [admin key]  

//		{  
//			  "value": [  
//			    {  
//			      "@search.action": "upload (default) | merge | mergeOrUpload | delete",  
//			      "key_field_name": "unique_key_of_document", (key/value pair for key field from index schema)  
//			      "field_name": field_value (key/value pairs matching index schema)  
//			        ...  
//			    },  
//			    ...  
//			  ]  
//			}  

	}

}
