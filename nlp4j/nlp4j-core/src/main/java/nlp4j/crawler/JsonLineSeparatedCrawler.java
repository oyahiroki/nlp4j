package nlp4j.crawler;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nlp4j.Document;
import nlp4j.util.DocumentUtil;

/**
 * 改行区切りJSONのクローラー<br>
 * Crawler for Line Separated JSON
 * 
 * @author Hiroki Oya
 * @since 1.1.1
 */
public class JsonLineSeparatedCrawler extends AbstractFileCrawler implements Crawler {

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	/**
	 * 改行区切りJSONのクローラー コンストラクタ <br>
	 * Default Constructor of Line Separated JSON Crawler
	 */
	public JsonLineSeparatedCrawler() {
		super();
		prop.setProperty("target", "text");
	}

	@Override
	public List<Document> crawlDocuments() {

		ArrayList<Document> docs = new ArrayList<>();

		for (File file : super.files) {
			try {

				// 形態素解析済みの文書を読み込む
				docs.addAll(DocumentUtil.readFromLineSeparatedJson(file));

			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}

		logger.debug("documents read: " + docs.size());

		return docs;
	}

}
