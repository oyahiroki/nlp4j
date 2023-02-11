package nlp4j.util;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import nlp4j.impl.DefaultNlpServiceResponse;

public class HttpClientTestCase extends TestCase {

	public void testHttpClient() {
		fail("Not yet implemented");
	}

	public void testGetString001() throws Exception {
		HttpClient httpclient = new HttpClient();
		DefaultNlpServiceResponse res = httpclient.get("https://nlp4j.org");
		System.err.println(res.getOriginalResponseBody());
	}

	public void testGetStringMapOfStringString001() throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("test", "123");
		HttpClient httpclient = new HttpClient();
		DefaultNlpServiceResponse res = httpclient.get("https://nlp4j.org", map);
		System.err.println(res.getOriginalResponseBody());
	}

	public void testGetContentLength001() throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("test", "123");
		HttpClient httpclient = new HttpClient();
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

	}

	public void testGetInputStream() {
		fail("Not yet implemented");
	}

	public void testPostStringMapOfStringStringString() {
		fail("Not yet implemented");
	}

	public void testPostStringString() {
		fail("Not yet implemented");
	}

}
