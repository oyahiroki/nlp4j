package nlp4j.wiki.util;

import java.io.File;
import java.util.List;

import junit.framework.TestCase;
import nlp4j.wiki.WikiPage;

public class MediaWikiPageUtilsTestCase extends TestCase {

	public void testParsePagesFromPagesXml001() throws Exception {
		File xmlFile = new File(
				"src/test/resources/nlp4j.wiki/jawiki-20221101-pages-articles-multistream-225-うる星やつら.xml");

		List<WikiPage> pages = MediaWikiPageUtils.parsePagesFromPagesXml(xmlFile);
		for (WikiPage page : pages) {
			System.err.println(page.getTitle());
			System.err.println(page.getText());
		}

	}

	public void testParsePagesFromPagesXml002() throws Exception {
		File xmlFile = new File(
				"src/test/resources/nlp4j.wiki/jawiki-20221101-pages-articles-multistream-1555-DRAGON_BALL.xml");

		List<WikiPage> pages = MediaWikiPageUtils.parsePagesFromPagesXml(xmlFile);
		for (WikiPage page : pages) {
			System.err.println(page.getTitle());
			System.err.println(page.getText());
		}

	}

}
