package nlp4j.wiki;

import java.io.IOException;

import info.bliki.wiki.model.WikiModel;
import nlp4j.wiki.util.MediaWikiTextUtils;

/**
 * <pre>
 * Wiki Page Entry
 * 
 * created_at 2021-06-25
 * </pre>
 * 
 * @author Hiroki Oya
 */
public class WikiPage {

	private String title = null;
	private String id = null;
	private String format = null;
	private String text = null;

	private String xml;
	private String timestamp;

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
		return MediaWikiTextUtils.toPlainText(this.title, this.text);
	}

	/**
	 * Return Wiki Entry as Wiki Text
	 * 
	 * @return Wiki Text
	 */
	public String getText() {
		return text;
	}

	public String getTimestamp() {
		return timestamp;
	}

	/**
	 * @return title of wiki entry
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return page XML data : &lt;page&gt;...&lt;/page&gt;
	 */
	public String getXml() {
		return this.xml;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @param xml XML data : &lt;page&gt;...&lt;/page&gt;
	 */
	public void setXml(String xml) {
		this.xml = xml;
	}

	@Override
	public String toString() {

		String t = (this.text != null && this.text.length() > 16) ? text.substring(0, 16).replace("\n", "") + "..."
				: "" + text;

		int textlength = (this.text == null) ? -1 : this.text.length();

		String x = (this.xml != null && this.xml.length() > 16) ? xml.substring(0, 16).replace("\n", "") + "..."
				: "" + xml;

		return "WikiPage [" //
				+ "id=" + id + ", "//
				+ "timestamp=" + timestamp + ", "//
				+ "format=" + format + ", "//
				+ "title=" + title + ", "//
				+ "text=" + t + ", "//
				+ "text.length=" + textlength + ", "//
				+ "xml=" + x //
				+ "]";
	}

}
