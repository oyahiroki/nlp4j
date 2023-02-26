package nlp4j.wiki.template;

import junit.framework.TestCase;
import nlp4j.wiki.template.MediaWikiTemplateUtils;

public class MediaWikiTemplateUtilsTestCase extends TestCase {

	public void testToText() {

	}

	public void testIsTemplateOf001() {
		String s = "{{Sname||Apple}}";
		String prefix = "sname";
		boolean expected = true;
		boolean b = MediaWikiTemplateUtils.isTemplateOf(s, prefix);
		assertEquals(expected, b);
	}

	public void testLowerStartsWith001() {
		String s = "ABCxx";
		String prefix = "abc";
		boolean expected = true;
		boolean b = MediaWikiTemplateUtils.lowerStartsWith(s, prefix);
		assertEquals(expected, b);
	}

	public void testLowerStartsWith901() {
		// performance test
		long time1 = System.currentTimeMillis();
		for (int n = 0; n < 1000 * 1000; n++) {
			String s = "ABCxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
			String prefix = "abc";
			MediaWikiTemplateUtils.lowerStartsWith(s, prefix);
		}
		long time2 = System.currentTimeMillis();
		System.err.println(time2 - time1);
		long time3 = System.currentTimeMillis();
		for (int n = 0; n < 1000 * 1000; n++) {
			String s = "ABCxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
			String prefix = "abc";
			s.toLowerCase().startsWith(prefix);
		}
		long time4 = System.currentTimeMillis();
		System.err.println(time4 - time3);

	}

}
