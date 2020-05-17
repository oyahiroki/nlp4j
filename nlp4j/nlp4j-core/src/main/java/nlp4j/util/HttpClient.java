package nlp4j.util;

import java.io.IOException;
import java.util.Map;

import nlp4j.NlpServiceResponse;
import nlp4j.impl.DefaultNlpServiceResponse;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * HTTPクライアントのクラスです。<br>
 * HTTP Client class.
 * 
 * @author Hiroki Oya
 * @version 1.0
 *
 */
public class HttpClient {
	/**
	 * JSONのMediaTypeです。<br>
	 * Media Type of JSON
	 */
	public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

	OkHttpClient client = new OkHttpClient();

	/**
	 * @param url  APIのURL
	 * @param json API パラメータ
	 * @return NLPの結果
	 * @throws IOException 例外発生時にスローされる
	 */
	public NlpServiceResponse post(String url, String json) throws IOException {
		RequestBody body = RequestBody.create(JSON, json);
		Request request = new Request.Builder().url(url).post(body).build();
		try (Response response = client.newCall(request).execute()) {
			int responseCode = response.code();
			String originalResponseBody = response.body().string();
			DefaultNlpServiceResponse res //
					= new DefaultNlpServiceResponse(responseCode, originalResponseBody);
			return res;
		}
	}

	/**
	 * @param url             APIのURL
	 * @param requestHeader   リクエストヘッダ
	 * @param jsonRequestBody リクエストボディJSON
	 * @return NLPの結果
	 * @throws IOException 例外発生時にスローされる
	 * @since 1.3
	 */
	public DefaultNlpServiceResponse post(String url, Map<String, String> requestHeader, String jsonRequestBody)
			throws IOException {

		RequestBody body = RequestBody.create(JSON, jsonRequestBody);
		Builder builder = new Request.Builder().url(url);
		if (requestHeader != null) {
			requestHeader.forEach(builder::addHeader);
		}

		Request request = builder.post(body).build();
		try (Response response = client.newCall(request).execute()) {
			int responseCode = response.code();
			String originalResponseBody = response.body().string();
			DefaultNlpServiceResponse res //
					= new DefaultNlpServiceResponse(responseCode, originalResponseBody);
			return res;
		}
	}

	/**
	 * @param url    APIのURL
	 * @param params API パラメータ
	 * @return NLPの結果
	 * @throws IOException 例外発生時にスローされる
	 */
	public DefaultNlpServiceResponse get(String url, Map<String, String> params) throws IOException {

		HttpUrl.Builder builder = HttpUrl.parse(url).newBuilder();
		if (params != null) {
			params.forEach(builder::addQueryParameter);
		}

		Request request = new Request.Builder().url(builder.build()).build();

		try (Response response = client.newCall(request).execute()) {
			int responseCode = response.code();
			String originalResponseBody = response.body().string();
			DefaultNlpServiceResponse res //
					= new DefaultNlpServiceResponse(responseCode, originalResponseBody);
			return res;
		}
	}

}
