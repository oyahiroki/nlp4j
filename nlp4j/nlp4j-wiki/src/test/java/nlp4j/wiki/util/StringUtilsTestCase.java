package nlp4j.wiki.util;

import junit.framework.TestCase;

public class StringUtilsTestCase extends TestCase {

	public void testRemoveBracketted001() {
		String s = "aaa(xxx(xxx))bbb(xxx)ccc";
		System.err.println(s);
		s = StringUtils.removeBracketted(s, "()");
		System.err.println(s);
		assertEquals("aaabbbccc", s);
	}

	public void testRemoveBracketted002() {
		String s = "aaa(\nxxx(\nxxx)\n)bbb(xxx\n)ccc";
		System.err.println(s);
		s = StringUtils.removeBracketted(s, "()");
		System.err.println(s);
		assertEquals("aaabbbccc", s);
	}

}
