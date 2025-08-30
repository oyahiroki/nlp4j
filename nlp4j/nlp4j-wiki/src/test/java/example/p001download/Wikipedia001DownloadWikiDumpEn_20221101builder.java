package example.p001download;

import nlp4j.util.DateUtils;
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

		String yyyyMMdd = DateUtils.get_yyyyMM() + "01";
		String lang = "en";

		MediaWikiDownloader dl = (new MediaWikiDownloader.Builder()) //
				.version(yyyyMMdd) //
				.outdir("/usr/local/wiki/enwiki/" + yyyyMMdd) //
				.language(lang) //
				.media(media.wiktionary) //
				.build();
		dl.crawlDocuments();
	}
}
