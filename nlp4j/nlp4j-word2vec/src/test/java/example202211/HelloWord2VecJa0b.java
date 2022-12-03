package example202211;

import java.util.List;

import nlp4j.Document;
import nlp4j.crawler.Crawler;
import nlp4j.crawler.JsonLineSeparatedCrawler;
import nlp4j.util.DocumentUtil;

public class HelloWord2VecJa0b {

	public static void main(String[] args) throws Exception {
		// ファイルの読み込み確認
		Crawler crawler = new JsonLineSeparatedCrawler();
		String fileName = "files/example_ja/mlit_carinfo-202112_json.txt";
		crawler.setProperty("file", fileName);

		List<Document> docs = crawler.crawlDocuments();
		System.err.println(DocumentUtil.toPrettyJsonString(docs.get(0)));
		System.err.println("docs: " + docs.size());
		System.err.println(docs.get(0).getAttribute("申告内容の要約"));
	}

}
