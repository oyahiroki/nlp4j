package nlp4j.util;

/**
 * @since 1.3.7.15
 */
public class PropertyUtils {

	/**
	 * @param key
	 * @return
	 * @since 1.3.7.15
	 */
	static public String getProperty(String key) {
		String value = null;

		value = System.getenv(key);
		if (value != null) {
			return value;
		}
		value = System.getProperty(key);

		if (value != null) {
			return value;
		}

		return null;
	}

	/**
	 * @param key
	 * @return
	 * @since 1.3.7.18
	 */
	static public String getPropertyThrowOnNotSet(String key) {
		String value = null;

		value = System.getenv(key);
		if (value != null) {
			return value;
		}
		value = System.getProperty(key);

		if (value != null) {
			return value;
		}

		throw new IllegalArgumentException("NOT_SET: " + key);

	}

}
