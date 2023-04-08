package nlp4j.webcrawler.mlit;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.JsonObject;

public class W202304ConvertTempData {

	public static void main(String[] args) throws Exception {

		String fileName = "src/main/resources/data/" + "不具合情報検索結果｜自動車のリコール・不具合情報20230408.html";

		String encoding = "UTF-8";

		String baseUrl = "https://www.mlit.go.jp/jidosha/carinf/rcl/cgi-bin/cis_search.cgi";

		String outFileName = System.getenv("USERPROFILE") + "/git/nlp4j-data/data-mlit/"
				+ "mlit_carinfo-all_20230401_json.txt";
		File outFile = new File(outFileName);

		File file = new File(fileName);

		org.jsoup.nodes.Document document = Jsoup.parse(file, encoding, baseUrl);

		Elements elements = document.select("#r1 tr");

		System.err.println(elements.size());

		StringBuilder sb = new StringBuilder();

		for (int n = 1; n < elements.size(); n++) {
			Element el = elements.get(n);

			String tag = (n == 0) ? "th" : "td";

			Elements tds = el.select(tag);

//			for (int nn = 0; nn < tds.size(); nn++) {
//				String text = tds.get(nn).text();
//				System.err.println(text);
//			}

			JsonObject jsonData = new JsonObject();

			{
				jsonData.addProperty("発生日", tds.get(0).text());
				jsonData.addProperty("都道府県", tds.get(1).text());
				jsonData.addProperty("車名", tds.get(2).html().split("<br>")[0].trim());
				jsonData.addProperty("通称名", tds.get(2).html().split("<br>")[1].trim());
				{
					if (n == 0) {
//						String katashiki = tds.get(3).html().split("<br>")[0];
//						String gendoukikatashiki = tds.get(3).html().split("<br>")[1];
					} else {
						Elements lis = tds.get(3).select("li");
						{
							jsonData.addProperty("型式", lis.get(0).text());
							jsonData.addProperty("原動機型式", lis.get(1).text());
						}
					}
				}
				jsonData.addProperty("初度登録年", tds.get(4).text());
				jsonData.addProperty("走行距離", tds.get(5).text());
				jsonData.addProperty("装置名", tds.get(6).text());
				jsonData.addProperty("不具合発生時期", tds.get(7).text());
				jsonData.addProperty("申告内容", tds.get(8).text());
			}

			sb.append(jsonData.toString() + "\n");

		}

		FileUtils.write(outFile, sb.toString(), "UTF-8", false);

	}

}
