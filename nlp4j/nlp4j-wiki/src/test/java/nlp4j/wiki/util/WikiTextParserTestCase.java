package nlp4j.wiki.util;

import java.io.File;
import java.util.List;

import junit.framework.TestCase;
import nlp4j.wiki.WikiPage;
import nlp4j.wiki.WikiPageReader;
import nlp4j.wiki.entity.WikiEntity;
import nlp4j.wiki.entity.WikiDefault;

public class WikiTextParserTestCase extends TestCase {

	public void testParse001() throws Exception {
		String s = "{{AAA}}\n" //
				+ "{{AAA}}\n"//
				+ "TTT\n"//
				+ "TTT\n" //
				+ "{{AAA}}\n"//
		;
		WikiTextParser parser = new WikiTextParser();
		List<WikiEntity> ee = parser.parse(s);
		for (WikiEntity e : ee) {
			System.err.println("---");
//			System.err.println(e.getText());
			System.err.println(e);
		}

	}

	public void testParse002() throws Exception {
		String s = "{{Otheruses}}\r\n" //
				+ "{{複数の問題\r\n" //
				+ "| 出典の明記 = 2021年3月\r\n" //
				+ "| 更新 = 2021年3月\r\n" //
				+ "}}\r\n" //
				+ "[[ファイル:Менська районна гимназія; 13.08.19.jpg|200px|サムネイル|右]] \r\n" //
				+ "[[ファイル:Larkmead School, Abingdon, Oxfordshire.png|thumb|right|250px|[[イングランド]]の[[オックスフォードシャー州]]にある学校課（[[2007年]]）。]]\r\n" //
				+ "'''学校'''（がっこう）または'''スクール'''（英語: <span lang=\"en-us\" dir=\"ltr\">School</span>）は、[[幼児]]・[[児童]]・[[生徒]]・[[学生]]などに対する[[教育]][[制度]]の中核的な役割を果たす機関。また、その施設。[[学園]]、[[学院]]などもほぼ同様の意味を持つ。\r\n" //
				+ "\r\n" //
				+ "== 概説 ==\r\n" //
				+ "学校制度は[[社会システム]]の1つである教育制度の中心的システムの一つである<ref name=\"syakai11\">高橋靖直編『学校制度と社会 第二版』玉川大学出版局、2007年、11頁</ref>。社会的作用・社会的活動としての教育は、個人、家庭、小集団、地域社会、国家社会などにもみられるが、現代国家では学校が教育制度の中核的役割を担っている<ref name=\"syakai10\">高橋靖直編『学校制度と社会 第二版』玉川大学出版局、2007年、10頁</ref>。\r\n" //
				+ "\r\n" //
				+ "";
//		s = "{{XXX}}\n" //
//				+ "{{XXX}}\n"//
//				+ "YYY\n"//
//				+ "ZZZ\n" + "{{XXX}}\n"//
//		;
		WikiTextParser parser = new WikiTextParser();
		List<WikiEntity> ee = parser.parse(s);
		for (WikiEntity e : ee) {
			System.err.println("---");
//			System.err.println(e.getText());
			System.err.println(e);
		}
	}

	public void testParse003() throws Exception {
		File xmlFile = new File("src/test/resources/nlp4j.wiki/jawiki-20221101-pages-articles-multistream-255425.xml");
		WikiPageReader wpr = new WikiPageReader();
		WikiPage data = wpr.readWikiPageXmlFile(xmlFile);
		assertNotNull(data);
		WikiTextParser parser = new WikiTextParser();
		List<WikiEntity> ee = parser.parse(data.getText());
		for (WikiEntity e : ee) {
			System.err.println("---");
//			System.err.println(e.getText());
			System.err.println(e);
		}
	}

	public void testParse003b() throws Exception {
		File xmlFile = new File(
				"src/test/resources/nlp4j.wiki/" + "jawiki-20221101-pages-articles-multistream-255425.xml");
		WikiPage page = (new WikiPageReader()).readWikiPageXmlFile(xmlFile);
		assertNotNull(page);
		WikiTextParser parser = new WikiTextParser();

		List<WikiEntity> ee = parser.parse(page.getText());

		for (WikiEntity e : ee) {
			System.err.println(e.getName() + "," + e.toString());
		}
	}

	public void testParse003c() throws Exception {
		String wikiText = "{{テンプレート}}aaa{{テンプレート}}bbb{{テンプレート}}ccc{{テンプレート}}";
		WikiTextParser parser = new WikiTextParser();
		List<WikiEntity> ee = parser.parse(wikiText);
		for (WikiEntity e : ee) {
			System.err.println(e.getName() + "," + e.toString());
		}
		assertEquals(7, ee.size());
	}

	public void testParse003d() throws Exception {
		String wikiText = "{{テンプレート}}aaa\n[[カテゴリ]]\nbbb\n{{テンプレート}}\nccc\n[[カテゴリ]]\nddd";
		WikiTextParser parser = new WikiTextParser();
		List<WikiEntity> ee = parser.parse(wikiText);
		for (WikiEntity e : ee) {
			System.err.println(e.getName() + "," + e.toString());
		}
		System.err.println("???");
//		assertEquals(8, ee.size());
	}

