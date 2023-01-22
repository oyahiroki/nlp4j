package nlp4j.util;

import java.util.List;

import com.google.gson.JsonArray;

/**
 * @author Hiroki Oya
 * @since 1.3.7.5
 */
public class JsonObjectUtils {

	/**
	 * created_at : 2023-01-19
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
