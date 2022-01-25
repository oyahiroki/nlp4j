package nlp4j.util;

import java.util.List;

import junit.framework.TestCase;

/**
 * <pre>
 * created_at : 2022-01-16
 * </pre>
 * 
 * @author Hiroki Oya
 *
 */
public class FacetUtilsTestCase extends TestCase {

	public void testSplitFacetPath001() {
		String s = "word.aa.bb.cc";
		List<String> ss = FacetUtils.splitFacetPath(s);
		System.err.println(ss);
		assertEquals("[word, word.aa, word.aa.bb, word.aa.bb.cc]", ss.toString());
	}

	public void testSplitFacetPath002() {
		String s = "word";
		List<String> ss = FacetUtils.splitFacetPath(s);
		System.err.println(ss);
		assertEquals("[word]", ss.toString());
	}

	public void testSplitFacetPath101() {
		String s = null;
		List<String> ss = FacetUtils.splitFacetPath(s);
		System.err.println(ss);
		assertEquals("[]", ss.toString());
	}

	public void testSplitFacetPath102() {
		String s = "";
		List<String> ss = FacetUtils.splitFacetPath(s);
		System.err.println(ss);
		assertEquals("[]", ss.toString());
	}

}
