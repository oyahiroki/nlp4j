package nlp4j.util;

import junit.framework.TestCase;

/**
 * @author Hiroki Oya
 * @version 1.0
 *
 */
public class XmlUtilsTestCase extends TestCase {

	public void testPrettyFormatXml() {
		String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><xml><head></head><body><h1>TEST</h1></body></xml>";
		String xml = XmlUtils.prettyFormatXml(xmlString);

		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" //
				+ "<xml>\r\n" //
				+ "    <head/>\r\n" //
				+ "    <body>\r\n" //
				+ "        <h1>TEST</h1>\r\n" //
				+ "    </body>\r\n" //
				+ "</xml>\r\n";

		System.err.println(xml);

		System.err.println(xml.length());
		System.err.println(expected.length());

		for (int n = 0; n < xml.length(); n++) {
			char c1 = xml.charAt(n);
			char c2 = expected.charAt(n);
			System.err.println("c1=" + c1 + ",c2=" + c2 + "," + (c1 == c2));
		}

		assertEquals(expected, xml);
	}

}
