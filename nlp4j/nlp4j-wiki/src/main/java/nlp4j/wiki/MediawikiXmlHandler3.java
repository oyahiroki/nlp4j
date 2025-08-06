package nlp4j.wiki;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import nlp4j.wiki.util.MediaWikiTextUtils;
import nlp4j.xml.AbstractXmlHandler;

/**
 * <pre>
 * XML Handler for Media Wiki
 * </pre>
 * 
 * created on 2022-05-31
 * 
 * @author Hiroki Oya
 */
public class MediawikiXmlHandler3 extends AbstractXmlHandler {

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	private boolean keepObject = false;

	private String ROOT = null;

	private static final String MEDIAWIKI = "mediawiki";
	private static final String QNAME_PAGE = "page";

	private static final String ATT_XML_SPACE = "xml:space";
	/**
	 * page
	 */
	private String PATH_MEDIAWIKI_PAGE = "page";
	private String MEDIAWIKI_PAGE_REVISION_ID = "page/revision/id";
	private String MEDIAWIKI_PAGE_REVISION_TIMESTAMP = "page/revision/timestamp";
	private String PATH001_MEDIAWIKI_PAGE_TITLE = "page/title";
	private String PATH003_MEDIAWIKI_PAGE_NAMESPACE = "page/ns";
	private String PATH003_MEDIAWIKI_PAGE_ID = "page/id";
	private String PATH003_MEDIAWIKI_PAGE_PARENTID = "page/parentid";
	private String PATH003_MEDIAWIKI_PAGE_REDIRECT = "page/redirect";
	private String PATH_MEDIAWIKI_PAGE_REVISION_TEXT = "page/revision/text";
	private String MEDIAWIKI_PAGE_REVISION_FORMAT = "page/revision/format";

	// pageID -> WikiPage Object
	private HashMap<String, String> pageInfo = new HashMap<>();

	private void resetPageInfo() {
		pageInfo = new HashMap<>();
	}

	private WikiPageHandler wikiPageHander;

	private WikiPage pageObj;

	public WikiPage getPage() {
		return pageObj;
	}

	public Map<String, WikiPage> getPages() {
		if (pageObj == null) {
			return null;
		} else {
			Map<String, WikiPage> pages = new HashMap<String, WikiPage>();
			pages.put(this.pageObj.getTitle(), pageObj);
			return pages;
		}
	}

