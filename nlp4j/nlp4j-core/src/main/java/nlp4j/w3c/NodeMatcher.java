package nlp4j.w3c;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class NodeMatcher {

	@SuppressWarnings("unused")
	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	public static List<Node> getHitNodes(Node target, Node condition) {

		List<Node> ret = new ArrayList<>();
		List<Node> targetNodes = NodeUtils.forEachPatternNodes2(target);

		for (Node targetNode : targetNodes) {
			Node targetClone = targetNode.cloneNode(true);
			Node conditionClone = condition.cloneNode(true);
			Set<Node> used = new HashSet<Node>();
			Set<Node> hit = new HashSet<Node>();
			boolean b = match(targetClone, conditionClone, used, hit, false);
			if (b == true) {
				boolean contain = false;
				for (Node n : ret) {
					if (match(n.cloneNode(true), targetClone.cloneNode(true), true)) {
						contain = true;
						break;
					}
				}
				if (contain == false) {
					ret.add(targetClone);
				}
			}
		}

		return ret;
	}

	public static boolean match(Node target, Node condition) {
		for (Node n : NodeUtils.forEachPatternNodes(target)) {
//			System.err.println("debug: target:" + NodeUtils.toString(n));
//			System.err.println("debug: condition:" + NodeUtils.toString(condition));
			Set<Node> used = new HashSet<Node>();
			Set<Node> hit = new HashSet<Node>();
			if (match(n, condition, used, hit, false, false)) {
				return true;
			}
		}
		return false;
	}

	public static boolean match(Node target, Node condition, boolean ignoreID) {
		for (Node n : NodeUtils.forEachPatternNodes(target)) {
			Set<Node> used = new HashSet<Node>();
			Set<Node> hit = new HashSet<Node>();
			if (match(n, condition, used, hit, false, ignoreID)) {
				return true;
			}
		}
		return false;
	}

	public static boolean match(Node target, Node condition, Set<Node> used, Set<Node> hit, boolean def) {
		for (Node n : NodeUtils.forEachPatternNodes(target)) {
			if (match(n, condition, used, hit, false, false)) {
				return true;
			}
		}
		return false;
	}

	public static boolean matchAsSingleNode(Node target, Node condition, boolean ignoreID) {
		if (target == null || condition == null) {
			return false;
		} //
		else if (target.getNodeType() == Node.DOCUMENT_NODE) {
			return matchAsSingleNode(target.getFirstChild(), condition, ignoreID);
		} //
		else if (condition.getNodeType() == Node.DOCUMENT_NODE) {
			return matchAsSingleNode(target, condition.getFirstChild(), ignoreID);
		} //
		else if (target.getNodeType() != Node.ELEMENT_NODE) {
			return matchAsSingleNode(target.getNextSibling(), condition, ignoreID);
		} //
		else if (condition.getNodeType() != Node.ELEMENT_NODE) {
			return matchAsSingleNode(target, condition.getNextSibling(), ignoreID);
		} //
		else {
			NamedNodeMap condAttributes = condition.getAttributes();
			// IF(condAttributes == null)
			if (condAttributes == null) {
				return true;
			} //
				// ELSE(condAttributes != null)
			else {
				// for each condition attribute (for example 'lex')
				for (int n = 0; n < condAttributes.getLength(); n++) {
					Node condAttribute = condAttributes.item(n);
					String condAttName = condAttribute.getNodeName();
					String condAttValue = condAttribute.getNodeValue();
					// ignoreID == false のとき id はチェックしない / no check for 'id'
					// _ はチェックしない / no check for '_*'
					if (ignoreID == false && condAttName.equals("id")) {
						continue;
					} //
					else if (condAttName.startsWith("_")) {
						continue;
					} //
					else {
						// IF(target.getAttributes() != null)
						if (target.getAttributes() != null) {
							Node t = target.getAttributes().getNamedItem(condAttName);
							String value2 = t.getNodeValue();
							// 異なる値であれば false
							if (value2.equals(condAttValue) == false) {
								return false;
							}
						}
						// ELSE(target.getAttributes() != null)
						else {
							return false;
						}
					}
				} // end of for each condition attribute
					// 条件の属性について全てチェック完了
				String idval = condAttributes.getNamedItem("id").getNodeValue();
				((Element) target).setAttribute("_hit", idval);
				return true;
			}
		}
	}

	public static boolean match(Node target, Node condition, Set<Node> used, Set<Node> hit, boolean def,
			boolean ignoreID) {
		if (target == null) {
			throw new RuntimeException("no null");
		}
		if (condition == null) {
			throw new RuntimeException("no null");
		}
		if (used.contains(condition)) {
			return def;
		}
		if (target.getNodeType() == Node.DOCUMENT_NODE) {
			return match(NodeUtils.getFirstElementChild(target), condition, used, hit, def);
		}
		if (condition.getNodeType() == Node.DOCUMENT_NODE) {
			return match(target, NodeUtils.getFirstElementChild(condition), used, hit, def);
		}

		if (target.getNodeType() != Node.ELEMENT_NODE) {
			return match(target.getNextSibling(), condition, used, hit, def);
		}
		if (condition.getNodeType() != Node.ELEMENT_NODE) {
			if (condition.getNextSibling() != null) {
				return match(target, condition.getNextSibling(), used, hit, def);
			} else {
				return def;
			}
		}

//		NamedNodeMap condAttributes = condition.getAttributes();
//		if (condAttributes != null) {
//			// for each condition attribute (for example 'lex')
//			for (int n = 0; n < condAttributes.getLength(); n++) {
//				Node condAttribute = condAttributes.item(n);
//				String condAttName = condAttribute.getNodeName();
//				String condAttValue = condAttribute.getNodeValue();
//				// ignoreID == false のとき id はチェックしない / no check for 'id'
//				// _ はチェックしない / no check for '_*'
//				if (ignoreID == false && condAttName.equals("id")) {
//					continue;
//				} //
//				else if (condAttName.startsWith("_")) {
//					continue;
//				} //
//				else {
//					if (target.getAttributes() != null) {
//						Node t = target.getAttributes().getNamedItem(condAttName);
//						String value2 = t.getNodeValue();
//						// 異なる値であれば false
//						if (value2.equals(condAttValue) == false) {
//							return false;
//						}
//					} else {
//						return false;
//					}
//				}
//			} // end of for each condition attribute
//				// 条件の属性について全てチェック完了
//			String idval = condAttributes.getNamedItem("id").getNodeValue();
//			if (idval.equals("0") == false) {
//				System.err.println("debug");
//			}
//			((Element) target).setAttribute("_hit", idval);
//
//			hit.add(target);
//			used.add(condition);
//
////			System.err.println("_hit = " + idval);
////			System.err.println("debug: hit: " + idval);
//
//		} else {
//		}

		{
			boolean b = matchAsSingleNode(target, condition, ignoreID);
			if (b == false) {
				return false;
			} else {
				hit.add(target);
				used.add(condition);
			}
		}

//		System.err.println("size: " + NodeUtils.forEachChildNodes(condition).size());

		// 条件ノードの子ノードについてチェックする
		// condNodeChild
		// for each child nodes of condition node
		for (Node condChildNode : NodeUtils.forEachChildNodes(condition)) {
			boolean b = false;
			int idx = 0;
			for (Node targetChildNode : NodeUtils.forEachChildNodes(target)) {
				// どれか一つでも当たればOK
				b = match(targetChildNode, condChildNode, used, hit, def);
				if (idx == 0 && b == false) {
					// 最初の子ノードは必ずヒットしなければならない
					// 子ノードをカットしながら調べるので問題ない
					return false;
				}
				if (b == true) {
//					hit.add(targetChildNode);
//					used.add(condChildNode);
					break;
				} //
				idx++;
			}
			if (b == false) {
				return false;
			}
		}

		return true;
	}

//	public static boolean match(Node target, Node condition) {
//		if (target == null) {
//			return true;
//		}
//		if (condition == null) {
//			return true;
//		}
////		System.err.println("nodeType: " + target.getNodeType());
//		if (target.getNodeType() != Node.ELEMENT_NODE) {
//			return match(target.getNextSibling(), condition);
//		}
//		if (condition.getNodeType() != Node.ELEMENT_NODE) {
//			return match(target, condition.getNextSibling());
//		}
//
////		System.err.println("hi");
//
//		NamedNodeMap condNodeMap = condition.getAttributes();
//		if (condNodeMap != null) {
//			for (int n = 0; n < condNodeMap.getLength(); n++) {
//				Node condNode = condNodeMap.item(n);
////				System.err.println(condNode.getNodeName()); // attribute names
//				String condAttName = condNode.getNodeName();
//				String condAttValue = condNode.getNodeValue();
////				System.err.println("name: " + condAttName);
////				System.err.println("value: " + condAttValue);
//
//				if (condAttName.equals("id")) {
//					continue;
//				} //
//				else {
//					if (target.getAttributes() != null) {
//						Node t = target.getAttributes().getNamedItem(condAttName);
//						String value2 = t.getNodeValue();
//						// 異なる値であれば false
//						if (value2.equals(condAttValue) == false) {
//							return false;
//						}
//					} else {
//
//					}
//				}
//
//			}
//			String idval = condNodeMap.getNamedItem("id").getNodeValue();
//			((Element) target).setAttribute("_hit", idval);
//			System.err.println("_hit = " + idval);
//		} else {
//		}
//
//		NodeList condNodeChildList = condition.getChildNodes();
//		// condNodeChild
//		if (condNodeChildList != null) {
//			for (int n = 0; n < condNodeChildList.getLength(); n++) {
//				Node condChildNode = condNodeChildList.item(n);
//
//				boolean b = false;
//
//				NodeList targetNodeChildList = target.getChildNodes();
//				if (targetNodeChildList != null) {
//					for (int m = 0; m < targetNodeChildList.getLength(); m++) {
//						Node targetChildNode = targetNodeChildList.item(m);
//						b = match(targetChildNode, condChildNode);
//						if (b == true) {
//							continue;
//						}
//					}
//				} else {
//					b = true;
//				}
//
//				if (b == false) {
//					return false;
//				}
//
//			}
//
//		} else {
//
//		}
//
//		return true;
//	}

}
