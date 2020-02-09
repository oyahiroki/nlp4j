package nlp4j.webcrawler.mlit;

import java.io.File;
import java.util.List;

import nlp4j.Document;
import nlp4j.util.DocumentUtil;

public class MlitCarInfoDownloadMain4 {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		MlitCarInfoCrawler crawler = new MlitCarInfoCrawler();
		crawler.setProperty("from_date", "2017/01/01");
		crawler.setProperty("to_date", "2017/12/31");
		// crawler.setProperty("debug", "true");

		List<Document> docs = crawler.crawlDocuments();

		for (Document doc : docs) {
			System.err.println(DocumentUtil.toJsonString(doc));
		}

		DocumentUtil.writeAsLineSeparatedJson(docs,
				new File("src/test/resources/nlp4j.webcrawler.mlit/milt_carinfo-20170101-20171231_json.txt"));

	}

}
