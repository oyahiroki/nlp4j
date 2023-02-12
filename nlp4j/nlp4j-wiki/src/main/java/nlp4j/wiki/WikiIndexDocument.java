package nlp4j.wiki;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nlp4j.impl.DefaultDocument;

/**
 * <pre>
 * getAttributeAsString() 
 *  が呼ばれたタイミングで
 *  データがファイルから読みだされる。
 * 
 * getAttributeAsString(null) 
 *  が呼ばれると
 *  リソースが解放される。
 * 
 * </pre>
 * 
 * created on 2021-08-19
 * 
 * @author Hiroki Oya
 */
public class WikiIndexDocument extends DefaultDocument {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	WikiDumpReader dumpReader;

	String wikiplaintext;

	String wikitext;

	String wikihtml;

	private boolean loaded = false;

	/**
	 * @param key (wikiplaintext | wikitext | wikihtml | null) null の時にリソースを解放する
	 */
	public String getAttributeAsString(String key) {

//		System.err.println("WikiIndexDocument key=" + key);

		if (key == null) {
			this.wikitext = null;
			this.wikiplaintext = null;
			this.wikihtml = null;
			logger.debug("data null set");
			return null;
		}

		if ("item".equals(key)) {
			return super.getAttributeAsString(key);
		}

		else {

			// wikiplaintext
			// wikitext
			// wikihtml

			if (loaded == false) {
				load();
				logger.debug("data loaded");
			}

			if ("wikiplaintext".equals(key)) {
				return wikiplaintext;
			}
			if ("wikitext".equals(key)) {
				return wikitext;
			}
			if ("wikihtml".equals(key)) {
				return wikihtml;
			}

			return super.getAttributeAsString(key);

		}
	}

	/**
	 * @return WikiDumpReader
	 */
	public WikiDumpReader getDumpReader() {
		return dumpReader;
	}

	private void load() {
		String entry = super.getAttributeAsString("item");

		WikiPage page;

		try {

			page = dumpReader.getItem(entry);

			if (page == null) {
				return;
			}

			wikiplaintext = page.getPlainText();

//			System.err.println("<text>");
//			System.err.println(page.getText());
//			System.err.println("</text>");

			wikitext = page.getText();

//			System.err.println("<html>");
//			System.err.println(page.getHtml());
//			System.err.println("</html>");

			wikihtml = page.getHtml();

		} catch (IOException e) {
			e.printStackTrace();
		}

		loaded = true;

	}

	@Override
	public void putAttribute(String key, Object object) {

		logger.debug("WikiIndexDocument putAttribute key=" + key);

		if (key != null) {
			super.putAttribute(key, object);
		}

		if (key == null && object == null) {
			this.wikitext = null;
			this.wikiplaintext = null;
			this.wikihtml = null;
			logger.debug("data null set");
			return;
		}

	}

	@Override
	public void putAttribute(String key, String value) {
//		if ("item".equals(key) == false) {
//			System.err.println("WikiIndexDocument putAttribute key=" + key);
//		}
		super.putAttribute(key, value);
	}

	/**
	 * @param dumpReader
	 */
	public void setDumpReader(WikiDumpReader dumpReader) {
		this.dumpReader = dumpReader;
	}

}
