package nlp4j.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;

import junit.framework.TestCase;

public class TextFileUtilsTestCase extends TestCase {

	public void testWc_l_001() throws Exception {
		File file = new File("src/test/resources/nlp4j.util/TextFileUtilsTest001.txt");
		int count = TextFileUtils.wc_l(file);
		System.err.println(count);
	}

	public void testHead001() throws Exception {
		File file = new File("src/test/resources/nlp4j.util/TextFileUtilsTest001.txt");
		TextFileUtils.head(5, file);
	}

	public void testHead501() throws Exception {
		try {
			File file = new File("src/test/resources/nlp4j.util/TextFileUtilsTest001.txtxx");
			TextFileUtils.head(5, file);
			fail();
		} catch (FileNotFoundException e) {
			System.out.println("OK");
		}
	}

	public void testOpenPlainTextFileAsBufferedReaderFile() {
//		fail("Not yet implemented");
	}

	public void testOpenPlainTextFileAsBufferedReaderFileString001() throws Exception {
		File file = new File("./src/test/resources/nlp4j.util/TextFileUtilsTestCase001.txt");
		BufferedReader br = TextFileUtils.openPlainTextFileAsBufferedReader(file, "UTF-8");
		String s;
		int line_count = 0;
		int expected_line_count = 3;
		while ((s = br.readLine()) != null) {
			line_count++;
			System.err.println(s);
		}
		assertEquals(expected_line_count, line_count);
	}

	public void testOpenPlainTextFileAsBufferedReaderURL001() throws Exception {
		URL url_of_file = new URL("https://nlp4j.org/");
		BufferedReader br = TextFileUtils.openPlainTextFileAsBufferedReader(url_of_file);
		String s;
		int line_count = 0;
		while ((s = br.readLine()) != null) {
			line_count++;
			System.err.println(s);
		}
		assertTrue(line_count > 0);
	}

	public void testOpenPlainTextFileAsBufferedReaderURL002() throws Exception {
		URL url_of_file = new URL("file:./src/test/resources/nlp4j.util/TextFileUtilsTestCase001.txt");
		BufferedReader br = TextFileUtils.openPlainTextFileAsBufferedReader(url_of_file);
		String s;
		int line_count = 0;
		int expected_line_count = 3;
		while ((s = br.readLine()) != null) {
			line_count++;
			System.err.println(s);
		}
		assertEquals(expected_line_count, line_count);
	}

	public void testSortLinesByValueFileFile() {
//		fail("Not yet implemented");
	}

	public void testSortLinesByValueFileStringFileStringBoolean() {
//		fail("Not yet implemented");
	}

	public void testWriteFileListOfString() {
//		fail("Not yet implemented");
	}

	public void testWriteFileListOfStringStringBoolean() {
//		fail("Not yet implemented");
	}

}
