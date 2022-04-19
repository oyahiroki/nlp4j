package example;

import java.io.File;
import java.io.FileNotFoundException;

import nlp4j.wiki.WikiDumpReader;
import nlp4j.wiki.WikiPage;

public class WiktionaryReadDumpIndexExample {

	public static void main(String[] args) throws Exception {

		String wikidumpfile_name = "/usr/local/data/wiki/" + "jawiktionary-20210401-pages-articles-multistream.xml.bz2";
		String wikiindexfile_name = "src/test/resources/nlp4j.wiki/jawiktionary-20210401-pages-articles-multistream-index.txt.bz2";
		String entry = "学校";

		File wikidumpfile = new File(wikidumpfile_name);

		if (wikidumpfile.exists() == false) {
			FileNotFoundException e = new FileNotFoundException(wikidumpfile.getAbsolutePath());
			e.printStackTrace();
			// skip this test
			return;
		} //
		else {

			File wikiindexfile = new File(wikiindexfile_name);

			WikiDumpReader dumpReader = new WikiDumpReader(wikidumpfile, wikiindexfile);

			{
				WikiPage page = dumpReader.getItem(entry);
				System.err.println("<plaintext>");
				System.err.println(page.getPlainText());
				System.err.println("</plaintext>");

				System.err.println("<text>");
				System.err.println(page.getText());
				System.err.println("</text>");

				System.err.println("<html>");
				System.err.println(page.getHtml());
				System.err.println("</html>");

			}

			dumpReader.close();

		}
	}

}
