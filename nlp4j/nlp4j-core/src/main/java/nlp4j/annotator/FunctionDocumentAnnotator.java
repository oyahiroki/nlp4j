package nlp4j.annotator;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import nlp4j.AbstractDocumentAnnotator;
import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.FieldAnnotator;

/**
 * @since 1.3.7.13
 */
public class FunctionDocumentAnnotator extends AbstractDocumentAnnotator implements DocumentAnnotator, FieldAnnotator {

	private Map<String, Function<Object, Object>> delta = new HashMap<>();

	public void put(String key, Function<Object, Object> f) {
		delta.put(key, f);
	}

	/**
	 * <pre>
	 * Example:
	 * ann.put("item1", value -> {	return ((String) value).toLowerCase(); });
	 * </pre>
	 */
	public void annotate(Document doc) throws Exception {
		doc.getAttributeKeys().forEach(k -> { //
			if (delta.containsKey(k)) {
				Object result = delta.get(k).apply(doc.getAttribute(k));
				doc.putAttribute(k, result);
			}
		});

	}

}
