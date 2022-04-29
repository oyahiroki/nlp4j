package example;

import java.io.File;

import nlp4j.wiki.WikiIndex;
import nlp4j.wiki.WikiIndexItem;
import nlp4j.wiki.WikiIndexReader;

public class WiktionaryReadIndexExample1 {

	public static void main(String[] args) throws Exception {

		String indexFileName = "src/test/resources/nlp4j.wiki/"
				+ "jawiktionary-20210401-pages-articles-multistream-index.txt.bz2";

		String itemString = "ヨーロッパ";

		File indexFile = new File(indexFileName);

		if (indexFile.exists() == false) {
			System.err.println("Not exists: " + indexFile.getAbsolutePath());
			return;
		}

		System.err.println(indexFile);

		// Read Wiki Index File
		WikiIndex wikiIndex = WikiIndexReader.readIndexFile(indexFile); // throws IOException
		{
			// 見出しの数
			// titles: 296564
			System.err.println("titles: " + wikiIndex.getWikiItemTitles().size());
			// WikiIndex [wikiItemTitles=296564]
			System.err.println(wikiIndex);

			System.err.println("---");

			for (String title : wikiIndex.getWikiItemTitles()) {
				System.err.println(title);
			}

			System.err.println("---");

			System.err.println("titles: " + wikiIndex.getWikiItemTitles().size());
		}

		WikiIndexItem item = wikiIndex.getItem(itemString);

		// WikiIndexItem [blockNum=1227884, itemID=7613, title=ヨーロッパ, namespace=null,
		// nextBlock=1263980]
		System.err.println(item);

	}

}
