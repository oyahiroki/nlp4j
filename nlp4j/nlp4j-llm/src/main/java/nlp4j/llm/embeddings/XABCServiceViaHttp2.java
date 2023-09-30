package nlp4j.llm.embeddings;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import nlp4j.Document;
import nlp4j.Keyword;
import nlp4j.NlpService;
import nlp4j.NlpServiceResponse;
import nlp4j.counter.Counter;
import nlp4j.http.HttpClient;
import nlp4j.http.HttpClient5;
import nlp4j.test.TestUtils;
import nlp4j.util.JsonUtils;
import nlp4j.util.TextFileUtils;

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
public class XABCServiceViaHttp2 implements NlpService {

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	private String endPoint;

	public XABCServiceViaHttp2(String endPoint) {
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
	public double[] process(String t0, String t1, String t2, String t3) throws IOException {

//		if (text == null || text.isEmpty() || text.trim().isEmpty()) {
//			return null;
//		}

		Map<String, String> params = new HashMap<>();
//		params.put("text", text);

		// Http client
		HttpClient client = new HttpClient5();

		JsonObject requestBod = new JsonObject();
		{
			requestBod.addProperty("t0", t0);
			requestBod.addProperty("t1", t1);
			requestBod.addProperty("t2", t2);
			requestBod.addProperty("t3", t3);
		}

		// client.post throws IOException
		try {
			NlpServiceResponse res = client.post(this.endPoint, requestBod.toString());

			if (logger.isDebugEnabled()) {
				logger.debug("debug is enabled");
				logger.debug(res.getOriginalResponseBody());
			}

			double[] scores = new double[3];

			JsonObject jo = (new Gson()).fromJson(res.getOriginalResponseBody(), JsonObject.class);

			{
				JsonArray arr = jo.get("r").getAsJsonArray().get(0).getAsJsonArray();
				for (int n = 0; n < arr.size(); n++) {
					int x = arr.get(n).getAsJsonObject().get("corpus_id").getAsInt();
					double d = arr.get(n).getAsJsonObject().get("score").getAsDouble();
					scores[x] = d;
				}
			}

			System.err.println("scores: " + Arrays.toString(scores));

//			EmbeddingsResponseParser parser = new EmbeddingsResponseParser();
//			Document doc = parser.parseResponse(res.getOriginalResponseBody());

			return scores;
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

	static public void main(String[] args) throws Exception {

		TestUtils.setLevelInfo();

		String dir = "/usr/local/doshisha/iip/cb_cb202301c_ja/";

		String dir_xabc = dir + "data/test_xabc/";

//		String fileName_xabc = "テストセット1_utf8.txt";
		String fileName_xabc = "テストセット1b_utf8.txt";
//		String fileName_xabc = "テストセット2_utf8.txt";
//		String fileName_xabc = "テストセット3b_utf8.txt";

		File xabcFile1 = new File(dir_xabc + fileName_xabc);

		String endPoint = "http://localhost:8888/";
		XABCServiceViaHttp2 nlp = new XABCServiceViaHttp2(endPoint);

		BufferedReader br = TextFileUtils.openPlainTextFileAsBufferedReader(xabcFile1);
		String s;

		Counter<Boolean> counter_xab = new Counter<>("X-AB");
		Counter<Boolean> counter_xbc = new Counter<>("X-BC");
		Counter<Boolean> counter_xac = new Counter<>("X-AC");
		Counter<Boolean> counter_xabc = new Counter<>("X-ABC");

		while ((s = br.readLine()) != null) {
//			System.err.println(s);
			String[] ss = s.split("\t");
			double[] dd = nlp.process(ss[0], ss[1], ss[2], ss[3]);
			System.err.println(Arrays.toString(dd));

			{
				boolean b = (dd[0] > dd[1]) && (dd[1] > dd[2]);
				counter_xabc.add(b);
			}
			{
				boolean b = (dd[0] > dd[2]);
				counter_xac.add(b);
			}
			{
				boolean b = (dd[0] > dd[1]);
				counter_xab.add(b);
			}
			{
				boolean b = (dd[1] > dd[2]);
				counter_xbc.add(b);
			}
		}

		System.out.println();
		counter_xab.print();
		System.out.println();
		counter_xbc.print();
		System.out.println();
		counter_xac.print();
		System.out.println();
		counter_xabc.print();
		System.out.println();

//		String endPoint = "http://localhost:8888/";
//		XABCServiceViaHttp2 nlp = new XABCServiceViaHttp2(endPoint);
//		String t0 = "今日";
//		String t1 = "明日";
//		String t2 = "犬";
//		String t3 = "トマト";
//		NlpServiceResponse res = nlp.process(t0, t1, t2, t3);
////		System.err.println(res);
//		System.err.println(JsonUtils.prettyPrint(res.getOriginalResponseBody()));
	}
}
