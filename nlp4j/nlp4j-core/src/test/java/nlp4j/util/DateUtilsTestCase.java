package nlp4j.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import junit.framework.TestCase;

public class DateUtilsTestCase extends TestCase {

	public void testFormatUsFormat001() {
		String s = DateUtils.formatUs("202101", "yyyyMM", "MMM. yyyy");
		System.err.println(s);
	}

	public void testFormatUsLong001() {
		String s = DateUtils.formatUsLong("202101", "yyyyMM");
		System.err.println(s);
	}

	public void testFormatUsMidium001() {
		String s = DateUtils.formatUsMidium("202101", "yyyyMM");
		System.err.println(s);
	}

	public void testGet_yyyyMMdd001() throws Exception {
		String s = DateUtils.get_yyyyMMdd();
		System.err.println("Today: " + s);
	}

	public void testGet_yyyyMMddLong001() throws Exception {
		long diffDate = 0;
		String s = DateUtils.get_yyyyMMdd(diffDate);
		System.err.println("Today: " + s);
	}

	public void testGet_yyyyMMddLong002() throws Exception {
		long diffDate = -1;
		String s = DateUtils.get_yyyyMMdd(diffDate);
		System.err.println("Yesterday: " + s);
	}

	public void testGet_yyyyMMddLong003() throws Exception {
		long diffDate = 1;
		String s = DateUtils.get_yyyyMMdd(diffDate);
		System.err.println("Tomorrow: " + s);
	}

	public void testGetCalendarValues() {

	}

	public void testGetCalendarValuesDay() {
		String min = "20210101";
		String max = "20220301";
		String format = "yyyyMMdd";
		List<String> ss = DateUtils.getCalendarValuesDay(min, max, format);
		for (String s : ss) {
			System.err.println(s);
		}
		assertTrue(ss.contains("20210101") == true);
		assertTrue(ss.contains("20210132") == false);
		assertTrue(ss.contains("20210229") == false);
		assertTrue(ss.contains("20220301") == true);
		assertTrue(ss.contains("20220302") == false);
	}

	public void testGetCalendarValuesMonth() {
		String min = "202101";
		String max = "202203";
		String format = "yyyyMM";
		List<String> ss = DateUtils.getCalendarValuesMonth(min, max, format);
		for (String s : ss) {
			System.err.println(s);
		}
		assertTrue(ss.contains("202101") == true);
		assertTrue(ss.contains("202102") == true);
		assertTrue(ss.contains("202113") == false);
		assertTrue(ss.contains("202203") == true);
		assertTrue(ss.contains("202204") == false);
	}

	public void testGetString() {
		String currentDate = DateUtils.getString("yyyy-MM-dd");
		System.err.println(currentDate);
	}

	public void testGetUtcDate001() throws Exception {
		Date d = DateUtils.getUtcDate();
		System.err.println(d);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ZZ");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		System.out.println(sdf.format(d));
	}

	public void testToDate() {
		String s = "2022-01-11T12:34:56+09:00";
		Date d = DateUtils.toDate(s);
		System.err.println(d);
	}

	public void testToISO8601() throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date d = sdf.parse("20220111123456");
		String s = DateUtils.toISO8601(d);
		System.err.println(s);
		assertEquals("2022-01-11T12:34:56+09:00", s);
	}

}
