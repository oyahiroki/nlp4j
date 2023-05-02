package nlp4j.util;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;

public class PrintUtilsTestCase extends TestCase {

	public void testHeadJsonIntIntFile() throws IOException {
		String fileName = "src/test/resources/nlp4j.util/PrintUtils_json.txt";
		int n = 1;
		int skip = 0;
		File file = new File(fileName);
		PrintUtils.headJson(n, file, skip);
	}

	public void testHeadJsonIntIntString() throws IOException {
		String fileName = "src/test/resources/nlp4j.util/PrintUtils_json.txt";
		int n = 1;
		int skip = 1;
		PrintUtils.headJson(n, fileName, skip);
	}

	public void testHeadJsonIntString() throws IOException {
		String fileName = "src/test/resources/nlp4j.util/PrintUtils_json.txt";
		int n = 1;
		PrintUtils.headJson(n, fileName);
	}

	public void testHeadTextIntIntFile001() throws IOException {
		String fileName = "src/test/resources/nlp4j.util/PrintUtils.txt";
		int n = 1;
		int skip = 0;
		File file = new File(fileName);
		PrintUtils.headText(n, file, skip);
	}

	public void testHeadTextIntIntFile002() throws IOException {
		String fileName = "src/test/resources/nlp4j.util/PrintUtils.txt";
		int n = 3;
		int skip = 0;
		File file = new File(fileName);
		PrintUtils.headText(n, file, skip);
	}

	public void testHeadTextIntIntFile003() throws IOException {
		String fileName = "src/test/resources/nlp4j.util/PrintUtils.txt";
		int n = 10;
		int skip = 0;
		File file = new File(fileName);
		PrintUtils.headText(n, file, skip);
	}

}
