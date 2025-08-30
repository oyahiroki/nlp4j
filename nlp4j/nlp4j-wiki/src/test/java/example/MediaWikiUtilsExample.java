package example;

import nlp4j.wiki.util.MediaWikiUtils;

public class MediaWikiUtilsExample {

	public static void main(String[] args) throws Exception {

		String itemString = "中学校";

		String dir = "/usr/local/wiki/jawiktionary/20210401";
		String lang = "ja";
		String media = "wiktionary";
		String version = "20210401";

		MediaWikiUtils.printXml(dir, lang, media, version, itemString);

	}

}
