package nlp4j.json;

import java.io.File;
import java.io.IOException;

import com.google.gson.JsonObject;

public class JsonLinesGZipFileRead {

	public static void main(String[] args) throws IOException {
		File jsonFile = new File("src/main/resources/nlp4j/jsonlines.gz");
		try (JsonFileReader reader = new JsonFileReader(jsonFile);) {
			JsonObject jo;
			while ((jo = reader.next()) != null) {
				System.err.println(jo.toString());
				/*
				 * {"key1-1":"value1-1","key1-2":"value1-2","key1-3":"value1-3"}
				 * {"key2-1":"value2-1","key2-2":"value2-2","key2-3":"value2-3"}
				 * {"key3-1":"value3-1","key3-2":"value3-2","key3-3":"value3-3"}
				 * 
				 */
			}
		}

	}

}
