package nlp4j.llm.embeddings;

import java.io.Closeable;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonObject;

import nlp4j.NlpService;
import nlp4j.NlpServiceResponse;
import nlp4j.http.HttpClient;
import nlp4j.http.HttpClient5;
import nlp4j.http.HttpClientBuilder;
import nlp4j.util.JsonObjectUtils;

/**
 * <pre>
 * LLM Embeddings Service
 * </pre>
 * 
 * @author Hiroki Oya
 * @since 1.0.0.0
 * @created on 2023-09-29
 *
 */
public class SemanticSearchServiceViaHttp implements NlpService, Closeable {

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

//	static public void main(String[] args) throws Exception {
//		long time1 = System.currentTimeMillis();
//		String endPoint = "http://localhost:8888/";
//		try (SemanticSearchServiceViaHttp nlp = new SemanticSearchServiceViaHttp(endPoint);) {
//			String text1 = "今日はとてもいい天気です。";
//			String text2 = "私は学校に行きます。";
//			String text3 = "明日の天気は雨になるでしょう。";
//			List<String> ss = new ArrayList<>();
//			ss.add(text2);
//			ss.add(text3);
//			NlpServiceResponse res = nlp.process(text1, ss);
//			System.err.println(JsonUtils.prettyPrint(res.getOriginalResponseBody()));
//
//		}
//		long time2 = System.currentTimeMillis();
//		System.err.println("time: " + (time2 - time1));
//	}

	// Http client
	private final HttpClient client = (new HttpClientBuilder()).build();

	private String endPoint;

	public SemanticSearchServiceViaHttp(String endPoint) {
		super();
		this.endPoint = endPoint;
	}

	@Override
	public void close() throws IOException {
		client.close();
	}

	/**
	 * <pre>
	 * Server にテキストをPOSTリクエストで送信する
	 * Post text to Server
	 * </pre>
	 * 
	 * @param text 解析対象のテキスト
	 * @return
	 * @throws IOException
	 */
	public NlpServiceResponse process(String text, List<String> ss) throws IOException {

		if (text == null || text.isEmpty() || text.trim().isEmpty()) {
			return null;
		}

		Map<String, String> params = new HashMap<>();
		params.put("text", text);

		JsonObject requestbody_json = new JsonObject();
		requestbody_json.addProperty("s", text);

		requestbody_json.add("tt", JsonObjectUtils.toJsonArray(ss));

		// client.post throws IOException
		{
			NlpServiceResponse res = client.post(this.endPoint, requestbody_json.toString());
			if (logger.isDebugEnabled()) {
				logger.debug("debug is enabled");
				logger.debug(res.getOriginalResponseBody());
			}
			return res;
		}

	}
}
