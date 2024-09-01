package nlp4j.importer;

import java.lang.reflect.Constructor;

import nlp4j.DocumentImporter;

/**
 * @since 1.3.7.14
 */
public class DocumentImporterBuilder {
	DocumentImporter t;

	public <T extends DocumentImporter> DocumentImporterBuilder(Class<T> b) throws Exception {
		Constructor<T> c = b.getConstructor(); // throws NoSuchMethodException | SecurityException
		t = c.newInstance();
	}

	public DocumentImporterBuilder p(String k, String v) {
		this.t.setProperty(k, v);
		return this;
	}

	public DocumentImporter build() {
		return this.t;

	}

}
