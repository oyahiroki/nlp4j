package nlp4j.util;

import junit.framework.TestCase;

/**
 * @author Hiroki Oya
 * @created_at 2021-07-24
 * @since 1.3.2.0
 */
public class StringUtilsTestCase extends TestCase {

	public void testCharAt001() {
		String s = "😀😀😀"; // EMOJI SMILE
		String c0 = StringUtils.charAt(s, 0);
		assertEquals("😀", c0);// EMOJI SMILE
		String c1 = StringUtils.charAt(s, 1);
		assertEquals("😀", c1);// EMOJI SMILE
		String c2 = StringUtils.charAt(s, 2);
		assertEquals("😀", c2);// EMOJI SMILE
	}

	public void testChopString001() {
		String s = "1234567890";
		String s2 = StringUtils.chop(s, 3);
		String expected = "123";
		assertEquals(expected, s2);
	}

	public void testChopString002() {
		String s = "123";
		String s2 = StringUtils.chop(s, 5);
		String expected = "123";
		assertEquals(expected, s2);
	}

	public void testFilter() {
		String s = "𩸽ほっけ"; // 𩸽 is not defined in MS932
		String s2 = StringUtils.filter(s, "MS932");
		System.err.println(s2);
		assertEquals("ほっけ", s2);
	}

	public void testIsJaHiragana001() {
		String s = "あいうえお";
		boolean b = StringUtils.isJaHiragana(s);
		boolean expected = true;
		assertEquals(expected, b);
	}

	public void testIsJaHiragana002() {
		String s = "あいうえおア";
		boolean b = StringUtils.isJaHiragana(s);
		boolean expected = false;
		assertEquals(expected, b);
	}

	public void testIsJaKana001() {
		String s = "あいうえおア";
		boolean b = StringUtils.isJaKana(s);
		boolean expected = true;
		assertEquals(expected, b);
	}

	public void testIsJaKana002() {
		String s = "あいうえおア漢";
		boolean b = StringUtils.isJaKana(s);
		boolean expected = false;
		assertEquals(expected, b);
	}

	public void testIsJaKana003() {
		String s = "abc";
		boolean b = StringUtils.isJaKana(s);
		boolean expected = false;
		assertEquals(expected, b);
	}

	/**
	 * 漢字判定
	 */
	public void testIsKanji001() {
		String s = "漢字"; // KANJI ONLY
		boolean b = StringUtils.isKanji(s);
		boolean expected = true;
		assertEquals(expected, b);
	}

	/**
	 * 漢字判定
	 */
	public void testIsKanji002() {
		String s = "漢字とひらがな"; // KANJI-HIRAGANA
		boolean b = StringUtils.isKanji(s);
		boolean expected = false;
		assertEquals(expected, b);
	}

	/**
	 * 漢字判定
	 */
	public void testIsKanji003() {
		String s = "漢字ABC"; // KANJI-ALPHABET
		boolean b = StringUtils.isKanji(s);
		boolean expected = false;
		assertEquals(expected, b);
	}

	/**
	 * 漢字判定
	 */
	public void testIsKanji004() {
		String s = "한글"; // HAN-GUL
		boolean b = StringUtils.isKanji(s);
		boolean expected = false;
		assertEquals(expected, b);
	}

	public void testIsNumeric001() {
		String s = "100";
		boolean expected = true;
		boolean b = StringUtils.isNumeric(s);
		assertEquals(expected, b);
	}

	public void testIsNumeric002() {
		String s = "100.0";
		boolean expected = false;
		boolean b = StringUtils.isNumeric(s);
		assertEquals(expected, b);
	}

	public void testIsNumeric003() {
		String s = "a";
		boolean expected = false;
		boolean b = StringUtils.isNumeric(s);
		assertEquals(expected, b);
	}

	public void testIsNumeric004() {
		String s = "-1";
		boolean expected = false;
		boolean b = StringUtils.isNumeric(s);
		assertEquals(expected, b);
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

	public void testSubstringBefore001() {
		String s = "ABC#123";
		String s2 = StringUtils.substringBefore(s, "#");
		String expected = "ABC";
		assertEquals(expected, s2);
	}

	// 0.00004707386081488301

	public void testSubstringBefore002() {
		String s = "ABC";
		String s2 = StringUtils.substringBefore(s, "#");
		String expected = "ABC";
		assertEquals(expected, s2);
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

	/**
	 * カタカナ → ひらがな
	 */
	public void testToJaHiraganaFromKatakana001() {
		String s1 = "アイウエオABC";
		String s2 = StringUtils.toJaHiraganaFromKatakana(s1);
		String expected = "あいうえおABC";
		assertEquals(expected, s2);
	}

	/**
	 * ひらがな → カタカナ
	 */
	public void testToJaKatakanaFromHiragana001() {
		String s1 = "あいうえおABC";
		String s2 = StringUtils.toJaKatakanaFromHiragana(s1);
		String expected = "アイウエオABC";
		assertEquals(expected, s2);
	}

	public void testToStringDouble001() {
		double d = 0.00000000000000001;
		String s = StringUtils.toString(d);
		String expected = "0.000000000000000010";
		System.err.println(s);
		assertEquals(expected, s);
	}

	public void testToStringDouble002() {
		double d = 0.00004707386081488301;
		String s = StringUtils.toString(d);
		String expected = "0.00004707386081488301";
		System.err.println(s);
		assertEquals(expected, s);
	}

}
