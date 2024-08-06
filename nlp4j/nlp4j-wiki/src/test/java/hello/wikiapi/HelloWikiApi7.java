package hello.wikiapi;

import java.io.IOException;
import java.net.URLEncoder;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import nlp4j.NlpServiceResponse;
import nlp4j.http.HttpClient;
import nlp4j.http.HttpClientBuilder;
import nlp4j.util.JsonUtils;

public class HelloWikiApi7 {

	public static void main(String[] args) throws IOException {

		String category = "Category:en:Medicine";

		category = "Category:en:Auto_parts";

		int depth = 0;

		list(category, depth);

		// サブカテゴリをリストする

	}

	private static void list(String category, int depth) throws IOException {

		try (HttpClient client = (new HttpClientBuilder()).build();) {
			// see
			// https://www.mediawiki.org/wiki/API:Categorymembers

			// http だと 301が返る

			String from = "";

			String url = "https://en.wiktionary.org/w/api.php" //
					+ "?"//
					+ "format=json"//
//				+ "format=xml"//
					+ "&action=query"//
					+ "&list=categorymembers"//
					+ "&cmtitle=" + category//
					+ "&cmlimit=500"// 1-500
//				+ "&cmsort=sortkey"//
					+ "&cmtype=subcat"// //Default: page|subcat|file
//				+ "&cmtype=page|subcat"// //Default: page|subcat|file
					+ "&cmprop=ids" + URLEncoder.encode("|", "UTF-8") + "title" + URLEncoder.encode("|", "UTF-8")
					+ "sortkey" + URLEncoder.encode("|", "UTF-8") + "sortkeyprefix" + URLEncoder.encode("|", "UTF-8")
					+ "type" + URLEncoder.encode("|", "UTF-8") + "timestamp" //
//				+ "&cmprop=title|sortkey" //
//				+ "&cmstarthexsortkey=" + from //
			;//

			// action: query: query Fetch data from and about MediaWiki.
			// format: One of the following values: json, jsonfm, none, php, phpfm, rawfm,
			// xml, xmlfm

			System.err.println(url);

			System.err.println(url.length());

			NlpServiceResponse res = client.get(url);

//		System.err.println(res.getResponseCode());

//		System.err.println(res.getHeaders());

			// content-type

//		System.err.println(XmlUtils.prettyFormatXml(res.getOriginalResponseBody()));
			System.err.println(JsonUtils.prettyPrint(res.getOriginalResponseBody()));

			JsonObject jo = res.getAsJsonObject();
//		System.err.println(JsonUtils.prettyPrint(jo));

			{ // list pages
				JsonArray pages = jo.get("query").getAsJsonObject().get("categorymembers").getAsJsonArray();
				for (int n = 0; n < pages.size(); n++) {
					String type = pages.get(n).getAsJsonObject().get("type").getAsString();
					if (type != null && type.equals("subat") == false) {
						continue;
					}
					String title = pages.get(n).getAsJsonObject().get("title").getAsString();
					System.err.println("\t".repeat(depth) + title);
					list(title, depth + 1);
				}
			}
//		System.err.println(JsonUtils.prettyPrint(res.getOriginalResponseBody()));

			System.err.println("Finished");

		}

	}

}
