package nlp4j.annotator;

import java.util.Arrays;
import java.util.List;

import nlp4j.AbstractDocumentAnnotator;
import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.FieldAnnotator;

/**
 * @author Hiroki Oya
 * @since 1.3.1.0
 *
 */
public class AttributeFilterAnnotator extends AbstractDocumentAnnotator implements DocumentAnnotator, FieldAnnotator {

	List<String> fields = null;

	@Override
	public void setProperty(String key, String value) {

		super.setProperty(key, value);

		if ("fields".equals(key)) {
			String[] ff = value.split(",");
			fields = Arrays.asList(ff);
		}

	}

	@Override
	public void annotate(Document doc) throws Exception {

		if (fields == null) {
			return;
		} else {
			for (String key : doc.getAttributeKeys()) {
				if (fields.contains(key) == false) {
					doc.remove(key);
				}
			}
		}

	}

}
