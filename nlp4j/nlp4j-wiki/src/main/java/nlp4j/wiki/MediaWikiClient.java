package nlp4j.wiki;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import nlp4j.impl.DefaultNlpServiceResponse;
import nlp4j.util.HttpClient;

/**
 * <pre>
 * Media Wiki API Client
 * https://www.mediawiki.org/wiki/API:Tutorial/en
 * https://www.mediawiki.org/wiki/API:Tutorial/ja
 * 
 * created at: 2022-03-03
 * </pre>
 * 
 * @author Hiroki Oya
 *
 */
public class MediaWikiClient {

	/**
	 * en.wikipedia.org
	 */
	static public String HOST_EN_WIKIPEDIA_ORG = "en.wikipedia.org";
	/**
	 * en.wiktionary.org
	 */
	static public String HOST_EN_WIKTIONARY_ORG = "en.wiktionary.org";
	/**
	 * ja.wikipedia.org
	 */
	static public String HOST_JA_WIKIPEDIA_ORG = "ja.wikipedia.org";
	/**
	 * ja.wiktionary.org
	 */
	static public String HOST_JA_WIKTIONARY_ORG = "ja.wiktionary.org";

	private static final int MAX_QUERY_COUNT = 100;

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	private HttpClient client = new HttpClient();

	private String host;

	private boolean fetchSubCategory = false;

	/**
	 * @param host example: "en.wikipedia.org";
	 */
	public MediaWikiClient(String host) {
		this.host = host;
	}

	/**
	 * <pre>
	 * Query page titles by Category
	 * 
	 * <pre>
	 * 
	 * @see https://www.mediawiki.org/wiki/API:Categorymembers
	 * @param category example: "Category:Auto_parts"
	 * @return
	 * @throws IOException
	 */
	public List<String> getPageTitlesByCategory(String category) throws IOException {

		logger.info("category=" + category);

		List<String> titles = new ArrayList<>();

		String from = "";

		for (int x = 0; x < MAX_QUERY_COUNT; x++) {
			logger.info("count=" + x);

			// https://www.mediawiki.org/wiki/API:Categorymembers

			String url = "https://" + host + "/w/api.php";

			// action: query: query Fetch data from and about MediaWiki.
			// format: One of the following values: json, jsonfm, none,
			// php, phpfm, rawfm,
			// xml, xmlfm

			Map<String, String> params = new LinkedHashMap<>();
			{
				params.put("format", "json");
				params.put("action", "query");
				params.put("list", "categorymembers");// https://www.mediawiki.org/wiki/API:Categorymembers
				params.put("cmtitle", category);
				params.put("cmlimit", "500");
				params.put("cmsort", "sortkey");
				params.put("cmprop", "ids|title|sortkey|sortkeyprefix|type|timestamp");
				params.put("cmstarthexsortkey", from);
			}

			DefaultNlpServiceResponse res = client.get(url, params);

			// content-type
			JsonObject jo = res.getAsJsonObject();
			{ // list pages
				JsonArray pages = jo.get("query").getAsJsonObject().get("categorymembers").getAsJsonArray();
				for (int n = 0; n < pages.size(); n++) {
					String type = pages.get(n).getAsJsonObject().get("type").getAsString();
					String title = pages.get(n).getAsJsonObject().get("title").getAsString();
					logger.info("type=" + type + ",title=" + title);

					// fetchSubCategory??
					if ("subcat".equals(type) && this.fetchSubCategory == true) {
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

	public void setFetchSubCategory(boolean fetchSubCategory) {
		this.fetchSubCategory = fetchSubCategory;
	}

}
