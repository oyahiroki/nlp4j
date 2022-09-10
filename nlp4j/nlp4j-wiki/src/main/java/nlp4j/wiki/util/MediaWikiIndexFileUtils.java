package nlp4j.wiki.util;

import java.io.File;
import java.io.IOException;

import nlp4j.wiki.BreakException;
import nlp4j.wiki.WikiIndexItem;
import nlp4j.wiki.WikiIndexItemHandler;
import nlp4j.wiki.WikiIndexReader;

public class MediaWikiIndexFileUtils {

	static public void printTitles(File indexFile) throws IOException {

		// Read Wiki Index File
		WikiIndexReader.readIndexFile(indexFile, new WikiIndexItemHandler() {
			@Override
			public void read(WikiIndexItem item) throws BreakException {
				System.out.println(item.getTitle());
			}
		}); // throws IOException

	}

}
