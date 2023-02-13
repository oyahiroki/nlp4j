package nlp4j.ginza;

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

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import nlp4j.DefaultEnv;
import nlp4j.Keyword;
import nlp4j.KeywordWithDependency;
import nlp4j.NlpService;
import nlp4j.impl.DefaultNlpServiceResponse;
import nlp4j.util.HttpClient;
import nlp4j.util.JsonUtils;

/**
 * <pre>
 * </pre>
 * 
 * @author Hiroki Oya
 * @since 0.1.0.0
 * @created_at 2021-04-24
 *
 */
public class GinzaNlpServiceViaHttp implements NlpService {

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	private String endPoint;

	public GinzaNlpServiceViaHttp(String endPoint) {
		super();
		this.endPoint = endPoint;
	}

	public DefaultNlpServiceResponse process(String text) throws IOException {

		if (text == null || text.isEmpty() || text.trim().isEmpty()) {
			return null;
		}

		Map<String, String> params = new HashMap<>();
		params.put("text", text);

		HttpClient client = new HttpClient();
		DefaultNlpServiceResponse res = client.get(this.endPoint, params);

		if (logger.isDebugEnabled()) {
			logger.debug(res.getOriginalResponseBody());
		}

//		JsonArray arr = (new Gson()).fromJson(res.getOriginalResponseBody(), JsonArray.class);
//		System.err.println(JsonUtils.prettyPrint(arr));

		GinzaJsonResponseParser parser = new GinzaJsonResponseParser();

		parser.parseResponse(res.getOriginalResponseBody());

//		try {
//			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
//			SAXParser saxParser = saxParserFactory.newSAXParser();
//			YJpDaServiceResponseHandler handler = new YJpDaServiceResponseHandler(text);
//			saxParser.parse(new ByteArrayInputStream(res.getOriginalResponseBody().getBytes("utf-8")), handler);
////			KeywordWithDependency root = handler.getRoot();
//			ArrayList<KeywordWithDependency> roots = handler.getRoots();
//
//			ArrayList<Keyword> kwds = new ArrayList<Keyword>();
////			kwds.add(root);
//			kwds.addAll(roots);
//
//			res.setKeywords(kwds);
//		} catch (Exception e) {
//			throw new IOException(e);
//		}

		return res;
	}

//	/**
//	 * Get response from dependency analysis service 日本語係り受け解析の結果を取得する
//	 * 
//	 * @param text 日本語の自然文文字列
//	 * @return 日本語係り受け解析の結果
//	 * @throws IOException IOで発生した例外
//	 */
//	public ArrayList<KeywordWithDependency> getKeywords(String text) throws IOException {
//		if (text == null) {
//			System.err.println("INFO: text is null");
//			return null;
//		}
//		DefaultNlpServiceResponse res = process(text);
//		if (res != null) {
//			logger.debug(res.getOriginalResponseBody());
//
//			ArrayList<KeywordWithDependency> ret = new ArrayList<KeywordWithDependency>();
//			for (Keyword kwd : res.getKeywords()) {
//				ret.add((KeywordWithDependency) kwd);
//			}
//
//			return ret;
//		} else {
//			return null;
//		}
//	}

}
