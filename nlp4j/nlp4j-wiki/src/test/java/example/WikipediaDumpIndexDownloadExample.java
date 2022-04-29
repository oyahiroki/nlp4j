package example;

import nlp4j.wiki.MediaWikiDownloader;

public class WikipediaDumpIndexDownloadExample {

	static public void main(String[] args) throws Exception {
		MediaWikiDownloader dl = new MediaWikiDownloader();
		dl.setProperty("version", "20220401");
		dl.setProperty("outdir", "/usr/local/data/wiki");
		dl.setProperty("language", "ja");
		dl.setProperty("media", "wiki");
		dl.crawlDocuments();
	}
}
