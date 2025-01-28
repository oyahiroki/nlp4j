package nlp4j.util;

import junit.framework.TestCase;

/**
 * @since 1.3.7.3
 */
public class TextUtilsTestCase extends TestCase {

	public void testTextUtils() {
		String s1 = "Ａ\r\nＢ\tＣ  \n";
		String s2 = (new TextUtils(s1)).nfkc().removeNewline().removeTab().trim().get();
		System.err.println(s1);
		System.err.println(s2);
		assertEquals("ABC", s2);
	}

	public void testNString001() throws Exception {
		String text = "ＡＢＣ(xyz)１２３(zzz)";
		String text2 = TextUtils.n(text).nfkc().removeBrackets().get();
		System.err.println(text2);
	}

	public void testRemoveBrackets001() throws Exception {
		String text = "ABC(DEF)XYZ";
		String text2 = TextUtils.n(text).removeBrackets().get();
		System.err.println(text2);
	}

	public void testRemoveBrackets002() throws Exception {
		String text = "ABC[[((XXX)){{XXX}}]]XYZ";
		String text2 = TextUtils.n(text).removeBrackets().get();
		System.err.println(text2);
	}

	public void testRemoveBracketsString001() throws Exception {
		String text = "ABC{{((XXX)){{XXX}}}}XYZ";
		String text2 = TextUtils.removeBrackets(text);
		String expected = "ABCXYZ";
		System.err.println(text2);
		assertEquals(expected, text2);
	}

	public void testRemoveBracketsString002() throws Exception {
		String text = "ABCXYZ";
		String text2 = TextUtils.removeBrackets(text);
		String expected = "ABCXYZ";
		System.err.println(text2);
		assertEquals(expected, text2);
	}

}
