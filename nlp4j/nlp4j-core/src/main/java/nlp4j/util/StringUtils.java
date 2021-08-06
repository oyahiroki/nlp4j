package nlp4j.util;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;

/**
 * @author Hiroki Oya
 * @created_at 2021-07-24
 * @since 1.3.2.0
 */
public class StringUtils {

	/**
	 * Remove characters that is not covered in encoding from String<br>
	 * 文字エンコーディングに存在しない文字を除去します.
	 * 
	 * @param s        文字列
	 * @param encoding エンコーディング
	 * @return フィルターされた文字
	 */
	static public String filter(String s, String encoding) {
		if (s == null || encoding == null || s.isEmpty()) {
			return s;
		}
		CharsetEncoder encoder;
		try {
			encoder = Charset.forName(encoding).newEncoder();
		} catch (Exception e) {
			return s;
		}
		StringBuilder sb = new StringBuilder();
		int len = length(s);
		for (int n = 0; n < len; n++) {
			String s1 = charAt(s, n);
			if (encoder.canEncode(s1)) {
				sb.append(s1);
			}
		}
		return sb.toString();
	}

	/**
	 * Convert String to code point array.<br>
	 * 文字列をコードポイントの配列に変換します.
	 * 
	 * @param str 文字列
	 * @return コードポイントの配列
	 */
	static public int[] toCodePointArray(String str) {
		int len = str.length(); // the length of str
		int[] cpa = new int[str.codePointCount(0, len)];
		for (int i = 0, j = 0; i < len; i = str.offsetByCodePoints(i, 1)) {
			cpa[j++] = str.codePointAt(i);
		}
		return cpa;
	}

	/**
	 * Return length of string in consideration of code point.<br>
	 * コードポイントを考慮した文字列長さを返します.
	 * 
	 * @param s 文字列
	 * @return コードポイントを考慮した文字列長
	 */
	static public int length(String s) {
		return s.codePointCount(0, s.length());
	}

	/**
	 * Return String Characters in consideration of code point.<br>
	 * コードポイントを考慮して１文字ずつの配列を返します.
	 * 
	 * @param s 文字列
	 * @return コードポイントを考慮した文字配列
	 */
	static public String[] toChars(String s) {
		int len = s.length();
		ArrayList<String> ss = new ArrayList<>();
		for (int i = 0; i < len; i = s.offsetByCodePoints(i, 1)) {
			String s1 = new String(Character.toChars(s.codePointAt(i)));
			ss.add(s1);
		}
		return ss.toArray(new String[0]);
	}

	static public String charAt(String s, int n) {
		int len = s.length();
		int x = 0;
		for (int i = 0; i < len; i = s.offsetByCodePoints(i, 1), x++) {
			if (x == n) {
				return new String(Character.toChars(s.codePointAt(i)));
			}
		}
		return null;
	}

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
