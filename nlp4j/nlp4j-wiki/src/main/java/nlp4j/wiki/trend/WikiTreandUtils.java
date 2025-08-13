package nlp4j.wiki.trend;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import nlp4j.util.IncrementalDateFormatter;
import nlp4j.util.SimpleDateFormatUtil;
import nlp4j.util.UTCUtils;

public class WikiTreandUtils {

	static public List<String> getUrls(Date date, int range_hour) {
		List<String> urls = new ArrayList<String>();
		String url_base = "https://dumps.wikimedia.org/other/pageviews/";

		String year = UTCUtils.getUTC("yyyy",date); // "2023";
		String m = UTCUtils.getUTC("MM",date); // "12";
		String d = UTCUtils.getUTC("dd",date); // "31";
		String day = year + m + d;
		String HHmmss = UTCUtils.getUTC("HH'0000'",date);

//		int range_hour = 23;

		String format_url_path = "yyyy/yyyy-MM/'pageviews'-yyyyMMdd-HHmmss'.gz'";
		String initialDate = year + "/" + year + "-" + m + "/pageviews-" + day + "-" + HHmmss + ".gz";
		IncrementalDateFormatter formatter;
		try {
			formatter = new IncrementalDateFormatter(format_url_path, //
					initialDate, // 初期値
					Calendar.HOUR_OF_DAY, // 時間単位
					1, // １時間単位ずつ
					range_hour, // 幅
					true // true:逆順
			);

			String s;
			while ((s = formatter.next()) != null) {
				urls.add(url_base + s);
			}

			return urls;

		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}

	}

}
