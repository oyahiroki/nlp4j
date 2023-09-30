package nlp4j.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import junit.framework.TestCase;

public class IOUtilsTestCase extends TestCase {

	public void testPrintWriterFile() throws IOException {
		File tempFile = File.createTempFile("nlp4j-test", ".txt");
		PrintWriter pw = IOUtils.pw(tempFile);
		pw.println("OK");

	}

	public void testPrintWriterFileBooleanCharsetBoolean() {
	}

	public void testPrintWriterString() {
	}

	public void testPw() {
	}

	public void testPwSystemErr001() throws IOException {
		PrintWriter pw = IOUtils.pwSystemErr();
		pw.println("OK");
	}

	public void testPwSystemOut001() throws IOException {
		PrintWriter pw = IOUtils.pwSystemOut();
		pw.println("OK");
	}

}
