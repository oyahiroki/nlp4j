package example.p001download;

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
public class Wikipedia001DownloadWiktinaryDumpExampleEn_20221101 {

	static public void main(String[] args) throws Exception {
		MediaWikiDownloader dl = new MediaWikiDownloader();
		dl.setProperty("version", "20221101");
		dl.setProperty("outdir", "/usr/local/data/wiki/enwiktionary/20221101");
		dl.setProperty("language", "en");
		dl.setProperty("media", "wiktionary");
		dl.crawlDocuments();
	}
}
