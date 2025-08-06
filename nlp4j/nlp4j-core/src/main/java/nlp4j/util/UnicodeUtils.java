package nlp4j.util;

/**
 * created on 2022-12-31
 * 
 * @author Hiroki Oya
 * 
 */
public class UnicodeUtils {

	public static String nfkc(String s) {
		return TextUtils.nfkc(s);
	}

	public static final String BOM = "\uFEFF";

	/**
	 * Remove BOM and trim string;
	 * 
	 * @param s Removed BOM and Trimmed
	 * @return
	 */
	public static String removeBOM(String s) {
		if (s == null) {
			return s;
		} else {
			if (s.startsWith(BOM)) {
				return s.substring(1).trim();
			} else {
				return s.trim();
			}
		}
	}

}
