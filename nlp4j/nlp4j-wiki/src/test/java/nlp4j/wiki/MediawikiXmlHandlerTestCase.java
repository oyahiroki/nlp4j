package nlp4j.wiki;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import junit.framework.TestCase;

public class MediawikiXmlHandlerTestCase extends TestCase {

	public void testGetPages101() throws Exception {

		String fileName = "src/test/resources/nlp4j.wiki/jawiktionary-dump-fragment2.xml";

		// XML Handler for Media Wiki
		MediawikiXmlHandler handler = new MediawikiXmlHandler();

		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		SAXParser saxParser = saxParserFactory.newSAXParser();

		InputStream bais = new FileInputStream(new File(fileName));

		saxParser.parse(bais, handler);

		HashMap<String, WikiPage> pages = handler.getPages();

		System.err.println(pages.keySet());

	}

	public void testGetPages102() throws Exception {

		String fileName = "src/test/resources/nlp4j.wiki/jawiktionary-dump-fragment3.xml";

		// XML Handler for Media Wiki
		MediawikiXmlHandler handler = new MediawikiXmlHandler();

		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		SAXParser saxParser = saxParserFactory.newSAXParser();

		InputStream bais = new FileInputStream(new File(fileName));

		saxParser.parse(bais, handler);

		HashMap<String, WikiPage> pages = handler.getPages();

		System.err.println(pages.keySet());

		WikiPage page = pages.get("11349");

		System.err.println(page);

	}

}
