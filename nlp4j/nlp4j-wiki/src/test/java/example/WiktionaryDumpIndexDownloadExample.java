package example;

import nlp4j.wiki.MediaWikiDownloader;

public class WiktionaryDumpIndexDownloadExample {

	static public void main(String[] args) throws Exception {
		MediaWikiDownloader dl = new MediaWikiDownloader();
		dl.setProperty("version", "20220301");
		dl.setProperty("outdir", "/usr/local/data/wiki");
		dl.setProperty("language", "ja");
		dl.setProperty("media", "wiktionary");
		dl.crawlDocuments();
	}
}
