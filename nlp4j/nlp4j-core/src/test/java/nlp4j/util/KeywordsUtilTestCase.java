package nlp4j.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;
import nlp4j.Keyword;
import nlp4j.impl.DefaultKeyword;

public class KeywordsUtilTestCase extends TestCase {

	public void testIsSameFacet() {
		// TODO
	}

	public void testPrintlnListOfKeyword() {
		// TODO
	}

	public void testPrintlnListOfKeywordPrintWriter() {
		// TODO
	}

	public void testSortByCorrelationAsc() {
		// TODO
	}

	public void testSortByCorrelationDesc() {
		// TODO
	}

	public void testSortByCountAsc() {
		// TODO
	}

	public void testSortByCountDesc() {
		// TODO
	}

	public void testToLex() {
		// TODO
	}

	public void testToLexArray() {
		List<Keyword> kwds = new ArrayList<>();
		{
			kwds.add(new DefaultKeyword(null, "aaa"));
			kwds.add(new DefaultKeyword(null, "bbb"));
			kwds.add(new DefaultKeyword(null, "ccc"));
		}
		String[] lexArray = KeywordsUtil.toLexArray(kwds);
		System.err.println(Arrays.toString(lexArray));
	}

	public void testToLexList() {
		List<Keyword> kwds = new ArrayList<>();
		{
			kwds.add(new DefaultKeyword(null, "aaa"));
			kwds.add(new DefaultKeyword(null, "bbb"));
			kwds.add(new DefaultKeyword(null, "ccc"));
		}
		List<String> list = KeywordsUtil.toLexList(kwds);
		System.err.println(list);
	}

	public void testToLexSet() {
		List<Keyword> kwds = new ArrayList<>();
		{
			kwds.add(new DefaultKeyword(null, "aaa"));
			kwds.add(new DefaultKeyword(null, "bbb"));
			kwds.add(new DefaultKeyword(null, "ccc"));
		}
		Set<String> set = KeywordsUtil.toLexSet(kwds);
		System.err.println(set);
	}

	public void testIsSameLexSet001() {
		boolean expected = true;
		List<Keyword> kwds1 = new ArrayList<>();
		{
			kwds1.add(new DefaultKeyword(null, "aaa"));
			kwds1.add(new DefaultKeyword(null, "bbb"));
			kwds1.add(new DefaultKeyword(null, "ccc"));
		}
		List<Keyword> kwds2 = new ArrayList<>();
		{
			kwds2.add(new DefaultKeyword(null, "aaa"));
			kwds2.add(new DefaultKeyword(null, "bbb"));
			kwds2.add(new DefaultKeyword(null, "ccc"));
		}
		boolean b = KeywordsUtil.isSameLexSet(kwds1, kwds2);
		assertEquals(expected, b);
	}

	public void testIsSameLexSet002() {
		boolean expected = false;
		List<Keyword> kwds1 = new ArrayList<>();
		{
			kwds1.add(new DefaultKeyword(null, "aaa"));
			kwds1.add(new DefaultKeyword(null, "bbb"));
			kwds1.add(new DefaultKeyword(null, "xxx"));
		}
		List<Keyword> kwds2 = new ArrayList<>();
		{
			kwds2.add(new DefaultKeyword(null, "aaa"));
			kwds2.add(new DefaultKeyword(null, "bbb"));
			kwds2.add(new DefaultKeyword(null, "ccc"));
		}
		boolean b = KeywordsUtil.isSameLexSet(kwds1, kwds2);
		assertEquals(expected, b);
	}

	public void testIsSameLexSet003() {
		boolean expected = false;
		List<Keyword> kwds1 = new ArrayList<>();
		{
			kwds1.add(new DefaultKeyword(null, "aaa"));
			kwds1.add(new DefaultKeyword(null, "bbb"));
			kwds1.add(new DefaultKeyword(null, "ccc"));
		}
		List<Keyword> kwds2 = new ArrayList<>();
		{
			kwds2.add(new DefaultKeyword(null, "aaa"));
			kwds2.add(new DefaultKeyword(null, "bbb"));
			kwds2.add(new DefaultKeyword(null, "xxx"));
		}
		boolean b = KeywordsUtil.isSameLexSet(kwds1, kwds2);
		assertEquals(expected, b);
	}
}
