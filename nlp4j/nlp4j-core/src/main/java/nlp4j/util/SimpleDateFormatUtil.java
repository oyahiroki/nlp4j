package nlp4j.util;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * created on 2025-05-26
 * 
 * @author Hiroki Oya
 *
 */
public class SimpleDateFormatUtil {

	/**
	 * created on 2025-05-26
	 * 
	 * @param format
	 * @return
	 */
	static public SimpleDateFormat getUTCFormatter(String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		return formatter;
	}

}
