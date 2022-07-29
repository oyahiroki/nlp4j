package example;

import java.io.File;
import java.lang.invoke.MethodHandles;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nlp4j.util.FilePrinter;
import nlp4j.wiki.WikiIndex;
import nlp4j.wiki.WikiIndexReader;
import nlp4j.wiki.util.MediaWikiFileUtils;

public class Wiktionary100CompareWithWikipedia {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	public static void main(String[] args) throws Exception {

		// Wiktionary の見出しのうち，Wikipedia の見出しにも含まれるものを出力する

		FilePrinter prn = new FilePrinter("/usr/local/data/wiki/20220501", "wikidiff", ".txt");
		List<String> wiktionaryTitles = null;
		{
			File indexFile1Wiktionary = MediaWikiFileUtils.getIndexFile( //
					"/usr/local/data/wiki/20220501", //
					"ja", //
					"wiktionary", //
					"20220501"); //
			System.err.println(indexFile1Wiktionary.getAbsolutePath());
			// Read Wiki Index File
			WikiIndex wikiIndex1Wiktionary //
					= WikiIndexReader.readIndexFile(indexFile1Wiktionary); // throws IOException
			{
				// 見出しの数
				// titles: 329428
				System.err.println("Wiktionary titles: " + wikiIndex1Wiktionary.getWikiItemTitles().size());
			}
			wiktionaryTitles = wikiIndex1Wiktionary.getWikiItemTitles();
		}

		File indexFile2Wikipedia = MediaWikiFileUtils.getIndexFile( //
				"/usr/local/data/wiki/20220501", //
				"ja", //
				"wiki", //
				"20220501"); //
		System.err.println(indexFile2Wikipedia.getAbsolutePath());

		// Read Wiki Index File
		WikiIndex wikiIndex2Wikipedia //
				= WikiIndexReader.readIndexFile(indexFile2Wikipedia); // throws IOException
		{
			System.err.println("Wikipedia titles: " + wikiIndex2Wikipedia.getWikiItemTitles().size());
		}

		List<String> WikiItemTitles = wikiIndex2Wikipedia.getWikiItemTitles();

		int found = 0;
		for (String wiktionaryTitle : wiktionaryTitles) {
			if (WikiItemTitles.contains(wiktionaryTitle) == true) {
				prn.println(wiktionaryTitle);
				found++;
				logger.info(found + " " + wiktionaryTitle);
			}
		}

		prn.close();

	}

}
