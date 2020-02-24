package nlp4j.cotoha;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import nlp4j.NlpService;
import nlp4j.impl.DefaultNlpServiceResponse;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * COTOA API (https://api.ce-cotoha.com/contents/index.html) を利用するクラス
 * 
 * @author Hiroki Oya
 * @since 1.0.0.0
 */
public class CotohaNlpService implements NlpService {

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	/**
	 * Media Type JSON
	 */
	public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

	/**
	 * API利用のアクセストークン<br>
	 * https://api.ce-cotoha.com/contents/reference/accesstoken.html
	 */
	AccessToken accessToken;

	/**
	 * COTOA API (https://api.ce-cotoha.com/contents/index.html) を利用するクラス<br>
	 * デフォルトコンストラクタ
	 */
	public CotohaNlpService() {
	}

	/**
	 * アクセストークンを取得する
	 * 
	 * @return アクセストークン(CotohaNlpServiceクラス内部で利用される)
	 * @throws IOException 例外発生時
	 */
	public AccessToken accessToken() throws IOException {

		String url = CotohaConfig.COTOHA_URL_ACCESSTOKEN;
		String clientId = CotohaConfig.COTOHA_CLIENT_ID;
		String clientSecret = CotohaConfig.COTOHA_CLIENT_SECRET;

//		request body
//		{
//		 "grantType": "client_credentials",
//		 "clientId": "[client id]",
//		 "clientSecret": "[client secret]"
//		}

		Gson gson = new Gson();

		JsonObject jsonObjRequestBody = new JsonObject();
		{
			jsonObjRequestBody.addProperty("grantType", "client_credentials");
			jsonObjRequestBody.addProperty("clientId", clientId);
			jsonObjRequestBody.addProperty("clientSecret", clientSecret);
		}

		OkHttpClient client = new OkHttpClient();

		RequestBody body = RequestBody.create(JSON, jsonObjRequestBody.toString());

		Request request = new Request.Builder() //
				.url(url) //
				.post(body) //
				.build();

		try (Response response = client.newCall(request).execute()) {

			int responseCode = response.code();

			String originalResponseBody = response.body().string();

			logger.debug(responseCode);
			logger.debug(response.headers().toString());
			logger.debug(originalResponseBody);

			// 201 normal response
//	          {
//	              "access_token": "xxx", 
//	              "token_type": "bearer",
//	              "expires_in": "86399" ,
//	              "scope": "" ,    
//	              "issued_at": "1581590104700"           
//	          }

			JsonObject responseJson = gson.fromJson(originalResponseBody, JsonObject.class);
			if (responseJson.get("access_token") != null) {

				String access_token = responseJson.get("access_token").getAsString();
				String token_type = responseJson.get("token_type").getAsString();
				long issued_at = Long.parseLong(responseJson.get("issued_at").getAsString());
				long expires_in = Long.parseLong(responseJson.get("expires_in").getAsString());
				String scope = responseJson.get("scope").getAsString();
				AccessToken accessToken = new AccessToken(access_token, token_type, expires_in, scope, issued_at);
				this.accessToken = accessToken;
				return accessToken;
			} //
			else {
				logger.error(responseCode);
				System.err.println(responseCode);
				logger.error(response.headers().toString());
				System.err.println(response.headers().toString());
				logger.error(originalResponseBody);
				System.err.println(originalResponseBody);
				return null;
			}

		} // END OF try

	}

	/**
	 * 自然文をデフォルト設定で解析する<br>
	 * https://api.ce-cotoha.com/contents/reference/apireference.html#parsing
	 * 
	 * @param sentence 自然文
	 * @return 解析結果
	 * @throws IOException 例外発生時
	 */
	public DefaultNlpServiceResponse nlpV1Parse(String sentence) throws IOException {
		// $ curl -X POST -H "Content-Type:application/json;charset=UTF-8"
		// -H "Authorization:Bearer [Access Token]"
		// -d '{"sentence":"犬は歩く。","type": "default"}' "[API Base URL]/nlp/v1/parse"
		return nlpV1Parse(sentence, "default");
	}

	/**
	 * 自然文を指定した設定で解析する<br>
	 * https://api.ce-cotoha.com/contents/reference/apireference.html#parsing
	 * 
	 * @param sentence 自然文
	 * @param type     解析オプション( default - 通常文 | kuzure - SNSなどの崩れた文)
	 * @return 解析結果
	 * @throws IOException 例外発生時
	 */
	public DefaultNlpServiceResponse nlpV1Parse(String sentence, String type) throws IOException {

		// curl sample
		// $ curl -X POST
		// -H "Content-Type:application/json;charset=UTF-8"
		// -H "Authorization:Bearer [Access Token]"
		// -d '{"sentence":"犬は歩く。","type": "default"}' "[API Base URL]/nlp/v1/parse"

		if (accessToken == null) {
			accessToken();
		}

		// Not Initialized
		if (accessToken == null) {
			throw new IOException("accessToken not initialized.");
		}
		String url = CotohaConfig.COTOHA_API_BASE_URL + "/nlp/v1/parse";

		JsonObject jsonObjRequestBody = new JsonObject();
		{
			jsonObjRequestBody.addProperty("sentence", sentence);
			jsonObjRequestBody.addProperty("type", type);
		}

//		default config
//		OkHttpClient client = new OkHttpClient();
		
// connection timeout, read timeout
		OkHttpClient client = new OkHttpClient.Builder() //
				.connectTimeout(60, TimeUnit.SECONDS)//
				.readTimeout(60, TimeUnit.SECONDS)//
				.build();

		RequestBody body = RequestBody.create(JSON, jsonObjRequestBody.toString());

		Request request = new Request.Builder() //
				.addHeader("Authorization", "Bearer " + this.accessToken.getAccess_token()) //
				.url(url) //
				.post(body) //
				.build();

		try (Response response = client.newCall(request).execute()) {

			int responseCode = response.code();

			String originalResponseBody = response.body().string();

			DefaultNlpServiceResponse nlpResponse = new DefaultNlpServiceResponse(responseCode, originalResponseBody);

			logger.debug(responseCode);
			logger.debug(response.headers().toString());
			logger.debug(originalResponseBody);

//			400
			// URLにスラッシュが２つついていた場合
//			X-Cloud-Trace-Context: c6f651411c7d5075872ece0dd386f721
//			Server: Google Frontend
//			Content-Type: application/json; charset=utf-8
//			Strict-Transport-Security: max-age=15552000; includeSubDomains
//			Date: Thu, 13 Feb 2020 12:16:20 GMT
//			X-Download-Options: noopen
//			X-XSS-Protection: 1; mode=block
//			X-Content-Type-Options: nosniff
//			ETag: W/"35-jYYgw6WhGK+YVg3y/zjbTAGq2u0"
//			X-DNS-Prefetch-Control: off
//			Connection: close
//			set-cookie: sessionId=s%3AiyeDvAOSozM85N4Qrebj3c3PxQvnsxt4.VX5MBqo0Egrc0bxXYmLvPpkebkQbtRQYJ%2Btlwbtn5Ds; Path=/; Expires=Fri, 14 Feb 2020 12:16:20 GMT; HttpOnly; Secure
//			X-Frame-Options: SAMEORIGIN
//			Content-Length: 53
//
//			{"status":"error","errorType":"authentication_error"}

			CotohaNlpV1ResponseHandler handler = new CotohaNlpV1ResponseHandler();
			handler.parse(originalResponseBody);

			return nlpResponse;

		} // END OF try

	}

}
