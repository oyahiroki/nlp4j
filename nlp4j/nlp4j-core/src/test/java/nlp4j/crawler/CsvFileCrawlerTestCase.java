package nlp4j.crawler;

import java.util.List;

import junit.framework.TestCase;
import nlp4j.Document;
import nlp4j.util.DocumentUtil;

/**
 * @author Hiroki Oya
 * @since 1.3.1.0
 */
public class CsvFileCrawlerTestCase extends TestCase {

	public void testCrawlDocuments001() {

		CsvFileCrawler crawler = new CsvFileCrawler();

		crawler.setProperty("file", "src/test/resources/nlp4j.crawler/CsvFileCrawlerTestCase_utf8.csv");

		List<Document> docs = crawler.crawlDocuments();

		System.err.println(DocumentUtil.toJsonPrettyString(docs));

		assertTrue(docs.size() == 3);

	}

}
