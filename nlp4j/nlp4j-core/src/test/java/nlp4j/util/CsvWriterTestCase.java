package nlp4j.util;

import java.io.File;

import junit.framework.TestCase;

public class CsvWriterTestCase extends TestCase {

	/**
	 * @throws Exception
	 * @since 1.3.7.10
	 */
	public void testWrite001() throws Exception {
		File tempFile = File.createTempFile("nlp4j_test_csvwriter_", ".csv");
		System.err.println(tempFile.getAbsolutePath());

		try (CsvWriter csvWriter = new CsvWriter(tempFile);) {
			csvWriter.write("head1", "head2", "head3");
			for (int n = 1; n <= 10; n++) {
				csvWriter.write("data" + n + "-1\t\n,\"テスト\"", "data" + n + "-2", "data" + n + "-3");
			}
		}

	}

}
