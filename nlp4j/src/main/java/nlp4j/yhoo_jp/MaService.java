/**
 * 
 */
package nlp4j.yhoo_jp;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import nlp4j.util.XmlUtils;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * https://developer.yahoo.co.jp/webapi/jlp/ma/v1/parse.html
 * 
 * @author oyahiroki
 *
 */
public class MaService {

	public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

	OkHttpClient client = new OkHttpClient();

	String post(String url, String json) throws IOException {
		RequestBody body = RequestBody.create(JSON, json);
		Request request = new Request.Builder().url(url).post(body).build();
		try (Response response = client.newCall(request).execute()) {
			return response.body().string();
		}
	}

	String get(String url, Map<String, String> params) throws IOException {

		HttpUrl.Builder builder = HttpUrl.parse(url).newBuilder();
		if (params != null) {
			params.forEach(builder::addQueryParameter);
		}

		Request request = new Request.Builder().url(builder.build()).build();

		try (Response response = client.newCall(request).execute()) {
			return response.body().string();
		}
	}

	public void process() throws IOException {

		// https://e.developer.yahoo.co.jp/dashboard/
		// -Dyhoo_jp.appid=xxx
		String appID = System.getProperty("yhoo_jp.appid");

		if (appID == null) {
			throw new IOException("no appid");
		}

		String url = "https://jlp.yahooapis.jp/MAService/V1/parse?" + "appid=" + appID;

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
		params.put("sentence", "今日はいい天気なので走って会社に行きました。");

		String res = get(url, params);
		System.err.println(res);

		String s2 = XmlUtils.prettyFormatXml(res);
		System.err.println(s2);

		try {
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = saxParserFactory.newSAXParser();
			MaServiceResponseHandler handler = new MaServiceResponseHandler();

			saxParser.parse(new ByteArrayInputStream(res.getBytes("utf-8")), handler);

		} catch (Exception e) {
			throw new IOException(e);
		}
	}

}
