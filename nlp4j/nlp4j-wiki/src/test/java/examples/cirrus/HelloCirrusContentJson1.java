package examples.cirrus;

import java.io.File;

import org.apache.commons.io.FileUtils;

import com.google.gson.JsonObject;

import nlp4j.json.JsonFileReader;
import nlp4j.util.JsonObjectUtils;
import nlp4j.util.JsonUtils;

public class HelloCirrusContentJson1 {

	public static void main(String[] args) throws Exception {
		File jsonFile = new File(
				"D:/local/wiki/other/cirrussearch/20240902/jawiki-20240902-cirrussearch-content.json.gz");
		try (JsonFileReader reader = new JsonFileReader(jsonFile, 2);) {
			JsonObject jo;
			while ((jo = reader.next()) != null) {
				File tempFile = File.createTempFile("nlp4j-", ".txt");
				System.err.println(tempFile.getAbsolutePath());
				FileUtils.write(tempFile, JsonUtils.prettyPrint(jo), "UTF-8", false);
//				System.err.println(JsonUtils.prettyPrint(jo));
			}
		}
	}

}
