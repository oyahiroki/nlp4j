package nlp4j.util;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import junit.framework.TestCase;

public class JsonObjectUtilsTestCase extends TestCase {

	public void test2DArrayAsList001() {
		JsonObject jo = new JsonObject();
		JsonArray arr = new JsonArray();
		{
			{
				JsonArray arr2 = new JsonArray();
				arr2.add("1-1");
				arr2.add("1-2");
				arr2.add("1-3");
				arr.add(arr2);
			}
			{
				JsonArray arr2 = new JsonArray();
				arr2.add("2-1");
				arr2.add("2-2");
				arr2.add("2-3");
				arr.add(arr2);
			}
			{
				JsonArray arr2 = new JsonArray();
				arr2.add("3-1");
				arr2.add("3-2");
				arr2.add("3-3");
				arr.add(arr2);
			}

		}
		jo.add("arr", arr);

		List<String> list = JsonObjectUtils.get2DArrayAsList(jo, "arr");

		list.forEach(s -> {
			System.err.println(s);
		});

		assertEquals("1-1", list.get(0));
		assertEquals("3-3", list.get(8));
	}

	public void testGetAsList001() {
		JsonObject jo = new JsonObject();
		JsonArray arr = new JsonArray();
		{
			arr.add("AAA");
			arr.add("BBB");
			arr.add("CCC");
		}
		jo.add("arr", arr);

		JsonObjectUtils.getAsList(jo, "arr").stream().forEach(a -> {
			System.err.println(a);
		});
	}

	public void testGetAsList002() {
		JsonObject jo = new JsonObject();
		jo.addProperty("arr", "AAA");

		JsonObjectUtils.getAsList(jo, "arr").stream().forEach(a -> {
			System.err.println(a);
		});

		// NullPointerException is not thrown
	}

	public void testIsTrue001() {
		JsonObject jo = new JsonObject();
		jo.addProperty("aaa", true);
		boolean b = JsonObjectUtils.isTrue(jo, "aaa");
		assertEquals(true, b);
	}

	public void testIsTrue002() {
		JsonObject jo = new JsonObject();
		jo.addProperty("aaa", false);
		boolean b = JsonObjectUtils.isTrue(jo, "aaa");
		assertEquals(false, b);
	}

	public void testIsTrue003() {
		JsonObject jo = new JsonObject();
		jo.addProperty("aaa", "xxx");
		boolean b = JsonObjectUtils.isTrue(jo, "aaa");
		assertEquals(false, b);
	}

	public void testIsTrue004() {
		JsonObject jo = new JsonObject();
		boolean b = JsonObjectUtils.isTrue(jo, "aaa");
		assertEquals(false, b);
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

	public void testToStringJsonObject001() throws Exception {
		String json = "{\"text\"=\"日本語\"}";
		Gson gson = new Gson();
		JsonObject jo = gson.fromJson(json, JsonObject.class);
		System.err.println(jo.toString());
	}

}
