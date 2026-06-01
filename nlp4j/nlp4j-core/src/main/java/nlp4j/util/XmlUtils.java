package nlp4j.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * XML用ユーティリティのクラスです。<br>
 * XML Utility class.
 * 
 * @author Hiroki Oya
 * @version 1.0
 *
 */
public class XmlUtils {

	/**
	 * @param w3cdoc
	 * @return
	 */
	static public String prettyFormatXml(Document w3cdoc) {
		try {

			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			StreamResult result = new StreamResult(new StringWriter());
			DOMSource source = new DOMSource(w3cdoc);
			transformer.transform(source, result);
			String xmlString = result.getWriter().toString();
			String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
			return header + "\n" + xmlString;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Format String as XML / XML形式の文字列をフォーマットします
	 * 
	 * @param xml XML形式の文字列
	 * @return フォーマットされたXML形式の文字列
	 */
	static public String prettyFormatXml(String xml) {
		try {
			Document w3cdoc = toW3CDocument(xml);
			return prettyFormatXml(w3cdoc);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * @param xml
	 * @return
	 * @throws Exception
	 */
	static public Document toW3CDocument(String xml) throws IOException {

		try (InputStream bais = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8))) {
			// be closed safely automatically
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			{ // XXE対策
				factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
				factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
				factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
				factory.setXIncludeAware(false);
				factory.setExpandEntityReferences(false);
			}
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document w3cdoc = builder.parse(bais);
			w3cdoc.setXmlStandalone(true);
			return w3cdoc;
		} catch (ParserConfigurationException | SAXException e) {
			throw new IOException(e);
		}
	}

}
