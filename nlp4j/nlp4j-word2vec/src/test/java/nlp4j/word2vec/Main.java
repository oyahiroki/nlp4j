package nlp4j.word2vec;

import java.util.List;

import nlp4j.Document;
import nlp4j.crawler.TextFileLineSeparatedCrawler;

public class Main {

	public static void main(String[] args) throws Exception {
		TextFileLineSeparatedCrawler crawler = new TextFileLineSeparatedCrawler();
		crawler.setProperty("file", "C:/usr/local/nlp4j/collections/mlit/models/data.txt");

		List<Document> docs = crawler.crawlDocuments();

		System.err.println(docs.size());

	}

}
