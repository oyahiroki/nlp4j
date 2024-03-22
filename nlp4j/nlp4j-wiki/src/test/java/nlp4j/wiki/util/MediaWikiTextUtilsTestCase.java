package nlp4j.wiki.util;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.sweble.wikitext.engine.EngineException;
import org.sweble.wikitext.parser.parser.LinkTargetException;

import junit.framework.TestCase;
import nlp4j.wiki.client.MediaWikiClient;

public class MediaWikiTextUtilsTestCase extends TestCase {

	public void testgetRootNodeTextFirstSentence001() throws Exception {
		String wikiText = "'''イチロー'''（本名：'''鈴木 一朗'''〈すずき いちろう〉、" + "[[1973年]][[10月22日]]" + "<ref name=\"mlb\">"
				+ "{{Cite web2 |url=http://m.mlb.com/player/400085/ichiro-suzuki#sectionType=career |title=Ichiro Suzuki Stats, Fantasy & News |website=MLB.com |publisher=MLB Advanced Media, LP |date= |accessdate=2017年2月2日}}"
				+ "</ref>"
				+ " - ）は、[[愛知県]][[西春日井郡]][[豊山町]]出身{{R|number20170202|sanspo20160616}}の元[[プロ野球選手]]（[[外野手]]）。右投左打。";
		String title = "イチロー";
		String lang = "ja";
		String firstSentence = MediaWikiTextUtils.getRootNodeTextFirstSentence(wikiText, title, lang);
		System.out.println(firstSentence);
	}

	public void testgetRootNodeTextFirstSentence002() throws Exception {
		String wikiText = "{{Nihongo|'''Ichiro Suzuki''' {{IPAc-en|ˈ|iː|tʃ|ɪ|r|oʊ|_|s|uː|ˈ|z|uː|k|i}}<!---This is the English pronunciation; feel free to add the Japanese pronunciation SEPARATELY.--->|鈴木 一朗|Suzuki Ichirō|born 22 October 1973}}, also known [[mononymously]] as {{Nihongo|'''Ichiro'''|イチロー|Ichirō}}, is a Japanese former [[professional baseball]] [[outfielder]] who played professionally for 28 seasons. He played the first nine years of his career with the [[Orix Buffaloes|Orix BlueWave]] of [[Nippon Professional Baseball]] (NPB), and the next 12 years with the [[Seattle Mariners]] of [[Major League Baseball]] (MLB). ";
		String title = "Ichiro Suzuki";
		String lang = "en";
		String firstSentence = MediaWikiTextUtils.getRootNodeTextFirstSentence(wikiText, title, lang);
		System.out.println(firstSentence);
	}

	public void testgetRootNodeTextFirstSentence101() throws Exception {

		String wikipage_title = "イチロー";
		String lang = "ja";
		String host = lang + ".wikipedia.org";

		try (MediaWikiClient client = new MediaWikiClient(host);) {
			String wiki_content = client.getPageContentByTitle(wikipage_title);
			String rootNodeText = MediaWikiTextUtils.getRootNodeTextFirstSentence(wiki_content, wikipage_title, lang);
			System.out.println("rootNodeText: " + rootNodeText);
		}

	}

	public void testGetRootNodeText001() throws IOException {
		// x-wiki 形式のテキストを用意する
		// Prepare x-wiki format text
		File file = new File("src/test/resources/hello.sweble/wiki-example.txt");
		String charsetName = "UTF-8";
		String wikitext = FileUtils.readFileToString(file, charsetName);
		String rootNodeText = MediaWikiTextUtils.getRootNodeText(wikitext);

		System.err.println("<rootNodeText>");
		System.err.println(rootNodeText);
		System.err.println("</rootNodeText>");

	}

