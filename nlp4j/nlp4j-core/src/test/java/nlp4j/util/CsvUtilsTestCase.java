package nlp4j.util;

import java.io.File;
import java.util.List;

import junit.framework.TestCase;

public class CsvUtilsTestCase extends TestCase {

	public void testReadCsv001() throws Exception {
		File csvFile = new File("files/nlp4j.util/test1.csv");
		List<List<String>> data = CsvUtils.readCsv(csvFile);
		for (List<String> d : data) {
			System.err.println(d);
		}
	}

	public void testReadCsv002() throws Exception {
		File csvFile = new File("files/nlp4j.util/test1_bom.csv");
		List<List<String>> data = CsvUtils.readCsv(csvFile);
		for (List<String> d : data) {
			System.err.println(d);
			for (String d0 : d) {
				System.err.print(d0.length() + ",");
			}
			System.err.println();
		}
	}

}
