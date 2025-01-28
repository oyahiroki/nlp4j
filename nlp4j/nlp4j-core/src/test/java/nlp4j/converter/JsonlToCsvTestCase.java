package nlp4j.converter;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;

public class JsonlToCsvTestCase extends TestCase {

	public void testConvert_001() throws IOException {
		File jsonl = new File("src/test/resources/nlp4j.converter/jsonlines_001.txt");
		System.err.println("jsonl: " + jsonl.getAbsolutePath());
		File csv = File.createTempFile("nlp4j_", ".csv");
		System.err.println("csv: " + csv.getAbsolutePath());
		Jsonl2Csv.convert(jsonl, csv);
	}

	public void testConvert_100() throws IOException {
		File jsonl = new File("src/test/resources/nlp4j.converter/jsonlines.txt");
		System.err.println("jsonl: " + jsonl.getAbsolutePath());
		File csv = File.createTempFile("nlp4j_", ".csv");
		System.err.println("csv: " + csv.getAbsolutePath());
		Jsonl2Csv.convert(jsonl, csv);
	}

}
