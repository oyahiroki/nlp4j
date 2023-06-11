package nlp4j.impl;

import junit.framework.TestCase;
import nlp4j.Keyword;
import nlp4j.KeywordWithDependency;
import nlp4j.test.NLP4JTestCase;
import nlp4j.util.XmlUtils;

/**
 * Test for DefaultKeywordWithDependency
 * 
 * @author Hiroki Oya
 * @created_at 2021-05-04
 */
public class DefaultKeywordWithDependencyTestCase extends NLP4JTestCase {

	/**
	 * DefaultKeywordWithDependency()
	 */
	public DefaultKeywordWithDependencyTestCase() {
		target = DefaultKeywordWithDependency.class;
	}

	public void test001() throws Exception {
		DefaultKeywordWithDependency kwd1 = new DefaultKeywordWithDependency();
		{
			kwd1.setLex("a");
		}
		DefaultKeywordWithDependency kwd2 = new DefaultKeywordWithDependency();
		{
			kwd2.setLex("b");
			kwd1.addChild(kwd2);
		}
		DefaultKeywordWithDependency kwd3 = new DefaultKeywordWithDependency();
		{
			kwd3.setLex("c");
			kwd1.addChild(kwd3);
		}
		DefaultKeywordWithDependency kwd4 = new DefaultKeywordWithDependency();
		{
			kwd4.setLex("d");
			kwd3.addChild(kwd4);
		}
		DefaultKeywordWithDependency kwd5 = new DefaultKeywordWithDependency();
		{
			kwd5.setLex("e");
			kwd3.addChild(kwd5);
		}

		System.err.println(kwd1.toStringAsXml());

	}

	public void testEqualsObject001() {
		description = "Test for equals()";
		Keyword kwd = new DefaultKeywordWithDependency();
		{
			kwd.setFacet("facet");
			kwd.setLex("lex");
		}
		assertTrue(kwd.equals(kwd));
	}

	public void testEqualsObject002() {
		description = "Test for equals()";
		Keyword kwd1 = new DefaultKeywordWithDependency();
		{
			kwd1.setFacet("facet");
			kwd1.setLex("lex");
		}
		Keyword kwd2 = new DefaultKeywordWithDependency();
		{
			kwd2.setFacet("facet");
			kwd2.setLex("lex");
		}
		assertTrue(kwd1.equals(kwd2));
	}

	public void testToString001() {
		description = "Test for toString()";
		DefaultKeywordWithDependency kwd1 = new DefaultKeywordWithDependency();
		{
			kwd1.setLex("aaa-1");
			kwd1.setStr("aaa-2");
		}
		String s = kwd1.toString();
		System.err.println(s);
	}

	public void testClone001() throws Exception {

		DefaultKeywordWithDependency kwd1 = new DefaultKeywordWithDependency();
		{
			kwd1.setLex("aaa");
		}
		DefaultKeywordWithDependency kwd2 = new DefaultKeywordWithDependency();
		{
			kwd2.setLex("bbb");
		}
		kwd1.addChild(kwd2);

		System.err.println(kwd1.toStringAsXml());

		assertEquals(1, kwd1.getChildren().size());

		DefaultKeywordWithDependency kwdCloned = kwd1.clone();

		assertEquals(1, kwd1.getChildren().size());

		int size = kwd1.getChildren().size();

		assertEquals(1, size);

		for (int n = 0; n < size; n++) {
			KeywordWithDependency c1 = kwd1.getChildren().get(n);
			System.err.println(c1.toStringAsXml());
			KeywordWithDependency c2 = kwdCloned.getChildren().get(n);
			assertFalse(c1 == c2);
		}

	}

	public void testAddChild001() {
		DefaultKeywordWithDependency kwd1 = new DefaultKeywordWithDependency();
		{
			kwd1.setLex("aaa");
		}

		DefaultKeywordWithDependency kwd2 = new DefaultKeywordWithDependency();
		{
			kwd2.setLex("bbb");
		}

		kwd1.addChild(kwd2);

		System.err.println(kwd1.toString());
		System.err.println(kwd1.toStringAsXml());

		assertEquals(1, kwd1.getChildren().size());
	}

	public void testSize001() {
		DefaultKeywordWithDependency kwd1 = new DefaultKeywordWithDependency();
		{
			kwd1.setLex("aaa");
		}
		DefaultKeywordWithDependency kwd2 = new DefaultKeywordWithDependency();
		{
			kwd2.setLex("bbb");
		}
		kwd1.addChild(kwd2);
		assertEquals(2, kwd1.size());
	}

	public void testSize002() {
		DefaultKeywordWithDependency kwd1 = new DefaultKeywordWithDependency();
		{
			kwd1.setLex("aaa");
		}
		DefaultKeywordWithDependency kwd2 = new DefaultKeywordWithDependency();
		{
			kwd2.setLex("bbb");
		}
		kwd1.addChild(kwd2);
		assertEquals(1, kwd2.size());
	}

