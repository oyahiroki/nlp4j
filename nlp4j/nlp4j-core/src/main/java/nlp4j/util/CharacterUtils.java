package nlp4j.util;

import java.lang.Character.UnicodeBlock;

/**
 * @author Hiroki Oya
 * @created_at 2021-08-05
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

}
