package examples;

import nlp4j.wiki.BreakException;
import nlp4j.wiki.WikiDumpReader;
import nlp4j.wiki.WikiPage;
import nlp4j.wiki.WikiPageHandler;

public class ReadWikipediaDump2 {

	public static void main(String[] args) throws Exception {

		String dir = "/usr/local/wiki/enwiki/20230101/";

		String dumpFileName = dir + "enwiki-20230101-pages-articles-multistream.xml.bz2";

		// Create WikiPage handler
		WikiPageHandler wikiPageHander = new WikiPageHandler() {
			int count = 0;

			@Override
			public void read(WikiPage page) throws BreakException {
				System.err.println(page);
				System.err.println(page.getNamespace());
				if (page != null && page.getNamespace().equals("0")) {
					count++;
					System.err.println(page.getTitle());
					if (count > 3) { // IF YOU WANT TO BREAK
						throw new BreakException();
					}
				} else {
					// do nothing
				} // END_OF_IF
			} // END_OF_read()
		}; // Handler

		try (WikiDumpReader dumpReader = new WikiDumpReader(dumpFileName)) {
			try {
				dumpReader.read(wikiPageHander);
			} catch (BreakException be) {
				// OK
			}
		}

	}
}
