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
	 * <p>
	 * created on 2025-05-26
	 * </p>
	 * 
	 * 指定された日付フォーマット文字列を使用して、UTCタイムゾーンの {@link SimpleDateFormat} インスタンスを返します。
	 * <p>
	 * このメソッドは、与えられたフォーマットパターンを使用し、タイムゾーンをUTCに設定した {@link SimpleDateFormat} を生成します。
	 * 日付や時刻をUTC基準でフォーマット・解析したい場合に利用します。
	 * </p>
	 *
	 * Returns a {@link SimpleDateFormat} instance configured with the given date
	 * format string and the UTC time zone.
	 * <p>
	 * This method creates a {@link SimpleDateFormat} using the specified pattern
	 * and sets its time zone to UTC. Use this when you need to format or parse
	 * dates/times based on UTC.
	 * </p>
	 *
	 * <h3>使用例 / Example:</h3>
	 * 
	 * <pre>{@code
	 * // 日本語の例:
	 * SimpleDateFormat sdf = getUTCFormatter("yyyy-MM-dd'T'HH:mm:ss'Z'");
	 * String utcNow = sdf.format(new Date());
	 * System.out.println("UTC時刻: " + utcNow);
	 *
	 * // English example:
	 * SimpleDateFormat sdf = getUTCFormatter("yyyy-MM-dd'T'HH:mm:ss'Z'");
	 * String utcNow = sdf.format(new Date());
	 * System.out.println("UTC time: " + utcNow);
	 * }</pre>
	 *
	 * @param format 日付フォーマット文字列 / Date format pattern string
	 * @return UTCタイムゾーンの {@link SimpleDateFormat} インスタンス / {@link SimpleDateFormat}
	 *         instance with UTC time zone
	 */
	static public SimpleDateFormat getUTCFormatter(String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		return formatter;
	}

}
