/**
 * Abstract Indexer
 */
package nlp4j.indexer;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import nlp4j.Document;
import nlp4j.Keyword;

/**
 * AbstractIndexer
 * 
 * @author Hiroki Oya
 * @since 1.2.1.0
 */
public abstract class AbstractDocumentIndexer implements DocumentIndexer {

	@Override
	public void addDocuments(List<Document> docs) {
		for (Document doc : docs) {
			addDocument(doc);
		}
	}

	@Override
	public List<Keyword> getKeywords() {
		return null;
	}

	@Override
	public List<Keyword> getKeywords(String facet) {
		return null;
	}

	@Override
	public List<Keyword> getKeywords(String facet, String condition) {
		return null;
	}

	@Override
	public void setProperties(Properties prop) {
		for (Object key : prop.keySet()) {
			String k = key.toString();
			String value = prop.getProperty(k);
			setProperty(k, value);
		}
	}

	@Override
	public void setProperty(String key, String value) {
	}

	@Override
	public void commit() throws IOException {
	}

	@Override
	public void close() {
	}

}
