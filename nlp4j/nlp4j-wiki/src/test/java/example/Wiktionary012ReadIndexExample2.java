package example;

import java.io.File;

import nlp4j.wiki.WikiIndex;
import nlp4j.wiki.WikiIndexItem;
import nlp4j.wiki.WikiIndexReader;
import nlp4j.wiki.util.MediaWikiFileUtils;

public class Wiktionary012ReadIndexExample2 {

	public static void main(String[] args) throws Exception {

		File indexFile = MediaWikiFileUtils.getIndexFile( //
				"/usr/local/data/wiki", //
				"ja", //
				"wiktionary", //
				"20210401"); //
		System.err.println(indexFile.getAbsolutePath());

		String itemString = "学校";

		// Read Wiki Index File
		WikiIndex wikiIndex = WikiIndexReader.readIndexFile(indexFile); // throws IOException

		WikiIndexItem item = wikiIndex.getItem(itemString);

		// Expected output
// WikiIndexItem [blockNum=2293539, itemID=11349, title=学校, namespace=null, nextBlock=2357913]
		System.err.println(item);

	}

}
