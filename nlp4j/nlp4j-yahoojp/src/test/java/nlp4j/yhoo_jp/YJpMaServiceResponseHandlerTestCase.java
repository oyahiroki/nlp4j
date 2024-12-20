/**
 * 
 */
package nlp4j.yhoo_jp;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import junit.framework.TestCase;
import nlp4j.Keyword;
import nlp4j.yhoo_jp.YJpMaServiceResponseHandler;

/**
 * @author Hiroki Oya
 * @version 1.0
 *
 */
public class YJpMaServiceResponseHandlerTestCase extends TestCase {

	public void test001() throws Exception {

		String sentece = "庭には二羽ニワトリがいる。";
		String inFileName = "src/test/resources/nlp4j.yhoo_jp/response_MAService.xml";

		try {
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = saxParserFactory.newSAXParser();
			YJpMaServiceResponseHandler handler = new YJpMaServiceResponseHandler(sentece);

			saxParser.parse(new FileInputStream(inFileName), handler);

			ArrayList<Keyword> kwds = handler.getKeywords();

			for (Keyword kwd : kwds) {
				System.err.println(kwd);
			}

		} catch (Exception e) {
			throw new IOException(e);
		}
	}

}
