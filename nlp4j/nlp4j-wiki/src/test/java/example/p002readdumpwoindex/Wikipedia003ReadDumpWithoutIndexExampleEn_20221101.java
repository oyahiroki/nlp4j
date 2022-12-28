package example.p002readdumpwoindex;

import java.io.File;

import nlp4j.wiki.BreakException;
import nlp4j.wiki.WikiDumpReader;
import nlp4j.wiki.WikiPage;
import nlp4j.wiki.WikiPageHandler;
import nlp4j.wiki.util.MediaWikiFileUtils;

public class Wikipedia003ReadDumpWithoutIndexExampleEn_20221101 {

	public static void main(String[] args) throws Exception {

		// WIKI DUMP FILE
		File dumpFile = MediaWikiFileUtils.getDumpFile( //
				"/usr/local/data/wiki/enwiki/20221101/", // File Path
				"en", // Language
				"wiki", // Type (wiki | wiktionary)
				"20221101"); // Version

		System.err.println("FILE: " + dumpFile.getAbsolutePath());
		System.err.println("FILE_SIZE: " + String.format("%,d", dumpFile.length()));

		// 自作のHandlerを指定する DEFINE Wiki Dump Handler
		WikiPageHandler wikiPageHander = new WikiPageHandler() {
			@Override
			public void read(WikiPage page) throws BreakException {
				System.err.println(page);
				if (page != null && page.getTitle().contains(":") == true) {
					// SKIP
					System.err.println("SKIP: " + page.getTitle());
					return;
				} //
				else {
					System.err.println("TITLE: " + page.getTitle());
					System.err.println(page.getText());
					{ // BREAK
						throw new BreakException();
					}
				}
			}
		};

		try (WikiDumpReader dumpReader = new WikiDumpReader(dumpFile)) {
			try {
				dumpReader.read(wikiPageHander);
			} catch (BreakException be) {
				System.err.println("OK");
			}
		}

	}
}
