package nlp4j.annotator;

import java.lang.invoke.MethodHandles;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nlp4j.AbstractDocumentAnnotator;
import nlp4j.Document;
import nlp4j.util.DocumentUtil;

/**
 * @author Hiroki Oya
 * @since 1.3.3.0
 *
 */
public class DebugAnnotator extends AbstractDocumentAnnotator {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	@Override
	public void annotate(Document doc) throws Exception {
		logger.info("annotate(Document doc)");
		System.err.println(DocumentUtil.toJsonPrettyString(doc));
	}

	@Override
	public void setProperty(String key, String value) {
		logger.info("key=" + key + ",value=" + value);
		super.setProperty(key, value);
	}

	@Override
	public void annotate(List<Document> docs) throws Exception {
		logger.info("annotate(List<Document> docs)");
		super.annotate(docs);
	}

}
