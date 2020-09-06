package nlp4j;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import nlp4j.impl.DefaultDocument;
import nlp4j.impl.DefaultKeyword;

public class DefaultDocumentTestCase extends TestCase {

	Class target = DefaultDocument.class;

	public void testAddKeyword() {
		fail("Not yet implemented");
	}

	public void testAddKeywords() {
		fail("Not yet implemented");
	}

	public void testGetAttribute() {
		fail("Not yet implemented");
	}

	public void testGetAttribute001() {
		String key = "item";
		String value = "value";
		DefaultDocument doc = new DefaultDocument();
		doc.putAttribute(key, value);

		assertEquals(value, doc.getAttribute(key));
	}

	public void testGetAttributeAsDate() {
		fail("Not yet implemented");
	}

	public void testGetAttributeAsNumber() {
		fail("Not yet implemented");
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
		fail("Not yet implemented");
	}

	public void testGetId() {
		fail("Not yet implemented");
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

	public void testGetKeywordsString() {
		fail("Not yet implemented");
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

	public void testPutAttributeStringDate() {
		fail("Not yet implemented");
	}

	public void testPutAttributeStringNumber() {
		fail("Not yet implemented");
	}

	public void testPutAttributeStringObject() {
		fail("Not yet implemented");
	}

	public void testPutAttributeStringString() {
		fail("Not yet implemented");
	}

	public void testRemove() {
		fail("Not yet implemented");
	}

	public void testRemoveFlaggedKeyword() {
		fail("Not yet implemented");
	}

	public void testRemoveKeyword() {
		fail("Not yet implemented");
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
