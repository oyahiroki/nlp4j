package nlp4j.json;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import nlp4j.util.FileUtils;

public class JsonStreamReader {

	public static Stream<JsonObject> stream(File file) throws IOException {
		BufferedReader br = FileUtils.openTextFileAsBufferedReader(file, "UTF-8");

		return br.lines().map(line -> {
			try {
				return (new Gson()).fromJson(line, JsonObject.class);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}

		}).onClose(() -> {
			try {
				br.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});
	}

}
