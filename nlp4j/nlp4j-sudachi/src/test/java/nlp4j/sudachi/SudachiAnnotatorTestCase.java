package nlp4j.sudachi;

import java.util.List;

import junit.framework.TestCase;
import nlp4j.Keyword;
import nlp4j.KeywordParser;

public class SudachiAnnotatorTestCase extends TestCase {

	public void testAnnotateDocument001() throws Exception {
		SudachiAnnotator ann = new SudachiAnnotator();
		ann.setProperty("target", "text");
		try (KeywordParser parser = new KeywordParser(ann);) {
			List<Keyword> kwds = parser.parse("今日はいい天気です。走って学校に行きました。");
			for (Keyword kwd : kwds) {
				System.err.println(kwd.toString());
			}
		}
	}

	public void testAnnotateDocument002() throws Exception {
		SudachiAnnotator ann = new SudachiAnnotator();
		ann.setProperty("systemDict", "/usr/local/sudachi/system_full.dic");
		ann.setProperty("target", "text");
		try (KeywordParser parser = new KeywordParser(ann);) {
			List<Keyword> kwds = parser.parse("日本アイ・ビー・エム株式会社。機動戦士ガンダム。");
			for (Keyword kwd : kwds) {
				System.err.println(kwd.toString());
			}
			assertEquals("機動戦士ガンダム", kwds.get(3).getLex());
		}
	}

	public void testAnnotateDocument003() throws Exception {
		SudachiAnnotator ann = new SudachiAnnotator();
		ann.setProperty("target", "text");
		try (KeywordParser parser = new KeywordParser(ann);) {
			List<Keyword> kwds = parser.parse("日本アイ・ビー・エム株式会社。機動戦士ガンダム。");
			for (Keyword kwd : kwds) {
				System.err.println(kwd.toString());
			}
			assertEquals("機動戦士ガンダム", kwds.get(3).getLex());
		}
	}

	public void testAnnotateDocument101() throws Exception {
		SudachiAnnotator ann = new SudachiAnnotator();
		ann.setProperty("systemDict", "system_core.dic");
		ann.setProperty("target", "text");
		try (KeywordParser parser = new KeywordParser(ann);) {
			List<Keyword> kwds = parser.parse("日本アイ・ビー・エム株式会社。機動戦士ガンダム。");
			for (Keyword kwd : kwds) {
				System.err.println(kwd.toString());
			}
			assertEquals("ガンダム", kwds.get(5).getLex());
		}
	}

}
