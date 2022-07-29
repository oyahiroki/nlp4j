package example;

import nlp4j.wiki.MediaWikiDownloader;

public class Wiktionary001DumpIndexDownloadExampleEn_20220401 {

	static public void main(String[] args) throws Exception {
		MediaWikiDownloader dl = new MediaWikiDownloader();
		dl.setProperty("version", "20220401");
		dl.setProperty("outdir", "/usr/local/data/wiki/20220401");
		dl.setProperty("language", "en");
		dl.setProperty("media", "wiktionary");
		dl.crawlDocuments();

//INFO   nlp4j.webcrawler.FileDownloader :53   Already exists: C:\\usr\local\data\wiki\20220401\enwiktionary-20220401-md5sums.txt
//INFO   nlp4j.webcrawler.FileDownloader :53   Already exists: C:\\usr\local\data\wiki\20220401\enwiktionary-20220401-pages-articles-multistream-index.txt.bz2
//INFO   nlp4j.wiki.MediaWikiDownloader  :127  Checking MD5: C:\\usr\local\data\wiki\20220401\enwiktionary-20220401-pages-articles-multistream-index.txt.bz2
//INFO   nlp4j.wiki.util.MediaWikiMD5Util:63   Expected MD5=83fed3220dbe4ce7d960a2d460555f9a
//INFO   nlp4j.wiki.util.MediaWikiMD5Util:64   File     MD5=83fed3220dbe4ce7d960a2d460555f9a
//INFO   nlp4j.wiki.MediaWikiDownloader  :132  Checking MD5: done
//INFO   nlp4j.webcrawler.FileDownloader :53   Already exists: C:\\usr\local\data\wiki\20220401\enwiktionary-20220401-pages-articles-multistream.xml.bz2
//INFO   nlp4j.wiki.MediaWikiDownloader  :147  Checking MD5: C:\\usr\local\data\wiki\20220401\enwiktionary-20220401-pages-articles-multistream.xml.bz2
//INFO   nlp4j.wiki.util.MediaWikiMD5Util:63   Expected MD5=5165050790ebc91a4cc6d3a2b11c6eff
//INFO   nlp4j.wiki.util.MediaWikiMD5Util:64   File     MD5=5165050790ebc91a4cc6d3a2b11c6eff
//INFO   nlp4j.wiki.MediaWikiDownloader  :153  Checking MD5: done

	}
}
