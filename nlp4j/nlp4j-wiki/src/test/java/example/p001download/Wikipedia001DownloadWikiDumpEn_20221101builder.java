package example.p001download;

import nlp4j.wiki.MediaWikiDownloader;
import nlp4j.wiki.MediaWikiDownloader.media;

/**
 * <pre>
 * Download MediaWiki (MD5 AND Index AND Dump)
 * Wikiのダンプファイルとインデックスファイルをまとめてダウンロードし，MD5もチェックする
 * </pre>
 * 
 * @author Hiroki Oya
 *
 */
public class Wikipedia001DownloadWikiDumpEn_20221101builder {

	static public void main(String[] args) throws Exception {
		MediaWikiDownloader dl = (new MediaWikiDownloader.Builder()) //
				.version("20221101") //
//				.outdir("/usr/local/wiki/enwiki/20221101") //
				.language("en") //
				.media(media.wiki) //
				.build();
		dl.crawlDocuments();
	}
}
