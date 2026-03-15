package nlp4j.wiki.beta;

import java.util.Map;

public class WikiPageProcessor {

	public static WikiPage processPage(String title, String wikiText) {
		Map<String, String> infobox = InfoboxParser.extractFirstInfoboxAsPlainTextMap(wikiText);
		String plainText = WikiCleaner.toPlainText(wikiText);
		return new WikiPage(title, plainText, infobox);
	}

}
