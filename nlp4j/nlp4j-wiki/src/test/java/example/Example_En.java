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
public class Example_En {

	public static void main(String[] args) throws Exception {
		// Language: en
		// Media: wiktionary
		// Version: 20220501 (May 1 2022)

		// Wiktionary Page Title
		String itemString = "sukiyaki";

		String dir = "/usr/local/data/wiki/enwiktionary/20221101";
		String lang = "en";
		String media = "wiktionary";
		String version = "20221101";

		// Index File
		File indexFile = MediaWikiFileUtils.getIndexFile(dir, lang, media, version);
		System.err.println(indexFile.getAbsolutePath());
		// Dump File
		File dumpFile = MediaWikiFileUtils.getDumpFile(dir, lang, media, version);
		System.err.println(dumpFile.getAbsolutePath());

		try (WikiDumpReader dumpReader = new WikiDumpReader(dumpFile, indexFile);) {
			{
				WikiPage page = dumpReader.getItem(itemString);
				System.err.println("---");
				System.err.println(page.getTimestamp());
				System.err.println("<text>");
				System.err.println(page.getText()); // <text>...</text>
				System.err.println("</text>");
				System.err.println("---");
				System.err.println("<xml>");
				System.err.println(page.getXml()); // <page>...</page>
				System.err.println("</xml>");
			}
		}

	}

}
