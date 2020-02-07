package nlp4j.node;

import java.util.ArrayList;

public class Node implements Cloneable {

	String value = null;

	Node parent = null;

	ArrayList<Node> children = new ArrayList<>();

	int idxChild = 0;

	int depth = 0;

	@Override
	protected Node clone() {
		Node c = new Node(this.value);
		for (Node child : children) {
			Node cc = child.clone();
			c.addChildNode(cc);
		}
		return c;
	}

	public Node(String value) {
		this.value = value;
	}

	public Node nextChild() {
		if (hasNextChild()) {
			Node n = children.get(idxChild);
			idxChild++;
			return n;
		} else {
			return null;
		}
	}

	public Node next() {
		if (this.hasNextChild()) {
			return this.nextChild();
		} else {
			if (parent != null) {
				return parent.next();
			} else {
				return null;
			}
		}
	}

	protected boolean hasNextChild() {
		if (idxChild == children.size()) {
			return false;
		} else {
			return true;
		}
	}

	public void addChildNode(Node n) {
		n.parent = this;
		this.children.add(n);
		n.depth++;
	}

	public boolean matchAll(Node cond) {

		if (this.match(cond) == false) {
			Node n1 = this.next();
			if (n1 == null) {
				return false;
			} //
			else {
				return n1.matchAll(cond);
			}
		} else {
			Node n1 = this.next();
			Node n2 = cond.next();
			if (n2 == null) {
				return true;
			} else {
				if (n1 == null) {
					return false;
				} else {
					return n1.matchAll(n2);
				}
			}
		}
	}

	public boolean match(Node n) {
		System.err.println(this.value + "," + n.value);
		return this.value.equals(n.value) & this.depth == n.depth;
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

		for (Node n : children) {
			n.print(depth + 1);
		}

	}

	@Override
	public String toString() {
		return "N(" + value + "),depth=" + depth + "," + children + "";
	}

}
