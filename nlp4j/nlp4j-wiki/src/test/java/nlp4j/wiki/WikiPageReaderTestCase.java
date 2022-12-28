package nlp4j.wiki;

import java.io.File;

import junit.framework.TestCase;

public class WikiPageReaderTestCase extends TestCase {

	public void test101() throws Exception {

		File xmlFile = new File("src/test/resources/nlp4j.wiki/jawiki-20220501-pages-articles-multistream-255425.xml");

		WikiPageReader wpr = new WikiPageReader();
		WikiPage data = wpr.readWikiPageXmlFile(xmlFile);

		// IN WIKI FORMAT
		System.err.println(data.getText());
	}

}
