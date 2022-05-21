package example;

import java.io.File;

import nlp4j.wiki.WikiDumpReader;
import nlp4j.wiki.WikiPage;
import nlp4j.wiki.util.MediaWikiFileUtils;

public class Wiktionary020ReadPageExample1 {

	public static void main(String[] args) throws Exception {

		String itemString = "中学校";

		String dir = "/usr/local/data/wiki";
		String lang = "ja";
		String media = "wiktionary";
		String version = "20210401";

		File indexFile = MediaWikiFileUtils.getIndexFile(dir, lang, media, version);
		File dumpFile = MediaWikiFileUtils.getDumpFile(dir, lang, media, version);

		System.err.println(indexFile);

		// WikiPedia のインデックスが読めるかどうかテスト

		try (WikiDumpReader dumpReader = new WikiDumpReader(dumpFile, indexFile);) {
			{
				WikiPage page = dumpReader.getItem(itemString);
				System.err.println("<text>");
				System.err.println(page.getTimestamp());
				System.err.println(page.getText()); // (1) Wiki 形式
				System.err.println("</text>");
			}
		}

	}

}
