package hellovector2;

import java.util.Arrays;

import com.google.gson.Gson;

import nlp4j.NlpServiceResponse;
import nlp4j.llm.embeddings.EmbeddingResponse;
import nlp4j.llm.embeddings.EmbeddingServiceViaHttp;

public class Hello1 {

	public static void main(String[] args) throws Exception {
		String text = "今日はとてもいい天気です。";
		String endPoint = "http://localhost:8888/";
		EmbeddingServiceViaHttp nlp = new EmbeddingServiceViaHttp(endPoint);
		NlpServiceResponse res = nlp.process(text);
//		System.err.println(res);
//		System.err.println(JsonUtils.prettyPrint(res.getOriginalResponseBody()));
//		JsonObject jo = JsonObjectUtils.fromJson(res.getOriginalResponseBody());
//		System.err.println(JsonUtils.prettyPrint(jo));

		EmbeddingResponse r = (new Gson()).fromJson(res.getOriginalResponseBody(), EmbeddingResponse.class);
		System.err.println(Arrays.toString(r.getEmbeddings()));
	}

}
