package yhoo_jp;

import java.io.IOException;

import junit.framework.TestCase;
import nlp4j.KeywordWithDependency;
import nlp4j.yhoo_jp.DaService;

/**
 * @author Hiroki Oya
 * @version 1.0
 *
 */
public class DaServiceTestCase extends TestCase {

	public void testDaService() {
		DaService service = new DaService();
	}

	public void testGetKeywords001() throws IOException {
		String text = "今日はいい天気です";
		DaService service = new DaService();
		KeywordWithDependency kwd = service.getKeywords(text);
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

	public void testGetKeywords002() throws IOException {
		// 着る: 一段
		String text = "ボールを美しく蹴る";
		DaService service = new DaService();
		KeywordWithDependency kwd = service.getKeywords(text);
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

}
