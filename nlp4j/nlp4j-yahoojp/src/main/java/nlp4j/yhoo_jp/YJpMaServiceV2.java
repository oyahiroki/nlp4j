package nlp4j.yhoo_jp;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import nlp4j.DefaultEnv;
import nlp4j.Keyword;
import nlp4j.NlpService;
import nlp4j.NlpServiceResponse;
import nlp4j.http.HttpClient5;
import nlp4j.impl.DefaultKeyword;

/**
 * Yahoo! Japan dependency analysis 日本語係り受け解析 V2<br>
 * NLP Service of Yahoo! Japan dependency analysis.
 * 
 * <pre>
 * https://developer.yahoo.co.jp/webapi/jlp/ma/v2/parse.html
 * </pre>
 * 
 * 
 * @author Hiroki Oya
 * @version 2.0
 * @since 1.0
 *
 */
public class YJpMaServiceV2 implements NlpService {

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	String appID;
	static final String baseUrl = "https://jlp.yahooapis.jp/MAService/V2/parse";

	/**
	 * Yahoo! Japan dependency analysis 日本語係り受け解析
	 */
	public YJpMaServiceV2() {

		super();

		// appID
		this.appID = System.getProperty("yhoo_jp.appid");

		if (this.appID == null) {
			// throw new RuntimeException("no appid");
			this.appID = DefaultEnv.YHOO_JP_API_ID;
			Exception e = new Exception( //
					"Please get your own APP_ID for Yahoo! Japan API " //
							+ "and set as Dyhoo_jp.appid={your_app_id} " //
							+ "- https://e.developer.yahoo.co.jp/dashboard/" //
			);
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
		// id (required)
		requestBodyJson.addProperty("id", "1");
		// jsonrpc (required)
		requestBodyJson.addProperty("jsonrpc", "2.0");
		// method (required)
		requestBodyJson.addProperty("method", "jlp.daservice.parse");
		// params (required)
		{
			JsonObject params = new JsonObject();
			params.addProperty("q", text);
			requestBodyJson.add("params", params);
		}
		// params/context (optional)
		// for user defined dictionary

		try (nlp4j.http.HttpClient client = new HttpClient5();) {
			// User-Agent: Yahoo AppID: <あなたのアプリケーションID>

			NlpServiceResponse res = client.post(url, requestHeader, requestBodyJson.toString());

			logger.debug(res);
			logger.debug(res.getOriginalResponseBody());

			Gson gson = new Gson();

			JsonObject responseJson = gson.fromJson(res.getOriginalResponseBody(), JsonObject.class);

//			System.err.println(JsonUtils.prettyPrint(responseJson));

			List<Keyword> kwds = new ArrayList<Keyword>();

			{
				JsonObject result = responseJson.get("result").getAsJsonObject();
				JsonArray chunks = result.get("chunks").getAsJsonArray();
				for (int n = 0; n < chunks.size(); n++) {
					JsonObject chunk = chunks.get(n).getAsJsonObject();
					JsonArray tokens = chunk.get("tokens").getAsJsonArray();
					for (int x = 0; x < tokens.size(); x++) {
						JsonArray token = tokens.get(x).getAsJsonArray();
//						System.err.println("str: " + token.get(0).getAsString());
//						System.err.println("yomi: " + token.get(1).getAsString());
//						System.err.println("lex: " + token.get(2).getAsString());
//						System.err.println("pos: " + token.get(3).getAsString());
//						System.err.println("pos2: " + token.get(4).getAsString());
//						System.err.println("katsuyou: " + token.get(5).getAsString());
//						System.err.println("katsuyou2: " + token.get(6).getAsString());
//						System.err.println("---");

						Keyword kwd = new DefaultKeyword();
						kwd.setStr(token.get(0).getAsString());
						kwd.setReading(token.get(1).getAsString());
						kwd.setLex(token.get(2).getAsString());
						kwd.setFacet(token.get(3).getAsString());
						kwds.add(kwd);
					}
				}
			}

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
	public List<Keyword> getKeywords(String text) throws IOException {

		if (text == null) {
			System.err.println("INFO: text is null");
			return null;
		}

		NlpServiceResponse res = process(text);

		if (res != null) {

			logger.debug(res.getOriginalResponseBody());

			List<Keyword> ret = new ArrayList<Keyword>();

			for (Keyword kwd : res.getKeywords()) {
				ret.add(kwd);
			}

			return ret;
		} else {
			return null;
		}
	}

}
