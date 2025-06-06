package nlp4j.util;

import java.lang.invoke.MethodHandles;
import java.util.NoSuchElementException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * <pre>
 * 環境変数やシステムプロパティからプロパティ値を取得するユーティリティクラス。
 * Utility class for retrieving property values from environment variables or system properties.
 * </pre>
 * 
 * @since 1.3.7.15
 */
public class PropertyUtils {

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	/**
	 * <pre>
	     * 指定されたキーに対応するプロパティ値を環境変数またはシステムプロパティから取得します。
	     * Retrieves the property value for the specified key from the environment variables or system properties.
	 * </pre>
	 * 
	 * @param key
	 * @return
	 * @since 1.3.7.15
	 * @throws NoSuchElementException if not found
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
		logger.warn("NOT_SET: " + key);

//		return null;

		// 2025/5/29
		throw new NoSuchElementException(key);
	}

	/**
	 * <pre>
	     * 指定されたキーに対応するプロパティ値を環境変数またはシステムプロパティから取得します。
	     * Retrieves the property value for the specified key from the environment variables or system properties.
	     * 値が設定されていない場合は例外をスローします。
	     * Throws an exception if the value is not set.
	 * </pre>
	 * 
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

		throw new NoSuchElementException("NOT_SET: " + key);

	}

}
