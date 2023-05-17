package nlp4j.ginza;

import junit.framework.TestCase;
import nlp4j.NlpServiceResponse;
import nlp4j.util.JsonUtils;

public class GinzaNlpServiceViaHttpTestCase extends TestCase {

	public void testProcess001() throws Exception {
		String text = "今日はとてもいい天気です。明日は晴れるかな。";
		String endPoint = "http://localhost:8888/";
		GinzaNlpServiceViaHttp nlp = new GinzaNlpServiceViaHttp(endPoint);
		NlpServiceResponse res = nlp.process(text);
//		System.err.println(res);
		System.err.println(JsonUtils.prettyPrint(res.getOriginalResponseBody()));
	}

	public void testProcess002() throws Exception {
		String text = "今日はとてもいい天気です。\n\n明日は晴れるかな。";
		String endPoint = "http://localhost:8888/";
		GinzaNlpServiceViaHttp nlp = new GinzaNlpServiceViaHttp(endPoint);
		NlpServiceResponse res = nlp.process(text);
//		System.err.println(res);
		System.err.println(JsonUtils.prettyPrint(res.getOriginalResponseBody()));
	}

	public void testProcess003() throws Exception {
		String text = "私は朝に歩いて学校に行きます。";
		String endPoint = "http://localhost:8888/";
		GinzaNlpServiceViaHttp nlp = new GinzaNlpServiceViaHttp(endPoint);
		NlpServiceResponse res = nlp.process(text);
//		System.err.println(res);
		System.err.println(JsonUtils.prettyPrint(res.getOriginalResponseBody()));
	}

	public void testProcess010() throws Exception {
		String text = "立地は悪いが食事は美味しい";
		String endPoint = "http://localhost:8888/";
		GinzaNlpServiceViaHttp nlp = new GinzaNlpServiceViaHttp(endPoint);
		NlpServiceResponse res = nlp.process(text);
//		System.err.println(res);
		System.err.println(JsonUtils.prettyPrint(res.getOriginalResponseBody()));
	}
}
