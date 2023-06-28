package nlp4j.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import junit.framework.TestCase;
import nlp4j.NlpServiceResponse;
import nlp4j.impl.DefaultNlpServiceResponse;

public class HttpClient5TestCase extends TestCase {

	public void testGetString() throws Exception {

		try (HttpClient5 client = new HttpClient5();) {
			String url = "https://nlp4j.org";
			DefaultNlpServiceResponse res = client.get(url);
			System.err.println(res.getOriginalResponseBody());
		}

	}

	public void testGetStringMapOfStringString() throws IOException {

		String url = "http://httpbin.org/get";

		Map<String, String> params = new LinkedHashMap<>();
		params.put("param1", "value1");

		try (HttpClient5 client = new HttpClient5();) {
			NlpServiceResponse res = client.get(url, params);
			System.err.println(res.getOriginalResponseBody());
		}

	}

	public void testGetContentLength() throws IOException {
		String url = "http://httpbin.org/get";

		Map<String, String> params = new LinkedHashMap<>();
		params.put("param1", "value1");

		try (HttpClient5 client = new HttpClient5();) {
			InputStream is = client.getInputStream(url, params);
			System.err.println(IOUtils.toString(is, "UTF-8"));
			is.close();
		}
	}

	public void testGetInputStream() throws IOException {
		String url = "http://httpbin.org/get";

		Map<String, String> params = new LinkedHashMap<>();
		params.put("param1", "value1");

		try (HttpClient5 client = new HttpClient5();) {
			InputStream is = client.getInputStream(url, params);
			System.err.println(IOUtils.toString(is, "UTF-8"));
			System.err.println(client.getContentLength());
			is.close();
		}
	}

	public void testPostStringMapOfStringStringString() throws IOException {
		String url = "http://httpbin.org/post";
		String requestBody = "{\"test\":\"This is test.\"}";

		Map<String, String> params = new LinkedHashMap<>();
		params.put("param1", "value1");

		try (HttpClient5 client = new HttpClient5();) {
			DefaultNlpServiceResponse res = client.post(url, params, requestBody);
			System.err.println(res.getOriginalResponseBody());
		}
	}

	public void testPostStringString() throws IOException {
		String url = "http://httpbin.org/post";
		String requestBody = "{\"test\":\"This is test.\"}";

		try (HttpClient5 client = new HttpClient5();) {
			NlpServiceResponse res = client.post(url, requestBody);
			System.err.println(res.getOriginalResponseBody());
		}
	}

}