	public void setKeepObject(boolean keepObject) {
		this.keepObject = keepObject;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		logger.debug("qName=" + qName);

		// root tag is <mediawiki> or <page>
		if (ROOT == null) {
			ROOT = qName;
			if (ROOT.equals(MEDIAWIKI)) {
				PATH_MEDIAWIKI_PAGE = MEDIAWIKI + "/" + PATH_MEDIAWIKI_PAGE;

				MEDIAWIKI_PAGE_REVISION_ID = MEDIAWIKI + "/" + MEDIAWIKI_PAGE_REVISION_ID;
				MEDIAWIKI_PAGE_REVISION_TIMESTAMP = MEDIAWIKI + "/" + MEDIAWIKI_PAGE_REVISION_TIMESTAMP;
				PATH001_MEDIAWIKI_PAGE_TITLE = MEDIAWIKI + "/" + PATH001_MEDIAWIKI_PAGE_TITLE;
				PATH003_MEDIAWIKI_PAGE_NAMESPACE = MEDIAWIKI + "/" + PATH003_MEDIAWIKI_PAGE_NAMESPACE;
				PATH003_MEDIAWIKI_PAGE_ID = MEDIAWIKI + "/" + PATH003_MEDIAWIKI_PAGE_ID;
				PATH003_MEDIAWIKI_PAGE_PARENTID = MEDIAWIKI + "/" + PATH003_MEDIAWIKI_PAGE_PARENTID;
				PATH003_MEDIAWIKI_PAGE_REDIRECT = MEDIAWIKI + "/" + PATH003_MEDIAWIKI_PAGE_REDIRECT;
				PATH_MEDIAWIKI_PAGE_REVISION_TEXT = MEDIAWIKI + "/" + PATH_MEDIAWIKI_PAGE_REVISION_TEXT;
				MEDIAWIKI_PAGE_REVISION_FORMAT = MEDIAWIKI + "/" + MEDIAWIKI_PAGE_REVISION_FORMAT;
			}
		}

		// AbstractXmlHandler
		super.startElement(uri, localName, qName, attributes);

		if (logger.isDebugEnabled()) {
			for (int idx = 0; idx < attributes.getLength(); idx++) {
				logger.debug(attributes.getLocalName(idx) + "=" + attributes.getValue(idx));
			}
			logger.debug("uri=" + uri + ",localName=" + localName + ",qName=" + qName + ",attributes=");
		}

		if (qName.equals(QNAME_PAGE)) {
			// 255425
			resetPageInfo();
		}
		// ELSE IF(リダイレクト)
		else if (super.getPath().equals(PATH003_MEDIAWIKI_PAGE_REDIRECT)) {
			String redirect_title = attributes.getValue("title");
			pageInfo.put(PATH003_MEDIAWIKI_PAGE_REDIRECT, redirect_title);
		}
		//
		else if (super.getPath().equals(PATH_MEDIAWIKI_PAGE_REVISION_TEXT)) {
			pageInfo.put(ATT_XML_SPACE, attributes.getValue(ATT_XML_SPACE));
		} //
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
			String pageNamespace = pageInfo.get(PATH003_MEDIAWIKI_PAGE_NAMESPACE);
			String pageId = pageInfo.get(PATH003_MEDIAWIKI_PAGE_ID);
			String pageParentId = pageInfo.get(PATH003_MEDIAWIKI_PAGE_PARENTID);
			String pageRedirect = pageInfo.get(PATH003_MEDIAWIKI_PAGE_REDIRECT);
			String pageRevisonId = pageInfo.get(MEDIAWIKI_PAGE_REVISION_ID);
			String pageTimestamp = pageInfo.get(MEDIAWIKI_PAGE_REVISION_TIMESTAMP);
			String pageFormat = pageInfo.get(MEDIAWIKI_PAGE_REVISION_FORMAT);
			String pageText = pageInfo.get(PATH_MEDIAWIKI_PAGE_REVISION_TEXT);

			WikiPage page = new WikiPage(pageTitle, pageId, pageFormat);
			{
				// 1 NAMESPACE NS
				if (pageNamespace != null) {
					page.setNamespace(pageNamespace);
				}
				// 2 PARENTID
				if (pageParentId != null) {
					page.setParentId(pageParentId);
				}
				// 3 REVISION_ID
				if (pageRevisonId != null) {
					page.setRevisionId(pageRevisonId);
				}
				{// 4 TIMESTAMP
					page.setTimestamp(pageTimestamp);
				}
				// REDIRECT
				if (pageRedirect != null) {
					page.setRedirectTitle(pageRedirect);
				}
			}
			// SET TEXT
			{
				String text = org.apache.commons.text.StringEscapeUtils.unescapeXml(pageText);
				page.setText(text);
				{ // CATEGORY TAGS (CATEGORIES)
					List<String> categoryTags = MediaWikiTextUtils.parseCategoryTags(text);
					page.setCategoryTags(categoryTags);
				}
				{ // TEMPLATE TAGS
					List<String> templateTags = MediaWikiTextUtils.parseTemplateTags(text);
					page.setTemplateTags(templateTags);
				}
			}
			// nlp4j.wiki.util.MediaWikiTextUtils.parseCategoryTags(String)
			// SET XML
			{
				page.setXml(pageText);
			}

			if (this.wikiPageHander != null) {
				try {
					this.wikiPageHander.read(page);
				} catch (BreakException be) {
					SAXException e = new SAXException();
					e.initCause(be);
					throw e;
				}
			}

			if (this.keepObject == true) {
				this.pageObj = page;
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

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		if (this.wikiPageHander instanceof AbstractWikiPageHandler) {
			((AbstractWikiPageHandler) this.wikiPageHander).startDocument();
		}
	}

	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
		if (this.wikiPageHander instanceof AbstractWikiPageHandler) {
			((AbstractWikiPageHandler) this.wikiPageHander).endDocument();
		}
	}

}
