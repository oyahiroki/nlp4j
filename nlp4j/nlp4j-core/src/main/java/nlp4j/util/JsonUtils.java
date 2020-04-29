package nlp4j.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

/**
 * @author Hiroki Oya
 * @since 1.2.1.0
 *
 */
public class JsonUtils {

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

	/**
	 * @param json
	 * @return String in Pretty Print
	 */
	static public String prettyPrint(JsonObject json) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(json);
	}

}
