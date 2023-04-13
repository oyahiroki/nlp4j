package nlp4j.util;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import nlp4j.NlpServiceResponse;
import nlp4j.http.HttpClient;

public class HttpClientOkTestCase extends TestCase {

	public void testHttpClient() {
	}

	public void testGetString001() throws Exception {
		HttpClient httpclient = new HttpClientOk();
		NlpServiceResponse res = httpclient.get("https://nlp4j.org");
		System.err.println(res.getOriginalResponseBody());
		httpclient.close();
	}

	public void testGetStringMapOfStringString001() throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("test", "123");
		HttpClient httpclient = new HttpClientOk();
		NlpServiceResponse res = httpclient.get("https://nlp4j.org", map);
		System.err.println(res.getOriginalResponseBody());
		httpclient.close();
	}

	public void testGetContentLength001() throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("test", "123");
		HttpClient httpclient = new HttpClientOk();
		String url = "https://nlp4j.org";
		try ( //
				InputStream is = httpclient.getInputStream(url, null);
				BufferedInputStream bis = new BufferedInputStream(is);
		//
		) {

			long contentLength = httpclient.getContentLength();
			byte[] buff = new byte[1];
			int readSize;
			int count = 0;
			while ((readSize = bis.read(buff)) != -1) {
				System.err.print(".");
				count++;
				if (count % 10 == 0) {
					System.err.println("\n");
				}
			}

			System.err.println("count: " + count);
			System.err.println("contentLength: " + contentLength);

		}
		httpclient.close();
	}

	public void testGetInputStream() {
	}

	public void testPostStringMapOfStringStringString() {
	}

	public void testPostStringString() {
	}

}
