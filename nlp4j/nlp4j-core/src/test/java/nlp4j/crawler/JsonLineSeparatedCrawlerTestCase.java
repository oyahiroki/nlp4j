package nlp4j.crawler;

import java.util.List;

import junit.framework.TestCase;
import nlp4j.Document;
import nlp4j.util.DocumentUtil;

/**
 * @author Hiroki Oya
 * @created_at 2021-07-31
 */
public class JsonLineSeparatedCrawlerTestCase extends TestCase {

	@SuppressWarnings("javadoc")
	public void testCrawlDocuments001() throws Exception {
		JsonLineSeparatedCrawler crawler = new JsonLineSeparatedCrawler();
		crawler.setProperty("file", "src/test/resources/nlp4j.crawler/documents001_json.txt");
		List<Document> docs = crawler.crawlDocuments();
		for (Document doc : docs) {
			System.err.println(DocumentUtil.toPrettyJsonString(doc));
		}
	}

	public void testCrawlDocuments101() throws Exception {
		String[] args1 = { "src/test/resources/nlp4j.crawler/col_test_csvfilecrawlertest.properties" };

		nlp4j.DocumentProcessor.main(args1);

	}

}
