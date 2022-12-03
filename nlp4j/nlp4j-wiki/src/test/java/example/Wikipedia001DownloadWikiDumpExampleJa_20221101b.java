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
public class Wikipedia001DownloadWikiDumpExampleJa_20221101b {

	static public void main(String[] args) throws Exception {
		String version = "20221101";
		String lang = "ja";
		String media = "wiktionary";
		MediaWikiDownloader dl = new MediaWikiDownloader();
		dl.setProperty("version", version);
		dl.setProperty("outdir", "/usr/local/data/wiki/" + lang + media + "/" + version);
		dl.setProperty("language", lang);
		dl.setProperty("media", media);
		dl.crawlDocuments();
	}
}
