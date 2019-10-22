package nlp4j.crawler;

import java.util.ArrayList;
import java.util.Properties;

import nlp4j.Document;

public interface Crawler {

	public void setProperties(Properties prop);

	public void setProperty(String key, String value);

	public ArrayList<Document> crawlDocuments();

}
