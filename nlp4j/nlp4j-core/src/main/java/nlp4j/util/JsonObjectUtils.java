package nlp4j.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @author Hiroki Oya
 * @since 1.3.7.5
 */
public class JsonObjectUtils {

	/**
	 * @param json
	 * @return
	 * @since 1.3.7.9
	 */
	static public JsonObject fromJson(String json) {
		return (new Gson()).fromJson(json, JsonObject.class);
	}

	/**
	 * @param jsonObject Target Object
	 * @param memberName
	 * @return as List of String, Not null
	 */
	static public List<String> getAsList(JsonObject jsonObject, String memberName) {
		JsonArray arr = (jsonObject.get(memberName) != null) ? jsonObject.get(memberName).getAsJsonArray() : null;
		if (arr != null) {
			List<String> ss = new ArrayList<>(arr.size());
			for (int n = 0; n < arr.size(); n++) {
				ss.add(arr.get(n).getAsString());
			}
			return ss;
		} else {
			return new ArrayList<>();
		}
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

}
