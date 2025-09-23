package nlp4j.xml;

import org.xml.sax.Attributes;

public abstract class StandardXmlHandler {

	abstract public void startElement(String uri, String localName, String qName, Attributes attributes, String path);

	abstract public void endElement(String uri, String localName, String qName, String path, String text);

}
