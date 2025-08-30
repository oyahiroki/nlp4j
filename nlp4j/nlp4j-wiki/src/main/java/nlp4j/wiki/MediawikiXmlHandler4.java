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
import org.xml.sax.helpers.DefaultHandler;

import nlp4j.wiki.util.MediaWikiTextUtils;
import nlp4j.xml.AbstractXmlHandler;

/**
 * <pre>
 * XML Handler for Media Wiki
 * </pre>
 * 
 * created on 2025-08-17
 * 
 * @author Hiroki Oya
 */
public class MediawikiXmlHandler4 extends DefaultHandler {

	private static final String ATT_BYTES = "bytes";
//	private static final String TAG_TEXT = "text";

	static private final int sbInitialSize_Small = 16;
	static private final int sbInitialSize_Large = 1024 * 1024;

	protected StringBuilder sb = new StringBuilder(sbInitialSize_Small);

	private static final String TITLE = "title";

//	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	private boolean keepObject = false;

//	private String ROOT = null;

//	private static final String MEDIAWIKI = "mediawiki";
//	private static final String QNAME_PAGE = "page";

	private static final String ATT_XML_SPACE = "xml:space";
	/**
	 * page
	 */
//	private String PATH_MEDIAWIKI_PAGE = "page";
//	private String MEDIAWIKI_PAGE_REVISION_ID = "page/revision/id";
//	private String MEDIAWIKI_PAGE_REVISION_TIMESTAMP = "page/revision/timestamp";
//	private String PATH001_MEDIAWIKI_PAGE_TITLE = "page/title";
//	private String PATH003_MEDIAWIKI_PAGE_NAMESPACE = "page/ns";
//	private String PATH003_MEDIAWIKI_PAGE_ID = "page/id";
//	private String PATH003_MEDIAWIKI_PAGE_PARENTID = "page/parentid";
//	private String PATH003_MEDIAWIKI_PAGE_REDIRECT = "page/redirect";
//	private String PATH_MEDIAWIKI_PAGE_REVISION_TEXT = "page/revision/text";
//	private String MEDIAWIKI_PAGE_REVISION_FORMAT = "page/revision/format";

	// pageID -> WikiPage Object
//	private HashMap<String, String> pageInfo = new HashMap<>();

//	private void resetPageInfo() {
//		pageInfo = new HashMap<>();
//	}

	private WikiPageHandler wikiPageHander;

	private WikiPage pageObj;

	public WikiPage getPage() {
		return pageObj;
	}

	Map<String, WikiPage> pages = new HashMap<String, WikiPage>();

	public Map<String, WikiPage> getPages() {
		if (pageObj == null) {
			return null;
		} else {
			return pages;
		}
	}

	public void setKeepObject(boolean keepObject) {
		this.keepObject = keepObject;
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		if (this.sb != null) {
			sb.append(ch, start, length);

		}
	}

