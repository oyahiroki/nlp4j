package nlp4j.impl;

import java.util.HashMap;
import java.util.Map;

public class Node<T> {
	private T data;
	private Node<T> parent;
	private Map<T, Node<T>> children = new HashMap<T, Node<T>>();

	public Node(T data) {
		this.data = data;
	}

	public Node(T data, Node<T> parent) {
		this.data = data;
		this.parent = parent;
	}

	public void addChild(Node<T> child) {
		child.setParentInternal(this);
		this.children.put(child.data, child);
	}

	public void addChild(T data) {
		addChild(new Node<T>(data));
	}

	public boolean hasChildren() {
		if (this.children != null) {
			return this.children.size() > 0;
		}
		return false;
	}

	public void setParent(Node<T> parent) {
		parent.addChild(this);
		setParentInternal(parent);
	}

	private void setParentInternal(Node<T> parent) {
		this.parent = parent;
	}
}