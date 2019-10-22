package nlp4j.impl;

import junit.framework.TestCase;
import nlp4j.Keyword;

public class DefaultKeywordTestCase extends TestCase {

	public void testGetBegin() {
		DefaultKeyword kw = new DefaultKeyword();
		kw.setBegin(100);
		assertEquals(100, kw.getBegin());
	}

	public void testGetCorrelation() {
		DefaultKeyword kw = new DefaultKeyword();
		kw.setCorrelation(1.23);
		assertEquals(1.23, kw.getCorrelation());
	}

	public void testGetCount() {
		DefaultKeyword kw = new DefaultKeyword();
		kw.setCount(100);
		assertEquals(100, kw.getCount());
	}

	public void testGetEnd() {
		DefaultKeyword kw = new DefaultKeyword();
		kw.setEnd(100);
		assertEquals(100, kw.getEnd());
	}

	public void testGetFacet() {
		DefaultKeyword kw = new DefaultKeyword();
		kw.setFacet("facet1");
		assertEquals("facet1", kw.getFacet());
	}

	public void testGetLex() {
		DefaultKeyword kw = new DefaultKeyword();
		kw.setLex("lex");
		assertEquals("lex", kw.getLex());
	}

	public void testGetReading() {
		DefaultKeyword kw = new DefaultKeyword();
		kw.setReading("よみ");
		assertEquals("よみ", kw.getReading());
	}

	public void testGetSequence() {
		DefaultKeyword kw = new DefaultKeyword();
		kw.setSequence(10);
		assertEquals(10, kw.getSequence());
	}

	public void testGetStr() {
		DefaultKeyword kw = new DefaultKeyword();
		kw.setStr("abc");
		assertEquals("abc", kw.getStr());
	}

	public void testSetBegin() {
		DefaultKeyword kw = new DefaultKeyword();
		kw.setBegin(10);
		assertEquals(10, kw.getBegin());
	}

	public void testSetCorrelation() {
		DefaultKeyword kw = new DefaultKeyword();
		kw.setCorrelation(1.23);
		assertEquals(1.23, kw.getCorrelation());
	}

	public void testSetCount() {
		DefaultKeyword kw = new DefaultKeyword();
		kw.setCount(100);
		assertEquals(100, kw.getCount());
	}

	public void testSetEnd() {
		DefaultKeyword kw = new DefaultKeyword();
		kw.setEnd(100);
		assertEquals(100, kw.getEnd());
	}

	public void testSetFacet() {
		DefaultKeyword kw = new DefaultKeyword();
		kw.setFacet("facet1");
		assertEquals("facet1", kw.getFacet());
	}

	public void testSetLex() {
		DefaultKeyword kw = new DefaultKeyword();
		kw.setLex("lex");
		assertEquals("lex", kw.getLex());
	}

	public void testSetReading() {
		DefaultKeyword kw = new DefaultKeyword();
		kw.setReading("よみ");
		assertEquals("よみ", kw.getReading());
	}

	public void testSetSequence() {
		DefaultKeyword kw = new DefaultKeyword();
		kw.setSequence(10);
		assertEquals(10, kw.getSequence());
	}

	public void testSetStr() {
		DefaultKeyword kw = new DefaultKeyword();
		kw.setStr("abc");
		assertEquals("abc", kw.getStr());
	}

	public void testEqualsObject001() {
		Keyword kwd = new DefaultKeyword();
		kwd.setFacet("facet");
		kwd.setLex("lex");
		assertTrue(kwd.equals(kwd));
	}

	public void testEqualsObject002() {
		Keyword kwd1 = new DefaultKeyword();
		kwd1.setFacet("facet");
		kwd1.setLex("lex");
		Keyword kwd2 = new DefaultKeyword();
		kwd2.setFacet("facet");
		kwd2.setLex("lex");
		assertTrue(kwd1.equals(kwd2));
	}

	public void testEqualsObject003() {
		Keyword kwd1 = new DefaultKeyword();
		kwd1.setFacet("facet");
		kwd1.setLex("lex");
		Keyword kwd2 = new DefaultKeyword();
		kwd2.setFacet("facet0");
		kwd2.setLex("lex");
		assertTrue(kwd1.equals(kwd2) == false);
	}

	public void testToString() {
		Keyword kwd = new DefaultKeyword();
		System.err.println(kwd.toString());
		assertNotNull(kwd.toString());
	}

}
