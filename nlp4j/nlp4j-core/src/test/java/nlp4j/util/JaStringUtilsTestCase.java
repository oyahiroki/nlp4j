package nlp4j.util;

import junit.framework.TestCase;

public class JaStringUtilsTestCase extends TestCase {

	public void testIsAllHiragana() {
		String s1 = "ふぁみりーこんぴゅーた";
		boolean b = JaStringUtils.isAllHiragana(s1);
		boolean expected = false;
		System.err.println(b);
		assertEquals(expected, b);
	}

	public void testIsAllKatakana() {
		String s1 = "カメラ";
		boolean b = JaStringUtils.isAllHiragana(s1);
		boolean expected = true;

		System.err.println(b);
		assertEquals(expected, b);
	}

	public void testToHiragana() {
		String s1 = "ウドンを食べる";
		String s2 = JaStringUtils.toHiragana(s1);
		String expected = "うどんを食べる";

		System.err.println(s2);
		assertEquals(expected, s2);
	}

	public void testToKatakana() {
		String s1 = "ふぁみりーこんぴゅーた";
		String s2 = JaStringUtils.toKatakana(s1);
		String expected = "ファミリーコンピュータ";

		System.err.println(s2);
		assertEquals(expected, s2);
	}

}
