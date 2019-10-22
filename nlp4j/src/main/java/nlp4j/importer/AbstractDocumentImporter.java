package nlp4j.importer;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import nlp4j.Document;

/**
 * 文書をインデックスにインポートするドキュメントインポーターの抽象クラスです。<br/>
 * Abstract class of document importer for indexing.
 * 
 * @author Hiroki Oya
 * @since 1.0
 *
 */
public abstract class AbstractDocumentImporter implements DocumentImporter {

	protected Properties props = new Properties();

	@Override
	public void importDocumentAndCommit(Document doc) throws IOException {
		importDocument(doc);
		commit();
	}

	@Override
	public void importDocuments(List<Document> docs) throws IOException {
		for (Document doc : docs) {
			importDocument(doc);
		}
	}

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
