package nlp4j.llm.embeddings;

import java.util.Arrays;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import junit.framework.TestCase;
import nlp4j.NlpServiceResponse;
import nlp4j.util.JsonObjectUtils;
import nlp4j.util.JsonUtils;

public class EmbeddingServiceViaHttpTestCase extends TestCase {

	public void testProcess001() throws Exception {
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
