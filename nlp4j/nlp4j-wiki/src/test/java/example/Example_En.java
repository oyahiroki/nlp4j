package example;

import java.io.File;

import nlp4j.wiki.WikiDumpReader;
import nlp4j.wiki.WikiPage;

/**
 * Read WikiPedia Wiktionary data from Dump file
 * 
 * @author Hiroki Oya
 *
 */
public class Example_En {

	public static void main(String[] args) throws Exception {

		String dumpIndexFileName = "/usr/local/wiki/enwiktionary/20230101/"
				+ "enwiktionary-20230101-pages-articles-multistream-index.txt.bz2";
		String dumpFileName = "/usr/local/wiki/enwiktionary/20230101/"
				+ "enwiktionary-20230101-pages-articles-multistream.xml.bz2";

		String itemString = "NLP";

		// Index File
		File indexFile = new File(dumpIndexFileName);
		// Dump File
		File dumpFile = new File(dumpFileName);

		try (WikiDumpReader dumpReader = new WikiDumpReader(dumpFile, indexFile);) {
			{
				WikiPage page = dumpReader.getItem(itemString);
				System.out.println("<text>\n" + page.getText() + "\n</text>");
//				System.out.println("<xml>\n" + page.getXml() + "\n</xml>");
			}
		}

	}

}
