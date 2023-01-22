package nlp4j.util;

import java.io.BufferedReader;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import junit.framework.TestCase;

public class FileUtilsTestCase extends TestCase {

	public void testLineCount001() throws Exception {
		File file = new File("src/test/resources/nlp4j.util/FileUtilsTest.txt");
		long count = FileUtils.lineCount(file);
		System.err.println(count);
	}

	public void testLineCountGZipTextFile() throws Exception {
		File file = new File("src/test/resources/nlp4j.util/FileUtilsTest.txt.gz");
		long count = FileUtils.lineCount(file);
		System.err.println(count);
	}

	public void testOpenTextFileAsBufferedReaderFile001() throws Exception {
		File file = new File("src/test/resources/nlp4j.util/FileUtilsTest.txt.gz");
		BufferedReader br = FileUtils.openTextFileAsBufferedReader(file);
		String line;
		while ((line = br.readLine()) != null) {
			System.err.println(line);
		}
	}

	public void testOpenTextFileAsBufferedReaderFile002() throws Exception {
		File file = new File("src/test/resources/nlp4j.util/FileUtilsTest.txt");
		BufferedReader br = FileUtils.openTextFileAsBufferedReader(file);
		String line;
		while ((line = br.readLine()) != null) {
			System.err.println(line);
		}
	}

	public void testOpenTextFileAsBufferedReaderFileCharset001() throws Exception {
		File file = new File("src/test/resources/nlp4j.util/FileUtilsTest.txt.gz");
		BufferedReader br = FileUtils.openTextFileAsBufferedReader(file, StandardCharsets.UTF_8);
		String line;
		while ((line = br.readLine()) != null) {
			System.err.println(line);
		}
	}

	public void testOpenTextFileAsBufferedReaderFileCharset002() throws Exception {
		File file = new File("src/test/resources/nlp4j.util/FileUtilsTest.txt");
		BufferedReader br = FileUtils.openTextFileAsBufferedReader(file, StandardCharsets.UTF_8);
		String line;
		while ((line = br.readLine()) != null) {
			System.err.println(line);
		}
	}

	public void testOpenTextFileAsBufferedReaderFileCharset003() throws Exception {
		File file = new File("src/test/resources/nlp4j.util/FileUtilsTest.txt");
		BufferedReader br = FileUtils.openTextFileAsBufferedReader(file, Charset.forName("UTF-8"));
		String line;
		while ((line = br.readLine()) != null) {
			System.err.println(line);
		}
	}

}
