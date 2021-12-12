package nlp4j.xml;

import java.io.FileInputStream;
import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class AbstractXmlHandlerImplForTest extends AbstractXmlHandler {

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		System.err.println(super.getPath());
		super.endElement(uri, localName, qName);
	}

	static public void main(String[] args) throws Exception {

		String fileName = "src/test/resources/nlp4j.xml/test.xml";

		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		SAXParser saxParser = saxParserFactory.newSAXParser();

		AbstractXmlHandlerImplForTest handler = new AbstractXmlHandlerImplForTest();

		InputStream bais = new FileInputStream(fileName);

		saxParser.parse(bais, handler);

	}

}
