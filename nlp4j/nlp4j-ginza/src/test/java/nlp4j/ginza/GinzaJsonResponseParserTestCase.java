package nlp4j.ginza;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;

import junit.framework.TestCase;
import nlp4j.Keyword;
import nlp4j.KeywordWithDependency;
import nlp4j.KeywordWithDependencyParser;

public class GinzaJsonResponseParserTestCase extends TestCase {

	/**
	 * Test parsing GiNZA Server response
	 * 
	 * @throws Exception
	 */
	public void testParseResponse001() throws Exception {

		String fileName = "src/test/resources/nlp4j.ginza/response_ginza.json";

		String json = FileUtils.readFileToString(new File(fileName), "UTF-8");

		GinzaJsonResponseParser parser = new GinzaJsonResponseParser();

		List<Keyword> kwds = parser.parseResponse(json);

		for (Keyword kwd : kwds) {
			System.err.println(kwd.getLex());
			if (kwd instanceof KeywordWithDependency) {
				KeywordWithDependency kd = (KeywordWithDependency) kwd;
				// 係り受けのツリーを表示する
				System.err.println(((KeywordWithDependency) kwd).toStringAsXml());
				List<Keyword> kk = KeywordWithDependencyParser.flatten(kd, null);
				for (Keyword k : kk) {
					System.err.println(k.getFacet() + "," + k.getLex() + "," + k.getSequence());
				}

			}
		}

	}

}
