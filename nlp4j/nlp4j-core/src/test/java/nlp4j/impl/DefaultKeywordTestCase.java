package nlp4j.impl;

import junit.framework.TestCase;
import nlp4j.Keyword;

/**
 * test:nlp4j.impl.DefaultKeyword
 * 
 * @author Hiroki Oya
 *
 */
public class DefaultKeywordTestCase extends TestCase {

	/**
	 * test:get:キーワードの開始位置
	 */
	public void testGetBegin() {
		DefaultKeyword kw = new DefaultKeyword();
		kw.setLex("今日");
		kw.setStr("今日");
		kw.setFacet("名詞");
		kw.setBegin(0);
		kw.setEnd(2);
		System.err.println(kw);
		assertEquals(0, kw.getBegin());
	}

	/**
	 * test:get:キーワードの共起性
	 */
	public void testGetCorrelation() {
		DefaultKeyword kw = new DefaultKeyword();
		kw.setLex("今日");
		kw.setStr("今日");
		kw.setFacet("名詞");
		kw.setBegin(0);
		kw.setEnd(2);
		kw.setCorrelation(1.5);
		System.err.println(kw);
		assertEquals(1.5, kw.getCorrelation());
	}

	/**
	 * test:get:キーワードのカウント
	 */
	public void testGetCount() {
		DefaultKeyword kw = new DefaultKeyword();
		kw.setLex("今日");
		kw.setStr("今日");
		kw.setFacet("名詞");
		kw.setBegin(0);
		kw.setEnd(2);
		kw.setCorrelation(1.5);
		kw.setCount(1);
		System.err.println(kw);
		assertEquals(1, kw.getCount());
	}

	/**
	 * test:get:キーワードの終了位置
	 */
	public void testGetEnd() {
		DefaultKeyword kw = new DefaultKeyword();
		kw.setLex("今日");
		kw.setStr("今日");
		kw.setFacet("名詞");
		kw.setBegin(0);
		kw.setEnd(2);
		System.err.println(kw);
		assertEquals(2, kw.getEnd());
	}

	/**
	 * test:get:キーワードのファセット
	 */
	public void testGetFacet() {
		DefaultKeyword kw = new DefaultKeyword();
		kw.setLex("今日");
		kw.setStr("今日");
		kw.setFacet("名詞");
		kw.setBegin(0);
		kw.setEnd(2);
		System.err.println(kw);
		assertEquals("名詞", kw.getFacet());
	}

	/**
	 * test:get:キーワードの正規形
	 */
	public void testGetLex() {
		DefaultKeyword kw = new DefaultKeyword();
		kw.setLex("今日");
		kw.setStr("今日");
		kw.setFacet("名詞");
		kw.setBegin(0);
		kw.setEnd(2);
		System.err.println(kw);
		assertEquals("今日", kw.getLex());
	}

	/**
	 * test:get:読み
	 */
	public void testGetReading() {
		DefaultKeyword kw = new DefaultKeyword();
		kw.setLex("今日");
		kw.setStr("今日");
		kw.setFacet("名詞");
		kw.setBegin(0);
		kw.setEnd(2);
		kw.setReading("きょう");
		System.err.println(kw);
		assertEquals("きょう", kw.getReading());
	}

	/**
	 * test:get:連番
	 */
	public void testGetSequence() {
		DefaultKeyword kw = new DefaultKeyword();
		kw.setLex("今日");
		kw.setStr("今日");
		kw.setFacet("名詞");
		kw.setBegin(0);
		kw.setEnd(2);
		kw.setSequence(10);
		assertEquals(10, kw.getSequence());
	}

	/**
	 * test:get:キーワードの正規形
	 */
	public void testGetStr() {
		DefaultKeyword kw = new DefaultKeyword();
		kw.setLex("今日");
		kw.setStr("今日");
		kw.setFacet("名詞");
		kw.setBegin(0);
		kw.setEnd(2);
		assertEquals("今日", kw.getStr());
	}

	/**
	 * test:set:キーワードの開始位置
	 */
	public void testSetBegin() {
		DefaultKeyword kw = new DefaultKeyword();
		kw.setLex("今日");
		kw.setStr("今日");
		kw.setFacet("名詞");
		kw.setBegin(0);
		kw.setEnd(2);
		assertEquals(0, kw.getBegin());
	}

