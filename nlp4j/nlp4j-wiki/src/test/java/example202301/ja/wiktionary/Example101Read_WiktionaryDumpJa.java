package example202301.ja.wiktionary;

import java.io.File;

import nlp4j.wiki.WikiDumpReader;
import nlp4j.wiki.WikiPage;

public class Example101Read_WiktionaryDumpJa {

	public static void main(String[] args) throws Exception {
		// Language: en
		// Media: wiktionary
		// Version: 20220501 (May 1 2022)

		// Wiktionary Page Title
		String itemString = "学校";

		// Index File
		String indexFileName = "/usr/local/wiki/jawiktionary/20221101/jawiktionary-20221101-pages-articles-multistream-index.txt.bz2";
		// Dump File
		String dumpFileName = "/usr/local/wiki/jawiktionary/20221101/jawiktionary-20221101-pages-articles-multistream.xml.bz2";
		// Index File
		File indexFile = new File(indexFileName);
		System.err.println(indexFile.getAbsolutePath());
		// Dump File
		File dumpFile = new File(dumpFileName);
		System.err.println(dumpFile.getAbsolutePath());

		try (WikiDumpReader dumpReader = new WikiDumpReader(dumpFile, indexFile);) {
			{
				WikiPage page = dumpReader.getItem(itemString);
				if (page == null) {
					System.err.println("Not found: " + itemString);
				} //
				else {
					System.err.println("---");
					System.err.println("title: " + page.getTitle());
					System.err.println("---");
					System.err.println(page.getTimestamp());
					System.err.println("---");
					System.err.println("redirect: " + page.isRediect());
					if (page.isRediect()) {
						System.err.println("This page is redirected to: " + page.getRediect_title());
					}
					//
					else {
						System.err.println("---");
						System.err.println("categories: " + page.getCategoryTags());
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

	}

}
