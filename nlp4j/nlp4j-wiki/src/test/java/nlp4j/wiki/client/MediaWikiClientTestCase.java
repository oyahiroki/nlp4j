package nlp4j.wiki.client;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class MediaWikiClientTestCase extends TestCase {

	/**
	 * Query "Category:Auto_parts" 英語版 WikiPedia 自動車部品
	 * 
	 * @throws Exception
	 */
	public void testMediaWikiClient001() throws Exception {
		String host = "en.wikipedia.org";
		String category = "Category:Auto_parts";
		MediaWikiClient client = new MediaWikiClient(host);
		List<String> titles = client.getPageTitlesByCategory(category);
		for (String title : titles) {
			System.out.println(title);
		}
		System.out.println(titles.size());
	}

	/**
	 * Query "Category:en:Medicine" 英語版 WikiPedia 医療用語
	 * 
	 * @throws Exception
	 */
	public void testMediaWikiClient002() throws Exception {
		String host = "en.wikipedia.org";
		String category = "Category:en:Medicine";
		MediaWikiClient client = new MediaWikiClient(host);
		List<String> titles = client.getPageTitlesByCategory(category);
		for (String title : titles) {
			System.out.println(title);
		}
		System.out.println(titles.size());
	}

	/**
	 * Query "カテゴリ:日本語_ことわざ" 日本語版 WikiPedia ことわざ
	 * 
	 * @throws Exception
	 */
	public void testMediaWikiClient101() throws Exception {
		String host = "ja.wiktionary.org";
		String category = "カテゴリ:日本語_ことわざ";
		MediaWikiClient client = new MediaWikiClient(host);
		List<String> titles = client.getPageTitlesByCategory(category);
		for (String title : titles) {
			System.out.println(title);
		}
		System.out.println(titles.size());
	}

	/**
	 * "Category:RNAウイルス" 日本語版 WikiPedia RNAウィルス
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
			System.out.println(title);
		}
	}

	/**
	 * 
	 * 
	 * @throws Exception
	 */
	public void testMediaWikiClient103() throws Exception {
		String host = "ja.wikipedia.org";
//		String category = "Category:自動車部品";
		String category = "Category:自動車ブレーキメーカー";
		MediaWikiClient client = new MediaWikiClient(host);
		client.setFetchSubCategory(true);

		List<String> categories = new ArrayList<String>();

		client.getSubcategoryTitlesByCategory(0, categories, null, category);
		for (String title : categories) {
			System.out.println(title);
		}
	}

	/**
	 * 
	 * 
	 * @throws Exception
	 */
	public void testMediaWikiClient104() throws Exception {
//		String host = "ja.wikipedia.org";
//		String category = "Category:鉄道駅";
//		MediaWikiClient client = new MediaWikiClient(host);
//		client.setFetchSubCategory(true);
//
//		List<String> categories = new ArrayList<String>();
//
//		client.getSubcategoryTitlesByCategory(0, categories, null, category);
//		for (String title : categories) {
//			System.out.println(title);
//		}
	}

}
