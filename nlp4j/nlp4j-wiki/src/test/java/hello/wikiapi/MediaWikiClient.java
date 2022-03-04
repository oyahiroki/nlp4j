package hello.wikiapi;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import nlp4j.impl.DefaultNlpServiceResponse;
import nlp4j.util.HttpClient;

public class MediaWikiClient {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	private HttpClient client = new HttpClient();

	private String host;

	private boolean fetchSubCategory = false;

	public MediaWikiClient(String host) {
		this.host = host;
	}

	public List<String> getPageTitlesByCategory(String category) throws IOException {
		List<String> titles = new ArrayList<>();

		String from = "";

		for (int x = 0; x < 100; x++) {
			logger.info("x=" + x);
			String url = "https://" //
//					+ "en.wiktionary.org"
					+ host //
					+ "/w/api.php" //
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

			DefaultNlpServiceResponse res = client.get(url);

			// content-type
			JsonObject jo = res.getAsJsonObject();
			{ // list pages
				JsonArray pages = jo.get("query").getAsJsonObject().get("categorymembers").getAsJsonArray();
				for (int n = 0; n < pages.size(); n++) {
					String type = pages.get(n).getAsJsonObject().get("type").getAsString();
					logger.debug("type=" + type);
					String title = pages.get(n).getAsJsonObject().get("title").getAsString();
					if ("subcat".equals(type) && fetchSubCategory == true) {
						List<String> ss = getPageTitlesByCategory(title);
						titles.addAll(ss);
					} //
					else if ("page".equals(type)) {
						if (titles.contains(title) == false) {
							titles.add(title);
						}
					}
				}
//				System.err.println(titles.size());
			}
			{ // fetch continue
				if (jo.get("continue") == null) {
					break;
				}
				String cmcontinue = jo.get("continue").getAsJsonObject().get("cmcontinue").getAsString()
						.split("\\|")[1];
//				System.err.println(cmcontinue);
				from = cmcontinue;
			}

//			System.err.println(JsonUtils.prettyPrint(res.getOriginalResponseBody()));
//			System.err.println(XmlUtils.prettyFormatXml(res.getOriginalResponseBody()));

		}
		return titles;
	}

	public static void main(String[] args) throws Exception {
		{
//			String host = "en.wiktionary.org";
//			String category = "Category:en:Medicine";
//			category = "Category:en:Medicinex";
//			category = "Category:en:Auto_parts";
//
//			MediaWikiClient client = new MediaWikiClient(host);
//
//			List<String> titles = client.getPageTitlesByCategory(category);
//
//			for (String title : titles) {
//				System.err.println(title);
//			}
//			System.err.println(titles.size());
		}
		{
			String host = "en.wikipedia.org";
			String category = "Category:Auto_parts";

			MediaWikiClient client = new MediaWikiClient(host);

			List<String> titles = client.getPageTitlesByCategory(category);

			for (String title : titles) {
				System.err.println(title);
			}
			System.err.println(titles.size());
		}

	}

}
