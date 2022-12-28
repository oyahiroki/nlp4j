package nlp4j.wiki;

import java.io.File;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * crated_at: 2022-12-09
 * 
 * @author Hiroki Oya
 *
 */
public class WikiPageReader {

	/**
	 * @param xmlFile of Wiki Page
	 * @return Text in Wiki Format
	 * @throws IOException on error
	 */
	public WikiPage readWikiPageXmlFile(File xmlFile) throws IOException {
		try {
			WikiPageHandlerReader h = new WikiPageHandlerReader();
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			saxParserFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
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

			return h.getPage();

		} catch (Exception e) {
			throw new IOException(e);
		}
	}

}
