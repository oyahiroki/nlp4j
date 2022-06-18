package nlp4j.util;

/**
 * String utilities for Japanese Text
 * 
 * @author Hiroki Oya
 * @since 1.3.6.1
 *
 */
public class JaStringUtils {

	static public boolean isAllHiragana(String s) {
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (isHiragana(c) == false) {
				return false;
			}
		}
		return true;
	}

	static public boolean isAllKatakana(String s) {
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (isKatakana(c) == false) {
				return false;
			}
		}
		return true;
	}

	static boolean isHiragana(char c) {
		return ((c >= 0x3041) && (c <= 0x3093));
	}

	static public boolean isKatakana(char c) {
		return ((c >= 0x30a1) && (c <= 0x30f3));
	}

	static public String toHiragana(String s) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (isKatakana(c)) {
				sb.append((char) (c - 0x60));
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	static public String toKatakana(String s) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (isHiragana(c)) {
				sb.append((char) (c + 0x60));
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

}
