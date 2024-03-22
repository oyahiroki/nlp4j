package nlp4j.wiki.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import junit.framework.TestCase;
import nlp4j.Keyword;
import nlp4j.test.TestUtils;
import nlp4j.util.JsonUtils;
import nlp4j.util.KeywordsUtil;
import nlp4j.wiki.util.MediaWikiTextUtils;
import nlp4j.wiki.util.WikiUtils;

public class MediaWikiClientTestCase extends TestCase {

	public void testExpandTemplates001() throws Exception {
		String host = "ja.wikipedia.org";
		MediaWikiClient client = new MediaWikiClient(host);
		String wikiText = "test";
		client.expandTemplates(wikiText);
	}

	public void testExpandTemplates002() throws Exception {

		// Template:大学
		// https://ja.wikipedia.org/w/index.php?title=Template:%E5%A4%A7%E5%AD%A6&action=edit

		String host = "ja.wikipedia.org";
		MediaWikiClient client = new MediaWikiClient(host);
		String wikiText = "{{日本の大学\r\n" //
				+ "| 大学名=同志社大学\r\n" //
				+ "| ロゴ= [[File:Dousisya univ.svg|210px]]\r\n" //
				+ "| 画像=170128 Doshisha University Imadegawa Campus Kyoto Japan08s3.jpg\r\n" //
				+ "| 画像説明=今出川キャンパス（2017年1月）\r\n" //
				+ "| pxl=250px\r\n" //
				+ "| 大学設置年=1920年\r\n" //
				+ "| 創立年=1875年\r\n" //
				+ "| 学校種別=私立\r\n" //
				+ "| 設置者=[[学校法人同志社]]\r\n" //
				+ "| 本部所在地=[[京都府]][[京都市]][[上京区]][[今出川通]][[烏丸通|烏丸]]東入玄武町601番地\r\n" //
				+ "| 緯度度=35 |緯度分=1 |緯度秒=47.1\r\n" //
				+ "| 経度度=135 |経度分=45 |経度秒=38.7\r\n" //
				+ "| キャンパス=[[同志社大学今出川キャンパス|今出川]]（京都府京都市上京区）<br />[[同志社大学新町キャンパス|新町]]（京都府京都市上京区）<br />[[同志社大学室町キャンパス|室町]]（京都府京都市上京区）<br />[[同志社大学京田辺キャンパス|京田辺]]（京都府[[京田辺市]]）<br />学研都市（京都府[[木津川市]]）<br />烏丸（京都府京都市上京区）\r\n" //
				+ "|学部=[[同志社大学神学部|神学部]]<br />[[同志社大学文学部|文学部]]<br />[[同志社大学社会学部|社会学部]]<br />[[同志社大学法学部|法学部]]<br />[[同志社大学経済学部|経済学部]]<br />[[同志社大学商学部|商学部]]<br />[[同志社大学政策学部|政策学部]]<br />[[同志社大学文化情報学部|文化情報学部]]<br />[[同志社大学理工学部|理工学部]]<br />[[同志社大学大学院生命医科学研究科・生命医科学部|生命医科学部]]<br />[[同志社大学スポーツ健康科学部|スポーツ健康科学部]]<br />[[同志社大学心理学部|心理学部]]<br />グローバル・コミュニケーション学部<br />グローバル地域文化学部\r\n" //
				+ "|研究科=神学研究科<br />[[同志社大学大学院文学研究科・文学部|文学研究科]]<br />[[同志社大学大学院社会学研究科・社会学部|社会学研究科]]<br />[[同志社大学大学院法学研究科・法学部|法学研究科]]<br /> [[同志社大学大学院経済学研究科・経済学部|経済学研究科]]<br />商学研究科<br />総合政策科学研究科<br />文化情報学研究科<br />理工学研究科<br />[[同志社大学大学院生命医科学研究科・生命医科学部|生命医科学研究科]]<br />スポーツ健康科学研究科<br />心理学研究科<br />グローバル・スタディーズ研究科<br />[[同志社大学法学部|司法研究科]]<br />ビジネス研究科<br />脳科学研究科\r\n" //
				+ "|ウェブサイト= https://www.doshisha.ac.jp/\r\n" //
				+ "}}";
		client.expandTemplates(wikiText);
	}

