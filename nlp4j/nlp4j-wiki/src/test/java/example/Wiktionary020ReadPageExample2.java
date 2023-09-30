package example;

import java.io.File;

import nlp4j.wiki.WikiDumpReader;
import nlp4j.wiki.WikiIndex;
import nlp4j.wiki.WikiIndexReader;
import nlp4j.wiki.WikiPage;
import nlp4j.wiki.util.MediaWikiFileUtils;

public class Wiktionary020ReadPageExample2 {

	public static void main(String[] args) throws Exception {

		String dir = "/usr/local/wiki/jawiki/20230101/";
		String lang = "ja";
		String media = "wiki";
		String version = "20230101";

		File indexFile = MediaWikiFileUtils.getIndexFile(dir, lang, media, version);
		File dumpFile = MediaWikiFileUtils.getDumpFile(dir, lang, media, version);

		System.err.println(indexFile);

		// WikiPedia のインデックスが読めるかどうかテスト

		WikiIndex wikiIndex = WikiIndexReader.readIndexFile(indexFile); // throws IOException
		try ( //
				WikiDumpReader dumpReader = new WikiDumpReader(dumpFile, indexFile);) {
			{

				for (String title : wikiIndex.getWikiItemTitles()) {

					if (title.startsWith("Category")) {
						WikiPage page = dumpReader.getItem(title);
						System.err.println("<text>");
						System.err.println(page.getTimestamp());
						System.err.println(page.getText()); // (1) Wiki 形式
						System.err.println("</text>");
						System.err.println(page.getXml() != null);
						System.err.println(page.getXml());
						return;
					}

				} // END OF FOR EACH

			}
		}

	}

}
