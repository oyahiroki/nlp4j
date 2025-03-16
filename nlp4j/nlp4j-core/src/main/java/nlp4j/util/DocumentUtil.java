package nlp4j.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.stream.Stream;

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
import nlp4j.crawler.CsvFileStreamCrawler;
import nlp4j.impl.DefaultDocument;
import nlp4j.impl.DefaultKeyword;

/**
 * @author Hiroki Oya
 * @since 1.1
 *
 */
public class DocumentUtil {

	static private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	static public final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

	static {
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
	}

	/**
	 * @param doc
	 * @return
	 * @since 1.3.7.13
	 */
	static public Map<String, Object> toMap(Document doc) {
		Map<String, Object> m = new HashMap<String, Object>();

		doc.getAttributeKeys().forEach(k -> { //
			Object o = doc.getAttribute(k); //
			m.put(k, o); //
		});

		return m;
	}

	/**
	 * created on 2022-11-19
	 * 
	 * @param doc
	 * @param lex
	 * @return doc contains keyword of lex
	 */
	static public boolean containsKeyword(Document doc, String facet, String lex) {
		Set<String> lexs = new HashSet<String>();
		for (Keyword kwd : doc.getKeywords(facet)) {
			if (kwd.getLex() != null) {
				lexs.add(kwd.getLex());
			}
		}
		return lexs.contains(lex);
	}

	/**
	 * Copy attributes of document to JSON Object
	 * 
	 * @param doc
	 * @param jsonObj
	 */
	@SuppressWarnings("unchecked")
	private static void copyAttributes(Document doc, JsonObject jsonObj) {
		if (doc == null) {
			return;
		}
		// FOR EACH DOCUMENT ATTRIBUTE
		for (String key : doc.getAttributeKeys()) {
			Object obj = doc.getAttribute(key);
			// NUMBER
			if (obj instanceof Number) {
				jsonObj.addProperty(key, doc.getAttributeAsNumber(key));
			}
			// DATE
			else if (obj instanceof Date) {
				Date dd = doc.getAttributeAsDate(key);
				jsonObj.addProperty(key, sdf.format(dd));
			}
			// JSON ELEMENT
			else if (obj instanceof JsonElement) {
				JsonElement je = (JsonElement) obj;
				jsonObj.add(key, je);
			}
			// JSON ARRAY
			else if (obj instanceof Collection) {
				JsonArray arr = new JsonArray();
				for (Object o : (Collection<Object>) obj) {
					if (o instanceof Number) {
						arr.add((Number) o);
					} else {
						arr.add(o.toString());
					}
				}
				jsonObj.add(key, arr);
			}
			// Array 2024-08-25
			else if (obj instanceof Object[]) {
				Object[] oo = (Object[]) obj;
				JsonArray arr = new JsonArray();
				for (Object o : oo) {
					if (o instanceof Number) {
						arr.add((Number) o);
					} else {
						arr.add(o.toString());
					}
				}
				jsonObj.add(key, arr);
			}
			// 2024-08-25
			else if (obj instanceof int[]) {
				int[] oo = (int[]) obj;
				JsonArray arr = new JsonArray();
				for (Object o : oo) {
					if (o instanceof Number) {
						arr.add((Number) o);
					} else {
						arr.add(o.toString());
					}
				}
				jsonObj.add(key, arr);
			}
			// 2024-08-25
			else if (obj instanceof long[]) {
				long[] oo = (long[]) obj;
				JsonArray arr = new JsonArray();
				for (Object o : oo) {
					if (o instanceof Number) {
						arr.add((Number) o);
					} else {
						arr.add(o.toString());
					}
				}
				jsonObj.add(key, arr);
			}
			// 2024-08-25
			else if (obj instanceof float[]) {
				float[] oo = (float[]) obj;
				JsonArray arr = new JsonArray();
				for (Object o : oo) {
					if (o instanceof Number) {
						arr.add((Number) o);
					} else {
						arr.add(o.toString());
					}
				}
				jsonObj.add(key, arr);
			}
			// 2024-08-25
			else if (obj instanceof double[]) {
				double[] oo = (double[]) obj;
				JsonArray arr = new JsonArray();
				for (Object o : oo) {
					if (o instanceof Number) {
						arr.add((Number) o);
					} else {
						arr.add(o.toString());
					}
				}
				jsonObj.add(key, arr);
			}

			// ELSE
			else if (doc.getAttribute(key) != null) {
				jsonObj.addProperty(key, doc.getAttribute(key).toString());
			}
		} // END OF FOR EACH DOCUMENT ATTRIBUTE
	}

