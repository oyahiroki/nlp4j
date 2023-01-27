package nlp4j.wiki;

import junit.framework.TestCase;

public class WikiPageTestCase extends TestCase {

	public void testWikiPageStringStringString001() {
		String title = "大学";
		String id = "30345";
		String format = "text/x-wiki";

		WikiPage page = new WikiPage(title, id, format);
		System.err.println(page);
		assertNotNull(page);
	}

	public void testWikiPageStringStringStringString001() {
		// TODO TEST
	}

	public void testGetCategoryTags() {
		// TODO TEST
	}

	public void testGetFormat() {
		// TODO TEST
	}

	public void testGetHtml() {
		// TODO TEST
	}

	public void testGetId() {
		// TODO TEST
	}

	public void testGetPlainText() {
		// TODO TEST
	}

	public void testGetRediect_title() {
		// TODO TEST
	}

	public void testGetText() {
		// TODO TEST
	}

	public void testGetTimestamp() {
		// TODO TEST
	}

	public void testGetTitle() {
		// TODO TEST
	}

	public void testGetXml() {
		// TODO TEST
	}

	public void testIsRediect() {
		// TODO TEST
	}

	public void testSetCategoryTags() {
		// TODO TEST
	}

	public void testSetFormat() {
		// TODO TEST
	}

	public void testSetId() {
		// TODO TEST
	}

	public void testSetRedirectTitle() {
		// TODO TEST
	}

	public void testSetText() {
		// TODO TEST
	}

	public void testSetTimestamp() {
		// TODO TEST
	}

	public void testSetTitle() {
		// TODO TEST
	}

	public void testSetXml() {
		// TODO TEST
	}

	public void testToString() {
		// TODO TEST
	}

}
