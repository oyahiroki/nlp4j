package work202302;

import java.io.File;

import junit.framework.TestCase;
import nlp4j.wiki.WikiDumpReader;
import nlp4j.wiki.WikiItemTextParser;
import nlp4j.wiki.WikiItemTextParserInterface;
import nlp4j.wiki.WikiPage;
import nlp4j.wiki.WikiPageNode;
import nlp4j.wiki.util.StringUtils;

public class Work20230223_Template extends TestCase {

	public void testWikiTemplate101() throws Exception {
		String itemString = "テンプレート:ja";
		String dir = "files/wiki/jawiktionary/20220501/";
		String fileIndexName = "jawiktionary-20220501-pages-articles-multistream-index.txt.bz2";
		String fileDumpName = "jawiktionary-20220501-pages-articles-multistream.xml.bz2";

		// WikiPedia のインデックスが読めるかどうかテスト
		try (WikiDumpReader dumpReader = new WikiDumpReader(new File(dir + fileDumpName),
				new File(dir + fileIndexName));) {
			WikiPage page = dumpReader.getItem(itemString);
			WikiItemTextParserInterface parser = new WikiItemTextParser();
			WikiPageNode rootNode = parser.parse(page.getText());
			System.out.println(rootNode.getText());
		}
	}

	public void testWikiTemplate102() throws Exception {
		String itemString = "テンプレート:L";
		String dir = "files/wiki/jawiktionary/20220501/";
		String fileIndexName = "jawiktionary-20220501-pages-articles-multistream-index.txt.bz2";
		String fileDumpName = "jawiktionary-20220501-pages-articles-multistream.xml.bz2";

		// WikiPedia のインデックスが読めるかどうかテスト
		try (WikiDumpReader dumpReader = new WikiDumpReader(new File(dir + fileDumpName),
				new File(dir + fileIndexName));) {
			WikiPage page = dumpReader.getItem(itemString);
			WikiItemTextParserInterface parser = new WikiItemTextParser();
			WikiPageNode rootNode = parser.parse(page.getText());
			System.out.println(rootNode.getText());
		}
	}

	public void testWikiTemplate103() throws Exception {
		String[] targets = { "L", "ja", "pron", "noun", "comp", "rel", "desc", "trans" };
		String itemString = "テンプレート:";
		String dir = "files/wiki/jawiktionary/20220501/";
		String fileIndexName = "jawiktionary-20220501-pages-articles-multistream-index.txt.bz2";
		String fileDumpName = "jawiktionary-20220501-pages-articles-multistream.xml.bz2";

		// WikiPedia のインデックスが読めるかどうかテスト
		try (WikiDumpReader dumpReader = new WikiDumpReader(new File(dir + fileDumpName),
				new File(dir + fileIndexName));) {

			for (String target : targets) {
				String itemTitle = itemString + target;
				WikiPage page = dumpReader.getItem(itemTitle);
				WikiItemTextParserInterface parser = new WikiItemTextParser();
				WikiPageNode rootNode = parser.parse(page.getText());
				System.out.println(target);
				System.out.println("---");
				System.out.println(rootNode.getText());
				System.out.println("---");
				System.out.println(StringUtils.remove(rootNode.getText(), "<noinclude>", "</noinclude>"));
				System.out.println("---");
				System.out.println("plaintext: " + page.getPlainText());
				System.out.println("------");
			}

		}
	}

}
