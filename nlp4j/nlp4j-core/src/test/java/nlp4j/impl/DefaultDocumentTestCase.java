package nlp4j.impl;

import java.util.List;

import junit.framework.TestCase;
import nlp4j.Keyword;
import nlp4j.util.DocumentUtil;

public class DefaultDocumentTestCase extends TestCase {

	/**
	 * created on: 2024-11-17
	 * 
	 * @throws Exception
	 * @since 1.3.7.15
	 */
	public void testChangeAttribute001() throws Exception {
		DefaultDocument doc = new DefaultDocument();
		{
			doc.putAttribute("aaa", "xxx");
		}

		assertTrue(doc.getAttribute("aaa") != null);
		assertTrue(doc.getAttribute("bbb") == null);

		doc.changeAttributeKey("aaa", "bbb");

		assertTrue(doc.getAttribute("aaa") == null);
		assertTrue(doc.getAttribute("bbb") != null);

	}

	public void test001() throws Exception {
		DefaultDocument doc = new DefaultDocument();
		{
			doc.putAttribute("text", "私は学校に行きました。");
		}
		{
			doc.addKeyword(new DefaultKeyword(0, 1, "名詞", "私", "私"));
			doc.addKeyword(new DefaultKeyword(1, 2, "助詞", "は", "は"));
			doc.addKeyword(new DefaultKeyword(2, 4, "名詞", "学校", "学校"));
			doc.addKeyword(new DefaultKeyword(5, 6, "助詞", "に", "に"));
			doc.addKeyword(new DefaultKeyword(6, 8, "動詞", "行く", "行き"));
			doc.addKeyword(new DefaultKeyword(8, 10, "助動詞", "ます", "まし"));
			doc.addKeyword(new DefaultKeyword(10, 11, "助動詞", "た", "た"));
			doc.addKeyword(new DefaultKeyword(11, 12, "記号", "。", "。"));
		}
		{
			for (Keyword kwd : doc.getKeywords()) {
				System.err.println(kwd);
			}
		}
	}

	public void testAddKeyword() {
	}

	public void testAddKeywords() {
	}

	public void testGetAttribute001() {
		DefaultDocument doc = new DefaultDocument();
		{
			doc.putAttribute("aaa", "111");
			doc.putAttribute("bbb", "222");
			doc.putAttribute("ccc", "333");
		}

		System.err.println(doc.getAttribute("aaa"));
		assertEquals("111", doc.getAttribute("aaa"));
	}

	public void testGetAttributeAsString001() {
		DefaultDocument doc = new DefaultDocument();
		{
			doc.putAttribute("aaa", "111");
			doc.putAttribute("bbb", "222");
			doc.putAttribute("ccc", "333");
		}
		System.err.println(doc.getAttributeAsString("aaa"));
		assertEquals("111", doc.getAttributeAsString("aaa"));
	}

	public void testGetAttributeAsString002() {
		DefaultDocument doc = new DefaultDocument();
		{
			doc.putAttribute("aaa", "111");
			doc.putAttribute("bbb", "222");
			doc.putAttribute("ccc", "333");
			doc.putAttribute("都道府県", "北海道");
		}
		System.err.println(doc.getAttributeAsString("都道府県"));
		assertEquals("北海道", doc.getAttributeAsString("都道府県"));
	}

	public void testGetAttributeKeys() {
		DefaultDocument doc = new DefaultDocument();
		{
			doc.putAttribute("aaa", "111");
			doc.putAttribute("bbb", "222");
			doc.putAttribute("ccc", "333");
		}

		List<String> keys = doc.getAttributeKeys();

		for (String key : keys) {
			System.err.println(key);
		}

		assertEquals("aaa", keys.get(0));
		assertEquals("bbb", keys.get(1));
		assertEquals("ccc", keys.get(2));

	}

	public void testGetId() {
	}

	public void testGetKeywords() {
	}

	public void testGetKeywordsString() {
		DefaultDocument doc = new DefaultDocument();
		{
			DefaultKeyword kwd = new DefaultKeyword();
			kwd.setFacet("word.NN");
			kwd.setStr("TEST");
			kwd.setLex("test");
			doc.addKeyword(kwd);
		}
		{
			doc.putAttribute("field1", "TEST");
			doc.setText("This is test.");
		}
		System.err.println(doc.toString());
	}

	public void testGetText() {
	}

	public void testPutAttribute001() {
		DefaultDocument doc = new DefaultDocument();
		doc.putAttribute("field1", "TEST");
		System.err.println(DocumentUtil.toJsonPrettyString(doc));

	}

	/**
	 * @throws Exception
	 * @since 1.3.7.15
	 */
	public void testRemoveKeywords001() throws Exception {
		DefaultDocument doc = new DefaultDocument();
		{
			doc.putAttribute("text", "私は学校に行きました。");
		}
		{
			doc.addKeyword(new DefaultKeyword(0, 1, "名詞", "私", "私"));
			doc.addKeyword(new DefaultKeyword(1, 2, "助詞", "は", "は"));
			doc.addKeyword(new DefaultKeyword(2, 4, "名詞", "学校", "学校"));
			doc.addKeyword(new DefaultKeyword(5, 6, "助詞", "に", "に"));
			doc.addKeyword(new DefaultKeyword(6, 8, "動詞", "行く", "行き"));
			doc.addKeyword(new DefaultKeyword(8, 10, "助動詞", "ます", "まし"));
			doc.addKeyword(new DefaultKeyword(10, 11, "助動詞", "た", "た"));
			doc.addKeyword(new DefaultKeyword(11, 12, "記号", "。", "。"));
		}
		{
			assertTrue(doc.getKeywords() != null);
			assertTrue(doc.getKeywords().size() > 0);
		}

		doc.removeKeywords();

		{
			assertTrue(doc.getKeywords() != null);
			assertTrue(doc.getKeywords().size() == 0);
		}

	}

	public void testSetId() {
	}

	public void testSetKeywords() {
	}

	public void testSetText() {
	}

	public void testToString() {
	}

}
