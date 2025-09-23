package nlp4j.xml;

import java.io.File;

import org.xml.sax.SAXException;

import junit.framework.TestCase;

public class AbstractXmlHandlerTestCase extends TestCase {

	public void test001() throws Exception {

		File f = new File("src/test/resources/nlp4j.xml/test.xml");

		AbstractXmlHandler hander = new AbstractXmlHandler() {

			@Override
			public void endElement(String uri, String localName, String qName) throws SAXException {
				// TODO Auto-generated method stub
				System.err.println("path: " + super.getPath());

				if ("a/text".equals(super.getPath())) {
					System.err.println("text: " + super.getText().trim());
				}

				super.endElement(uri, localName, qName);
			}

		};

		SAXParserUtils.getNewSAXParser().parse(f, hander);

	}

}
