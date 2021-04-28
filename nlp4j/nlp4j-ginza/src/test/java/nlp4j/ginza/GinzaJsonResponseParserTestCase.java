package nlp4j.ginza;

import java.io.File;

import org.apache.commons.io.FileUtils;

import junit.framework.TestCase;

public class GinzaJsonResponseParserTestCase extends TestCase {

	public void testParseResponse001() throws Exception {

		String fileName = "src/main/resources/python/response_ginza.json";
		String json = FileUtils.readFileToString(new File(fileName), "UTF-8");

		GinzaJsonResponseParser parser = new GinzaJsonResponseParser();
		
		parser.parseResponse(json);
	}

}
