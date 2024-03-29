package nlp4j.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import junit.framework.TestCase;

public class IncrementalDateFormatterTestCase extends TestCase {

	@SuppressWarnings("rawtypes")
	Class target = IncrementalDateFormatter.class;

	public void test001() throws Exception {

		List<String> expected = new ArrayList<>();
		expected.add("20240101-000000");
		expected.add("20240101-010000");
		expected.add("20240101-020000");
		expected.add("20240101-030000");

		IncrementalDateFormatter formatter = new IncrementalDateFormatter("yyyyMMdd-HHmmss", "20240101-000000",
				Calendar.HOUR_OF_DAY, 1, 3);
		String v;
		int idx = 0;
		while ((v = formatter.next()) != null) {
			System.err.println(v);
			assertEquals(expected.get(idx).trim(), v);
			idx++;
		}
	}

	public void test002() throws Exception {
		IncrementalDateFormatter formatter = new IncrementalDateFormatter("yyyyMMdd-HHmmss", "20240101-000000",
				Calendar.HOUR_OF_DAY, 3, 24);
		String v;
		while ((v = formatter.next()) != null) {
			System.err.println(v);
		}
	}

	public void test003() throws Exception {
		IncrementalDateFormatter formatter = new IncrementalDateFormatter("yyyyMMdd-HHmmss", "20240101-000000",
				Calendar.DAY_OF_MONTH, 7, 12);
		String v;
		while ((v = formatter.next()) != null) {
			System.err.println(v);
		}
	}

	public void test004() throws Exception {
		IncrementalDateFormatter formatter = new IncrementalDateFormatter("yyyyMMdd", "20231230", Calendar.DAY_OF_MONTH,
				1, 6);
		String v;
		while ((v = formatter.next()) != null) {
			System.err.println(v);
		}
	}

	public void test101() throws Exception {

		String url_base = "https://dumps.wikimedia.org/other/pageviews/";
//		String url = url_base + "2024/2024-01/pageviews-20240101-000000.gz";

		IncrementalDateFormatter formatter = new IncrementalDateFormatter(
				"yyyy/yyyy-MM/'pageviews'-yyyyMMdd-HHmmss'.gz'", "2024/2024-01/pageviews-20231231-150000.gz",
				Calendar.HOUR_OF_DAY, 1, 23);
		String v;
		while ((v = formatter.next()) != null) {
			System.err.println(url_base + v);
		}
	}

	public void test501() throws Exception {
		IncrementalDateFormatter formatter = new IncrementalDateFormatter("yyyyMMdd-HHmmss", "20240101-000000",
				Calendar.HOUR_OF_DAY, 1, -1);
		String v;
		while ((v = formatter.next()) != null) {
			System.err.println(v);
		}
	}

}
