package nlp4j.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.io.FileUtils;
import org.apache.commons.text.StringEscapeUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;

import nlp4j.Document;
import nlp4j.Keyword;
import nlp4j.KeywordWithDependency;
import nlp4j.impl.DefaultDocument;
import nlp4j.impl.DefaultKeyword;

/**
 * @author Hiroki Oya
 * @since 1.1
 *
 */
public class DocumentUtil {

	static private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	static String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

	static {
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
	}

	/**
	 * @since 1.3.1.0
	 * @param json
	 * @return Parsed Document
	 * @throws Exception 例外発生時
	 */
	static public Document parseFromJson(String json) throws Exception {

		Document doc = new DefaultDocument();

		JsonObject jsonObj;

		try {
			jsonObj = (new Gson()).fromJson(json, JsonObject.class); // throws JsonSyntaxException
		} catch (JsonSyntaxException e) {
			throw new Exception("Syntax Exception", e);
		}

		for (String key : jsonObj.keySet()) {
			// keywords
			if (key.equals("keywords")) {
				JsonArray kwds = jsonObj.get(key).getAsJsonArray();
				for (int n = 0; n < kwds.size(); n++) {
					JsonObject kwd = kwds.get(n).getAsJsonObject();
					String facet = null;
					if (kwd.get("facet") != null && kwd.get("facet").isJsonNull() == false) {
						facet = kwd.get("facet").getAsString();
					}
					int begin = -1;
					if (kwd.get("begin") != null && kwd.get("begin").isJsonNull() == false) {
						begin = kwd.get("begin").getAsInt();
					}
					int end = -1;
					if (kwd.get("end") != null && kwd.get("end").isJsonNull() == false) {
						end = kwd.get("end").getAsInt();
					}
					String str = null;
					if (kwd.get("str") != null && kwd.get("str").isJsonNull() == false) {
						str = kwd.get("str").getAsString();
					}
					String lex = null;
					if (kwd.get("lex") != null && kwd.get("lex").isJsonNull() == false) {
						lex = kwd.get("lex").getAsString();
					}
					Keyword kw = new DefaultKeyword(begin, end, facet, lex, str);

					doc.addKeyword(kw);
				}
			} //
				// fields
			else {
				String fieldName = key;
				JsonElement elm = jsonObj.get(key);
				if (elm.isJsonNull()) {
					// System.err.println("json null");
				} //
				else if (elm.isJsonArray()) {
					String value = jsonObj.get(key).toString();
					doc.putAttribute(fieldName, value);
				} //
				else if (elm.isJsonObject()) {
					String value = jsonObj.get(key).toString();
					doc.putAttribute(fieldName, value);
				} //
				else if (elm.isJsonPrimitive()) {
					JsonPrimitive p = (JsonPrimitive) elm;
					if (p.isString()) {
						String value = jsonObj.get(key).getAsString();
						doc.putAttribute(fieldName, value);
					} else if (p.isNumber()) {
						Number value = jsonObj.get(key).getAsNumber();
						doc.putAttribute(fieldName, value);
					} else {
						String value = jsonObj.get(key).toString();
						doc.putAttribute(fieldName, value);
					}
				} //
				else {
					String value = jsonObj.get(key).toString();
					doc.putAttribute(fieldName, value);
				}
			}
		}
		return doc;
	}

	private static void copyAttributes(Document doc, JsonObject jsonObj) {
		for (String key : doc.getAttributeKeys()) {
			if (doc.getAttribute(key) instanceof Number) {
				jsonObj.addProperty(key, doc.getAttributeAsNumber(key));
			} else if (doc.getAttribute(key) instanceof Date) {
				Date dd = doc.getAttributeAsDate(key);
				jsonObj.addProperty(key, sdf.format(dd));
			} else {
				jsonObj.addProperty(key, doc.getAttribute(key).toString());
			}
		}
	}

