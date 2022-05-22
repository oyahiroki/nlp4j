package nlp4j.util;

import junit.framework.TestCase;

/**
 * @author Hiroki Oya
 * @created_at 2021-07-24
 * @since 1.3.2.0
 */
public class StringUtilsTestCase extends TestCase {

	public void testCharAt001() {
		String s = "😀😀😀";
		String c0 = StringUtils.charAt(s, 0);
		assertEquals("😀", c0);
		String c1 = StringUtils.charAt(s, 1);
		assertEquals("😀", c1);
		String c2 = StringUtils.charAt(s, 2);
		assertEquals("😀", c2);
	}

	public void testFilter() {
		String s = "𩸽ほっけ"; // 𩸽 is not defined in MS932
		String s2 = StringUtils.filter(s, "MS932");
		System.err.println(s2);
		assertEquals("ほっけ", s2);
	}

	public void testLength001() {
		String s = "ABC";
		int len = StringUtils.length(s);
		assertEquals(3, len);
	}

	public void testLength002() {
		String s = "あいう";
		int len = StringUtils.length(s);
		assertEquals(3, len);
	}

	public void testLength003() {
		String s = "𩸽𩸽𩸽";
		int len = StringUtils.length(s);
		int len0 = s.length();
		assertEquals(3, len);
		System.err.println("len0=" + len0);
		assertTrue(len != len0);
	}

	public void testSubstringStringIntInt001() {
		String s = "𠮷田";
		String s2 = StringUtils.substring(s, 0, 1);
		assertEquals("𠮷", s2);
	}

	public void testToChars001() {
		String s = "ABC";
		String[] cc = StringUtils.toChars(s);
		assertEquals("A", cc[0]);
		assertEquals("B", cc[1]);
		assertEquals("C", cc[2]);
	}

	public void testToChars002() {
		String s = "あいう";
		String[] cc = StringUtils.toChars(s);
		assertEquals("あ", cc[0]);
		assertEquals("い", cc[1]);
		assertEquals("う", cc[2]);
	}

	public void testToChars003() {
		String s = "𩸽𩸽𩸽";
		String[] cc = StringUtils.toChars(s);
		assertEquals("𩸽", cc[0]);
		assertEquals("𩸽", cc[1]);
		assertEquals("𩸽", cc[2]);
	}

	public void testToChars004() {
		String s = "😀😀😀";
		String[] cc = StringUtils.toChars(s);
		assertEquals("😀", cc[0]);
		assertEquals("😀", cc[1]);
		assertEquals("😀", cc[2]);
		System.err.println(s.charAt(1)); // invalid
	}

	public void testToCodePointArray001() {
		String s = "ABC";
		int[] cp = StringUtils.toCodePointArray(s);
		for (int c : cp) {
			System.err.println(Integer.toHexString(c));
		}
		assertEquals(0x41, cp[0]);
		assertEquals(0x42, cp[1]);
		assertEquals(0x43, cp[2]);
	}

	public void testToCodePointArray002() {
		String s = "あいう";
		int[] cp = StringUtils.toCodePointArray(s);
		for (int c : cp) {
			System.err.println(Integer.toHexString(c));
		}
		assertEquals(0x3042, cp[0]);
		assertEquals(0x3044, cp[1]);
		assertEquals(0x3046, cp[2]);
	}

	public void testToHexUnicodeChar001() {
		char c = '　';
		String s = StringUtils.toHexUnicode(c);
		System.err.println(s);
		assertEquals("\\u3000", s);
	}

	public void testToHexUnicodeChar004() {
		char c = 'A';
		String c0 = StringUtils.toHexUnicode(c);
		System.err.println(c0);
		assertEquals("\\u0041", c0);
	}

	public void testToHexUnicodeString001() {
		String s = "　";
		String s2 = StringUtils.toHexUnicode(s);
		System.err.println(s2);
		assertEquals("\\u3000", s2);
	}

	public void testToHexUnicodeString002() {
		String s = "学校";
		String s2 = StringUtils.toHexUnicode(s);
		System.err.println(s2);
		assertEquals("\\u5b66\\u6821", s2);
	}

	public void testToHexUnicodeString003() {
		String s = "𠮷田";
		String s2 = StringUtils.toHexUnicode(s);
		System.err.println(s2);
		assertEquals("\\ud842\\udfb7\\u7530", s2);
	}

}
