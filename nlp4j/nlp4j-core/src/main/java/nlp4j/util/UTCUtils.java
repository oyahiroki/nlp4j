package nlp4j.util;

import java.util.Date;

public class UTCUtils {

	static public String getUTC(Date date, String format) {
		return SimpleDateFormatUtil.getUTCFormatter(format).format(date);
	}

	static public String getUTC(String format) {
		return getUTC(format, new Date());
	}

	static public String getUTC(String format, Date date) {
		return SimpleDateFormatUtil.getUTCFormatter(format).format(date);
	}

}
