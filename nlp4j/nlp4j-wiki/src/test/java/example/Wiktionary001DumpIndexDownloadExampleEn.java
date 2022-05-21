package example;

import nlp4j.wiki.MediaWikiDownloader;

public class Wiktionary001DumpIndexDownloadExampleEn {

	static public void main(String[] args) throws Exception {
		MediaWikiDownloader dl = new MediaWikiDownloader();
		dl.setProperty("version", "20220501");
		dl.setProperty("outdir", "/usr/local/data/wiki");
		dl.setProperty("language", "en");
		dl.setProperty("media", "wiktionary");
		dl.crawlDocuments();
	}
}
