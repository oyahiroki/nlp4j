package z_com.google.gson;

import com.google.gson.JsonObject;

import junit.framework.TestCase;

public class GsonTestCase extends TestCase {

	public void testGson001() throws Exception {
		// Gson が改行コードをどのように扱っているかの確認。
		String v = "aaa\nbbb\nccc";
		String expected = "{\"text\":\"aaa\\nbbb\\nccc\"}";

		JsonObject jo = new JsonObject();

		jo.addProperty("text", v);

		String v2 = (jo.toString());

		assertEquals(expected, v2);

	}

}
