package nlp4j.util;

/**
 * created on 2021-07-27
 * 
 * @author Hiroki Oya
 * @since 1.3.2.0
 */
public class EnvUtil {

	/**
	 * <pre>
	 * IF(System.getProperty() != NULL) THEN RETURN System.getProperty()
	 * ELSE RETURN System.getenv()
	 * </pre>
	 * 
	 * @param key of System.prpoerty or System.env
	 * @return value
	 */
	static public String get(String key) {
		String v = null;
		v = System.getProperty(key);
		if (v != null) {
			return v;
		}
		v = System.getenv(key);
		return v;
	}

}
