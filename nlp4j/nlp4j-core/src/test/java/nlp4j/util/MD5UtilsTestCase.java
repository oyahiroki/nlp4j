package nlp4j.util;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;

/**
 * Created on: 2023-02-12
 */
public class MD5UtilsTestCase extends TestCase {

	public void testMd5String() throws IOException {
		String expected = "5e2990ed671335c9d420dde0d6923b83";
		String filename = "src/test/resources/nlp4j.util/md5test.txt";
		String md5 = MD5Utils.md5(filename);
		System.err.println(md5);
		assertEquals(expected, md5);
	}

	public void testMd5File() throws IOException {
		String expected = "5e2990ed671335c9d420dde0d6923b83";
		File file = new File("src/test/resources/nlp4j.util/md5test.txt");
		String md5 = MD5Utils.md5(file);
		System.err.println(md5);
		assertEquals(expected, md5);
	}

}
