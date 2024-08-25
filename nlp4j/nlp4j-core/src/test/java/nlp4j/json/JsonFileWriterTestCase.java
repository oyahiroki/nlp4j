package nlp4j.json;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import junit.framework.TestCase;
import nlp4j.Document;
import nlp4j.DocumentBuilder;

/**
 * @since 1.3.7.14
 */
public class JsonFileWriterTestCase extends TestCase {

	public void testWrite001() throws Exception {
		List<Document> docs = new ArrayList<Document>();
		{
			docs.add((new DocumentBuilder()).text("This is test 001").build());
			docs.add((new DocumentBuilder()).text("This is test 002").build());
			docs.add((new DocumentBuilder()).text("This is test 003").build());
		}

		File tempFile = File.createTempFile("nlp4j-JsonFileWriterTestCase", ".txt");

		JsonFileWriter.write(tempFile, docs);

		System.out.println(tempFile.getAbsolutePath());

		String s = FileUtils.readFileToString(tempFile, "UTF-8");

		String expected = "{\"text\":\"This is test 001\",\"keywords\":[]}\r\n"//
				+ "{\"text\":\"This is test 002\",\"keywords\":[]}\r\n"//
				+ "{\"text\":\"This is test 003\",\"keywords\":[]}\r\n" //
				+ ""; //

		assertEquals(expected, s);
		tempFile.deleteOnExit();
	}

	public void testWrite002() throws Exception {
		List<Document> docs = new ArrayList<Document>();
		for (int n = 0; n < 10; n++) {
			Document doc = (new DocumentBuilder()).text("This is test " + n).build();
			float[] vector = { 1.0f, 0.0f };
			doc.putAttribute("vector", vector);
			docs.add(doc);
		}

		File tempFile = File.createTempFile("nlp4j-JsonFileWriterTestCase", ".txt");

		JsonFileWriter.write(tempFile, docs);

		System.out.println(tempFile.getAbsolutePath());

		String s = FileUtils.readFileToString(tempFile, "UTF-8");

		String expected = "{\"text\":\"This is test 0\",\"vector\":[1.0,0.0],\"keywords\":[]}\r\n" //
				+ "{\"text\":\"This is test 1\",\"vector\":[1.0,0.0],\"keywords\":[]}\r\n" //
				+ "{\"text\":\"This is test 2\",\"vector\":[1.0,0.0],\"keywords\":[]}\r\n" //
				+ "{\"text\":\"This is test 3\",\"vector\":[1.0,0.0],\"keywords\":[]}\r\n" //
				+ "{\"text\":\"This is test 4\",\"vector\":[1.0,0.0],\"keywords\":[]}\r\n" //
				+ "{\"text\":\"This is test 5\",\"vector\":[1.0,0.0],\"keywords\":[]}\r\n" //
				+ "{\"text\":\"This is test 6\",\"vector\":[1.0,0.0],\"keywords\":[]}\r\n" //
				+ "{\"text\":\"This is test 7\",\"vector\":[1.0,0.0],\"keywords\":[]}\r\n" //
				+ "{\"text\":\"This is test 8\",\"vector\":[1.0,0.0],\"keywords\":[]}\r\n" //
				+ "{\"text\":\"This is test 9\",\"vector\":[1.0,0.0],\"keywords\":[]}\r\n" //
				+ ""; //

		assertEquals(expected, s);
		tempFile.deleteOnExit();
	}

}
