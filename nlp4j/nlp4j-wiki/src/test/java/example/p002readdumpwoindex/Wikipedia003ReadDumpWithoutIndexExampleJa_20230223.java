package example.p002readdumpwoindex;

import java.io.File;

import nlp4j.wiki.BreakException;
import nlp4j.wiki.WikiDumpReader;
import nlp4j.wiki.WikiPage;
import nlp4j.wiki.WikiPageHandler;

public class Wikipedia003ReadDumpWithoutIndexExampleJa_20230223 {

	public static void main(String[] args) throws Exception {

		String dumpFileName = "/usr/local/wiki/enwiktionary/20230101/"
				+ "enwiktionary-20230101-pages-articles-multistream.xml.bz2";

		// WIKI DUMP FILE
		File dumpFile = new File(dumpFileName);

		try (WikiDumpReader dumpReader = new WikiDumpReader(dumpFile)) {
			dumpReader.read(new WikiPageHandler() {
				int count = 0; // YOUR BREAK CONDITIN IF YOU NEED

				@Override
				public void read(WikiPage page) throws BreakException {

					if (page.getTitle().contains(":")) {
						return; // SKIP Template page
					}

					count++;

					if (count > 3) { // YOUR BREAK CONDITION HERE
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
