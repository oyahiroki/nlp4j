package nlp4j.node;

import java.lang.invoke.MethodHandles;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nlp4j.KeywordWithDependency;
import nlp4j.pattern.KeywordRule;

/**
 * @author Hiroki Oya
 * @created_at 2021-05-03
 * @param <T>
 */
public class NodeKeyword<T extends KeywordWithDependency> extends Node<Object> {

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

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

		logger.debug("Class name: " + n.getClass().getName());

		T v1 = this.value;
		T v2 = (T) n.getValue();

		logger.debug("v2 instanceof KeywordRule: " + (v2 instanceof KeywordRule));

		return v1.match(v2);
	}

}
