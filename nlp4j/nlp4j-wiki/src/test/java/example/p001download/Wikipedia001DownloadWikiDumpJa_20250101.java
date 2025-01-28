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
public class Wikipedia001DownloadWikiDumpJa_20250101 {

	static public void main(String[] args) throws Exception {
		MediaWikiDownloader dl = new MediaWikiDownloader();
		dl.setProperty("version", "20250101");
		dl.setProperty("outdir", "/usr/local/wiki/jawiki/20250101");
		dl.setProperty("language", "ja");
		dl.setProperty("media", "wiki");
		dl.crawlDocuments();
	}
}
