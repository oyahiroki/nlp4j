package example;

import nlp4j.wiki.MediaWikiDownloader;

/**
 * Download MediaWiki (MD5 AND Index AND Dump)
 * 
 * @author Hiroki Oya
 *
 */
public class Wikipedia001DumpIndexDownloadExample {

	static public void main(String[] args) throws Exception {
		MediaWikiDownloader dl = new MediaWikiDownloader();
		dl.setProperty("version", "20220401");
		dl.setProperty("outdir", "/usr/local/data/wiki");
		dl.setProperty("language", "ja");
		dl.setProperty("media", "wiki");
		dl.crawlDocuments();
	}
}
