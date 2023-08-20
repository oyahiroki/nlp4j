package nlp4j.annotator;

import nlp4j.DocumentAnnotator;

/**
 * @author Hiroki Oya
 *
 * @param <T>
 * @since 1.3.7.12
 */
public class AnnotatorBuilder<T extends DocumentAnnotator> {

	T ann;

	/**
	 * @param instance
	 * @since 1.3.7.12
	 */
	public AnnotatorBuilder(T instance) {
		this.ann = instance;
	}

	/**
	 * @param k
	 * @param v
	 * @return
	 * @since 1.3.7.12
	 */
	public AnnotatorBuilder<T> prop(String k, String v) {
		this.ann.setProperty(k, v);
		return this;
	}

	/**
	 * @return
	 * @since 1.3.7.12
	 */
	public T build() {
		return this.ann;
	}

}
