package nlp4j.wiki.printer;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nlp4j.wiki.WikiDumpReader;
import nlp4j.wiki.WikiPage;

public class WikiPagePrinter implements AutoCloseable {

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	WikiDumpReader dumpReader;

	public WikiPagePrinter(File indexFile, File dumpFile) throws IOException {
		this.dumpReader = new WikiDumpReader(dumpFile, indexFile);
	}

	public void printPage(String itemString) throws IOException {
		WikiPage page = dumpReader.getItem(itemString);
		logger.info("xml:" + page.getXml()); // <page>...</page>
	}

	@Override
	public void close() {
		if (dumpReader != null) {
			dumpReader.close();
		}

	}

}
