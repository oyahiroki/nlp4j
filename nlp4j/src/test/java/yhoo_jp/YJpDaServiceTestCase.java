package yhoo_jp;

import java.io.IOException;

import junit.framework.TestCase;
import nlp4j.KeywordWithDependency;
import nlp4j.yhoo_jp.YJpDaService;

/**
 * @author Hiroki Oya
 * @version 1.0
 *
 */
public class YJpDaServiceTestCase extends TestCase {

	public void testDaService() {
		new YJpDaService();
	}

	public void testGetKeywords001() throws IOException {
		String text = "今日はいい天気です";
		YJpDaService service = new YJpDaService();
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
		assertNotNull(kwd);
	}

	public void testGetKeywords002() throws IOException {
		// 着る: 一段
		String text = "ボールを美しく蹴る";
		YJpDaService service = new YJpDaService();
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
		assertNotNull(kwd);
	}

	public void testGetKeywords501() throws IOException {
		String text = " ";
		YJpDaService service = new YJpDaService();
		KeywordWithDependency kwd = service.getKeywords(text);
		assertNull(kwd);
	}

	public void testGetKeywords502() throws IOException {
		String text = " ";
		YJpDaService service = new YJpDaService();
		KeywordWithDependency kwd = service.getKeywords(text);
		assertNull(kwd);
	}

	public void testGetKeywords503() throws IOException {
		String text = null;
		YJpDaService service = new YJpDaService();
		KeywordWithDependency kwd = service.getKeywords(text);
		assertNull(kwd);
	}

}
