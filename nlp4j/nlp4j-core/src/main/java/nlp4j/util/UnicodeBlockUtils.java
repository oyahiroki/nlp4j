package nlp4j.util;

import java.lang.Character.UnicodeBlock;

public class UnicodeBlockUtils {

	/**
	 * カタカナ
	 */
	static public final UnicodeBlock KATAKANA = UnicodeBlock.KATAKANA;

	/**
	 * ひらがな
	 */
	static public final UnicodeBlock HIRAGANA = UnicodeBlock.HIRAGANA;

	/**
	 * CJK漢字
	 */
	static public final UnicodeBlock KANJI = UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS;

	/**
	 * 文字列が指定されたUnicodeBlockを含むことをチェックする
	 * 
	 * @param s   チェック対象の文字列
	 * @param ubb 複数のUnicodeBlock
	 * @return 文字列が指定されたUnicodeBlockの文字を含むか
	 */
	static public boolean contains(String s, UnicodeBlock... ubb) {
		if (ubb == null || ubb.length == 0) {
			return false;
		}
		for (int n = 0; n < s.length(); n++) {
			char c = s.charAt(n);

			for (UnicodeBlock ub : ubb) {
				boolean b = isOf(c, ub);
				if (b == true) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 文字列のすべてが指定されたUnicodeBlockで構成されていることをチェックする
	 * 
	 * @param s   チェック対象の文字列
	 * @param ubb 複数のUnicodeBlock
	 * @return 文字列のすべてが指定されたUnicodeBlockで構成されているか
	 */
	static public boolean isOf(String s, UnicodeBlock... ubb) {
		if (ubb == null || ubb.length == 0) {
			return false;
		}
		for (int n = 0; n < s.length(); n++) {
			char c = s.charAt(n);

			boolean b = false;

			for (UnicodeBlock ub : ubb) {
				b = b || isOf(c, ub);
				if (b == true) {
					break;
				}
			}
			if (b == false) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 漢字であるかを判定する
	 * 
	 * @param c 対象文字
	 * @return 漢字であるか
	 */
	static public boolean isCJKKanji(char c) {
		return UnicodeBlock.of(c) == UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS;
	}

	/**
	 * ひらがなであるかを判定する
	 * 
	 * @param c 対象文字
	 * @return ひらがなであるか
	 */
	static public boolean isJaHiragana(char c) {
		return UnicodeBlock.of(c) == UnicodeBlock.HIRAGANA;
	}

	/**
	 * カタカナであるかを判定する
	 * 
	 * @param c 対象文字
	 * @return カタカナであるか
	 */
	static public boolean isJaKatakana(char c) {
		return UnicodeBlock.of(c) == UnicodeBlock.KATAKANA;
	}

	/**
	 * 文字が指定されたUnicodeBlockに入っているかを判定する
	 * 
	 * @param c
	 * @param ub
	 * @return
	 */
	static public boolean isOf(char c, UnicodeBlock ub) {
		return UnicodeBlock.of(c) == ub;
	}

}
