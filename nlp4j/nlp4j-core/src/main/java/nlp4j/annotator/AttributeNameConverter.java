/**
 * 
 */
package nlp4j.annotator;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import nlp4j.AbstractDocumentAnnotator;
import nlp4j.Document;
import nlp4j.DocumentAnnotator;

/**
 * @author Hiroki Oya
 * @since 1.3
 *
 */
public class AttributeNameConverter extends AbstractDocumentAnnotator implements DocumentAnnotator {

	HashMap<String, String> fieldMap = new LinkedHashMap<String, String>();

	@Override
	public void setProperty(String key, String value) {

		super.setProperty(key, value);

		if (key.equals("mapping")) {
			String[] maps = value.split(",");

			for (String map : maps) {
				String[] ss = map.split("->");
				String from = ss[0];
				String to = ss[1];
				fieldMap.put(from, to);
			}
		}

	}

	@Override
	public void annotate(Document doc) throws Exception {

		Map<String, Object> attributes2 = new LinkedHashMap<String, Object>();

		List<String> keys = doc.getAttributeKeys();

		for (String key : keys) {
			if (this.fieldMap.containsKey(key)) {
				String field2 = fieldMap.get(key);
				attributes2.put(field2, doc.getAttribute(key));
			} else {
				attributes2.put(key, doc.getAttribute(key));
			}
		}

		for (String key : keys) {
			doc.remove(key);
		}

		for (String key : attributes2.keySet()) {
			doc.putAttribute(key, attributes2.get(key));
		}

	}

}
