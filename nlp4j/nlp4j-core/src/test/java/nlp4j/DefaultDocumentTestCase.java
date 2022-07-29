package nlp4j;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;
import nlp4j.impl.DefaultDocument;
import nlp4j.impl.DefaultKeyword;
import nlp4j.util.DocumentUtil;

/**
 * target:nlp4j.impl.DefaultDocument
 * 
 * @author Hiroki Oya
 *
 */
public class DefaultDocumentTestCase extends TestCase {

	Class target = DefaultDocument.class;

	/**
	 * test:add:keywords
	 */
	public void testAddKeyword() {
		DefaultDocument doc = new DefaultDocument();

		// 今日 [facet=名詞, str=今日]
		// は [facet=助詞, str=は]
		// いい [facet=形容詞, str=いい]
		// 天気 [facet=名詞, str=天気]
		// です [facet=助動詞, str=です]
		// 。 [facet=特殊, str=。]
		{
			DefaultKeyword kw = new DefaultKeyword();
			kw.setLex("今日");
			kw.setStr("今日");
			kw.setFacet("名詞");
			kw.setBegin(0);
			kw.setEnd(2);
			doc.addKeyword(kw);
		}
		{
			DefaultKeyword kw = new DefaultKeyword();
			kw.setLex("は");
			kw.setStr("は");
			kw.setFacet("助詞");
			kw.setBegin(2);
			kw.setEnd(3);
			doc.addKeyword(kw);
		}
		{
			DefaultKeyword kw = new DefaultKeyword();
			kw.setLex("いい");
			kw.setStr("いい");
			kw.setFacet("形容詞");
			kw.setBegin(3);
			kw.setEnd(5);
			doc.addKeyword(kw);
		}
		{
			DefaultKeyword kw = new DefaultKeyword();
			kw.setLex("天気");
			kw.setStr("天気");
			kw.setFacet("名詞");
			kw.setBegin(5);
			kw.setEnd(7);
			doc.addKeyword(kw);
		}
		{
			DefaultKeyword kw = new DefaultKeyword();
			kw.setLex("です");
			kw.setStr("です");
			kw.setFacet("助動詞");
			kw.setBegin(7);
			kw.setEnd(9);
			doc.addKeyword(kw);
		}
		{
			DefaultKeyword kw = new DefaultKeyword();
			kw.setLex("。");
			kw.setStr("。");
			kw.setFacet("特殊");
			kw.setBegin(9);
			kw.setEnd(10);
			doc.addKeyword(kw);
		}

		System.err.println(doc);
		int expected = 6;
		assertEquals(expected, doc.getKeywords().size());
	}

	/**
	 * test:add:keywords
	 */
	public void testAddKeywords() {
		DefaultDocument doc = new DefaultDocument();

		ArrayList<Keyword> kwds = new ArrayList<Keyword>();

		// 今日 [facet=名詞, str=今日]
		// は [facet=助詞, str=は]
		// いい [facet=形容詞, str=いい]
		// 天気 [facet=名詞, str=天気]
		// です [facet=助動詞, str=です]
		// 。 [facet=特殊, str=。]
		{
			DefaultKeyword kw = new DefaultKeyword();
			kw.setLex("今日");
			kw.setStr("今日");
			kw.setFacet("名詞");
			kw.setBegin(0);
			kw.setEnd(2);
			kwds.add(kw);
		}
		{
			DefaultKeyword kw = new DefaultKeyword();
			kw.setLex("は");
			kw.setStr("は");
			kw.setFacet("助詞");
			kw.setBegin(2);
			kw.setEnd(3);
			kwds.add(kw);
		}
		{
			DefaultKeyword kw = new DefaultKeyword();
			kw.setLex("いい");
			kw.setStr("いい");
			kw.setFacet("形容詞");
			kw.setBegin(3);
			kw.setEnd(5);
			kwds.add(kw);
		}
		{
			DefaultKeyword kw = new DefaultKeyword();
			kw.setLex("天気");
			kw.setStr("天気");
			kw.setFacet("名詞");
			kw.setBegin(5);
			kw.setEnd(7);
			kwds.add(kw);
		}
		{
			DefaultKeyword kw = new DefaultKeyword();
			kw.setLex("です");
			kw.setStr("です");
			kw.setFacet("助動詞");
			kw.setBegin(7);
			kw.setEnd(9);
			kwds.add(kw);
		}
		{
			DefaultKeyword kw = new DefaultKeyword();
			kw.setLex("。");
			kw.setStr("。");
			kw.setFacet("特殊");
			kw.setBegin(9);
			kw.setEnd(10);
			kwds.add(kw);
		}

		doc.addKeywords(kwds);

		System.err.println(doc);

		int expected = 6;
		assertEquals(expected, doc.getKeywords().size());
	}

