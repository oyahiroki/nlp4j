package nlp4j.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
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

}
