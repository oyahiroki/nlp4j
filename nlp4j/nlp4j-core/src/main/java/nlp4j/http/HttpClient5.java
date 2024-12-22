package nlp4j.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.net.URIBuilder;

import com.google.gson.JsonObject;

import nlp4j.NlpServiceResponse;
import nlp4j.impl.DefaultNlpServiceResponse;

public class HttpClient5 implements HttpClient {

	private long content_length = -1;

	CloseableHttpClient httpclient = HttpClients.createDefault();

	/**
	 * please use HttpClientBuilder()
	 */
	public HttpClient5() {
		super();
	}

	protected HttpClient5(String user, String password, String protocol, String hostname, int port) {
		super();
//		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(user, password.toCharArray());
//		BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
//		credentialsProvider.setCredentials(new AuthScope(null, -1), credentials);

		final HttpHost host = new HttpHost(protocol, hostname, port);

		final BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		// Only for demo purposes. Don't specify your credentials in code.
		credentialsProvider.setCredentials(new AuthScope(host),
				new UsernamePasswordCredentials(user, password.toCharArray()));

		this.httpclient = HttpClients.custom().setDefaultCredentialsProvider(credentialsProvider).build();
	}

	@Override
	public void close() throws IOException {
		this.httpclient.close();
	}

	/**
	 *
	 */
	@Override
	public DefaultNlpServiceResponse get(String url) throws IOException {

		int code = -1;
		String body = null;

		HttpGet httpGet = new HttpGet(url);
		// The underlying HTTP connection is still held by the response object
		// to allow the response content to be streamed directly from the network
		// socket.
		// In order to ensure correct deallocation of system resources
		// the user MUST call CloseableHttpResponse#close() from a finally clause.
		// Please note that if response content is not fully consumed the underlying
		// connection cannot be safely re-used and will be shut down and discarded
		// by the connection manager.
		try (@SuppressWarnings("deprecation")
		CloseableHttpResponse response1 = httpclient.execute(httpGet)) {
			code = response1.getCode();
			HttpEntity entity1 = response1.getEntity();
			// do something useful with the response body
			// and ensure it is fully consumed
			body = EntityUtils.toString(entity1);
			EntityUtils.consume(entity1);
		} catch (ParseException e) {
			throw new IOException(e);
		}

		DefaultNlpServiceResponse res = new DefaultNlpServiceResponse(code, body);

		return res;
	}

	@Override
	public NlpServiceResponse get(String url, Map<String, String> params) throws IOException {
		return get(url, null, params);
	}

	@Override
	public NlpServiceResponse get(String url, Map<String, String> requestHeaders, Map<String, String> params)
			throws IOException {

		int code = -1;
		String body = null;

		URIBuilder uriBuilder;
		try {
			uriBuilder = new URIBuilder(url);
		} catch (URISyntaxException e1) {
			throw new IOException(e1);
		}

		// パラメーター処理
		if (params != null) {
			for (String key : params.keySet()) {
				uriBuilder.addParameter(key, params.get(key));
			}
		}

		HttpGet httpGet;
		try {
			httpGet = new HttpGet(uriBuilder.build().toString());
		} catch (URISyntaxException e1) {
			throw new IOException(e1);
		}

		// ヘッダー処理
		if (requestHeaders != null) {
			for (String key : requestHeaders.keySet()) {
				String value = requestHeaders.get(key);
				httpGet.addHeader(key, value);
			}
		}

		Map<String, List<String>> responseHeaders = new HashMap<>();

		// The underlying HTTP connection is still held by the response object
		// to allow the response content to be streamed directly from the network
		// socket.
		// In order to ensure correct deallocation of system resources
		// the user MUST call CloseableHttpResponse#close() from a finally clause.
		// Please note that if response content is not fully consumed the underlying
		// connection cannot be safely re-used and will be shut down and discarded
		// by the connection manager.
		try (@SuppressWarnings("deprecation")
		CloseableHttpResponse response1 = httpclient.execute(httpGet)) {
			code = response1.getCode();
			HttpEntity entity1 = response1.getEntity();
			// do something useful with the response body
			// and ensure it is fully consumed
			body = EntityUtils.toString(entity1);

			Iterator<Header> it = response1.headerIterator();

			while (it.hasNext()) {
				Header header = it.next();
				String name = header.getName();
				String value = header.getValue();
				if (responseHeaders.get(name) == null) {
					responseHeaders.put(name, new ArrayList<>());
				}
				responseHeaders.get(name).add(value);
			}

			EntityUtils.consume(entity1);
		} catch (ParseException e) {
			throw new IOException(e);
		}

		DefaultNlpServiceResponse res = new DefaultNlpServiceResponse(code, body);
		res.setHeaders(responseHeaders);

		return res;

	}

