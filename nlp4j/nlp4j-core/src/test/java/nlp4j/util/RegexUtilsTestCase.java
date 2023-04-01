package nlp4j.util;

import junit.framework.TestCase;

public class RegexUtilsTestCase extends TestCase {

	public void test_REGEX_HASHTAG_001() throws Exception {
		String s1 = "This is test. #test";
		String s2 = s1.replaceAll(RegexUtils.REGEX_HASHTAG, "");

		System.err.println(s1);
		System.err.println(s2);
	}

	public void test_REGEX_URL_002() throws Exception {
		String s1 = "This is test. http://test.com/test this is test.";
		String s2 = s1.replaceAll(RegexUtils.REGEX_URL, "");

		System.err.println(s1);
		System.err.println(s2);
	}

	public void test_REGEX_JA_CHARS_001() throws Exception {
		String s1 = "日本語の文字列です。アメリカ・カナダ・ハンガリー";
		boolean b = s1.matches(RegexUtils.REGEX_JA_CHARS);
		boolean expected = true;
		System.err.println(s1);
		assertEquals(expected, b);
	}

}
