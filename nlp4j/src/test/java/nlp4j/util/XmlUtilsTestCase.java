package nlp4j.util;

import junit.framework.TestCase;

public class XmlUtilsTestCase extends TestCase {

	public void testPrettyFormatXml() {
		String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><xml><head></head><body><h1>TEST</h1></body></xml>";
		String xml = XmlUtils.prettyFormatXml(xmlString);
		System.err.println(xml);
	}

}
