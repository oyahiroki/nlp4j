package nlp4j.wiki.util;

import junit.framework.TestCase;

public class WikiTemplateNormalizerTestCase extends TestCase {

	public void testNormalize101() {
		String s1 = "{{ふりがな|煙草|たばこ}}";
		String s2 = WikiTemplateNormalizer.normalize(s1);
		System.err.println(s2);
		String expected = "煙草";
		assertEquals(expected, s2);
	}

	public void testNormalize102() {
		String s1 = "{{ふりがな|青丹|あおに}}";
		String s2 = WikiTemplateNormalizer.normalize(s1);
		System.err.println(s2);
		String expected = "青丹";
		assertEquals(expected, s2);
	}

	public void testNormalize103() {
		String s1 = "{{おくりがな2|吸|す|う|すう}}";
		String s2 = WikiTemplateNormalizer.normalize(s1);
		System.err.println(s2);
		String expected = "吸う";
		assertEquals(expected, s2);
	}
}