	/**
	 * created on 2022-09-13
	 * 
	 * @throws IOException
	 */
	public void testGetRootNodeText002() throws IOException {
		// x-wiki 形式のテキストを用意する
		// Prepare x-wiki format text
		File file = new File("src/test/resources/nlp4j.wiki.util/wiki-example-ichiro.txt");
		String charsetName = "UTF-8";
		String wikitext = FileUtils.readFileToString(file, charsetName);
		String rootNodeText = MediaWikiTextUtils.getRootNodeText(wikitext);

		System.err.println("<rootNodeText>");
		System.err.println(rootNodeText);
		System.err.println("</rootNodeText>");
	}

	/**
	 * created on 2022-09-13
	 * 
	 * @throws IOException
	 */
	public void testGetRootNodeText003() throws IOException {
		// x-wiki 形式のテキストを用意する
		// Prepare x-wiki format text
		File file = new File("src/test/resources/nlp4j.wiki.util/wiki-example-ichiro.txt");
		String charsetName = "UTF-8";
		String title = "イチロー";
		String wikitext = FileUtils.readFileToString(file, charsetName);
		String t_rootNodeText = MediaWikiTextUtils.getRootNodeText(wikitext);

//		System.err.println("<rootNodeText>");
//		System.err.println(t_rootNodeText);
//		System.err.println("</rootNodeText>");

		t_rootNodeText = MediaWikiTextUtils.toPlainText(title, t_rootNodeText);
		t_rootNodeText = t_rootNodeText.replace("\n\n", " ") //
				.replace("\n", "") //
				.replaceAll("\\[\\[.*?\\]\\]", "") //
				.replaceAll("（.*?）", "") //
				.trim();
		t_rootNodeText = t_rootNodeText.replace("**", "");

		System.err.println(t_rootNodeText);

	}

	/**
	 * created on 2022-09-13
	 * 
	 * @throws IOException
	 */
	public void testGetRootNodeText004() throws IOException {
		// x-wiki 形式のテキストを用意する
		// Prepare x-wiki format text
		File file = new File("src/test/resources/nlp4j.wiki.util/wiki-example-matsui.txt");
		String charsetName = "UTF-8";
		String title = "松井秀喜";
		String wikitext = FileUtils.readFileToString(file, charsetName);
		String t_rootNodeText = MediaWikiTextUtils.getRootNodeText(wikitext);

		System.err.println("<rootNodeText>");
		System.err.println(t_rootNodeText);
		System.err.println("</rootNodeText>");

		t_rootNodeText = MediaWikiTextUtils.toPlainText(title, t_rootNodeText);
		t_rootNodeText = t_rootNodeText.replace("\n\n", " ") //
				.replace("\n", "") //
				.replaceAll("\\[\\[.*?\\]\\]", "") //
				.replaceAll("（.*?）", "") //
				.trim();
		t_rootNodeText = t_rootNodeText.replace("**", "");

		System.err.println(t_rootNodeText);

	}

	public void testGetWikiPageLinks() {

	}

	public void testParseTemplateTags001() throws IOException {
		// x-wiki 形式のテキストを用意する
		// Prepare x-wiki format text
		File file = new File("src/test/resources/hello.sweble/wiki-example.txt");
		String charsetName = "UTF-8";
		String wikitext = FileUtils.readFileToString(file, charsetName);
		List<String> tags = MediaWikiTextUtils.parseTemplateTags(wikitext);

		System.err.println("<tags>");
		System.err.println(tags.toString());
		System.err.println("</tags>");
	}

	/**
	 * created on 2023-01-18
	 * 
	 * @throws IOException
	 */
	public void testParseTemplateTags002() throws IOException {
		String wikitext = "{{Otheruses}}\r\n" //
				+ "{{Redirect|鈴木一朗|その他の同名の人物|鈴木一朗 (曖昧さ回避)}}\r\n" //
				+ "xxx\r\n" //
				+ "{{abc}}\r\n" //
				+ "{{def}}";
		List<String> tags = MediaWikiTextUtils.parseTemplateTags(wikitext);

		System.err.println("<tags>");
		System.err.println(tags.toString());
		System.err.println("</tags>");
	}

