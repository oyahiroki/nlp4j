package example;

import java.io.File;

import nlp4j.wiki.WikiDumpReader;
import nlp4j.wiki.WikiPage;
import nlp4j.wiki.util.MediaWikiFileUtils;

public class WiktionaryReadPageExample1 {

	public static void main(String[] args) throws Exception {

		String itemString = "中学校";

		File indexFile = MediaWikiFileUtils.getIndexFile( //
				"/usr/local/data/wiki", //
				"ja", //
				"wiktionary", //
				"20210401"); //

		File dumpFile = MediaWikiFileUtils.getDumpFile( //
				"/usr/local/data/wiki", //
				"ja", //
				"wiktionary", //
				"20210401"); //

		System.err.println(indexFile);

		// WikiPedia のインデックスが読めるかどうかテスト

		WikiDumpReader dumpReader = new WikiDumpReader(dumpFile, indexFile);
		{
			WikiPage page = dumpReader.getItem(itemString);
			System.err.println("<text>");
			System.err.println(page.getText()); // (1) Wiki 形式
			System.err.println("</text>");

//			System.err.println("<plaintext>");
//			System.err.println(page.getPlainText()); // (2) Plain Text に近い形式
//			System.err.println("</plaintext>");

//			System.err.println("<html>");
//			System.err.println(page.getHtml()); // (3) HTML 形式
//			System.err.println("</html>");
		}
		dumpReader.close();

	}

}
