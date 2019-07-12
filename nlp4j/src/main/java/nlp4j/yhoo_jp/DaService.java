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

import nlp4j.Keyword;
import nlp4j.KeywordWithDependency;
import nlp4j.NlpService;
import nlp4j.impl.NlpServiceResponseImpl;
import nlp4j.util.HttpClient;

/**
 * https://developer.yahoo.co.jp/webapi/jlp/da/v1/parse.html
 * 
 * @author oyahi
 *
 */
public class DaService implements NlpService {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	String appID;
	static String baseUrl = "https://jlp.yahooapis.jp/DAService/V1/parse";

	public DaService() {
		super();
		appID = System.getProperty("yhoo_jp.appid");

		if (appID == null) {
			throw new RuntimeException("no appid");
		}
	}

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
			DaServiceResponseHandler handler = new DaServiceResponseHandler();
			saxParser.parse(new ByteArrayInputStream(res.getOriginalResponseBody().getBytes("utf-8")), handler);
			KeywordWithDependency root = handler.getRoot();
			return root;
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

}
