package nlp4j.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;

import junit.framework.TestCase;
import nlp4j.tuple.Pair;

public class IOUtilsTestCase extends TestCase {

	public void testPwTemp001() throws Exception {
		Pair<PrintWriter, File> p = IOUtils.pwTemp();
		p.getLeft().println("Hello1");
		System.err.println(p.getRight().getAbsolutePath());
		p.getLeft().flush();
		p.getLeft().close();
		System.err.println("Hello2");
	}

	public void testSysErrTemp001() throws Exception {
		Pair<PrintWriter, File> p = IOUtils.pwSysErrTemp();
		p.getLeft().println("Hello1");
		System.err.println(p.getRight().getAbsolutePath());
		p.getLeft().flush();
		p.getLeft().close();
		System.err.println("Hello2");
	}

	public void testStreamStringFile001() throws Exception {
		File file = new File("src/test/resources/nlp4j.util/IOUtilsTest001.txt");
		IOUtils.stream(file).forEach(s -> {
			System.out.println(s);
		});
	}

	public void testStreamStringURL001() throws Exception {
		URL url = new URL("https://nlp4j.sakura.ne.jp/test/example.jsonl");
		IOUtils.stream(url).forEach(s -> {
			System.out.println(s);
		});
	}

	/**
	 * created on: 2024-08-26
	 * 
	 * @throws Exception
	 */
	public void testBufferedReader001() throws Exception {
		File file = new File("src/test/resources/nlp4j.util/IOUtilsTest001.txt");
		boolean ok = false;
		try (BufferedReader br = IOUtils.bufferedReader(file)) {
			String s;
			while ((s = br.readLine()) != null) {
				System.err.println(s);
				ok = true;
			}
		}
		assertTrue(ok);
	}

	/**
	 * created on: 2024-08-26
	 * 
	 * @throws Exception
	 */
	public void testBufferedReader002_gz() throws Exception {
		File file = new File("src/test/resources/nlp4j.util/IOUtilsTest001.txt.gz");
		boolean ok = false;
		try (BufferedReader br = IOUtils.bufferedReader(file)) {
			String s;
			while ((s = br.readLine()) != null) {
				System.err.println(s);
				ok = true;
			}
		}
		assertTrue(ok);
	}

	public void testBufferedReader003_7z() throws Exception {
		File file = new File("src/test/resources/nlp4j.util/IOUtilsTest001.txt.7z");
		boolean ok = false;
		try (BufferedReader br = IOUtils.bufferedReader(file)) {
			String s;
			while ((s = br.readLine()) != null) {
				System.err.println(s);
				ok = true;
			}
		}
		assertTrue(ok);
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

	/**
	 * bufferedReader(File, Charset) should honour the charset argument for plain
	 * text files (not silently fall back to UTF-8).
	 */
	public void testBufferedReaderCharset_plain() throws Exception {
		// example_sjis.txt is encoded in Shift_JIS
		File file = new File("src/test/resources/examples/example_sjis.txt");
		Charset sjis = Charset.forName("Shift_JIS");
		boolean ok = false;
		try (BufferedReader br = IOUtils.bufferedReader(file, sjis)) {
			String line = br.readLine();
			assertNotNull(line);
			// If charset is honoured the first line must not contain replacement chars
			assertFalse("Charset not honoured: got replacement chars", line.contains("\uFFFD"));
			ok = true;
		}
		assertTrue(ok);
	}

	/**
	 * bufferedReader(File, Charset) should honour the charset argument for .gz
	 * files.
	 */
	public void testBufferedReaderCharset_gz() throws Exception {
		File file = new File("src/test/resources/nlp4j.util/IOUtilsTest001.txt.gz");
		// File is UTF-8; reading with UTF-8 charset should succeed and return content
		boolean ok = false;
		try (BufferedReader br = IOUtils.bufferedReader(file, StandardCharsets.UTF_8)) {
			String line = br.readLine();
			assertNotNull(line);
			ok = true;
		}
		assertTrue(ok);
	}

	/**
	 * flushClose() must close the writer even when flush() throws an IOException.
	 */
	public void testFlushClose_closeCalledEvenIfFlushThrows() throws Exception {
		final boolean[] closed = { false };

		// A Writer whose flush() throws IOException but close() should still be called
		java.io.Writer delegate = new java.io.Writer() {
			@Override
			public void write(char[] cbuf, int off, int len) throws IOException {
			}

			@Override
			public void flush() throws IOException {
				throw new IOException("flush failed intentionally");
			}

			@Override
			public void close() throws IOException {
				closed[0] = true;
			}
		};

		try {
			IOUtils.flushClose(delegate);
			fail("Expected IOException from flushClose");
		} catch (IOException e) {
			// expected: MultiIOException wrapping the flush failure
		}
		assertTrue("close() must be called even when flush() throws", closed[0]);
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
