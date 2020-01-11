package nlp4j.w3c;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import junit.framework.TestCase;

public class XPathUtilTestCase extends TestCase {

	// NUMBER, STRING, BOOLEAN, NODE or NODESET

	public void testEvaluateAsNumber001() throws Exception {
		String xmlString = "<w id='0' lex=\"A\">\n" + //
				"	<w id='1' lex=\"B\" />\n" + //
				"	<w id='2' lex=\"C\">\n" + //
				"		<w id='3' lex=\"D\" />\n" + //
				"		<w id='4' lex=\"E\" />\n" + //
				"	</w>\n" + //
				"</w>";//

		Document w3Doc = DocumentUtil.parse(xmlString);

		Number s = XPathUtil.evaluateAsNumber(w3Doc, "/w/@id");
		int n = s.intValue();
		assertNotNull(s);
		System.err.println(s);
		System.err.println(n);
	}

	public void testEvaluateAsString001() throws Exception {
		String xmlString = "<w id='0' lex=\"A\">\n" + //
				"	<w id='1' lex=\"B\" />\n" + //
				"	<w id='2' lex=\"C\">\n" + //
				"		<w id='3' lex=\"D\" />\n" + //
				"		<w id='4' lex=\"E\" />\n" + //
				"	</w>\n" + //
				"</w>";//

		Document w3Doc = DocumentUtil.parse(xmlString);

		String s = XPathUtil.evaluateAsString(w3Doc, "/w/@pos");
		assertNotNull(s);
		System.err.println(s);
	}

	public void testEvaluateAsBoolean001() throws Exception {
		String xmlString = "<w id='0' lex=\"A\">\n" + //
				"	<w id='1' lex=\"B\" />\n" + //
				"	<w id='2' lex=\"C\">\n" + //
				"		<w id='3' lex=\"D\" b=\"true\" />\n" + //
				"		<w id='4' lex=\"E\" />\n" + //
				"	</w>\n" + //
				"</w>";//

		Document w3Doc = DocumentUtil.parse(xmlString);

		Boolean b = XPathUtil.evaluateAsBoolean(w3Doc, "//w[@lex='D']/@b");
		assertNotNull(b);
		assertTrue(b);
		System.err.println(b);
	}

	public void testEvaluateAsNode001() throws Exception {
		String xmlString = "<w id='0' lex=\"A\">\n" + //
				"	<w id='1' lex=\"B\" />\n" + //
				"	<w id='2' lex=\"C\">\n" + //
				"		<w id='3' lex=\"D\" />\n" + //
				"		<w id='4' lex=\"E\" />\n" + //
				"	</w>\n" + //
				"</w>";//

		Document w3Doc = DocumentUtil.parse(xmlString);

		Node s = XPathUtil.evaluateAsNode(w3Doc, "/w/@lex");
		assertNotNull(s);
		System.err.println(s.getNodeName());
		System.err.println(s.getNodeValue());
	}

	public void testEvaluateAsNodeList001() throws Exception {
		String xmlString = "<w id='0' lex=\"A\">\n" + //
				"	<w id='1' lex=\"B\" />\n" + //
				"	<w id='2' lex=\"C\">\n" + //
				"		<w id='3' lex=\"D\" />\n" + //
				"		<w id='4' lex=\"E\" />\n" + //
				"	</w>\n" + //
				"</w>";//

		Document w3Doc = DocumentUtil.parse(xmlString);

		NodeList s = XPathUtil.evaluateAsNodeSet(w3Doc, "//w");
		assertNotNull(s);

		for (int n = 0; n < s.getLength(); n++) {
			Node node = s.item(n);
			System.err.println(node.getNodeName());
			System.err.println(node.getAttributes().getNamedItem("id").getNodeValue());
		}

	}
}
