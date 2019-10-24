package nlp4j.crawler;

import java.util.ArrayList;
import java.util.Properties;

import nlp4j.Document;

/**
 * データクローラーのインターフェイスクラスです。<br>
 * Interface class for data crawler.
 * 
 * @author Hiroki Oya
 *
 */
public interface Crawler {

	/**
	 * プロパティをセットします。<br>
	 * Set properties.
	 * 
	 * @param prop プロパティ
	 */
	public void setProperties(Properties prop);

	/**
	 * プロパティをセットします。<br>
	 * Set property.
	 * 
	 * @param key   プロパティのキー
	 * @param value プロパティの値
	 */
	public void setProperty(String key, String value);

	public ArrayList<Document> crawlDocuments();

}
