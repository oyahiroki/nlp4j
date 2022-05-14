package nlp4j.crawler;

import java.util.List;

import nlp4j.Document;
import nlp4j.util.DocumentUtil;

public class TextFileLineSeparatedCrawlerExample {

	public static void main(String[] args) throws Exception {

		List<Document> docs = null;
		{
			String fileName = "src/main/resources/nlp4j1crawler/example_ja.txt";
			TextFileLineSeparatedCrawler crawler = new TextFileLineSeparatedCrawler();
			crawler.setProperty("file", fileName);
			crawler.setProperty("skipemptyline", "true");
			docs = crawler.crawlDocuments();
		}

		// FOR EACH(DOCUMENT)
		for (Document doc : docs) {
			System.err.println(DocumentUtil.toPrettyJsonString(doc));
		} // END OF FOR

// Expected output		
//	{
//  	"text": "これはテストです．",
//  	"keywords": []
//	}
//	{
//	  "text": "今日はいい天気です．",
//	  "keywords": []
//	}
//	{
//	  "text": "私は歩いて学校に行きます．",
//	  "keywords": []
//	}

	}

}
