package nlp4j.wiki;

import java.io.File;

import javax.xml.XMLConstants;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import junit.framework.TestCase;

public class MediawikiXmlHandler3TestCase extends TestCase {

	public void test001() throws Exception {
		File xmlFile = new File("src/test/resources/nlp4j.wiki/jawiki-20220501-pages-articles-multistream-2170292.xml");
		WikiPageHandler h = new WikiPageHandler() {
			@Override
			public void read(WikiPage page) throws BreakException {
				System.err.println(page);
				System.err.println(page.getXml());
			}
		};
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		{ // 2022-08-01
			// to prevent org.xml.sax.SAXParseException; lineNumber: 111758337;
			// columnNumber: 66; JAXP00010004:
			// エンティティの累積サイズ"50,000,001"は、"FEATURE_SECURE_PROCESSING"で設定された制限"50,000,000"を超えました。
			saxParserFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, false);
		}
		SAXParser saxParser = saxParserFactory.newSAXParser();
		// XML Handler for Media Wiki
		MediawikiXmlHandler3 handler = new MediawikiXmlHandler3();
		handler.setWikiPageHander(h);
		saxParser.parse(xmlFile, handler);
	}

	public void test002() throws Exception {
		File xmlFile = new File("src/test/resources/nlp4j.wiki/jawiki-20220501-pages-articles-multistream-255425.xml");
		WikiPageHandler h = new WikiPageHandler() {
			@Override
			public void read(WikiPage page) throws BreakException {
				System.err.println(page);
				System.err.println(page.getTitle());
				System.err.println(page.getPlainText());
			}
		};
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		{ // 2022-08-01
			// to prevent org.xml.sax.SAXParseException; lineNumber: 111758337;
			// columnNumber: 66; JAXP00010004:
			// エンティティの累積サイズ"50,000,001"は、"FEATURE_SECURE_PROCESSING"で設定された制限"50,000,000"を超えました。
			saxParserFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, false);
		}
		SAXParser saxParser = saxParserFactory.newSAXParser();
		// XML Handler for Media Wiki
		MediawikiXmlHandler3 handler = new MediawikiXmlHandler3();
		handler.setWikiPageHander(h);
		saxParser.parse(xmlFile, handler);
	}

}
