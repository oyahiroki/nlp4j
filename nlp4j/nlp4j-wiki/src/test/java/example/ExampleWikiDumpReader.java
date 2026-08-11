package example;

import java.io.File;

import nlp4j.wiki.WikiDumpReader;
import nlp4j.wiki.WikiPage;

public class ExampleWikiDumpReader {

	public static void main(String[] args) throws Exception {
		File indexFile = new File(
				"/usr/local/wiki/jawiki/20260801/jawiki-20260801-pages-articles-multistream-index.txt.bz2");
		File dumpFile = new File("/usr/local/wiki/jawiki/20260801/jawiki-20260801-pages-articles-multistream.xml.bz2__");
		String itemString = "日本";

		// WikiPedia のインデックスが読めるかどうかテスト

		System.out.println("indexFile: " + indexFile);
		System.out.println("dumpFile: " + dumpFile);
		System.out.println("itemString: " + itemString);

		try (WikiDumpReader dumpReader = new WikiDumpReader(dumpFile, indexFile);) {
			{
				WikiPage page = dumpReader.getItem(itemString);
				if (page == null) {
					System.err.println("Not Found: " + itemString);
					return;
				}
				System.out.println("<text>");
				System.out.println(page.getTimestamp());
				System.out.println(page.getText()); // (1) Wiki 形式
				System.out.println("</text>");
				System.out.println(page.getXml() != null);
				System.out.println(page.getXml());
			}
		}

	}

}