	public void testSize003() {
		DefaultKeywordWithDependency kwd1 = new DefaultKeywordWithDependency();
		{
			kwd1.setLex("aaa");
		}
		for (int n = 0; n < 10; n++) {
			DefaultKeywordWithDependency kwd2 = new DefaultKeywordWithDependency();
			{
				kwd2.setLex("bbb" + n);
			}
			kwd1.addChild(kwd2);
		}
		assertEquals(11, kwd1.size());
	}

	public void testGetChildren001() {
		DefaultKeywordWithDependency kwd1 = new DefaultKeywordWithDependency();
		{
			kwd1.setLex("aaa");
		}

		DefaultKeywordWithDependency kwd2 = new DefaultKeywordWithDependency();
		{
			kwd2.setLex("bbb");
		}

		kwd1.addChild(kwd2);
		System.err.println(kwd1.toString());
		System.err.println(kwd1.toStringAsXml());
		assertEquals(1, kwd1.getChildren().size());
		assertEquals(0, kwd2.getChildren().size());
	}

	public void testGetDependencyKey001() {
		DefaultKeywordWithDependency kwd1 = new DefaultKeywordWithDependency();
		{
			kwd1.setLex("aaa");
		}
		assertNull(kwd1.getDependencyKey());

		kwd1.setDependencyKey("key1");
		assertEquals("key1", kwd1.getDependencyKey());
	}

	public void testGetDepth001() {
		DefaultKeywordWithDependency kwd1 = new DefaultKeywordWithDependency();
		kwd1.setLex("aaa");

		DefaultKeywordWithDependency kwd2 = new DefaultKeywordWithDependency();
		kwd2.setLex("bbb");

		kwd1.addChild(kwd2);

		System.err.println(kwd2.getDepth());
		assertEquals(1, kwd2.getDepth());
	}

	public void testGetDepth002() {
		DefaultKeywordWithDependency kwd1 = new DefaultKeywordWithDependency();
		kwd1.setLex("aaa");

		DefaultKeywordWithDependency kwd2 = new DefaultKeywordWithDependency();
		kwd2.setLex("bbb");

		kwd2.setParent(kwd1);

		System.err.println(kwd2.getDepth());
		assertEquals(1, kwd2.getDepth());
	}

	public void testGetParent001() {
		DefaultKeywordWithDependency kwd1 = new DefaultKeywordWithDependency();
		{
			kwd1.setLex("aaa");
		}
		DefaultKeywordWithDependency kwd2 = new DefaultKeywordWithDependency();
		{
			kwd2.setLex("bbb");
		}
		kwd2.setParent(kwd1);
		assertEquals(kwd1, kwd2.getParent());
	}

	public void testGetParentInt001() {
		DefaultKeywordWithDependency kwd1 = new DefaultKeywordWithDependency();
		{
			kwd1.setLex("aaa");
		}
		DefaultKeywordWithDependency kwd2 = new DefaultKeywordWithDependency();
		{
			kwd2.setLex("bbb");
			kwd2.setParent(kwd1);
		}
		System.err.println(kwd2.getParent(-1));
		System.err.println(kwd2.getParent(0));
		System.err.println(kwd2.getParent(1));
		System.err.println(kwd2.getParent(2));
	}

	public void testGetRoot001() {
		DefaultKeywordWithDependency kwd1 = new DefaultKeywordWithDependency();
		kwd1.setLex("aaa");

		assertEquals(kwd1, kwd1.getRoot());
	}

	public void testGetRoot002() {
		DefaultKeywordWithDependency kwd1 = new DefaultKeywordWithDependency();
		kwd1.setLex("aaa");

		DefaultKeywordWithDependency kwd2 = new DefaultKeywordWithDependency();
		kwd2.setLex("bbb");

		kwd2.setParent(kwd1);

		assertEquals(kwd1.getLex(), kwd2.getRoot().getLex());
	}

	public void testGetRoot003() {
		DefaultKeywordWithDependency kwd1 = new DefaultKeywordWithDependency();
		kwd1.setLex("aaa");

		DefaultKeywordWithDependency kwd2 = new DefaultKeywordWithDependency();
		kwd2.setLex("bbb");

		DefaultKeywordWithDependency kwd3 = new DefaultKeywordWithDependency();
		kwd3.setLex("ccc");

		kwd2.setParent(kwd1);
		kwd3.setParent(kwd2);

		assertEquals(kwd1, kwd3.getRoot());
	}

	public void testHasChild001() {
		DefaultKeywordWithDependency kwd1 = new DefaultKeywordWithDependency();
		{
			kwd1.setLex("aaa");
		}
		DefaultKeywordWithDependency kwd2 = new DefaultKeywordWithDependency();
		{
			kwd2.setLex("bbb");
			kwd2.setParent(kwd1);
		}
		assertEquals(true, kwd1.hasChild());
		assertEquals(false, kwd2.hasChild());
	}

	public void testHasParent001() {
		DefaultKeywordWithDependency kwd1 = new DefaultKeywordWithDependency();
		{
			kwd1.setLex("aaa");
		}
		DefaultKeywordWithDependency kwd2 = new DefaultKeywordWithDependency();
		{
			kwd2.setLex("bbb");
			kwd2.setParent(kwd1);
		}
		assertEquals(false, kwd1.hasParent());
		assertEquals(true, kwd2.hasParent());
	}

