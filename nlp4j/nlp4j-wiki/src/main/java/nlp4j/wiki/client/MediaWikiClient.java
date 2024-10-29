package nlp4j.wiki.client;

import java.io.Closeable;
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

import nlp4j.NlpServiceResponse;
import nlp4j.http.HttpClient;
import nlp4j.http.HttpClient5;
import nlp4j.http.HttpClientBuilder;
import nlp4j.util.HtmlUtils;
import nlp4j.util.JsonObjectUtils;
import nlp4j.util.JsonUtils;

/**
 * <pre>
 * Media Wiki API Client
 * https://www.mediawiki.org/wiki/API:Tutorial/en
 * API:チュートリアル
 * https://www.mediawiki.org/wiki/API:Tutorial/ja
 * 
 * created at: 2022-03-03
 * </pre>
 * 
 * @author Hiroki Oya
 *
 */
public class MediaWikiClient implements Closeable {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
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

	private HttpClient client = (new HttpClientBuilder()).build();

	private String host;

	private boolean fetchSubCategory = false;

	/**
	 * @param host example: "en.wikipedia.org";
	 */
	public MediaWikiClient(String host) {
		this.host = host;
	}

	@Override
	public void close() throws IOException {
		if (this.client != null) {
			this.client.close();
		}

	}

	/**
	 * <pre>
	 * API:テンプレートの展開
	 * API:Expandtemplates
	 * </pre>
	 * 
	 * @throws IOException
	 * @see https://www.mediawiki.org/wiki/API:Expandtemplates
	 */
	public void expandTemplates(String wikiText) throws IOException {

		// https://www.mediawiki.org/wiki/API:Categorymembers

		String url = "https://" + host + "/w/api.php";

		// action: query: query Fetch data from and about MediaWiki.
		// format: One of the following values: json, jsonfm, none,
		// php, phpfm, rawfm,
		// xml, xmlfm

		// api.php?action=expandtemplates&text={{Project:Sandbox}}&prop=wikitext [try in
		// ApiSandbox]

		Map<String, String> params = new LinkedHashMap<>();
		{
			params.put("action", "expandtemplates");
			params.put("text", wikiText);
			params.put("prop", "wikitext");
			params.put("format", "json");
		}

		NlpServiceResponse res = client.get(url, params);

		// content-type
		JsonObject jo = res.getAsJsonObject();

		System.err.println(jo);

		System.out.println(jo.get("expandtemplates").getAsJsonObject().get("wikitext").getAsString());

	}

	/**
	 * Parse wiki text
	 * 
	 * @param wikiText
	 * @throws IOException
	 * @see https://www.mediawiki.org/wiki/API:Parsing_wikitext/ja
	 */
	public MediaWikiApiResponse parse(String wikiText) throws IOException {
		String url = "https://" + "www.mediawiki.org" + "/w/api.php";
		Map<String, String> params = new LinkedHashMap<>();
		{
			params.put("action", "parse");
			params.put("format", "json");
			params.put("text", wikiText);
			params.put("contentmodel", "wikitext");
		}
		NlpServiceResponse res = client.get(url, params);
		// content-type
		JsonObject jo = res.getAsJsonObject();
		MediaWikiApiResponse r = new AbstractMediaWikiApiResponse(jo) {
			@Override
			public String getText() {
//				String html = jo.get("parse").getAsJsonObject().get("text").getAsJsonObject().get("*").getAsString();
				String html = JsonObjectUtils.query(jo, String.class, "/parse/text/*");
				html = HtmlUtils
						.removeHtmlComments("<html><body><div id=\"nlp4j_removecomments\">" + html + "</body></html>");
				return html;
			}
		};
		return r;
	}

