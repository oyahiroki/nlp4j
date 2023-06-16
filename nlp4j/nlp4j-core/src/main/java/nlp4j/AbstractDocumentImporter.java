package nlp4j;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nlp4j.util.DocumentUtil;
import nlp4j.util.JsonUtils;

/**
 * 文書をインデックスにインポートするドキュメントインポーターの抽象クラスです。<br>
 * Abstract class of document importer for indexing.
 * 
 * @author Hiroki Oya
 * @since 1.0
 *
 */
public abstract class AbstractDocumentImporter implements DocumentImporter {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	protected Properties props = new Properties();
	protected boolean debug = false;

	int docCount = 0;

	@Override
	public void importDocumentAndCommit(Document doc) throws IOException {
		importDocument(doc);
		commit();
	}

	@Override
	public void importDocuments(List<Document> docs) throws IOException {
		for (Document doc : docs) {
			docCount++;
			logger.info("importing ... " + docCount);
			importDocument(doc);
			logger.info("importing ... " + docCount + " done");
		}
	}

	@Override
	public void setProperties(Properties prop) {
		for (Object key : prop.keySet()) {
			setProperty(key.toString(), prop.getProperty(key.toString()));
		}
	}

	/**
	 * supported parameter: "debug"
	 */
	@Override
	public void setProperty(String key, String value) {
		if (value == null) {
			throw new InvalidPropertyException(key, value);
		}
		if (key.equals("debug")) {
			this.debug = Boolean.parseBoolean(value);
		}
		props.put(key, value);
	}

	/**
	 * Print document for debugging
	 * 
	 * @param doc target Document to print debug
	 */
	public void debugPrint(Document doc) {
		logger.info("debug");
		System.err.println("<debug>");
		System.err.println(JsonUtils.prettyPrint(DocumentUtil.toJsonObject(doc)));
		System.err.println("</debug>");
	}

}