	public void testgetPageContentByTitle001() throws Exception {

		String host = "ja.wikipedia.org";

		{
			String title = "イチロー";
			String expected = "野球";
			checkFirstSentenceOfContent(host, title, expected);
		}

		{
			String title = "松井秀喜";
			String expected = "野球";
			checkFirstSentenceOfContent(host, title, expected);
		}
		{
			String title = "タモリ";
			String expected = "タレント";
			checkFirstSentenceOfContent(host, title, expected);
		}
		{
			String title = "うしろゆびさされ組";
			String expected = "ユニット";
			checkFirstSentenceOfContent(host, title, expected);
		}
		{
			String title = "鈴木彩艶";
			String expected = "サッカー";
			checkFirstSentenceOfContent(host, title, expected);
		}
		{
			String title = "ハリー・ポッターシリーズ";
			String expected = "小説";
			checkFirstSentenceOfContent(host, title, expected);
		}
		{
			String title = "即死チートが最強すぎて、異世界のやつらがまるで相手にならないんですが。";
			String expected = "ライトノベル";
			checkFirstSentenceOfContent(host, title, expected);
		}
		{
			String title = "Twitter";
			String expected = "ソーシャル";
			checkFirstSentenceOfContent(host, title, expected);
		}
		{
			String title = "貴乃花光司";
			String expected = "横綱";
			checkFirstSentenceOfContent(host, title, expected);
		}
		{
			String title = "能登半島地震_(2024年)";
			String expected = "穴水";
			checkFirstSentenceOfContent(host, title, expected);
		}
		{
			String title = "Apple Watch";
			String expected = "腕時計";
			checkFirstSentenceOfContent(host, title, expected);
		}

	}

	private void checkFirstSentenceOfContent(String host, String title, String expected) throws IOException {
		try (MediaWikiClient client = new MediaWikiClient(host);) {
			String wiki_content = client.getPageContentByTitle(title);
			String rootNodeText = MediaWikiTextUtils.getRootNodeTextFirstSentence(wiki_content, title, "ja");
			System.out.println(rootNodeText);
			assertTrue(rootNodeText.contains(expected));
		}
	}

	public void testgetPageContentByTitle002() throws Exception {

		String host = "ja.wikipedia.org";

		{
			String title = "光る君へ";
			try (MediaWikiClient client = new MediaWikiClient(host);) {
				String wiki_content = client.getPageContentByTitle(title);
				String rootNodeText = MediaWikiTextUtils.getRootNodeTextFirstSentence(wiki_content, title, "ja");
				System.out.println(rootNodeText);
			}
		}

	}

	public void testgetPageContentByTitle003() throws Exception {

		String host = "ja.wikipedia.org";

		{
			String title = "ゲート_自衛隊_彼の地にて、斯く戦えり";
			try (MediaWikiClient client = new MediaWikiClient(host);) {
				String wiki_content = client.getPageContentByTitle(title);
				String rootNodeText = MediaWikiTextUtils.getRootNodeTextFirstSentence(wiki_content, title, "ja");
				System.out.println(rootNodeText);
			}
		}

	}

	public void testgetPageContentByTitle004() throws Exception {
		String host = "ja.wikipedia.org";
		{
			String title = "即死チートが最強すぎて、異世界のやつらがまるで相手にならないんですが。";
			String expected = "ライトノベル";
			checkFirstSentenceOfContent(host, title, expected);
		}

	}

	public void testgetPageContentByTitle005() throws Exception {
		String lang = "ja";
		String host = lang + ".wikipedia.org";
		String title = "大谷翔平";
		{
			try (MediaWikiClient client = new MediaWikiClient(host);) {
				String wiki_content = client.getPageContentByTitle(title);
				String rootNodeText = MediaWikiTextUtils.getRootNodeTextFirstSentence(wiki_content, title, lang);
				System.out.println(rootNodeText);
				{
					String wiki_content_html = WikiUtils.toHtml(wiki_content);
					List<Keyword> kwds = WikiUtils.extractKeywordsFromWikiHtml(wiki_content_html, "wikilink");
					kwds.stream().forEach(kw -> {
						System.out.println(kw.getLex());
					});
				}
			}
		}

	}

	public void testgetPageContentByTitle006() throws Exception {
		String lang = "ja";
		String host = lang + ".wikipedia.org";
		List<Keyword> kwds1 = new ArrayList<Keyword>();
		List<Keyword> kwds2 = new ArrayList<Keyword>();
		List<Keyword> kwds3 = new ArrayList<Keyword>();
		{
			try (MediaWikiClient client = new MediaWikiClient(host);) {
				{
					String title = "笠置シヅ子";
					String wiki_content = client.getPageContentByTitle(title);
//					String rootNodeText = MediaWikiTextUtils.getRootNodeTextFirstSentence(wiki_content, title, lang);
//					System.out.println(rootNodeText);
					{
						kwds1 = WikiUtils.extractKeywordsFromWikiText2(wiki_content, "wiki");
						kwds1.stream().forEach(kw -> {
							System.out.println(kw.getLex());
						});
					}
				}
				System.out.println("---");
				{
					String title2 = "美空ひばり";
					String wiki_content = client.getPageContentByTitle(title2);
//					String rootNodeText = MediaWikiTextUtils.getRootNodeTextFirstSentence(wiki_content, title2, lang);
//					System.out.println(rootNodeText);
					{
						kwds2 = WikiUtils.extractKeywordsFromWikiText2(wiki_content, "wiki");
						kwds2.stream().forEach(kw -> {
							System.out.println(kw.getLex());
						});
					}
				}
				System.out.println("---");
				{
					String title2 = "美空ひばり";
					String wiki_content = client.getPageContentByTitle(title2);
//					String rootNodeText = MediaWikiTextUtils.getRootNodeTextFirstSentence(wiki_content, title2, lang);
//					System.out.println(rootNodeText);
					{
						kwds2 = WikiUtils.extractKeywordsFromWikiText2(wiki_content, "wiki");
						kwds2.stream().forEach(kw -> {
							System.out.println(kw.getLex());
						});
					}
				}
				System.out.println("---");
				{
					String title3 = "あいみょん";
					String wiki_content = client.getPageContentByTitle(title3);
					{
						kwds3 = WikiUtils.extractKeywordsFromWikiText2(wiki_content, "wiki");
						kwds3.stream().forEach(kw -> {
							System.out.println(kw.getLex());
						});
					}
				}

			}
		}
		System.out.println("---");
		List<Keyword> kwds12 = KeywordsUtil.And(kwds1, kwds2);
		kwds12.stream().forEach(kw -> {
			System.out.println(kw.getLex());
		});
		System.out.println("---");
		List<Keyword> kwds13 = KeywordsUtil.And(kwds1, kwds3);
		kwds13.stream().forEach(kw -> {
			System.out.println(kw.getLex());
		});

		System.out.println(kwds1.size());
		System.out.println(kwds2.size());
		System.out.println(kwds3.size());
		System.out.println(kwds12.size());
		System.out.println(kwds13.size());
		System.out.println((double) kwds12.size() / (double) kwds1.size());
		System.out.println((double) kwds12.size() / (double) kwds2.size());
		System.out.println((double) kwds13.size() / (double) kwds1.size());
		System.out.println((double) kwds13.size() / (double) kwds3.size());

	}

