package nlp4j.llm.embeddings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import junit.framework.TestCase;
import nlp4j.NlpServiceResponse;
import nlp4j.util.JsonUtils;

public class SemanticSearchServiceViaHttpTestCase extends TestCase {

	/**
	 * 1回リクエストを送信
	 * 
	 * @throws Exception
	 */
	public void testProcess001() throws Exception {
		long time1 = System.currentTimeMillis();
		String endPoint = "http://localhost:8888/";
		try (SemanticSearchServiceViaHttp nlp = new SemanticSearchServiceViaHttp(endPoint);) {
			String text1 = "今日はとてもいい天気です。";
			String text2 = "私は学校に行きます。";
			String text3 = "明日の天気は雨になるでしょう。";
			List<String> ss = new ArrayList<>();
			ss.add(text2);
			ss.add(text3);
			NlpServiceResponse res = nlp.process(text1, ss);
			System.err.println(JsonUtils.prettyPrint(res.getOriginalResponseBody()));

		}
		long time2 = System.currentTimeMillis();
		System.err.println("time: " + (time2 - time1));
	}

	/**
	 * 100回繰り返し
	 * 
	 * @throws Exception
	 */
	public void testProcess101() throws Exception {
		long time1 = System.currentTimeMillis();
		String endPoint = "http://localhost:8888/";
		try (SemanticSearchServiceViaHttp nlp = new SemanticSearchServiceViaHttp(endPoint);) {

			String text1 = "今日はとてもいい天気です。";
			String text2 = "私は学校に行きます。";
			String text3 = "明日の天気は雨になるでしょう。";
			List<String> ss = new ArrayList<>();
			ss.add(text2);
			ss.add(text3);
			for (int n = 0; n < 100; n++) {
				NlpServiceResponse res = nlp.process(text1, ss);
				System.err.println(JsonUtils.prettyPrint(res.getOriginalResponseBody()));
//				System.err.println(res.getOriginalResponseBody());
			}

		}
		long time2 = System.currentTimeMillis();
		System.err.println("time: " + (time2 - time1));
	}

	/**
	 * 100回並列
	 * 
	 * @throws Exception
	 */
	public void testProcess201() throws Exception {
		final int NUM_THREADS = 100;
		String endPoint = "http://localhost:8888/";
		ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);

		Runnable task = () -> {
			try (SemanticSearchServiceViaHttp nlp = new SemanticSearchServiceViaHttp(endPoint);) {
				String text1 = "今日はとてもいい天気です。";
				String text2 = "私は学校に行きます。";
				String text3 = "明日の天気は雨になるでしょう。";
				List<String> ss = new ArrayList<>();
				ss.add(text2);
				ss.add(text3);
				NlpServiceResponse res = nlp.process(text1, ss);
				System.err.println(JsonUtils.prettyPrint(res.getOriginalResponseBody()));

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		};

		for (int i = 0; i < NUM_THREADS; i++) {
			executor.submit(task);
		}

		executor.shutdown();
		try {
			if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
				executor.shutdownNow();
			}
		} catch (InterruptedException e) {
			executor.shutdownNow();
		}
	}

}
