package nlp4j;

import java.util.List;

import junit.framework.TestCase;
import nlp4j.impl.DefaultDocument;
import nlp4j.impl.DefaultKeyword;

public class DefaultDocumentTestCase extends TestCase {

	public void testGetAttribute() {
		String key = "item";
		String value = "value";
		DefaultDocument doc = new DefaultDocument();
		doc.putAttribute(key, value);

		assertEquals(value, doc.getAttribute(key));
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

	public void testGetText() {
		fail("Not yet implemented");
	}

	public void testPutAttribute() {
		fail("Not yet implemented");
	}

	public void testSetId() {
		fail("Not yet implemented");
	}

	public void testSetKeywords() {
		fail("Not yet implemented");
	}

	public void testSetText() {
		fail("Not yet implemented");
	}

	public void testToString() {
		fail("Not yet implemented");
	}

}
