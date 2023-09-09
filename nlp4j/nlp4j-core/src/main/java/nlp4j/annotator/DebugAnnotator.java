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

	private boolean wait = false;
	private long wait_time_ms = 0;

	@Override
	public void annotate(Document doc) throws Exception {
		logger.info("annotate(Document doc) ... ");
		if (wait) {
			logger.info("annotate(Document doc) ... waiting ... ");
			try {
				Thread.sleep(wait_time_ms);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		{
			String text = doc.getText();
			if (text != null) {
				text = text + " " + "ann";
			} else {
				text = "ann";
			}
			doc.setText(text);
		}

		System.err.println(DocumentUtil.toJsonPrettyString(doc));
		logger.info("annotate(Document doc) ... done");
	}

	/**
	 * wait=1000 for wait in annotate()
	 * 
	 */
	@Override
	public void setProperty(String key, String value) {
		logger.info("key=" + key + ",value=" + value);
		super.setProperty(key, value);

		if ("wait".equals(key)) {
			this.wait = true;
			this.wait_time_ms = Long.parseLong(value);
		}

	}

	@Override
	public void annotate(List<Document> docs) throws Exception {
		logger.info("annotate(List<Document> docs)");
		super.annotate(docs);
	}

}
