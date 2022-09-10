package example;

import java.io.File;

import nlp4j.wiki.BreakException;
import nlp4j.wiki.WikiDumpReader;
import nlp4j.wiki.WikiPage;
import nlp4j.wiki.WikiPageHandler;

public class Wikipedia003ReadDumpWithoutIndexExampleJa_20220501 {

	public static void main(String[] args) throws Exception {

		// WIKI DUMP FILE
		File dumpFile = new File("C:/usr/local/data/wiki/20220501/jawiki-20220501-pages-articles-multistream.xml.bz2");

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

					System.err.println(page.getTitle());
					System.err.println(page.getText());
				}
			});
		} catch (BreakException be) {
			System.err.println("OK");
		}

	}
}
