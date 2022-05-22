package example;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import nlp4j.wiki.MediawikiXmlHandler2;
import nlp4j.wiki.WikiPage;

public class Wikipedia021ReadPageXml3WikiText {

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

		System.err.println(p.getText());

//		for (WikiPage p : pages.values()) {
//			System.err.println(p.getPlainText());
//		}
	}
}
