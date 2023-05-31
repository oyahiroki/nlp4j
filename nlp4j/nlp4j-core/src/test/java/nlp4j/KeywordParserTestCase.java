package nlp4j;

import java.util.List;

import junit.framework.TestCase;
import nlp4j.annotator.TestAnnotator;

public class KeywordParserTestCase extends TestCase {

	public void testParse() throws Exception {
		String s = "This is test";

		try (KeywordParser parser = new KeywordParser(new TestAnnotator());) {
			List<Keyword> kwds = parser.parse(s);
			for (Keyword kwd : kwds) {
				System.err.println(kwd);
			}
		}

//		try(){
//			
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

}
