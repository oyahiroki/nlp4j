package example202301.ja.wiktionary;

import java.io.File;

import nlp4j.wiki.BreakException;
import nlp4j.wiki.WikiDumpReader;
import nlp4j.wiki.WikiPage;
import nlp4j.wiki.WikiPageHandler;

public class Example201Read_WiktionaryDumpJa_Handler {

	public static void main(String[] args) throws Exception {

		// WIKI DUMP FILE
		String dumpFileName = "/usr/local/wiki/jawiktionary/20221101/jawiktionary-20221101-pages-articles-multistream.xml.bz2";
		File dumpFile = new File(dumpFileName);

		try (WikiDumpReader dumpReader = new WikiDumpReader(dumpFile)) {
			dumpReader.read(new WikiPageHandler() {
				int count = 0;

				@Override
				public void read(WikiPage page) throws BreakException {

					if (page.getTitle().contains(":")) {
						return; // skip
					}

					count++;

					if (count > 3) {
						throw new BreakException();
					}

					System.err.println("title: " + page.getTitle());
					System.err.println("isRedirect: " + page.isRediect());
					System.err.println("redirect_title: " + page.getRediect_title());

//					System.err.println(page.getText());
					System.err.println("---");
				}
			});
		} catch (BreakException be) {
			System.err.println("OK");
		}

	}
}
