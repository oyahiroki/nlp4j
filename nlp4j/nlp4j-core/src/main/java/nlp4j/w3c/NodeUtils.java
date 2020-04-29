package nlp4j.w3c;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class NodeUtils {

	static public Node removeFirstElementChild(Node node) {
		Node ptr = getFirstElementChild(node);
		if (ptr == null) {
			return null;
		} //
		else {
			return node.removeChild(ptr);
		}
	}

	/**
	 * Elementである子ノードを返す（テキストは返さない）
	 * 
	 * @param node
	 * @return Pointer not clone
	 */
	static public Node getFirstElementChild(Node node) {
		for (int n = 0; n < node.getChildNodes().getLength(); n++) {
			Node item = node.getChildNodes().item(n);
			if (item.getNodeType() == Node.ELEMENT_NODE) {
				return item;
			}
		}
		return null;
	}

	static int getElementSize(Node node) {
//		System.err.println("debug:" + node.getNodeType());
		if (node == null) {
			return 0;
		} //
		else if (node.getNodeType() == Node.DOCUMENT_NODE) {
			return getElementSize(node.getFirstChild());
		} //
			// 1
		else if (node.getNodeType() == Node.ELEMENT_NODE) {
			if (node.getChildNodes() == null) {
				return 1;
			} else {
				int x = 1;
				for (int n = 0; n < node.getChildNodes().getLength(); n++) {
					Node item = node.getChildNodes().item(n);
					x += getElementSize(item);
				}
				return x;
			}
		} //
			// 3
		else if (node.getNodeType() == Node.TEXT_NODE) {
			return 0;
		} else {
			return 0;
		}
	}

	static public Node getDeepestUpperElement(Node node) {
		if (node == null) {
			return null;
		}
		if (node.getChildNodes() == null) {
			return node;
		} else {
			Node n = getFirstElementChild(node);
			if (n != null) {
				return getDeepestUpperElement(n);
			} else {
				return node;
			}
		}
	}

	static public List<Node> forEachChildNodes(Node n) {
		ArrayList<Node> list = new ArrayList<>();
		forEachChildNodes(n, list);
		return list;
	}

	static protected void forEachChildNodes(Node n, List<Node> node) {
		NodeList condNodeChildList = n.getChildNodes();
		if (condNodeChildList != null) {
			for (int x = 0; x < condNodeChildList.getLength(); x++) {
				Node condChildNode = condNodeChildList.item(x);
				if (condChildNode.getNodeType() == Node.ELEMENT_NODE) {
					// not clone
					node.add(condChildNode);
				}
			}
		}
	}

	static public List<Node> forEachNodes(Node n) {
		return forEachNodes(n, true);
	}

	static protected List<Node> forEachNodes(Node n, boolean returnClone) {
		ArrayList<Node> list = new ArrayList<>();
		forEachNodes(n, list, returnClone);
		return list;
	}

	static protected void forEachNodes(Node n, List<Node> node, boolean addClone) {
		if (n.getNodeType() == Node.ELEMENT_NODE) {
			if (addClone) {
				node.add(n.cloneNode(true));
			} else {
				node.add(n);
			}
		}
		NodeList condNodeChildList = n.getChildNodes();
		if (condNodeChildList != null) {
			for (int x = 0; x < condNodeChildList.getLength(); x++) {
				Node condChildNode = condNodeChildList.item(x);
				forEachNodes(condChildNode, node, addClone);
			}
		}
	}

	static public List<Node> forEachPatternNodes2(Node n) {
		ArrayList<Node> list = new ArrayList<>();
		list.add(n.cloneNode(true));
		forEachPatternNodes2(n.cloneNode(true), list);
		return list;
	}

	static protected void forEachPatternNodes2(Node node, List<Node> list) {
//		System.err.println("node");
//		NodePrinter.print(node);
		while (true) {
			Node ptr = getDeepestUpperElement(node);
//			System.err.println("ptr");
//			NodePrinter.print(ptr);
			if (ptr == node) {
				break;
			} else if (ptr == null) {
				break;
			} else {
//				System.err.println("removing..");
				ptr.getParentNode().removeChild(ptr);
				if (getElementSize(node) > 0) {
					list.add(node.cloneNode(true));
				}
			}
		}
	}

	static public List<Node> forEachPatternNodes(Node n) {
		ArrayList<Node> list = new ArrayList<>();
		forEachPatternNodes(n, list);
		return list;
	}

	static protected void forEachPatternNodes(Node n, List<Node> node) {

		// 各ノード
		List<Node> list = forEachNodes(n, true);

		node.addAll(list);

		for (Node nn : list) {
			Node clone = nn.cloneNode(true);
			Node ptr = NodeUtils.removeFirstElementChild(clone);
			while (ptr != null) {
				node.add(clone.cloneNode(true));
				ptr = NodeUtils.removeFirstElementChild(clone);
			}
		}

	}

	/**
	 * ルートのNodeを文字列表現を返します。
	 * 
	 * @param singleNode
	 * @return Nodeの文字列表現
	 */
	public static String toStringAsSingleNode(Node singleNode) {
		StringBuilder sbAttributes = new StringBuilder();
		if (singleNode.getAttributes() != null) {
			for (int n = 0; n < singleNode.getAttributes().getLength(); n++) {
				Node item = singleNode.getAttributes().item(n);
				sbAttributes.append(" " + item.getNodeName() + "=\"" + item.getNodeValue() + "\"");
			}
		}
		return "<" + singleNode.getNodeName() + sbAttributes.toString() + " />" + " "
				+ Integer.toHexString(singleNode.hashCode());
	}

	/**
	 * 文字列表現を返します。
	 * 
	 * @param nodeSet
	 * @return 文字列表現
	 */
	public static String toString(Set<Node> nodeSet) {
		StringBuilder sb = new StringBuilder();
		Iterator<Node> itr = nodeSet.iterator();
		while (itr.hasNext()) {
			sb.append("[");
			sb.append(NodeUtils.toStringAsSingleNode(itr.next()));
			sb.append("]");
		}
		return sb.toString();
	}

	/**
	 * 文字列表現を返します。
	 * 
	 * @param nodeList
	 * @return 文字列表現
	 */
	static public String toString(List<Node> nodeList) {
		StringBuilder sb = new StringBuilder();
		for (Node n : nodeList) {
			sb.append("[");
			sb.append(toStringAsSingleNode(n));
			sb.append("]");
		}
		return sb.toString();
	}

	/**
	 * 子Nodeも含めて文字列表現を返します。
	 * 
	 * @param node
	 * @return 文字列表現
	 */
	static public String toString(Node node) {
		StringBuilder sb = new StringBuilder();
		NodePrinter.print(sb, node, 0);
		return sb.toString();
	}

}
