package example;

import java.io.File;

import nlp4j.wiki.WikiIndex;
import nlp4j.wiki.WikiIndexItem;
import nlp4j.wiki.WikiIndexReader;
import nlp4j.wiki.util.MediaWikiFileUtils;

public class Wiktionary011ReadIndexExample1 {

	public static void main(String[] args) throws Exception {

		File indexFile = MediaWikiFileUtils.getIndexFile( //
				"/usr/local/data/wiki", //
				"ja", //
				"wiktionary", //
				"20210401"); //
		System.err.println(indexFile.getAbsolutePath());

		String itemString = "ヨーロッパ";

		// Read Wiki Index File
		WikiIndex wikiIndex = WikiIndexReader.readIndexFile(indexFile); // throws IOException

		WikiIndexItem item = wikiIndex.getItem(itemString);

		// Expected output
// WikiIndexItem [blockNum=1227884, itemID=7613, title=ヨーロッパ, namespace=null, nextBlock=1263980]
		System.err.println(item);

	}

}
