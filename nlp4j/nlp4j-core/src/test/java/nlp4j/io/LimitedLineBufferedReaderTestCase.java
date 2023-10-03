package nlp4j.io;

import java.io.File;
import java.io.FileReader;

import junit.framework.TestCase;

/**
 * Created on: 2023-10-03
 * 
 * @author Hiroki Oya
 * 
 *
 */
public class LimitedLineBufferedReaderTestCase extends TestCase {

	public void testReadLine001() throws Exception {
		LimitedLineBufferedReader reader = new LimitedLineBufferedReader(
				new FileReader(new File("src/test/resources/nlp4j.io/test.txt")), 3);
		String s;
		int count = 0;
		int count_expected = 3;
		while ((s = reader.readLine()) != null) {
			count++;
			System.err.println(s);
		}
		assertEquals(count_expected, count);
	}

	public void testReadLine002() throws Exception {
		LimitedLineBufferedReader reader = new LimitedLineBufferedReader(
				new FileReader(new File("src/test/resources/nlp4j.io/test.txt")), 0);
		String s;
		int count = 0;
		int count_expected = 0;
		while ((s = reader.readLine()) != null) {
			count++;
			System.err.println(s);
		}
		assertEquals(count_expected, count);
	}

	public void testReadLine003() throws Exception {
		LimitedLineBufferedReader reader = new LimitedLineBufferedReader(
				new FileReader(new File("src/test/resources/nlp4j.io/test.txt")), 100);
		String s;
		int count = 0;
		int count_expected = 10;
		while ((s = reader.readLine()) != null) {
			count++;
			System.err.println(s);
		}
		assertEquals(count_expected, count);
	}

}
