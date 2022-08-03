package example;

import nlp4j.wiki.MediaWikiDownloader;

/**
 * <pre>
 * Download MediaWiki (MD5 AND Index AND Dump)
 * Wikiのダンプファイルとインデックスファイルをまとめてダウンロードし，MD5もチェックする
 * </pre>
 * 
 * @author Hiroki Oya
 *
 */
public class Wikipedia001DownloadWikiDumpExampleEn_20220401 {

	static public void main(String[] args) throws Exception {
		MediaWikiDownloader dl = new MediaWikiDownloader();
		dl.setProperty("version", "20220401");
		dl.setProperty("outdir", "/usr/local/data/wiki/20220401");
		dl.setProperty("language", "en");
		dl.setProperty("media", "wiki");
		dl.crawlDocuments();
	}
}
