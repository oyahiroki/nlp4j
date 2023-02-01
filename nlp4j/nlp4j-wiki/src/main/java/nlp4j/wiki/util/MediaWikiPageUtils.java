package nlp4j.wiki.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import nlp4j.wiki.MediawikiXmlHandler2;
import nlp4j.wiki.MediawikiXmlHandler3;
import nlp4j.wiki.WikiPage;

public class MediaWikiPageUtils {

	static public List<WikiPage> parsePagesFromPagesXml(File xmlFile) throws IOException {

//		HashMap<String, WikiPage> pages;
		WikiPage page;
		try {
			// XML Handler for Media Wiki
			MediawikiXmlHandler3 handler = new MediawikiXmlHandler3();
			handler.setKeepObject(true);

			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = saxParserFactory.newSAXParser();

			InputStream bais = new FileInputStream(xmlFile);

			saxParser.parse(bais, handler);

//			pages = handler.getPages();
			page = handler.getPage();

		} catch (SAXException | ParserConfigurationException e) {
			throw new IOException(e);
		}

//		System.err.println(pages.keySet());

//		for (WikiPage page : pages.values()) {
//			System.err.println("<page>");
//			System.err.println(page.toString());
//			System.err.println("categories: " + page.getCategoryTags());
//			System.err.println("</page>");
//		}

//		List<WikiPage> pp = new ArrayList<>(pages.values());
		List<WikiPage> pp = new ArrayList<>();
		pp.add(page);
		return pp;
	}

}
