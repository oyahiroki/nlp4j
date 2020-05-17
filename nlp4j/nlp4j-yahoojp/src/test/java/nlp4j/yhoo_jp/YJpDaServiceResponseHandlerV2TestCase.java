package nlp4j.yhoo_jp;

import java.io.File;

import org.apache.commons.io.FileUtils;

import junit.framework.TestCase;

public class YJpDaServiceResponseHandlerV2TestCase extends TestCase {

	/**
	 * Yahoo 日本語かかりうけ解析V2のレスポンスをパースします。
	 * https://developer.yahoo.co.jp/webapi/jlp/da/v2/parse.html
	 * https://developer.yahoo.co.jp/changelog/web_apiapi.html
	 * 
	 * @throws Exception 例外発生時
	 */
	public void testParseJsonString001() throws Exception {

		File file = new File("src/test/resources/nlp4j.yhoo_jp/response_DAServiceV2.json");

		String json = FileUtils.readFileToString(file, "UTF-8");

		YJpDaServiceResponseHandlerV2 handler = new YJpDaServiceResponseHandlerV2(null);

		handler.parseJson(json);

	}

}
