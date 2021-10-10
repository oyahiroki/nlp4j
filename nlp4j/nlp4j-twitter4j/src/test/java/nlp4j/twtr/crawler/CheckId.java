package nlp4j.twtr.crawler;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class CheckId {

	public static void main(String[] args) throws Exception {

		long maxId = -1;

		File jsonFile = new File("/usr/local/data/twitter/searchresponse_json.txt");
//		File jsonFile = new File("src/test/resources/nlp4j.twtr.crawler/test_json.txt");

		List<String> lines = FileUtils.readLines(jsonFile, "UTF-8");

		for (String line : lines) {
			Gson gson = new Gson();

			System.err.println(line);

			JsonObject tweet = gson.fromJson(line, JsonObject.class);

			long id = tweet.get("id").getAsLong();

			System.err.println(id);

			if (id > maxId) {
				maxId = id;
			}

		}

		System.err.println("maxId : " + maxId);

	}

}
