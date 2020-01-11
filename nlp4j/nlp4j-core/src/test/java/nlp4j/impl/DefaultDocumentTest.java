package nlp4j.impl;

import junit.framework.TestCase;

public class DefaultDocumentTest extends TestCase {

	public void testGetAttributeKeys() {
	}

	public void testAddKeyword() {
	}

	public void testAddKeywords() {
	}

	public void testGetAttribute() {
	}

	public void testGetId() {
	}

	public void testGetKeywords() {
	}

	public void testGetText() {
	}

	public void testPutAttribute() {
	}

	public void testSetId() {
	}

	public void testSetKeywords() {
	}

	public void testSetText() {
	}

	public void testToString() {
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

}
