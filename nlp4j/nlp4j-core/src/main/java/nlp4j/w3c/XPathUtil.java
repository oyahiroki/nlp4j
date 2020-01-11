package nlp4j.w3c;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XPathUtil {

	static Number evaluateAsNumber(Document document, String xpathExpression) {
		XPath xpath = XPathFactory.newInstance().newXPath();
		try {
			// NUMBER, STRING, BOOLEAN, NODE or NODESET
			Number num = (Number) xpath.evaluate(xpathExpression, document, XPathConstants.NUMBER);
			return num;
		} catch (XPathExpressionException e) {
			e.printStackTrace();
			return null;
		}
	}

	static String evaluateAsString(Document document, String xpathExpression) {
		XPath xpath = XPathFactory.newInstance().newXPath();
		try {
			// NUMBER, STRING, BOOLEAN, NODE or NODESET
			String s = (String) xpath.evaluate(xpathExpression, document, XPathConstants.STRING);
			return s;
		} catch (XPathExpressionException e) {
			e.printStackTrace();
			return null;
		}
	}

	static Boolean evaluateAsBoolean(Document document, String xpathExpression) {
		XPath xpath = XPathFactory.newInstance().newXPath();
		try {
			// NUMBER, STRING, BOOLEAN, NODE or NODESET
			Boolean b = (Boolean) xpath.evaluate(xpathExpression, document, XPathConstants.BOOLEAN);
			return b;
		} catch (XPathExpressionException e) {
			e.printStackTrace();
			return null;
		}
	}

	static Node evaluateAsNode(Document document, String xpathExpression) {
		XPath xpath = XPathFactory.newInstance().newXPath();
		try {
			// NUMBER, STRING, BOOLEAN, NODE or NODESET
			Node node = (Node) xpath.evaluate(xpathExpression, document, XPathConstants.NODE);
			return node;
		} catch (XPathExpressionException e) {
			e.printStackTrace();
			return null;
		}
	}

	static NodeList evaluateAsNodeSet(Document document, String xpathExpression) {
		XPath xpath = XPathFactory.newInstance().newXPath();
		try {
			// NUMBER, STRING, BOOLEAN, NODE or NODESET
			NodeList nodeset = (NodeList) xpath.evaluate(xpathExpression, document, XPathConstants.NODESET);
			return nodeset;
		} catch (XPathExpressionException e) {
			e.printStackTrace();
			return null;
		}
	}

}
