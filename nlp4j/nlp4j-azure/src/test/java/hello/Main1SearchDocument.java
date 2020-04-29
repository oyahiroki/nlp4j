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

public class Main1SearchDocument {

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

		String json = "{  \r\n" + 
				"     \"count\": true,  \r\n" + 
//				"     \"facets\": [ \"facet_expression_1\", \"facet_expression_2\", ... ],  \r\n" + 
//				"     \"filter\": \"odata_filter_expression\",  \r\n" + 
//				"     \"highlight\": \"highlight_field_1, highlight_field_2, ...\",  \r\n" + 
//				"     \"highlightPreTag\": \"pre_tag\",  \r\n" + 
//				"     \"highlightPostTag\": \"post_tag\",  \r\n" + 
//				"     \"minimumCoverage\": # (% of index that must be covered to declare query successful; default 100),  \r\n" + 
//				"     \"orderby\": \"orderby_expression\",  \r\n" + 
//				"     \"scoringParameters\": [ \"scoring_parameter_1\", \"scoring_parameter_2\", ... ],  \r\n" + 
//				"     \"scoringProfile\": \"scoring_profile_name\",  \r\n" + 
				"     \"search\": \"*\"  \r\n" + 
//				"     \"searchFields\": \"field_name_1, field_name_2, ...\",  \r\n" + 
//				"     \"searchMode\": \"any\" (default) | \"all\",  \r\n" + 
//				"     \"select\": \"field_name_1, field_name_2, ...\",  \r\n" + 
//				"     \"skip\": # (default 0),  \r\n" + 
//				"     \"top\": #  \r\n" + 
				"   }  ";

		System.err.println(json);

		Gson gson = new Gson();
		JsonObject jsonObj = gson.fromJson(json, JsonObject.class);
		System.err.println(jsonObj);

		RequestBody body = RequestBody.create(JSON, json); // new
		// RequestBody body = RequestBody.create(JSON, json); // old

		Request request = new Request.Builder() //
				.url(url) //
				.addHeader("api-key", ADMIN_KEY) // 忘れると403
				.post(body) //
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
