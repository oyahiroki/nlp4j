package nlp4j.llm.embeddings;

import nlp4j.NlpServiceResponse;
import nlp4j.util.JsonUtils;

public class HelloEmbeddingServiceViaHttpMain {

	public static void main(String[] args) throws Exception {
		String text = "今日はとてもいい天気です。";
		String endPoint = "http://localhost:8888/";
		EmbeddingServiceViaHttp nlp = new EmbeddingServiceViaHttp(endPoint);
		NlpServiceResponse res = nlp.process(text);
//		System.err.println(res);
		System.err.println(JsonUtils.prettyPrint(res.getOriginalResponseBody()));
		// {
		// "message": "ok",
		// "time": "2024-06-15T00:44:53",
		// "text": "今日はとてもいい天気です。",
		// "embeddings": [
		// 0.055711280554533005,
		// 0.002218654379248619,
		// -0.026986829936504364,
		// ...
		// -0.017001923173666,
		// -0.04435045272111893,
		// -0.010230879299342632
		// ]
		// }
	}

}
