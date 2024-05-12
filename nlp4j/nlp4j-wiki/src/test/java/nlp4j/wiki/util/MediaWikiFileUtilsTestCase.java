package nlp4j.wiki.util;

import java.io.File;

import junit.framework.TestCase;

public class MediaWikiFileUtilsTestCase extends TestCase {

	public void testGetIndexFile() {
	}

	public void testGetDumpFile() {
	}

	public void testGetFile001() throws Exception {
		String dir = "/usr/local/wiki/jawiki/20230101";
		String lang = "ja";
		String media = "wiki";
		String version = "20230101";
		String type = "dump";
		File file = MediaWikiFileUtils.getFile(dir, lang, media, version, type);
		System.out.println(file.getAbsolutePath());
		System.out.println(file.exists());
	}

	public void testGetAbstarctFile() {
	}

	public static void main(String[] args) throws Exception {
		// 約70分
	}

}
