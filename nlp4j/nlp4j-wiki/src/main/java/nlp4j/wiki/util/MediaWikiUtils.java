package nlp4j.wiki.util;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nlp4j.wiki.WikiDumpReader;
import nlp4j.wiki.WikiPage;

public class MediaWikiUtils {

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	/**
	 * Print MediaWiki XML to Console
	 * 
	 * @param dir        Directory of the file
	 * @param language   Language of MediaWiki like "en", "ja"
	 * @param media      Media Type (wiktionary|wiki)
	 * @param version    version in yyyyMMdd format like "20220501"
	 * @param itemString title of the Wiki Page
	 * @throws IOException on Error
	 */
	public static void printXml(String dir, String lang, String media, String version, String itemString)
			throws IOException {

		WikiPage page = getPage(dir, lang, media, version, itemString);
		if (page != null) {

			logger.info(String.format("https://%s.%s.org/wiki/%s", lang, media, itemString));

			System.err.println(page.getXml());
		}

	}

	private static WikiPage getPage(String dir, String lang, String media, String version, String itemString)
			throws IOException {
		// INDEX FILE
		File indexFile = MediaWikiFileUtils.getIndexFile(dir, lang, media, version);
		logger.info("Index_file: " + indexFile.getAbsolutePath());
		// DUMP FILE
		File dumpFile = MediaWikiFileUtils.getDumpFile(dir, lang, media, version);
		logger.info("Dump_file: " + dumpFile.getAbsolutePath());
		// READ FILE
		try (WikiDumpReader dumpReader = new WikiDumpReader(dumpFile, indexFile);) {
			{
				WikiPage page = dumpReader.getItem(itemString);
				return page;
			}
		}

	}

}
