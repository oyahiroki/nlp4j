package nlp4j.llm.embeddings;

import java.util.ArrayList;
import java.util.List;

import nlp4j.NlpServiceResponse;
import nlp4j.util.JsonUtils;

public class HelloCosineSimiralitySemanticSearch2 {
	static public void main(String[] args) throws Exception {
		long time1 = System.currentTimeMillis();
		String endPoint = "http://localhost:8888/";
		try (SemanticSearchServiceViaHttp nlp = new SemanticSearchServiceViaHttp(endPoint);) {
			String text1 = "私は（原作：梶原一騎・作画：川崎のぼるによる日本の漫画作品。）が好きです ";
//			String text2 = "私は漫画が好きです";
			String text2 = "私は星が好きです "; //
//			String text2 = "私は漫画が好きです";
			// 『巨人の星』（きょじんのほし）は、原作：梶原一騎・作画：川崎のぼるによる日本の漫画作品。
			List<String> ss = new ArrayList<>();
			ss.add(text2);
//			ss.add(text3);
			NlpServiceResponse res = nlp.process(text1, ss);
			System.err.println(JsonUtils.prettyPrint(res.getOriginalResponseBody()));

		}
		long time2 = System.currentTimeMillis();
		System.err.println("time: " + (time2 - time1));
	}
}
