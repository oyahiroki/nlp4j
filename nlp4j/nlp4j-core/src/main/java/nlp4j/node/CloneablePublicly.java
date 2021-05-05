package nlp4j.node;

/**
 * @author Hiroki Oya
 * @created_at 2021-05-04
 */
interface CloneablePublicly<T> extends Cloneable {
	T clone();
}
