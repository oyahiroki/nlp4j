package nlp4j.node;

/**
 * created on 2021-05-04
 * 
 * @author Hiroki Oya
 */
interface CloneablePublicly<T> extends Cloneable {
	T clone();
}
