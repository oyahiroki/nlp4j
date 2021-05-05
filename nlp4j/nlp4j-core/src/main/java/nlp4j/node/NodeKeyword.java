package nlp4j.node;

import nlp4j.KeywordWithDependency;

/**
 * @author Hiroki Oya
 * @created_at 2021-05-03
 * @param <T>
 */
public class NodeKeyword<T extends KeywordWithDependency> extends Node<Object> {

	T value;

	/**
	 * @param value of Node
	 */
	public NodeKeyword(T value) {
		super(value);
		this.value = value;
		for (KeywordWithDependency c : value.getChildren()) {
			this.addChildNode(new NodeKeyword<KeywordWithDependency>(c));
		}
	}

	@Override
	public boolean match(Node n) {
		if (this.value == null) {
			return false;
		}
		return this.value.match((T) n.getValue());
	}

}