	public DefaultNlpServiceResponse get(String url, Map<String, String> requestHeader, String requestBody)
			throws IOException {
		HttpUriRequestBase httpPost = new HttpGet(url);
		return request(httpPost, requestHeader, requestBody);
	}

	@Override
	public NlpServiceResponse get(String url, String jsonBody) throws IOException {
		return get(url, null, jsonBody);
	}

	@Override
	public long getContentLength() {
		return this.content_length;
	}

	private InputStream getInputStream(String url, HttpUriRequestBase httpGet) throws IOException {
		int code;
		// The underlying HTTP connection is still held by the response object
		// to allow the response content to be streamed directly from the network
		// socket.
		// In order to ensure correct deallocation of system resources
		// the user MUST call CloseableHttpResponse#close() from a finally clause.
		// Please note that if response content is not fully consumed the underlying
		// connection cannot be safely re-used and will be shut down and discarded
		// by the connection manager.
		@SuppressWarnings("deprecation")
		CloseableHttpResponse response1 = httpclient.execute(httpGet);
//		try () 
		{
			code = response1.getCode();
			if (((code >= 200) && (code < 300)) == false) {
				throw new HttpException(url, code);
			}
			HttpEntity entity1 = response1.getEntity();

			if (entity1 != null) {
				return entity1.getContent();
			} else {
				throw new IOException();
			}

			// do something useful with the response body
			// and ensure it is fully consumed
//			String b = EntityUtils.toString(entity1);
//			byte[] bb = EntityUtils.toByteArray(entity1);

//			EntityUtils.consume(entity1);
//			byte[] bb = b.getBytes("UTF-8");
//			this.content_length = bb.length;
//			return new ByteArrayInputStream(bb);
		}
//		catch (ParseException e) {
//			throw new IOException(e);
//		}
	}

	@Override
	public InputStream getInputStream(String url, Map<String, String> params) throws IOException {
//		String body = null;

		URIBuilder uriBuilder;
		try {
			uriBuilder = new URIBuilder(url);
		} catch (URISyntaxException e1) {
			throw new IOException(e1);
		}

		if (params != null) {
			for (String key : params.keySet()) {
				uriBuilder.addParameter(key, params.get(key));
			}
		}

		HttpUriRequestBase httpGet;
		try {
			httpGet = new HttpGet(uriBuilder.build().toString());
		} catch (URISyntaxException e1) {
			throw new IOException(e1);
		}
		return getInputStream(url, httpGet);
	}

	@Override
	public InputStream getInputStreamPost(String url, Map<String, String> params, Map<String, String> requestHeader,
			JsonObject requestBody) throws IOException {
		try {
			URIBuilder uriBuilder = new URIBuilder(url);
			{ // Request Parameter
				if (params != null) {
					for (String key : params.keySet()) {
						uriBuilder.addParameter(key, params.get(key));
					}
				}

			}
			HttpUriRequestBase httpPost;
			{
				httpPost = new HttpPost(uriBuilder.build().toString());
				{ // Request Header
					if (requestHeader != null) {
						for (String key : requestHeader.keySet()) {
							httpPost.addHeader(key, requestHeader.get(key));
						}
					}
				}
			}

			// Request body
			httpPost.setEntity(new StringEntity(requestBody.toString(), ContentType.APPLICATION_JSON, "UTF-8", false));

			@SuppressWarnings("deprecation")
			CloseableHttpResponse response1 = httpclient.execute(httpPost);
			{
				int code = response1.getCode();
				if (((code >= 200) && (code < 300)) == false) {
					throw new HttpException(url, code);
				}
				HttpEntity entity1 = response1.getEntity();
				if (entity1 != null) {
					return entity1.getContent();
				} else {
					throw new IOException();
				}

			}
		} catch (URISyntaxException e1) {
			throw new IOException(e1);
		}

	}

