package nlp4j.wiki.converter;

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
public class MediaWikiJsonConverter_001_Download {

	static public void main(String[] args) throws Exception {

		String lang = "ja";
		String media = "wiki";
		String date = "20231001";

		MediaWikiDownloader dl = (new MediaWikiDownloader.Builder()) //
				.version(date) //
				.outdir("/usr/local/wiki/" //
						+ lang //
						+ media //
						+ "/" //
						+ date //
						+ "") //
				.language(lang) //
				.media(media) //
				.build();
		dl.crawlDocuments();
	}
}
