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
 */
public class MediawikiXmlHandler2 extends AbstractXmlHandler {

	private static final String MEDIAWIKI_PAGE_REVISION_TIMESTAMP = "mediawiki/page/revision/timestamp";

	private static final String PATH001_MEDIAWIKI_PAGE_TITLE = "mediawiki/page/title";

	private static final String PATH003_MEDIAWIKI_PAGE_ID = "mediawiki/page/id";

	private static final String MEDIAWIKI_PAGE_REVISION_TEXT = "mediawiki/page/revision/text";

	private static final String MEDIAWIKI_PAGE_REVISION_FORMAT = "mediawiki/page/revision/format";

	// pageID -> WikiPage Object
	private HashMap<String, WikiPage> pages = new HashMap<>();

	private HashMap<String, String> pageInfo = new HashMap<>();

	private void resetPage() {
		pageInfo = new HashMap<>();
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
		else if (super.getPath().equals(MEDIAWIKI_PAGE_REVISION_TEXT)) {
			pageInfo.put("xml:space", attributes.getValue("xml:space"));
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		if (super.getText() != null && super.getText().isEmpty() == false) {
			this.pageInfo.put(super.getPath(), super.getText());
		}

		// IF (END OF PAGE TAG) THEN
		if (super.getPath().equals("mediawiki/page")) {

			String pageTitle = pageInfo.get(PATH001_MEDIAWIKI_PAGE_TITLE);
			String pageId = pageInfo.get(PATH003_MEDIAWIKI_PAGE_ID);
			String pageTimestamp = pageInfo.get(MEDIAWIKI_PAGE_REVISION_TIMESTAMP);
			String pageFormat = pageInfo.get(MEDIAWIKI_PAGE_REVISION_FORMAT);
			String pageText = pageInfo.get(MEDIAWIKI_PAGE_REVISION_TEXT);

			WikiPage page = new WikiPage(pageTitle, pageId, pageFormat, pageText);
			page.setTimestamp(pageTimestamp);

			pages.put(page.getId(), page);
		} //

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
