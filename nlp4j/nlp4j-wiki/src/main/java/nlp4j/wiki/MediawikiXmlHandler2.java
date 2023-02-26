package nlp4j.wiki;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 * created on 2021-06-25
 * 
 * @author Hiroki Oya
 */
public class MediawikiXmlHandler2 extends AbstractXmlHandler {

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	//

	private String root = null;

	private static final String MEDIAWIKI = "mediawiki";
	private static final String QNAME_PAGE = "page";

	private static final String ATT_XML_SPACE = "xml:space";
	//
	private String MEDIAWIKI_PAGE_REVISION_ID = MEDIAWIKI + "/page" + "/revision/id";
	private String MEDIAWIKI_PAGE_REVISION_TIMESTAMP = MEDIAWIKI + "/page" + "/revision/timestamp";
	private String PATH001_MEDIAWIKI_PAGE_TITLE = MEDIAWIKI + "/page" + "/title";
	private String PATH003_MEDIAWIKI_PAGE_NAMESPACE = MEDIAWIKI + "/page" + "/ns";
	private String PATH003_MEDIAWIKI_PAGE_ID = MEDIAWIKI + "/page" + "/id";
	private String PATH003_MEDIAWIKI_PAGE_PARENTID = MEDIAWIKI + "/page" + "/parentid";
	private String MEDIAWIKI_PAGE_REDIRECT = MEDIAWIKI + "/page" + "/redirect";
	private String MEDIAWIKI_PAGE_REVISION_TEXT = MEDIAWIKI + "/page" + "/revision/text";
	private String MEDIAWIKI_PAGE_REVISION_FORMAT = MEDIAWIKI + "/page" + "/revision/format";

	// pageID -> WikiPage Object
	private HashMap<String, WikiPage> pages = new HashMap<>();

	private HashMap<String, String> pageInfo = new HashMap<>();

	private void resetPage() {
		pageInfo = new HashMap<>();
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		// root mediawiki
		// root page
		// 悩ましい (2023-01-27)
		if (this.root == null) {
			this.root = qName;
			if ("mediawiki".equals(qName)) {

			} //
			else if ("page".equals(qName)) {

			} //
			else {

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
			resetPage();
		}
		// ELSE IF(リダイレクト)
		else if (super.getPath().equals(MEDIAWIKI_PAGE_REDIRECT)) {
			String redirect_title = attributes.getValue("title");
			pageInfo.put(MEDIAWIKI_PAGE_REDIRECT, redirect_title);
		}
		//
		else if (super.getPath().equals(MEDIAWIKI_PAGE_REVISION_TEXT)) {
			pageInfo.put(ATT_XML_SPACE, attributes.getValue(ATT_XML_SPACE));
		} //
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		if (super.getText() != null && super.getText().isEmpty() == false) {
			this.pageInfo.put(super.getPath(), super.getText());
		}

		// IF (END OF PAGE TAG) THEN
		if (super.getPath().equals("mediawiki/page")) {

			String pageTitle = pageInfo.get(PATH001_MEDIAWIKI_PAGE_TITLE);
			String pageNamespace = pageInfo.get(PATH003_MEDIAWIKI_PAGE_NAMESPACE);
			String pageId = pageInfo.get(PATH003_MEDIAWIKI_PAGE_ID);
			String pageParentId = pageInfo.get(PATH003_MEDIAWIKI_PAGE_PARENTID);
			String pageRevisionId = pageInfo.get(MEDIAWIKI_PAGE_REVISION_ID);
			String pageTimestamp = pageInfo.get(MEDIAWIKI_PAGE_REVISION_TIMESTAMP);
			String pageFormat = pageInfo.get(MEDIAWIKI_PAGE_REVISION_FORMAT);
			// タグ名が紛らわしい感じもするが、本文は mediawiki/page/revision/text に記述されている 2023-01-26
			String pageText = pageInfo.get(MEDIAWIKI_PAGE_REVISION_TEXT);

			WikiPage page = new WikiPage(pageTitle, pageId, pageFormat, pageText);
			{
				// 1 NAMESPACE
				if (pageNamespace != null) {
					page.setNamespace(pageNamespace);
				}
				// 2 PARENTID
				if (pageParentId != null) {
					page.setParentId(pageParentId);
				}
				// 3 REVISION ID
				if (pageRevisionId != null) {
					page.setRevisionId(pageRevisionId);
				}
				{ // 4 TIMESTAMP
					page.setTimestamp(pageTimestamp);
				}
				{ // CATEGORY TAGS (CATEGORIES)
					List<String> categoryTags = MediaWikiTextUtils.parseCategoryTags(pageText);
					if (categoryTags != null && categoryTags.size() > 0) {
						page.setCategoryTags(categoryTags);
					}
				}
				{ // TEMPLATE TAGS
					List<String> templateTags = MediaWikiTextUtils.parseTemplateTags(pageText);
					if (templateTags != null && templateTags.size() > 0) {
						page.setTemplateTags(templateTags);
					}
				}
				// リダイレクト情報
				if (pageInfo.containsKey(MEDIAWIKI_PAGE_REDIRECT)) {
					page.setRedirectTitle(pageInfo.get(MEDIAWIKI_PAGE_REDIRECT));
				}
			}

			pages.put(page.getId(), page);
		} //

		// end of process
		super.endElement(uri, localName, qName);
	}

	/**
	 * @return WikiPage Map (ID -&gt; Object)
	 */
	public Map<String, WikiPage> getPages() {
		return pages;
	}

}
