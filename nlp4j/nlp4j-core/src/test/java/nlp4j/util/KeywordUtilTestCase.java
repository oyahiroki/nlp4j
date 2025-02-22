package nlp4j.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import nlp4j.Keyword;
import nlp4j.KeywordWithDependency;
import nlp4j.impl.DefaultKeyword;
import nlp4j.impl.DefaultKeywordWithDependency;
import nlp4j.test.NLP4JTestCase;

@SuppressWarnings("javadoc")
public class KeywordUtilTestCase extends NLP4JTestCase {

	public KeywordUtilTestCase() {
		target = KeywordUtil.class;
	}

	public void testBuilder001() throws Exception {

		KeywordUtil.Builder<DefaultKeyword> builder = new KeywordUtil.Builder<>(DefaultKeyword.class);

		DefaultKeyword kwd = (DefaultKeyword) builder.newKwd().facet("記号").lex("。").str("。").upos("SYM").begin(9)
				.end(10).build();

		System.err.println(kwd);

	}

	public void testBuilder002() throws Exception {

		KeywordUtil.Builder<DefaultKeywordWithDependency> builder = new KeywordUtil.Builder<>(
				DefaultKeywordWithDependency.class);

		DefaultKeywordWithDependency kwd = (DefaultKeywordWithDependency) builder.newKwd().facet("記号").lex("。").str("。")
				.upos("SYM").begin(9).end(10).build();
		{
			DefaultKeywordWithDependency kwd2 = (DefaultKeywordWithDependency) builder.newKwd() //
					.facet("助動詞").lex("です").str("です").upos("AUX").begin(7).end(9).build();
			kwd.addChild(kwd2);
		}

		System.err.println(kwd.toStringAsXml());

	}

	public void testToKeywordList001() {
		DefaultKeywordWithDependency kwd1 = new DefaultKeywordWithDependency();
		{
			kwd1.setLex("aaa");
			kwd1.setBegin(0);
		}
		DefaultKeywordWithDependency kwd2 = new DefaultKeywordWithDependency();
		{
			kwd2.setLex("bbb");
			kwd2.setBegin(3);
		}
		DefaultKeywordWithDependency kwd3 = new DefaultKeywordWithDependency();
		{
			kwd3.setLex("ccc");
			kwd3.setBegin(6);
		}
		kwd2.setParent(kwd1);
		kwd3.setParent(kwd2);

		List<Keyword> kwd = KeywordUtil.toKeywordList(kwd1);

		assertEquals(0, kwd.get(0).getBegin());
		assertEquals("aaa", kwd.get(0).getLex());
		assertEquals(3, kwd.get(1).getBegin());
		assertEquals("bbb", kwd.get(1).getLex());
		assertEquals(6, kwd.get(2).getBegin());
		assertEquals("ccc", kwd.get(2).getLex());

	}

	public void testFromXml001() throws Exception {
		File xmlFile = new File("src/test/resources/nlp4j.util/keyword-with-dependency-001.xml");
		List<KeywordWithDependency> kwds = KeywordUtil.fromXml(xmlFile);
		System.err.println(kwds.size());
	}

	public void testFromXml002() throws Exception {
		File xmlFile = new File("src/test/resources/nlp4j.util/keyword-with-dependency-002.xml");
		List<KeywordWithDependency> kwds = KeywordUtil.fromXml(xmlFile);
		System.err.println(kwds.size());
	}

	public void testFromXml003() throws Exception {
		File xmlFile = new File("src/test/resources/nlp4j.util/keyword-with-dependency-003.xml");
		List<KeywordWithDependency> kwds = KeywordUtil.fromXml(xmlFile);
		System.err.println(kwds.size());
	}

	public void testFromXml004() throws Exception {
		File xmlFile = new File("src/test/resources/nlp4j.util/keyword-with-dependency-004.xml");
		List<KeywordWithDependency> kwds = KeywordUtil.fromXml(xmlFile);
		System.err.println(kwds.size());
	}

	/**
	 * creatd_at: 2022-09-14
	 * 
	 * @throws Exception
	 */
	public void testToLexStringList001() throws Exception {
		File xmlFile = new File("src/test/resources/nlp4j.util/keyword-with-dependency-001.xml");
		String title = "生物";
		List<KeywordWithDependency> kwds = KeywordUtil.fromXml(xmlFile);
		KeywordWithDependency kw = kwds.get(0);
		List<List<Keyword>> listList = KeywordUtil.toListList(kw);
		for (List<Keyword> list : listList) {
			boolean b = KeywordUtil.containsLex(list, title);
			String s = KeywordUtil.toStrString(list);
			if (b == false) {
				System.err.println(s);
			} else {
				System.err.println("contains title -> " + s);
			}
		}
	}

	/**
	 * creatd_at: 2022-09-14
	 * 
	 * @throws Exception
	 */
	public void testToLexStringList002() throws Exception {
		File xmlFile = new File("src/test/resources/nlp4j.util/keyword-with-dependency-002.xml");
		String title = "コケ植物";
		List<KeywordWithDependency> kwds = KeywordUtil.fromXml(xmlFile);
		KeywordWithDependency kw = kwds.get(0);
		List<List<Keyword>> listList = KeywordUtil.toListList(kw);
		for (List<Keyword> list : listList) {
			boolean b = KeywordUtil.containsLex(list, title);
			String s = KeywordUtil.toStrString(list);
			if (b == false) {
				System.err.println(s);
			} else {
				System.err.println("contains title -> " + s);
			}
		}
	}

	/**
	 * creatd_at: 2022-09-14
	 * 
	 * @throws Exception
	 */
	public void testToLexStringList003() throws Exception {
		File xmlFile = new File("src/test/resources/nlp4j.util/keyword-with-dependency-003.xml");
		List<KeywordWithDependency> kwds = KeywordUtil.fromXml(xmlFile);
		KeywordWithDependency kw = kwds.get(0);
		List<List<Keyword>> listList = KeywordUtil.toListList(kw);
		for (List<Keyword> list : listList) {
			System.err.println(KeywordUtil.toLexString(list));
		}
	}

	/**
	 * creatd_at: 2022-09-14
	 * 
	 * @throws Exception
	 */
	public void testToLexStringList004() throws Exception {
		File xmlFile = new File("src/test/resources/nlp4j.util/keyword-with-dependency-004.xml");
		List<KeywordWithDependency> kwds = KeywordUtil.fromXml(xmlFile);
		KeywordWithDependency kw = kwds.get(0);
		List<List<Keyword>> listList = KeywordUtil.toListList(kw);
		for (List<Keyword> list : listList) {
			System.err.println(KeywordUtil.toLexString(list));
		}
	}

}