	/**
	 * read documents from line separated json
	 * 
	 * @param file UTF-8 Line Separated Json File
	 * @return documents parsed from file
	 * @throws IOException on Error
	 * @since 1.2.1.0
	 */
	static public List<Document> readFromLineSeparatedJson(File file) throws IOException {
		ArrayList<Document> docs = new ArrayList<>();
		List<String> lines = FileUtils.readLines(file, "UTF-8");

		for (String line : lines) {
			Document doc = toDocument(line);
			docs.add(doc);
		}
		return docs;

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
	 * @since 1.3
	 * @param doc target Document
	 * @return Json String of Document
	 */
	static public JsonObject toJsonObject(Document doc) {
		JsonObject jsonObj = new JsonObject();
		copyAttributes(doc, jsonObj);

		{
			JsonArray arr = new JsonArray();
			for (Keyword kwd : doc.getKeywords()) {
				if (kwd instanceof KeywordWithDependency == false) {
					arr.add(toJsonObject(kwd));
				}
			}
			jsonObj.add("keywords", arr);
		}
		return jsonObj;
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

	/**
	 * @since 1.3
	 * @param doc target Document
	 * @return Json String of Document
	 */
	static public JsonObject toJsonObjectForIndex(Document doc) {
		JsonObject jsonObj = new JsonObject();

		copyAttributes(doc, jsonObj);

		HashMap<String, ArrayList<String>> kwMap = new LinkedHashMap<String, ArrayList<String>>();

		{
			for (Keyword kwd : doc.getKeywords()) {
				String facet = kwd.getFacet();

				if (kwMap.containsKey(facet)) {
					kwMap.get(facet).add(kwd.getLex());
				} else {
					ArrayList<String> list = new ArrayList<String>();
					list.add(kwd.getLex());
					kwMap.put(facet, list);
				}
			}
			for (String key : kwMap.keySet()) {
				String facet2 = key.toLowerCase().replace(".", "_");
				JsonArray arr = new JsonArray();
				for (String lex : kwMap.get(key)) {
					arr.add(lex);
				}
				jsonObj.add(facet2, arr);
			}
		}
		return jsonObj;
	}

	/**
	 * @since 1.1
	 * @param doc target Document
	 * @return Json String of Document
	 */
	static public String toJsonString(Document doc) {
		JsonObject jsonObj = toJsonObject(doc);
		Gson gson = new Gson();
		return gson.toJson(jsonObj);
	}

	/**
	 * @since 1.3.1.0
	 * @param doc target Document
	 * @return Pretty Json String of Document
	 */
	static public String toPrettyJsonString(Document doc) {
		return JsonUtils.prettyPrint(toJsonString(doc));
	}

	/**
	 * @since 1.1
	 * @param kwd target Keyword
	 * @return Json String of Keyword
	 */
	static public String toJsonString(Keyword kwd) {

		JsonObject json = new JsonObject();

		json.addProperty("facet", kwd.getFacet());
		json.addProperty("lex", kwd.getLex());
		json.addProperty("str", kwd.getStr());
		json.addProperty("begin", kwd.getBegin());
		json.addProperty("end", kwd.getEnd());

		json.addProperty("@classname", kwd.getClass().getCanonicalName());

		return json.toString();
	}

	/**
	 * Convert to XML
	 * 
	 * @param doc to be converted
	 * @return XML of document
	 */
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

	/**
	 * Convert to XML
	 * 
	 * @param kwd to be converted
	 * @return XML of Keyword
	 */
	static public String toXml(Keyword kwd) {
		StringBuilder xml = new StringBuilder();
		xml.append("<w ");
		xml.append(toXmlAttributes(kwd));
		xml.append(" />");
		return xml.toString();
	}

	/**
	 * Convert to XML
	 * 
	 * @param kwd to be converted
	 * @return XML of document
	 */
	static public String toXmlAttributes(Keyword kwd) {
		StringBuilder xml = new StringBuilder();
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

		return xml.toString();

	}

	/**
	 * Write documents as Line Separated Json
	 * 
	 * @param docs Document to write to file
	 * @param file to write
	 * @throws IOException on error writing file
	 * @since 1.2.1.0
	 */
	static public void writeAsLineSeparatedJson(List<Document> docs, File file) throws IOException {

		File parentDir = file.getParentFile();
		if (parentDir.exists() == false) {
			FileUtils.forceMkdir(parentDir);
		}

		for (Document doc : docs) {
			String json = toJsonString(doc);
			String encoding = "UTF-8";
			boolean append = true;
			FileUtils.write(file, json + "\n", encoding, append);
		}

	}

	/**
	 * Write documents as Line Separated Json
	 * 
	 * @param doc  Document to write to file
	 * @param file to write
	 * @throws IOException on error writing file
	 * @since 1.2.1.0
	 */
	static public void writeAsLineSeparatedJson(Document doc, File file) throws IOException {

		File parentDir = file.getParentFile();
		if (parentDir.exists() == false) {
			FileUtils.forceMkdir(parentDir);
		}

		{
			String json = toJsonString(doc);
			String encoding = "UTF-8";
			boolean append = true;
			FileUtils.write(file, json + "\n", encoding, append);
		}

	}

}
