package nlp4j1crawler;

import java.util.List;

import nlp4j.Document;
import nlp4j.crawler.CsvFileCrawler;
import nlp4j.util.DocumentUtil;

@SuppressWarnings("javadoc")
public class CsvFileCrawlerExample {

	public static void main(String[] args) throws Exception {

		CsvFileCrawler crawler = new CsvFileCrawler();

		crawler.setProperty("file", "src/main/resources/nlp4j1crawler/example_utf8.csv");

		List<Document> docs = crawler.crawlDocuments();

		System.err.println(DocumentUtil.toJsonPrettyString(docs));

	}

}
