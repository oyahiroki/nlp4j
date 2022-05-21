package example;

import java.io.File;

import nlp4j.wiki.WikiDumpReader;
import nlp4j.wiki.WikiPage;
import nlp4j.wiki.util.MediaWikiFileUtils;

/**
 * Read WikiPedia Wiktionary data from Dump file
 * 
 * @author Hiroki Oya
 *
 */
public class Example {

	public static void main(String[] args) throws Exception {

		// Wiktionary Page Title
		String itemString = "中学校";

		String dir = "/usr/local/data/wiki";
		String lang = "ja";
		String media = "wiktionary";
		String version = "20210401";

		// Index File
		File indexFile = MediaWikiFileUtils.getIndexFile(dir, lang, media, version);
		// Dump File
		File dumpFile = MediaWikiFileUtils.getDumpFile(dir, lang, media, version);

		try (WikiDumpReader dumpReader = new WikiDumpReader(dumpFile, indexFile);) {
			{
				WikiPage page = dumpReader.getItem(itemString);
				System.err.println("<text>");
				System.err.println(page.getTimestamp());
				System.err.println(page.getText()); // (1) Wiki 形式
				System.err.println("</text>");
//				System.err.println(page.getXml());
			}
		}

	}

}
