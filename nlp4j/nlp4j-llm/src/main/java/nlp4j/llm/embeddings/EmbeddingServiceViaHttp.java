package nlp4j.llm.embeddings;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
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
public class EmbeddingServiceViaHttp implements NlpService {

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	private String endPoint;

	public EmbeddingServiceViaHttp(String endPoint) {
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
	public NlpServiceResponse process(String text) throws IOException {

		if (text == null || text.isEmpty() || text.trim().isEmpty()) {
			return null;
		}

		Map<String, String> params = new HashMap<>();
		params.put("text", text);

		// Http client
		HttpClient client = new HttpClient5();

		JsonObject jsonObj = new JsonObject();
		jsonObj.addProperty("text", text);

		// client.post throws IOException
		try {
			NlpServiceResponse res = client.post(this.endPoint, jsonObj.toString());

			if (logger.isDebugEnabled()) {
				logger.debug("debug is enabled");
				logger.debug(res.getOriginalResponseBody());
			}

			EmbeddingsResponseParser parser = new EmbeddingsResponseParser();
			Document doc = parser.parseResponse(res.getOriginalResponseBody());

			return res;
		} catch (IOException e) {
//			e.printStackTrace();
			throw e;
		} finally {
			try {
				client.close();
			} //
			catch (Exception e) {
//				e.printStackTrace();
				throw new IOException(e);
			}
		}

	}
}