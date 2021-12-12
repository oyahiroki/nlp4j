package nlp4j.xml;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class SAXParserUtils {

	/**
	 * @return SAXParser that ignores External DTD
	 */
	static public SAXParser getNewSAXParser() {

		try {
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = saxParserFactory.newSAXParser(); // throws SaxException

			// https://stackoverflow.com/questions/10257576/how-to-ignore-inline-dtd-when-parsing-xml-file-in-java

			XMLReader reader = saxParser.getXMLReader();// throws SaxException
			reader.setFeature("http://xml.org/sax/features/validation", false); // throws NotSaxSupportedException
			reader.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
			reader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
			reader.setFeature("http://xml.org/sax/features/external-general-entities", false);
			reader.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
			reader.setFeature("http://xml.org/sax/features/use-entity-resolver2", false);
			reader.setFeature("http://apache.org/xml/features/validation/unparsed-entity-checking", false);
			reader.setFeature("http://xml.org/sax/features/resolve-dtd-uris", false);
			reader.setFeature("http://apache.org/xml/features/validation/dynamic", false);
			reader.setFeature("http://apache.org/xml/features/validation/schema/augment-psvi", false);

			return saxParser;
		} catch (SAXException e) {
			e.printStackTrace();
			return null;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return null;
		}

	}

}
