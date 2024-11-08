package nlp4j.crawler;

import java.io.File;

import junit.framework.TestCase;

public class CsvFileStreamCrawlerTestCase extends TestCase {

	public void testCrawlDocuments() {
	}

	public void testStreamDocuments() {
	}

	/**
	 * @throws Exception
	 * @since 1.3.7.15
	 */
	public void testStreamDocumentsFile001() throws Exception {
		File csv = new File("src/test/resources/nlp4j.crawler/CsvFileStreamCrawlerTestCase.csv");
		CsvFileStreamCrawler crl = new CsvFileStreamCrawler();
		crl.streamDocuments(csv).forEach(d -> {
			System.err.println(d);
		});
	}

	public void testStreamDocumentsURL() {
	}

}
