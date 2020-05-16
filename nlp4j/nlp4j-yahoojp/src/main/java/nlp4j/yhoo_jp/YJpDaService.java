package nlp4j.yhoo_jp;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nlp4j.DefaultEnv;
import nlp4j.Keyword;
import nlp4j.KeywordWithDependency;
import nlp4j.NlpService;
import nlp4j.impl.DefaultNlpServiceResponse;
import nlp4j.util.HttpClient;

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
 * @version 1.0
 * @since 1.0
 * @deprecated
 *
 */
public class YJpDaService implements NlpService {

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	String appID;
	static final String baseUrl = "https://jlp.yahooapis.jp/DAService/V1/parse";

	/**
	 * Yahoo! Japan dependency analysis 日本語係り受け解析
	 */
	public YJpDaService() {
		super();
		appID = System.getProperty("yhoo_jp.appid");

		if (appID == null) {
			// throw new RuntimeException("no appid");
			appID = DefaultEnv.YHOO_JP_API_ID;
			Exception e = new Exception("Please get your own APP_ID for Yahoo! Japan API "
					+ "and set as Dyhoo_jp.appid={your_app_id} " + "- https://e.developer.yahoo.co.jp/dashboard/");
			System.err.println("WARNING: " + e.getMessage());
		}
	}

	/**
	 * @param text 自然言語文字列
	 * @return 自然言語処理の結果
	 * @throws IOException 例外発生時にスローされる
	 */
	public DefaultNlpServiceResponse process(String text) throws IOException {
		if (text == null || text.isEmpty() || text.trim().isEmpty()) {
			return null;
		}

		// https://e.developer.yahoo.co.jp/dashboard/
		// -Dyhoo_jp.appid=xxx

		String url = baseUrl;

		Map<String, String> params = new HashMap<>();
		params.put("appid", appID);
		params.put("sentence", text);

		HttpClient client = new HttpClient();
		DefaultNlpServiceResponse res = client.get(url, params);

		logger.debug(res);
		logger.debug(res.getOriginalResponseBody());
		try {
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = saxParserFactory.newSAXParser();
			YJpDaServiceResponseHandler handler = new YJpDaServiceResponseHandler(text);
			saxParser.parse(new ByteArrayInputStream(res.getOriginalResponseBody().getBytes("utf-8")), handler);
//			KeywordWithDependency root = handler.getRoot();
			ArrayList<KeywordWithDependency> roots = handler.getRoots();

			ArrayList<Keyword> kwds = new ArrayList<Keyword>();
//			kwds.add(root);
			kwds.addAll(roots);

			res.setKeywords(kwds);
		} catch (Exception e) {
			throw new IOException(e);
		}

		return res;
	}

	/**
	 * Get response from dependency analysis service 日本語係り受け解析の結果を取得する
	 * 
	 * @param text 日本語の自然文文字列
	 * @return 日本語係り受け解析の結果
	 * @throws IOException IOで発生した例外
	 */
	public ArrayList<KeywordWithDependency> getKeywords(String text) throws IOException {
		if (text == null) {
			System.err.println("INFO: text is null");
			return null;
		}
		DefaultNlpServiceResponse res = process(text);
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
