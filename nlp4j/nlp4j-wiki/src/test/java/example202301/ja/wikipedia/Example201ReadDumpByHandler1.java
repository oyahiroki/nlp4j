package example202301.ja.wikipedia;

import java.io.File;

import nlp4j.wiki.BreakException;
import nlp4j.wiki.WikiDumpReader;
import nlp4j.wiki.WikiPage;
import nlp4j.wiki.WikiPageHandler;

public class Example201ReadDumpByHandler1 {

	public static void main(String[] args) throws Exception {

		String version = "20221101";

		// Dump File
		String dumpFileName = "/usr/local/wiki/jawiki/" + version + "/" //
				+ "jawiki-" + version + "-pages-articles-multistream.xml.bz2";
		File dumpFile = new File(dumpFileName);

		int count_max = 1;

		WikiPageHandler handler = new WikiPageHandler() {
			private int count = 0;

			@Override
			public void read(WikiPage page) throws BreakException {

				if (page.getTitle() != null && page.getTitle().startsWith("Wikipedia:")) {
					return;
				}

				count++;
				System.out.println("--------");
				System.out.println(count + ": " + page.getTitle());
				System.out.println("--------");
				System.out.println(page.toString2());
				System.out.println("--------");
				System.out.println(page.getXml());
				System.out.println("--------");
				System.out.println(page.getPlainText());
				System.out.println("--------");
				System.out.println(page.getRootNodeWikiText());
				System.out.println("--------");
				System.out.println(page.getRootNodePlainText());
				System.out.println("--------");
				if (count >= count_max) {
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
