package nlp4j.word2vec;

import java.util.List;

import nlp4j.Document;
import nlp4j.crawler.Crawler;
import nlp4j.crawler.JsonLineSeparatedCrawler;
import nlp4j.util.DocumentUtil;

public class HelloWord2VecJa0b {

	public static void main(String[] args) throws Exception {
//crawler[0].name=nlp4j.crawler.JsonLineSeparatedCrawler
//crawler[0].param[0].key=file
//crawler[0].param[0].value=/usr/local/nlp4j/collections/nhtsa/data/nhtsa2020_NISSAN_20200315.json

		Crawler crawler = new JsonLineSeparatedCrawler();
		String fileName = "C:/usr/local/nlp4j/collections/mlit/data/json1/milt_carinfo-20190101-20201231_json.txt";
		crawler.setProperty("file", fileName);

		List<Document> docs = crawler.crawlDocuments();

		System.err.println(DocumentUtil.toPrettyJsonString(docs.get(0)));

		System.err.println("docs: " + docs.size());

		System.err.println(docs.get(0).getAttribute("申告内容の要約"));

	}

}
