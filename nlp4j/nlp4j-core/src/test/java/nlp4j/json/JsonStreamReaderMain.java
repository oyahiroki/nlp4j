package nlp4j.json;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

import com.google.gson.JsonObject;

public class JsonStreamReaderMain {

	public static void main(String[] args) throws Exception {

		File file = new File("src/test/resources/nlp4j.json/JsonFileReaderTestCase.txt");

		try (Stream<JsonObject> stream = JsonStreamReader.stream(file)) {
			stream.forEach(jo -> {
				System.err.println(jo.toString());
			});
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