	/**
	 * タイトルを指定してコンテンツを取得する<br>
	 * created_on: 2024-01-21
	 * 
	 * @param title
	 * @return
	 * @throws IOException
	 * @since 1.1.0.0
	 * 
	 */
	public String getPageContentByTitle(String title) throws IOException {

		if (logger.isDebugEnabled()) {
			logger.debug("title=" + title);
		}

		String url = "https://" + host + "/w/api.php";

		Map<String, String> params = new LinkedHashMap<>();
		{
			params.put("action", "query");
			params.put("prop", "revisions");
			params.put("titles", title);
			params.put("rvslots", "*");
			params.put("rvprop", "content");
			params.put("formatversion", "1");
			params.put("format", "json");
		}

		NlpServiceResponse res = client.get(url, params);

		// content-type
		JsonObject jo = res.getAsJsonObject();

		if (logger.isDebugEnabled()) {
			logger.debug(res.getOriginalResponseBody());
		}

		String key = jo //
				.get("query").getAsJsonObject() //
				.get("pages").getAsJsonObject().keySet() //
				.toArray(new String[0])[0];

		if (jo. //
				get("query").getAsJsonObject() //
				.get("pages").getAsJsonObject() //
				.get(key).getAsJsonObject().get("revisions") == null) {
			logger.info("null");
			return null;
		}

//		String wiki_content = jo. //
//				get("query").getAsJsonObject() //
//				.get("pages").getAsJsonObject() //
//				.get(key).getAsJsonObject() //
//				.get("revisions").getAsJsonArray() //
//				.get(0).getAsJsonObject() //
//				.get("slots").getAsJsonObject() //
//				.get("main").getAsJsonObject() //
//				.get("*").getAsString() //
//		;

		String wiki_content = JsonObjectUtils.query(jo, String.class,
				"/query/pages/" + key + "/revisions[0]/slots/main/*");

		return wiki_content;
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
				params.put("action", "query");
				params.put("list", "categorymembers");// https://www.mediawiki.org/wiki/API:Categorymembers
				params.put("cmtitle", category);
				params.put("format", "json");
				params.put("cmlimit", "500");
				params.put("cmsort", "sortkey");
				params.put("cmprop", "ids|title|sortkey|sortkeyprefix|type|timestamp");
				params.put("cmstarthexsortkey", from);
			}

			NlpServiceResponse res = client.get(url, params);

			// content-type
			JsonObject jo = res.getAsJsonObject();

//			System.err.println(JsonUtils.prettyPrint(res.getOriginalResponseBody()));
//			System.err.println(JsonUtils.prettyPrint(jo));

