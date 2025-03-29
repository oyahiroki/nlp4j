package nlp4j.util;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;
import nlp4j.util.MD5Utils;

public class MD5UtilsTestCase extends TestCase {

	public void testMd5String() throws IOException {
		String filename = "src/test/resources/nlp4j.webcrawler.utils/md5test.txt";
		String md5 = MD5Utils.md5(filename);
		System.err.println(md5);
	}

	public void testMd5File() throws IOException {
		File file = new File("src/test/resources/nlp4j.webcrawler.utils/md5test.txt");
		String md5 = MD5Utils.md5(file);
		System.err.println(md5);
	}

}