	private String pre_qName = null;

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		switch (qName) { // switch
		case "mediawiki":
			this.sb = null;
			break;
		case "page":
			this.sb = null;
			break;
		case "title":
			sb = new StringBuilder(sbInitialSize_Small);
			break;
		case "ns":
			sb = new StringBuilder(1);
			break;
		case "id": // page/id, page/revision/id, page/revision/contributor/id

			switch (pre_qName) {
			case "page":
				sb = new StringBuilder(8);
				break;
			case "revision":
				sb = new StringBuilder(8);
				break;
			case "contributor":
				sb = null;
				break;
			default:
				sb = null;
			}

			break;
		// リダイレクト先
		// <redirect title="音楽家の一覧" />
		case "redirect":
			this.page_redirect = attributes.getValue(TITLE);
			break;
		case "parentid":
			sb = new StringBuilder(8);
			break;
		case "timestamp":
			sb = new StringBuilder(20);
			break;
		case "username":
			sb = new StringBuilder(8);
			break;
		case "minor":
			sb = null;
			break;
		case "comment":
			sb = null;
			break;
		case "model":
			break;
		// <text bytes="34469" sha1="q677enf99f9g1nsbfba63wci96aqapd"
		// xml:space="preserve">{{混同|語学}}...
		case "text": {
			{
				String v = attributes.getValue(ATT_BYTES);
				if (v != null) {
					int size = Integer.parseInt(v);
					sb = new StringBuilder(size);
				} else {
					sb = new StringBuilder(sbInitialSize_Large);
				}

			}
			{
				this.page_revision_text_xml_space = attributes.getValue(ATT_XML_SPACE);
			}

		}
			break;
		default:
			sb = null;

		} // END_OF_SWITCH

//		// root tag is <mediawiki> or <page>
//		if (ROOT == null) {
//			ROOT = qName;
//			// mediawiki
//			if (ROOT.equals(MEDIAWIKI)) {
//				PATH_MEDIAWIKI_PAGE = MEDIAWIKI + "/" + PATH_MEDIAWIKI_PAGE;
//				MEDIAWIKI_PAGE_REVISION_ID = MEDIAWIKI + "/" + MEDIAWIKI_PAGE_REVISION_ID;
//				MEDIAWIKI_PAGE_REVISION_TIMESTAMP = MEDIAWIKI + "/" + MEDIAWIKI_PAGE_REVISION_TIMESTAMP;
//				PATH001_MEDIAWIKI_PAGE_TITLE = MEDIAWIKI + "/" + PATH001_MEDIAWIKI_PAGE_TITLE;
//				PATH003_MEDIAWIKI_PAGE_NAMESPACE = MEDIAWIKI + "/" + PATH003_MEDIAWIKI_PAGE_NAMESPACE;
//				PATH003_MEDIAWIKI_PAGE_ID = MEDIAWIKI + "/" + PATH003_MEDIAWIKI_PAGE_ID;
//				PATH003_MEDIAWIKI_PAGE_PARENTID = MEDIAWIKI + "/" + PATH003_MEDIAWIKI_PAGE_PARENTID;
//				PATH003_MEDIAWIKI_PAGE_REDIRECT = MEDIAWIKI + "/" + PATH003_MEDIAWIKI_PAGE_REDIRECT;
//				PATH_MEDIAWIKI_PAGE_REVISION_TEXT = MEDIAWIKI + "/" + PATH_MEDIAWIKI_PAGE_REVISION_TEXT;
//				MEDIAWIKI_PAGE_REVISION_FORMAT = MEDIAWIKI + "/" + MEDIAWIKI_PAGE_REVISION_FORMAT;
//			}
//		}

//		// AbstractXmlHandler
//		super.startElement(uri, localName, qName, attributes);

//		if (logger.isDebugEnabled()) {
//			for (int idx = 0; idx < attributes.getLength(); idx++) {
//				logger.debug(attributes.getLocalName(idx) + "=" + attributes.getValue(idx));
//			}
//			logger.debug("uri=" + uri + ",localName=" + localName + ",qName=" + qName + ",attributes=");
//		}

//		if (qName.equals(QNAME_PAGE)) {
//			// 255425
//			resetPageInfo();
//		}
//		// ELSE IF(リダイレクト)
//		// リダイレクト先
//		// <redirect title="音楽家の一覧" />
//		else if (super.getPath().equals(PATH003_MEDIAWIKI_PAGE_REDIRECT)) {
//			String redirect_title = attributes.getValue(TITLE);
//			pageInfo.put(PATH003_MEDIAWIKI_PAGE_REDIRECT, redirect_title);
//		}
//		//
//		else if (super.getPath().equals(PATH_MEDIAWIKI_PAGE_REVISION_TEXT)) {
//			pageInfo.put(ATT_XML_SPACE, attributes.getValue(ATT_XML_SPACE));
//		} //

	}

//	private String page; // 0
	private String page_title; // 1
	private String page_ns; // 2
	private String page_id; // 3
	private String page_redirect; // 3b = 無いときがある
//	private String page_revision; // 4
	private String page_revision_id; // 5
	private String page_revision_parentid; // 6
	private String page_revision_timestamp; // 7
	private String page_revision_contributor_username; // 8
	private String page_revision_contributor_id; // 9
	private String page_revision_minor; // 10
	private String page_revision_comment; // 11
	private String page_revision_model;// 12
	private String page_revision_format; // 13
	private String page_revision_text_xml_space;
	private String page_revision_text; // 14

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		switch (qName) {
		case "mediawiki":
			break;
		case "page":

			WikiPage page = new WikiPage(this.page_title, this.page_id, page_revision_format);

			// 1 NAMESPACE NS
			page.setNamespace(this.page_ns);
			// 2 PARENTID
			page.setParentId(this.page_revision_parentid);
			// 3 REVISION_ID
			page.setRevisionId(this.page_revision_id);
			// 4 TIMESTAMP
			page.setTimestamp(this.page_revision_timestamp);
			// REDIRECT
			page.setRedirectTitle(this.page_redirect);
			// TEXT (xml)
			page.setXml(this.page_revision_text);

			if (this.wikiPageHander != null) {
				try {
					this.wikiPageHander.read(page);
				} catch (BreakException be) {
					SAXException e = new SAXException();
					e.initCause(be);
					throw e;
				}
			}

			if (this.keepObject) {
				this.pageObj = page;
				pages.put(page.getTitle(), page);
			}

		{ // reset
			page_title = null; // 1
			page_ns = null; // 2
			page_id = null; // 3
			page_redirect = null; // 3b = 無いときがある
//				page_revision = null; // 4
			page_revision_id = null; // 5
			page_revision_parentid = null; // 6
			page_revision_timestamp = null; // 7
			page_revision_contributor_username = null; // 8
			page_revision_contributor_id = null; // 9
			page_revision_minor = null; // 10
			page_revision_comment = null; // 11
			page_revision_model = null;// 12
			page_revision_format = null; // 13
			page_revision_text_xml_space = null;
			page_revision_text = null; // 14

		}

			break;
		case "title":
			if (sb != null) {
				this.page_title = sb.toString().trim();
			}
			break;
		case "ns":
			if (sb != null) {
				this.page_ns = sb.toString().trim();
			}
			break;
		case "id": // page/id, page/revision/id, page/revision/contributor/id

			if (sb == null) {
				break;
			}

			switch (pre_qName) {
			case "ns":
				this.page_id = sb.toString().trim();
				break;
			case "id":
				this.page_revision_id = sb.toString().trim();
				break;
			case "username":
				sb = null;
				break;
			default:
			}

			break;
		case "parentid":
			if (sb != null) {
				this.page_revision_parentid = sb.toString().trim();
			}
			break;
		case "timestamp":
			if (sb != null) {
				this.page_revision_timestamp = sb.toString().trim();
			}
			break;
		case "username":
			if (sb != null) {
				this.page_revision_contributor_username = sb.toString().trim();
			}
			break;
		case "minor":
			if (sb != null) {
				this.page_revision_minor = sb.toString().trim();
			}
			break;
		case "comment":
			if (sb != null) {
				this.page_revision_comment = sb.toString().trim();
			}
			break;
		case "model":
			if (sb != null) {
				this.page_revision_model = sb.toString().trim();
			}
			break;
		case "text":

			if (sb != null) {
				page_revision_text = (sb.toString().trim());
			}

			break;
		case "x":
			break;
		default:

		}

