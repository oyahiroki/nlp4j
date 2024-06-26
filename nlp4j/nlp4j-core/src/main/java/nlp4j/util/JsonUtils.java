package nlp4j.util;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 * @author Hiroki Oya
 * @since 1.2.1.0
 *
 */
public class JsonUtils {

	/**
	 * @param jo
	 * @return
	 * @since 1.3.7.13
	 */
	static public Properties asProperties(JsonObject jo) {
		Properties p = new Properties();
		Set<String> kk = jo.keySet();
		for (String k : kk) {
			JsonElement je = jo.get(k);
			if (je.isJsonNull() == false) {
				if (je.isJsonPrimitive() == true) {
					JsonPrimitive jp = je.getAsJsonPrimitive();
					if (jp.isString()) {
						String v = jp.getAsString();
						p.put(k, v);
					} //
					else if (jp.isNumber()) {
						String v = "" + jp.getAsNumber();
						p.put(k, v);
					} //
					else {
						String v = jp.toString();
						p.put(k, v);
					}
				} //
				else {
					String v = je.toString();
					p.put(k, v);
				}
			}
		}
		return p;
	}

	/**
	 * @param json
	 * @return
	 * @since 1.3.7.13
	 */
	static public Properties asProperties(String json) {
		JsonObject jo = (new Gson()).fromJson(json, JsonObject.class);
		return asProperties(jo);
	}

	/**
	 * @param json of JsonElement
	 * @return String in Pretty Print
	 * @since 1.3.1.0
	 */
	static public String prettyPrint(JsonElement json) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(json);
	}

	/**
	 * @param json of JsonObject
	 * @return String in Pretty Print
	 */
	static public String prettyPrint(JsonObject json) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(json);
	}

	/**
	 * @param json of String
	 * @return String in Pretty Print
	 */
	static public String prettyPrint(String json) {
		Gson gson = new Gson();
		JsonObject obj = gson.fromJson(json, JsonObject.class);
		return prettyPrint(obj);
	}

	/**
	 * @param list of String
	 * @return JsonArray of String
	 * @since 1.3.7.3
	 */
	static public JsonArray toJsonArray(List<String> list) {
		JsonArray arr = new JsonArray();
		if (list == null) {
			return null;
		} else {
			for (int n = 0; n < list.size(); n++) {
				arr.add(list.get(n));
			}
			return arr;
		}
	}

	/**
	 * @param outFile  the file to write
	 * @param jsonData the JSON content write to the file
	 * @throws IOException in case of an I/O error
	 */
	static public void write(File outFile, String jsonData) throws IOException {

		Gson gson = new Gson();

		// tab, new line is removed
		JsonObject json = gson.fromJson(jsonData, JsonObject.class);

		String encoding = "UTF-8";

		boolean append = true;

		if (outFile.getParentFile().exists() == false) {
			FileUtils.forceMkdir(outFile.getParentFile());
		}

		FileUtils.write(outFile, json.toString() + "\n", encoding, append);

	}

}
