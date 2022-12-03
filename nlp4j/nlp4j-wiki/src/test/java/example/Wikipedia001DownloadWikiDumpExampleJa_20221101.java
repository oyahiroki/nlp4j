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
public class Wikipedia001DownloadWikiDumpExampleJa_20221101 {

	static public void main(String[] args) throws Exception {
		MediaWikiDownloader dl = new MediaWikiDownloader();
		dl.setProperty("version", "20221101");
		dl.setProperty("outdir", "/usr/local/data/wiki/jawiktionary/20221101");
		dl.setProperty("language", "ja");
		dl.setProperty("media", "wiktionary");
		dl.crawlDocuments();
	}
}
