package nlp4j.importer;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import nlp4j.Document;
import nlp4j.DocumentImporter;

/**
 * 文書をインデックスにインポートするドキュメントインポーターの抽象クラスです。<br>
 * Abstract class of document importer for indexing.
 * 
 * @author Hiroki Oya
 * @since 1.0
 *
 */
public abstract class AbstractDocumentImporter implements DocumentImporter {

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

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
	public final void setProperties(Properties prop) {
		for (Object key : prop.keySet()) {
			setProperty(key.toString(), prop.getProperty(key.toString()));
		}
	}

	@Override
	public void setProperty(String key, String value) {
		logger.debug(key + "=" + value);
		if (key != null) {
			props.put(key, value);
		}
	}

	/**
	 * @since 1.3.7.14
	 */
	@Override
	public String toString() {
		return "AbstractDocumentImporter " + this.getClass().getCanonicalName() + " " + " [props=" + props + "]";
	}

}
