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
import nlp4j.NlpService;
import nlp4j.impl.NlpServiceResponseImpl;
import nlp4j.util.HttpClient;

/**
 * <pre>
 * Yahoo! Japan Morphological analysis 日本語形態素解析
 * https://developer.yahoo.co.jp/webapi/jlp/ma/v1/parse.html
 * </pre>
 * 
 * @author Hiroki Oya
 * @version 1.0
 *
 */
public class YJpMaService implements NlpService {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	String appID;
	static final String baseUrl = "https://jlp.yahooapis.jp/MAService/V1/parse";

	/**
	 * Yahoo! Japan Morphological analysis 日本語形態素解析
	 */
	public YJpMaService() {
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

	public NlpServiceResponseImpl process(String text) throws IOException {

		if (text == null || text.isEmpty() || text.trim().isEmpty()) {
			return null;
		}

		// https://e.developer.yahoo.co.jp/dashboard/
		// -Dyhoo_jp.appid=xxx

		String url = baseUrl + "?" //
				+ "appid=" + appID;

		Map<String, String> params = new HashMap<>();
		params.put("results", "ma,uniq");
		params.put("response", "surface,reading,pos,baseform,feature");
//		filterに指定可能な品詞番号:
//			1 : 形容詞
//			2 : 形容動詞
//			3 : 感動詞
//			4 : 副詞
//			5 : 連体詞
//			6 : 接続詞
//			7 : 接頭辞
//			8 : 接尾辞
//			9 : 名詞
//			10 : 動詞
//			11 : 助詞
//			12 : 助動詞
//			13 : 特殊（句読点、カッコ、記号など）
		params.put("uniq_filter", "1|2|3|4|5|6|7|8|9|10|11|12|13");
		params.put("sentence", text);

		HttpClient client = new HttpClient();
		NlpServiceResponseImpl res = client.get(url, params);
		logger.debug(res);

//		String s2 = XmlUtils.prettyFormatXml(res);
//		System.err.println(s2);

		try {
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = saxParserFactory.newSAXParser();
			YJpMaServiceResponseHandler handler = new YJpMaServiceResponseHandler(text);

			saxParser.parse(new ByteArrayInputStream(res.getOriginalResponseBody().getBytes("utf-8")), handler);

			ArrayList<Keyword> keywords = handler.getKeywords();

			res.setKeywords(keywords);

			return res;

		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	/**
	 * Get response of Morphological analysis 日本語形態素解析の結果を取得する
	 * 
	 * @param text
	 * @return
	 * @throws IOException
	 */
	public ArrayList<Keyword> getKeywords(String text) throws IOException {
		NlpServiceResponseImpl r = process(text);
		return r.getKeywords();
	}

}
