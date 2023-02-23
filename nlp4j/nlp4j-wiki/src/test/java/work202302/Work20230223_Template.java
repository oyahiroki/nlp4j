package work202302;

import java.io.File;

import junit.framework.TestCase;
import nlp4j.wiki.WikiDumpReader;
import nlp4j.wiki.WikiItemTextParser;
import nlp4j.wiki.WikiPage;
import nlp4j.wiki.WikiPageNode;

public class Work20230223_Template extends TestCase {

	public void testWikiTemplate101() throws Exception {
		String itemString = "テンプレート:ja";

		String dir = "files/wiki/jawiktionary/20220501/";
		String fileIndexName = "jawiktionary-20220501-pages-articles-multistream-index.txt.bz2";
		String fileDumpName = "jawiktionary-20220501-pages-articles-multistream.xml.bz2";

		File indexFile = new File(dir + fileIndexName);
		File dumpFile = new File(dir + fileDumpName);

		System.err.println(indexFile.getAbsolutePath());
		System.err.println(dumpFile.getAbsolutePath());

		// WikiPedia のインデックスが読めるかどうかテスト

		try (WikiDumpReader dumpReader = new WikiDumpReader(dumpFile, indexFile);) {
			WikiPage page = dumpReader.getItem(itemString);
			WikiItemTextParser parser = new WikiItemTextParser();
			WikiPageNode rootNode = parser.parse(page.getText());
			System.err.println(rootNode.getText());
		}
	}

	public void testWikiTemplate102() throws Exception {
		String itemString = "テンプレート:L";

		String dir = "files/wiki/jawiktionary/20220501/";
		String fileIndexName = "jawiktionary-20220501-pages-articles-multistream-index.txt.bz2";
		String fileDumpName = "jawiktionary-20220501-pages-articles-multistream.xml.bz2";

		File indexFile = new File(dir + fileIndexName);
		File dumpFile = new File(dir + fileDumpName);

		System.err.println(indexFile.getAbsolutePath());
		System.err.println(dumpFile.getAbsolutePath());

		// WikiPedia のインデックスが読めるかどうかテスト

		try (WikiDumpReader dumpReader = new WikiDumpReader(dumpFile, indexFile);) {
			WikiPage page = dumpReader.getItem(itemString);
			WikiItemTextParser parser = new WikiItemTextParser();
			WikiPageNode rootNode = parser.parse(page.getText());
			System.out.println(rootNode.getText());
		}
	}

}
