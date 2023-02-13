package nlp4j.ginza;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.CharSet;

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
