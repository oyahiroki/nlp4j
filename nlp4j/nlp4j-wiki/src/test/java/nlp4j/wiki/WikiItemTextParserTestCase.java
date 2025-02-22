package nlp4j.wiki;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import junit.framework.TestCase;
import nlp4j.wiki.util.MediaWikiFileUtils;
import nlp4j.wiki.util.MediaWikiTextUtils;
import nlp4j.wiki.util.WikiPageNodeUtil;

public class WikiItemTextParserTestCase extends TestCase {

	private final String wiktinaryText_Gakkou = "{{Wikipedia|学校}}\r\n" //
			+ "{{ja-DEFAULTSORT|がっこう}}\r\n" //
			+ "=={{ja}}==\r\n" //
			+ "[[Category:{{ja}}]]\r\n" //
			+ "==={{noun}}===\r\n" //
			+ "[[Category:{{ja}}_{{noun}}]]\r\n" //
			+ "[[Category:{{ja}}_学校|*]]\r\n" //
			+ "'''[[学]][[校]]'''（がっこう　旧字体:[[學校]]）\r\n" //
			+ "#なんらかの[[教育]]が体系的に行われる[[組織]]又はその[[設備]]。[[学舎]]、[[まなびや]]。\r\n" //
			+ "#語義1のうち、教育する事項について[[履修]][[課程]]を定め、[[学生]]又は[[児童]]、[[生徒]]の[[修学]]状況を管理し、それを証する[[機関]]。ただし教育機関であっても学習塾や予備校などは除く。\r\n" //
			+ "#（法律）日本の学校教育法においては[[幼稚園]]、[[小学校]]、[[中学校]]、[[義務教育学校]]、[[高等学校]]、[[中等教育学校]]、[[特別支援学校]]、[[大学]][[及び]][[高等専門学校]]とされる（同法第一条）。 \r\n" //
			+ "#[[授業]]。\r\n" //
			+ "#*今日は学校がある日だ。\r\n" //
			+ "===={{rel}}====\r\n" //
			+ "* [[学校教育]]\r\n" //
			+ "* [[幼稚園]]\r\n" //
			+ "* [[小学校]]\r\n" //
			+ "* [[中学校]]\r\n" //
			+ "* [[高等学校]]\r\n" //
			+ "* [[専門学校]]\r\n" //
			+ "* [[大学]]／[[大学校]]\r\n" //
			+ "* [[学院]]、[[学園]]、[[学館]]\r\n" //
			+ "\r\n" //
			+ "\r\n" //
			+ "=={{zho}}==\r\n" //
			+ "[[category:{{zho}}|xue2xiao4]]\r\n" //
			+ "==={{noun}}===\r\n" //
			+ "[[Category:{{zho}}_{{noun}}|xue2xiao4]]\r\n" //
			+ "[[Category:{{zho}}_学校|*]]\r\n" //
			+ "[[Category:学校|*]]\r\n" //
			+ "{{ZHsim|'''学校'''}} {{zh-ts|'''[[學校]]'''}} (xuéxiào)\r\n" //
			+ "#（日本語に同じ）学校\r\n" //
			+ "[[Category:HSKレベル1級|xue2xiao4]]\r\n" //
			+ "[[Category:HSKレベル甲|xue2xiao4]]";

	private final String wiktinaryText_example = "{{Wikipedia|学校}}\r\n" //
			+ "=={{ja}}==\r\n" //
			+ "[[Category:{{ja}}]]\r\n" //
			+ "==={{noun}}===\r\n" //
			+ "[[Category:{{ja}}_{{noun}}]]\r\n" //
			+ "[[Category:{{ja}}_学校|*]]\r\n" //
			+ "'''[[学]][[校]]'''（がっこう　旧字体:[[學校]]）\r\n" //
			+ "#なんらかの[[教育]]が体系的に行われる[[組織]]又はその[[設備]]。[[学舎]]、[[まなびや]]。\r\n" //
			+ "===={{rel}}====\r\n" //
			+ "* [[学校教育]]\r\n" //
			+ "* [[幼稚園]]\r\n" //
			+ "* [[小学校]]\r\n" //
			+ "=={{zho}}==\r\n" //
			+ "[[category:{{zho}}|xue2xiao4]]\r\n" //
			+ "==={{noun}}===\r\n" //
			+ "[[Category:{{zho}}_{{noun}}|xue2xiao4]]\r\n" //
			+ "[[Category:HSKレベル甲|xue2xiao4]]";

	public void testGetRoot000() {

		WikiItemTextParserInterface parser = new WikiItemTextParser();

		parser.parse(wiktinaryText_example);

		WikiPageNode rootNode = parser.getRoot();
		{
			System.err.println("<root>");
			System.err.println(rootNode.getText());
			System.err.println("</root>");
			// {{Wikipedia|学校}}
			// {{ja-DEFAULTSORT|がっこう}}
			System.err.println("<descendant>");
			System.err.println(rootNode.getTextDescendant());
			System.err.println("</descendant>");
		}

		System.err.println("---");
		{
			List<WikiPageNode> nodes = rootNode.get("=={{ja}}==");
			System.err.println(nodes.get(0).getText());
			// [[Category:{{ja}}]]
		}
		System.err.println("---");
		{
			List<WikiPageNode> nodes = rootNode.get("=={{ja}}==/==={{noun}}===");
			System.err.println(nodes.get(0).getText());
			// [[Category:{{ja}}]]
		}
		System.err.println("---");

	}

