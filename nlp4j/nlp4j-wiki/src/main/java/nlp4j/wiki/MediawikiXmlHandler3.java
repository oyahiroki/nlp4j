package nlp4j.wiki;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import nlp4j.xml.AbstractXmlHandler;

/**
 * <pre>
 * XML Handler for Media Wiki
 * created_at 2022-05-31
 * </pre>
 * 
 * @author Hiroki Oya
 */
public class MediawikiXmlHandler3 extends AbstractXmlHandler {

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	private static final String ATT_XML_SPACE = "xml:space";

	private static final String PATH_MEDIAWIKI_PAGE = "mediawiki/page";

	private static final String QNAME_PAGE = "page";

	private static final String MEDIAWIKI_PAGE_REVISION_TIMESTAMP = "mediawiki/page/revision/timestamp";

	private static final String PATH001_MEDIAWIKI_PAGE_TITLE = "mediawiki/page/title";

	private static final String PATH003_MEDIAWIKI_PAGE_ID = "mediawiki/page/id";

	private static final String PATH_MEDIAWIKI_PAGE_REVISION_TEXT = "mediawiki/page/revision/text";

	private static final String MEDIAWIKI_PAGE_REVISION_FORMAT = "mediawiki/page/revision/format";

	// pageID -> WikiPage Object
//	private HashMap<String, WikiPage> pages = new HashMap<>();

	private HashMap<String, String> pageInfo = new HashMap<>();

	private void resetPageInfo() {
		pageInfo = new HashMap<>();
	}

//	private int count = 0;

	private WikiPageHandler wikiPageHander;

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		logger.debug("qName=" + qName);

//		count++;
//
//		if (count > 10) {
//			SAXException e = new SAXException();
//			e.initCause(new BreakException());
//			throw e;
//		}

		super.startElement(uri, localName, qName, attributes);

//		for (int idx = 0; idx < attributes.getLength(); idx++) {
//			System.err.println(attributes.getLocalName(idx) + "=" + attributes.getValue(idx));
//		}
//		System.err.println("uri=" + uri + ",localName=" + localName + ",qName=" + qName + ",attributes=");

		if (qName.equals(QNAME_PAGE)) {
			// 255425
			resetPageInfo();
		}
		// System.err.println(qName);
		else if (super.getPath().equals(PATH_MEDIAWIKI_PAGE_REVISION_TEXT)) {
			pageInfo.put(ATT_XML_SPACE, attributes.getValue(ATT_XML_SPACE));
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		if (super.getText() != null && super.getText().isEmpty() == false) {
			this.pageInfo.put(super.getPath(), super.getText());
		}

		// IF (END OF PAGE TAG) THEN
		if (super.getPath().equals(PATH_MEDIAWIKI_PAGE)) {

			String pageTitle = pageInfo.get(PATH001_MEDIAWIKI_PAGE_TITLE);
			String pageId = pageInfo.get(PATH003_MEDIAWIKI_PAGE_ID);
			String pageTimestamp = pageInfo.get(MEDIAWIKI_PAGE_REVISION_TIMESTAMP);
			String pageFormat = pageInfo.get(MEDIAWIKI_PAGE_REVISION_FORMAT);
			String pageText = pageInfo.get(PATH_MEDIAWIKI_PAGE_REVISION_TEXT);

			WikiPage page = new WikiPage(pageTitle, pageId, pageFormat, pageText);
			page.setTimestamp(pageTimestamp);

			if (this.wikiPageHander != null) {
				try {
					this.wikiPageHander.read(page);
				} catch (BreakException be) {
					SAXException e = new SAXException();
					e.initCause(be);
					throw e;
				}
			}

//			pages.put(page.getId(), page);
			
			
		} //

		// end of process
		super.endElement(uri, localName, qName);
	}

	/**
	 * @return WikiPage Map (ID -&gt; Object)
	 */
//	public HashMap<String, WikiPage> getPages() {
//		return pages;
//	}

	public void setWikiPageHander(WikiPageHandler wikiPageHander) {
		this.wikiPageHander = wikiPageHander;

	}

}
