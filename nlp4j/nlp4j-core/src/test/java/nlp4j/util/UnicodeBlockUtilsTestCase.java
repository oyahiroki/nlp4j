package nlp4j.util;

import java.lang.Character.UnicodeBlock;

import junit.framework.TestCase;

public class UnicodeBlockUtilsTestCase extends TestCase {

	public void testIsJaKatakana001() {
		char c = 'ア';
		boolean b = UnicodeBlockUtils.isJaKatakana(c);
		boolean expected = true;
		assertEquals(expected, b);
	}

	public void testIsJaKatakana002() {
		char c = 'あ';
		boolean b = UnicodeBlockUtils.isJaKatakana(c);
		boolean expected = false;
		assertEquals(expected, b);
	}

	public void testIsJaKatakana003() {
		char c = 'a';
		boolean b = UnicodeBlockUtils.isJaKatakana(c);
		boolean expected = false;
		assertEquals(expected, b);
	}

	public void testIsJaHiragana001() {
		char c = 'あ';
		boolean b = UnicodeBlockUtils.isJaHiragana(c);
		boolean expected = true;
		assertEquals(expected, b);
	}

	public void testIsJaHiragana002() {
		char c = 'ア';
		boolean b = UnicodeBlockUtils.isJaHiragana(c);
		boolean expected = false;
		assertEquals(expected, b);
	}

	public void testIsJaHiragana003() {
		char c = 'A';
		boolean b = UnicodeBlockUtils.isJaHiragana(c);
		boolean expected = false;
		assertEquals(expected, b);
	}

	public void testIsCJKKanji001() {
		char c = '漢';
		boolean b = UnicodeBlockUtils.isCJKKanji(c);
		boolean expected = true;
		assertEquals(expected, b);
	}

	public void testIsCJKKanji002() {
		char c = 'あ';
		boolean b = UnicodeBlockUtils.isCJKKanji(c);
		boolean expected = false;
		assertEquals(expected, b);
	}

	public void testIsCJKKanji003() {
		char c = 'A';
		boolean b = UnicodeBlockUtils.isCJKKanji(c);
		boolean expected = false;
		assertEquals(expected, b);
	}

	public void testIsCJKKanji004() {
		char c = '한';
		boolean b = UnicodeBlockUtils.isCJKKanji(c);
		boolean expected = false;
		assertEquals(expected, b);
	}

	public void testContains001() {
		String s = "あいう";
		boolean b = UnicodeBlockUtils.contains(s, UnicodeBlock.HIRAGANA);
		boolean expected = true;
		assertEquals(expected, b);
	}

	public void testContains002() {
		String s = "アイウ";
		boolean b = UnicodeBlockUtils.contains(s, UnicodeBlock.HIRAGANA);
		boolean expected = false;
		assertEquals(expected, b);
	}

	public void testContains003() {
		String s = "アイウ";
		boolean b = UnicodeBlockUtils.contains(s, UnicodeBlock.HIRAGANA);
		boolean expected = false;
		assertEquals(expected, b);
	}

	public void testContains004() {
		String s = "これは日本語デス。";
		boolean b = UnicodeBlockUtils.contains(s, UnicodeBlock.HIRAGANA, UnicodeBlock.KATAKANA,
				UnicodeBlockUtils.KANJI);
		boolean expected = true;
		assertEquals(expected, b);
	}

}
