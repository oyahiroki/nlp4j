package nlp4j.yhoo_jp;

import java.io.IOException;
import java.util.ArrayList;

import junit.framework.TestCase;
import nlp4j.Keyword;
import nlp4j.KeywordWithDependency;
import nlp4j.impl.DefaultNlpServiceResponse;
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
		for (Keyword kwd : service.getKeywords(text)) {
			if (kwd instanceof KeywordWithDependency) {
				KeywordWithDependency k = (KeywordWithDependency) kwd;
				System.err.println("<tostring1>");
				System.err.println(k.toStringAsDependencyTree());
				System.err.println("</tostring1>");
				System.err.println("<tostring2>");
				System.err.println(k.toStringAsDependencyList());
				System.err.println("</tostring2>");
				System.err.println("<tostring3>");
				System.err.println(k.toStringAsXml());
				System.err.println("</tostring3>");
				assertNotNull(k);
			}
		}
	}

	public void testGetKeywords002() throws IOException {
		// 着る: 一段
		String text = "ボールを美しく蹴る";
		YJpDaService service = new YJpDaService();
		for (Keyword kwd : service.getKeywords(text)) {
			if (kwd instanceof KeywordWithDependency) {
				KeywordWithDependency k = (KeywordWithDependency) kwd;
				System.err.println("<tostring1>");
				System.err.println(k.toStringAsDependencyTree());
				System.err.println("</tostring1>");
				System.err.println("<tostring2>");
				System.err.println(k.toStringAsDependencyList());
				System.err.println("</tostring2>");
				System.err.println("<tostring3>");
				System.err.println(k.toStringAsXml());
				System.err.println("</tostring3>");
				assertNotNull(k);
			}
		}
	}

	public void testGetKeywords003() throws IOException {
		// 「モーニング娘。」の扱いを確認する
		String text = "モーニング娘。のコンサートに行きました。";
		YJpDaService service = new YJpDaService();
		DefaultNlpServiceResponse res = service.process(text);

		System.err.println(res.getOriginalResponseBody());
	}

	public void testGetKeywords501() throws IOException {
		String text = " ";
		YJpDaService service = new YJpDaService();
		ArrayList<KeywordWithDependency> kwds = service.getKeywords(text);
		assertNull(kwds);
	}

	public void testGetKeywords502() throws IOException {
		String text = " ";
		YJpDaService service = new YJpDaService();
		ArrayList<KeywordWithDependency> kwds = service.getKeywords(text);
		assertNull(kwds);
	}

	public void testGetKeywords503() throws IOException {
		String text = null;
		YJpDaService service = new YJpDaService();
		ArrayList<KeywordWithDependency> kwds = service.getKeywords(text);
		assertNull(kwds);
	}

}
