package nlp4j.wiki.examples;

import java.io.File;

import nlp4j.wiki.BreakException;
import nlp4j.wiki.WikiDumpReader;
import nlp4j.wiki.WikiPage;
import nlp4j.wiki.WikiPageHandler;

public class WikiDumpReaderExample2_withoutIndex {

	public static void main(String[] args) throws Exception {

		// WIKI DUMP FILE
		File dumpFile = new File("/usr/local/wiki/enwiki/20230101/enwiki-20230101-pages-articles-multistream.xml.bz2");

		try (WikiDumpReader dumpReader = new WikiDumpReader(dumpFile)) {
			dumpReader.read(new WikiPageHandler() {
				int count = 0;

				@Override
				public void read(WikiPage page) throws BreakException {
					count++;
					if (count > 30) {
						throw new BreakException();
					}
					System.out.println(page.getTitle());
//					System.out.println(page.getText());
				}
			});
		} catch (BreakException be) {
		}
	}
}
