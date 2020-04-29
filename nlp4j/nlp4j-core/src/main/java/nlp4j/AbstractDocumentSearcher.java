package nlp4j;

import java.util.Properties;

/**
 * @author Hiroki Oya
 * @since 1.3
 *
 */
public abstract class AbstractDocumentSearcher implements DocumentSearcher {

	protected Properties props = new Properties();

	@Override
	public void setProperties(Properties prop) {
		for (Object key : prop.keySet()) {
			setProperty(key.toString(), prop.getProperty(key.toString()));
		}
	}

	@Override
	public void setProperty(String key, String value) {
		props.put(key, value);
	}

}
