package example;

import nlp4j.wiki.MediaWikiDownloader;

public class Wiktionary001DumpIndexDownloadExampleEn_20220401 {

	static public void main(String[] args) throws Exception {
		MediaWikiDownloader dl = new MediaWikiDownloader();
		dl.setProperty("version", "20220401");
		dl.setProperty("outdir", "/usr/local/data/wiki/20220401");
		dl.setProperty("language", "en");
		dl.setProperty("media", "wiktionary");
		dl.crawlDocuments();
	}
}
