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
 * テキストファイルを改行区切りでクロールします。<br>
 * Crawl documents from line separated plain text
 * 
 * @author Hiroki Oya
 * @since 1.1.1
 */
public class TextFileLineSeparatedCrawler extends AbstractFileCrawler implements Crawler {

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
	private boolean skipemptyline = false;

	/**
	 * コンストラクタ <br>
	 * Default Constructor
	 */
	public TextFileLineSeparatedCrawler() {
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

				for (String text : FileUtils.readLines(file, encoding)) {

					// 2022-05-11
					if (text == null || text.trim().isEmpty() == true && this.skipemptyline == true) {
						continue;
					}

					Document doc = new DefaultDocument();
					doc.putAttribute(target, text);
					docs.add(doc);
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return docs;
	}

	@Override
	public void setProperty(String key, String value) {
		super.setProperty(key, value);
		if ("skipemptyline".equals(key)) {
			this.skipemptyline = Boolean.parseBoolean(value);
		}
	}

}
