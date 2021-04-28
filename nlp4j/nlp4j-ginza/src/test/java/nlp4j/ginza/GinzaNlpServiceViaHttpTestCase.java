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

		String text = "今日はいい天気です。明日は晴れるかな。";

		String endPoint = "http://localhost:8888/";

		GinzaNlpServiceViaHttp nlp = new GinzaNlpServiceViaHttp(endPoint);

		nlp.process(text);

	}


}
