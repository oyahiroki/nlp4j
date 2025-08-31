package nlp4j.http;

import java.io.IOException;

import junit.framework.TestCase;

public class HttpUtilsTestCase extends TestCase {

	public void testisStatus200() throws Exception {
		String url = "https://nlp4j.org";
		boolean b = HttpUtils.isStatus200(url);
		boolean expected = true;
		assertEquals(expected, b);
	}

	public void testFetchImage101() throws Exception {
		String url = "https://nlp4j.org/wp-content/uploads/2021/09/nlp4j_680x680.png";
		Base64Response p = HttpUtils.fetchImageAsBase64(url);
		System.err.println("mime_type: " + p.getMimeType());
		System.err.println("data_size: " + p.getData().length());
	}

	public void testFetchImage501() throws Exception {
		String url = "https://nlp4j.org/";
		try {
			HttpUtils.fetchImageAsBase64(url);
		} catch (IOException e) {
			assertTrue(e.getMessage().contains("not"));
			return;
		}
		fail();
	}
}
