package yhoo_jp;

import java.io.IOException;

import junit.framework.TestCase;
import nlp4j.KeywordWithDependency;
import nlp4j.yhoo_jp.DaService;

public class DaServiceTestCase extends TestCase {

	public void testDaService() {
		fail("Not yet implemented");
	}

	public void testGetKeywords001() throws IOException {
		String text = "今日は走って学校に行った。";
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
