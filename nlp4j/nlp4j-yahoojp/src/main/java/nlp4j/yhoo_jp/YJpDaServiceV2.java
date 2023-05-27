package nlp4j.yhoo_jp;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import nlp4j.DefaultEnv;
import nlp4j.Keyword;
import nlp4j.KeywordWithDependency;
import nlp4j.NlpService;
import nlp4j.NlpServiceResponse;
import nlp4j.http.HttpClient5;

/**
 * Yahoo! Japan dependency analysis 日本語係り受け解析 <br>
 * NLP Service of Yahoo! Japan dependency analysis.
 * 
 * <pre>
 * https://developer.yahoo.co.jp/webapi/jlp/da/v1/parse.html
 * </pre>
 * 
 * 
 * @author Hiroki Oya
 * @version 2.0
 * @since 1.0
 *
 */
public class YJpDaServiceV2 implements NlpService {

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	String appID;
	static final String baseUrl = "https://jlp.yahooapis.jp/DAService/V2/parse";

	/**
	 * Yahoo! Japan dependency analysis 日本語係り受け解析
	 */
	public YJpDaServiceV2() {

		super();

		this.appID = System.getProperty("yhoo_jp.appid");

		if (this.appID == null) {
			// throw new RuntimeException("no appid");
			this.appID = DefaultEnv.YHOO_JP_API_ID;
			Exception e = new Exception("Please get your own APP_ID for Yahoo! Japan API "
					+ "and set as Dyhoo_jp.appid={your_app_id} " + "- https://e.developer.yahoo.co.jp/dashboard/");
			System.err.println("WARNING: " + e.getMessage());
		}
	}

	/**
	 * @param text 自然言語文字列（ヤフー構文解析の仕様は単一文だが、句点区切りの複数文も可とする）
	 * @return 自然言語処理の結果
	 * @throws IOException 例外発生時にスローされる
	 */
	public NlpServiceResponse process(String text) throws IOException {

		if (text == null || text.isEmpty() || text.trim().isEmpty()) {
			return null;
		}

		// https://e.developer.yahoo.co.jp/dashboard/
		// -Dyhoo_jp.appid=xxx

		String url = baseUrl;

		Map<String, String> requestHeader = new HashMap<>();
		requestHeader.put("User-Agent", "Yahoo AppID: " + appID);

		JsonObject requestBodyJson = new JsonObject();
		requestBodyJson.addProperty("id", "1");
		requestBodyJson.addProperty("jsonrpc", "2.0");
		requestBodyJson.addProperty("method", "jlp.daservice.parse");
		{
			JsonObject params = new JsonObject();
			params.addProperty("q", text);
			requestBodyJson.add("params", params);
		}

		try (nlp4j.http.HttpClient client = new HttpClient5();) {
			// User-Agent: Yahoo AppID: <あなたのアプリケーションID>

			NlpServiceResponse res = client.post(url, requestHeader, requestBodyJson.toString());

			logger.debug(res);
			logger.debug(res.getOriginalResponseBody());

			Gson gson = new Gson();

			JsonObject responseJson = gson.fromJson(res.getOriginalResponseBody(), JsonObject.class);

			YJpDaServiceResponseHandlerV2 handler = new YJpDaServiceResponseHandlerV2(text);

			handler.parseJson(responseJson.toString());

			ArrayList<KeywordWithDependency> roots = handler.getRoots();

			ArrayList<Keyword> kwds = new ArrayList<Keyword>();
			kwds.addAll(roots);
			res.setKeywords(kwds);

			return res;

		} catch (Exception e) {
			throw new IOException(e);
		}

	}

	/**
	 * Get response from dependency analysis service 日本語係り受け解析の結果を取得する
	 * 
	 * @param text 日本語の自然文文字列（単一の文である必要がある）
	 * @return 日本語係り受け解析の結果
	 * @throws IOException IOで発生した例外
	 */
	public ArrayList<KeywordWithDependency> getKeywords(String text) throws IOException {

		if (text == null) {
			System.err.println("INFO: text is null");
			return null;
		}

		NlpServiceResponse res = process(text);

		if (res != null) {

			logger.debug(res.getOriginalResponseBody());

			ArrayList<KeywordWithDependency> ret = new ArrayList<KeywordWithDependency>();

			for (Keyword kwd : res.getKeywords()) {
				ret.add((KeywordWithDependency) kwd);
			}

			return ret;
		} else {
			return null;
		}
	}

}
