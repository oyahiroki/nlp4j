package nlp4j.node;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Hiroki Oya
 * @param <T> Value of this Node
 * @created_at 2021-01-17
 * @since 1.3.1.0
 */
public class Node<T> implements CloneablePublicly<Node<T>> {

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	protected ArrayList<Node<T>> childNodes = new ArrayList<>();

	protected int childrenIndex = 0;

	protected int depth = 0;

	protected Node<T> parent = null;

	protected T value = null;

	/**
	 * このNodeがChildの時のインデックス番号<br>
	 * Index number when this node is a child
	 */
	int indexAsChild = -1;

	private Node<T> hitNode;

	/**
	 * @param value of this node
	 */
	public Node(T value) {
		this.value = value;
	}

	public void addChildNode(Node<T> childNode) {
		// Set Parent
		childNode.parent = this;
		this.childNodes.add(childNode);

		childNode.indexAsChild = (this.childNodes.size() - 1);
		childNode.depth++;
	}

	public Node<T> getChildNode(int idx) {
		{ // FOR DEBUG
			logger.debug("this.children != null: " + (this.childNodes != null));
			if (this.childNodes != null) {
				logger.debug(this.childNodes.size());
			}
			logger.debug("param idx: " + idx);
		}

		if (this.childNodes != null && this.childNodes.size() > 0 && this.childNodes.size() > idx) {
			return this.childNodes.get(idx);
		} //
		else if (this.indexAsChild != -1 && this.parent != null) {
			return parent.getChildNode(indexAsChild + 1);
		} //
		else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Node<T> clone() {
		Node<T> c;
		try {
			c = (Node<T>) super.clone();
		} //
		catch (CloneNotSupportedException e) {
			e.printStackTrace();
			logger.error(e);
			return null;
		}

		c.value = this.value;

		ArrayList<Node<T>> nn = new ArrayList<>();

		// Clone child nodes
		for (int n = 0; n < childNodes.size(); n++) {
			nn.add(childNodes.get(n).clone());
		}

		c.childNodes = nn;
		return c;
	}

	/**
	 * @return size of child nodes
	 */
	public int getChildNodesSize() {
		if (this.childNodes == null) {
			return 0;
		} else {
			return this.childNodes.size();
		}
	}

	public List<Node<T>> clonePatterns() {

		ArrayList<Node<T>> ret = new ArrayList<Node<T>>();

		Node<T> ptr = this.clone();
		ret.add(ptr);
		if (ptr.getChildNodesSize() > 0) {
			Node<T> cloned2 = ptr.clone();
			for (int n = 0; n < cloned2.getChildNodesSize(); n++) {
				cloned2.removeChild(n);
				ret.add(cloned2.clone());
			}
		}

		while ((ptr = ptr.next()) != null) {
			Node<T> cloned = ptr.clone();
			ret.add(cloned);

			if (cloned.getChildNodesSize() > 0) {
				Node<T> cloned2 = cloned.clone();
				for (int n = 0; n < cloned2.getChildNodesSize(); n++) {
					cloned2.removeChild(n);
					ret.add(cloned2.clone());
				}
			}

		}

		return ret;
	}

	/**
	 * @return value of this node
	 */
	public T getValue() {
		return value;
	}

	protected boolean hasNextChild() {
		if (this.childrenIndex == childNodes.size()) {
			return false;
		} //
		else {
			return true;
		}
	}

	/**
	 * @param index of child node
	 * @return removed node or null
	 */
	public Node<T> removeChild(int index) {
		if (this.childNodes == null || this.childNodes.size() == 0 || this.childNodes.size() < index) {
			return null;
		} //
		else {
			Node<T> removed = this.childNodes.remove(index);
			for (int n = 0; n < this.childNodes.size(); n++) {
				this.childNodes.get(n).indexAsChild = 0;
			}
			return removed;
		}
	}

	/**
	 * @param cond : node to be matched
	 * @return match result of node
	 */
	public boolean match(Node<T> cond) {
		if (cond == null || cond.value == null) {
			return false;
		} //
		else {
			logger.debug(this.value + "," + cond.value);
			boolean matched = this.value.equals(cond.value);
			if (matched) {
				if (cond.hitNode != null) {
					throw new RuntimeException("hitNode is not null");
				}
				cond.hitNode = this;
			}
			return matched;
		}
	}

	/**
	 * @return child nodes of this node
	 */
	public ArrayList<Node<T>> getChildNodes() {
		return this.childNodes;
	}

	public boolean matchAll(Node<T> cond) {
		
//		Node<T> ts = this;
//		Node<T> rule = cond;
		
		Node<T> ts = this;
		Node<T> rule = cond;

		logger.debug("this.value=" + ts.value);
		logger.debug("cond.value=" + rule.value);

		// ルートNodeは必ずマッチしなければならない
		// match した時点で cond に hitNode がセットされる
		if (ts.match(rule) == false) {
			logger.debug("NOT hit: " + ts.value + " >--< " + rule.value);
			return false;
		} //

		// 子ノードは、マッチしない場合はスキップして次の子ノードをチェックする
		// Check Child Nodes
		else {

			logger.debug("hit: " + ts.value + " <--> " + rule.value);

			ArrayList<Node<T>> cc1 = ts.childNodes;
			ArrayList<Node<T>> cc2 = rule.childNodes;
			int cc1idx = 0;

			for (int idxCondChild = 0; idxCondChild < cc2.size(); idxCondChild++) {

				if (cc1.size() <= cc1idx) {
					return false;
				}

				Node<T> c1 = cc1.get(cc1idx);
				Node<T> c2 = cc2.get(idxCondChild);

				if (c1.matchAll(c2) == true) {
//					if (c2.matchAll(c1) == true) {
					continue; // n++
				} //
				else {
					cc1idx++;
					idxCondChild--; // keep n
					continue;
				}

			} // END OF FOR

			return true;
		}
	}

	/**
	 * @return Next Node or Next Leaf
	 */
	public Node<T> next() {
		if (this.childNodes != null && this.childNodes.size() > 0) {
			return childNodes.get(0);
		} //
		else if (this.indexAsChild != -1) {
			return parent.getChildNode(this.indexAsChild + 1);
		} //
		else {
			return null;
		}
	}

	/**
	 * @return whether this node has next node or next leaf
	 */
	public boolean hasNext() {
		return (next() != null);
//		if (this.hasNextChild()) {
//			return this.nextChild() != null;
//		} else {
//			if (parent != null) {
//				return parent.next() != null;
//			} else {
//				return false;
//			}
//		}
	}

	public Node<T> nextChild() {
		if (hasNextChild()) {
			Node<T> n = childNodes.get(childrenIndex);
			this.childrenIndex++;
			return n;
		} else {
			return null;
		}
	}

	public void print() {
		print(0);
	}

	private void print(int depth) {
		StringBuilder sb = new StringBuilder();
		for (int n = 0; n < depth; n++) {
			sb.append("--");
		}
		System.err.println(sb.toString() + value + "[" + depth + "]");

		for (Node n : childNodes) {
			n.print(depth + 1);
		}

	}

	public void resetIndex() {
		this.childrenIndex = 0;
		for (Node c : childNodes) {
			c.resetIndex();
		}
	}

	@Override
	public String toString() {

		String hitNodeValue = null;

		if (this.hitNode != null) {
			hitNodeValue = this.hitNode.value.toString();
		}

		return "N(" + value + ")," //
				+ "depth=" + depth + "," //
				+ "hitNodeValue=" + hitNodeValue + ","//
				+ "" + childNodes + "";
	}

}
