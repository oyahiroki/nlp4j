package example;

import java.io.File;

import nlp4j.wiki.BreakException;
import nlp4j.wiki.WikiDumpReader;
import nlp4j.wiki.WikiPage;
import nlp4j.wiki.WikiPageHandler;
import nlp4j.wiki.util.MediaWikiFileUtils;

public class Wiktionary003ReadDumpExampleJa_20220401 {

	public static void main(String[] args) throws Exception {

		File dumpFile = MediaWikiFileUtils.getDumpFile( //
				"/usr/local/data/wiki/20220401", //
				"ja", //
				"wiktionary", //
				"20220401"); //

		System.err.println(dumpFile.getAbsolutePath());
		System.err.println(String.format("%,d", dumpFile.length()));

		WikiPageHandler wikiPageHander = new WikiPageHandler() {

			int count = 0;

			@Override
			public void read(WikiPage page) throws BreakException {
				System.err.println(page);
				if (page != null && page.getTitle().contains(":") == false) {
					count++;
					System.err.println(page.getText());
					if (count > 3) {
						throw new BreakException();
					}
				}
			}
		};

		try (WikiDumpReader dumpReader = new WikiDumpReader(dumpFile);) {

			try {
				dumpReader.read(wikiPageHander);
			} catch (BreakException be) {
				System.err.println("OK");
			}

		}

	}
}
