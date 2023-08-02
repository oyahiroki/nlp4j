package nlp4j.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * String utilities for Japanese Text
 * 
 * @author Hiroki Oya
 * @since 1.3.6.1
 *
 */
public class JaStringUtils {

	/**
	 * <pre>
	 * ひらがな50音（濁音除く）
	 * あいうえおかきくけこさしすせそたちつてとなにぬねのはひふへほまみむめもやゆよらりるれろわをん
	 * </pre>
	 */
	static public final String HIRAGANA50 = "あいうえおかきくけこさしすせそたちつてとなにぬねのはひふへほまみむめもやゆよらりるれろわをん";

	/**
	 * @return ひらがな文字（濁音・小さい文字を除く）のリスト
	 */
	static public Collection<Character> getHiragana50AsCollection() {
		List<Character> cc = new ArrayList<>();
		for (int n = 0; n < HIRAGANA50.length(); n++) {
			cc.add(HIRAGANA50.charAt(n));
		}
		return cc;
	}

	/**
	 * すべての文字がひらがな(HIRAGANA)であるかどうかを判定する
	 * 
	 * @param s
	 * @return
	 */
	static public boolean isAllHiragana(String s) {
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (isHiragana(c) == false) {
				return false;
			}
		}
		return true;
	}

	/**
	 * すべての文字がカタカナ(KATAKANA)であるかどうかを判定する
	 * 
	 * @param s
	 * @return
	 */
	static public boolean isAllKatakana(String s) {
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (isKatakana(c) == false) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 文字が「ひらがな(HIRAGANA)」であることを判定する
	 * 
	 * @param c
	 * @return
	 */
	static boolean isHiragana(char c) {
		return ((c >= 0x3041) && (c <= 0x3093));
	}

	/**
	 * 文字が「カタカナ(KATAKANA)」であることを判定する
	 * 
	 * @param c
	 * @return
	 */
	static public boolean isKatakana(char c) {
		return ((c >= 0x30a1) && (c <= 0x30f3));
	}

	/**
	 * 「カタカナ」を「ひらがな」に変換する
	 * 
	 * @param s
	 * @return
	 */
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

	/**
	 * 「ひらがな」を「カタカナ」に変換する
	 * 
	 * @param s
	 * @return
	 */
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
