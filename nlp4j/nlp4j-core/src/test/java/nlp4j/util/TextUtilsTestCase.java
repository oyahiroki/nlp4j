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

}
