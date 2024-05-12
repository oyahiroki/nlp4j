package nlp4j.wiki.util;

import java.io.File;

public class MediaWikiDumpFileUtilsMain001 {

	public static void main(String[] args) throws Exception {
		String dir = "/usr/local/wiki/jawiki/20230101";
		String lang = "ja";
		String media = "wiki";
		String version = "20230101";
		String type = "dump";

		File mediaWikiDumpFile = MediaWikiFileUtils.getFile(dir, lang, media, version, type);

		MediaWikiDumpFileUtils.printStats(mediaWikiDumpFile);

	}

}
