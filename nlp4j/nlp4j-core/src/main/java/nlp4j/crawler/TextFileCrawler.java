package nlp4j.crawler;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nlp4j.Document;
import nlp4j.impl.DefaultDocument;

/**
 * テキストファイルをクロールします。<br>
 * Crawl documents from plain text
 * 
 * @author Hiroki Oya
 *
 */
public class TextFileCrawler extends AbstractFileCrawler implements Crawler {

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	/**
	 * コンストラクタ <br>
	 * Default Constructor
	 */
	public TextFileCrawler() {
		super();
		prop.setProperty("target", "text");
	}

	@Override
	public ArrayList<Document> crawlDocuments() {

		ArrayList<Document> docs = new ArrayList<>();

		String target = prop.getProperty("target");

		if (target == null) {
			logger.warn("target is not set.");
			return docs;
		}

		for (File file : super.files) {
			try {
				String text = FileUtils.readFileToString(file, encoding);
				Document doc = new DefaultDocument();
				doc.putAttribute(target, text);
				docs.add(doc);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return docs;
	}
}
