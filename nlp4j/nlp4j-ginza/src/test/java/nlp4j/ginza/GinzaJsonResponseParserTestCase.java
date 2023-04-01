package nlp4j.ginza;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;

import junit.framework.TestCase;
import nlp4j.Keyword;
import nlp4j.KeywordWithDependency;

public class GinzaJsonResponseParserTestCase extends TestCase {

	/**
	 * Test parsing GiNZA Server response
	 * 
	 * @throws Exception
	 */
	public void testParseResponse001() throws Exception {

		String fileName = "src/main/resources/nlp4j.ginza/response_ginza_20230401.json";

		String json = FileUtils.readFileToString(new File(fileName), "UTF-8");

		GinzaJsonResponseParser parser = new GinzaJsonResponseParser();

		List<Keyword> kwds = parser.parseResponse(json);

		for (Keyword kwd : kwds) {
			System.err.println(kwd.getLex());
			if (kwd instanceof KeywordWithDependency) {
				// 係り受けのツリーを表示する
				System.err.println(((KeywordWithDependency) kwd).toStringAsXml());
			}
		}

	}

}
