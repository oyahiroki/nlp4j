package nlp4j.crawler;

import java.util.List;

import nlp4j.Document;
import nlp4j.util.DocumentUtil;

public class CsvFileCrawlerTestMain2 {

	public static void main(String[] args) throws Exception {

		CsvFileCrawler crawler = new CsvFileCrawler();
		{
			crawler.setProperty("add_header", "true");
			crawler.setProperty("add_data", "true");
		}

		crawler.setProperty("file", "src/test/resources/nlp4j.crawler/CsvFileCrawlerTestCase_utf8.csv");

		List<Document> docs = crawler.crawlDocuments();

		System.err.println(DocumentUtil.toJsonPrettyString(docs));

		// Expected Results ...

//		[
//		  {
//		    "data": [
//		      "001",
//		      "2020-01-01",
//		      "AAA",
//		      "THIS IS TEST1."
//		    ],
//		    "header": [
//		      "DOCUMENTID",
//		      "DATE",
//		      "ITEM1",
//		      "TEXT"
//		    ],
//		    "DOCUMENTID": "001",
//		    "DATE": "2020-01-01",
//		    "ITEM1": "AAA",
//		    "TEXT": "THIS IS TEST1.",
//		    "keywords": []
//		  },
//		  {
//		    "data": [
//		      "002",
//		      "2020-01-02",
//		      "BBB",
//		      "THIS IS TEST2."
//		    ],
//		    "header": [
//		      "DOCUMENTID",
//		      "DATE",
//		      "ITEM1",
//		      "TEXT"
//		    ],
//		    "DOCUMENTID": "002",
//		    "DATE": "2020-01-02",
//		    "ITEM1": "BBB",
//		    "TEXT": "THIS IS TEST2.",
//		    "keywords": []
//		  },
//		  {
//		    "data": [
//		      "003",
//		      "2020-01-01",
//		      "AAA",
//		      "THIS IS TEST3."
//		    ],
//		    "header": [
//		      "DOCUMENTID",
//		      "DATE",
//		      "ITEM1",
//		      "TEXT"
//		    ],
//		    "DOCUMENTID": "003",
//		    "DATE": "2020-01-01",
//		    "ITEM1": "AAA",
//		    "TEXT": "THIS IS TEST3.",
//		    "keywords": []
//		  }
//		]

	}

}