	public void testParse004() throws Exception {

		File xmlFile = new File(
				"src/test/resources/nlp4j.wiki/" + "jawiki-20221101-pages-articles-multistream-25635.xml");

		WikiPageReader wpr = new WikiPageReader();

		WikiPage data = wpr.readWikiPageXmlFile(xmlFile);
		assertNotNull(data);

		WikiTextParser wikiTextParser = new WikiTextParser();

		List<WikiEntity> ee = wikiTextParser.parse(data.getText());
		for (WikiEntity e : ee) {
			System.err.println("---<entity>");
//			System.err.println(e.getText());
			if (e.isEmpty() || e.isTemplate()) {

				if (e.isTemplate()) {
					String t = MediaWikiTemplateUtils.toText(e);
					System.err.println(t);
				}

				continue;
			}
			{
				String wikiText = e.getText();
//				System.err.println(wikiText);
				if (wikiText.contains("Wt")) {
					System.err.println("debug");
				}
				wikiText = wikiText.replace("<WtXmlEndTag />", "");
				{
					if (wikiText.contains("<ref ")) {
						int idx1 = wikiText.indexOf("<ref ");
						int idx2 = wikiText.indexOf("/>");
						wikiText = wikiText.substring(0, idx1) + wikiText.substring(idx2 + 2);
					}
				}
				{
					if (wikiText.contains("</ref>")) {
						int idx1 = wikiText.indexOf("</ref>");
						wikiText = wikiText.substring(idx1 + 6);
					}
				}
				if (wikiText.contains("<!--")) {
					int idx = wikiText.indexOf("<!--");
					wikiText = wikiText.substring(0, idx);
				}
				if (wikiText.contains("-->")) {
					int idx = wikiText.indexOf("-->");
					wikiText = wikiText.substring(idx + 3);
				}

				System.err.println(wikiText);

//				String plainText = MediaWikiTextUtils.toPlainText(data.getTitle(), wikiText);
//				if (plainText.contains("Wt")) {
//					System.err.println("OK");
//				}
//				System.err.println(plainText);
			}
		}

	}

	public void testParse004b() throws Exception {
		File xmlFile = new File(
				"src/test/resources/nlp4j.wiki/" + "jawiki-20221101-pages-articles-multistream-25635b.xml");
		WikiPageReader wpr = new WikiPageReader();
		WikiPage data = wpr.readWikiPageXmlFile(xmlFile);
		assertNotNull(data);

		WikiTextParser parser = new WikiTextParser();
		List<WikiEntity> ee = parser.parse(data.getText());
		for (WikiEntity e : ee) {
			System.err.println("---");
//			if (e.isEmpty() || e.isTemplate()) {
//				continue;
//			}
//			System.err.println(e.getText());
			System.err.println(e.getText());
		}

	}

	public void testParse004c() throws Exception {
		File xmlFile = new File(
				"src/test/resources/nlp4j.wiki/" + "jawiki-20221101-pages-articles-multistream-30345.xml");
		WikiPageReader wpr = new WikiPageReader();
		WikiPage data = wpr.readWikiPageXmlFile(xmlFile);
		assertNotNull(data);

		WikiTextParser parser = new WikiTextParser();
		List<WikiEntity> ee = parser.parse(data.getText());
		for (WikiEntity e : ee) {
			System.err.println("---<entity>");
			// System.err.println(e.getText());
			if (e.isEmpty() || e.isTemplate()) {

				if (e.isTemplate()) {
					String t = MediaWikiTemplateUtils.toText(e);
					System.err.println(t);
				}

				continue;
			}
			{
				String wikiText = e.getText();
				// System.err.println(wikiText);
				if (wikiText.contains("Wt")) {
					System.err.println("debug");
				}
				wikiText = wikiText.replace("<WtXmlEndTag />", "");
				{
					if (wikiText.contains("<ref ")) {
						int idx1 = wikiText.indexOf("<ref ");
						int idx2 = wikiText.indexOf("/>");
						wikiText = wikiText.substring(0, idx1) + wikiText.substring(idx2 + 2);
					}
				}
				{
					if (wikiText.contains("</ref>")) {
						int idx1 = wikiText.indexOf("</ref>");
						wikiText = wikiText.substring(idx1 + 6);
					}
				}
				if (wikiText.contains("<!--")) {
					int idx = wikiText.indexOf("<!--");
					wikiText = wikiText.substring(0, idx);
				}
				if (wikiText.contains("-->")) {
					int idx = wikiText.indexOf("-->");
					wikiText = wikiText.substring(idx + 3);
				}

				System.err.println(wikiText);

				// String plainText = MediaWikiTextUtils.toPlainText(data.getTitle(), wikiText);
				// if (plainText.contains("Wt")) {
				// System.err.println("OK");
				// }
				// System.err.println(plainText);
			}
		}

	}

}
