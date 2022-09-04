package nlp4j.wiki.util;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;

public class MediaWikiIndexFileUtilsTestCase extends TestCase {

	public void testPrintTitles() throws IOException {
		File indexFile = new File(
				"C:/usr/local/data/wiki/20220501/" + "enwiktionary-20220501-pages-articles-multistream-index.txt.bz2");

		if (indexFile.exists() == false) {
			System.err.println("Not found: " + indexFile.getAbsolutePath());
			return; // SKIP THIS FILE
		}

		MediaWikiIndexFileUtils.printTitles(indexFile);
	}

}
