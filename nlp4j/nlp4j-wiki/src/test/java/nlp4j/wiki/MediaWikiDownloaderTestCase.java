package nlp4j.wiki;

import junit.framework.TestCase;
import nlp4j.wiki.MediaWikiDownloader.media;

public class MediaWikiDownloaderTestCase extends TestCase {

	public void testCrawlDocuments() throws Exception {
		MediaWikiDownloader dl = (new MediaWikiDownloader.Builder()) //
				.version("20221101") //
				.outdir("/usr/local/wiki/enwiki/20221101") //
				.language("en") //
				.media(media.wiki) //
				.build();
		assertNotNull(dl);
		System.err.println("SKIP test download.");
		// dl.crawlDocuments();
	}

	public void testPrintURL001() {
		MediaWikiDownloader dl = (new MediaWikiDownloader.Builder()) //
				.version("20221101") //
				.language("en") //
				.media(media.wiki) //
				.build();
		dl.printURL();
	}

	public void testPrintURL002() {
		MediaWikiDownloader dl = (new MediaWikiDownloader.Builder()) //
				.version("20221101") //
				.language("ja") //
				.media(media.wiki) //
				.build();
		dl.printURL();
	}

}
