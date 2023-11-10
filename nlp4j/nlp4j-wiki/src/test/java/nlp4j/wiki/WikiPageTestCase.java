package nlp4j.wiki;

import junit.framework.TestCase;

public class WikiPageTestCase extends TestCase {

	public void testWikiPageStringStringString001() {
		String title = "大学";
		String id = "30345";
		String format = "text/x-wiki";

		WikiPage page = new WikiPage(title, id, format);
		System.err.println(page);
		assertNotNull(page);
	}

	public void testWikiPageStringStringStringString001() {
		String title = "test";
		String id = "1";
		String format = "text/x-wiki";
		String text = "# TEST\nThis is test.";
		WikiPage page = new WikiPage(title, id, format, text);
		System.err.println(page.getText());
	}

	public void testGetCategoryTags() {
		// TODO TEST
	}

	public void testGetFormat() {
		// TODO TEST
	}

	public void testGetHtml() {
		// TODO TEST
	}

	public void testGetId() {
		// TODO TEST
	}

	public void testGetPlainText001() {
		String title = "test";
		String id = "1";
		String format = "text/x-wiki";
		String text = "== TEST ==\nThis is test.";
		WikiPage page = new WikiPage(title, id, format, text);
		System.err.println(page.getPlainText());
	}

	public void testGetPlainText002() {
		String title = "地理学";
		String id = "1";
		String format = "text/x-wiki";
		String text = "{{otheruses||プトレマイオスの著書|地理学 (プトレマイオス)}}\r\n" //
				+ "{{出典の明記|date=2009年4月}}\r\n" //
				+ "{{ウィキポータルリンク|地理学|[[画像:Gnome-globe.svg|34px|Portal:地理学]]}}\r\n" //
				+ "{{読み仮名_ruby不使用|'''地理学'''|ちりがく|{{Lang-en-short|geography}}、{{Lang-fr-short|géographie}}、{{lang-it-short|geografia}}、{{Lang-de-short|Geographie (-fie)}} または {{lang|de|Erdkunde}}}}は、[[地球]]表面の自然・人文事象の状態と、それらの相互関係を研究する学問<ref>「[https://www.scj.go.jp/ja/info/kohyo/pdf/kohyo-22-h140930-7.pdf  報告 大学教育の分野別質保証のための教育課程編成上の参照基準 地理学分野]」平成26年（2014年）9月30日 日本学術会議 地域研究委員会・地球惑星科学委員会合同 地理教育分科会 2023年1月15日閲覧</ref>。[[地域]]や空間、場所、[[自然環境]]という[[物理]]的存在を対象の中に含むことから、<!--ここから出典あり-->[[人文科学]]、[[社会科学]]、[[自然科学]]のいずれの性格も有する<ref name=\"ajg18\">{{Cite web|url=http://www.ajg.or.jp/wp-content/uploads/2018/03/shin_vision.pdf|title=公益社団法人日本地理学会『新ビジョン（中期目標）』|format=PDF|accessdate=2018-07-07}}</ref>。広範な領域を網羅する。また「地理学と[[哲学]]は諸科学の母」と称される<ref>{{Cite web|url=https://www.hosei.ac.jp/bungaku/gakka/geography/naiyo.html|title=地理学科の内容｜文学部｜法政大学|accessdate=2018-09-21}}</ref>。\r\n" //
				+ "\r\n" //
				+ "元来は[[農耕]]や[[戦争]]、[[統治]]のため、各地の[[情報]]を[[調査]]しまとめるための研究領域として成立した。"; //
		WikiPage page = new WikiPage(title, id, format, text);
		System.err.println(page.getPlainText());
	}

	public void testGetRediect_title() {
		// TODO TEST
	}

	public void testGetText() {
		// TODO TEST
	}

	public void testGetTimestamp() {
		// TODO TEST
	}

	public void testGetTitle() {
		// TODO TEST
	}

	public void testGetXml() {
		// TODO TEST
	}

	public void testIsRediect() {
		// TODO TEST
	}

	public void testSetCategoryTags() {
		// TODO TEST
	}

	public void testSetFormat() {
		// TODO TEST
	}

	public void testSetId() {
		// TODO TEST
	}

	public void testSetRedirectTitle() {
		// TODO TEST
	}

	public void testSetText() {
		// TODO TEST
	}

	public void testSetTimestamp() {
		// TODO TEST
	}

	public void testSetTitle() {
		// TODO TEST
	}

	public void testSetXml() {
		// TODO TEST
	}

	public void testToString() {
		// TODO TEST
	}

}
