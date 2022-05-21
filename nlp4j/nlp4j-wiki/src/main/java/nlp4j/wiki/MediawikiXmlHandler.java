package nlp4j.wiki;

import java.util.HashMap;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import nlp4j.xml.AbstractXmlHandler;

/**
 * <pre>
 * XML Handler for Media Wiki
 * created_at 2021-06-25
 * </pre>
 * 
 * @author Hiroki Oya
 * @deprecated
 */
public class MediawikiXmlHandler extends AbstractXmlHandler {

	// pageID -> WikiPage Object
	HashMap<String, WikiPage> pages = new HashMap<>();

	private String pageTitle = null;
	private String pageNS = null;

	private String pageId = null;
	private String pageParentid = null;
	private String pageTimestamp = null;
	private String pageContributorUsername = null;
	private String pageContributorId = null;
	private String pageComment = null;
	private String pageModel = null;
	private String pageFormat = null;
	private String pageTextXmlSpace = null;
	private String pageText = null;

	private void resetPage() {
		this.pageTitle = null;
		this.pageNS = null;

		this.pageId = null;
		this.pageParentid = null;
		this.pageFormat = null;
		this.pageTextXmlSpace = null;
		this.pageText = null;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		super.startElement(uri, localName, qName, attributes);

//		for (int idx = 0; idx < attributes.getLength(); idx++) {
//			System.err.println(attributes.getLocalName(idx) + "=" + attributes.getValue(idx));
//		}
//		System.err.println("uri=" + uri + ",localName=" + localName + ",qName=" + qName + ",attributes=");

		if (qName.equals("page")) {
			// 255425
			resetPage();
		}
		// System.err.println(qName);
		else if (super.getPath().equals("mediawiki/page/revision/text")) {
			this.pageTextXmlSpace = attributes.getValue("xml:space");
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		// mediawiki/page/title
		if (super.getPath().equals("mediawiki/page/title")) {
			this.pageTitle = super.getText();
		} //
			// mediawiki/page/ns
		else if (super.getPath().equals("mediawiki/page/ns")) {
			this.pageNS = super.getText();
		} //
			// mediawiki/page/id
		else if (super.getPath().equals("mediawiki/page/id")) {
			this.pageId = super.getText();
		} //
		else if (super.getPath().equals("mediawiki/page/parentid")) {
			this.pageParentid = super.getText();
		} //
		else if (super.getPath().equals("mediawiki/page/timestamp")) {
			this.pageTimestamp = super.getText();
		} //
		else if (super.getPath().equals("mediawiki/page/contributor/username")) {
			this.pageContributorUsername = super.getText();
		} //
		else if (super.getPath().equals("mediawiki/page/contributor/id")) {
			this.pageContributorId = super.getText();
		} //
		else if (super.getPath().equals("mediawiki/page/comment")) {
			this.pageComment = super.getText();
		} //
		else if (super.getPath().equals("mediawiki/page/model")) {
			this.pageModel = super.getText();
		} //
		else if (super.getPath().equals("mediawiki/page/revision/format")) {
			this.pageFormat = super.getText();
		} //
		else if (super.getPath().equals("mediawiki/page/revision/text")) {
			this.pageText = super.getText();
		} //
		else if (super.getPath().equals("mediawiki/page")) {
//			if (this.pageId != null && this.pageId.equals("32")) 
			{
//				System.err.println(this.pageTitle);
//				System.err.println(this.pageId);
//				System.err.println(this.pageFormat);
//				System.err.println(this.pageText);

				WikiPage page = new WikiPage(this.pageTitle, this.pageId, this.pageFormat, this.pageText);

				pages.put(page.getId(), page);
			}
		} //

//		System.err.println(super.getPath());

		// end of process
		super.endElement(uri, localName, qName);
	}

	/**
	 * @return WikiPage Map (ID -&gt; Object)
	 */
	public HashMap<String, WikiPage> getPages() {
		return pages;
	}

}
