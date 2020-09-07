package nlp4j.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Hiroki Oya
 *
 */
public class DateUtils {

	static private SimpleDateFormat sdfISO8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");

	/**
	 * 
	 * @param date
	 * @return ISO 8601 format
	 */
	static public String toISO8601(Date date) {
		return sdfISO8601.format(date);
	}

	/**
	 * @param iso8601
	 * @return
	 */
	static public Date toDate(String iso8601) {
		try {
			return sdfISO8601.parse(iso8601);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

}
