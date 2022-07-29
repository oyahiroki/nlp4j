package example;

import java.io.File;

import nlp4j.wiki.WikiDumpReader;
import nlp4j.wiki.WikiPage;
import nlp4j.wiki.util.MediaWikiFileUtils;

/**
 * <pre>
 * Read Wiktionary Page by Page Title
 * ページタイトルを指定してダンプページを読み込む
 * </pre>
 * 
 * @author Hiroki Oya
 *
 */
public class Wikipedia020ReadWiktionaryPageXml1Xml {

	public static void main(String[] args) throws Exception {

		String itemString = "大学";

		String dir = "/usr/local/data/wiki/20220401";
		String lang = "ja";
		String media = "wiktionary";
		String version = "20220401";

		File indexFile = MediaWikiFileUtils.getIndexFile(dir, lang, media, version);
		File dumpFile = MediaWikiFileUtils.getDumpFile(dir, lang, media, version);

		System.err.println(indexFile);

		// WikiPedia のインデックスが読めるかどうかテスト

		try (WikiDumpReader dumpReader = new WikiDumpReader(dumpFile, indexFile);) {
			WikiPage page = dumpReader.getItem(itemString);
//				System.err.println("<text>");
//				System.err.println(page.getTimestamp());
//				System.err.println(page.getText()); // (1) Wiki 形式
//				System.err.println("</text>");
//				System.err.println(page.getXml() != null);
			System.err.println(page.getXml());
		}

		// ### Expected Output ###

		// /usr/local/data/wiki/20220401/jawiktionary-20220401-pages-articles-multistream-index.txt.bz2
		// 2022/06/11 23:14:41.815 [main] INFO nlp4j.wiki.WikiIndexReader :55 Reading:
		// C:/usr/local/data/wiki/20220401/jawiktionary-20220401-pages-articles-multistream-index.txt.bz2
		// 2022/06/11 23:14:41.819 [main] INFO nlp4j.wiki.WikiIndexReader :56 Reading
		// File size (bytes): 2,132,452
		// 2022/06/11 23:14:42.502 [main] INFO nlp4j.wiki.WikiIndexReader :98 Read done
		// (ms): 677
		// 2022/06/11 23:14:42.502 [main] INFO nlp4j.wiki.WikiIndexReader :99 WikiIndex
		// item size (bytes): 325243
		// <page>
		// <title>大学</title>
//		     <ns>0</ns>
//		     <id>22814</id>
//		     <revision>
//		       <id>1655348</id>
//		       <parentid>1327232</parentid>
//		       <timestamp>2022-03-27T14:00:55Z</timestamp>
//		       <contributor>
//		         <username>MathXplore</username>
//		         <id>54530</id>
//		       </contributor>
//		       <minor />
//		       <comment>カテゴリ間複製：[[Category:日本語 学校]] から [[Category:日本語 大学]] using [[c:Help:Cat-a-lot|Cat-a-lot]]</comment>
//		       <model>wikitext</model>
//		       <format>text/x-wiki</format>
//		       <text bytes="2016" xml:space="preserve">{{DEFAULTSORT:たいかく だいがく}}
		// =={{ja}}==
		// [[Category:{{ja}}]]
		// {{wikipedia}}
		// ==={{noun}}===
		// [[Category:{{ja}} {{noun}}]]
		// [[Category:{{ja}} 学校]]
		// [[Category:{{ja}} 儒教]]
		// {{jachars}}（だいがく）
		// # [[学問]]の[[研究]]と[[教授]]にあたる[[高等教育]]機関。
		// # [[古代]][[日本]]の[[律令制]]における[[官吏]]養成機関、{{wikipedia-s|大学寮}}の略。
		// # [[中国]]における[[儒教]]の経典のひとつ。著者・成立年未詳。
		//
		// ===={{etym}}====
		// {{w|ジュリオ・アレーニ}}による造語。日本でも広く読まれた『[[s:zh:職方外紀 (四庫全書本)|職方外紀]]』巻2に以下の記述がある。
		// : {{lang|zh-Hant|歐邏巴諸國皆尚文學，國王廣設學校。一國一郡有'''大學'''、中學，一邑一鄉有小學。}}
		// 1872年の{{w|学制}}では日本全国に大学区、中学区、小学区を設け、それぞれ大学校、中学校、小学校を置くことを定めている。
		//
		// ===={{trans}}====
		// {{trans-top|高等教育機関}}
		// *{{it}}: {{t|it|università}}
		// *{{uk}}: {{t|uk|університет|tr=universytet|sc=Cyrl}}
		// *{{en}}: {{t|en|university}}, {{t|en|college}}, {{t|en|institute}}
		// *{{el}}: {{t|el|πανεπιστήμιο|tr=panepistímio|sc=Grek}}
		// *{{sv}}: {{t|sv|universitet|n}}
		// *{{es}}: {{t|es|universidad|f}}
		// *{{cs}}: {{t|cs|univerzita|f}}
		// *{{ko}}: {{t|ko|대학교|tr=daehakgyo}}
		// *{{de}}: {{t|de|Universität|f}}, {{t|de|Hochschule|f}}
		// *{{fr}}: {{t|fr|université|f}}
		// *{{pt}}: {{t|pl|universidade}}
		// *{{ro}}: {{t|ro|universitate}}
		// *{{ru}}: {{t|ru|университет|alt=университе́т|tr=universitét|sc=Cyrl|m}}
		// {{trans-bottom}}
		//
		// -----
		//
		// =={{zh}}==
		// [[Category:{{zh}}|da4xue2]]
		// {{wikipedia|大學|lang=zh}}
		// ==={{noun}}===
		// [[Category:{{zh}} {{noun}}|da4xue2]]
		// [[Category:{{zh}} 学校|da4xue2]]
		// [[Category:{{zh}} 儒教|da4xue2]]
		// {{PAGENAME}}（繁体:[[大學]] dàxué）
		// # （日本語に同じ）大学。
		// [[Category:HSKレベル甲|da4xue2]]
		// [[カテゴリ:日本語 大学]]</text>
//		       <sha1>1p1tglepdok4wtl355p31hilk84pxgr</sha1>
//		     </revision>
		//
		// </page>
		// 2022/06/11 23:14:42.572 [main] INFO nlp4j.wiki.WikiDumpReader :90 File
		// closed.

	}

}
