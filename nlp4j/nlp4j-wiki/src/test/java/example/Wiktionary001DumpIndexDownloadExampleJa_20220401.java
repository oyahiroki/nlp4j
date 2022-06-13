package example;

import nlp4j.wiki.MediaWikiDownloader;

public class Wiktionary001DumpIndexDownloadExampleJa_20220401 {

	static public void main(String[] args) throws Exception {
		MediaWikiDownloader dl = new MediaWikiDownloader();
		dl.setProperty("version", "20220401");
		dl.setProperty("outdir", "/usr/local/data/wiki/20220401");
		dl.setProperty("language", "ja");
		dl.setProperty("media", "wiktionary");
		dl.crawlDocuments();
		// ### Expected Output ###
		//
		// accessing:
		// https://dumps.wikimedia.org/jawiktionary/20220401/jawiktionary-20220401-md5sums.txt
		// Created: C:/usr/local/data/wiki/20220401
		// Download status (%): 34.92
		// Download status (%): 69.84
		// Download status (%): 100.0
		// accessing:
		// https://dumps.wikimedia.org/jawiktionary/20220401/jawiktionary-20220401-pages-articles-multistream-index.txt.bz2
		// Download status (%): 1.04
		// Download status (%): 2.0
		// Download status (%): 3.0
		//
		// Download status (%): 97.03
		// Download status (%): 98.04
		// Download status (%): 99.0
		// Download status (%): 100.0
		// Checking MD5:
		// C:/usr/local/data/wiki/20220401/jawiktionary-20220401-pages-articles-multistream-index.txt.bz2
		// Expected MD5=7f70d4f7af15efa2f1fa7cbeb80ffe6c
		// File MD5=7f70d4f7af15efa2f1fa7cbeb80ffe6c
		// Checking MD5: done
		// accessing:
		// https://dumps.wikimedia.org/jawiktionary/20220401/jawiktionary-20220401-pages-articles-multistream.xml.bz2
		// Download status (%): 1.0
		// Download status (%): 2.0
		// Download status (%): 3.0
		//
		// Download status (%): 97.0
		// Download status (%): 98.0
		// Download status (%): 99.0
		// Download status (%): 100.0
		// Checking MD5:
		// C:/usr/local/data/wiki/20220401/jawiktionary-20220401-pages-articles-multistream.xml.bz2
		// Expected MD5=758dac2da6ac55aa638ade3f040ca6f4
		// File MD5=758dac2da6ac55aa638ade3f040ca6f4
		// Checking MD5: done
	}
}
