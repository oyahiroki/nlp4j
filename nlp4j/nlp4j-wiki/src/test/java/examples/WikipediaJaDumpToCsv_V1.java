package examples;

import java.io.File;
import java.io.IOException;

import nlp4j.util.CsvWriter;
import nlp4j.wiki.BreakException;
import nlp4j.wiki.WikiDumpReader;
import nlp4j.wiki.WikiPage;
import nlp4j.wiki.WikiPageHandler;

public class WikipediaJaDumpToCsv_V1 {

	public static void main(String[] args) throws Exception {

		boolean debug = false;

		String lang = "ja";
		String media = "wiki";

		String dir = "/usr/local/wiki/" + lang + media + "/20230101/";

		String dumpFileName = dir + lang + media + "-20230101-pages-articles-multistream.xml.bz2";

		String out_csvFileName = dir + lang + media + "-20230101-pages-abstract.csv";

		CsvWriter csv_writer = new CsvWriter(new File(out_csvFileName));

		// Create WikiPage handler
		WikiPageHandler wikiPageHander = new WikiPageHandler() {
			int count = 0;

			@Override
			public void read(WikiPage page) throws BreakException {
				if (page != null && page.getNamespace().equals("0")) {
					count++;
					if (debug && count > 3) { // IF YOU WANT TO BREAK
						throw new BreakException();
					}
					if (count % 1000 == 0) {
						System.err.println(count);
					}

					String title = page.getTitle();
					String abst_text = page.getRootNodePlainText();

					try {
						csv_writer.write(title, abst_text);
					} catch (IOException e) {
						System.err.println("title: " + title);
						e.printStackTrace();
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

		csv_writer.close();

	}

}
