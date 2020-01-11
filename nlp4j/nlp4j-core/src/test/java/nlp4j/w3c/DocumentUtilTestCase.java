package nlp4j.w3c;

import java.io.IOException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import junit.framework.TestCase;

public class DocumentUtilTestCase extends TestCase {

	public void testParseString001() throws IOException {
		String xmlString = "<w id='0' pos=\"VB\">\n" + //
				"	<w id='1' relation=\"nsubj\" />\n" + //
				"	<w id='2' relation=\"nsubj\">\n" + //
				"		<w id='3' relation=\"nsubj\" />\n" + //
				"		<w id='4' relation=\"nsubj\" />\n" + //
				"	</w>\n" + //
				"</w>";//

		Document w3Doc = DocumentUtil.parse(xmlString);

		System.err.println(w3Doc.getFirstChild().getFirstChild().getNodeType() == Node.ELEMENT_NODE);
		System.err.println(w3Doc.getFirstChild().getFirstChild().getNodeType() == Node.TEXT_NODE);

		System.err.println(w3Doc.getFirstChild().getFirstChild().getNextSibling().getNodeType() == Node.ELEMENT_NODE);
		System.err.println(w3Doc.getFirstChild().getFirstChild().getNextSibling().getNodeType() == Node.TEXT_NODE);

		NodePrinter.print(w3Doc);
	}

}
