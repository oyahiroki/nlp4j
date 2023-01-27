package example202301.ja.wiktionary;

import java.io.File;

import example202301.PagePrinter;
import nlp4j.wiki.WikiDumpReader;
import nlp4j.wiki.WikiPage;

public class Example101Read_WiktionaryDumpJa3 {

	public static void main(String[] args) throws Exception {
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
			WikiPage page = dumpReader.getItem(itemString);
			PagePrinter.print(page);
		}

	}

}