	public void testRemoveFirstTemplate001() {
		String wikitext = "{{...}}abc";
		String t = MediaWikiTextUtils.removeFirstTemplate(wikitext);
		String expected = "abc";
		assertEquals(expected, t);
	}

	public void testRemoveFirstTemplate002() {
		String wikitext = "{{...}}abc{{...}}";
		String t = MediaWikiTextUtils.removeFirstTemplate(wikitext);
		String expected = "abc{{...}}";
		assertEquals(expected, t);
	}

	public void testRemoveFirstTemplate003() {
		String wikitext = "{{...}}";
		String t = MediaWikiTextUtils.removeFirstTemplate(wikitext);
		String expected = "";
		assertEquals(expected, t);
	}

	public void testRemoveInfobox001() {
		String wikitext = "aaa\r\n" //
				+ "{{Infobox Continent\r\n" //
				+ "|image = [[File:Europe (orthographic projection).svg|200px]]\r\n" //
				+ "|countries = 50\r\n" //
				+ "}}\r\n" //
				+ "\r\n" //
				+ "bbb\r\n" //
				+ "\r\n" //
				+ "ccc\r\n" //
				+ "{{Infobox Continent\r\n" //
				+ "|image = [[File:Europe (orthographic projection).svg|200px]]\r\n" //
				+ "|countries = 50\r\n" //
				+ "}}\r\n" //
				+ "{{Infobox {{}}{{}}{{{}}}xxx}}" + "";

		System.err.println(wikitext);

		System.err.println("---");

		String wikitext2 = MediaWikiTextUtils.removeInfobox(wikitext);

		System.err.println(wikitext2);

	}

	public void testRemoveLinkFirst001() {
		String wikitext = "[[...]]abc[[...]]def[[...]]";
		String t = MediaWikiTextUtils.removeLinkFirst(wikitext);
		String expected = "abc[[...]]def[[...]]";
		assertEquals(expected, t);
	}

	public void testRemoveLinkFirst002() {
		String wikitext = "abcdef[[...]]";
		String t = MediaWikiTextUtils.removeLinkFirst(wikitext);
		String expected = "abcdef";
		assertEquals(expected, t);
	}

	public void testRemoveTable001() {
		String wikitext = "{{Pathnav|スーパー戦隊シリーズ|frame=1}}\r\n" //
				+ "{{半保護}}\r\n" //
				+ "{{注意|クレジットなどで確認できない[[スーツアクター]]の役柄を記載する場合には、'''必ず[[Wikipedia:信頼できる情報源|信頼可能な情報源]]からの[[Wikipedia:出典を明記する|出典を示してください]]。'''出典の無い情報については、[[Wikipedia:独自研究は載せない]]に基づき一定期間ののち除去されるおそれがあります（[[プロジェクト:特撮/スーツアクターの役名記載について]]での議論に基づく）}}\r\n" //
				+ "{| style=\"float: right; text-align:center; border-collapse:collapse; border:2px solid black; white-space:nowrap\"\r\n" //
				+ "|-\r\n" //
				+ "|colspan=\"3\" style=\"background-color:#ffccff; border:1px solid black; white-space:nowrap\"|'''[[スーパー戦隊シリーズ]]'''\r\n" //
				+ "|-\r\n" //
				+ "|style=\"border:1px solid black; background-color:#ffccff; white-space:nowrap\"|'''第1作'''\r\n" //
				+ "|style=\"border:1px solid black; white-space:nowrap\"|'''秘密戦隊<br />ゴレンジャー'''\r\n" //
				+ "|style=\"border:1px solid black; white-space:nowrap\"|1975年4月<br />- 1977年3月\r\n" //
				+ "|-\r\n" //
				+ "|style=\"border:1px solid black; white-space:nowrap; background-color:#ffccff\"|'''第2作'''\r\n" //
				+ "|style=\"border:1px solid black; white-space:nowrap\"|[[ジャッカー電撃隊|ジャッカー<br />電撃隊]]\r\n" //
				+ "|style=\"border:1px solid black; white-space:nowrap\"|1977年4月<br />- 1977年12月\r\n" //
				+ "|}\r\n" //
				+ "『'''秘密戦隊ゴレンジャー'''』 (ひみつせんたいゴレンジャー) は、[[1975年]][[4月5日]]から[[1977年]][[3月26日]]まで、NET系列で毎週土曜19:30 - 20:00 ([[日本標準時|JST]]) に全84話が放送された、[[テレビ朝日|NET]] (現・テレビ朝日)・[[東映]]制作の[[特撮テレビ番組一覧|特撮テレビドラマ]]、および作中に登場するヒーローチームの名称。\r\n" //
				+ "";

		System.err.println(wikitext);

		String wikitext2 = MediaWikiTextUtils.removeTable(wikitext);

		System.err.println("<result>");
		System.err.println(wikitext2);
		System.err.println("</result>");

	}

