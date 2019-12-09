package nlp4j.util;

import org.apache.commons.text.StringEscapeUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import nlp4j.Document;
import nlp4j.Keyword;
import nlp4j.impl.DefaultDocument;
import nlp4j.impl.DefaultKeyword;

/**
 * @author Hiroki Oya
 * @since 1.1
 *
 */
public class DocumentUtil {

	static String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

	static public String toXml(Document doc) {
		StringBuilder xml = new StringBuilder();

		xml.append(XML_HEADER);
		xml.append("<document>");

		{
			xml.append("<attributes>");
			for (String key : doc.getAttributeKeys()) {
				xml.append("<attribute key=\"" + key + "\" value=\""
						+ StringEscapeUtils.escapeXml11(doc.getAttribute(key).toString()) + "\" />");
			}
			xml.append("</attributes>");
		}
		{
			xml.append("<keywords>");
			for (Keyword kwd : doc.getKeywords()) {
				xml.append(toXml(kwd));
			}
			xml.append("</keywords>");
		}

		xml.append("</document>");

		return xml.toString();

	}

	static public String toXml(Keyword kwd) {
		StringBuilder xml = new StringBuilder();
		xml.append("<keyword ");
		xml.append(" begin=\"" + kwd.getBegin() + "\"");
		xml.append(" correlation=\"" + kwd.getCorrelation() + "\"");
		xml.append(" count=\"" + kwd.getCount() + "\"");
		xml.append(" end=\"" + kwd.getEnd() + "\"");
		if (kwd.getFacet() != null) {
			xml.append(" facet=\"" + StringEscapeUtils.escapeXml11(kwd.getFacet()) + "\"");
		}
		xml.append(" lex=\"" + StringEscapeUtils.escapeXml11(kwd.getLex()) + "\"");
		if (kwd.getReading() != null) {
			xml.append(" reading=\"" + StringEscapeUtils.escapeXml11(kwd.getReading()) + "\"");
		}
		if (kwd.getStr() != null) {
			xml.append(" str=\"" + StringEscapeUtils.escapeXml11(kwd.getStr()) + "\"");
		}
		xml.append(" />");
		return xml.toString();
	}

	/**
	 * Parse Document Object from Json
	 * 
	 * @param json JSON String
	 * @return Instance of DefaultDocument
	 */
	static public Document toDocument(String json) {
		Document doc = new DefaultDocument();
		Gson gson = new Gson();
		JsonObject jsonObj = gson.fromJson(json, JsonObject.class);
		if (jsonObj.get("keywords") != null) {
			JsonArray arr = jsonObj.get("keywords").getAsJsonArray();
			for (int n = 0; n < arr.size(); n++) {
				JsonElement elm = arr.get(n);
				Keyword kwd = gson.fromJson(elm, DefaultKeyword.class);
				doc.addKeyword(kwd);
			}
		}
		for (String key : jsonObj.keySet()) {
			if (key.equals("keywords")) {
				continue;
			} else {
				String value = jsonObj.get(key).getAsString();
				doc.putAttribute(key, value);
			}
		}
		return doc;
	}

	/**
	 * @since 1.1
	 * @param doc target Document
	 * @return Json String of Document
	 */
	static public String toJsonString(Document doc) {
		JsonObject jsonObj = new JsonObject();

		{
			JsonArray arr = new JsonArray();
			for (Keyword kwd : doc.getKeywords()) {
				arr.add(toJsonObject(kwd));
			}
			jsonObj.add("keywords", arr);
		}
		for (String key : doc.getAttributeKeys()) {
			jsonObj.addProperty(key, "" + doc.getAttribute(key));
		}
		Gson gson = new Gson();
		return gson.toJson(jsonObj);
	}

	/**
	 * @since 1.1
	 * @param kwd target Keyword
	 * @return Json String of Keyword
	 */
	static public String toJsonString(Keyword kwd) {
		Gson gson = new Gson();
		return gson.toJson(kwd);
	}

	/**
	 * @since 1.1
	 * @param kwd target Keyword
	 * @return JsonObject of Keyword
	 */
	static public JsonObject toJsonObject(Keyword kwd) {
		Gson gson = new Gson();
		String json = toJsonString(kwd);
		return gson.fromJson(json, JsonObject.class);
	}

}
