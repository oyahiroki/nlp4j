package nlp4j.wiki;

import java.io.IOException;

import org.sweble.wikitext.engine.PageId;
import org.sweble.wikitext.engine.PageTitle;
import org.sweble.wikitext.engine.WtEngineImpl;
import org.sweble.wikitext.engine.config.WikiConfig;
import org.sweble.wikitext.engine.nodes.EngProcessedPage;
import org.sweble.wikitext.engine.utils.DefaultConfigEnWp;
import org.sweble.wikitext.example.TextConverter;

import info.bliki.wiki.model.WikiModel;

/**
 * Wiki Page Entry
 * 
 * @author Hiroki Oya
 * @created_at 2021-06-25
 */
public class WikiPage {

	// Set-up a simple wiki configuration
	WikiConfig config = DefaultConfigEnWp.generate();

	String title = null;
	String id = null;
	String format = null;
	String text = null;

	private String xml;

	/**
	 * @param title  : title of wiki entry
	 * @param id     : id of wiki entry
	 * @param format : format of wiki entry
	 * @param text   : text of wiki entry
	 */
	public WikiPage(String title, String id, String format, String text) {
		super();
		this.title = title;
		this.id = id;
		this.format = format;
		this.text = text;
	}

	/**
	 * @return format of wiki entry
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * Return Wiki Entry as HTML
	 * 
	 * @return HTML null on error
	 */
	public String getHtml() {
		WikiModel model = new WikiModel("", "");
		model.setUp();
		String html;
		try {
			html = model.render(this.text);
			return html;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * @return id of wiki entry
	 */
	public String getId() {
		return id;
	}

	/**
	 * Return Wiki Entry as Plain Text
	 * 
	 * @return TEXT null on error
	 */
	public String getPlainText() {
		try {
			final int wrapCol = 1000;
			// Retrieve a page
			PageTitle pageTitle = PageTitle.make(this.config, this.title);
			PageId pageId = new PageId(pageTitle, -1);
			// Instantiate a compiler for wiki pages
			WtEngineImpl engine = new WtEngineImpl(config);
			// Compile the retrieved page
			EngProcessedPage cp = engine.postprocess(pageId, this.text, null);
			TextConverter p = new TextConverter(config, wrapCol);
			String text = (String) p.go(cp.getPage());
			return text;

		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Return Wiki Entry as Wiki Text
	 * 
	 * @return Wiki Text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @return title of wiki entry
	 */
	public String getTitle() {
		return title;
	}

	public String getXml() {
		return this.xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

}
