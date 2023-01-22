package nlp4j.wiki.util;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import junit.framework.TestCase;

public class MediaWikiTextUtilsTestCase extends TestCase {

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
	 * created_at: 2022-09-13
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
	 * created_at: 2022-09-13
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
	 * created_at: 2022-09-13
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

		String plainText = MediaWikiTextUtils.toPlainText2(wikiText);

		System.err.println("<plainText>");
		System.err.println(plainText);
		System.err.println("</plainText>");
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
	 * created_at : 2023-01-18
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

	public void testGetWikiPageLinks() {

	}

}
