package work20250816wikidumpbench;

import org.apache.commons.lang3.time.StopWatch;

import nlp4j.wiki.BreakException;
import nlp4j.wiki.WikiDumpReader;
import nlp4j.wiki.WikiPage;
import nlp4j.wiki.WikiPageHandler;

public class WikiDumpBench {

	/**
	 * 
	 * 
	 * <pre>
	100 	00:00:01.496
	200 	00:00:02.298
	300 	00:00:03.795
	400 	00:00:04.543
	500 	00:00:04.933
	600 	00:00:05.689
	700 	00:00:06.213
	800 	00:00:07.232
	900 	00:00:08.331
	1000 	00:00:09.071
	 * </pre>
	 * 
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

//		String wiki_dir = "/usr/local/wiki/jawiki/20250801/";
//		String wiki_dumpFileName = wiki_dir + "jawiki-20250801-pages-articles-multistream.xml.bz2";

//		String wiki_dir = "r:/";
//		String wiki_dumpFileName = wiki_dir + "jawiki-20250801-pages-articles-multistream.xml.bz2";

//		String wiki_dir = "src/test/resources/nlp4j.wiki/";
		String wiki_dir = "r:/";
		String wiki_dumpFileName = wiki_dir + "jawiki-20250801-pages-articles-multistream_1m_lines.xml.bz2";

		// Create WikiPage handler
		WikiPageHandler wikiPageHander = new WikiPageHandler() {
			org.apache.commons.lang3.time.StopWatch sw = new StopWatch();
			int count = 0;

			@Override
			public void startDocument() {

				WikiPageHandler.super.startDocument();
				sw.start();
			}

			@Override
			public void endDocument() {
				WikiPageHandler.super.endDocument();
				sw.stop();
			}

			@Override
			public void read(WikiPage page) throws BreakException {
				count++;
				if (count % 100 == 0) {
					System.err.println(count);
					sw.split();
					System.err.println(sw.formatSplitTime());
				}
			} // END_OF_read()
		}; // Handler

		long time1 = System.currentTimeMillis();
		try (WikiDumpReader dumpReader = new WikiDumpReader(wiki_dumpFileName)) {
			try {
				dumpReader.read(wikiPageHander);
			} catch (BreakException be) {
				// OK
			}
		}
		long time2 = System.currentTimeMillis();
		System.err.println("time: " + (time2 - time1));

	}
}
