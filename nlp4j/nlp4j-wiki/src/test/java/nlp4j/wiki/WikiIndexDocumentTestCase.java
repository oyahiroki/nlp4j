package nlp4j.wiki;

import junit.framework.TestCase;

public class WikiIndexDocumentTestCase extends TestCase {

	public void testGetAttributeAsString() {
		WikiIndexDocument wikiIndexDocument = new WikiIndexDocument();
	}

	public void testPutAttributeStringObject() {
		WikiIndexDocument wikiIndexDocument = new WikiIndexDocument();
		{
			wikiIndexDocument.setId("ID");
			wikiIndexDocument.setText("TEXT");
		}
		System.err.println(wikiIndexDocument);
		Object value = null;
		{
			wikiIndexDocument.putAttribute(null, value);
		}
		System.err.println(wikiIndexDocument);
	}

	public void testPutAttributeStringString() {
		WikiIndexDocument wikiIndexDocument = new WikiIndexDocument();
	}

	public void testGetDumpReader() {
		WikiIndexDocument wikiIndexDocument = new WikiIndexDocument();
	}

	public void testSetDumpReader() {
		WikiIndexDocument wikiIndexDocument = new WikiIndexDocument();
	}

}
