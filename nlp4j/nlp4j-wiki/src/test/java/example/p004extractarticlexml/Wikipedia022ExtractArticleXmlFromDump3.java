package example.p004extractarticlexml;

import java.io.File;

import org.apache.commons.io.FileUtils;

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
public class Wikipedia022ExtractArticleXmlFromDump3 {

	public static void main(String[] args) throws Exception {

		String[] items = { "リンゴ", "バナナ", "大学", "同志社大学", "DRAGON BALL", "ドラゴンボール" };
		String dir = "/usr/local/data/wiki/jawiki/20221101";
		String lang = "ja";
		String media = "wiki";
		String version = "20221101";
		String outFile = "src/test/resources/nlp4j.wiki/jawiki-20221101-pages-articles-multistream-{id}-{title}.xml";
		File indexFile = MediaWikiFileUtils.getIndexFile(dir, lang, media, version);
		File dumpFile = MediaWikiFileUtils.getDumpFile(dir, lang, media, version);

		try (WikiDumpReader dumpReader = new WikiDumpReader(dumpFile, indexFile);) {
			for (String item : items) {

				System.err.println(indexFile);

				// WikiPedia のインデックスが読めるかどうかテスト

				WikiPage page = dumpReader.getItem(item);
				File outF = new File( //
						outFile.replace("{id}", page.getId()).replace("{title}", page.getTitle().replace(" ", "_")) //
				);
//					System.err.println("<text>");
//					System.err.println(page.getTimestamp());
//					System.err.println(page.getText()); // (1) Wiki 形式
//					System.err.println("</text>");
//					System.err.println(page.getXml() != null);
				System.err.println(page.getXml());
				if (outF.exists() == false) {
					FileUtils.write(outF, page.getXml(), "UTF-8", false);
				}
			}

		}

		// ### Expected Output ###

//	<page>
//	<title>大学</title>
//	    <ns>0</ns>
//	    <id>22814</id>
//	    <revision>
//	      <id>1698017</id>
//	      <parentid>1655348</parentid>
//	      <timestamp>2022-09-09T23:35:14Z</timestamp>
//	      <contributor>
//	        <username>ギャラクシーライナー</username>
//	        <id>92274</id>
//	      </contributor>
//	      <model>wikitext</model>
//	      <format>text/x-wiki</format>
//	      <text bytes="2019" xml:space="preserve">{{DEFAULTSORT:たいかく だいがく}}
//	=={{ja}}==
//	[[Category:{{ja}}]]
//	{{wikipedia}}
//	=== {{noun}} ===
//	[[Category:{{ja}} {{noun}}]]
//	[[Category:{{ja}} 学校]]
//	[[Category:{{ja}} 儒教]]
//	{{jachars}}（だいがく）
//	# [[学問]]の[[研究]]と[[教授]]にあたる[[高等教育]]機関。
//	# [[古代]][[日本]]の[[律令制]]における[[官吏]]養成機関、{{wikipedia-s|大学寮}}の略。
//	# [[中国]]における[[儒教]]の経典の一つ。著者・成立年未詳。
//
//	==== {{etym}} ====
//	{{w|ジュリオ・アレーニ}}による造語。日本でも広く読まれた『[[s:zh:職方外紀 (四庫全書本)|職方外紀]]』巻2に以下の記述がある。
//	: {{lang|zh-Hant|歐邏巴諸國皆尚文學，國王廣設學校。一國一郡有'''大學'''、中學，一邑一鄉有小學。}}
//	1872年の{{w|学制}}では日本全国に大学区、中学区、小学区を設け、それぞれ大学校、中学校、小学校を置くことを定めている。
//
//	==== {{trans}} ====
//	{{trans-top|高等教育機関}}
//	*{{it}}: {{t|it|università}}
//	*{{uk}}: {{t|uk|університет|tr=universytet|sc=Cyrl}}
//	*{{en}}: {{t|en|university}}, {{t|en|college}}, {{t|en|institute}}
//	*{{el}}: {{t|el|πανεπιστήμιο|tr=panepistímio|sc=Grek}}
//	*{{sv}}: {{t|sv|universitet|n}}
//	*{{es}}: {{t|es|universidad|f}}
//	*{{cs}}: {{t|cs|univerzita|f}}
//	*{{ko}}: {{t|ko|대학교|tr=daehakgyo}}
//	*{{de}}: {{t|de|Universität|f}}, {{t|de|Hochschule|f}}
//	*{{fr}}: {{t|fr|université|f}}
//	*{{pt}}: {{t|pl|universidade}}
//	*{{ro}}: {{t|ro|universitate}}
//	*{{ru}}: {{t|ru|университет|alt=университе́т|tr=universitét|sc=Cyrl|m}}
//	{{trans-bottom}}
//
//	-----
//
//	=={{zh}}==
//	[[Category:{{zh}}|da4xue2]] 
//	{{wikipedia|大學|lang=zh}}
//	==={{noun}}=== 
//	[[Category:{{zh}} {{noun}}|da4xue2]] 
//	[[Category:{{zh}} 学校|da4xue2]]
//	[[Category:{{zh}} 儒教|da4xue2]]
//	{{PAGENAME}}（繁体:[[大學]]　dàxué）
//	# （日本語に同じ）大学。
//	[[Category:HSKレベル甲|da4xue2]]
//	[[カテゴリ:日本語 大学]]</text>
//	      <sha1>nrvgnzmkmg39j4wbd80t7so3ivsgk6x</sha1>
//	    </revision>
//	  
//	</page>

	}

}
