package hello.wikiapi;

import java.util.List;

import junit.framework.TestCase;

public class MediaWikiClientTestCase extends TestCase {

	public void testMediaWikiClient001() throws Exception {
		String host = "en.wikipedia.org";
		String category = "Category:Auto_parts";
		MediaWikiClient client = new MediaWikiClient(host);
		List<String> titles = client.getPageTitlesByCategory(category);
		for (String title : titles) {
			System.err.println(title);
		}
		System.err.println(titles.size());
	}

	public void testMediaWikiClient002() throws Exception {
		String host = "ja.wiktionary.org";
		String category = "カテゴリ:日本語_ことわざ";
		MediaWikiClient client = new MediaWikiClient(host);
		List<String> titles = client.getPageTitlesByCategory(category);
		for (String title : titles) {
			System.err.println(title);
		}
		System.err.println(titles.size());
	}

}
