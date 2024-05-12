package nlp4j.wiki.util;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;

public class MediaWikiDumpFileUtilsTestCase extends TestCase {

	public void testPrintStats001() throws IOException {

		String dir = "/usr/local/wiki/jawiktionary/20230101";
		String lang = "ja";
		String media = "wiktionary";
		String version = "20230101";
		String type = "dump";

		File mediaWikiDumpFile = MediaWikiFileUtils.getFile(dir, lang, media, version, type);

		MediaWikiDumpFileUtils.printStats(mediaWikiDumpFile);

	}

}
