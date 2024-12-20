package example.p002readdumpwoindex;

import java.io.File;

import nlp4j.wiki.BreakException;
import nlp4j.wiki.WikiDumpReader;
import nlp4j.wiki.WikiPage;
import nlp4j.wiki.WikiPageHandler;

public class Wikipedia003ReadDumpWithoutIndexExampleJa_20230223 {

	public static void main(String[] args) throws Exception {

		// DUMP FILE NAME
		String dumpFileName = "/usr/local/data/wiki/jawiki/20221101/"
				+ "jawiki-20221101-pages-articles-multistream.xml.bz2";

		// WIKI DUMP FILE
		File dumpFile = new File(dumpFileName);

		try (WikiDumpReader dumpReader = new WikiDumpReader(dumpFile)) {
			dumpReader.read(
					// YOUR WikiPageHandelr HERE
					new WikiPageHandler() {
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
							System.out.println(page.getTitle());
							System.out.println(page.getText());
						}
					});
		} catch (BreakException be) {
			System.err.println("OK");
		}

	}
}
