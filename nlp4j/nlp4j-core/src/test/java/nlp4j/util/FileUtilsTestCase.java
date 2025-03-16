package nlp4j.util;

import java.io.BufferedReader;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class FileUtilsTestCase extends TestCase {

	/**
	 * @throws Exception
	 * @since 1.3.7.18
	 */
	public void testStreamFile001() throws Exception {
		File jsonFile = new File("src/test/resources/nlp4j.util/FileUtilsTestCase_testStreamFile001.txt");
		FileUtils.stream(jsonFile).forEach(s -> {
			System.out.println(s);
		});
	}

	/**
	 * @throws Exception
	 * @since 1.3.7.18
	 */
	public void testStreamFile002() throws Exception {
		File jsonFile = new File("src/test/resources/nlp4j.util/FileUtilsTestCase_testStreamFile002.txt.gz");
		FileUtils.stream(jsonFile).forEach(s -> {
			System.out.println(s);
		});
	}

	/**
	 * Created on: 2025-3-2
	 * 
	 * @since 1.3.7.16
	 * @throws Exception
	 */
	public void testCheck001() throws Exception {
		File tempFile = File.createTempFile("nlp4j", ".temp");
		FileUtils.checExists(tempFile);
	}

	/**
	 * @since 1.3.7.12
	 * @throws Exception
	 */
	public void testJsonToCsv001() throws Exception {
		File jsonFile = new File("src/test/resources/nlp4j.util/json_utils_test_001_json.txt");
		File csvFile = File.createTempFile("json_utils_test_001_json", ".txt");
		System.err.println("from: " + jsonFile.getAbsolutePath());
		System.err.println("to: " + csvFile.getAbsolutePath());
		nlp4j.util.FileUtils.jsonToCsv(jsonFile, csvFile);
	}

	public void testConcatFiles001() throws Exception {

		List<File> fromFiles = new ArrayList<>();
		File toFile = File.createTempFile("text", ".txt");

		fromFiles.add(new File("src/test/resources/nlp4j.util/file1.txt"));
		fromFiles.add(new File("src/test/resources/nlp4j.util/file2.txt"));

		FileUtils.concat(fromFiles, toFile);

		System.err.println(toFile.getAbsolutePath());
	}

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
