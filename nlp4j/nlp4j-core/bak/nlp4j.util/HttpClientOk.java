package nlp4j.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nlp4j.NlpServiceResponse;
import nlp4j.http.HttpClient;
import nlp4j.impl.DefaultNlpServiceResponse;
import okhttp3.Headers;
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
public class HttpClientOk implements HttpClient {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	/**
	 * JSONのMediaTypeです。<br>
	 * Media Type of JSON
	 */
	public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

	private OkHttpClient client = new OkHttpClient();

	private long content_length = -1;

	/**
	 * @since 1.3.1.0
	 */
	public HttpClientOk() {
		super();
		client = new OkHttpClient().newBuilder() //
				.followRedirects(false) //
				.followSslRedirects(false) //
				.build();
	}

	/**
	 * created on 2020-04-29
	 * 
	 * @param url APIのURL
	 * @return NLPの結果
	 * @throws IOException 例外発生時にスローされる
	 * @since 1.3.1.0
	 */
	@Override
	public DefaultNlpServiceResponse get(String url) throws IOException {
		Map<String, String> params = null;
		return get(url, params);
	}

	/**
	 * @param url    APIのURL
	 * @param params API パラメータ
	 * @return NLPの結果
	 * @throws IOException 例外発生時にスローされる
	 */
	@Override
	public DefaultNlpServiceResponse get(String url, Map<String, String> params) throws IOException {

		HttpUrl.Builder builder = HttpUrl.parse(url).newBuilder();

		if (params != null) {
			params.forEach(builder::addQueryParameter);
		}

		Request request = new Request.Builder().url(builder.build()).build();

		try (Response response = client.newCall(request).execute()) {
			int responseCode = response.code();

//			System.err.println(response.headers().toMultimap());

			String originalResponseBody = response.body().string();
			DefaultNlpServiceResponse res //
					= new DefaultNlpServiceResponse(responseCode, originalResponseBody);

			// how to close connection??
			// https://square.github.io/okhttp/4.x/okhttp/okhttp3/-ok-http-client/

			res.setHeaders(response.headers().toString());
			res.setHeaders(response.headers().toMultimap());

			response.body().close();

			return res;
		}
	}

	@Override
	public long getContentLength() {
		return content_length;
	}

	@Override
	public InputStream getInputStream(String url, Map<String, String> params) throws IOException {

		HttpUrl.Builder builder = HttpUrl.parse(url).newBuilder();

		if (params != null) {
			params.forEach(builder::addQueryParameter);
		}

		Request request = new Request.Builder().url(builder.build()).build();

		{

			Response response = client.newCall(request).execute();
			Headers headers = response.headers();
			for (String name : headers.names()) {
				String value = headers.get(name);
//				System.err.println(name + "=" + value);
				if ("Content-Length".equals(name)) {
					long n = Long.parseLong(value);
					this.content_length = n;
				}
			}

			int responseCode = response.code();

			if (responseCode == 200) {
				return response.body().byteStream();
			} else {
				throw new IOException("responseCode:" + responseCode);
			}
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
	@Override
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
			{
				Headers headers = response.headers();
				for (String key : headers.names()) {
					logger.debug(key + "=" + headers.get(key));
				}
			}
			String originalResponseBody = response.body().string();
			DefaultNlpServiceResponse res //
					= new DefaultNlpServiceResponse(responseCode, originalResponseBody);
			return res;
		}
	}

	/**
	 * @param url  APIのURL
	 * @param json API パラメータ
	 * @return NLPの結果
	 * @throws IOException 例外発生時にスローされる
	 */
	@Override
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

	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub

	}

}