	/**
	 * test:set:キーワードの共起性
	 */
	public void testSetCorrelation() {
		DefaultKeyword kw = new DefaultKeyword();
		kw.setLex("今日");
		kw.setStr("今日");
		kw.setFacet("名詞");
		kw.setBegin(0);
		kw.setEnd(2);
		kw.setCorrelation(1.5);
		System.err.println(kw);
		assertEquals(1.5, kw.getCorrelation());
	}

	/**
	 * test:set:キーワードのカウント
	 */
	public void testSetCount() {
		DefaultKeyword kw = new DefaultKeyword();
		kw.setLex("今日");
		kw.setStr("今日");
		kw.setFacet("名詞");
		kw.setBegin(0);
		kw.setEnd(2);
		kw.setCorrelation(1.5);
		kw.setCount(1);
		System.err.println(kw);
		assertEquals(1, kw.getCount());
	}

	/**
	 * test:set:キーワードの終了位置
	 */
	public void testSetEnd() {
		DefaultKeyword kw = new DefaultKeyword();
		kw.setLex("今日");
		kw.setStr("今日");
		kw.setFacet("名詞");
		kw.setBegin(0);
		kw.setEnd(2);
		System.err.println(kw);
		assertEquals(2, kw.getEnd());
	}

	/**
	 * test:set:キーワードのファセット
	 */
	public void testSetFacet() {
		DefaultKeyword kw = new DefaultKeyword();
		kw.setLex("今日");
		kw.setStr("今日");
		kw.setFacet("名詞");
		kw.setBegin(0);
		kw.setEnd(2);
		System.err.println(kw);
		assertEquals("名詞", kw.getFacet());
	}

	/**
	 * test:set:キーワードの正規形
	 */
	public void testSetLex() {
		DefaultKeyword kw = new DefaultKeyword();
		kw.setLex("今日");
		kw.setStr("今日");
		kw.setFacet("名詞");
		kw.setBegin(0);
		kw.setEnd(2);
		System.err.println(kw);
		assertEquals("今日", kw.getLex());
	}

	/**
	 * test:set:読み
	 */
	public void testSetReading() {
		DefaultKeyword kw = new DefaultKeyword();
		kw.setLex("今日");
		kw.setStr("今日");
		kw.setFacet("名詞");
		kw.setBegin(0);
		kw.setEnd(2);
		kw.setReading("きょう");
		System.err.println(kw);
		assertEquals("きょう", kw.getReading());
	}

	/**
	 * test:set:連番
	 */
	public void testSetSequence() {
		DefaultKeyword kw = new DefaultKeyword();
		kw.setLex("今日");
		kw.setStr("今日");
		kw.setFacet("名詞");
		kw.setBegin(0);
		kw.setEnd(2);
		kw.setSequence(10);
		assertEquals(10, kw.getSequence());
	}

	/**
	 * test:set:キーワードの正規形
	 */
	public void testSetStr() {
		DefaultKeyword kw = new DefaultKeyword();
		kw.setLex("今日");
		kw.setStr("今日");
		kw.setFacet("名詞");
		kw.setBegin(0);
		kw.setEnd(2);
		assertEquals("今日", kw.getStr());
	}

	/**
	 * test:equals:001
	 */
	public void testEqualsObject001() {
		Keyword kwd = new DefaultKeyword();
		kwd.setFacet("facet");
		kwd.setLex("lex");
		assertTrue(kwd.equals(kwd));
	}

	/**
	 * test:equals:002
	 */
	public void testEqualsObject002() {
		Keyword kwd1 = new DefaultKeyword();
		kwd1.setFacet("facet");
		kwd1.setLex("lex");
		Keyword kwd2 = new DefaultKeyword();
		kwd2.setFacet("facet");
		kwd2.setLex("lex");
		assertTrue(kwd1.equals(kwd2));
	}

	/**
	 * test:equals:003
	 */
	public void testEqualsObject003() {
		Keyword kwd1 = new DefaultKeyword();
		kwd1.setFacet("facet");
		kwd1.setLex("lex");
		Keyword kwd2 = new DefaultKeyword();
		kwd2.setFacet("facet0");
		kwd2.setLex("lex");
		assertTrue(kwd1.equals(kwd2) == false);
	}

	/**
	 * test:toString
	 */
	public void testToString() {
		DefaultKeyword kw = new DefaultKeyword();
		kw.setLex("今日");
		kw.setStr("今日");
		kw.setFacet("名詞");
		kw.setBegin(0);
		kw.setEnd(2);
		System.err.println(kw);
		assertNotNull(kw.toString());
	}

}
