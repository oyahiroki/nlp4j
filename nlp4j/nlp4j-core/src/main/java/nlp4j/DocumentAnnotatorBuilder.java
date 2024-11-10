package nlp4j;

import java.lang.reflect.InvocationTargetException;

/**
 * <pre>
 * Usage Example: DocumentAnnotator ann = (new DocumentAnnotatorBuilder<>(DebugAnnotator.class)).set("target", "text").build();
 * </pre>
 * 
 * @param <T>
 */
public class DocumentAnnotatorBuilder<T extends DocumentAnnotator> {

	T annotator;

	public DocumentAnnotatorBuilder(Class<T> classOfT) {

		try {
			this.annotator = classOfT.getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}

	public DocumentAnnotatorBuilder<T> set(String propertyKey, String propertyValue) {
		this.annotator.setProperty(propertyKey, propertyValue);
		return this;
	}

	public T build() {
		return this.annotator;
	}

}