	/**
	 * <pre>
	 * 名詞と動詞について「読み」をキーワードとして追加する
	 * 2022-06-18
	 * </pre>
	 * 
	 * @param doc
	 * @since 1.3.6.1
	 */
	static public void extractReading(Document doc) {
		{// よみがな展開
			List<Keyword> kk = new ArrayList<>();
			for (Keyword kwd : doc.getKeywords()) {
				if (kwd == null) {
					continue;
				} else {
					if (kwd.getLex() != null //
							// LEX 全ひらがな以外
							&& JaStringUtils.isAllHiragana(kwd.getLex()) == false //
							&& kwd.getReading() != null //
							// READING
							&& JaStringUtils.isAllKatakana(kwd.getReading()) //
							&& ("NOUN".equals(kwd.getUPos()) || "VERB".equals(kwd.getUPos())) //

					) {
						Keyword kwd1 = new DefaultKeyword();
						kwd1.setFacet("wiki.yomi");
						kwd1.setLex(JaStringUtils.toHiragana(kwd.getReading()));
						kwd1.setStr(kwd.getStr());
						kwd1.setUPos(kwd.getUPos());
						kwd1.setBegin(kwd.getBegin());
						kwd1.setEnd(kwd.getEnd());
						kk.add(kwd1);
					}
				}
			}
			doc.addKeywords(kk);
		}
	}

	/**
	 * @param url
	 * @return
	 * @throws IOException
	 * @since 1.3.7.18
	 */
	public static Stream<Document> stream(URL url) throws IOException {
		if ( //
		url.toString().endsWith(".csv") //
				|| url.toString().endsWith(".csv.gz") //
		) {
			CsvFileStreamCrawler crl = new CsvFileStreamCrawler();
			return crl.streamDocuments(url);
		} //

		else {
			BufferedReader br = IOUtils.br(url);
			return stream(br);
		}

	}

	/**
	 * @param gzipJSONL_plainJSONL_gzipCSV_plainCSV (plain JSONL | gzip JSONL )
	 * @return
	 * @throws IOException
	 * @since 1.3.7.15
	 */
	public static Stream<Document> stream(File gzipJSONL_plainJSONL_gzipCSV_plainCSV) throws IOException {

		// CSV
		if (gzipJSONL_plainJSONL_gzipCSV_plainCSV.getAbsolutePath().endsWith(".csv") //
				|| gzipJSONL_plainJSONL_gzipCSV_plainCSV.getAbsolutePath().endsWith(".csv.gz")) { //
			// CSV Stream
			return CsvUtils.stream(gzipJSONL_plainJSONL_gzipCSV_plainCSV);
		} //
			// JSON
		else {
			// gzipなどに対応
			BufferedReader br = nlp4j.util.FileUtils.openTextFileAsBufferedReader(gzipJSONL_plainJSONL_gzipCSV_plainCSV,
					"UTF-8");
			// JSON Stream
			return stream(br);
		}

	}

