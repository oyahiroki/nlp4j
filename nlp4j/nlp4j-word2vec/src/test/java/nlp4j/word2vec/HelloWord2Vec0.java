package nlp4j.word2vec;

import java.util.List;

import nlp4j.Document;
import nlp4j.crawler.Crawler;
import nlp4j.crawler.JsonLineSeparatedCrawler;
import nlp4j.util.DocumentUtil;

public class HelloWord2Vec0 {

	public static void main(String[] args) throws Exception {
//crawler[0].name=nlp4j.crawler.JsonLineSeparatedCrawler
//crawler[0].param[0].key=file
//crawler[0].param[0].value=/usr/local/nlp4j/collections/nhtsa/data/nhtsa2020_NISSAN_20200315.json

		Crawler crawler = new JsonLineSeparatedCrawler();
		String fileName = "/usr/local/nlp4j/collections/nhtsa/data/nhtsa2020_NISSAN_20200315.json";
		crawler.setProperty("file", fileName);

		List<Document> docs = crawler.crawlDocuments();

		System.err.println(DocumentUtil.toPrettyJsonString(docs.get(0)));

		System.err.println("docs: " + docs.size());

	}

}
