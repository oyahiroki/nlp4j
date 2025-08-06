package examples;

import java.io.File;

import nlp4j.wiki.WikiDumpReader;
import nlp4j.wiki.WikiPage;

/**
 * <pre>
 * Read ( WikiPedia | Wiktionary ) data from Dump file
 * Notice: It takes approximately 60 seconds to load a file.
 * </pre>
 * 
 * @author Hiroki Oya
 *
 */
public class ReadWikipediaDump1Ja_ByItem {

	public static void main(String[] args) throws Exception {

		String wiki_dir = "/usr/local/wiki/jawiki/20230101/";

		String wiki_dumpIndexFileName = wiki_dir + "jawiki-20230101-pages-articles-multistream-index.txt.bz2";
		String wiki_dumpFileName = wiki_dir + "jawiki-20230101-pages-articles-multistream.xml.bz2";

		String itemString = "任天堂";

		try (WikiDumpReader dumpReader = new WikiDumpReader( //
				new File(wiki_dumpFileName), //
				new File(wiki_dumpIndexFileName)); //
		) {

			WikiPage page = dumpReader.getItem(itemString);

			System.out.println("<root_node_text>");
			System.out.println(page.getRootNodePlainText());
			System.out.println("</root_node_text>");
			/*
			 * <root_node_text> is a Japanese multinational video game company headquartered
			 * in Kyoto, Japan. It develops video games and video game consoles ...
			 * </root_node_text>
			 * 
			 */
			System.out.println("<text>\n" + page.getText() + "\n</text>");
			/*
			 * <text> {{Short description|Japanese video game company}} <!-- popup
			 * [[File:Nintendo.svg]] --> {{Pp-vandalism|small=yes}} {{Use dmy
			 * dates|date=October 2022}} {{Use American English|date=November 2020}}
			 * {{Infobox company | name = Nintendo Co., Ltd. | logo = Nintendo.svg |
			 * logo_alt = Logo in white on red background since 2016 | logo_caption = Logo
			 * in white on red background since 2016 | image =
			 * Nintendo_Headquarters_-_panoramio.jpg ... </text>
			 * 
			 */

//			System.out.println("<xml>\n" + page.getXml() + "\n</xml>");
		}

	}

}
