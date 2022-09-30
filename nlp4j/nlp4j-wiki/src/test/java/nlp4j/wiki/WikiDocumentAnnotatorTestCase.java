package nlp4j.wiki;

import junit.framework.TestCase;
import nlp4j.Document;
import nlp4j.impl.DefaultDocument;
import nlp4j.util.DocumentUtil;

public class WikiDocumentAnnotatorTestCase extends TestCase {

	public void testAnnotateDocument001() throws Exception {

		Document doc = new DefaultDocument();

		String wikitext = "{{半保護S}}\r\n" //
				+ "=={{ja}}==\r\n" //
				+ "{{ja-DEFAULTSORT|がっこう}}\r\n" //
				+ "{{wikipedia}}\r\n" //
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
				+ "";

		doc.putAttribute("wikitext", wikitext);

		WikiDocumentAnnotator ann1 = new WikiDocumentAnnotator();
		{
			String paths = //
					"=={{ja}}==/==={{noun}}===" //
							+ ",=={{ja}}==/===固有名詞===" //
							+ ",=={{ja}}==/===慣用句===" //
							+ ",=={{ja}}==/==={{adj}}===" //
							+ ",=={{ja}}==/===和語の漢字表記===" //
							+ ",=={{ja}}==/==={{adverb}}===" //
							+ ",=={{ja}}==/==={{adjectivenoun}}===" //
							+ "";
			ann1.setProperty("paths", paths);
		}

		ann1.annotate(doc);

		System.err.println(doc.getAttributeAsString("text"));

	}

	public void testAnnotateDocument002() throws Exception {

		Document doc = new DefaultDocument();

		String wikitext = "{{DEFAULTSORT:かくねん}}\r\n" //
				+ "=={{ja}}==\r\n" //
				+ "[[Category:{{ja}}]]\r\n" //
				+ "==={{noun}}===\r\n" //
				+ "[[Category:{{ja}} {{noun}}]]\r\n" //
				+ "'''[[隔]] [[年]]''' (かくねん)\r\n" //
				+ "#[[一年]]おき。\r\n" //
				+ "\r\n" //
				+ "===={{rel}}====\r\n" //
				+ "* {{syn}}: [[隔歳]]\r\n" //
				+ "\r\n" //
				+ "[[zh:隔年]]\r\n" //
				+ "[[zh-min-nan:隔年]]";

		doc.putAttribute("wikitext", wikitext);

		WikiDocumentAnnotator ann1 = new WikiDocumentAnnotator();
		{
			String paths = //
					"=={{ja}}==/==={{noun}}===" //
							+ ",=={{ja}}==/===固有名詞===" //
							+ ",=={{ja}}==/===慣用句===" //
							+ ",=={{ja}}==/==={{adj}}===" //
							+ ",=={{ja}}==/===和語の漢字表記===" //
							+ ",=={{ja}}==/==={{adverb}}===" //
							+ ",=={{ja}}==/==={{adjectivenoun}}===" //
							+ "";
			ann1.setProperty("paths", paths);
		}

		ann1.annotate(doc);

		System.err.println(doc.getAttributeAsString("text"));

		System.err.println(DocumentUtil.toJsonPrettyString(doc));

	}

}
