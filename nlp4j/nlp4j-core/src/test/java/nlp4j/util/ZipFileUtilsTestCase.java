package nlp4j.util;

import java.io.File;

import junit.framework.TestCase;

public class ZipFileUtilsTestCase extends TestCase {

	public void testExtract() throws Exception {
		File zipFile = new File("src/test/resources/nlp4j.util/a.zip");
		File outFile = File.createTempFile("xxx", ".txt");

		ZipFileUtils.extract(zipFile, "MS932", "a/b.txt", outFile);

		System.err.println(zipFile.getAbsolutePath());
		System.err.println(outFile.getAbsolutePath());

		long len = outFile.length();

		System.err.println(len);
	}

}
