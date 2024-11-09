package nlp4j.webcrawler.caa.v202411;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import javax.lang.model.element.Element;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import com.google.gson.JsonObject;

import nlp4j.NlpServiceResponse;
import nlp4j.http.HttpClient;
import nlp4j.http.HttpClientBuilder;
import nlp4j.util.IOUtils;
import nlp4j.util.MapBuilder;

public class CaaRecallCrawler202411Main1 {
	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	static String url = "https://www.recall.caa.go.jp/result/index.php";

	public static void main(String[] args) throws Exception {

		// https://www.recall.caa.go.jp/result/index.php?search=&viewCount=10&screenkbn=01&viewCountdden=10&portarorder=2&actionorder=0&pagingHidden=1

		File outFile = File.createTempFile("nlp4j-caa-index-", "_json.txt");
		PrintWriter pw = IOUtils.pw(outFile);

		System.err.println(outFile.getAbsolutePath());

//		Map<String, String> params = (new MapBuilder<String, String>())//
//				.put("search", "") //
//				.put("screenkbn", "01") //
//				.put("category", "8") //
//				.build();

		int count = 60;

		int maxPageSkip = 10;

//		URLEncoder.encode("日産","UTF-8")

//		search: 
//			viewCount: 15
//			screenkbn: 01
//			category: 8
//			viewCountdden: 15
//			portarorder: 2
//			actionorder: 0
//			pagingHidden: 

//		search: 
//			viewCount: 15
//			screenkbn: 01
//			category: 8
//			viewCountdden: 15
//			portarorder: 2
//			actionorder: 0
//			pagingHidden: 1

		for (int x = 0; x <= maxPageSkip; x++) {
			extracted(pw, count, x);
		}

		pw.close();

		System.err.println(outFile.getAbsolutePath());
	}

	private static void extracted(PrintWriter pw, int count, int x) throws Exception {

		try (HttpClient client = (new HttpClientBuilder()).build();) {

			Map<String, String> params = Map.of( //
					"search", "", //
					"viewCount", "" + count, //
					"screenkbn", "01", //
					"category", "8", //
					"viewCountdden", "" + count, //
					"portarorder", "0", // 1: 古い順 2:新しい順
					"actionorder", "0", //
					"pagingHidden", "" + x // ←何ページスキップするか
			);

			logger.info("accessing: " + url);

			NlpServiceResponse res = client.post(url, params);

			logger.info("response: " + res.getResponseCode());

			if (res.getResponseCode() == 200) {
				String responseBody = res.getOriginalResponseBody();
//					System.err.println(responseBody);

				System.err.println("---");

				org.jsoup.nodes.Document jsDoc = Jsoup.parse(responseBody); // throws IOException

				String selector = "#contents_inner > div.search_result > div.search_result_main > table > tbody > tr";

				// #contents_inner > div.search_result > div.search_result_main > table > tbody
				// > tr:nth-child(1) > td:nth-child(1)

				Elements els = jsDoc.select(selector);

//				System.err.println(els.size());

				for (int n = 0; n < els.size(); n++) {
					org.jsoup.nodes.Element el = els.get(n);
//						Elements td1 = el.select(":nth-child(1)");
//						System.err.println(td1.text());
//						System.err.println(el.text());
//						System.err.println(el.html());
//						System.err.println("-");
					Elements tds = el.select("td");
					System.err.println("カテゴリー: " + tds.get(0).text()); // カテゴリー
//						System.err.println(tds.get(1).html()); // 画像
					System.err.println("画像URL: " + new URL(new URL(url), tds.get(1).select("img").attr("src"))); // 画像URL
//						System.err.println(tds.get(2).html()); // 件名とリンク
					URL url_link = new URL(new URL(url), tds.get(2).select("a").attr("href"));
					System.err.println("リンクURL: " + url_link); //
					System.err.println("リンクテキスト: " + tds.get(2).select("a").text()); //
					String date = tds.get(3).text();
					System.err.println("掲載日: " + date); // 掲載日

					System.err.println("対応開始日: " + tds.get(4).text()); // 対応開始日
					System.err.println("-");

//					JsonObject jo = new JsonObject();
//					{
//						jo.addProperty("url", url_link.toString());
//					}
//					pw.println(jo.toString());

					JsonObject jo = CaaRecallCrawlerForContents.crawl(url_link.toString());
					pw.println(jo);

					Thread.sleep(1000);
				}

			} //
			else {
				System.err.println(res.getResponseCode());
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
