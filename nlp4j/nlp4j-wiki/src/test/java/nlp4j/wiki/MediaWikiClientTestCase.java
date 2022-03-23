package nlp4j.wiki;

import java.util.List;

import junit.framework.TestCase;

public class MediaWikiClientTestCase extends TestCase {

	/**
	 * Query "Category:Auto_parts"
	 * 
	 * @throws Exception
	 */
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

	/**
	 * Query "Category:en:Medicine"
	 * 
	 * @throws Exception
	 */
	public void testMediaWikiClient002() throws Exception {
		String host = "en.wikipedia.org";
		String category = "Category:en:Medicine";
		MediaWikiClient client = new MediaWikiClient(host);
		List<String> titles = client.getPageTitlesByCategory(category);
		for (String title : titles) {
			System.err.println(title);
		}
		System.err.println(titles.size());
	}

	/**
	 * Query "カテゴリ:日本語_ことわざ"
	 * 
	 * @throws Exception
	 */
	public void testMediaWikiClient101() throws Exception {
		String host = "ja.wiktionary.org";
		String category = "カテゴリ:日本語_ことわざ";
		MediaWikiClient client = new MediaWikiClient(host);
		List<String> titles = client.getPageTitlesByCategory(category);
		for (String title : titles) {
			System.err.println(title);
		}
		System.err.println(titles.size());
	}

	/**
	 * "Category:RNAウイルス"
	 * 
	 * @throws Exception
	 */
	public void testMediaWikiClient102() throws Exception {
		String host = "ja.wikipedia.org";
		String category = "Category:RNAウイルス";
		MediaWikiClient client = new MediaWikiClient(host);
		client.setFetchSubCategory(false);
		List<String> titles = client.getPageTitlesByCategory(category);
		for (String title : titles) {
			if (title.contains("ウイルス")) {
				System.err.println(title);
			}
		}
		System.err.println(titles.size());
	}

}
