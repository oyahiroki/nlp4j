package nlp4j.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.io.FileUtils;

import junit.framework.TestCase;

public class IOUtilsTestCase extends TestCase {

	public void testPrintWriterFile() throws IOException {
		File tempFile = File.createTempFile("nlp4j-test", ".txt");
		try (PrintWriter pw = IOUtils.pw(tempFile);) {
			pw.println("OK");
		}
	}

	public void testPrintWriterFilePrintStream001() throws IOException {

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
