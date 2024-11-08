package nlp4j.webcrawler.caa.v202411;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Element;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import nlp4j.NlpServiceResponse;
import nlp4j.http.Base64Response;
import nlp4j.http.HttpClient;
import nlp4j.http.HttpClientBuilder;
import nlp4j.http.HttpUtils;
import nlp4j.util.DateUtils;
import nlp4j.util.JsonObjectUtils;
import nlp4j.util.JsonUtils;
import nlp4j.util.MapBuilder;

public class CaaRecallCrawler202411Main2 {
	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	public static void main(String[] args) throws Exception {

		// https://www.recall.caa.go.jp/result/index.php?search=&viewCount=10&screenkbn=01&viewCountdden=10&portarorder=2&actionorder=0&pagingHidden=1

//		String url = "https://www.recall.caa.go.jp/result/detail.php?rcl=00000033030&screenkbn=01";

//		String url = "https://www.recall.caa.go.jp/result/detail.php?rcl=00000033029&screenkbn=01";

//		String url = "https://www.recall.caa.go.jp/result/detail.php?rcl=00000032937&screenkbn=01";
		String url = "https://www.recall.caa.go.jp/result/detail.php?rcl=00000033028&screenkbn=01";

		JsonObject jox = new JsonObject();
		jox.addProperty("url", url);

		JsonArray attrs = new JsonArray();
		jox.add("attrs", attrs);

		boolean appendImage = false;

		StringBuilder md = new StringBuilder();

		try (HttpClient client = (new HttpClientBuilder()).build();) {

			logger.info("accessing: " + url);

			NlpServiceResponse res = client.get(url);

			logger.info("response: " + res.getResponseCode());

			if (res.getResponseCode() == 200) {
				String responseBody = res.getOriginalResponseBody();

				System.err.println(responseBody);

				System.err.println("---");

				jox.addProperty("timestamp", DateUtils.toISO8601());

				List<String> contents = new ArrayList<String>();
				for (String line : responseBody.split("\\n")) {
					line = line.trim();
					if (line.startsWith("contentsText")) {
						String json = line.substring("contentsText = '".length(), line.length() - 2);
						json = json.replace("\\\"", "\"").replace("\\\\n", "\\n");
						JsonObject jo = (new Gson()).fromJson(json, JsonObject.class);
						String text = jo.get("ops").getAsJsonArray().get(0).getAsJsonObject().get("insert")
								.getAsString();
						contents.add(text);
					}
				}
				{
					System.err.println("" + contents.get(0)); // 連絡先
					System.err.println("" + contents.get(1)); // 対応方法
					System.err.println("" + contents.get(2)); // 対象の特定情報
					System.err.println("" + contents.get(3)); // 備考
				}

				System.err.println("---");

				org.jsoup.nodes.Document jsDoc = Jsoup.parse(responseBody); // throws IOException

				{ // タイトル
					String selector = "#contents_inner > div.detail_title";
					Elements els = jsDoc.select(selector);
					System.err.println(els.text());
					String att = "タイトル";
					attrs.add(att);
					md.append("# " + att + "\n" + els.text() + "\n");
				}

				{ // 商品名
					String selector = "#contents_inner > div.detail_box_top.clearfix > div.detail_box_left > ul > li:nth-child(1) > span.detail_text";
					Elements els = jsDoc.select(selector);
					System.err.println(els.text());
					String att = "商品名";
					attrs.add(att);
					md.append("# " + att + "\n" + els.text() + "\n");
				}
				{ // 連絡先
					String att = "連絡先";
					attrs.add(att);
					md.append("# " + att + "\n" + contents.get(0) + "\n");
				}
				{ // 対応方法
					String att = "対応方法";
					attrs.add(att);
					md.append("# " + att + "\n" + contents.get(1) + "\n");
				}
				{ // 対応開始日
					String selector = "#contents_inner > div.detail_box_top.clearfix > div.detail_box_left > ul > li:nth-child(4) > span.detail_text";
					Elements els = jsDoc.select(selector);
					System.err.println(els.text());
					String att = "対応開始日";
					attrs.add(att);
					md.append("# " + att + "\n" + els.text() + "\n");
				}
				{ // 対象の特定情報
					String att = "対象の特定情報";
					attrs.add(att);
					md.append("# " + att + "\n" + contents.get(2) + "\n");
				}
				{ // 参照情報
//					String selector = "#contents_inner > div.detail_box_bottom.clearfix > ul > li:nth-child(2) > span.detail_text.clearfix.quilltext";
//					Elements els = jsDoc.select(selector);
//					System.err.println(els.text());
//					attrs.add("参照情報");
				}
				{ // 参照情報リンク
					String selector = "#contents_inner > div.detail_box_bottom.clearfix > ul > li:nth-child(2) > span.detail_text.clearfix.quilltext > span > a";
					Elements els = jsDoc.select(selector);
					System.err.println(els.attr("href"));
					String att = "参照情報";
					attrs.add(att);
					md.append("# " + att + "\n" + els.attr("href") + "\n");
				}
				{ // 備考
					String att = "備考";
					attrs.add(att);
					md.append("# " + att + "\n" + contents.get(3) + "\n");
				}

				{ // 管理番号
					String selector = "#contents_inner > div.control_number";
					Elements els = jsDoc.select(selector);
					System.err.println(els.text());
					String att = "管理番号";
					attrs.add(att);
					md.append("# " + att + "\n" + els.text() + "\n");
				}

				if (appendImage) { // 画像

					String selector = "#contents_inner > div.detail_box_top.clearfix > div.detail_box_right > ul.detail_main_img > li > a";
					Elements els = jsDoc.select(selector);
					System.err.println(els.size());
					JsonArray images = new JsonArray();
					for (int n = 0; n < els.size(); n++) {
						String path = els.get(n).attr("href");
						String imageURL = (new URL(new URL(url), path)).toString();
						System.err.println(imageURL);
						Base64Response r = HttpUtils.fetchImageAsBase64(imageURL);
						JsonObject img = new JsonObject();
						img.addProperty("url", imageURL);
						img.addProperty("mimeType", r.getMimeType());
						img.addProperty("data", r.getData());
						images.add(img);
					}
					jox.add("images", images);
				}

			} //
			else {
				System.err.println(res.getResponseCode());
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		jox.addProperty("md", md.toString());

		System.err.println(JsonUtils.prettyPrint(jox));

	}

}
