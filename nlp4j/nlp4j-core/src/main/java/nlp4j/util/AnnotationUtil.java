package nlp4j.util;

import java.lang.invoke.MethodHandles;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.impl.DefaultDocument;

public class AnnotationUtil {

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	static public Document annotate(String text, boolean ignoreException, DocumentAnnotator... annotators) throws Exception {

		Document doc = new DefaultDocument(text);

		for (DocumentAnnotator ann : annotators) {
			try {
				ann.annotate(doc);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				if (ignoreException == false) {
					throw e;
				}
			}
		}

		return doc;
	}

}