	public void testRemoveTemplateAll001() {
		String wikitext = "{{...}}abc{{...}}def{{...}}";
		String t = MediaWikiTextUtils.removeTemplateAll(wikitext);
		String expected = "abcdef";
		assertEquals(expected, t);
	}

	public void testRemoveTemplateAll002() {
		String wikitext = "abcdef";
		String t = MediaWikiTextUtils.removeTemplateAll(wikitext);
		String expected = "abcdef";
		assertEquals(expected, t);
	}

	public void testSweble001() throws IOException, LinkTargetException, EngineException {
		String wikititle = "テスト";
		String wikitext = "== TEST ==\nTHIS IS TEST.\nTHIS IS TEST";
		String t = MediaWikiTextUtils.sweble(wikititle, wikitext);

		System.err.println("<t>");
		System.err.println(t);
		System.err.println("</t>");
	}

	public void testToPlainText001() {
		String wikitext = "{{otheruses}}\r\n" //
				+ "{{Infobox Continent\r\n" //
				+ "|image = [[File:Europe (orthographic projection).svg|200px]]\r\n" //
				+ "|countries = 50\r\n" //
				+ "}}\r\n" //
				+ "\r\n" //
				+ "This is test. [TEST]\r\n" //
				+ "\r\n" //
				+ "== TITLE ==\r\n" //
				+ "\r\n" //
				+ "This is test2.\r\n" //
				+ "\r\n" //
				+ "";
		String plainText = MediaWikiTextUtils.toPlainText("test", wikitext);

		System.err.println("<before>");
		System.err.println(wikitext);
		System.err.println("</before>");

		System.err.println("<after>");
		System.err.println(plainText);
		System.err.println("</after>");
	}

	public void testToPlainText002() throws IOException {
		// x-wiki 形式のテキストを用意する
		// Prepare x-wiki format text
		File file = new File("src/test/resources/hello.sweble/wiki-example.txt");
		String charsetName = "UTF-8";
		String wikitext = FileUtils.readFileToString(file, charsetName);
		String plainText = MediaWikiTextUtils.toPlainText("test", wikitext);

		System.err.println("<before>");
		System.err.println(wikitext);
		System.err.println("</before>");

		System.err.println("<after>");
		System.err.println(plainText);
		System.err.println("</after>");
	}

