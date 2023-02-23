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

	/**
	 * <pre>
	 * Remove XML Comment
	 * XMLコメントを削除する
	 * </pre>
	 */
	public void testRemoveXmlComment001() {
		String s = "aaa<!-- comment -->bbb<!-- comment -->ccc";
		System.err.println(s);
		s = StringUtils.removeXmlComment(s);
		System.err.println(s);
		assertEquals("aaabbbccc", s);
	}

	/**
	 * <pre>
	 * Remove XML Comment
	 * XMLコメントを削除する
	 * </pre>
	 */
	public void testRemoveXmlComment002() {
		String s = "aaa<!-- \n comment \n -->bbb<!-- \n comment \n -->ccc";
		System.err.println(s);
		s = StringUtils.removeXmlComment(s);
		System.err.println(s);
		assertEquals("aaabbbccc", s);
	}

	/**
	 * <pre>
	 * Remove XML Comment
	 * XMLコメントを削除する
	 * </pre>
	 */
	public void testRemoveXmlComment003() {
		String s = "aaa\n<!-- \n comment \n -->bbb\n<!-- \n comment \n -->ccc";
		System.err.println(s);
		s = StringUtils.removeXmlComment(s);
		System.err.println(s);
		assertEquals("aaa\nbbb\nccc", s);
	}

}
