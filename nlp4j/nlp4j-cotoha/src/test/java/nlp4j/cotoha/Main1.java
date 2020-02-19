package nlp4j.cotoha;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Main1 {

	public static void main(String[] args) throws Exception {
		
	String url = "https://api.ce-cotoha.com/v1/oauth/accesstokens";
	String clientId = "[client id]";
	String clientSecret = "[client secret]";
	
clientId = "VtGKQ9GQxUOkGGvhAHSSoATKNAjQ7Ocn";
clientSecret = "uNRzcGVTG1rBTGeq";
	

//		{
//		 "grantType": "client_credentials",
//		 "clientId": "[client id]",
//		 "clientSecret": "[client secret]"
//		}

	Gson gson = new Gson();

	JsonObject jsonObj = new JsonObject();

	jsonObj.addProperty("grantType", "client_credentials");
	jsonObj.addProperty("clientId", clientId);
	jsonObj.addProperty("clientSecret", clientSecret);

	OkHttpClient client = new OkHttpClient();
	
	MediaType JSON = MediaType.get("application/json; charset=utf-8");

	RequestBody body = RequestBody.create(JSON, jsonObj.toString());

	Request request = new Request.Builder() //
			.url(url) //
			.post(body) //
			.build();

	try (Response response = client.newCall(request).execute()) {
		int responseCode = response.code();
		String originalResponseBody = response.body().string();
		System.err.println(responseCode); // 201
		System.err.println(originalResponseBody);

		// 201
//	          {
//	              "access_token": "xxx", 
//	              "token_type": "bearer",
//	              "expires_in": "86399" ,
//	              "scope": "" ,    
//	              "issued_at": "1581590104700"           
//	          }

		}

	}

}
