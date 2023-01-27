package nlp4j.wiki;

import java.util.HashMap;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import nlp4j.wiki.util.MediaWikiTextUtils;
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

	private String root = null;

	private String MEDIAWIKI_PAGE_REVISION_TIMESTAMP = "mediawiki/page" + "/revision/timestamp";

	private String PATH001_MEDIAWIKI_PAGE_TITLE = "mediawiki/page" + "/title";

	private String PATH003_MEDIAWIKI_PAGE_ID = "mediawiki/page" + "/id";

	private String MEDIAWIKI_PAGE_REDIRECT = "mediawiki/page" + "/redirect";

	private String MEDIAWIKI_PAGE_REVISION_TEXT = "mediawiki/page" + "/revision/text";

	private String MEDIAWIKI_PAGE_REVISION_FORMAT = "mediawiki/page" + "/revision/format";

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
		// ELSE IF(リダイレクト)
		else if (super.getPath().equals(MEDIAWIKI_PAGE_REDIRECT)) {
			String redirect_title = attributes.getValue("title");
			pageInfo.put(MEDIAWIKI_PAGE_REDIRECT, redirect_title);
//			System.err.println("redirect_title: " + redirect_title);
		}
		//
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
			// タグ名が紛らわしい感じもするが、本文は mediawiki/page/revision/text に記述されている 2023-01-26
			String pageText = pageInfo.get(MEDIAWIKI_PAGE_REVISION_TEXT);

			WikiPage page = new WikiPage(pageTitle, pageId, pageFormat, pageText);
			// TIMESTAMP
			{
				page.setTimestamp(pageTimestamp);
			}

			// CATEGORY TAGS (CATEGORIES)
			{
				List<String> categoryTags = MediaWikiTextUtils.parseCategoryTags(pageText);
				if (categoryTags != null && categoryTags.size() > 0) {
					page.setCategoryTags(categoryTags);
				}
			}

			// リダイレクト情報
			if (pageInfo.containsKey(MEDIAWIKI_PAGE_REDIRECT)) {
				page.setRedirectTitle(pageInfo.get(MEDIAWIKI_PAGE_REDIRECT));
			}

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
