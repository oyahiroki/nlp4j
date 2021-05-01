/**
 * 
 */
package nlp4j.webcrawler.caa;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import nlp4j.Document;
import nlp4j.impl.DefaultDocument;
import nlp4j.util.DocumentUtil;

/**
 * @author Hiroki Oya
 * @created_at 2021-04-30
 */
public class CaaParser {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	/**
	 * Parse recall info HTML <br>
	 * https://www.recall.caa.go.jp/result/detail.php?rcl=00000001312
	 * 
	 * @param html
	 * @return
	 */
	public Document parse(String html) {

		if (html == null || html.trim().isEmpty()) {
			return null;
		}

		DefaultDocument doc = new DefaultDocument();

		org.jsoup.nodes.Document jsDoc = Jsoup.parse(html); // throws IOException

		Elements el = jsDoc.select("#contents");

		{ // detail title
			Elements el_detail_title = jsDoc.select(".detail_title");
			for (Element e : el_detail_title) {
				String title = e.select("h3").get(0).text().trim();
				String description = e.select("p").get(0).text().trim();
				doc.putAttribute("title", title);
				doc.putAttribute("description", description);
			}
		}
		{ // detail_box_left
			Element el_detail_box_left = jsDoc.select(".detail_box_left").first();
			Elements lis = el_detail_box_left.select("li");
			// FOR EACH LIST ELEMENT
			for (Element li : lis) {
				String cap = null;
				{ // PARSE cap
					cap = li.select(".detail_cap").first().text().trim();
				}
				String text = null;
				{ // PARSE text
					text = li.select(".detail_text").first().text().trim();
					if (li.select("script") != null && li.select("script").size() > 0) {
						String scriptBody = li.select("script").first().data();
						for (String scriptLine : scriptBody.split("\n")) {
							if (scriptLine.trim().startsWith("contentsText")) {
								String objText = extractText(scriptLine);
								text = text + objText;
							}
						}
					}
				}
				if (cap != null && text != null) {
					doc.putAttribute(cap.trim(), text.trim());
				}
			} // END OF FOR EACH LIST ELEMENT

		} // END OF detail_box_left

		{ // detail_box_bottom
			Element el_detail_box = jsDoc.select(".detail_box_bottom").first();
			Elements lis = el_detail_box.select("li");
			// FOR EACH LIST ELEMENT
			for (Element li : lis) {
				String cap = null;
				{ // PARSE cap
					cap = li.select(".detail_cap").first().text().trim();
				}
				String text = null;
				{ // PARSE text
					text = li.select(".detail_text").first().text().trim();
					if (li.select("script") != null && li.select("script").size() > 0) {
						String scriptBody = li.select("script").first().data();
						for (String scriptLine : scriptBody.split("\n")) {
							if (scriptLine.trim().startsWith("contentsText")) {
								String objText = extractText(scriptLine);
								text = text + objText;
							}
						}
					}
				}
				if (cap != null && text != null) {
					doc.putAttribute(cap.trim(), text.trim());
				}
			} // END OF FOR EACH LIST ELEMENT
		} // END OF detail_box_left

		return doc;
	}

	/**
	 * Extract text from JavaScript Code
	 * 
	 * @param scriptLine
	 * @return
	 */
	private String extractText(String scriptLine) {

		if (scriptLine == null) {
			return null;
		}

		scriptLine = scriptLine.substring(scriptLine.indexOf("'") + 1, scriptLine.lastIndexOf("'"));

		// エスケープしなくていいのにエスケープされている場合がある
		if (scriptLine.startsWith("{\\\"")) {
			scriptLine = StringEscapeUtils.unescapeJson(scriptLine);
		}

		Gson gson = new Gson();

		JsonObject contentsTextObj = gson.fromJson(scriptLine, JsonObject.class);

		String objText = contentsTextObj.get("ops").getAsJsonArray().get(0).getAsJsonObject() //
				.get("insert").getAsString();

		objText = StringEscapeUtils.unescapeJson(objText);

		return objText;

	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		String baseDir = "C:/usr/local/nlp4j/collections/caa/data/html";
		File directory = new File(baseDir);
		String[] extensions = { "html" };
		boolean recursive = true;
		Collection<File> files = FileUtils.listFiles(directory, extensions, recursive);

		int count = 0;

		Date date = new Date();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		String dt = sdf.format(date);

		File outJson = new File(
				String.format("C:/usr/local/nlp4j/collections/caa/data/json/caa_recall_%s_json.txt", dt));

		for (File file : files) {

			if (file.length() == 0) {
				continue;
			}

			String htmlData = FileUtils.readFileToString(file, "UTF-8");
			htmlData = htmlData.trim();

			if (htmlData.isEmpty()) {
				continue;
			}

			String fileName = file.getName();
			System.err.println(fileName);

			int idNum = CaaFileUtil.getIdNum(fileName);
			String url = CaaRecallDownloader.getUrl(idNum);

			CaaParser parser = new CaaParser();
			Document doc = parser.parse(htmlData);
			doc.putAttribute("url", url);
			String hexString = DigestUtils.md5Hex(htmlData);
			doc.putAttribute("md5", hexString);
			doc.putAttribute("id", idNum);

			System.err.println(DocumentUtil.toPrettyJsonStringShort(doc));

			DocumentUtil.writeAsLineSeparatedJson(doc, outJson);

			count++;
			if (count > 10) {
//				break;
			}
		}

	}

}
