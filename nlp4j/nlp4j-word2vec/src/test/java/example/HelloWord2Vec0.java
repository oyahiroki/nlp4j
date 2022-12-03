package example;

import java.util.List;

import nlp4j.Document;
import nlp4j.crawler.Crawler;
import nlp4j.crawler.JsonLineSeparatedCrawler;
import nlp4j.util.DocumentUtil;

@SuppressWarnings("javadoc")
public class HelloWord2Vec0 {

	/**
	 * <pre>
	 * 改行区切りJSONを読み込む
	 * プリント
	 * </pre>
	 */
	public static void main(String[] args) throws Exception {
		// Crawler for Line Separated JSON
		Crawler crawler = new JsonLineSeparatedCrawler();
		{ // NHTSA Nissan
			crawler.setProperty("file", "/usr/local/nlp4j/collections/nhtsa/data/nhtsa2020_NISSAN_20200315.json");
		}

		List<Document> docs = crawler.crawlDocuments();
		{ // PRINT ONLY
			System.err.println(DocumentUtil.toPrettyJsonString(docs.get(0)));
			System.err.println("docs: " + docs.size());
		}

	}

}
