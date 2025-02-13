package nlp4j.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import junit.framework.TestCase;

public class IncrementalDateFormatterTestCase extends TestCase {

	@SuppressWarnings("rawtypes")
	Class target = IncrementalDateFormatter.class;

	public void test001() throws Exception {
		// 2024/1/1 0:00 から 1時間追加を3回繰り返す
		List<String> expected = new ArrayList<>();
		expected.add("20240101-000000");
		expected.add("20240101-010000");
		expected.add("20240101-020000");
		expected.add("20240101-030000");

		String initialDate = "20240101-000000";
		int calendarUnit = Calendar.HOUR_OF_DAY;
		int amount = 1;
		int repeat = 3;

		IncrementalDateFormatter formatter = new IncrementalDateFormatter("yyyyMMdd-HHmmss", initialDate, calendarUnit,
				amount, repeat);
		String v;
		int idx = 0;
		while ((v = formatter.next()) != null) {
			System.err.println(v);
			assertEquals(expected.get(idx).trim(), v);
			idx++;
		}
	}

	public void test001b() throws Exception {
		// 2024/1/1 0:00 から 1時間追加を0回繰り返す
		List<String> expected = new ArrayList<>();
		expected.add("20240101-000000");

		String initialDate = "20240101-000000";
		int calendarUnit = Calendar.HOUR_OF_DAY;
		int amount = 1;
		int repeat = 0;

		IncrementalDateFormatter formatter = new IncrementalDateFormatter("yyyyMMdd-HHmmss", initialDate, calendarUnit,
				amount, repeat);
		String v;
		int idx = 0;
		while ((v = formatter.next()) != null) {
			System.err.println(v);
			assertEquals(expected.get(idx).trim(), v);
			idx++;
		}
	}

	public void test001c() throws Exception {
		// 2024/2/27 0:00 から 1日追加を7回繰り返す(うるう年の計算)
		List<String> expected = new ArrayList<>();
		{
			expected.add("20240227-000000");
			expected.add("20240228-000000");
			expected.add("20240229-000000"); // うるう年の計算
			expected.add("20240301-000000");
			expected.add("20240302-000000");
			expected.add("20240303-000000");
			expected.add("20240304-000000");
			expected.add("20240305-000000");
		}

		String initialDate = "20240227-000000";
		int calendarUnit = Calendar.DAY_OF_MONTH;
		int amount = 1;
		int repeat = 7;

		IncrementalDateFormatter formatter = new IncrementalDateFormatter("yyyyMMdd-HHmmss", initialDate, calendarUnit,
				amount, repeat);
		String v;
		int idx = 0;
		while ((v = formatter.next()) != null) {
			System.err.println(v);
			assertEquals(expected.get(idx).trim(), v);
			idx++;
		}
	}

	public void test001d() throws Exception {
		// 2024/2/27 0:00 から 1日追加を7回繰り返す(うるう年の計算)
		List<String> expected = new ArrayList<>();
		{
			expected.add("20240227");
			expected.add("20240228");
			expected.add("20240229"); // うるう年の計算
			expected.add("20240301");
			expected.add("20240302");
			expected.add("20240303");
			expected.add("20240304");
			expected.add("20240305");
		}

		String initialDate = "20240227";
		int calendarUnit = Calendar.DAY_OF_MONTH;
		int amount = 1;
		int repeat = 7;

		IncrementalDateFormatter formatter = new IncrementalDateFormatter("yyyyMMdd", initialDate, calendarUnit, amount,
				repeat);
		String v;
		int idx = 0;
		while ((v = formatter.next()) != null) {
			System.err.println(v);
			assertEquals(expected.get(idx).trim(), v);
			idx++;
		}
	}

	public void test001e() throws Exception {
		// 2024/2/27 0:00 から 1日追加を7回繰り返す(うるう年の計算)
		// test reverse option
		List<String> expected = new ArrayList<>();
		{
			expected.add("20240305");
			expected.add("20240304"); // decrement not increment
			expected.add("20240303");
			expected.add("20240302");
			expected.add("20240301");
			expected.add("20240229"); // うるう年の計算
			expected.add("20240228");
			expected.add("20240227");
		}

		String initialDate = "20240227";
		int calendarUnit = Calendar.DAY_OF_MONTH;
		int amount = 1;
		int repeat = 7;

		IncrementalDateFormatter formatter = new IncrementalDateFormatter("yyyyMMdd", initialDate, calendarUnit, amount,
				repeat, true);
		String v;
		int idx = 0;
		while ((v = formatter.next()) != null) {
			System.err.println(v);
			assertEquals(expected.get(idx).trim(), v);
			idx++;
		}
	}
	
	public void test001f() throws Exception {
		// 2024/2/27 0:00 から 1日追加を7回繰り返す(うるう年の計算)
		// test reverse option
		List<String> expected = new ArrayList<>();
		{
			expected.add("20240227");
			expected.add("20240228");
			expected.add("20240229"); // うるう年の計算
			expected.add("20240301");
			expected.add("20240302");
			expected.add("20240303");
			expected.add("20240304"); // decrement not increment
			expected.add("20240305");
		}

		String initialDate = "20240227";
		int calendarUnit = Calendar.DAY_OF_MONTH;
		int amount = 1;
		int repeat = 7;

		IncrementalDateFormatter formatter = new IncrementalDateFormatter("yyyyMMdd", initialDate, calendarUnit, amount,
				repeat, false);
		String v;
		int idx = 0;
		while ((v = formatter.next()) != null) {
			System.err.println(v);
			assertEquals(expected.get(idx).trim(), v);
			idx++;
		}
	}
	

	public void test002() throws Exception {
		// 2024/1/1 0:00 から 3時間追加を7回繰り返す
		IncrementalDateFormatter formatter = new IncrementalDateFormatter("yyyyMMdd-HHmmss", "20240101-000000",
				Calendar.HOUR_OF_DAY, 3, 7);
		List<String> expected = new ArrayList<>();
		{
			expected.add("20240101-000000");
			expected.add("20240101-030000");
			expected.add("20240101-060000");
			expected.add("20240101-090000");
			expected.add("20240101-120000");
			expected.add("20240101-150000");
			expected.add("20240101-180000");
			expected.add("20240101-210000");
		}
		String v;
		int idx = 0;
		while ((v = formatter.next()) != null) {
			System.err.println(v);
			assertEquals(expected.get(idx).trim(), v);
			idx++;
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
