/**
 * 
 */
package yhoo_jp;

import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import junit.framework.TestCase;
import nlp4j.yhoo_jp.YJpMaServiceResponseHandler;

/**
 * @author Hiroki Oya
 * @version 1.0
 *
 */
public class YJpMaServiceResponseHandlerTestCase extends TestCase {

	public void test001() throws Exception {

		String inFileName = "src/test/resources/nlp4j/yhoo_jp/response_MAService.xml";

		try {
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = saxParserFactory.newSAXParser();
			YJpMaServiceResponseHandler handler = new YJpMaServiceResponseHandler();

			saxParser.parse(new FileInputStream(inFileName), handler);

		} catch (Exception e) {
			throw new IOException(e);
		}
	}

}
