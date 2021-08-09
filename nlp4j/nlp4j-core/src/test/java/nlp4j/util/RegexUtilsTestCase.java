package nlp4j.util;

import junit.framework.TestCase;

public class RegexUtilsTestCase extends TestCase {

	public void test001() throws Exception {
		String s1 = "This is test. #test";
		String s2 = s1.replaceAll(RegexUtils.REGEX_HASHTAG, "");

		System.err.println(s1);
		System.err.println(s2);
	}

	public void test002() throws Exception {
		String s1 = "This is test. http://test.com/test this is test.";
		String s2 = s1.replaceAll(RegexUtils.REGEX_URL, "");

		System.err.println(s1);
		System.err.println(s2);
	}

}
