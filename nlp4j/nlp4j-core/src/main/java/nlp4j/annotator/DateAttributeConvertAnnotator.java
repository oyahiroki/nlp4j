/**
 * 
 */
package nlp4j.annotator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import nlp4j.AbstractDocumentAnnotator;
import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.FieldAnnotator;

/**
 * 文字列形式の日付を java.util.Date に変換する
 * @author Hiroki Oya
 * @since 1.3
 *
 */
public class DateAttributeConvertAnnotator extends AbstractDocumentAnnotator
		implements DocumentAnnotator, FieldAnnotator {

	String target = "date";
	String format = null;
	SimpleDateFormat sdf = null;

	@Override
	public void setProperty(String key, String value) {

//		super.setProperty(key, value);

		if (key.equals("target")) {
			this.target = value;
		}
		if (key.equals("format")) {
			this.format = value;
		}

	}

	@Override
	public void annotate(Document doc) throws Exception {

		if (this.target == null) {
			return;
		}
		if (this.format == null) {
			return;
		}

		Object obj = doc.getAttribute(target);

		if (obj != null && obj instanceof Date == false) {
			String value = obj.toString();
			if (this.sdf == null) {
				sdf = new SimpleDateFormat(this.format);
			}

			try {
				Date d = sdf.parse(value);
				doc.putAttribute(target, d);
			} catch (ParseException e) {
				System.err.println("info: value is not date: " + value);
				e.printStackTrace();
				doc.remove(target);
			}

		}

	}

}
