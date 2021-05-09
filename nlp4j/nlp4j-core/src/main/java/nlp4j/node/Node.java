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

	private Node<T> hitNode;

	/**
	 * このNodeがChildの時のインデックス番号<br>
	 * Index number when this node is a child
	 */
	int indexAsChild = -1;

	protected Node<T> parent = null;

	protected T value = null;

	/**
	 * @param value of this node
	 */
	public Node(T value) {
		this.value = value;
	}

	/**
	 * @param childNode for add
	 */
	public void addChildNode(Node<T> childNode) {
		// Set Parent
		childNode.parent = this;
		this.childNodes.add(childNode);

		childNode.indexAsChild = (this.childNodes.size() - 1);
		childNode.depth++;
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
	 * Split Nodes for Pattern Matching
	 * 
	 * @return cloned Nodes
	 */
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
	 * @param idx of Child Node
	 * @return Child Node
	 */
	public Node<T> getChildNode(int idx) {
		if (logger.isDebugEnabled()) { // FOR DEBUG
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

	/**
	 * @return child nodes of this node
	 */
	public ArrayList<Node<T>> getChildNodes() {
		return this.childNodes;
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

	/**
	 * @return value of this node
	 */
	public T getValue() {
		return value;
	}

	/**
	 * @return whether this node has next node or next leaf
	 */
	public boolean hasNext() {
		return (next() != null);
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
	 * @param target : node to be matched
	 * @return match result of node
	 */
	public boolean match(Node<T> target) {
		if (target == null || target.value == null) {
			return false;
		} //
		else {
			logger.debug(this.value + "," + target.value);
			boolean matched = this.value.equals(target.value);
			if (matched) {
				if (target.hitNode != null) {
					throw new RuntimeException("hitNode is not null");
				}
				target.hitNode = this;
			}
			return matched;
		}
	}

	/**
	 * @param target node for matching
	 * @return match result of all nodes
	 */
	public boolean matchAll(Node<T> target) {

		Node<T> ruleNode = this;
		Node<T> targetNode = target;

		logger.debug("this.value=" + ruleNode.value);
		logger.debug("cond.value=" + targetNode.value);

		// ルートNodeは必ずマッチしなければならない
		// match した時点で cond に hitNode がセットされる
		if (ruleNode.match(targetNode) == false) {
			logger.debug("NOT hit: " + ruleNode.value + " >--< " + targetNode.value);
			return false;
		} //

		// 子ノードは、マッチしない場合はスキップして次の子ノードをチェックする
		// Check Child Nodes
		else {

			logger.debug("hit: " + ruleNode.value + " <--> " + targetNode.value);

			ArrayList<Node<T>> childRule = ruleNode.childNodes;
			ArrayList<Node<T>> childTarget = targetNode.childNodes;
			int idxTarget = 0;

			for (int idxChildRule = 0; idxChildRule < childRule.size(); idxChildRule++) {

				if (childTarget.size() <= idxTarget) {
					return false;
				}

				Node<T> childNodeRule = childRule.get(idxChildRule);
				Node<T> childNodeTarget = childTarget.get(idxTarget);

				if (childNodeRule.matchAll(childNodeTarget) == true) {
					continue; // n++
				} //
				else {
					idxTarget++;
					idxChildRule--; // keep n
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
	 * @return Next Child Node
	 */
	public Node<T> nextChild() {
		if (hasNextChild()) {
			Node<T> n = childNodes.get(childrenIndex);
			this.childrenIndex++;
			return n;
		} else {
			return null;
		}
	}

	/**
	 * Print Node to System.err
	 */
	public void print() {
		print(0);
	}

	private void print(int depth) {
		StringBuilder sb = new StringBuilder();
		for (int n = 0; n < depth; n++) {
			sb.append("--");
		}
		System.err.println(sb.toString() + value + "[" + depth + "]");

		for (Node<T> n : childNodes) {
			n.print(depth + 1);
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
	 * Reset Index Pointer
	 */
	public void resetIndex() {
		this.childrenIndex = 0;
		for (Node<T> c : childNodes) {
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
