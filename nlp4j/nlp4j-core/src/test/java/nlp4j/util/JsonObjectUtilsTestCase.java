package nlp4j.util;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import junit.framework.TestCase;

public class JsonObjectUtilsTestCase extends TestCase {

	public void testToJsonArray001() {
		List<String> ss = new ArrayList<>();
		ss.add("aaa");
		ss.add("bbb");
		ss.add("ccc");
		JsonArray arr = JsonObjectUtils.toJsonArray(ss);
		for (int n = 0; n < arr.size(); n++) {
			System.err.println(arr.get(n));
		}
	}

	public void testSortByNumberDesc001() {
		JsonArray arr = new JsonArray();
		{
			JsonObject o1 = new JsonObject();
			o1.addProperty("key", 2.0);
			o1.addProperty("name", "AAA");
			arr.add(o1);
		}
		{
			JsonObject o1 = new JsonObject();
			o1.addProperty("key", 1.0);
			o1.addProperty("name", "BBB");
			arr.add(o1);
		}
		{
			JsonObject o1 = new JsonObject();
			o1.addProperty("key", 3.0);
			o1.addProperty("name", "CCC");
			arr.add(o1);
		}

		System.err.println(arr.toString());

		JsonObjectUtils.sortByNumberDesc(arr, "key");

		System.err.println(arr.toString());

	}

	public void testSortByNumberDesc501() {
		JsonArray arr = new JsonArray();
		{
			JsonObject o1 = new JsonObject();
			o1.addProperty("key", 2.0);
			o1.addProperty("name", "AAA");
			arr.add(o1);
		}
		{
			JsonObject o1 = new JsonObject();
			o1.addProperty("key", 1.0);
			o1.addProperty("name", "BBB");
			arr.add(o1);
		}
		{
			JsonObject o1 = new JsonObject();
			o1.addProperty("key", 3.0);
			o1.addProperty("name", "CCC");
			arr.add(o1);
		}

		System.err.println(arr.toString());

		JsonObjectUtils.sortByNumberDesc(arr, "keyx");

		System.err.println(arr.toString());

	}

	public void testToStringJsonObject001() throws Exception {
		String json = "{\"text\"=\"日本語\"}";
		Gson gson = new Gson();
		JsonObject jo = gson.fromJson(json, JsonObject.class);
		System.err.println(jo.toString());
	}

}
