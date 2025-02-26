package nlp4j.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 * @author Hiroki Oya
 * @since 1.3.7.5
 */
public class JsonObjectUtils {

	/**
	 * @param json
	 * @return
	 * @since 1.3.7.9
	 * @throws com.google.gson.JsonSyntaxException
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

	public static String join(String string, JsonArray arr) {
		List<String> list = new ArrayList<>();
		for (int n = 0; n < arr.size(); n++) {
			list.add(arr.get(n).getAsString());
		}
		return String.join(string, list);
	}

	/**
	 * @param string
	 * @param arr
	 * @return
	 */
	public static String join_unique(String string, JsonArray arr) {
		List<String> list = new ArrayList<>();
		for (int n = 0; n < arr.size(); n++) {
			String s = arr.get(n).getAsString();
			if (list.contains(s) == false) {
				list.add(s);
			}
		}
		return String.join(string, list);
	}

	/**
	 * Executes a JSONPath query on the given JsonObject and returns the result as
	 * an object of the specified type. This method allows for querying nested JSON
	 * structures using a path expression. The query expression should be a JSONPath
	 * string that specifies the location of the desired data within the JSON
	 * object.
	 *
	 * @param jo       The JsonObject on which the query is to be executed.
	 * @param classOfT The class type of the result. This is used to cast the
	 *                 returned data to the expected type.
	 * @param path     The JSONPath string that specifies the query. It should be
	 *                 formatted as a path with components separated by slashes
	 *                 ('/'), and array elements can be accessed using square
	 *                 brackets ('[]').
	 * @return Returns an object of type T containing the data found at the
	 *         specified path in the JSON structure. Returns null if the specified
	 *         path is not found or if the data at the path is not of the expected
	 *         type.
	 * @throws IllegalArgumentException if the provided path is invalid or if errors
	 *                                  occur during the query execution.
	 * @since 1.3.7.13
	 */

	@SuppressWarnings("unchecked")
	public static <T> T query(JsonObject jo, Class<T> classOfT, String path) {
		String[] pp = path.split("(/|\\[)");
		JsonElement ptr = jo;
		// FOR_EACH(PATH)
		for (int idx_path = 0; idx_path < pp.length; idx_path++) {
			String key = pp[idx_path];

			if (key.isEmpty()) {
				continue;
			}

			if (key.endsWith("]")) {
				String k = key.substring(0, key.length() - 1);
				if (org.apache.commons.lang3.StringUtils.isNumeric(k)) {
					int idx = Integer.parseInt(k);
					if (idx < 0 || idx >= ((JsonArray) ptr).size()) {
						return null;
					}
					JsonElement e = ((JsonArray) ptr).get(idx);
					ptr = e;
				}
				//
				else {
					JsonElement elm = ((JsonObject) ptr).get(key);
					// IF(elm is JsonArray)
					if (elm instanceof JsonArray) {
						ptr = elm.getAsJsonArray();
					} //
						// ELSE_IF(elm is JsonObject)
					else if (elm instanceof JsonObject) {
						ptr = elm.getAsJsonObject();
					} //
						// ELSE
					else {
						ptr = elm;
					} // END_OF_IF
				}
			}
			// ELSE_IF(key is String)
			else {
				JsonElement elm = ((JsonObject) ptr).get(key);
				// IF(elm is JsonArray)
				if (elm instanceof JsonArray) {
					ptr = elm.getAsJsonArray();
				} //
					// ELSE_IF(elm is JsonObject)
				else if (elm instanceof JsonObject) {
					ptr = elm.getAsJsonObject();
				} //
					// ELSE
				else {
					ptr = elm;
				} // END_OF_IF
			} // END_OF_IF

			// IF(path is last)
			if (idx_path == (pp.length - 1)) {
				// System.err.println("**");
				if (ptr == null) {
					return null;
				}
				// IF(ptr is Primitive)
				if (ptr.isJsonPrimitive()) {
					JsonPrimitive jp = ptr.getAsJsonPrimitive();
					if (jp.isString()) {
						String s = ptr.getAsString();
						return (T) s;
					} //
					else if (jp.isNumber()) {
						// T is String
						if (classOfT == String.class) {
							String s = ptr.getAsString();
							return (T) s;
						} //
							// T is Integer
						else if (classOfT == Integer.class) {
							Integer v = ptr.getAsInt();
							return (T) v;
						} //
							// T is Long
						else if (classOfT == Long.class) {
							Long v = ptr.getAsLong();
							return (T) v;
						} //
							// T is Double
						else if (classOfT == Double.class) {
							Double v = ptr.getAsDouble();
							return (T) v;
						} //
							// ELSE
						else {
							Number n = ptr.getAsNumber();
							return (T) n;
						}
					}
				} //
					// ELSE_IF(ptr is JsonArray)
				else if (ptr.isJsonArray()) {
					JsonArray o = ptr.getAsJsonArray();
					return (T) o;
				} //
					// ELSE_IF(ptr is JsonObject)
				else if (ptr.isJsonObject()) {
					JsonObject o = ptr.getAsJsonObject();
					return (T) o;
				} //
					// ELSE(ptr is Null)
				else if (ptr.isJsonNull()) {
					return null;
				}
				// ELSE
				else {
					return null;
				} // END_OF_IF
			} // END_OF_IF
		} // END_OF_FOR_EACH(PATH)
		return null;
	} // END_OF_METHOD

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
	 * created_on: 2025-2-26
	 * 
	 * @param s
	 * @return
	 * @since 1.3.7.16
	 */
	static public JsonArray toJsonArray(String s) {
		if (s == null) {
			return null;
		} else {
			JsonArray arr = new JsonArray();
			arr.add(s);
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
