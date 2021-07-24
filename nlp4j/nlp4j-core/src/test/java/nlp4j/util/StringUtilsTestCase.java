package nlp4j.util;

import junit.framework.TestCase;

/**
 * @author Hiroki Oya
 * @created_at 2021-07-24
 * @since 1.3.2.0
 */
public class StringUtilsTestCase extends TestCase {

	@SuppressWarnings("javadoc")
	public void testToHexUnicodeChar001() {
		char c = '　';
		String s = StringUtils.toHexUnicode(c);
		System.err.println(s);
		assertEquals("\\u3000", s);
	}

	@SuppressWarnings("javadoc")
	public void testToHexUnicodeString001() {
		String s = "　";
		String s2 = StringUtils.toHexUnicode(s);
		System.err.println(s2);
		assertEquals("\\u3000", s2);
	}

	@SuppressWarnings("javadoc")
	public void testToHexUnicodeString002() {
		String s = "学校";
		String s2 = StringUtils.toHexUnicode(s);
		System.err.println(s2);
		assertEquals("\\u5b66\\u6821", s2);
	}

}
