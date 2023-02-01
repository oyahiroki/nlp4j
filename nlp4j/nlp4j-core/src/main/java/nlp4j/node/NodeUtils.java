package nlp4j.node;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

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
	 * To prevent StackOverflow
	 */
	static private int MAX_DEPTH = 10000;

	/**
	 * @param outFile
	 * @param node
	 * @throws IOException
	 */
	public static void print(File outFile, Node<?> node) throws IOException {
		PrintStream ps = new PrintStream(outFile, "UTF-8");
		print(ps, node, "", 0);
	}

	/**
	 * @param ps
	 * @param node
	 */
	static public void print(PrintStream ps, Node<?> node) {
		print(ps, node, "", 0);
	}

	/**
	 * @param ps
	 * @param node
	 * @param parentPath
	 */
	static public void print(PrintStream ps, Node<?> node, String parentPath, int depth) {
		if (depth > MAX_DEPTH) {
			System.err.println("depth is over MAX_DEPTH");
			return;
		}
		String v = node.getValue().toString();
		String p = parentPath + "/" + v;
		ps.println(p);
		if (node.getChildNodes() != null) {
			for (int n = 0; n < node.getChildNodesSize(); n++) {
				Node<?> nn = node.getChildNode(n);
				print(ps, nn, p, (depth + 1));
			}
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

			toW3CNode(doc, node1, node, nodeTagName, 0);

			return doc;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @param doc
	 * @param node1
	 * @param n1
	 * @param nodeTagName
	 */
	static private void toW3CNode(Document doc, org.w3c.dom.Node node1, Node<?> n1, String nodeTagName, int depth) {
		if (depth > MAX_DEPTH) {
			System.err.println("depth is over MAX_DEPTH");
			return;
		}
		((Element) node1).setAttribute("value", n1.getValue().toString());
		if (n1.getChildNodes() != null) {
			for (int n = 0; n < n1.getChildNodesSize(); n++) {
				Node<?> childNode = n1.getChildNode(n);
				org.w3c.dom.Node nodeX = doc.createElement(nodeTagName);
				node1.appendChild(nodeX);
				toW3CNode(doc, nodeX, childNode, nodeTagName, (depth + 1));
			}
		}
	}

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

}
