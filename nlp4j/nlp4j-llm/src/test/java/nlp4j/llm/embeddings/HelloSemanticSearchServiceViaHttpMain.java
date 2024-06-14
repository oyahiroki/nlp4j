package nlp4j.llm.embeddings;

import java.util.ArrayList;
import java.util.List;

import nlp4j.NlpServiceResponse;
import nlp4j.util.JsonUtils;

public class HelloSemanticSearchServiceViaHttpMain {

	public static void main(String[] args) throws Exception {
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

}
