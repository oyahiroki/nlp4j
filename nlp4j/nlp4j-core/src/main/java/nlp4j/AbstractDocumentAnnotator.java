package nlp4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * ドキュメントアノテーターの抽象クラス。 <br>
 * Abstract Class of Document Annotator.
 * 
 * @author Hiroki Oya
 * @since 1.0
 *
 */
public abstract class AbstractDocumentAnnotator implements DocumentAnnotator {

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
		for (Document doc : docs) {
			annotate(doc);
		}
	}

}
