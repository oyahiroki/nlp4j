package nlp4j.wiki.util;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nlp4j.wiki.BreakException;
import nlp4j.wiki.WikiIndexItem;
import nlp4j.wiki.WikiIndexItemHandler;
import nlp4j.wiki.WikiIndexReader;

public class MediaWikiIndexFileUtils {

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	static public void printTitles(File indexFile) throws IOException {

		// Read Wiki Index File
		WikiIndexReader.readIndexFile(indexFile, new WikiIndexItemHandler() {
			@Override
			public void read(WikiIndexItem item) throws BreakException {

				if (logger.isDebugEnabled()) {
					logger.debug(item.getTitle());

				}
			}
		}); // throws IOException

	}

}
