package nlp4j.crawler;

import java.util.ArrayList;
import java.util.Properties;

import nlp4j.Document;

/**
 * データクローラーの抽象クラス<br>
 * Abstract class for data crawler
 * 
 * @author Hiroki Oya
 * @since 1.0
 *
 */
public abstract class AbstractCrawler implements Crawler {

	@Override
	abstract public ArrayList<Document> crawlDocuments();

	@Override
	public void setProperties(Properties prop) {
		for (Object key : prop.keySet()) {
			setProperty(key.toString(), prop.getProperty(key.toString()));
		}
	}

	@Override
	abstract public void setProperty(String key, String value);

}
