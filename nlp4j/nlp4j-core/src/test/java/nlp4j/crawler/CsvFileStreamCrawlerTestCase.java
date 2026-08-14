package nlp4j.crawler;

import java.io.File;

import junit.framework.TestCase;

public class CsvFileStreamCrawlerTestCase extends TestCase {

	public void testCrawlDocuments() {
	}

	public void testStreamDocumentsResource_001() throws Exception {
		CsvFileStreamCrawler crl = new CsvFileStreamCrawler();
		crl.streamDocumentsResource("nlp4j.crawler/CsvFileStreamCrawlerTestCase.csv") //
				.forEach(d -> {
					System.err.println(d);
				});
	}

	public void testStreamDocumentsResource_002() throws Exception {
		CsvFileStreamCrawler crl = new CsvFileStreamCrawler();
		crl.streamDocumentsResource("nlp4j.crawler/CsvFileStreamCrawlerTestCase_mlit.csv") //
				.forEach(d -> {
					System.err.println(d.getAttribute("番号") + "," + d.getAttribute("申告内容"));
				});
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
