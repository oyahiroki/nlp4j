package nlp4j.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author Hiroki Oya
 *
 */
public class DateUtils {

	static private SimpleDateFormat sdf_ISO8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");

	static private SimpleDateFormat sdf_yyyyMMdd_HHmmss = new SimpleDateFormat("yyyyMMdd-HHmmss");

	static private String formatUs(String data, String dataFormat, int style) {
		SimpleDateFormat sdf = new SimpleDateFormat(dataFormat);
		DateFormat df = DateFormat.getDateInstance(style, Locale.US);
		Date date;
		try {
			date = sdf.parse(data);
			return df.format(date);
		} catch (ParseException e) {
			return data;
		}
	}

	/**
	 * @param data       like "202101"
	 * @param dataFormat like "yyyyMM"
	 * @param format     like "MMM. yyyy"
	 * @return like "Jan. 2021"
	 */
	static public String formatUs(String data, String dataFormat, String format) {
		SimpleDateFormat sdf0 = new SimpleDateFormat(dataFormat, Locale.ENGLISH);
		SimpleDateFormat sdf1 = new SimpleDateFormat(format, Locale.ENGLISH);
		try {
			return sdf1.format(sdf0.parse(data));
		} catch (ParseException e) {
			e.printStackTrace();
			return data;
		}
	}

	/**
	 * @param data       "202101"
	 * @param dataFormat "yyyyMM"
	 * @return "January 1, 2021"
	 */
	static public String formatUsLong(String data, String dataFormat) {
		int style = DateFormat.LONG;
		return formatUs(data, dataFormat, style);
	}

	/**
	 * @param data       "202101"
	 * @param dataFormat "yyyyMM"
	 * @return
	 */
	static public String formatUsMidium(String data, String dataFormat) {
		int style = DateFormat.MEDIUM;
		return formatUs(data, dataFormat, style);
	}

	/**
	 * @return Date in yyyyMMdd-HHmmss
	 */
	static public String get_yyyyMMdd_HHmmss() {
		return sdf_yyyyMMdd_HHmmss.format(new Date());
	}

	/**
	 * @return Date in yyyyMMdd-HHmmss
	 * @since 1.3.7.12
	 */
	static public String get_yyyyMMdd_HHmmss(long time_ms) {
		return sdf_yyyyMMdd_HHmmss.format(new Date(time_ms));
	}

	/**
	 * <pre>
	 * </pre>
	 * 
	 * created on 2021-01-22
	 * 
	 * @param min
	 * @param max
	 * @param format
	 * @param calendarField
	 * @return
	 */
	static public List<String> getCalendarValues(String min, String max, String format, int calendarField) {
		List<String> vv = new ArrayList<String>();

		SimpleDateFormat sdf = new SimpleDateFormat(format);

		try {
			Date minDate = sdf.parse(min);
			Date maxDate = sdf.parse(max);

			/* addの基本的な使い方 */
			Calendar cl = Calendar.getInstance();
			cl.setTime(minDate);

			while (cl.getTime().before(maxDate) || cl.getTime().after(maxDate) == false) {
				vv.add(sdf.format(cl.getTime()));
				cl.add(calendarField, 1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return vv;

	}

	/**
	 * <pre>
	 * </pre>
	 * 
	 * created on 2021-01-22
	 * 
	 * @param min
	 * @param max
	 * @param format
	 * @return
	 */
	static public List<String> getCalendarValuesDay(String min, String max, String format) {
		return getCalendarValues(min, max, format, Calendar.DAY_OF_MONTH);
	}

	/**
	 * <pre>
	 * </pre>
	 * 
	 * created on 2021-01-22
	 * 
	 * @param min
	 * @param max
	 * @param format
	 * @return
	 */
	static public List<String> getCalendarValuesMonth(String min, String max, String format) {
		return getCalendarValues(min, max, format, Calendar.MONTH);
	}

	/**
	 * <pre>
	 * </pre>
	 * 
	 * created on 2021-01-19
	 * 
	 * @param format
	 * @return
	 */
	static public String getString(String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date());
	}

	/**
	 * @param iso8601
	 * @return
	 */
	static public Date toDate(String iso8601) {
		try {
			return sdf_ISO8601.parse(iso8601);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 * @param date
	 * @return ISO 8601 format
	 */
	static public String toISO8601(Date date) {
		return sdf_ISO8601.format(date);
	}

}
