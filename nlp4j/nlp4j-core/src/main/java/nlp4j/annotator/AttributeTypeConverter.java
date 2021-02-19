package nlp4j.annotator;

import java.lang.invoke.MethodHandles;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.TimeZone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nlp4j.AbstractDocumentAnnotator;
import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.FieldAnnotator;

/**
 * 属性のタイプを変換する<br>
 * プロパティ<br>
 * mapping "field_name-&gt;Type,field_name-&gt;Type,field_name-&gt;Type"<br>
 * Example<br>
 * mapping date-&gt;Date:yyyyMMdd,field_int1-&gt;Integer,field_int2-&gt;Integer
 * 
 * @author Hiroki Oya
 * @since 1.3.1.0
 *
 */
public class AttributeTypeConverter extends AbstractDocumentAnnotator implements DocumentAnnotator, FieldAnnotator {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	HashMap<String, String> fieldMap = new LinkedHashMap<String, String>();

	@Override
	public void setProperty(String key, String value) {

		if (key == null || value == null) {
			return;
		}

		super.setProperty(key, value);

		if (key.equals("mapping")) {
			String[] mappings = value.split(",");
			for (String mps : mappings) {
				int idx = mps.indexOf("->");
				if (idx == -1) {
					logger.warn("Invalid: " + mps);
					continue;
				} else {
					String field = mps.substring(0, idx);
					String rule = mps.substring(idx + "->".length());
					logger.debug("field=" + field + ",rule=" + rule);
					fieldMap.put(field, rule);

				}
			}
		}

		// date->Date:yyyyMMdd,field_int1->Integer,field_int2->Integer

	}

	@Override
	public void annotate(Document doc) throws Exception {

		for (String key : fieldMap.keySet()) {

			String fieldValue = doc.getAttributeAsString(key);

			if (fieldValue != null) {

				String rule = fieldMap.get(key);

				String type = rule.split(":")[0];

				if (type.equals("Integer")) {
					processAsInteger(doc, key);
				} //
				else if (type.equals("Double")) {
					processAsDouble(doc, key);
				} //
				else if (type.equals("Date")) {
					processAsDate(doc, key, rule);
				}

			} else {
				// SKIP
			}

		}

	}

	private void processAsDate(Document doc, String key, String rule) {
		String fieldValue2 = doc.getAttributeAsString(key);
		if (fieldValue2 == null || fieldValue2.trim().isEmpty() == true) {
			doc.remove(key);
			return;
		}
		String[] rr = rule.split(":");
		if (rr.length < 2) {
			logger.error("Invalid Rule description: " + rule);
		} //
		else if (rr.length == 2) {
			String dateFormat = rr[1];
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			try {
				Date d = sdf.parse(fieldValue2);
				doc.putAttribute(key, d);
			} catch (ParseException e) {
				logger.error(e.getMessage() + ",key=" + key + ",doc=" + doc);
				logger.error(e);
				doc.remove(key);
			}
		} //
		else if (rr.length == 3) {
			String dateFormat = rr[1];
			String timeZone = rr[2];
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
			try {
				Date d = sdf.parse(fieldValue2);
				doc.putAttribute(key, d);
			} catch (ParseException e) {
				logger.error(e.getMessage() + ",key=" + key + ",doc=" + doc);
				logger.error(e);
				doc.remove(key);
			}
		}
	}

	private void processAsDouble(Document doc, String key) {
		String fieldValue2 = doc.getAttributeAsString(key);
		if (fieldValue2 == null || fieldValue2.trim().isEmpty() == true) {
			doc.remove(key);
			return;
		}
		try {
			Double v = Double.parseDouble(fieldValue2);
			doc.putAttribute(key, v);
		} catch (NumberFormatException e) {
			logger.error(e.getMessage() + ",key=" + key + ",doc=" + doc);
			logger.error(e);
			doc.remove(key);
		}
	}

	private void processAsInteger(Document doc, String key) {
		String fieldValue2 = doc.getAttributeAsString(key);
		if (fieldValue2 == null || fieldValue2.trim().isEmpty() == true) {
			doc.remove(key);
			return;
		}
		try {
			Integer v = Integer.parseInt(fieldValue2);
			doc.putAttribute(key, v);
		} catch (NumberFormatException e) {
			logger.error(e.getMessage() + ",key=" + key + ",doc=" + doc);
			logger.error(e);
			doc.remove(key);
		}
	}

}