	/**
	 * test:get:attribute:String
	 */
	public void testGetAttribute001String() {
		DefaultDocument doc = new DefaultDocument();
		doc.putAttribute("key", "value");
		assertEquals("value", doc.getAttribute("key"));
	}

	/**
	 * test:get:attribute:Date
	 * 
	 * @throws Exception ParseException
	 */
	public void testGetAttribute002Date() throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		Date date = sdf.parse("2020/08/01");
		DefaultDocument doc = new DefaultDocument();
		doc.putAttribute("key", date);
		System.err.println(doc.getAttribute("key"));
		assertEquals("2020-08-01T00:00:00+09:00", doc.getAttribute("key").toString());
	}

	/**
	 * test:get:attribute:Number
	 */
	public void testGetAttribute003Number() {
		DefaultDocument doc = new DefaultDocument();
		doc.putAttribute("key", 1);
		System.err.println(doc.getAttribute("key"));
		assertEquals(1, doc.getAttribute("key"));
	}

	public void testGetAttribute001() {
		String key = "item";
		String value = "value";
		DefaultDocument doc = new DefaultDocument();
		doc.putAttribute(key, value);

		assertEquals(value, doc.getAttribute(key));
	}

	/**
	 * @throws Exception
	 */
	public void testGetAttributeAsDate() throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		Date date = sdf.parse("2020/08/01");
		Date date2 = sdf.parse("2020/08/01");
		DefaultDocument doc = new DefaultDocument();
		doc.putAttribute("key", date);
		System.err.println(doc.getAttribute("key"));
		assertEquals(date2, doc.getAttributeAsDate("key"));
	}

	public void testGetAttributeAsNumber() {
		DefaultDocument doc = new DefaultDocument();
		doc.putAttribute("key", "1");
		System.err.println(doc.getAttribute("key"));
		assertEquals(1, doc.getAttributeAsNumber("key"));
	}

	public void testGetAttributeAsNumber001() {
		DefaultDocument doc = new DefaultDocument();
		doc.putAttribute("field1", 100);

		System.err.println(doc.getAttribute("field1").getClass().getName());

		assertEquals(100, doc.getAttributeAsNumber("field1"));
	}

	public void testGetAttributeAsNumber501() {
		DefaultDocument doc = new DefaultDocument();
		doc.putAttribute("field1", "xxx");

		try {
			doc.getAttributeAsNumber("field1");
			fail();
		} catch (ClassCastException e) {
			System.err.println("OK. ClassCast Exception should be thrown.");
		}
	}

	public void testGetAttributeKeys() {
		DefaultDocument doc = new DefaultDocument();
		doc.putAttribute("key", "value");
		List<String> keys = doc.getAttributeKeys();

		assertEquals(1, keys.size());
		assertEquals("key", keys.get(0));

	}

	public void testGetId() {
		DefaultDocument doc = new DefaultDocument();
		String id = doc.getId();
		assertNotNull(id);
	}

	public void testGetId001() {
		DefaultDocument doc = new DefaultDocument();
		String id = doc.getId();
		assertNotNull(id);
	}

	public void testGetId002() {
		DefaultDocument doc = new DefaultDocument();
		doc.setId("DOC001");
		String id = doc.getId();
		assertNotNull(id);
		assertEquals("DOC001", doc.getId());
	}

	public void testGetKeywords() {
		DefaultDocument doc = new DefaultDocument();
		Keyword kwd = new DefaultKeyword();
		kwd.setLex("test");
		kwd.setFacet("word.noun");
		doc.addKeyword(kwd);

		List<Keyword> kwds = doc.getKeywords();

		assertEquals("test", kwds.get(0).getLex());
	}

	public void testGetKeywordsString() throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		Date date = sdf.parse("2020/08/01");
		Date date2 = sdf.parse("2020/08/01");
		DefaultDocument doc = new DefaultDocument();
		doc.putAttribute("key", date);
		System.err.println(doc.getAttribute("key"));
		assertEquals("2020-08-01T00:00:00+09:00", doc.getAttributeAsString("key"));
	}

	public void testGetText() {
		DefaultDocument doc = new DefaultDocument();
		doc.setText("text");
		assertEquals("text", doc.getText());
	}

	public void testPutAttribute() {
		DefaultDocument doc = new DefaultDocument();
		doc.putAttribute("key", "value");
		assertEquals("value", doc.getAttribute("key"));
	}

	public void testPutAttributeStringDate() throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		Date date = sdf.parse("2020/08/01");
		DefaultDocument doc = new DefaultDocument();
		doc.putAttribute("key", date);
		System.err.println(doc.getAttribute("key"));
		assertEquals("2020-08-01T00:00:00+09:00", doc.getAttribute("key").toString());
	}

	public void testPutAttributeStringNumber() {
		DefaultDocument doc = new DefaultDocument();
		doc.putAttribute("key", 1);
		System.err.println(doc.getAttribute("key"));
		assertEquals(1, doc.getAttribute("key"));
	}

	public void testPutAttributeStringObject() {
		Object o = new Object();
		DefaultDocument doc = new DefaultDocument();
		doc.putAttribute("key", o);
		assertEquals(o, doc.getAttribute("key"));
	}

	public void testPutAttributeStringObjectList001() {
		List<String> o = new ArrayList<>();
		{
			o.add("a");
			o.add("b");
			o.add("c");
		}
		DefaultDocument doc = new DefaultDocument();
		doc.putAttribute("key", o);
		assertEquals(o, doc.getAttribute("key"));

		System.err.println(DocumentUtil.toPrettyJsonString(doc));
	}

	public void testPutAttributeStringObjectList002() {
		List<Integer> o = new ArrayList<>();
		{
			o.add(1);
			o.add(2);
			o.add(3);
		}
		DefaultDocument doc = new DefaultDocument();
		doc.putAttribute("key", o);
		assertEquals(o, doc.getAttribute("key"));

		System.err.println(DocumentUtil.toPrettyJsonString(doc));
	}

	public void testPutAttributeStringString() {
		DefaultDocument doc = new DefaultDocument();
		doc.putAttribute("key", "value");
		assertEquals("value", doc.getAttribute("key"));
	}

	public void testRemove() {
		DefaultDocument doc = new DefaultDocument();
		doc.putAttribute("key", "value");
		assertEquals("value", doc.getAttribute("key"));
		doc.remove("key");
		assertEquals(0, doc.getAttributeKeys().size());
	}

	public void testRemoveFlaggedKeyword() {
		DefaultDocument doc = new DefaultDocument();

		// 今日 [facet=名詞, str=今日]
		// は [facet=助詞, str=は]
		// いい [facet=形容詞, str=いい]
		// 天気 [facet=名詞, str=天気]
		// です [facet=助動詞, str=です]
		// 。 [facet=特殊, str=。]
		{
			DefaultKeyword kw = new DefaultKeyword();
			kw.setLex("今日");
			kw.setStr("今日");
			kw.setFacet("名詞");
			kw.setBegin(0);
			kw.setEnd(2);
			doc.addKeyword(kw);
		}
		{
			DefaultKeyword kw = new DefaultKeyword();
			kw.setLex("は");
			kw.setStr("は");
			kw.setFacet("助詞");
			kw.setBegin(2);
			kw.setEnd(3);
			kw.setFlag(true);
			doc.addKeyword(kw);
		}
		{
			DefaultKeyword kw = new DefaultKeyword();
			kw.setLex("いい");
			kw.setStr("いい");
			kw.setFacet("形容詞");
			kw.setBegin(3);
			kw.setEnd(5);
			kw.setFlag(true);
			doc.addKeyword(kw);
		}
		{
			DefaultKeyword kw = new DefaultKeyword();
			kw.setLex("天気");
			kw.setStr("天気");
			kw.setFacet("名詞");
			kw.setBegin(5);
			kw.setEnd(7);
			doc.addKeyword(kw);
		}
		{
			DefaultKeyword kw = new DefaultKeyword();
			kw.setLex("です");
			kw.setStr("です");
			kw.setFacet("助動詞");
			kw.setBegin(7);
			kw.setEnd(9);
			kw.setFlag(true);
			doc.addKeyword(kw);
		}
		{
			DefaultKeyword kw = new DefaultKeyword();
			kw.setLex("。");
			kw.setStr("。");
			kw.setFacet("特殊");
			kw.setBegin(9);
			kw.setEnd(10);
			kw.setFlag(true);
			doc.addKeyword(kw);
		}

		System.err.println(doc);
		{
			int expected = 6;
			assertEquals(expected, doc.getKeywords().size());
		}

		doc.removeFlaggedKeyword();
		{
			int expected = 2;
			assertEquals(expected, doc.getKeywords().size());
		}
	}

	public void testRemoveKeyword() {
		DefaultDocument doc = new DefaultDocument();

		// 今日 [facet=名詞, str=今日]
		// は [facet=助詞, str=は]
		// いい [facet=形容詞, str=いい]
		// 天気 [facet=名詞, str=天気]
		// です [facet=助動詞, str=です]
		// 。 [facet=特殊, str=。]
		{
			DefaultKeyword kw = new DefaultKeyword();
			kw.setLex("今日");
			kw.setStr("今日");
			kw.setFacet("名詞");
			kw.setBegin(0);
			kw.setEnd(2);
			doc.addKeyword(kw);
		}
		{
			DefaultKeyword kw = new DefaultKeyword();
			kw.setLex("は");
			kw.setStr("は");
			kw.setFacet("助詞");
			kw.setBegin(2);
			kw.setEnd(3);
			doc.addKeyword(kw);
		}
		{
			DefaultKeyword kw = new DefaultKeyword();
			kw.setLex("いい");
			kw.setStr("いい");
			kw.setFacet("形容詞");
			kw.setBegin(3);
			kw.setEnd(5);
			doc.addKeyword(kw);
		}
		{
			DefaultKeyword kw = new DefaultKeyword();
			kw.setLex("天気");
			kw.setStr("天気");
			kw.setFacet("名詞");
			kw.setBegin(5);
			kw.setEnd(7);
			doc.addKeyword(kw);
		}
		{
			DefaultKeyword kw = new DefaultKeyword();
			kw.setLex("です");
			kw.setStr("です");
			kw.setFacet("助動詞");
			kw.setBegin(7);
			kw.setEnd(9);
			doc.addKeyword(kw);
		}
		{
			DefaultKeyword kw = new DefaultKeyword();
			kw.setLex("。");
			kw.setStr("。");
			kw.setFacet("特殊");
			kw.setBegin(9);
			kw.setEnd(10);
			doc.addKeyword(kw);
		}

		{
			System.err.println(doc);
			int expected = 6;
			assertEquals(expected, doc.getKeywords().size());
		}

		{
			DefaultKeyword kwRemoved = new DefaultKeyword();
			kwRemoved.setLex("いい");
			kwRemoved.setStr("いい");
			kwRemoved.setFacet("形容詞");
			kwRemoved.setBegin(3);
			kwRemoved.setEnd(5);
			doc.removeKeyword(kwRemoved);
		}
		{
			System.err.println(doc);
			int expected = 5;
			assertEquals(expected, doc.getKeywords().size());
		}

	}

	public void testSetId() {
		DefaultDocument doc = new DefaultDocument();
		doc.setId("id");
		assertEquals("id", doc.getId());
	}

	public void testSetKeywords() {
		DefaultDocument doc = new DefaultDocument();
		Keyword kwd = new DefaultKeyword();
		kwd.setLex("lex");
		List<Keyword> kwds = new ArrayList<Keyword>();
		kwds.add(kwd);
		doc.setKeywords(kwds);

	}

	public void testSetText() {
		DefaultDocument doc = new DefaultDocument();
		doc.setText("text");
		assertEquals("text", doc.getText());
	}

	public void testToString() {
		DefaultDocument doc = new DefaultDocument();
		String s = doc.toString();
		System.err.println(s);
	}

}
