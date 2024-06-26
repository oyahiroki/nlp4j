package example;

import java.io.File;

import nlp4j.wiki.WikiIndex;
import nlp4j.wiki.WikiIndexReader;
import nlp4j.wiki.util.MediaWikiFileUtils;

public class Wiktionary010ReadIndexExample1c {

	public static void main(String[] args) throws Exception {

		File indexFile = MediaWikiFileUtils.getIndexFile( //
				"/usr/local/wiki/jawiki/20230101", //
				"ja", //
				"wiki", //
				"20230101"); //
		System.err.println(indexFile.getAbsolutePath());

		// Read Wiki Index File
		WikiIndex wikiIndex = WikiIndexReader.readIndexFile(indexFile); // throws IOException
		{
			// 見出しの数
			// titles: 296564
			System.err.println("titles: " + wikiIndex.getWikiItemTitles().size());
			// WikiIndex [wikiItemTitles=296564]
			System.err.println(wikiIndex);

			System.err.println("---");

			int count = 1;
			int countMax = 100;

			// FOR EACH(WikiItemTitles) THEN print info
			for (String title : wikiIndex.getWikiItemTitles()) {
				System.err.println(String.format("%012d %s", count, title));
				if (title.contains(":")) {
					continue;
				}
				count++;
				if (count > countMax) {
					break;
				}
			} // END OF FOR EACH

			// <ExpectedOutput>
//			000000000001 MediaWiki:Mainpagetext
//			000000000002 MediaWiki:Aboutpage
//			000000000003 MediaWiki:Faqpage
//			000000000004 MediaWiki:Edithelppage
//			000000000005 MediaWiki:Wikipediapage
//			...
//			000000296556 てんぱん
//			000000296557 てんしょく
//			000000296558 osu
//			000000296559 preferreds
//			000000296560 せんとう
//			000000296561 せんどう
//			000000296562 preferred stock
//			000000296563 preferred stocks
//			000000296564 preferences
			// </ExpectedOutput>

			System.err.println("---");

			System.err.println("titles: " + wikiIndex.getWikiItemTitles().size());
		}

	}

}
