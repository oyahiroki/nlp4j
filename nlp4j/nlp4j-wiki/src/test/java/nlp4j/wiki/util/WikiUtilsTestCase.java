package nlp4j.wiki.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.BufferUnderflowException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.io.FileUtils;

import junit.framework.TestCase;
import nlp4j.Keyword;
import nlp4j.wiki.util.MediaWikiTextUtils;

public class WikiUtilsTestCase extends TestCase {

	public void testExtractSpells() {
//		fail("Not yet implemented");
	}

	public void testNormailzeHeader() {
//		fail("Not yet implemented");
	}

	public void testGetTemplateAsTag001() throws Exception {
		String wikitext = "{{stub|kanji}}\r\n" //
				+ "==[[漢字]]==\r\n" //
				+ "<span lang=\"ja\" xml:lang=\"ja\" style=\"font-size:350%\">{{PAGENAME}}</span>\r\n" //
				+ "* {{部首|亠|11}}\r\n" //
				+ "* {{総画|13}}\r\n" //
				+ "* [[異体字]] : <span style=\"font-size:250%\">[[𮧊]]</span>（[[俗字]]）\r\n" //
				+ "* 筆順 : [[image:{{PAGENAME}}-bw.png|350px]]\r\n" //
				+ "===字源===\r\n" //
				+ "* <!-- [[象形文字|象形]]』、『[[指事文字|指事]]』、『[[会意文字|会意]]』、『[[形声文字|形声]]』、『[[会意形声文字|会意形声]]』。-->\r\n" //
				+ "{{字源}}\r\n" //
				+ "===意義===\r\n" //
				+ "#\r\n" //
				+ "[[Category:漢字|せん]]\r\n" //
				+ "\r\n" //
				+ "=={{ja}}==\r\n" //
				+ "[[Category:{{ja}}|せん]]\r\n" //
				+ "===={{pron|ja}}====\r\n" //
				+ "* 音読み\r\n" //
				+ "** [[呉音]] : [[ゼン]]、[[タン]]\r\n" //
				+ "** [[漢音]] : [[セン]]、[[タン]]\r\n" //
				+ "* 訓読み\r\n" //
				+ "*: [[あつい|あつ-い]]、[[ほしいまま]]\r\n" //
				+ "\r\n" //
				+ "===={{prov}}====\r\n" //
				+ "\r\n" //
				+ "=={{zh}}==\r\n" //
				+ "[[Category:{{zh}}|dan3]]\r\n" //
				+ "{{trans_link|zh|{{PAGENAME}}}}\r\n" //
				+ "* '''ローマ字表記'''\r\n" //
				+ "** '''[[普通話]]'''\r\n" //
				+ "*** '''[[ピンイン]]''': [[dǎn]] (dan3)\r\n" //
				+ "*** '''[[ウェード式]]''': tan<sup>3</sup>\r\n" //
				+ "** '''[[広東語]]'''\r\n" //
				+ "*** '''[[イェール式]]''': taan2\r\n" //
				+ "===={{prov}}==== \r\n" //
				+ "\r\n" //
				+ "=={{ko}}== \r\n" //
				+ "[[Category:{{ko}}|ㄷㅏㄴ]] \r\n" //
				+ "{{ko-hanja|hangeul=[[단]]|rv=dan|mr=tan|eumhun=믿을 단}}\r\n" //
				+ "\r\n" //
				+ "===={{prov}}==== \r\n" //
				+ "\r\n" //
				+ "==コード等==\r\n" //
				+ "* [[Unicode]]\r\n" //
				+ "** 16進：4EB6\r\n" //
				+ "** 10進：20150\r\n" //
				+ "* JIS X 0208(-1978,1983,1990)\r\n" //
				+ "** [[JIS]]\r\n" //
				+ "*** 16進：5039\r\n" //
				+ "** [[Shift JIS]]\r\n" //
				+ "*** 16進：98B7\r\n" //
				+ "** [[区点]]：1面48区25点\r\n" //
				+ "<!--\r\n" //
				+ "* [[EUC]]\r\n" //
				+ "** JP 16進：D0B9\r\n" //
				+ "** CN 16進：818D\r\n" //
				+ "** KR 16進：D3A2\r\n" //
				+ "* [[Big5]]\r\n" //
				+ "** 16進：DCB3\r\n" //
				+ "** 10進：56499\r\n" //
				+ "* [[CNS]]\r\n" //
				+ "** 16進：C158\r\n" //
				+ "* [[GB18030]]\r\n" //
				+ "** 16進：818D\r\n" //
				+ "-->\r\n" //
				+ "* [[四角号碼]] : 0010<sub>6</sub>\r\n" //
				+ "* [[倉頡入力法]] : 卜田口一 (YWRM)\r\n" //
				+ "\r\n" //
				+ "{{追加カテゴリ}}\r\n" //
				+ "";
		WikiUtils.getTags(wikitext);
	}

	public void testToHtml001() throws Exception {
		String wikitext = "#なんらかの[[教育]]が体系的に行われる[[組織]]又はその[[設備]]。[[学舎]]、[[まなびや]]。";
		String html = WikiUtils.toHtml(wikitext);
		System.err.println(html);
	}

