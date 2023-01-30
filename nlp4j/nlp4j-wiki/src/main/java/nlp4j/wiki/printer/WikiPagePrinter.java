package nlp4j.wiki.printer;

import java.io.File;
import java.io.IOException;

import nlp4j.wiki.WikiDumpReader;
import nlp4j.wiki.WikiPage;

public class WikiPagePrinter implements AutoCloseable {

	WikiDumpReader dumpReader;

	public WikiPagePrinter(File indexFile, File dumpFile) throws IOException {
		this.dumpReader = new WikiDumpReader(dumpFile, indexFile);
	}

	public void printPage(String itemString) throws IOException {
		WikiPage page = dumpReader.getItem(itemString);
		System.out.println(page.getXml()); // <page>...</page>
	}

	@Override
	public void close() {
		if (dumpReader != null) {
			dumpReader.close();
		}

	}

}