	public void testToPlainText003() throws Exception {
		String title = "松井秀喜"; //
		String wikiText = "{{Infobox baseball player\r\n"//
				+ "| 選手名 = 松井 秀喜\r\n"//
				+ "| 所属球団 =\r\n"//
				+ "| 役職 =\r\n"//
				+ "| 背番号 =\r\n"//
				+ "| 画像 = Hideki Matsui in USA-7.jpg\r\n"//
				+ "| 画像説明 = ニューヨーク・ヤンキース選手時代<br />（2007年）\r\n"//
				+ "| 国籍 = {{JPN}}\r\n"//
				+ "| 出身地 = [[石川県]][[能美郡]][[根上町]]<br />（現・[[能美市]]）\r\n"//
				+ "| 生年月日 = {{生年月日と年齢|1974|6|12}}\r\n"//
				+ "| 身長 = {{フィートとcm (身長用変換)|6|2}}\r\n"//
				+ "| 体重 = {{ポンドとkg (体重用変換)|230}}\r\n"//
				+ "| 利き腕 = 右\r\n"//
				+ "| 打席 = 左\r\n"//
				+ "| 守備位置 = [[外野手]]\r\n"//
				+ "| プロ入り年度 = {{NPBドラフト|1992}}\r\n"//
				+ "| ドラフト順位 = ドラフト1位\r\n"//
				+ "| 初出場 = NPB / 1993年5月1日<br />MLB / 2003年3月31日\r\n"//
				+ "| 最終出場 = NPB / 2002年10月30日<br />MLB / 2012年7月22日\r\n"//
				+ "| 年俸 =\r\n"//
				+ "| 経歴 =\r\n"//
				+ "* [[星稜中学校・高等学校|星稜高等学校]]\r\n"//
				+ "* [[読売ジャイアンツ]] (1993 - 2002)\r\n"//
				+ "* [[ニューヨーク・ヤンキース]] (2003 - 2009)\r\n"//
				+ "* [[ロサンゼルス・エンゼルス・オブ・アナハイム]] (2010)\r\n"//
				+ "* [[オークランド・アスレチックス]] (2011)\r\n"//
				+ "* [[タンパベイ・レイズ]] (2012)\r\n"//
				+ "| 選出国 = 日本\r\n"//
				+ "| 選出年 = {{by|2018年}}\r\n"//
				+ "| 得票率 = 91.3%（368票中336票）\r\n"//
				+ "| 選出方法 = 競技者表彰（プレーヤー部門）\r\n"//
				+ "}}\r\n"//
				+ "[[ファイル:Hideki Matsui of sain.jpg|thumb|250px|自筆サイン]]\r\n"//
				+ "'''松井 秀喜'''（まつい ひでき、[[1974年]][[6月12日]] - ）は、[[石川県]][[能美郡]][[根上町]]（現在の[[能美市]]）出身の元[[プロ野球選手]]（[[外野手]]）。右投左打。現役引退後は[[ニューヨーク・ヤンキース]][[ゼネラルマネージャー#スポーツにおけるゼネラルマネージャー|GM]]特別アドバイザーを務める。愛称は「'''[[ゴジラ (架空の怪獣)|GODZILLA]]'''」または「'''ゴジラ松井'''」。\r\n"//
				+ "\r\n"//
				+ "[[1990年代]]から[[2000年代]]の球界を代表する打者で、[[日本プロ野球]]（以下：NPB）では[[読売ジャイアンツ]]、[[メジャーリーグベースボール]]（以下：MLB）では[[ニューヨーク・ヤンキース]]などで活躍した。2009年のニューヨーク・ヤンキース時代に[[ワールドシリーズ]]優勝を経験している。同年、アジア人初の[[ワールドシリーズMVP]]を受賞した。2013年には[[国民栄誉賞]]を受賞した。\r\n"//
				+ "";//

		String plainText = MediaWikiTextUtils.toPlainText(title, wikiText);

		System.err.println("<plainText>");
		System.err.println(plainText);
		System.err.println("</plainText>");
	}

	public void testToPlainText004() throws IOException {
		// x-wiki 形式のテキストを用意する
		// Prepare x-wiki format text
		String wikititle = "テスト";
		String wikitext = "== TEST ==\nTHIS IS TEST.\nTHIS IS TEST";
		String plainText = MediaWikiTextUtils.toPlainText(wikititle, wikitext);

		System.err.println("<before>");
		System.err.println(wikitext);
		System.err.println("</before>");

		System.err.println("<after>");
		System.err.println(plainText);
		System.err.println("</after>");
	}

}