	/**
	 * created at: 2022-07-18
	 */
	public void testToHtml002() throws Exception {
		// https://ja.wiktionary.org/wiki/%E4%BA%B6
		String wikitext = "=={{ja}}==\r\n" //
				+ "[[Category:{{ja}}|せん]]\r\n" //
				+ "===={{pron|ja}}====\r\n" //
				+ "* 音読み\r\n" //
				+ "** [[呉音]] : [[ゼン]]、[[タン]]\r\n" //
				+ "** [[漢音]] : [[セン]]、[[タン]]\r\n" //
				+ "* 訓読み\r\n" //
				+ "*: [[あつい|あつ-い]]、[[ほしいまま]]"; //
		String html = WikiUtils.toHtml(wikitext);
		System.err.println(html);

		List<Keyword> kwds = WikiUtils.extractKeywordsFromWikiHtml(html, "wiki.link");
		System.err.println(kwds);
	}

	/**
	 * created at: 2022-09-13
	 */
	public void testToHtml003() throws Exception {
		File file = new File("src/test/resources/nlp4j.wiki/wiki-example-ichiro.txt");
		String charsetName = "UTF-8";
		String wikitext = FileUtils.readFileToString(file, charsetName);
		String rootNodeText = MediaWikiTextUtils.getRootNodeText(wikitext);

		String html = WikiUtils.toHtml(rootNodeText);
		System.err.println(html);

		List<Keyword> kwds = WikiUtils.extractKeywordsFromWikiHtml(html, "wiki.link");
		System.err.println(kwds);
		System.err.println("<wiki.link>");
		kwds.forEach(kw -> {
			System.err.println(kw.getLex());
		});
		System.err.println("</wiki.link>");
	}

	public void testToPlainText001() {
		String wikitext = "#なんらかの[[教育]]が体系的に行われる[[組織]]又はその[[設備]]。[[学舎]]、[[まなびや]]。";
		String plaintext = WikiUtils.toPlainText(wikitext);
		System.err.println(wikitext);
		System.err.println(plaintext);

//		<ol>
//		<li>なんらかの<a href="" title="教育">教育</a>が体系的に行われる<a href="" title="組織">組織</a>又はその<a href="" title="設備">設備</a>。<a href="" title="学舎">学舎</a>、<a href="" title="まなびや">まなびや</a>。</li>
//		</ol>

	}

	public void testToPlainText002() {
		String wikitext = "# {{lb|en|US|Canada}} An [[institution]] dedicated to [[teaching]] and [[learning]]; an [[educational]] institution.";
		String plaintext = WikiUtils.toPlainText(wikitext);
		System.err.println(wikitext);
		System.err.println(plaintext);
	}

	public void testToPlainText003() {
		String wikitext = "'''アンパサンド'''（'''&''', ）は、並立助詞「…と…」を意味する[[記号]]である。[[ラテン語]]で「…と…」を表す接続詞 \"et\" の[[合字]]を起源とする。現代のフォントでも、[[Trebuchet MS]] など一部のフォントでは、\"et\" の合字であることが容易にわかる字形を使用している。";
		String plaintext = WikiUtils.toPlainText(wikitext);
		System.err.println(wikitext);
		System.err.println(plaintext);
	}

	public void testNormailzeHeader001() {
		String s1 = "=={{L|ja}}==";
		String s2 = WikiUtils.normailzeHeader(s1);
		String expected = "=={{L|ja}}==";
		System.err.println(s2);
		assertEquals(expected, s2);
	}

	public void testNormailzeHeaderPath001() {
		{
			String s1 = "=={{L|ja}}==";
			String s2 = WikiUtils.normailzeHeaderPath(s1);
			String expected = "=={{ja}}==";
			System.err.println(s2);
			assertEquals(expected, s2);
		}
		{
			String s1 = "==日本語==";
			String s2 = WikiUtils.normailzeHeaderPath(s1);
			String expected = "=={{ja}}==";
			System.err.println(s2);
			assertEquals(expected, s2);
		}
	}

	// # {{lb|en|US|Canada}} An [[institution]] dedicated to [[teaching]] and
	// [[learning]]; an [[educational]] institution.

	public void testExtractKeywordsFromWikiHtml() {
		String html = //
				"<html><body>"//
						+ "<ol>"//
						+ "<li><a href='gakkou.html' title='学校'>学校xx</a></li>"//
						+ "<li><a href='gakkou.html' title='病院'>病院xx</a></li>"//
						+ "</ol>"//
						+ "</body></html>";
		List<Keyword> kwds = WikiUtils.extractKeywordsFromWikiHtml(html, "wiki.link");

		for (Keyword kwd : kwds) {
			System.err.println(kwd.getLex());
		}
	}

	public void testExtractKeywordsFromWikiText001() {
		String text = //
				"{{ja-wagokanji|めだまやき}}\r\n" //
						+ "# {{wagokanji of|めだまやき}}";
		List<Keyword> kwds = WikiUtils.extractKeywordsFromWikiText(text, "wiki.link");

		for (Keyword kwd : kwds) {
			System.err.println(kwd.getLex());
		}
	}

	public void testExtractKeywordsFromWikiText002() {
		String text = //
				"{{ja-DEFAULTSORT}}\r\n" //
						+ "=={{L|ja}}==\r\n"//
						+ "\r\n"//
						+ "==={{noun}}===\r\n"//
						+ "{{ja-noun}}\r\n"//
						+ "#{{alternative form of|ja|バッファー}}\r\n"//
						+ "";
		List<Keyword> kwds = WikiUtils.extractKeywordsFromWikiText(text, "wiki.link");

		for (Keyword kwd : kwds) {
			System.err.println(kwd.getLex());
		}
	}

	public void testRead() throws IOException {
		InputStream is = this.getClass().getClassLoader().getResourceAsStream("nlp4j/wiki/util/WikiUtilsConfig.txt");
		InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
		BufferedReader br = new BufferedReader(isr);
		String s;
		while ((s = br.readLine()) != null) {
			System.err.println(s);
		}
		br.close();
	}

}
