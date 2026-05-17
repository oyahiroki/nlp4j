package nlp4j.wiki.util;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nlp4j.counter.Counter;
import nlp4j.util.DateUtils;
import nlp4j.util.NumberUtils;
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

	static public void printProperties(File indexFile) throws IOException {

		Counter<String> counter_ns = new Counter<String>();
		AtomicInteger counter_title = new AtomicInteger(0);

		// Read Wiki Index File
		WikiIndexReader.readIndexFile(indexFile, new WikiIndexItemHandler() {

			boolean debug = false;
			int max_count = 100;

			@Override
			public void read(WikiIndexItem item) throws BreakException {

				int count = counter_title.incrementAndGet();

				if (debug) {
					if (count > max_count) {
						throw new BreakException("count:" + counter_title);
					}
				}

//				System.out.println(item.getNamespace() + ":" + item.getTitle());

				counter_ns.add("" + item.getNamespace());

				if (logger.isDebugEnabled()) {
					logger.debug(item.getTitle());

				}
			}

			@Override
			public void end() {
				System.out.println("Index file name: " + indexFile.getAbsolutePath());
				System.out.println("Index file size: " + NumberUtils.format(indexFile.length()));
				System.out
						.println("Index file last update: " + DateUtils.toISO8601(new Date(indexFile.lastModified())));
				System.out.println("count: " + NumberUtils.format(counter_title.get()));
//				System.out.println("```namespaces");
//				counter_ns.getCountList().stream().forEach(c -> {
//					System.out.println("value:" + c.getValue() + ",count: " + c.getCount());
//				});
//				System.out.println("```");
			}

		}); // throws IOException

	}
}
