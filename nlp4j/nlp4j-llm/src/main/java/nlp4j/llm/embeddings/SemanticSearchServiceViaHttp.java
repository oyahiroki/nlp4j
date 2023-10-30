package nlp4j.llm.embeddings;

import java.io.Closeable;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonObject;

import nlp4j.Document;
import nlp4j.Keyword;
import nlp4j.NlpService;
import nlp4j.NlpServiceResponse;
import nlp4j.http.HttpClient;
import nlp4j.http.HttpClient5;
import nlp4j.util.JsonObjectUtils;
import nlp4j.util.JsonUtils;

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

	// Http client
	private final HttpClient client = new HttpClient5();

	private String endPoint;

	public SemanticSearchServiceViaHttp(String endPoint) {
		super();
		this.endPoint = endPoint;
	}

	/**
	 * <pre>
	 * GiNZA Server にテキストをPOSTリクエストで送信する
	 * Post text to GiNZA Server
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
		try {
			NlpServiceResponse res = client.post(this.endPoint, requestbody_json.toString());

			if (logger.isDebugEnabled()) {
				logger.debug("debug is enabled");
				logger.debug(res.getOriginalResponseBody());
			}

//			EmbeddingsResponseParser parser = new EmbeddingsResponseParser();
//			Document doc = parser.parseResponse(res.getOriginalResponseBody());

			return res;
		} catch (IOException e) {
//			e.printStackTrace();
			throw e;
		} finally {
//			try {
//				client.close();
//			} //
//			catch (Exception e) {
////				e.printStackTrace();
//				throw new IOException(e);
//			}
		}

	}

	static public void main(String[] args) throws Exception {
		long time1 = System.currentTimeMillis();
		String endPoint = "http://localhost:8888/";
		try (SemanticSearchServiceViaHttp nlp = new SemanticSearchServiceViaHttp(endPoint);) {
			for (int n = 0; n < 10; n++) {
				String text = "今日はとてもいい天気です。";
				List<String> ss = new ArrayList<>();
//		ss.add("今日はとてもいい天気です。");
//		ss.add("明日はとてもいい天気です。");
				ss.add("私は学校に行きます。");
				NlpServiceResponse res = nlp.process(text, ss);
//		System.err.println(res);
				System.err.println(JsonUtils.prettyPrint(res.getOriginalResponseBody()));
			} // 747, 704

		}
		long time2 = System.currentTimeMillis();
		System.err.println("time: " + (time2 - time1));
	}

	@Override
	public void close() throws IOException {
		client.close();
	}
}
