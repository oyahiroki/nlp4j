package example;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import nlp4j.wiki.MediawikiXmlHandler2;
import nlp4j.wiki.WikiPage;
import nlp4j.wiki.util.MediaWikiTextParser;

public class Wikipedia021ReadPageXml4ParseKeyset {

	static void parse2(String text) {
		System.err.println(text);

		if (text.startsWith("{{")) {
			text = text.substring(2, text.length() - 2);
		}

		for (String s : text.split("\n\\|")) {
			s = s.trim();
			System.err.println(s);
			int idx = s.indexOf("=");
			if (idx == -1) {
				continue;
			} else {
				String key = s.substring(0, idx);
				String value = s.substring(idx + 1);
				System.err.println(String.format("%sは%sです．", key, value));
			}
			System.err.println("---");
		}
	}

	static void parse(String wikiText) {

		String b1 = "{{";
		String b2 = "}}";

//		System.err.println("-----");
//		System.err.println(wikiText);
//		System.err.println("-----");

		List<String> ss = MediaWikiTextParser.parse(wikiText, b1, b2);

		for (String s : ss) {
			s = s.replace("\n", "");

			System.err.println(s);

		}

		System.err.println("---");

		parse2(ss.get(0));

	}

	public static void main(String[] args) throws Exception {

		String fileName = "src/test/resources/nlp4j.wiki/" //
				+ "jawiki-20220501-pages-articles-multistream-255425.xml";

		// XML Handler for Media Wiki
		MediawikiXmlHandler2 handler = new MediawikiXmlHandler2();

		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		SAXParser saxParser = saxParserFactory.newSAXParser();

		InputStream bais = new FileInputStream(new File(fileName));

		saxParser.parse(bais, handler);

		HashMap<String, WikiPage> pages = handler.getPages();

//		System.err.println(pages.keySet());

		WikiPage p = pages.values().toArray(new WikiPage[0])[0];

//		System.err.println(p.getText());

//		for (WikiPage p : pages.values()) {
//			System.err.println(p.getPlainText());
//		}

		String pageText = p.getText();

		parse(pageText);
//		parse("{{AAA  {{BBB}}   {{CCC}} }}");
	}
}