	public void testGetRoot001() {

		WikiItemTextParserInterface parser = new WikiItemTextParser();

		parser.parse(wiktinaryText_Gakkou);

		WikiPageNode rootNode = parser.getRoot();
		{
			System.err.println("<text>");
			System.err.println(rootNode.getText());
			System.err.println("</text>");
			// {{Wikipedia|学校}}
			// {{ja-DEFAULTSORT|がっこう}}
			System.err.println(rootNode.getTextDescendant());
		}

		System.err.println("---");
		{
			List<WikiPageNode> nodes = rootNode.get("=={{ja}}==");
			System.err.println(nodes.get(0).getText());
			// [[Category:{{ja}}]]
		}
		System.err.println("---");
		{
			List<WikiPageNode> nodes = rootNode.get("=={{ja}}==/==={{noun}}===");
			System.err.println(nodes.get(0).getText());
			// [[Category:{{ja}}]]
		}
		System.err.println("---");

	}

	public void testGetRoot001b() {

		WikiItemTextParserInterface parser = new WikiItemTextParser();

		parser.parse(wiktinaryText_Gakkou);

		WikiPageNode rootNode = parser.getRoot();
		System.err.println("<header>");
		System.err.println(rootNode.getHeader());
		System.err.println("</header>");
		System.err.println("<text>");
		System.err.println(rootNode.getText());
		System.err.println("</text>");
		{
			for (WikiPageNode n : rootNode.getChildren()) {
				System.err.println("\t" + n.getHeader());
				System.err.println("\t" + n.getText());
				for (WikiPageNode nn : n.getChildren()) {
					System.err.println("\t\t" + nn.getHeader());
					System.err.println("\t\t" + nn.getText());
				}
			}
		}

	}

	public void testGetRoot002() throws IOException {

		String wikiText2 = FileUtils.readFileToString(new File("src/test/resources/nlp4j.wiki/wiki-example.txt"),
				"UTF-8");
		WikiItemTextParserInterface parser = new WikiItemTextParser();

		parser.parse(wikiText2);

		WikiPageNode rootNode = parser.getRoot();
		{
			System.err.println("<text>");
			System.err.println(rootNode.getText());
			System.err.println("</text>");
			// {{Wikipedia|学校}}
			// {{ja-DEFAULTSORT|がっこう}}
//			System.err.println(rootNode.getTextDescendant());
			System.err.println("<text2>");
			System.err.println(MediaWikiTextUtils.toPlainText("ヨーロッパ", rootNode.getText()));
			System.err.println("</text2>");
			System.err.println("<text2b>");
			System.err.println(MediaWikiTextUtils.toPlainText("ヨーロッパ", rootNode.getText()).replace("**", ""));
			System.err.println("</text2b>");
		}

//		System.err.println("---");
//		{
//			List<WikiPageNode> nodes = rootNode.get("=={{ja}}==");
//			System.err.println(nodes.get(0).getText());
//			// [[Category:{{ja}}]]
//		}
//		System.err.println("---");
//		{
//			List<WikiPageNode> nodes = rootNode.get("=={{ja}}==/==={{noun}}===");
//			System.err.println(nodes.get(0).getText());
//			// [[Category:{{ja}}]]
//		}
//		System.err.println("---");

	}

	public void testGetRoot101() throws Exception {
		String itemString = "歩く";

		String dir = "files/wiki/jawiktionary/20220501/";
		String fileIndexName = "jawiktionary-20220501-pages-articles-multistream-index.txt.bz2";
		String fileDumpName = "jawiktionary-20220501-pages-articles-multistream.xml.bz2";

		File indexFile = new File(dir + fileIndexName);
		File dumpFile = new File(dir + fileDumpName);

		System.err.println(indexFile.getAbsolutePath());
		System.err.println(dumpFile.getAbsolutePath());

		// WikiPedia のインデックスが読めるかどうかテスト

		try (WikiDumpReader dumpReader = new WikiDumpReader(dumpFile, indexFile);) {
			WikiPage page = dumpReader.getItem(itemString);
			WikiItemTextParserInterface parser = new WikiItemTextParser();
			WikiPageNode rootNode = parser.parse(page.getText());
			System.err.println(WikiPageNodeUtil.toString(rootNode));
		}
	}

	public void testGetRoot102() throws Exception {
		String itemString = "あるく";

		String dir = "files/wiki/jawiktionary/20220501/";
		String fileIndexName = "jawiktionary-20220501-pages-articles-multistream-index.txt.bz2";
		String fileDumpName = "jawiktionary-20220501-pages-articles-multistream.xml.bz2";

		File indexFile = new File(dir + fileIndexName);
		File dumpFile = new File(dir + fileDumpName);

		System.err.println(indexFile.getAbsolutePath());
		System.err.println(dumpFile.getAbsolutePath());

		// WikiPedia のインデックスが読めるかどうかテスト

		try (WikiDumpReader dumpReader = new WikiDumpReader(dumpFile, indexFile);) {
			WikiPage page = dumpReader.getItem(itemString);
			WikiItemTextParserInterface parser = new WikiItemTextParser();
			WikiPageNode rootNode = parser.parse(page.getText());
			System.err.println(WikiPageNodeUtil.toString(rootNode));
		}
	}

	public void testToWikiPageNodeTree() {
		WikiItemTextParserInterface parser = new WikiItemTextParser();

		parser.parse(wiktinaryText_Gakkou);

		System.err.println(parser.toWikiPageNodeTree());

		// &nbsp;&nbsp;=={{ja}}==
		// &nbsp;&nbsp;&nbsp;&nbsp;==={{noun}}===
		// &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;===={{rel}}====
		// &nbsp;&nbsp;=={{zho}}==
		// &nbsp;&nbsp;&nbsp;&nbsp;==={{noun}}===

	}

}
