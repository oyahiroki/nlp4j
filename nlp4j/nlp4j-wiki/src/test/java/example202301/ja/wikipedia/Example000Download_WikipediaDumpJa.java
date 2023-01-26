package example202301.ja.wikipedia;

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
public class Example000Download_WikipediaDumpJa {

	static public void main(String[] args) throws Exception {
		MediaWikiDownloader dl = (new MediaWikiDownloader.Builder()) //
				.version("20221101") //
				.outdir("/usr/local/wiki/jawiki/20221101") //
				.language("ja") //
				.media(media.wiki) //
				.build();
		dl.crawlDocuments();
	}
}
