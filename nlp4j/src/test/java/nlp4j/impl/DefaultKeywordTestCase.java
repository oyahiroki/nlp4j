package nlp4j.impl;

import junit.framework.TestCase;
import nlp4j.Keyword;

public class DefaultKeywordTestCase extends TestCase {

	public void testGetBegin() {
		fail("Not yet implemented");
	}

	public void testGetCorrelation() {
		fail("Not yet implemented");
	}

	public void testGetCount() {
		fail("Not yet implemented");
	}

	public void testGetEnd() {
		fail("Not yet implemented");
	}

	public void testGetFacet() {
		fail("Not yet implemented");
	}

	public void testGetLex() {
		fail("Not yet implemented");
	}

	public void testGetReading() {
		fail("Not yet implemented");
	}

	public void testGetSequence() {
		fail("Not yet implemented");
	}

	public void testGetStr() {
		fail("Not yet implemented");
	}

	public void testSetBegin() {
		fail("Not yet implemented");
	}

	public void testSetCorrelation() {
		fail("Not yet implemented");
	}

	public void testSetCount() {
		fail("Not yet implemented");
	}

	public void testSetEnd() {
		fail("Not yet implemented");
	}

	public void testSetFacet() {
		fail("Not yet implemented");
	}

	public void testSetLex() {
		fail("Not yet implemented");
	}

	public void testSetReading() {
		fail("Not yet implemented");
	}

	public void testSetSequence() {
		fail("Not yet implemented");
	}

	public void testSetStr() {
		fail("Not yet implemented");
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
		fail("Not yet implemented");
	}

}
