package nlp4j.util;

/**
 * @author Hiroki Oya
 * @created_at 2021-07-24
 * @since 1.3.2.0
 */
public class StringUtils {

	static private String toHexString(char c) {
		String s = Integer.toHexString((int) c);
		if (s.length() == 1) {
			return "000" + s;
		} //
		else if (s.length() == 2) {
			return "00" + s;
		} //
		else if (s.length() == 3) {
			return "0" + s;
		} //
		else {
			return s;
		}
	}

	/**
	 * Character to Unicode in Hex String
	 * 
	 * @param c to be converted
	 * @return Unicode Hex String (Same as native2ascii)
	 * @since 1.3.2.0
	 */
	static public String toHexUnicode(char c) {
		return "\\u" + toHexString(c);
	}

	/**
	 * String to Unicode in Hex String
	 * 
	 * @param s to be converted
	 * @return Unicode Hex String (Same as native2ascii)
	 * @since 1.3.2.0
	 */
	static public String toHexUnicode(String s) {
		if (s == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (int n = 0; n < s.length(); n++) {
			char c = s.charAt(n);
			sb.append(toHexUnicode(c));
		}
		return sb.toString();
	}

}
