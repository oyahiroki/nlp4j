package example202301;

import nlp4j.wiki.WikiPage;

public class PagePrinterForTest {

	static public void print(WikiPage page) {
		if (page == null) {
			System.err.println("Not found: ");
		} //
		else {
			System.err.println("---");
			System.err.println("title: " + page.getTitle());
			System.err.println("---");
			System.err.println(page.getTimestamp());
			System.err.println("---");
			System.err.println("redirect: " + page.isRediect());
			if (page.isRediect()) {
				System.err.println("This page is redirected to: " + page.getRediect_title());
			}
			//
			else {
				System.err.println("---");
				System.err.println("categories: " + page.getCategoryTags());
				System.err.println("<text>");
				System.err.println(page.getText()); // <text>...</text>
				System.err.println("</text>");
				System.err.println("---");
				System.err.println("<xml>");
				System.err.println(page.getXml()); // <page>...</page>
				System.err.println("</xml>");
				System.err.println("<root_text>");
				System.err.println(page.getRootNodeWikiText());
				System.err.println("</root_text>");
			}
		}
	}

}
