package example202301.ja.wikipedia;

import java.io.File;

import nlp4j.util.StringUtils;
import nlp4j.wiki.WikiDumpReader;
import nlp4j.wiki.WikiPage;

public class Example101Read_WikipediaDumpJa {

	public static void main(String[] args) throws Exception {

		// Wiktionary のページタイトルを指定して取得する

		// Wiktionary Page Title
		String itemString = "学校";

		// Index File
		String indexFileName = "/usr/local/wiki/jawiki/20221101/jawiki-20221101-pages-articles-multistream-index.txt.bz2";
		// Dump File
		String dumpFileName = "/usr/local/wiki/jawiki/20221101/jawiki-20221101-pages-articles-multistream.xml.bz2";
		// Index File
		File indexFile = new File(indexFileName);
		System.err.println(indexFile.getAbsolutePath());
		// Dump File
		File dumpFile = new File(dumpFileName);
		System.err.println(dumpFile.getAbsolutePath());

		try (WikiDumpReader dumpReader = new WikiDumpReader(dumpFile, indexFile);) {
			// ページの情報を取得する
			WikiPage page = dumpReader.getItem(itemString);

			// ページが存在しない
			if (page == null) {
				System.err.println("Not found: " + itemString);
				// 終了
				return;
			}

			{ // IF(ページが存在する)
				System.err.println("title: " + page.getTitle());
				System.err.println("timestamp: " + page.getTimestamp());
				System.err.println("redirect: " + page.isRediect());
				if (page.isRediect()) {
					System.err.println("This page is redirected to: " + page.getRediect_title());
					return;
				}
				//
				else {
					System.err.println("categories: " + page.getCategoryTags());
					System.err.println("<text>");
					System.err.println(page.getText()); // <text>...</text>
					System.err.println("</text>");
					System.err.println("<xml>");
					System.err.println(page.getXml()); // <page>...</page>
					System.err.println("</xml>");
				}
			} // END_OF_IF(ページが存在する)

			page.getNodes().forEach(node -> {
				System.err.println("node.title: " + node.getTitle());
				System.err.println("node.header: " + node.getHeader());
				{
					String text = StringUtils.chop(node.getText(), 32).replace("\n", "\\n");
					System.err.println("node.text:" + text);
				}
				{
					String plainText = StringUtils.chop(node.getPlainText(), 32).replace("\n", "\\n");
					System.err.println("node.plainText:" + plainText);
				}

				node.getPlainText();

			});

		}

	}

}