	public void testIsRoot001() {
		DefaultKeywordWithDependency kwd1 = new DefaultKeywordWithDependency();
		kwd1.setLex("aaa");

		DefaultKeywordWithDependency kwd2 = new DefaultKeywordWithDependency();
		kwd2.setLex("bbb");

		kwd2.setParent(kwd1);
//		kwd1.addChild(kwd2);

		System.err.println(kwd1.isRoot());
		System.err.println(kwd2.isRoot());

		assertEquals(true, kwd1.isRoot());
		assertEquals(false, kwd2.isRoot());
	}

	public void testIsRoot002() {
		DefaultKeywordWithDependency kwd1 = new DefaultKeywordWithDependency();
		kwd1.setLex("aaa");

		DefaultKeywordWithDependency kwd2 = new DefaultKeywordWithDependency();
		kwd2.setLex("bbb");

//		kwd2.setParent(kwd1);
		kwd1.addChild(kwd2);

		System.err.println(kwd1.isRoot());
		System.err.println(kwd2.isRoot());

		assertEquals(true, kwd1.isRoot());
		assertEquals(false, kwd2.isRoot());
	}

	public void testSetDependencyKey001() {
		DefaultKeywordWithDependency kwd1 = new DefaultKeywordWithDependency();
		{
			kwd1.setLex("aaa");
			kwd1.setDependencyKey("key1");
		}
		assertEquals("key1", kwd1.getDependencyKey());
	}

	public void testSetParent001() {
		DefaultKeywordWithDependency kwd1 = new DefaultKeywordWithDependency();
		{
			kwd1.setLex("aaa");
		}
		DefaultKeywordWithDependency kwd2 = new DefaultKeywordWithDependency();
		{
			kwd2.setLex("bbb");
			kwd2.setParent(kwd1);
		}
		assertEquals(kwd1, kwd2.getParent());
		assertEquals(null, kwd1.getParent());
	}

	public void testToStringAsDependencyTree001() {
		DefaultKeywordWithDependency kwd1 = new DefaultKeywordWithDependency();
		{
			kwd1.setLex("aaa_lex");
			kwd1.setStr("aaa_str");
		}
		DefaultKeywordWithDependency kwd2 = new DefaultKeywordWithDependency();
		{
			kwd2.setLex("bbb_lex");
			kwd2.setStr("bbb_str");
		}
		kwd2.setParent(kwd1);
		String s = kwd1.toStringAsDependencyTree();
		System.err.println(s);
	}

	public void testToStringAsDependencyList001() {
		DefaultKeywordWithDependency kwd1 = new DefaultKeywordWithDependency();
		{
			kwd1.setLex("aaa_lex");
			kwd1.setStr("aaa_str");
		}
		DefaultKeywordWithDependency kwd2 = new DefaultKeywordWithDependency();
		{
			kwd2.setLex("bbb_lex");
			kwd2.setStr("bbb_str");
		}
		DefaultKeywordWithDependency kwd3 = new DefaultKeywordWithDependency();
		{
			kwd3.setLex("ccc_lex");
			kwd3.setStr("ccc_str");
		}
		kwd2.setParent(kwd1);
		kwd3.setParent(kwd2);
		String s = kwd1.toStringAsDependencyList();
		System.err.println(s);
	}

	public void testToStringAsXml001() {
		DefaultKeywordWithDependency kwd1 = new DefaultKeywordWithDependency();
		{
			kwd1.setLex("aaa_lex");
			kwd1.setStr("aaa_str");
		}
		DefaultKeywordWithDependency kwd2 = new DefaultKeywordWithDependency();
		{
			kwd2.setLex("bbb_lex");
			kwd2.setStr("bbb_str");
		}

		kwd2.setParent(kwd1);
		String s = kwd1.toStringAsXml();
		System.err.println(s);
	}

	public void testToStringAsXmlInt001() {
		DefaultKeywordWithDependency kwd1 = new DefaultKeywordWithDependency();
		{
			kwd1.setLex("aaa_lex");
			kwd1.setStr("aaa_str");
		}
		DefaultKeywordWithDependency kwd2 = new DefaultKeywordWithDependency();
		{
			kwd2.setLex("bbb_lex");
			kwd2.setStr("bbb_str");
		}
		kwd2.setParent(kwd1);
		String s = kwd1.toStringAsXml(0);
		System.err.println(s);
		System.err.println(XmlUtils.prettyFormatXml(s));
	}

	public void testToStringAsXmlInt002() {
		DefaultKeywordWithDependency kwd1 = new DefaultKeywordWithDependency();
		kwd1.setLex("<test>");
		kwd1.setStr("<test>");
		DefaultKeywordWithDependency kwd2 = new DefaultKeywordWithDependency();
		kwd2.setLex("a&b");
		kwd2.setStr("a&b");

		kwd2.setParent(kwd1);
		String s = kwd1.toStringAsXml(0);
		System.err.println(s);

		System.err.println(XmlUtils.prettyFormatXml(s));
	}

}
