package nlp4j.yhoo_jp;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nlp4j.DefaultEnv;
import nlp4j.KeywordWithDependency;
import nlp4j.NlpService;
import nlp4j.impl.NlpServiceResponseImpl;
import nlp4j.util.HttpClient;

/**
 * <pre>
 * Yahoo! Japan dependency analysis 日本語係り受け解析
 * https://developer.yahoo.co.jp/webapi/jlp/da/v1/parse.html
 * </pre>
 * 
 * @author Hiroki Oya
 * @version 1.0
 *
 */
public class YJpDaService implements NlpService {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

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
			e.printStackTrace();
		}
	}

	/**
	 * Get response from dependency analysis service 日本語係り受け解析の結果を取得する
	 * 
	 * @param text
	 * @return
	 * @throws IOException
	 */
	public KeywordWithDependency getKeywords(String text) throws IOException {

		// https://e.developer.yahoo.co.jp/dashboard/
		// -Dyhoo_jp.appid=xxx

		String url = baseUrl;

		Map<String, String> params = new HashMap<>();
		params.put("appid", appID);
		params.put("sentence", text);

		HttpClient client = new HttpClient();
		NlpServiceResponseImpl res = client.get(url, params);
		logger.debug(res);
		logger.debug(res.getOriginalResponseBody());
		try {
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = saxParserFactory.newSAXParser();
			YJpDaServiceResponseHandler handler = new YJpDaServiceResponseHandler();
			saxParser.parse(new ByteArrayInputStream(res.getOriginalResponseBody().getBytes("utf-8")), handler);
			KeywordWithDependency root = handler.getRoot();
			return root;
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

}
