package nlp4j.util;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.apache.commons.io.FileUtils;
import org.apache.commons.text.StringEscapeUtils;

import junit.framework.TestCase;

public class JsonUtilsTestCase extends TestCase {
	
	public void testWrite() throws IOException {

		File file = new File("src/test/resources/nlp4j.util/test_out.txt");

		if (file.exists()) {
			file.delete();
			System.err.println("INFO: deleted: " + file.getAbsolutePath());
		}

		{
			String json = "{\n" //
					+ "'test':'test1'\n" //
					+ "}\n"; //

			JsonUtils.write(file, json);

		}
		{
			String json = "{\n" //
					+ "'test':'test2'\n" //
					+ "}\n"; //

			JsonUtils.write(file, json);

		}

		String actual = FileUtils.readFileToString(file, "UTF-8");

		System.err.println(actual);

		String expected = "{\"test\":\"test1\"}\n" + "{\"test\":\"test2\"}\n";

		assertEquals(expected, actual);

	}

	public void testPrettyPrintString001() {
		String json = "{\"text\":\"日本語\"}";
		System.err.println(JsonUtils.prettyPrint(json));
	}

	public void testPrettyPrintString003() throws Exception {
		String json = "{\"text\":\"\\u30a2\\u30f3\\u30d1\\u30b5\\u30f3\\u30c9\"}";
		System.err.println(json);
		System.err.println(JsonUtils.prettyPrint(json));
	}

}
