package nlp4j.w3c;

import org.w3c.dom.Node;

/**
 * Node printer utility
 * 
 * @author Hiroki Oya
 *
 */
public class NodePrinter {

	static public void print(Node node) {
		print(node, 0);
	}

	static protected void print(Node node, int depth) {
		StringBuilder sb = new StringBuilder();
		print(sb, node, depth);
		System.err.println(sb);
	}

	static protected void print(StringBuilder sbb, Node node, int depth) {
		if (node == null) {
			return;
		} //
		else if (node.getNodeType() == Node.DOCUMENT_NODE) {
			for (int n = 0; n < node.getChildNodes().getLength(); n++) {
				Node item = node.getChildNodes().item(n);
				print(sbb, item, depth);
			}
			return;
		} //
		else if (node.getNodeType() == Node.ELEMENT_NODE) {
			// TAB Intend
			for (int n = 0; n < depth; n++) {
				sbb.append("\t");
			}
			StringBuilder sb = new StringBuilder();
			if (node.getAttributes() != null) {
				for (int n = 0; n < node.getAttributes().getLength(); n++) {
					Node item = node.getAttributes().item(n);
					sb.append(" " + item.getNodeName() + "=\"" + item.getNodeValue() + "\"");
				}
			}
			sbb.append("<" + node.getNodeName() + sb.toString() + " />" + " " + Integer.toHexString(node.hashCode())
					+ "\n");
		}
		for (int n = 0; n < node.getChildNodes().getLength(); n++) {
			Node item = node.getChildNodes().item(n);
			print(sbb, item, depth + 1);
		}
	}
}
