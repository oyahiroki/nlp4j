package nlp4j.wiki;

import java.util.List;

import junit.framework.TestCase;
import nlp4j.wiki.util.MediaWikiTextUtils;

public class WikiPageNodeTestCase extends TestCase {

	private final String wikiText = "{{Wikipedia|学校}}\r\n" //
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

	public void test001() throws Exception {
		WikiPageNode pageNode = new WikiPageNode();
		pageNode.setHeader("=={{ja}}==");
	}

	public void testAddChild() {
	}

	public void testAddText() {
	}

	public void testGetChildren() {
	}

	public void testGetHeader() {
	}

	public void testGetLevel() {
	}

	public void testGetParent() {
	}

	public void testGetParentByLevel() {
	}

	public void testGetText001() {

		WikiItemTextParserInterface parser = new WikiItemTextParser();

		parser.parse(wikiText);

		WikiPageNode rootNode = parser.getRoot();
		{
			System.err.println(rootNode.getText());
			// {{Wikipedia|学校}}
			// {{ja-DEFAULTSORT|がっこう}}
			System.err.println(rootNode.getTextDescendant());
		}

	}

	public void testGetText002() {
		WikiItemTextParserInterface parser = new WikiItemTextParser();

		parser.parse(wikiText);

		WikiPageNode rootNode = parser.getRoot();
		{
			List<WikiPageNode> nodes = rootNode.get("=={{ja}}==");
			System.err.println(nodes.get(0).getText());
			// [[Category:{{ja}}]]
		}
	}

	public void testGetText003() {
		WikiItemTextParserInterface parser = new WikiItemTextParser();

		parser.parse(wikiText);

		WikiPageNode rootNode = parser.getRoot();
		{
			List<WikiPageNode> nodes = rootNode.get("=={{ja}}==/==={{noun}}===");
			System.err.println(nodes.get(0).getText());
			// [[Category:{{ja}}]]
		}
	}

	public void testGetText004() {
		WikiItemTextParserInterface parser = new WikiItemTextParser();

		parser.parse(wikiText);

		WikiPageNode rootNode = parser.getRoot();
		{
			List<WikiPageNode> nodes = rootNode.find("rel");
			String text0 = nodes.get(0).getText();
			System.err.println(text0);
			// [[Category:{{ja}}]]

			System.err.println(MediaWikiTextUtils.getWikiPageLinks(text0));
		}
	}

	public void testGetText005() {
		String wikiText = "{{ja-DEFAULTSORT|こうとうがっこう}}\r\n" //
				+ "=={{L|ja}}==\r\n" //
				+ "{{wikipedia}}\r\n" //
				+ "[[Category:{{ja}}]]\r\n" //
				+ "\r\n" //
				+ "==={{noun}}===\r\n" //
				+ "[[Category:{{ja}} {{noun}}]]\r\n" //
				+ "[[Category:{{ja}} 学校]]\r\n" //
				+ "{{jachar|高|等|学|校}}（こうとうがっこう）\r\n" //
				+ "# 現在の[[日本]]の[[学校教育法]]においては、後期[[中等教育]]を行う[[学校]]である。高度な普通教育及び専門教育を施すことを目的とする<ref>学校教育法第50条</ref>。修業年限は3年。ただし定時制や通信制など、4年以上の修業年限を持つ課程もある。「[[高校]]」と略されることが多い。\r\n" //
				+ "# かつての日本における、[[男子]]に高等普通教育をおこなうための3年制の学校。[[中学校]]（旧制）4年修了、ないしは同等の学力が認められた者が進学、現在の大学教養課程に相当する。[[w:旧制高等学校]]。\r\n" //
				+ "#*で[[大学予備門]]（今の'''高等学校'''）へ入るには[[変則]]の方だと英語を余計やって居たから容易に入れたけれど、[[正則]]の方では英語をやらなかったから卒業して後更に英語を勉強しなければ予備門へは入れなかったのである。（{{w|夏目漱石}} 『[http://www.aozora.gr.jp/cards/000148/files/2676_6502.html 落第]』）\r\n" //
				+ "\r\n" //
				+ "===={{pron|ja}}====\r\n" //
				+ ";こ↗ーとーが↘っこー\r\n" //
				+ "\r\n" //
				+ "===={{trans}}====\r\n" //
				+ "*{{en}}: [[high school]]\r\n" //
				+ "*{{th}}: [[ไฮสกูล]]\r\n" //
				+ "*{{ko}}: {{lang|ko|[[고등학교]]}}\r\n" //
				+ "*{{zh}}: {{zh-ts|[[高級中學]]|[[高级中学]]}}\r\n" //
				+ "*{{pt}}: [[escola secundária]]、[[ensino médio]]（ブラジル）\r\n" //
				+ "[[カテゴリ:日中で意味が大きく異なる漢語|こうとうかつこう]]\r\n" //
				+ "\r\n" //
				+ "----\r\n" //
				+ "\r\n" //
				+ "=={{L|zh}}==\r\n" //
				+ "{{wikipedia|lang=zh}}\r\n" //
				+ "[[Category:{{zh}}|gao1deng3xue2xiao4]]\r\n" //
				+ "\r\n" //
				+ "==={{noun}}===\r\n" //
				+ "[[Category:{{zh}}_{{noun}}|gao1deng3xue2xiao4]]\r\n" //
				+ "[[Category:{{zh}}_学校|gao1deng3xue2xiao4]]\r\n" //
				+ "{{zhchar|高|等|学|校}}（gāoděngxuéxiào　繁体字:{{zh-ts|'''[[高等學校]]'''}}）\r\n" //
				+ "#[[大学]]、[[専門学院]]及び[[専科学校]]等[[高等教育]]を実施する学校の総称。[[高校]]と略称する。\r\n" //
				+ "\r\n" //
				+ "----\r\n" //
				+ "\r\n" //
				+ "==脚注==\r\n" //
				+ "<references/>";
		WikiItemTextParserInterface parser = new WikiItemTextParser();
		parser.parse(wikiText);

		WikiPageNode rootNode = parser.getRoot();

		System.err.println(rootNode.get("=={{L|ja}}==/==={{noun}}==="));

	}

