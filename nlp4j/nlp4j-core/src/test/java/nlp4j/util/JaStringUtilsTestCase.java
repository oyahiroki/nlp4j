package nlp4j.util;

import junit.framework.TestCase;

public class JaStringUtilsTestCase extends TestCase {

	public void testGetHiragana50AsCollection001() {
		// ひらがな文字（濁音・小さい文字を除く）のリスト
		for (char c : JaStringUtils.getHiragana50AsCollection()) {
			System.err.println(c);
		}
	}

	public void testHiragana50001() {
		// ひらがな50音（濁音除く）
		System.err.println(JaStringUtils.HIRAGANA50);
	}

	public void testIsAllHiragana001() {
		String s1 = "あいうえお";
		boolean b = JaStringUtils.isAllHiragana(s1);
		boolean expected = true;
		System.err.println(b);
		assertEquals(expected, b);
	}

	public void testIsAllHiragana002() {
		// 長音（ー）はカタカナ
		String s1 = "ふぁみり" //
				+ "ー" // KATAKANA
				+ "こんぴゅ" //
				+ "ー" // KATAKANA
				+ "た";
		boolean b = JaStringUtils.isAllHiragana(s1);
		boolean expected = false;
		System.err.println(b);
		assertEquals(expected, b);
	}

	public void testIsAllKatakana001() {
		String s1 = "カメラ";
		// すべての文字がカタカナ(KATAKANA)であるかどうかを判定する
		boolean b = JaStringUtils.isAllKatakana(s1);
		boolean expected = true;

		System.err.println(b);
		assertEquals(expected, b);
	}

	public void testToHiragana001() {
		String s1 = "ウドンを食べる";
		// 「カタカナ」を「ひらがな」に変換する
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
