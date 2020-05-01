package nlp4j.annotator;

import nlp4j.AbstractDocumentAnnotator;
import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.FieldAnnotator;

/**
 * 指定したフィールドを整数に変換する
 * 
 * @author Hiroki Oya
 *
 */
public class ParseIntAnnotator extends AbstractDocumentAnnotator implements DocumentAnnotator, FieldAnnotator {

	String target;
	static private final String regex = "[^0-9]";

	@Override
	public void setProperty(String key, String value) {

		super.setProperty(key, value);

		if (key != null && key.equals("target")) {
			target = value;
		}
	}

	@Override
	public void annotate(Document doc) throws Exception {

		if (target != null) {
			Object val = doc.getAttribute(target);
			if (val != null) {
				String val2 = val.toString().replaceAll(regex, "");
				try {
					int n = Integer.parseInt(val2);
					doc.putAttribute(target, n);
				} catch (NumberFormatException e) {
//					e.printStackTrace();
					System.err.println("info: value is not integer: " + val2);
					doc.remove(target);
				}
			}
		}

	}

}
