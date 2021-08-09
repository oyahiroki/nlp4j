package nlp4j.util;

import junit.framework.TestCase;

public class CharacterUtilsTestCase extends TestCase {

	public void testCharAt001() throws Exception {
		String s = "𩸽ほっけ";
		System.err.println(s);
		System.err.println(s.length());

		System.err.println(StringUtils.length(s));

		for (int n = 0; n < StringUtils.length(s); n++) {
			System.err.println(n + ": " + StringUtils.charAt(s, n));
		}

		System.err.println(StringUtils.filter(s, "MS932"));
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
		System.err.println(name);
	}

	public void testGetName002() throws Exception {
		char c = '漢';
		String name = CharacterUtils.getName(c);
		System.err.println(name);
	}

	public void testGetName003() throws Exception {
		char c = '\u1100';
		String name = CharacterUtils.getName(c);
		System.err.println(name);
	}

	public void testGetName101() throws Exception {
		int cp = 0x1F600;
		String name = CharacterUtils.getName(cp);
		System.err.println(name);
		System.err.println(Character.UnicodeBlock.of(cp));
	}

	public void testGetName102() throws Exception {
		int cp = 0x1F601;
		String name = CharacterUtils.getName(cp);
		System.err.println(name);
		System.err.println(Character.UnicodeBlock.of(cp));
	}

	public void testGetName103() throws Exception {
		int cp = 0x1F44F;
		String name = CharacterUtils.getName(cp);
		System.err.println(name);
		System.err.println(Character.UnicodeBlock.of(cp));
	}

	public void testGetName104() throws Exception {
		int cp = 0x1F37F;
		String name = CharacterUtils.getName(cp);
		System.err.println(name);
		System.err.println(Character.UnicodeBlock.of(cp));
	}

	// 🤗
	public void testGetName104b() throws Exception {
		int cp = 0x1F917;
		String name = CharacterUtils.getName(cp);
		System.err.println(name);
		System.err.println(Character.UnicodeBlock.of(cp));
	}

	public void testGetName105() throws Exception {
		String s = "漢あア";

		int[] cpa = StringUtils.toCodePointArray(s);

		for (int n = 0; n < cpa.length; n++) {
			int cp = cpa[n];
			System.err.println(CharacterUtils.getName(cp));
			System.err.println(CharacterUtils.getUnicodeBlock(cp));
		}

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

	// 𩸽
	public void testLength001() throws Exception {
		String s = "𩸽ほっけ";
		System.err.println(s);
		int len1 = StringUtils.length(s);
		System.err.println(len1);

		assertEquals(5, s.length());
		assertEquals(4, len1);
	}

}
