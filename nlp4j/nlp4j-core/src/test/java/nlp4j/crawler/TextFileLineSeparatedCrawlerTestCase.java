package nlp4j.crawler;

import java.util.List;

import junit.framework.TestCase;
import nlp4j.Document;
import nlp4j.util.DocumentUtil;

public class TextFileLineSeparatedCrawlerTestCase extends TestCase {

	public void testCrawlDocuments001() {

		List<Document> docs = null;
		{
			String fileName = "src/test/resources/nlp4j.crawler/example_ja.txt";
			TextFileLineSeparatedCrawler crawler = new TextFileLineSeparatedCrawler();
			crawler.setProperty("file", fileName);
			crawler.setProperty("skipemptyline", "true");
			docs = crawler.crawlDocuments();
		}

		// FOR EACH(DOCUMENT)
		for (Document doc : docs) {
			System.err.println(DocumentUtil.toPrettyJsonString(doc));
		} // END OF FOR

		assertEquals(3, docs.size());

	}

}
