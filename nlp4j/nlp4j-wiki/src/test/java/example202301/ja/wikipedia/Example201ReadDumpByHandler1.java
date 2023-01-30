package example202301.ja.wikipedia;

import java.io.File;

import nlp4j.wiki.BreakException;
import nlp4j.wiki.WikiDumpReader;
import nlp4j.wiki.WikiPage;
import nlp4j.wiki.WikiPageHandler;

public class Example201ReadDumpByHandler1 {

	public static void main(String[] args) throws Exception {

		// Dump File
		String dumpFileName = "/usr/local/wiki/jawiki/20221101/" + "jawiki-20221101-pages-articles-multistream.xml.bz2";
		File dumpFile = new File(dumpFileName);

		WikiPageHandler handler = new WikiPageHandler() {
			private int count = 0;

			@Override
			public void read(WikiPage page) throws BreakException {
				count++;
				System.out.println(count + ": " + page.getTitle());
				if (count >= 10) {
					throw new BreakException("10");
				}
			}
		};

		try (WikiDumpReader reader = new WikiDumpReader(dumpFile);) {
			reader.read(handler);
		} catch (BreakException e) {
			System.err.println("Message: " + e.getMessage());
			System.err.println("OK");
		}

	}

}
