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
import nlp4j.yhoo_jp.YJpDaServiceResponseHandler;

/**
 * @author Hiroki Oya
 * @version 1.0
 *
 */
public class YJpDaServiceResponseHandlerTestCase extends TestCase {

	public void test011() throws Exception {

		String sentence = "庭には二羽ニワトリがいる。";
		String inFileName = "src/test/resources/nlp4j/yhoo_jp/response_DAService.xml";

		try {
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = saxParserFactory.newSAXParser();
			YJpDaServiceResponseHandler handler = new YJpDaServiceResponseHandler(sentence);

			saxParser.parse(new FileInputStream(inFileName), handler);

			for (KeywordWithDependency kwd : handler.getRoots()) {
				System.err.println("<tostring1>");
				System.err.println(kwd.toStringAsDependencyTree());
				System.err.println("</tostring1>");

				System.err.println("<tostring2>");
				System.err.println(kwd.toStringAsDependencyList());
				System.err.println("</tostring2>");

				System.err.println("<tostring3>");
				System.err.println(kwd.toStringAsXml());
				System.err.println("</tostring3>");
			}
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	public void test012() throws Exception {

		String sentence = "今日はいい天気です。";
		String inFileName = "src/test/resources/nlp4j/yhoo_jp/response_DAService2.xml";

		try {
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = saxParserFactory.newSAXParser();
			YJpDaServiceResponseHandler handler = new YJpDaServiceResponseHandler(sentence);

			saxParser.parse(new FileInputStream(inFileName), handler);

			for (KeywordWithDependency kwd : handler.getRoots()) {
				System.err.println("<tostring1>");
				System.err.println(kwd.toStringAsDependencyTree());
				System.err.println("</tostring1>");

				System.err.println("<tostring2>");
				System.err.println(kwd.toStringAsDependencyList());
				System.err.println("</tostring2>");

				System.err.println("<tostring3>");
				System.err.println(kwd.toStringAsXml());
				System.err.println("</tostring3>");
			}

		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	/**
	 * 複数文のときに複数キーワード返すように仕様変更
	 * 
	 * @throws Exception
	 */
	public void test013() throws Exception {

		String sentence = "今日は走って学校に行きました。明日も学校です。";
		String inFileName = "src/test/resources/nlp4j/yhoo_jp/response_DAService5_multipule_sentences.xml";

		try {
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = saxParserFactory.newSAXParser();
			YJpDaServiceResponseHandler handler = new YJpDaServiceResponseHandler(sentence);

			saxParser.parse(new FileInputStream(inFileName), handler);

			for (KeywordWithDependency kwd : handler.getRoots()) {
				System.err.println("<tostring1>");
				System.err.println(kwd.toStringAsDependencyTree());
				System.err.println("</tostring1>");

				System.err.println("<tostring2>");
				System.err.println(kwd.toStringAsDependencyList());
				System.err.println("</tostring2>");

				System.err.println("<tostring3>");
				System.err.println(kwd.toStringAsXml());
				System.err.println("</tostring3>");
			}

		} catch (Exception e) {
			throw new IOException(e);
		}
	}

}