//		if (super.getText() != null && super.getText().isEmpty() == false) {
//			this.pageInfo.put(super.getPath(), super.getText());
//		}

//		System.err.println(super.getPath());

		// IF (END OF PAGE TAG) THEN
//		if (super.getPath().equals(PATH_MEDIAWIKI_PAGE)) {

//			String pageTitle = pageInfo.get(PATH001_MEDIAWIKI_PAGE_TITLE);
//			String pageNamespace = pageInfo.get(PATH003_MEDIAWIKI_PAGE_NAMESPACE);
//			String pageId = pageInfo.get(PATH003_MEDIAWIKI_PAGE_ID);
//			String pageParentId = pageInfo.get(PATH003_MEDIAWIKI_PAGE_PARENTID);
//			String pageRedirect = pageInfo.get(PATH003_MEDIAWIKI_PAGE_REDIRECT);
//			String pageRevisonId = pageInfo.get(MEDIAWIKI_PAGE_REVISION_ID);
//			String pageTimestamp = pageInfo.get(MEDIAWIKI_PAGE_REVISION_TIMESTAMP);
//			String pageFormat = pageInfo.get(MEDIAWIKI_PAGE_REVISION_FORMAT);
//			String pageText = pageInfo.get(PATH_MEDIAWIKI_PAGE_REVISION_TEXT);

//			WikiPage page = new WikiPage(pageTitle, pageId, pageFormat);
//			{
//				// 1 NAMESPACE NS
//				if (pageNamespace != null) {
//					page.setNamespace(pageNamespace);
//				}
//				// 2 PARENTID
//				if (pageParentId != null) {
//					page.setParentId(pageParentId);
//				}
//				// 3 REVISION_ID
//				if (pageRevisonId != null) {
//					page.setRevisionId(pageRevisonId);
//				}
//				{// 4 TIMESTAMP
//					page.setTimestamp(pageTimestamp);
//				}
//				// REDIRECT
//				if (pageRedirect != null) {
//					page.setRedirectTitle(pageRedirect);
//				}
//			}
		// SET TEXT
		{
//				String text = org.apache.commons.text.StringEscapeUtils.unescapeXml(pageText);
//				page.setText(text);
//				{ // CATEGORY TAGS (CATEGORIES)
//					List<String> categoryTags = MediaWikiTextUtils.parseCategoryTags(text);
//					page.setCategoryTags(categoryTags);
//				}
//				{ // TEMPLATE TAGS
//					List<String> templateTags = MediaWikiTextUtils.parseTemplateTags(text);
//					page.setTemplateTags(templateTags);
//				}
		}
		// nlp4j.wiki.util.MediaWikiTextUtils.parseCategoryTags(String)
		// SET XML
//			{
//				page.setXml(pageText);
//			}

//			if (this.wikiPageHander != null) {
//				try {
//					this.wikiPageHander.read(page);
//				} catch (BreakException be) {
//					SAXException e = new SAXException();
//					e.initCause(be);
//					throw e;
//				}
//			}

//			if (this.keepObject == true) {
//				this.pageObj = page;
//			}

//			pages.put(page.getId(), page);

//		} //

		// end of process
		super.endElement(uri, localName, qName);
		this.pre_qName = qName;

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
		if (this.wikiPageHander != null) {
			this.wikiPageHander.startDocument();
		}
	}

	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
		if (this.wikiPageHander != null) {
			this.wikiPageHander.endDocument();
		}
	}

}
