package nlp4j1crawler;

import java.util.List;

import nlp4j.Document;
import nlp4j.crawler.JsonLineSeparatedCrawler;
import nlp4j.util.DocumentUtil;

@SuppressWarnings("javadoc")
public class JsonFileCrawlerExample {

	public static void main(String[] args) throws Exception {
		JsonLineSeparatedCrawler crawler = new JsonLineSeparatedCrawler();
		crawler.setProperty("file", "src/main/resources/nlp4j1crawler/example_json.txt");
		List<Document> docs = crawler.crawlDocuments();
		for (Document doc : docs) {
			System.err.println(doc.getAttribute("text"));
		}
		System.err.println(DocumentUtil.toJsonPrettyString(docs));
	}

}
