package nlp4j.webcrawler.mlit;

import java.io.File;
import java.util.List;

import nlp4j.Document;
import nlp4j.util.DocumentUtil;

/**
 * 国土交通省「自動車のリコール・不具合情報」をダウンロードする<br>
 * http://carinf.mlit.go.jp/jidosha/carinf/opn/index.html<br>
 */
public class MlitCarInfoDownloadMain2 {

	/**
	 * @param args 利用しない
	 * @throws Exception 例外発生時
	 */
	public static void main(String[] args) throws Exception {

		MlitCarInfoCrawler crawler = new MlitCarInfoCrawler();
		crawler.setProperty("from_date", "2020/01/01");
		crawler.setProperty("to_date", "2020/01/31");
		// crawler.setProperty("debug", "true");

		List<Document> docs = crawler.crawlDocuments();

		for (Document doc : docs) {
			System.err.println(DocumentUtil.toJsonString(doc));
		}

		DocumentUtil.writeAsLineSeparatedJson(docs,
				new File("/usr/local/nlp4j/collections/mlit/data/json1/milt_carinfo-20200101-20200131_json.txt"));

	}

}
