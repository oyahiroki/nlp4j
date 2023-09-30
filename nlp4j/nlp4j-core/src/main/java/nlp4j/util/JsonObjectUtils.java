package nlp4j.util;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @author Hiroki Oya
 * @since 1.3.7.5
 */
public class JsonObjectUtils {

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	/**
	 * @param json
	 * @return
	 * @since 1.3.7.9
	 */
	static public JsonObject fromJson(String json) {
		return (new Gson()).fromJson(json, JsonObject.class);
	}

	/**
	 * created on: 2023-08-15
	 * 
	 * @param jo         that contains 2D Array of String
	 * @param memberName
	 * @since 1.3.7.10
	 * @return as List of String
	 */
	static public List<String> get2DArrayAsList(JsonObject jo, String memberName) {
		List<String> ss = new ArrayList<>();
		JsonArray arr1 = (jo.get(memberName) != null && jo.get(memberName).isJsonArray())
				? jo.get(memberName).getAsJsonArray()
				: null;
		if (arr1 != null) {
			for (int n = 0; n < arr1.size(); n++) {
				ss.addAll(getAsList(arr1.get(n).getAsJsonArray()));
			}
			return ss;
		} else {
//			logger.warn("size=0, member=" + memberName);
			return new ArrayList<>();
		}
	}

	/**
	 * @param arr
	 * @return
	 * @since 1.3.7.12
	 */
	static public List<String> getAsList(JsonArray arr) {
		List<String> ss = new ArrayList<>(arr.size());
		for (int n = 0; n < arr.size(); n++) {
			if (arr.get(n).isJsonPrimitive()) {
				ss.add(arr.get(n).getAsString());
			}
		}
		return ss;
	}

	/**
	 * @param jo                    Target Object
	 * @param memberNameOfJsonArray
	 * @return as List of String, Not null
	 */
	static public List<String> getAsList(JsonObject jo, String memberNameOfJsonArray) {
		JsonArray arr = (jo.get(memberNameOfJsonArray) != null && jo.get(memberNameOfJsonArray).isJsonArray())
				? jo.get(memberNameOfJsonArray).getAsJsonArray()
				: null;
		if (arr != null) {
			List<String> ss = new ArrayList<>(arr.size());
			for (int n = 0; n < arr.size(); n++) {
				ss.add(arr.get(n).getAsString());
			}
			return ss;
		} else {
//			logger.warn("size=0, member=" + memberNameOfJsonArray);
			return new ArrayList<>();
		}
	}

	public static boolean isTrue(JsonObject jo, String string) {

		return jo.get(string) != null //
				&& jo.get(string).isJsonPrimitive() //
				&& (jo.get(string).getAsBoolean() == true);
	}

	/**
	 * @param arr to be sorted
	 * @param key to sort
	 */
	static public void sortByNumberDesc(JsonArray arr, String key) {

		if (arr == null || arr.size() == 0 || arr.size() == 1) {
			return;
		}

		if (arr.get(0).getAsJsonObject() == null || arr.get(0).getAsJsonObject().get(key) == null) {
			return;
		}

		List<JsonElement> arr2 = new ArrayList<>();

		for (int n = 0; n < arr.size(); n++) {
			arr2.add(n, arr.get(n));
		}

		Collections.sort(arr2, (e1, e2) -> {
			if (((JsonElement) e1).getAsJsonObject().get(key) == null) {
				return 0;
			}
			if (((JsonElement) e2).getAsJsonObject().get(key) == null) {
				return 0;
			}
			Number n1 = ((JsonElement) e1).getAsJsonObject().get(key).getAsNumber();
			Number n2 = ((JsonElement) e2).getAsJsonObject().get(key).getAsNumber();

			if (n2.doubleValue() == n1.doubleValue()) {
				return 0;
			} else {
				return ((n2.doubleValue() - n1.doubleValue()) > 0) ? 1 : -1;
			}

		});

		for (int n = 0; n < arr2.size(); n++) {
			arr.set(n, arr2.get(n));
		}

	}

	/**
	 * created on 2023-01-19
	 * 
	 * @param list
	 * @return
	 */
	static public JsonArray toJsonArray(List<String> list) {
		if (list == null) {
			return null;
		} else {
			JsonArray arr = new JsonArray();
			for (String s : list) {
				arr.add(s);
			}
			return arr;
		}
	}

	/**
	 * created on 2023-09-13
	 * 
	 * @param ss
	 * @return
	 * @since 1.3.7.12
	 */
	static public JsonArray toJsonArray(Set<String> ss) {
		if (ss == null) {
			return null;
		} else {
			JsonArray arr = new JsonArray();
			for (String s : ss) {
				arr.add(s);
			}
			return arr;
		}
	}

	/**
	 * created on 2023-09-17
	 * 
	 * @param ss
	 * @return
	 * @since 1.3.7.12
	 */
	public static JsonArray toJsonArray(String[] ss) {
		if (ss == null) {
			return null;
		} else {
			JsonArray arr = new JsonArray();
			for (String s : ss) {
				arr.add(s);
			}
			return arr;
		}

	}

}
