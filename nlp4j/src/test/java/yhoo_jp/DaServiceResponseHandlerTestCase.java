/**
 * 
 */
package yhoo_jp;

import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import junit.framework.TestCase;
import nlp4j.KeywordWithDependency;
import nlp4j.yhoo_jp.DaServiceResponseHandler;
import nlp4j.yhoo_jp.DaServiceResponseHandler2;
import nlp4j.yhoo_jp.MaServiceResponseHandler;

/**
 * @author oyahiroki
 *
 */
public class DaServiceResponseHandlerTestCase extends TestCase {

	public void test001() throws Exception {

		String inFileName = "src/test/resources/nlp4j/yhoo_jp/response_DAService.xml";

		try {
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = saxParserFactory.newSAXParser();
			DaServiceResponseHandler handler = new DaServiceResponseHandler();

			saxParser.parse(new FileInputStream(inFileName), handler);

			KeywordWithDependency kwd = handler.getRoot();
			System.err.println("<tostring1>");
			System.err.println(kwd.toStringAsDependencyTree());
			System.err.println("</tostring1>");

			System.err.println("<tostring2>");
			System.err.println(kwd.toStringAsDependencyList());
			System.err.println("</tostring2>");
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	public void test011() throws Exception {

		String inFileName = "src/test/resources/nlp4j/yhoo_jp/response_DAService.xml";

		try {
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = saxParserFactory.newSAXParser();
			DaServiceResponseHandler2 handler = new DaServiceResponseHandler2();

			saxParser.parse(new FileInputStream(inFileName), handler);

			KeywordWithDependency kwd = handler.getRoot();
			System.err.println("<tostring1>");
			System.err.println(kwd.toStringAsDependencyTree());
			System.err.println("</tostring1>");

			System.err.println("<tostring2>");
			System.err.println(kwd.toStringAsDependencyList());
			System.err.println("</tostring2>");
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	public void test012() throws Exception {

		String inFileName = "src/test/resources/nlp4j/yhoo_jp/response_DAService2.xml";

		try {
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = saxParserFactory.newSAXParser();
			DaServiceResponseHandler2 handler = new DaServiceResponseHandler2();

			saxParser.parse(new FileInputStream(inFileName), handler);

			KeywordWithDependency kwd = handler.getRoot();
			System.err.println("<tostring1>");
			System.err.println(kwd.toStringAsDependencyTree());
			System.err.println("</tostring1>");

			System.err.println("<tostring2>");
			System.err.println(kwd.toStringAsDependencyList());
			System.err.println("</tostring2>");

			System.err.println("<tostring3>");
			System.err.println(kwd.toStringAsXml());
			System.err.println("</tostring3>");

		} catch (Exception e) {
			throw new IOException(e);
		}
	}

}
