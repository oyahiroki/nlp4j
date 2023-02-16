package nlp4j.ginza;

import junit.framework.TestCase;

public class GinzaNlpServiceViaHttpTestCase extends TestCase {

	public void testProcess001() throws Exception {
		String text = "今日はとてもいい天気です。明日は晴れるかな。";
		String endPoint = "http://localhost:8888/";
		GinzaNlpServiceViaHttp nlp = new GinzaNlpServiceViaHttp(endPoint);
		nlp.process(text);
	}

	public void testProcess001b() throws Exception {
		String text = "今日はとてもいい天気です。\n\n明日は晴れるかな。";
		String endPoint = "http://localhost:8888/";
		GinzaNlpServiceViaHttp nlp = new GinzaNlpServiceViaHttp(endPoint);
		nlp.process(text);
	}

	public void testProcess002() throws Exception {
		String text = "私は朝に歩いて学校に行きます。";
		String endPoint = "http://localhost:8888/";
		GinzaNlpServiceViaHttp nlp = new GinzaNlpServiceViaHttp(endPoint);
		nlp.process(text);
	}
}
