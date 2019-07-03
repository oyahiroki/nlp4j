/**
 * 
 */
package nlp4j.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

/**
 * @author oyahiroki
 *
 */
public class XmlUtils {

	static public String prettyFormatXml(String xml) {
		try (InputStream bais = new ByteArrayInputStream(xml.getBytes("utf-8"))) {
			// be closed safely automatically
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(bais);
			doc.setXmlStandalone(true);

			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			StreamResult result = new StreamResult(new StringWriter());
			DOMSource source = new DOMSource(doc);
			transformer.transform(source, result);
			String xmlString = result.getWriter().toString();
			String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
			return header + "\n" + xmlString;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return xml;

	}

}
