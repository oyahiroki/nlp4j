package example202301.ja.wikipedia;

import java.io.File;

import nlp4j.wiki.WikiDumpReader;
import nlp4j.wiki.WikiPage;

public class Example103Read_WikipediaDumpJa5 {

	public static void main(String[] args) throws Exception {
		// Language: en
		// Media: wiktionary
		// Version: 20220501 (May 1 2022)

		// Wiktionary Page Title
		String itemString = "松井秀喜";

		// Index File
		String indexFileName = "/usr/local/wiki/jawiki/20221101/jawiki-20221101-pages-articles-multistream-index.txt.bz2";
		// Dump File
		String dumpFileName = "/usr/local/wiki/jawiki/20221101/jawiki-20221101-pages-articles-multistream.xml.bz2";
		// Index File
		File indexFile = new File(indexFileName);
		System.err.println(indexFile.getAbsolutePath());
		// Dump File
		File dumpFile = new File(dumpFileName);
		System.err.println(dumpFile.getAbsolutePath());

		try (WikiDumpReader dumpReader = new WikiDumpReader(dumpFile, indexFile);) {
			{
				WikiPage page = dumpReader.getItem(itemString);
				System.err.println(page.getRootNodePlainText());
			}
		}

	}

}