	public static Stream<Document> stream(BufferedReader br) throws IOException {
		return br.lines().map(line -> {
			try {
				return parseFromJson(line);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}

		}).onClose(() -> {
			try {
				br.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});
	}

	/**
	 * @since 1.3.1.0
	 * @param json to parse
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
				// Null -> do nothing
				if (elm.isJsonNull()) {
					// System.err.println("json null");
				} //
					// JsonArray -> List
				else if (elm.isJsonArray()) {
					// FIXED 2022-09-08
					JsonArray arr = elm.getAsJsonArray();
					List<Object> list = new ArrayList<>();
					for (int n = 0; n < arr.size(); n++) {
						JsonElement el = arr.get(n);
						if (el.isJsonObject()) {
							String value = jsonObj.get(key).toString();
							list.add(value); // ADD
						} //
						else if (el.isJsonPrimitive()) {
							JsonPrimitive p = (JsonPrimitive) el;
							if (p.isString()) {
								String value = p.getAsString();
								list.add(value); // ADD
							} //
							else if (p.isNumber()) {
								double d = p.getAsDouble();
								list.add(d); // ADD
							} //
							else {
								String value = p.getAsString();
								list.add(value); // ADD
							}
						} //
						else {
							String value = jsonObj.get(key).toString();
							list.add(value); // ADD
						}
					}
					doc.putAttribute(key, list);
				} //
					// JsonObject -> String
				else if (elm.isJsonObject()) {
					String value = jsonObj.get(key).toString();
					doc.putAttribute(fieldName, value);
				} //
					// JsonPrimitive -> primitive
				else if (elm.isJsonPrimitive()) {
					JsonPrimitive p = (JsonPrimitive) elm;
					// string
					if (p.isString()) {
						String value = jsonObj.get(key).getAsString();
						doc.putAttribute(fieldName, value);
					}
					// number
					else if (p.isNumber()) {
						Number value = jsonObj.get(key).getAsNumber();
						doc.putAttribute(fieldName, value);
					}
					// else(not string && not number)
					else {
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
		List<String> lines = nlp4j.util.FileUtils.readLines(file);

		for (String line : lines) {
			Document doc = DocumentUtil.toDocument(line);
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

		// FOR EACH KEYWORD
		if (jsonObj.get("keywords") != null) {
			JsonArray arr = jsonObj.get("keywords").getAsJsonArray();
			for (int n = 0; n < arr.size(); n++) {
				JsonElement elm = arr.get(n);
				Keyword kwd = gson.fromJson(elm, DefaultKeyword.class);
				doc.addKeyword(kwd);
			}
		} // END OF FOR EACH KEYWORD

		// FOR EACH MEMBER
		for (String key : jsonObj.keySet()) {
			if (key.equals("keywords")) {
				continue;
			} //
			else {
				JsonElement elm = jsonObj.get(key);
				// Primitive
				if (elm.isJsonPrimitive() == true) {
					JsonPrimitive jp = elm.getAsJsonPrimitive();
					if (jp.isBoolean()) {
						boolean b = jp.getAsBoolean();
						doc.putAttribute(key, b);
					} //
					else if (jp.isString()) {
						String s = jp.getAsString();
						doc.putAttribute(key, s);
					} else if (jp.isNumber()) {
						Number n = jp.getAsNumber();
						doc.putAttribute(key, n);
					}
				} //
					// Array
				else if (elm.isJsonArray() == true) {
					JsonArray arr = (JsonArray) elm;
					if (arr.size() == 0) {
						continue;
					}
					JsonElement firstElement = arr.get(0);
					// Array(Primitive)
					if (firstElement.isJsonPrimitive()) {
						JsonPrimitive jp0 = firstElement.getAsJsonPrimitive();
						// Array(Number)
						if (jp0.isNumber()) {
							List<Number> nn = new ArrayList<>();
							{
								for (int n = 0; n < arr.size(); n++) {
									nn.add(arr.get(n).getAsNumber());
								}
							}
							doc.putAttribute(key, nn);
						} //
							// Array(Else)
						else {
							List<String> ss = new ArrayList<>();
							{
								for (int n = 0; n < arr.size(); n++) {
									JsonElement je = arr.get(n);
									String v = je.getAsString();
									ss.add(v);
								}

							}
							doc.putAttribute(key, ss);
						}
					} //
						// Array(Else)
					else {
						List<String> ss = new ArrayList<>();
						{
							for (int n = 0; n < arr.size(); n++) {
								JsonElement je = arr.get(n);
								String v = je.getAsString();
								ss.add(v);
							}

						}
						doc.putAttribute(key, ss);
					}
				} else {
					doc.putAttribute(key, elm);
				}
			}
		} // END OF FOR EACH MEMBER
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

		{ // FOR EACH(KEYWORD)
			JsonArray arr = new JsonArray();
			for (Keyword kwd : doc.getKeywords()) {
				if (kwd instanceof KeywordWithDependency == false) {
					arr.add(toJsonObject(kwd));
				} //
				else {
// 2022-05-14				logger.info("Skip this keyword: " + kwd.getClass().getCanonicalName() + ": " + kwd.getLex());
					KeywordWithDependency kwdd = (KeywordWithDependency) kwd;
					arr.add(toJsonObject(kwdd));
				}
			}
			jsonObj.add("keywords", arr);
		} // END OF FOR EACH(KEYWORD)

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
	 * created at: 2022-05-14
	 * 
	 * @param kwd
	 * @return
	 */
	static public JsonObject toJsonObject(KeywordWithDependency kwd) {
		Gson gson = new Gson();
		String json = toJsonString(kwd);

		JsonObject obj = gson.fromJson(json, JsonObject.class);

		List<KeywordWithDependency> cc = kwd.getChildren();
		JsonArray children = new JsonArray();

		for (KeywordWithDependency c : cc) {
			JsonObject o = toJsonObject(c);
			children.add(o);
		}

		obj.add("children", children);

		return obj;
	}

	/**
	 * @since 1.3.1.0
	 * @param docs target Documents
	 * @return Json Array
	 */
	static public JsonArray toJsonObject(List<Document> docs) {

		JsonArray jsonArray = new JsonArray();

		for (Document doc : docs) {
			JsonObject json = toJsonObject(doc);
			jsonArray.add(json);
		}

		return jsonArray;
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
			if (doc.getKeywords() != null && doc.getKeywords().size() > 0) {
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
	 * @since 1.3.2.0
	 * @param doc target Document
	 * @return Pretty JSON String of Documents
	 */
	static public String toJsonPrettyString(Document doc) {

		JsonElement el = toJsonObject(doc);

		return JsonUtils.prettyPrint(el);

	}

	/**
	 * @since 1.3.1.0
	 * @param docs target Documents
	 * @return Pretty JSON String of Documents
	 */
	static public String toJsonPrettyString(List<Document> docs) {

		JsonElement jsonArray = toJsonObject(docs);

		return JsonUtils.prettyPrint(jsonArray);

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
	 * @since 1.1
	 * @param kwd target Keyword
	 * @return Json String of Keyword
	 */
	static public String toJsonString(Keyword kwd) {

		JsonObject json = new JsonObject();

		json.addProperty("facet", kwd.getFacet());
		json.addProperty("upos", kwd.getUPos()); // @since 1.3.1.0
		json.addProperty("lex", kwd.getLex());
		json.addProperty("str", kwd.getStr());
		if (kwd.getBegin() != Keyword.BEGIN_INIT) {
			json.addProperty("begin", kwd.getBegin());
		}
		if (kwd.getEnd() != Keyword.END_INIT) {
			json.addProperty("end", kwd.getEnd());
		}
		if (kwd.getCorrelation() != Keyword.CORRELATION_INIT) {
			json.addProperty("correlation", kwd.getCorrelation());
		}
		json.addProperty("@classname", kwd.getClass().getCanonicalName());

		return json.toString();
	}

	/**
	 * @since 1.3.1.0
	 * @param docs target Documents
	 * @return JSON String of Documents
	 */
	static public String toJsonString(List<Document> docs) {

		JsonArray jsonArray = toJsonObject(docs);

		return jsonArray.toString();

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
	 * @since 1.3.1.0
	 * @param doc target Document
	 * @return Pretty Json String of Document in short length
	 */
	static public String toPrettyJsonStringShort(Document doc) {

		StringBuilder sb = new StringBuilder();

		String json = JsonUtils.prettyPrint(toJsonString(doc));

		for (String js : json.split("\n")) {
			js = js.trim();
			if (js.length() > 80) {
				js = js.substring(0, 80) + "...";
			}
			sb.append(js + "\n");
		}

		return sb.toString();
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
	 * @param file
	 * @return
	 * @throws IOException
	 * @since 1.3.7.18
	 */
	public static List<Document> read(File file) throws IOException {
		if ( //
		file.getAbsolutePath().endsWith(".csv") //
				|| file.getAbsolutePath().endsWith(".csv.gz") //
		) {
			return CsvUtils.stream(file).toList();
		} else {
			return stream(file).toList();
		}
	}

}
