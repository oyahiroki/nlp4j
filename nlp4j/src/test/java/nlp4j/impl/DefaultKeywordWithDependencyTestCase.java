package nlp4j.impl;

import junit.framework.TestCase;

public class DefaultKeywordWithDependencyTestCase extends TestCase {

	@SuppressWarnings("rawtypes")
	Class target = DefaultKeywordWithDependency.class;

	public void testToString() {
		DefaultKeywordWithDependency kwd1 = new DefaultKeywordWithDependency();
		kwd1.setLex("aaa-1");
		kwd1.setStr("aaa-2");
		String s = kwd1.toString();
		System.err.println(s);
	}

	public void testAddChild() {
		DefaultKeywordWithDependency kwd1 = new DefaultKeywordWithDependency();
		kwd1.setLex("aaa");

		DefaultKeywordWithDependency kwd2 = new DefaultKeywordWithDependency();
		kwd2.setLex("bbb");

		kwd1.addChild(kwd2);

		System.err.println(kwd1.toString());
		System.err.println(kwd1.toStringAsXml());

		assertEquals(1, kwd1.getChildren().size());
	}

	public void testGetChildren() {
		DefaultKeywordWithDependency kwd1 = new DefaultKeywordWithDependency();
		kwd1.setLex("aaa");

		DefaultKeywordWithDependency kwd2 = new DefaultKeywordWithDependency();
		kwd2.setLex("bbb");

		kwd1.addChild(kwd2);

		System.err.println(kwd1.toString());
		System.err.println(kwd1.toStringAsXml());

		assertEquals(1, kwd1.getChildren().size());
		assertEquals(0, kwd2.getChildren().size());
	}

	public void testGetDependencyKey() {
		DefaultKeywordWithDependency kwd1 = new DefaultKeywordWithDependency();
		kwd1.setLex("aaa");
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

	public void testGetParent() {
		DefaultKeywordWithDependency kwd1 = new DefaultKeywordWithDependency();
		kwd1.setLex("aaa");

		DefaultKeywordWithDependency kwd2 = new DefaultKeywordWithDependency();
		kwd2.setLex("bbb");

		kwd2.setParent(kwd1);

		assertEquals(kwd1, kwd2.getParent());

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

		assertEquals(kwd1, kwd2.getRoot());
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

	public void testHasChild() {
		DefaultKeywordWithDependency kwd1 = new DefaultKeywordWithDependency();
		kwd1.setLex("aaa");

		DefaultKeywordWithDependency kwd2 = new DefaultKeywordWithDependency();
		kwd2.setLex("bbb");

		kwd2.setParent(kwd1);

		assertEquals(true, kwd1.hasChild());
		assertEquals(false, kwd2.hasChild());
	}

	public void testHasParent() {
		DefaultKeywordWithDependency kwd1 = new DefaultKeywordWithDependency();
		kwd1.setLex("aaa");

		DefaultKeywordWithDependency kwd2 = new DefaultKeywordWithDependency();
		kwd2.setLex("bbb");

		kwd2.setParent(kwd1);

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

	public void testSetDependencyKey() {
		DefaultKeywordWithDependency kwd1 = new DefaultKeywordWithDependency();
		kwd1.setLex("aaa");
		kwd1.setDependencyKey("key1");
		assertEquals("key1", kwd1.getDependencyKey());
	}

	public void testSetParent() {
		DefaultKeywordWithDependency kwd1 = new DefaultKeywordWithDependency();
		kwd1.setLex("aaa");

		DefaultKeywordWithDependency kwd2 = new DefaultKeywordWithDependency();
		kwd2.setLex("bbb");

		kwd2.setParent(kwd1);

		assertEquals(kwd1, kwd2.getParent());
		assertEquals(null, kwd1.getParent());
	}

	public void testToStringAsDependencyTree() {
		DefaultKeywordWithDependency kwd1 = new DefaultKeywordWithDependency();
		kwd1.setLex("aaa_lex");
		kwd1.setStr("aaa_str");
		DefaultKeywordWithDependency kwd2 = new DefaultKeywordWithDependency();
		kwd2.setLex("bbb_lex");
		kwd2.setStr("bbb_str");

		kwd2.setParent(kwd1);
		String s = kwd1.toStringAsDependencyTree();
		System.err.println(s);
	}

	public void testToStringAsDependencyList() {
		DefaultKeywordWithDependency kwd1 = new DefaultKeywordWithDependency();
		kwd1.setLex("aaa_lex");
		kwd1.setStr("aaa_str");
		DefaultKeywordWithDependency kwd2 = new DefaultKeywordWithDependency();
		kwd2.setLex("bbb_lex");
		kwd2.setStr("bbb_str");

		kwd2.setParent(kwd1);
		String s = kwd1.toStringAsDependencyList();
		System.err.println(s);
	}

	public void testToStringAsXml() {
		DefaultKeywordWithDependency kwd1 = new DefaultKeywordWithDependency();
		kwd1.setLex("aaa_lex");
		kwd1.setStr("aaa_str");
		DefaultKeywordWithDependency kwd2 = new DefaultKeywordWithDependency();
		kwd2.setLex("bbb_lex");
		kwd2.setStr("bbb_str");

		kwd2.setParent(kwd1);
		String s = kwd1.toStringAsXml();
		System.err.println(s);
	}

	public void testToStringAsXmlInt() {
		DefaultKeywordWithDependency kwd1 = new DefaultKeywordWithDependency();
		kwd1.setLex("aaa_lex");
		kwd1.setStr("aaa_str");
		DefaultKeywordWithDependency kwd2 = new DefaultKeywordWithDependency();
		kwd2.setLex("bbb_lex");
		kwd2.setStr("bbb_str");

		kwd2.setParent(kwd1);
		String s = kwd1.toStringAsXml(0);
		System.err.println(s);
	}

}
