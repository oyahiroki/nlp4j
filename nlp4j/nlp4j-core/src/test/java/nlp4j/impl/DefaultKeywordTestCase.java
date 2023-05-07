package nlp4j.impl;

import nlp4j.Keyword;
import nlp4j.test.NLP4JTestCase;

/**
 * test:nlp4j.impl.DefaultKeyword
 * 
 * @author Hiroki Oya
 *
 */
public class DefaultKeywordTestCase extends NLP4JTestCase {

	Class<DefaultKeyword> target = DefaultKeyword.class;

	public DefaultKeywordTestCase() {
		target = DefaultKeyword.class;
	}

	/**
	 * Test Method : clone()
	 * 
	 * @throws Exception
	 */
	public void testClone001() throws Exception {
		DefaultKeyword kw = new DefaultKeyword();
		{
			kw.setLex("今日");
			kw.setStr("今日");
			kw.setFacet("名詞");
			kw.setBegin(0);
			kw.setEnd(2);
			kw.setCorrelation(1.5);
		}
		DefaultKeyword c1_cloned = (DefaultKeyword) kw.clone();
		System.err.println("Original: " + kw);
		System.err.println("Clone:" + c1_cloned);
		DefaultKeyword c2_not_cloned = kw;
		assertTrue((kw == c1_cloned) == false);
		assertTrue((kw == c2_not_cloned) == true);
		assertEquals(1.5, kw.getCorrelation());
		assertEquals(1.5, c1_cloned.getCorrelation());
	}

	/**
	 * test:equals:001
	 */
	public void testEqualsObject001() {
		Keyword kwd = new DefaultKeyword();
		{
			kwd.setFacet("facet");
			kwd.setLex("lex");
		}
		assertTrue(kwd.equals(kwd));
	}

	/**
	 * test:equals:002
	 */
	public void testEqualsObject002() {
		Keyword kwd1 = new DefaultKeyword();
		{
			kwd1.setFacet("facet");
			kwd1.setLex("lex");
		}
		Keyword kwd2 = new DefaultKeyword();
		{
			kwd2.setFacet("facet");
			kwd2.setLex("lex");
		}
		assertTrue(kwd1.equals(kwd2));
	}

	/**
	 * test:equals:003
	 */
	public void testEqualsObject003() {
		Keyword kwd1 = new DefaultKeyword();
		{
			kwd1.setFacet("facet");
			kwd1.setLex("lex");
		}
		Keyword kwd2 = new DefaultKeyword();
		{
			kwd2.setFacet("facet0");
			kwd2.setLex("lex");
		}
		assertTrue(kwd1.equals(kwd2) == false);
	}

	/**
	 * test:get:キーワードの開始位置
	 */
	public void testGetBegin001() {
		DefaultKeyword kw = new DefaultKeyword();
		{
			kw.setLex("今日");
			kw.setStr("今日");
			kw.setFacet("名詞");
			kw.setBegin(0);
			kw.setEnd(2);
		}
		System.err.println(kw);
		assertEquals(0, kw.getBegin());
	}

	/**
	 * test:get:キーワードの共起性
	 */
	public void testGetCorrelation001() {
		DefaultKeyword kw = new DefaultKeyword();
		{
			kw.setLex("今日");
			kw.setStr("今日");
			kw.setFacet("名詞");
			kw.setBegin(0);
			kw.setEnd(2);
			kw.setCorrelation(1.5);
		}
		System.err.println(kw);
		assertEquals(1.5, kw.getCorrelation());
	}

	/**
	 * test:get:キーワードのカウント
	 */
	public void testGetCount001() {
		DefaultKeyword kw = new DefaultKeyword();
		{
			kw.setLex("今日");
			kw.setStr("今日");
			kw.setFacet("名詞");
			kw.setBegin(0);
			kw.setEnd(2);
			kw.setCorrelation(1.5);
			kw.setCount(1);
		}
		System.err.println(kw);
		assertEquals(1, kw.getCount());
	}

	/**
	 * test:get:キーワードの終了位置
	 */
	public void testGetEnd001() {
		DefaultKeyword kw = new DefaultKeyword();
		{
			kw.setLex("今日");
			kw.setStr("今日");
			kw.setFacet("名詞");
			kw.setBegin(0);
			kw.setEnd(2);
		}
		System.err.println(kw);
		assertEquals(2, kw.getEnd());
	}

