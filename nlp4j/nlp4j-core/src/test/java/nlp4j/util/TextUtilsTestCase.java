package nlp4j.util;

import junit.framework.TestCase;

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

}
