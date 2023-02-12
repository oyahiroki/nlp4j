package nlp4j.json;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * <pre>
 * Read New Line Separated JSON TEXT FILE
 * 改行区切りのJSONファイルを読み込む
 * </pre>
 * 
 * created on: 2022-11-19
 * 
 * @author Hiroki Oya
 * @since 1.3.7.4
 *
 */
public class JsonFileReader {

	BufferedReader br;

	public JsonFileReader(File jsonFile) throws IOException {

		if (jsonFile.exists() == false) {
			throw new FileNotFoundException(jsonFile.getAbsolutePath());
		}

		if (jsonFile.getAbsolutePath().endsWith(".gz")) {
			br = new BufferedReader(
					new InputStreamReader(new GZIPInputStream(new FileInputStream(jsonFile)), StandardCharsets.UTF_8));
		} else {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(jsonFile), StandardCharsets.UTF_8));
		}

	}

	public JsonObject next() throws IOException {

		String line = br.readLine();

		if (line == null) {
			return null;
		} else {
			JsonObject jsonObject = (new Gson()).fromJson(line, JsonObject.class);
			return jsonObject;
		}

	}

}
