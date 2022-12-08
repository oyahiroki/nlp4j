package example;

import nlp4j.wiki.util.MediaWikiUtils;

public class MediaWikiUtilsExample {

	public static void main(String[] args) throws Exception {

		String itemString = "中学校";

		String dir = "/usr/local/data/wiki/jawiktionary/20221101";
		String lang = "ja";
		String media = "wiktionary";
		String version = "20221101";

		MediaWikiUtils.printXml(dir, lang, media, version, itemString);

	}

}
