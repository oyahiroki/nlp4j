package nlp4j;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * ドキュメントアノテーターの抽象クラス。 <br>
 * Abstract Class of Document Annotator.
 * 
 * @author Hiroki Oya
 * @since 1.0
 *
 */
public abstract class AbstractDocumentAnnotator implements DocumentAnnotator {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	protected ArrayList<String> targets = new ArrayList<>();

	protected Properties prop = new Properties();

	/**
	 * プロパティをセットします。 <br>
	 * Set Property
	 * 
	 * @param prop プロパティ
	 * @since 1.1.1
	 */
	public void setProperties(Properties prop) {
		for (Object key : prop.keySet()) {
			setProperty(key.toString(), prop.getProperty(key.toString()));
		}
	}

	/**
	 * プロパティをセットします。 <br>
	 * Set Property
	 * 
	 * @param key   キー
	 * @param value 値
	 * @since 1.1.1
	 */
	public void setProperty(String key, String value) {
		this.prop.setProperty(key, value);
		if ("target".equals(key) && targets.contains(value) == false) {
			targets.add(value);
		}
	}

	@Override
	public void annotate(List<Document> docs) throws Exception {
		int countDoc = 0;
		for (Document doc : docs) {
			countDoc++;
			if (countDoc == 1 || (countDoc != 0 && countDoc % 100 == 0)) {
				logger.debug("Processing ... " + countDoc);
			}
			annotate(doc);
		}
	}

	@Override
	public String toString() {
		return "AbstractDocumentAnnotator" + " " + this.getClass().getCanonicalName() + " " + "[targets=" + targets
				+ ", prop=" + prop + "]";
	}

}