			{ // list pages
				JsonArray pages = jo.get("query").getAsJsonObject().get("categorymembers").getAsJsonArray();

				if (logger.isDebugEnabled()) {
					logger.debug(JsonUtils.prettyPrint(pages));
				}
//				System.err.println(JsonUtils.prettyPrint(pages));

				for (int n = 0; n < pages.size(); n++) {
					String type = pages.get(n).getAsJsonObject().get("type").getAsString();
					String title = pages.get(n).getAsJsonObject().get("title").getAsString();
					logger.debug("type=" + type + ",title=" + title);

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
	public List<String> getSubcategoryTitlesByCategory(int depth, List<String> titles, String parent_category,
			String category) throws IOException {

		if (parent_category == null) {
			parent_category = "";
		}

		logger.info("category=" + category);

//		List<String> titles = new ArrayList<>();

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

			NlpServiceResponse res = client.get(url, params);

			// content-type
			JsonObject jo = res.getAsJsonObject();
			{ // list pages
				JsonArray pages = jo.get("query").getAsJsonObject().get("categorymembers").getAsJsonArray();

				if (logger.isDebugEnabled()) {
					logger.debug(JsonUtils.prettyPrint(pages));
				}
				System.err.println(JsonUtils.prettyPrint(pages));

				for (int n = 0; n < pages.size(); n++) {
					String page_type = pages.get(n).getAsJsonObject().get("type").getAsString();
					String page_title = pages.get(n).getAsJsonObject().get("title").getAsString();
					logger.debug("type=" + page_type + ",title=" + page_title);

					// fetchSubCategory??
					if ("subcat".equals(page_type)
//							&& this.fetchSubCategory == true
					) {
//						List<String> ss = getPageTitlesByCategory(page_title);
//						titles.addAll(ss);
						if (titles.contains(parent_category + "/" + page_title) == false) {
							titles.add(parent_category + "/" + page_title);

							getSubcategoryTitlesByCategory(depth + 1, titles, parent_category + "/" + page_title,
									page_title);

						}
					} //
					else if ("page".equals(page_type)) {
//						if (titles.contains(page_title) == false) {
//							titles.add(page_title);
//						}
					}
				}
				// System.err.println(titles.size());
			}

			{ // fetch continue
				if (jo.get("continue") == null) {
					break;
				}
				String cmcontinue = jo.get("continue").getAsJsonObject().get("cmcontinue").getAsString()
						.split("\\|")[1];
				// System.err.println(cmcontinue);
				from = cmcontinue;
			}

			// System.err.println(JsonUtils.prettyPrint(res.getOriginalResponseBody()));
			// System.err.println(XmlUtils.prettyFormatXml(res.getOriginalResponseBody()));

		} // END OF FOR

		return titles;
	}

	/**
	 * 
	*/
	public JsonObject search(String q) throws IOException {

		String url = "https://" + host + "/w/api.php";

		// action: query: query Fetch data from and about MediaWiki.
		// format: One of the following values: json, jsonfm, none,
		// php, phpfm, rawfm,
		// xml, xmlfm

		Map<String, String> params = new LinkedHashMap<>();
		{
			params.put("action", "query");
			params.put("list", "search");
			params.put("srsearch", q);
			params.put("format", "json");
			params.put("utf8", "1");
		}

		NlpServiceResponse res = client.get(url, params);

		// content-type
		JsonObject jo = res.getAsJsonObject();

		return jo;
	}

	public void setFetchSubCategory(boolean fetchSubCategory) {
		this.fetchSubCategory = fetchSubCategory;
	}

	/**
	 * タイトルを指定してコンテンツを取得する<br>
	 * created_on: 2024-10-29
	 * 
	 * @param title
	 * @return
	 * @throws IOException
	 * @since 1.1.0.0
	 * 
	 */
	public String getPageContentByTitleAsHtml(String title) throws IOException {

		if (logger.isDebugEnabled()) {
			logger.debug("title=" + title);
		}

		String url = "https://" + host + "/w/api.php";

		Map<String, String> params = new LinkedHashMap<>();
		{
			params.put("action", "parse");
			params.put("page", title);
			params.put("format", "json");
			params.put("prop", "text");
		}

		NlpServiceResponse res = client.get(url, params);

		// content-type
		JsonObject jo = res.getAsJsonObject();

		System.err.println(res.getOriginalResponseBody());

//		if (logger.isDebugEnabled()) {
//			logger.debug(res.getOriginalResponseBody());
//		}
//
//		String key = jo //
//				.get("query").getAsJsonObject() //
//				.get("pages").getAsJsonObject().keySet() //
//				.toArray(new String[0])[0];
//
//		if (jo. //
//				get("query").getAsJsonObject() //
//				.get("pages").getAsJsonObject() //
//				.get(key).getAsJsonObject().get("revisions") == null) {
//			logger.info("null");
//			return null;
//		}
//
//		// String wiki_content = jo. //
//		// get("query").getAsJsonObject() //
//		// .get("pages").getAsJsonObject() //
//		// .get(key).getAsJsonObject() //
//		// .get("revisions").getAsJsonArray() //
//		// .get(0).getAsJsonObject() //
//		// .get("slots").getAsJsonObject() //
//		// .get("main").getAsJsonObject() //
//		// .get("*").getAsString() //
//		// ;
//
//		String wiki_content = JsonObjectUtils.query(jo, String.class,
//				"/query/pages/" + key + "/revisions[0]/slots/main/*");
//
//		return wiki_content;

		System.out.println(jo.get("parse").getAsJsonObject().get("text").getAsJsonObject().get("*").getAsString());

		return "";
	}

	/**
	 * タイトルを指定してコンテンツを取得する<br>
	 * created_on: 2024-10-29
	 * 
	 * @param title
	 * @return
	 * @throws IOException
	 * @since 1.1.0.0
	 * 
	 */
	public String getPageContentByTitleAsPlaintext(String title) throws IOException {

		if (logger.isDebugEnabled()) {
			logger.debug("title=" + title);
		}

		String url = "https://" + host + "/w/api.php";

		Map<String, String> params = new LinkedHashMap<>();
		{
			params.put("action", "query");
			params.put("titles", title);
			params.put("prop", "extracts");
			params.put("format", "json");
			params.put("explaintext", "1");
			params.put("redirects", "1");
		}

		NlpServiceResponse res = client.get(url, params);

		// content-type
		JsonObject jo = res.getAsJsonObject();

		System.err.println(res.getOriginalResponseBody());

		// if (logger.isDebugEnabled()) {
		// logger.debug(res.getOriginalResponseBody());
		// }
		//
		// String key = jo //
		// .get("query").getAsJsonObject() //
		// .get("pages").getAsJsonObject().keySet() //
		// .toArray(new String[0])[0];
		//
		// if (jo. //
		// get("query").getAsJsonObject() //
		// .get("pages").getAsJsonObject() //
		// .get(key).getAsJsonObject().get("revisions") == null) {
		// logger.info("null");
		// return null;
		// }
		//
		// // String wiki_content = jo. //
		// // get("query").getAsJsonObject() //
		// // .get("pages").getAsJsonObject() //
		// // .get(key).getAsJsonObject() //
		// // .get("revisions").getAsJsonArray() //
		// // .get(0).getAsJsonObject() //
		// // .get("slots").getAsJsonObject() //
		// // .get("main").getAsJsonObject() //
		// // .get("*").getAsString() //
		// // ;
		//
		// String wiki_content = JsonObjectUtils.query(jo, String.class,
		// "/query/pages/" + key + "/revisions[0]/slots/main/*");
		//
		// return wiki_content;

		System.out.println(jo.get("query").getAsJsonObject().get("pages").getAsJsonObject().get("135686")
				.getAsJsonObject().get("extract").getAsString());

		return "";
	}

}
