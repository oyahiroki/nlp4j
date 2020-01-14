package nlp4j.crawler;

import java.util.List;

import junit.framework.TestCase;
import nlp4j.Document;

public class TextFileCrawlerTestCase extends TestCase {

	public void testCrawlDocuments() {
		TextFileCrawler crawler = new TextFileCrawler();
		crawler.setProperty("file", "src/test/resources/nlp4j.crawler/neko_short_utf8.txt");
		crawler.setProperty("encoding", "UTF-8");

		List<Document> docs = crawler.crawlDocuments();

		for (Document doc : docs) {
			System.err.println(doc);
		}

	}

}