	/**
	 * test:get:キーワードのファセット
	 */
	public void testGetFacet001() {
		DefaultKeyword kw = new DefaultKeyword();
		{
			kw.setLex("今日");
			kw.setStr("今日");
			kw.setFacet("名詞");
			kw.setBegin(0);
			kw.setEnd(2);
		}
		System.err.println(kw);
		assertEquals("名詞", kw.getFacet());
	}

	/**
	 * test:get:キーワードの正規形
	 */
	public void testGetLex001() {
		DefaultKeyword kw = new DefaultKeyword();
		{
			kw.setLex("今日");
			kw.setStr("今日");
			kw.setFacet("名詞");
			kw.setBegin(0);
			kw.setEnd(2);
		}
		System.err.println(kw);
		assertEquals("今日", kw.getLex());
	}

	/**
	 * test:get:読み
	 */
	public void testGetReading001() {
		DefaultKeyword kw = new DefaultKeyword();
		{
			kw.setLex("今日");
			kw.setStr("今日");
			kw.setFacet("名詞");
			kw.setBegin(0);
			kw.setEnd(2);
			kw.setReading("きょう");
		}
		System.err.println(kw);
		assertEquals("きょう", kw.getReading());
	}

	/**
	 * test:get:連番
	 */
	public void testGetSequence001() {
		DefaultKeyword kw = new DefaultKeyword();
		{
			kw.setLex("今日");
			kw.setStr("今日");
			kw.setFacet("名詞");
			kw.setBegin(0);
			kw.setEnd(2);
			kw.setSequence(10);
		}
		assertEquals(10, kw.getSequence());
	}

	/**
	 * test:get:キーワードの正規形
	 */
	public void testGetStr001() {
		DefaultKeyword kw = new DefaultKeyword();
		{
			kw.setLex("今日");
			kw.setStr("今日");
			kw.setFacet("名詞");
			kw.setBegin(0);
			kw.setEnd(2);
		}
		assertEquals("今日", kw.getStr());
	}

	/**
	 * @created_at 2021-05-03
	 */
	public void testMatch001() {
		DefaultKeyword kw = new DefaultKeyword();
		{
			kw.setLex("今日");
			kw.setStr("今日");
			kw.setFacet("名詞");
			kw.setBegin(0);
			kw.setEnd(2);
		}
		DefaultKeyword rule = new DefaultKeyword();
		{
			rule.setLex("今日");
			rule.setStr("今日");
			rule.setFacet("名詞");
			rule.setBegin(0);
			rule.setEnd(2);
		}
		boolean b = kw.match(rule);
		assertEquals(true, b);
	}

	/**
	 * @created_at 2021-05-03
	 */
	public void testMatch002() {
		DefaultKeyword kw = new DefaultKeyword();
		{
			kw.setLex("今日");
			kw.setStr("今日");
			kw.setFacet("名詞");
			kw.setBegin(0);
			kw.setEnd(2);
		}
		DefaultKeyword rule = new DefaultKeyword();
		{
			rule.setFacet("名詞");
			rule.setBegin(0);
			rule.setEnd(2);
		}
		boolean b = kw.match(rule);
		assertEquals(true, b);
	}

	/**
	 * @created_at 2021-05-03
	 */
	public void testMatch003() {
		DefaultKeyword kw = new DefaultKeyword();
		{
			kw.setLex("今日");
			kw.setStr("今日");
			kw.setFacet("名詞");
			kw.setBegin(0);
			kw.setEnd(2);
		}
		DefaultKeyword rule = new DefaultKeyword();
		{
			rule.setFacet("名詞");
			rule.setBegin(0);
			rule.setEnd(2);
		}
		boolean b = kw.match(rule);
		assertEquals(true, b);
	}

	/**
	 * @created_at 2021-05-03
	 */
	public void testMatch004() {
		DefaultKeyword kw = new DefaultKeyword();
		{
			kw.setLex("今日");
			kw.setStr("今日");
			kw.setFacet("名詞");
			kw.setBegin(0);
			kw.setEnd(2);
		}
		DefaultKeyword rule = new DefaultKeyword();
		{
			rule.setLex("明日");
			rule.setStr("明日");
			rule.setFacet("名詞");
			rule.setBegin(0);
			rule.setEnd(2);
		}
		boolean b = kw.match(rule);
		assertEquals(false, b);
	}

	/**
	 * @created_at 2021-05-03
	 */
	public void testMatch005() {
		DefaultKeyword kw = new DefaultKeyword();
		{
			kw.setLex("今日");
			kw.setStr("今日");
			kw.setFacet("名詞");
			kw.setBegin(0);
			kw.setEnd(2);
		}
		DefaultKeyword rule = new DefaultKeyword();
		{
			rule.setLex("明日");
			rule.setStr("明日");
			rule.setFacet("固有名詞");
			rule.setBegin(0);
			rule.setEnd(2);
		}
		boolean b = kw.match(rule);
		assertEquals(false, b);
	}

