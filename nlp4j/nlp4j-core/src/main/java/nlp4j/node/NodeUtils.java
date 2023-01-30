package nlp4j.node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import nlp4j.util.XmlUtils;

/**
 * created at: 2023-01-30
 * 
 * @author Hiroki Oya
 *
 */
public class NodeUtils {

	/**
	 * @param node
	 * @return
	 */
	static public String toXmlString(Node<?> node) {
		Document w3cdoc = toW3CDocument(node);
		try {
			return XmlUtils.prettyFormatXml(w3cdoc);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * @param node
	 * @return
	 */
	static public Document toW3CDocument(Node<?> node) {
		return toW3CDocument(node, "node");
	}

	/**
	 * @param node
	 * @param nodeTagName
	 * @return
	 */
	static public Document toW3CDocument(Node<?> node, String nodeTagName) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			factory.setValidating(false);

			Document doc = builder.newDocument();
			doc.setXmlStandalone(true);

			org.w3c.dom.Node node1 = doc.createElement(nodeTagName);
			doc.appendChild(node1);

			toW3CNode(doc, node1, node, nodeTagName);

			return doc;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	static private void toW3CNode(Document doc, org.w3c.dom.Node node1, Node<?> n1, String nodeTagName) {
		((Element) node1).setAttribute("value", n1.getValue().toString());
		if (n1.getChildNodes() != null) {
			for (int n = 0; n < n1.getChildNodesSize(); n++) {
				Node<?> childNode = n1.getChildNode(n);
				org.w3c.dom.Node nodeX = doc.createElement(nodeTagName);
				node1.appendChild(nodeX);
				toW3CNode(doc, nodeX, childNode, nodeTagName);
			}
		}
	}

}
