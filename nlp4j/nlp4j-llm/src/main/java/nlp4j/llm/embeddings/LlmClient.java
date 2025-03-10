package nlp4j.llm.embeddings;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import nlp4j.NlpServiceResponse;
import nlp4j.http.HttpClient;
import nlp4j.http.HttpClientBuilder;
import nlp4j.util.DoubleUtils;
import nlp4j.util.FloatUtils;
import nlp4j.util.JsonObjectUtils;
import nlp4j.util.LoggerUtils;

public class LlmClient implements AutoCloseable {

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	private HttpClient client;

	private String endPoint;

	public LlmClient(String endPoint) {
		super();
		this.endPoint = endPoint;
		this.client = (new HttpClientBuilder()).build();
	}

	@Override
	public void close() throws Exception {
		this.client.close();
	}

	public float[] embedding(String text) throws IOException {
		if (text == null || text.isEmpty() || text.trim().isEmpty()) {
			return null;
		}
		JsonObject requestbody_json = new JsonObject();
		requestbody_json.addProperty("text", text);
		// client.post throws IOException
		{
			NlpServiceResponse res = client.post((new URL(this.endPoint + "embedding")).toString(),
					requestbody_json.toString());
			if (logger.isDebugEnabled()) {
				logger.debug("debug is enabled");
				logger.debug(res.getOriginalResponseBody());
			}
			JsonObject jo_res = res.getAsJsonObject();

			JsonArray jo_res_embeddings = jo_res.get("embeddings").getAsJsonArray();
			float[] ff = new float[jo_res_embeddings.size()];

			for (int n = 0; n < jo_res_embeddings.size(); n++) {
				ff[n] = jo_res_embeddings.get(n).getAsFloat();
			}

//			JsonArray r = jo_res.get("r").getAsJsonArray();
//			double[] dd = new double[r.size()];
//			for (int n = 0; n < r.size(); n++) {
//				JsonObject o = r.get(n).getAsJsonObject();
//				int idx = o.get("corpus_id").getAsInt();
//				double score = o.get("score").getAsDouble();
//				dd[idx] = score;
//			}
//			return dd;

			return ff;
		}
	}

	/**
	 * 二つのベクトル間のコサイン類似度を計算します
	 * 
	 * @param text1
	 * @param text2
	 * @return
	 * @throws IOException
	 */
	public double cos_sim(String text1, String text2) throws IOException {
		if (text1 == null || text1.isEmpty() || text1.trim().isEmpty()) {
			throw new IllegalArgumentException(text1);
		}
		if (text2 == null || text2.isEmpty() || text2.trim().isEmpty()) {
			throw new IllegalArgumentException(text2);
		}

		JsonObject requestbody_json = new JsonObject();
		requestbody_json.addProperty("text1", text1);
		requestbody_json.addProperty("text2", text2);
		// client.post throws IOException
		{
			NlpServiceResponse res = client.post((new URL(this.endPoint + "cos_sim")).toString(),
					requestbody_json.toString());
			if (logger.isDebugEnabled()) {
				logger.debug("debug is enabled");
				logger.debug(res.getOriginalResponseBody());
			}
			JsonObject jo_res = res.getAsJsonObject();
			double cosine_similarity = jo_res.get("cosine_similarity").getAsDouble();
			return cosine_similarity;
		}
	}

	/**
	 * クエリと最も意味的に類似しているテキストのインデックスと類似度スコアを含む結果のリストを返します
	 * 
	 * @param text
	 * @param ss
	 * @return
	 * @throws IOException
	 */
	public double[] semantic_search(String text, List<String> ss) throws IOException {
		if (text == null || text.isEmpty() || text.trim().isEmpty()) {
			return null;
		}
		JsonObject requestbody_json = new JsonObject();
		requestbody_json.addProperty("text", text);
		requestbody_json.add("texts", JsonObjectUtils.toJsonArray(ss));
		// client.post throws IOException
		{
			NlpServiceResponse res = client.post((new URL(this.endPoint + "semantic_search")).toString(),
					requestbody_json.toString());
			if (logger.isDebugEnabled()) {
				logger.debug("debug is enabled");
				logger.debug(res.getOriginalResponseBody());
			}
			JsonObject jo_res = res.getAsJsonObject();
			JsonArray r = jo_res.get("r").getAsJsonArray();
			double[] dd = new double[r.size()];
			for (int n = 0; n < r.size(); n++) {
				JsonObject o = r.get(n).getAsJsonObject();
				int idx = o.get("corpus_id").getAsInt();
				double score = o.get("score").getAsDouble();
				dd[idx] = score;
			}
			return dd;
		}
	}


	static public void main(String[] args) throws Exception {
		LoggerUtils.setLoggerDebug();

		boolean debug = true;

		if (debug == true) { // embedding
			long time1 = System.currentTimeMillis();
			String endPoint = "http://localhost:8888/";
			try (LlmClient nlp = new LlmClient(endPoint);) {
				String text1 = "今日はとてもいい天気です。";
				float[] ff = nlp.embedding(text1);
				System.out.println("res: " + FloatUtils.toString(ff));
			}
			long time2 = System.currentTimeMillis();
			System.err.println("time: " + (time2 - time1));

		}
		if (debug == true) { // semanticsearch
			long time1 = System.currentTimeMillis();
			String endPoint = "http://localhost:8888/";
			try (LlmClient nlp = new LlmClient(endPoint);) {
				String text1 = "今日はとてもいい天気です。";
				String text2 = "私は学校に行きます。";
				String text3 = "明日の天気は雨になるでしょう。";
				List<String> ss = new ArrayList<>();
				ss.add(text2);
				ss.add(text3);
				// クエリと最も意味的に類似しているテキストのインデックスと類似度スコアを含む結果のリストを返します
				double[] dd = nlp.semantic_search(text1, ss);
				System.out.println("res: " + DoubleUtils.toPlainString(dd));
			}
			long time2 = System.currentTimeMillis();
			System.err.println("time: " + (time2 - time1));

		}
		if (debug == true) { // cos_sim
			long time1 = System.currentTimeMillis();
			String endPoint = "http://localhost:8888/";
			try (LlmClient nlp = new LlmClient(endPoint);) {
				String text1 = "今日はとてもいい天気です。";
				String text2 = "私は学校に行きます。";
				// 二つのベクトル間のコサイン類似度を計算します
				double d = nlp.cos_sim(text1, text2);
				System.out.println("res: " + DoubleUtils.toPlainString(d));
			}
			long time2 = System.currentTimeMillis();
			System.err.println("time: " + (time2 - time1));

		}
	}
}