	public void testgetPageContentByTitle007() throws Exception {
		String lang = "ja";
		String host = lang + ".wikipedia.org";
		{
			try (MediaWikiClient client = new MediaWikiClient(host);) {
				{
					String title = "笠置シヅ子";
					String wiki_content = client.getPageContentByTitle(title);
					List<String> tags = MediaWikiTextUtils.parseCategoryTags(wiki_content);
					System.out.println(tags);
				}
			}
		}
	}

	public void testgetPageContentByTitle007b() throws Exception {
		String lang = "ja";
		String host = lang + ".wikipedia.org";
		{
			try (MediaWikiClient client = new MediaWikiClient(host);) {
				{
					String title = "ダルビッシュ有";
					String wiki_content = client.getPageContentByTitle(title);
					List<String> tags = MediaWikiTextUtils.parseCategoryTags(wiki_content);
					System.out.println(tags);
				}
			}
		}
	}

	public void testgetPageContentByTitle008() throws Exception {
		String lang = "en";
		String host = lang + ".wikipedia.org";
		{
			try (MediaWikiClient client = new MediaWikiClient(host);) {
				{
					String title = "IBM";
					String wiki_content = client.getPageContentByTitle(title);
					List<String> tags = MediaWikiTextUtils.parseCategoryTags(wiki_content);
					System.out.println(tags);
				}
			}
		}
	}

	public void testgetPageContentByTitle101() throws Exception {
		String host = "en.wikipedia.org";
		{
			String title = "IBM";
			try (MediaWikiClient client = new MediaWikiClient(host);) {
				String wiki_content = client.getPageContentByTitle(title);
				String rootNodeText = MediaWikiTextUtils.getRootNodeTextFirstSentence(wiki_content, title, "en");
				System.out.println(rootNodeText);
			}
		}

	}

	public void testgetPageContentByTitle102() throws Exception {
		TestUtils.setLevelDebug();
		String host = "de.wikipedia.org";
		{
			String title = "Roy_Orbison";
			try (MediaWikiClient client = new MediaWikiClient(host);) {
				// タイトルを指定してコンテンツを取得する
				String wiki_content = client.getPageContentByTitle(title);
				String rootNodeText = MediaWikiTextUtils.getRootNodeTextFirstSentence(wiki_content, title, "en");
				System.out.println(rootNodeText);
			}
		}

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

	public void testMediaWikiClient110() throws Exception {
		String host = "ja.wiktionary.org";
		String q = "学校";

		MediaWikiClient client = new MediaWikiClient(host);
		client.setFetchSubCategory(true);

		JsonObject res = client.search(q);

		System.err.println(JsonUtils.prettyPrint(res));

		JsonArray results = res.get("query").getAsJsonObject().get("search").getAsJsonArray();
		for (int n = 0; n < results.size(); n++) {
			System.err.println(results.get(n).getAsJsonObject().get("title").getAsString());
			System.err.println(results.get(n).getAsJsonObject().get("snippet").getAsString());
		}

	}

	/**
	 * <pre>
	 * 英語版WikipediaのAuto_partsカテゴリーのページを検索する
	 * Get Page titles of "Category:Auto_parts" from WikiPedia English
	 * </pre>
	 * 
	 * @throws Exception
	 */
	public void testMediaWikiClientGetPageTitlesByCategory001() throws Exception {
		String host = "en.wikipedia.org";
		String category = "Category:Auto_parts";
		try (MediaWikiClient client = new MediaWikiClient(host);) {
			List<String> titles = client.getPageTitlesByCategory(category);
			for (String title : titles) {
				System.out.println(title);
			}
			System.out.println(titles.size());
		}
	}

}