	@Override
	public DefaultNlpServiceResponse post(String url, Map<String, String> requestHeader, String requestBody)
			throws IOException {
		HttpUriRequestBase httpRequest = new HttpPost(url);
		return request(httpRequest, requestHeader, requestBody);
	}

	@Override
	public NlpServiceResponse put(String url, Map<String, String> requestHeader, String requestBody)
			throws IOException {
		HttpUriRequestBase httpRequest = new HttpPut(url);
		return request(httpRequest, requestHeader, requestBody);
	}

	@Override
	public NlpServiceResponse delete(String url, Map<String, String> requestHeader, String requestBody)
			throws IOException {
		HttpUriRequestBase httpRequest = new HttpDelete(url);
		return request(httpRequest, requestHeader, requestBody);
	}

	public DefaultNlpServiceResponse post(String url, Map<String, String> requestParams) throws IOException {
		HttpUriRequestBase httpPost = new HttpPost(url);

		// Mapからリクエストパラメータをリストに変換
		List<BasicNameValuePair> params = new ArrayList<>();
		for (Map.Entry<String, String> entry : requestParams.entrySet()) {
			params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}

		// パラメータをエンコードしてリクエストエンティティに設定
		httpPost.setEntity(new UrlEncodedFormEntity(params));
		httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
//		httpPost.setEntity(null);
		return request(httpPost, null, null);
	}

	@Override
	public NlpServiceResponse post(String url, String json) throws IOException {
		return post(url, null, json);
	}

	@Override
	public NlpServiceResponse put(String url, String json) throws IOException {
		return put(url, null, json);
	}

	@Override
	public NlpServiceResponse delete(String url, String json) throws IOException {
		return delete(url, null, json);
	}

	private DefaultNlpServiceResponse request(HttpUriRequestBase httpRequest, Map<String, String> requestHeader,
			String requestBody) throws IOException {
		String responseBody = null;

		if (requestHeader != null) {
			for (String key : requestHeader.keySet()) {
				httpRequest.addHeader(key, requestHeader.get(key));
			}
		}

		if (requestBody != null) {
			httpRequest.setEntity(new StringEntity(requestBody, ContentType.APPLICATION_JSON, "UTF-8", false));
		}

//		// HttpClientResponseHandlerの実装
//		HttpClientResponseHandler<String> responseHandler = response -> {
//			int status = response.getCode();
//			if (status >= 200 && status < 300) {
//				HttpEntity entity = response.getEntity();
//				return entity != null ? EntityUtils.toString(entity) : null;
//			} else {
//				throw new ClientProtocolException("Unexpected response status: " + status);
//			}
//		};
//		// リクエストの実行とレスポンスハンドラーの処理
//		// throws IOException
//		responseBody = httpclient.execute(httpRequest, responseHandler);

		try ( //
				@SuppressWarnings("deprecation")
				CloseableHttpResponse response1 = httpclient.execute(httpRequest); //
		) {
			int code = response1.getCode();
			HttpEntity entity1 = response1.getEntity();
			// do something useful with the response body
			// and ensure it is fully consumed
			responseBody = EntityUtils.toString(entity1);
			EntityUtils.consume(entity1);
			DefaultNlpServiceResponse res = new DefaultNlpServiceResponse(code, responseBody);
			return res;
		} catch (ParseException e) {
			throw new IOException(e);
		}

//		int code = -1;
//		DefaultNlpServiceResponse res = new DefaultNlpServiceResponse(code, responseBody);

//		return res;
	}

}
