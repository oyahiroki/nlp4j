package nlp4j;

import java.lang.reflect.InvocationTargetException;

public class DocumentImporterBuilder<T extends DocumentImporter> {

	T ipt;

	public DocumentImporterBuilder(Class<T> classOfT) {

		try {
			this.ipt = classOfT.getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}

	public DocumentImporterBuilder<T> set(String propertyKey, String propertyValue) {
		this.ipt.setProperty(propertyKey, propertyValue);
		return this;
	}

	public T build() {
		return this.ipt;
	}

}
