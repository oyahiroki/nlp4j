package nlp4j.annotator;

import nlp4j.DocumentAnnotator;

/**
 * @author Hiroki Oya
 *
 * @since 1.3.7.14
 */
public class AnnotatorBuilder {

	DocumentAnnotator ann;

	/**
	 * @param instance
	 * @since 1.3.7.14
	 */
	public <T extends DocumentAnnotator> AnnotatorBuilder(Class<T> clazz) throws Exception {
		this.ann = (DocumentAnnotator) clazz.getConstructor().newInstance();
	}

	/**
	 * Set property
	 * 
	 * @param k
	 * @param v
	 * @return
	 * @since 1.3.7.14
	 */
	public AnnotatorBuilder p(String k, String v) {
		this.ann.setProperty(k, v);
		return this;
	}

	/**
	 * @return
	 * @since 1.3.7.14
	 */
	public DocumentAnnotator build() {
		return this.ann;
	}

}
