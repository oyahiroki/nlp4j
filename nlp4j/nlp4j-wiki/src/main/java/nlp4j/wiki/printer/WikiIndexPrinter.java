package nlp4j.wiki.printer;

import java.io.File;
import java.io.IOError;
import java.io.IOException;

import nlp4j.wiki.BreakException;
import nlp4j.wiki.WikiIndexItem;
import nlp4j.wiki.WikiIndexItemHandler;
import nlp4j.wiki.WikiIndexReader;

public class WikiIndexPrinter implements AutoCloseable {

	File indexFile;

	int maxCount = -1;

	public WikiIndexPrinter(File indexFile) throws IOException {
		this.indexFile = indexFile;
	}

	public WikiIndexPrinter(File indexFile, int maxCount) throws IOException {
		this.indexFile = indexFile;
		this.maxCount = maxCount;
	}

	public void print() throws IOException {

		// Read Wiki Index File
		WikiIndexReader.readIndexFile(indexFile, new WikiIndexItemHandler() {
			int count = 0;

			@Override
			public void read(WikiIndexItem item) throws BreakException {
				count++;
				if (maxCount != -1 && count >= maxCount) {
					throw new BreakException();
				}
				System.out.println(item.getTitle());
			}
		}); // throws IOException

	}

	@Override
	public void close() {

	}

}
