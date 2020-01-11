package nlp4j.w3c;

import org.w3c.dom.Document;

import junit.framework.TestCase;

public class NodePrinterTestCase extends TestCase {

	public void test001() throws Exception {

		String xmlString = "<w id='0' pos=\"VB\">\n" + //
				"	<w id='1' relation=\"nsubj\" />\n" + //
				"	<w id='2' relation=\"nsubj\">\n" + //
				"		<w id='3' relation=\"nsubj\" />\n" + //
				"		<w id='4' relation=\"nsubj\" />\n" + //
				"	</w>\n" + //
				"</w>";//

		Document w3Doc = DocumentUtil.parse(xmlString);

		NodePrinter.print(w3Doc);

	}

}
