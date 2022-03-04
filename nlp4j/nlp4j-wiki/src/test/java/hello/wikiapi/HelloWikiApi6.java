package hello.wikiapi;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import nlp4j.impl.DefaultNlpServiceResponse;
import nlp4j.util.HttpClient;
import nlp4j.util.JsonUtils;
import nlp4j.util.XmlUtils;

public class HelloWikiApi6 {

	public static void main(String[] args) throws IOException {

		// see
		// https://www.mediawiki.org/wiki/API:Categorymembers

		// http だと 301が返る

		String from = "";

		List<String> titles = new ArrayList<>();

		String category = "Category:en:Medicine";
		
		category = "Category:en:Auto_parts";

		for (int x = 0; x < 100; x++) {
			System.err.println("x=" + x);
			String url = "https://en.wiktionary.org/w/api.php" //
					+ "?"//
					+ "format=json"//
					+ "&action=query"//
					+ "&list=categorymembers"//
					+ "&cmtitle=" + category//
					+ "&cmlimit=500"// 1-500
					+ "&cmsort=sortkey"//
					+ "&cmprop=ids|title|sortkey|sortkeyprefix|type|timestamp" //
					+ "&cmstarthexsortkey=" + from //
			;//

			// action: query: query Fetch data from and about MediaWiki.
			// format: One of the following values: json, jsonfm, none, php, phpfm, rawfm,
			// xml, xmlfm
			HttpClient client = new HttpClient();
			DefaultNlpServiceResponse res = client.get(url);
			// content-type
			JsonObject jo = res.getAsJsonObject();
			{ // list pages
				JsonArray pages = jo.get("query").getAsJsonObject().get("categorymembers").getAsJsonArray();
				for (int n = 0; n < pages.size(); n++) {
					String title = pages.get(n).getAsJsonObject().get("title").getAsString();
					System.err.println(title);
					if (titles.contains(title) == false) {
						titles.add(title);
					}
				}
				System.err.println(titles.size());
			}
			{ // fetch continue
				if (jo.get("continue") == null) {
					break;
				}
				String cmcontinue = jo.get("continue").getAsJsonObject().get("cmcontinue").getAsString()
						.split("\\|")[1];
				System.err.println(cmcontinue);
				from = cmcontinue;
			}

//			System.err.println(JsonUtils.prettyPrint(res.getOriginalResponseBody()));
//			System.err.println(XmlUtils.prettyFormatXml(res.getOriginalResponseBody()));

		}

		File outFile = File.createTempFile("out", ".txt");
		System.err.println(outFile.getAbsolutePath());
		FileUtils.write(outFile, String.join("\r\n", titles), "UTF-8", false);

	}

}
