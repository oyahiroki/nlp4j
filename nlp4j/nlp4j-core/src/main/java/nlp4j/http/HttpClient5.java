package nlp4j.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.hc.client5.http.ClientProtocolException;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.net.URIBuilder;

import nlp4j.NlpServiceResponse;
import nlp4j.impl.DefaultNlpServiceResponse;

public class HttpClient5 implements HttpClient {

	private long content_length = -1;

	CloseableHttpClient httpclient = HttpClients.createDefault();

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
		try (CloseableHttpResponse response1 = httpclient.execute(httpGet)) {
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
		try (CloseableHttpResponse response1 = httpclient.execute(httpGet)) {
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

	@Override
	public long getContentLength() {
		return this.content_length;
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
		CloseableHttpResponse response1 = httpclient.execute(httpGet);
//		try () 
		{
			code = response1.getCode();
			if (((code >= 200) && (code < 300)) == false) {
				throw new IOException("url: " + url + ",response: " + code);
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
	public DefaultNlpServiceResponse post(String url, Map<String, String> requestHeader, String requestBody)
			throws IOException {

		int code = -1;
		String responseBody = null;

		HttpPost httpPost = new HttpPost(url);
		{
			if (requestHeader != null) {
				for (String key : requestHeader.keySet()) {
					httpPost.addHeader(key, requestHeader.get(key));
				}
			}
		}

		httpPost.setEntity(new StringEntity(requestBody, ContentType.APPLICATION_JSON, "UTF-8", false));

		// HttpClientResponseHandlerの実装
		HttpClientResponseHandler<String> responseHandler = response -> {
			int status = response.getCode();
			if (status >= 200 && status < 300) {
				HttpEntity entity = response.getEntity();
				return entity != null ? EntityUtils.toString(entity) : null;
			} else {
				throw new ClientProtocolException("Unexpected response status: " + status);
			}
		};

//		try (CloseableHttpResponse response1 = httpclient.execute(httpPost)) {
//			code = response1.getCode();
//			HttpEntity entity1 = response1.getEntity();
//			// do something useful with the response body
//			// and ensure it is fully consumed
//			responseBody = EntityUtils.toString(entity1);
//			EntityUtils.consume(entity1);
//		} catch (ParseException e) {
//			throw new IOException(e);
//		}

		// リクエストの実行とレスポンスハンドラーの処理
		responseBody = httpclient.execute(httpPost, responseHandler);

		DefaultNlpServiceResponse res = new DefaultNlpServiceResponse(code, responseBody);

		return res;

	}

	@Override
	public NlpServiceResponse post(String url, String json) throws IOException {
		return post(url, null, json);
	}

	@Override
	public void close() throws IOException {
		this.httpclient.close();
	}

}
