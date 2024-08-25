package nlp4j.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.io.FileUtils;

import junit.framework.TestCase;

public class IOUtilsTestCase extends TestCase {

	/**
	 * created on: 2024-08-26
	 * 
	 * @throws Exception
	 */
	public void testBufferedReader001() throws Exception {
		File file = new File("src/test/resources/nlp4j.util/IOUtilsTest001.txt");
		try (BufferedReader br = IOUtils.bufferedReader(file)) {
			String s;
			while ((s = br.readLine()) != null) {
				System.err.println(s);
			}
		}
	}

	/**
	 * created on: 2024-08-26
	 * 
	 * @throws Exception
	 */
	public void testBufferedReader002() throws Exception {
		File file = new File("src/test/resources/nlp4j.util/IOUtilsTest001.txt.gz");
		try (BufferedReader br = IOUtils.bufferedReader(file)) {
			String s;
			while ((s = br.readLine()) != null) {
				System.err.println(s);
			}
		}
	}

	public void testPrintWriterFile() throws IOException {
		File tempFile = File.createTempFile("nlp4j-test", ".txt");
		try (PrintWriter pw = IOUtils.pw(tempFile);) {
			pw.println("OK");
		}
	}

	public void testPrintWriterFilePrintStream001() throws IOException {
		String data = "this is test " + System.currentTimeMillis();
		File tempFile = File.createTempFile("nlp4j-test", ".txt");
		System.err.println("temp_file_created: " + tempFile.getAbsolutePath());
		try (PrintWriter pw = IOUtils.printWriter(tempFile, System.err);) {
			pw.println(data);
		}
		String s_from_file = FileUtils.readFileToString(tempFile, "UTF-8").trim();
		System.err.println("Data from File: " + s_from_file);
		assertEquals(data, s_from_file);
	}

	public void testPrintWriterFilePrintStream002() throws IOException {
		String data = "日本語文字列 " + System.currentTimeMillis();
		;
		File tempFile = File.createTempFile("nlp4j-test", ".txt");
		System.err.println("temp_file_created: " + tempFile.getAbsolutePath());
		try (PrintWriter pw = IOUtils.printWriter(tempFile, System.err);) {
			pw.println(data);
		}
		String s_from_file = FileUtils.readFileToString(tempFile, "UTF-8").trim();
		System.err.println("Data from File: " + s_from_file);
		assertEquals(data, s_from_file);
	}

	public void testPW001() throws IOException {

		File tempFile = File.createTempFile("nlp4j-test", ".txt");
		try (PrintWriter pw = IOUtils.pw(tempFile, System.err);) {
			pw.println("OK");
		}
		String s = FileUtils.readFileToString(tempFile, "UTF-8");
		System.err.println("File: " + s);
	}

	public void testPrintWriterFileBooleanCharsetBoolean() {
	}

	public void testPrintWriterString() {
	}

	public void testPw() {
	}

	public void testPwSystemErr001() throws IOException {
		try (PrintWriter pw = IOUtils.pwSystemErr();) {
			pw.println("OK");
		}
	}

	public void testPwSystemOut001() throws IOException {
		try (PrintWriter pw = IOUtils.pwSystemOut();) {
			pw.println("OK");
		}
	}

}
