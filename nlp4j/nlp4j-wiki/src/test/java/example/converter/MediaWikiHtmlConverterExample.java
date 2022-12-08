package example.converter;

import java.io.File;

import nlp4j.wiki.converter.MediaWikiHtmlConverter;
import nlp4j.wiki.util.MediaWikiFileUtils;

public class MediaWikiHtmlConverterExample {

	public static void main(String[] args) throws Exception {

		File dumpFile = MediaWikiFileUtils.getDumpFile( //
				"/usr/local/data/wiki/jawiktionary/20221101", // File Path
				"ja", // Language
				"wiktionary", // Type (wiki | wiktionary)
				"20221101"); // Version

		File outFile = new File("R:/jawiktionary-20221101-pages-articles-multistream.xml.bz2.html.txt");

		MediaWikiHtmlConverter conveter = new MediaWikiHtmlConverter();

		conveter.convert(dumpFile, outFile);

	}

}
