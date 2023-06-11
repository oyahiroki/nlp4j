package nlp4j.util;

import java.lang.Character.UnicodeBlock;

/**
 * created on 2021-08-05
 * 
 * @author Hiroki Oya
 * @since 1.3.1.2
 */
public class CharacterUtils {

	static public String getName(char c) {
		int codepoint = (int) c;
		String name = Character.getName(codepoint);
		return name;
	}

	static public String getName(int codepoint) {
		String name = Character.getName(codepoint);
		return name;
	}

	static public String getUnicodeBlock(int codepoint) {
		UnicodeBlock ub = Character.UnicodeBlock.of(codepoint);
		if (ub == null) {
			return null;
		} else {
			return "" + ub;
		}
	}

	static public String toChar(int cp) {
		char[] chars = java.lang.Character.toChars(cp);
		return new String(chars);
	}

	static public boolean isKanji(char c) {
		return (UnicodeBlock.of(c) == UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS);
	}

}
