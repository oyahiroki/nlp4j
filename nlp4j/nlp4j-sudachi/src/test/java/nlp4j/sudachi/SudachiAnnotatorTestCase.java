package nlp4j.sudachi;

import java.util.List;

import junit.framework.TestCase;
import nlp4j.Keyword;
import nlp4j.KeywordParser;

public class SudachiAnnotatorTestCase extends TestCase {

	public void testAnnotateDocument() throws Exception {

		SudachiAnnotator ann = new SudachiAnnotator();
		ann.setProperty("target", "text");

		try (KeywordParser parser = new KeywordParser(ann);) {
			List<Keyword> kwds = parser.parse("今日はいい天気です。走って学校に行きました。");

			for (Keyword kwd : kwds) {
				System.err.println(kwd.toString());
			}

		}

	}

}