	/**
	 * @created_at 2021-05-04
	 */
	public void testMatch006() {
		DefaultKeyword kw = new DefaultKeyword();
		{
			kw.setLex("今日");
			kw.setStr("今日XX");
			kw.setFacet("名詞");
			kw.setBegin(0);
			kw.setEnd(2);
		}
		DefaultKeyword rule = new DefaultKeyword();
		{
			rule.setLex("今日");
			rule.setStr("今日YY");
			rule.setFacet("名詞");
			rule.setBegin(0);
			rule.setEnd(2);
		}
		boolean b = kw.match(rule);
		assertEquals(true, b);
	}

	/**
	 * test:set:キーワードの開始位置
	 */
	public void testSetBegin001() {
		DefaultKeyword kw = new DefaultKeyword();
		{
			kw.setLex("今日");
			kw.setStr("今日");
			kw.setFacet("名詞");
			kw.setBegin(0);
			kw.setEnd(2);
		}
		assertEquals(0, kw.getBegin());
	}

	/**
	 * test:set:キーワードの共起性
	 */
	public void testSetCorrelation001() {
		DefaultKeyword kw = new DefaultKeyword();
		{
			kw.setLex("今日");
			kw.setStr("今日");
			kw.setFacet("名詞");
			kw.setBegin(0);
			kw.setEnd(2);
			kw.setCorrelation(1.5);
		}
		System.err.println(kw);
		assertEquals(1.5, kw.getCorrelation());
	}

	/**
	 * test:set:キーワードのカウント
	 */
	public void testSetCount001() {
		DefaultKeyword kw = new DefaultKeyword();
		{
			kw.setLex("今日");
			kw.setStr("今日");
			kw.setFacet("名詞");
			kw.setBegin(0);
			kw.setEnd(2);
			kw.setCorrelation(1.5);
			kw.setCount(1);
		}
		System.err.println(kw);
		assertEquals(1, kw.getCount());
	}

	/**
	 * test:set:キーワードの終了位置
	 */
	public void testSetEnd001() {
		DefaultKeyword kw = new DefaultKeyword();
		{
			kw.setLex("今日");
			kw.setStr("今日");
			kw.setFacet("名詞");
			kw.setBegin(0);
			kw.setEnd(2);
		}
		System.err.println(kw);
		assertEquals(2, kw.getEnd());
	}

	/**
	 * test:set:キーワードのファセット
	 */
	public void testSetFacet001() {
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
	public void testSetLex001() {
		DefaultKeyword kw = new DefaultKeyword();
		{
			kw.setLex("今日");
			kw.setStr("今日");
			kw.setFacet("名詞");
			kw.setBegin(0);
			kw.setEnd(2);
		}
		System.err.println(kw);
		assertEquals("今日", kw.getLex());
	}

	/**
	 * test:set:読み
	 */
	public void testSetReading001() {
		DefaultKeyword kw = new DefaultKeyword();
		{
			kw.setLex("今日");
			kw.setStr("今日");
			kw.setFacet("名詞");
			kw.setBegin(0);
			kw.setEnd(2);
			kw.setReading("きょう");
		}
		System.err.println(kw);
		assertEquals("きょう", kw.getReading());
	}

	/**
	 * test:set:連番
	 */
	public void testSetSequence001() {
		DefaultKeyword kw = new DefaultKeyword();
		{
			kw.setLex("今日");
			kw.setStr("今日");
			kw.setFacet("名詞");
			kw.setBegin(0);
			kw.setEnd(2);
			kw.setSequence(10);
		}
		assertEquals(10, kw.getSequence());
	}

	/**
	 * test:set:キーワードの正規形
	 */
	public void testSetStr001() {
		DefaultKeyword kw = new DefaultKeyword();
		{
			kw.setLex("今日");
			kw.setStr("今日");
			kw.setFacet("名詞");
			kw.setBegin(0);
			kw.setEnd(2);
		}
		assertEquals("今日", kw.getStr());
	}

	/**
	 * test:toString
	 */
	public void testToString001() {
		DefaultKeyword kw = new DefaultKeyword();
		{
			kw.setLex("今日");
			kw.setStr("今日");
			kw.setFacet("名詞");
			kw.setBegin(0);
			kw.setEnd(2);
		}
		System.err.println(kw);
		assertNotNull(kw.toString());
	}

}