	public void testGetText006() {
		String wikiText = "{{半保護S}}\r\n" //
				+ "{{also|學校}}\r\n" //
				+ "=={{L|ja}}==\r\n" //
				+ "{{ja-DEFAULTSORT|がっこう}}\r\n" //
				+ "{{ja-kanjitab|がく|k1=がっ|こう|yomi=o}}\r\n" //
				+ "{{wikipedia}}\r\n" //
				+ "\r\n" //
				+ "==={{pron}}===\r\n" //
				+ "{{ja-pron|がっこう|acc=0|acc_ref=DJR4}}\r\n" //
				+ "\r\n" //
				+ "==={{noun}}===\r\n" //
				+ "{{ja-noun|がっこう|kyu=學校}}\r\n" //
				+ "[[Category:{{ja}} 学校|*]]\r\n" //
				+ "#何らかの[[教育]]が体系的に行われる[[組織]]又はその[[設備]]。[[学舎]]、[[まなびや]]。\r\n" //
				+ "#語義1のうち、教育する事項について[[履修]][[課程]]を定め、[[学生]]又は[[児童]]、[[生徒]]の[[修学]]状況を管理し、それを証する[[機関]]。ただし教育機関であっても学習塾や予備校などは除く。\r\n" //
				+ "#{{タグ|ja|法律}} 日本の学校教育法においては[[幼稚園]]、[[小学校]]、[[中学校]]、[[義務教育学校]]、[[高等学校]]、[[中等教育学校]]、[[特別支援学校]]、[[大学]][[及び]][[高等専門学校]]とされる（同法第一条）。 \r\n" //
				+ "#[[授業]]。\r\n" //
				+ "#*今日は'''学校'''がある日だ。\r\n" //
				+ "\r\n" //
				+ "===={{comp}}====\r\n" //
				+ "{{top}}\r\n" //
				+ "* [[学校安全警備員]]\r\n" //
				+ "* [[学校安全ボランティア]]\r\n" //
				+ "* [[学校医]]\r\n" //
				+ "{{bottom}}\r\n" //
				+ "\r\n" //
				+ "===={{rel}}====\r\n" //
				+ "* [[学校教育]]\r\n" //
				+ "* [[幼稚園]]\r\n" //
				+ "* [[小学校]]\r\n" //
				+ "\r\n" //
				+ "===={{desc}}====\r\n" //
				+ "* {{desc|pwn|gaku}}, {{l|pwn|gakku}}, {{l|pwn|gak-ku}}\r\n" //
				+ "* {{desc|tao|gako}}\r\n" //
				+ "\r\n" //
				+ "===={{trans}}====\r\n" //
				+ "{{trans-top}}\r\n" //
				+ "*{{T|ga}}: [[scoil]] {{f}}\r\n" //
				+ "*{{T|af}}: [[skool]]\r\n" //
				+ "*{{T|ar}}: {{ARchar|[[مدرسة]]}} (madrása) {{f}}\r\n" //
				+ "{{trans-bottom}}\r\n" //
				+ "\r\n" //
				+ "----\r\n" //
				+ "\r\n" //
				+ "=={{L|zh}}==\r\n" //
				+ "{{wikipedia|lang=zh}}\r\n" //
				+ "[[category:{{zh}}|xue2xiao4]]\r\n" //
				+ "==={{noun}}===\r\n" //
				+ "[[Category:{{zh}}_{{noun}}|xue2xiao4]]\r\n" //
				+ "[[Category:{{zh}}_学校|*]]\r\n" //
				+ "[[Category:学校|*]]\r\n" //
				+ "{{ZHsim|'''学校'''}} {{zh-ts|'''[[學校]]'''}} (xuéxiào)\r\n" //
				+ "#（日本語に同じ）学校。\r\n" //
				+ "[[Category:HSKレベル1級|xue2xiao4]]\r\n" //
				+ "[[Category:HSKレベル甲|xue2xiao4]]\r\n" //
				+ "\r\n" //
				+ "----\r\n" //
				+ "\r\n" //
				+ "==脚注==\r\n" //
				+ "<references />\r\n" //
				+ "";
		WikiItemTextParserInterface parser = new WikiItemTextParser();
		parser.parse(wikiText);

		parser.getWikiPageNodesAsList().stream().forEach(p -> {
			System.err.println(p.getLevel() + "," + p.getHeaderPath());
		});

	}

	public void testSetHeader() {
	}

	public void testSetLevel() {
	}

	public void testSetParent() {
	}

	public void testMatchHeader() {
		WikiPageNode pageNode = new WikiPageNode();
		pageNode.setHeader("=={{ja}}==");

		boolean b = pageNode.matchHeader(".*\\{\\{ja\\}\\}.*");
		System.err.println(b);
	}

	public void testContainsHeader() {
		WikiPageNode pageNode = new WikiPageNode();
		pageNode.setHeader("=={{ja}}==");

		boolean b = pageNode.containsHeader("{{ja}}");
		System.err.println(b);
	}

	public void testToString() {
	}

}
