package work202301;

import java.io.File;
import java.io.IOException;

import nlp4j.wiki.printer.WikiPagePrinter;

public class Work20230129_1WikiPagePrinter {

	public static void main(String[] args) throws IOException {

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

		try ( //
				WikiPagePrinter printer = new WikiPagePrinter(indexFile, dumpFile);//
		) {

//			printer.printPage("イチロー");
			printer.printPage("Category:鉄道駅");
			printer.printPage("Category:鉄道駅一覧");
			printer.printPage("Category:日本の鉄道駅一覧");
			printer.printPage("Category:台湾の鉄道駅一覧");

		}

	}

}
