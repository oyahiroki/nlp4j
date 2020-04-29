package nlp4j;

import java.util.Properties;

/**
 * @author Hiroki Oya
 * @since 1.3
 *
 */
public interface DocumentSearcher {
	/**
	 * プロパティをセットします。<br>
	 * Set properties.
	 * 
	 * @param prop プロパティ
	 */
	public void setProperties(Properties prop);

	/**
	 * プロパティをセットします。<br>
	 * Set a property key and value.
	 * 
	 * @param key   プロパティのキー
	 * @param value プロパティの値
	 */
	public void setProperty(String key, String value);

}
