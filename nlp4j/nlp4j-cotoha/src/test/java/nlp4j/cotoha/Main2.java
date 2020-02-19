package nlp4j.cotoha;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Main2 {

	public static void main(String[] args) throws Exception{
		
	String url = "https://api.ce-cotoha.com/api/dev" + "/nlp/v1/parse";
	String sentence = "今日はいい天気です。";
	String type = "default";
	
	String access_token = "";

	Gson gson = new Gson();

	JsonObject jsonObj = new JsonObject();

	jsonObj.addProperty("sentence", sentence);
	jsonObj.addProperty("type", type);

	OkHttpClient client = new OkHttpClient();

	MediaType JSON = MediaType.get("application/json; charset=utf-8");
	
	RequestBody body = RequestBody.create(JSON, jsonObj.toString());

	Request request = new Request.Builder() //
			.addHeader("Content-Type", "application/json;charset=UTF-8") //
			.addHeader("Authorization", "Bearer " + access_token) //
			.url(url) //
			.post(body) //
			.build();

	try (Response response = client.newCall(request).execute()) {

		int responseCode = response.code();

		String originalResponseBody = response.body().string();

		System.err.println(responseCode);

		System.err.println(response.headers().toString());

		System.err.println(originalResponseBody);

	}
		

	}

}
