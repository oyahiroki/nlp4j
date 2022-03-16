package nlp4j.wiki.examples;

import java.io.File;

import nlp4j.wiki.WikiDumpReader;
import nlp4j.wiki.WikiPage;

public class WikiDumpReaderExample2 {

	public static void main(String[] args) throws Exception {
		String wikidumpfile_name = "/usr/local/data/wiki/" + "jawiktionary-20220301-pages-articles-multistream.xml.bz2";
		String wikiindexfile_name = "/usr/local/data/wiki/"
				+ "jawiktionary-20220301-pages-articles-multistream-index.txt.bz2";
		String entry = "学校";
		File wikidumpfile = new File(wikidumpfile_name);
		File wikiindexfile = new File(wikiindexfile_name);
		WikiDumpReader dumpReader = new WikiDumpReader(wikidumpfile, wikiindexfile);
		{
			WikiPage page = dumpReader.getItem(entry);
			System.err.println("<text>");
			System.err.println(page.getText()); // (1) Wiki 形式
			System.err.println("</text>");

			System.err.println("<plaintext>");
			System.err.println(page.getPlainText()); // (2) Plain Text に近い形式
			System.err.println("</plaintext>");

			System.err.println("<html>");
			System.err.println(page.getHtml()); // (3) HTML 形式
			System.err.println("</html>");
		}
		dumpReader.close();
	}

}
