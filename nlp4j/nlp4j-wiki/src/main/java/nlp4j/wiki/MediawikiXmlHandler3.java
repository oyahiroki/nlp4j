package nlp4j.wiki;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;

//import org.apache.commons.lang3.StringEscapeUtils;
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

	private String ROOT = null;

	private static final String TAG_MEDIAWIKI = "mediawiki";
	private static final String QNAME_PAGE = "page";

	private String ATT_XML_SPACE = "xml:space";
	private String PATH_MEDIAWIKI_PAGE = "page";
	private String MEDIAWIKI_PAGE_REVISION_TIMESTAMP = "page/revision/timestamp";
	private String PATH001_MEDIAWIKI_PAGE_TITLE = "page/title";
	private String PATH003_MEDIAWIKI_PAGE_ID = "page/id";
	private String PATH_MEDIAWIKI_PAGE_REVISION_TEXT = "page/revision/text";
	private String MEDIAWIKI_PAGE_REVISION_FORMAT = "page/revision/format";

	// pageID -> WikiPage Object
	private HashMap<String, String> pageInfo = new HashMap<>();

	private void resetPageInfo() {
		pageInfo = new HashMap<>();
	}

	private WikiPageHandler wikiPageHander;

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		logger.debug("qName=" + qName);

		// root tag is <mediawiki> or <page>
		if (ROOT == null) {
			ROOT = qName;
			if (ROOT.equals(TAG_MEDIAWIKI)) {
				PATH_MEDIAWIKI_PAGE = TAG_MEDIAWIKI + "/" + PATH_MEDIAWIKI_PAGE;

				MEDIAWIKI_PAGE_REVISION_TIMESTAMP = TAG_MEDIAWIKI + "/" + MEDIAWIKI_PAGE_REVISION_TIMESTAMP;
				PATH001_MEDIAWIKI_PAGE_TITLE = TAG_MEDIAWIKI + "/" + PATH001_MEDIAWIKI_PAGE_TITLE;
				PATH003_MEDIAWIKI_PAGE_ID = TAG_MEDIAWIKI + "/" + PATH003_MEDIAWIKI_PAGE_ID;
				PATH_MEDIAWIKI_PAGE_REVISION_TEXT = TAG_MEDIAWIKI + "/" + PATH_MEDIAWIKI_PAGE_REVISION_TEXT;
				MEDIAWIKI_PAGE_REVISION_FORMAT = TAG_MEDIAWIKI + "/" + MEDIAWIKI_PAGE_REVISION_FORMAT;
			}
		}

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

//		System.err.println(super.getPath());

		// IF (END OF PAGE TAG) THEN
		if (super.getPath().equals(PATH_MEDIAWIKI_PAGE)) {

			String pageTitle = pageInfo.get(PATH001_MEDIAWIKI_PAGE_TITLE);
			String pageId = pageInfo.get(PATH003_MEDIAWIKI_PAGE_ID);
			String pageTimestamp = pageInfo.get(MEDIAWIKI_PAGE_REVISION_TIMESTAMP);
			String pageFormat = pageInfo.get(MEDIAWIKI_PAGE_REVISION_FORMAT);
			String pageText = pageInfo.get(PATH_MEDIAWIKI_PAGE_REVISION_TEXT);

			WikiPage page = new WikiPage(pageTitle, pageId, pageFormat);
			page.setTimestamp(pageTimestamp);
			{
				String text = org.apache.commons.text.StringEscapeUtils.unescapeXml(pageText);
				page.setText(text);
			}
			page.setXml(pageText);

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
