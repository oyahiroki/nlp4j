package nlp4j.util;

import java.io.File;
import java.io.FileNotFoundException;

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

	public void testOpenPlainTextFileAsBufferedReaderFileString() {
//		fail("Not yet implemented");
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
