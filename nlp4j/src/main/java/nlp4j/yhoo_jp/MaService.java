/**
 * 
 */
package nlp4j.yhoo_jp;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nlp4j.Keyword;
import nlp4j.NlpService;
import nlp4j.NlpServiceResponse;
import nlp4j.impl.KeywordImpl;
import nlp4j.impl.NlpServiceResponseImpl;
import nlp4j.util.HttpClient;

/**
 * https://developer.yahoo.co.jp/webapi/jlp/ma/v1/parse.html
 * 
 * @author oyahiroki
 *
 */
public class MaService implements NlpService {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	String appID;

	public MaService() {
		appID = System.getProperty("yhoo_jp.appid");

		if (appID == null) {
			throw new RuntimeException("no appid");
		}
	}

	public NlpServiceResponseImpl process(String text) throws IOException {

		// https://e.developer.yahoo.co.jp/dashboard/
		// -Dyhoo_jp.appid=xxx

		String url = "https://jlp.yahooapis.jp/MAService/V1/parse?" //
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
			MaServiceResponseHandler handler = new MaServiceResponseHandler();

			saxParser.parse(new ByteArrayInputStream(res.getOriginalResponseBody().getBytes("utf-8")), handler);

			ArrayList<Keyword> keywords = handler.getKeywords();

			res.setKeywords(keywords);

			return res;

		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	@Override
	public ArrayList<Keyword> getKeywords(String text) throws IOException {
		NlpServiceResponseImpl r = process(text);
		return r.getKeywords();
	}

}
