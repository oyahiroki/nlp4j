package nlp4j.util;

/**
 * @author Hiroki Oya
 * @since 1.3.7.12
 */
public class NumberUtils {

	/**
	 * @param num
	 * @return
	 * @since 1.3.7.12
	 */
	static public String format(Number num) {
		if (num instanceof Double) {
			return String.format("%,f", num);
		} else {
			return String.format("%,d", num);
		}
	}

}
