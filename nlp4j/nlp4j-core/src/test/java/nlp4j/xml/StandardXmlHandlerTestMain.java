package nlp4j.xml;

import java.io.File;

import org.xml.sax.Attributes;

public class StandardXmlHandlerTestMain {

	public static void main(String[] args) throws Exception {
		File f = new File("src/test/resources/nlp4j.xml/test.xml");
		StandardXmlHandler h = new StandardXmlHandler() {

			@Override
			public void startElement(String uri, String localName, String qName, Attributes attributes, String path) {
				System.err.println("start path: " + path);
			}

			@Override
			public void endElement(String uri, String localName, String qName, String path, String text) {
				System.err.println("end path: " + path);
				System.err.println("text:" + text.trim());
			}

		};
		SAXParserUtils.parse(f, h);
	}

}
