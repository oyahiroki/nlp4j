package nlp4j.json;

import java.io.File;

import com.google.gson.JsonObject;

import junit.framework.TestCase;

/**
 * created on: 2023-05-02
 * 
 * @author Hiroki Oya
 *
 */
public class JsonFileReaderTestCase extends TestCase {

	public void testJsonFileReader001() throws Exception {
		File jsonFile = new File("src/test/resources/nlp4j.json/JsonFileReaderTestCase.txt");
		try (JsonFileReader reader = new JsonFileReader(jsonFile);) {
			JsonObject jo;
			while ((jo = reader.next()) != null) {
				System.err.println(jo.toString());
			}
		}
	}

	public void testJsonFileReader001b() throws Exception {
		File jsonFile = new File("src/test/resources/nlp4j.json/JsonFileReaderTestCase2.txt");
		try (JsonFileReader reader = new JsonFileReader(jsonFile);) {
			JsonObject jo;
			while ((jo = reader.next()) != null) {
				System.err.println(jo.toString());
			}
		}
	}

	public void testJsonFileReader002_readGZip() throws Exception {
		File jsonFile = new File("src/test/resources/nlp4j.json/JsonFileReaderTestCase.txt.gz");
		try (JsonFileReader reader = new JsonFileReader(jsonFile);) {
			JsonObject jo;
			while ((jo = reader.next()) != null) {
				System.err.println(jo.toString());
			}
		}
	}

	public void testJsonFileReader003a_Stream() throws Exception {
		File jsonFile = new File("src/test/resources/nlp4j.json/JsonFileReaderTestCase.txt.gz");
		try (JsonFileReader reader = new JsonFileReader(jsonFile);) {

			reader.jsonObjectStream().forEach(jo -> {
				System.err.println(jo);
			});

		}
	}

	public void testJsonFileReader003b_Stream() throws Exception {
		File jsonFile = new File("src/test/resources/nlp4j.json/JsonFileReaderTestCase.txt.gz");
		try (JsonFileReader reader = new JsonFileReader(jsonFile, 2);) {

			reader.jsonObjectStream().forEach(jo -> {
				System.err.println(jo);
			});

		}
	}

	public void testNext() {
//		fail("Not yet implemented");
	}

	public void testClose() {
//		fail("Not yet implemented");
	}

}
