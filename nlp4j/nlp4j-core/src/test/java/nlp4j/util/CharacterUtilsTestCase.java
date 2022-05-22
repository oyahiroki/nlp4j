package nlp4j.util;

import junit.framework.TestCase;

public class CharacterUtilsTestCase extends TestCase {

	public void testCharAt001() throws Exception {
		String s = "𩸽ほっけ";
		System.err.println("s=" + s);
		System.err.println("s.length=" + s.length());
		int cpLength = StringUtils.length(s);
		assertEquals(4, cpLength);
		System.err.println("CP length=" + cpLength);

		assertEquals("𩸽", StringUtils.charAt(s, 0));
		assertEquals("ほ", StringUtils.charAt(s, 1));
		assertEquals("っ", StringUtils.charAt(s, 2));
		assertEquals("け", StringUtils.charAt(s, 3));

		for (int n = 0; n < StringUtils.length(s); n++) {
			System.err.println(n + ": " + StringUtils.charAt(s, n));
		}

		System.err.println("Convert to MS932->" + StringUtils.filter(s, "MS932"));
	}

	public void testFilter001() throws Exception {
		String s = "𩸽ほっけ";
		System.err.println(s);
		String s1 = StringUtils.filter(s, "MS932");
		System.err.println(s1);
	}

	public void testGetName001() throws Exception {
		char c = 'あ';
		String name = CharacterUtils.getName(c);
		assertEquals("HIRAGANA LETTER A", name);
		System.err.println(name);
	}

	public void testGetName002() throws Exception {
		char c = '漢';
		String name = CharacterUtils.getName(c);
		assertEquals("CJK UNIFIED IDEOGRAPHS 6F22", name);
		System.err.println(name);
	}

	public void testGetName003() throws Exception {
		char c = '\u1100'; // ᄀ
		System.err.println(c);
		String name = CharacterUtils.getName(c);
		assertEquals("HANGUL CHOSEONG KIYEOK", name);
		System.err.println(name);
	}

	public void testGetName004() throws Exception {
		char c = '\uAC00'; // 가
		System.err.println(c);
		String name = CharacterUtils.getName(c);
		assertEquals("HANGUL SYLLABLES AC00", name);
		System.err.println(name);
	}

	public void testGetName101() throws Exception {
		int cp = 0x1F600; // 😀
		System.err.println(CharacterUtils.toChar(cp));
		String name = CharacterUtils.getName(cp);
		System.err.println(name);
		assertEquals("GRINNING FACE", name);
		String ubName = CharacterUtils.getUnicodeBlock(cp);
		assertEquals("EMOTICONS", ubName);
		System.err.println(Character.UnicodeBlock.of(cp));
	}

	public void testGetName102() throws Exception {
		int cp = 0x1F601; // 😁
		System.err.println(CharacterUtils.toChar(cp));
		String name = CharacterUtils.getName(cp);
		System.err.println(name);
		assertEquals("GRINNING FACE WITH SMILING EYES", name);
		String ubName = CharacterUtils.getUnicodeBlock(cp);
		assertEquals("EMOTICONS", ubName);
		System.err.println(Character.UnicodeBlock.of(cp));
	}

	public void testGetName103() throws Exception {
		int cp = 0x1F44F; // 👏
		System.err.println(CharacterUtils.toChar(cp));
		String name = CharacterUtils.getName(cp);
		System.err.println(name);
		assertEquals("CLAPPING HANDS SIGN", name);
		String ubName = CharacterUtils.getUnicodeBlock(cp);
		assertEquals("MISCELLANEOUS_SYMBOLS_AND_PICTOGRAPHS", ubName);
		System.err.println(Character.UnicodeBlock.of(cp));
	}

	public void testGetName104() throws Exception {
		int cp = 0x1F37F; // 🍿
		System.err.println(CharacterUtils.toChar(cp));
		String name = CharacterUtils.getName(cp);
		System.err.println(name);
		assertEquals("POPCORN", name);
		String ubName = CharacterUtils.getUnicodeBlock(cp);
		assertEquals("MISCELLANEOUS_SYMBOLS_AND_PICTOGRAPHS", ubName);
		System.err.println(Character.UnicodeBlock.of(cp));
	}

	public void testGetName105() throws Exception {
		int cp = 0x1F917; // 🤗
		System.err.println(CharacterUtils.toChar(cp));
		String name = CharacterUtils.getName(cp);
		System.err.println(name);
		assertEquals("HUGGING FACE", name);
		String ubName = CharacterUtils.getUnicodeBlock(cp);
		assertEquals("SUPPLEMENTAL_SYMBOLS_AND_PICTOGRAPHS", ubName);
		System.err.println(Character.UnicodeBlock.of(cp));
	}

	public void testGetName106() throws Exception {
		int cp = 0x1F370; // 🤗
		System.err.println(CharacterUtils.toChar(cp));
		String name = CharacterUtils.getName(cp);
		System.err.println(name);
		assertEquals("SHORTCAKE", name);
		String ubName = CharacterUtils.getUnicodeBlock(cp);
		assertEquals("MISCELLANEOUS_SYMBOLS_AND_PICTOGRAPHS", ubName);
		System.err.println(Character.UnicodeBlock.of(cp));
	}

	public void testGetName110() throws Exception {
		String s = "漢あア";

		int[] cpa = StringUtils.toCodePointArray(s);

		for (int n = 0; n < cpa.length; n++) {
			int cp = cpa[n];
			System.err.println(CharacterUtils.getName(cp));
			System.err.println(CharacterUtils.getUnicodeBlock(cp));
		}

	}

	public void testGetUnicodeBlock001() throws Exception {
		int cp = 0x1F600; // 😀
		String unicodeBlockName = CharacterUtils.getUnicodeBlock(cp);
		System.err.println(unicodeBlockName);
		assertEquals("EMOTICONS", unicodeBlockName);
	}

	// 𩸽
	public void testLength001() throws Exception {
		String s = "𩸽ほっけ";
		System.err.println(s);
		int cpLen = StringUtils.length(s);
		System.err.println(cpLen);

		assertEquals(5, s.length());
		assertEquals(4, cpLen);
		assertTrue(s.length() != cpLen);
	}

	public void testToChars001() throws Exception {
		String s = "漢あア";
		String[] ss = StringUtils.toChars(s);
		for (String s1 : ss) {
			System.err.println(s1);
		}
	}

	public void testToChars002() throws Exception {
		String s = "𩸽ほっけ";
		String[] ss = StringUtils.toChars(s);
		for (String s1 : ss) {
			System.err.println(s1);
		}
	}

}
