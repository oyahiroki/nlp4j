package nlp4j.wiki.examples;

import java.io.File;

import nlp4j.wiki.WikiDumpReader;
import nlp4j.wiki.WikiPage;

public class WikiDumpReaderExample1_withIndex {

	public static void main(String[] args) throws Exception {

		String wikidumpfile_name = "/usr/local/wiki/enwiki/20230101/enwiki-20230101-pages-articles-multistream.xml.bz2";
		String wikiindexfile_name = "/usr/local/wiki/enwiki/20230101/enwiki-20230101-pages-articles-multistream-index.txt.bz2";
		String entry = "Dragon Ball";

		File wikidumpfile = new File(wikidumpfile_name);
		File wikiindexfile = new File(wikiindexfile_name);

		// Read Wikipedia Dump with Index
		try (WikiDumpReader dumpReader = new WikiDumpReader(wikidumpfile, wikiindexfile);) {
			WikiPage page = dumpReader.getItem(entry);
			System.err.println("<title>");
			System.err.println(page.getTitle());
			System.err.println("</title>");
			System.err.println("<plaintext>");
			System.err.println(page.getRootNodeWikiText());
			System.err.println("</plaintext>");
		}
	}
}
