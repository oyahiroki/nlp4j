package nlp4j.sudachi;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;
import nlp4j.Keyword;
import nlp4j.KeywordParser;
import nlp4j.test.TestUtils;

public class SudachiAnnotatorTestCase extends TestCase {

	public void testAnnotateDocument001() throws Exception {
		SudachiAnnotator ann = new SudachiAnnotator();
		ann.setProperty("systemDict", "system_full.dic");
		ann.setProperty("target", "text");
		ann.setProperty("mode", "C");
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
		ann.setProperty("mode", "C");
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
		ann.setProperty("systemDict", "/usr/local/sudachi/system_full.dic");
		ann.setProperty("target", "text");
		ann.setProperty("mode", "C");
		try (KeywordParser parser = new KeywordParser(ann);) {
			List<Keyword> kwds = parser.parse("日本アイ・ビー・エム株式会社。機動戦士ガンダム。");
			for (Keyword kwd : kwds) {
				System.err.println(kwd.toString());
			}
			assertEquals("機動戦士ガンダム", kwds.get(3).getLex());
		}
	}

	public void testAnnotateDocument004() throws Exception {
		String text = "目玉焼きに塩胡椒で味を付ける";
		String mode = "A"; // A: Short
		int expected_keyword_size = 9;
		SudachiAnnotator ann = new SudachiAnnotator();
		ann.setProperty("systemDict", "/usr/local/sudachi/system_full.dic");
		ann.setProperty("target", "text");
		ann.setProperty("mode", mode);
		try (KeywordParser parser = new KeywordParser(ann);) {
			List<Keyword> kwds = parser.parse(text);
			for (Keyword kwd : kwds) {
				System.err.println(kwd.toString());
			}
			assertEquals("目玉", kwds.get(0).getLex());
			assertEquals("焼き", kwds.get(1).getLex());
			assertEquals("に", kwds.get(2).getLex());
			assertEquals("塩", kwds.get(3).getLex());
			assertEquals("胡椒", kwds.get(4).getLex());
			assertEquals("で", kwds.get(5).getLex());
			assertEquals("味", kwds.get(6).getLex());
			assertEquals("を", kwds.get(7).getLex());
			assertEquals("付ける", kwds.get(8).getLex());
			assertEquals(expected_keyword_size, kwds.size());
		}
	}

	public void testAnnotateDocument005() throws Exception {
		String text = "目玉焼きに塩胡椒で味を付ける";
		String mode = "C"; // C: Long
		int expected_keyword_size = 7;
		SudachiAnnotator ann = new SudachiAnnotator();
		ann.setProperty("systemDict", "/usr/local/sudachi/system_full.dic");
		ann.setProperty("target", "text");
		ann.setProperty("mode", mode);
		try (KeywordParser parser = new KeywordParser(ann);) {
			List<Keyword> kwds = parser.parse(text);
			for (Keyword kwd : kwds) {
				System.err.println(kwd.toString());
			}
			assertEquals("目玉焼き", kwds.get(0).getLex());
			assertEquals("に", kwds.get(1).getLex());
			assertEquals("塩胡椒", kwds.get(2).getLex());
			assertEquals("で", kwds.get(3).getLex());
			assertEquals("味", kwds.get(4).getLex());
			assertEquals("を", kwds.get(5).getLex());
			assertEquals("付ける", kwds.get(6).getLex());
			assertEquals(expected_keyword_size, kwds.size());
		}
	}

	public void testAnnotateDocument006() throws Exception {
		TestUtils.setLevelDebug();
		// 固有名詞の扱いを確認する
		String text = "京都は日本の都市である";
		String mode = "C";
		int expected_keyword_size = 7;
		SudachiAnnotator ann = new SudachiAnnotator();
		ann.setProperty("systemDict", "/usr/local/sudachi/system_full.dic");
		ann.setProperty("target", "text");
		ann.setProperty("mode", mode);
		try (KeywordParser parser = new KeywordParser(ann);) {
			List<Keyword> kwds = parser.parse(text);
			for (Keyword kwd : kwds) {
				System.err.println(kwd.toString());
			}
			assertEquals("京都", kwds.get(0).getLex());
			assertEquals("は", kwds.get(1).getLex());
			assertEquals("日本", kwds.get(2).getLex());
			assertEquals("の", kwds.get(3).getLex());
			assertEquals("都市", kwds.get(4).getLex());
			assertEquals("だ", kwds.get(5).getLex());
			assertEquals("ある", kwds.get(6).getLex());
			assertEquals(expected_keyword_size, kwds.size());
		}
	}

	public void testAnnotateDocument007() throws Exception {
		TestUtils.setLevelDebug();
		// 固有名詞の扱いを確認する
		String text = "赤いスイートピー";
		String mode = "C";
		int expected_keyword_size = 7;
		SudachiAnnotator ann = new SudachiAnnotator();
		ann.setProperty("systemDict", "/usr/local/sudachi/system_full.dic");
		ann.setProperty("target", "text");
		ann.setProperty("mode", mode);
		try (KeywordParser parser = new KeywordParser(ann);) {
			List<Keyword> kwds = parser.parse(text);
			for (Keyword kwd : kwds) {
				System.err.println(kwd.toString());
			}
		}
	}

	public void testAnnotateDocument008() throws Exception {
		TestUtils.setLevelDebug();
		// 固有名詞の扱いを確認する
		String text = "こどもの日";
		String mode = "C";
		SudachiAnnotator ann = new SudachiAnnotator();
		ann.setProperty("systemDict", "/usr/local/sudachi/system_full.dic");
		ann.setProperty("target", "text");
		ann.setProperty("mode", mode);
		try (KeywordParser parser = new KeywordParser(ann);) {
			List<Keyword> kwds = parser.parse(text);
			for (Keyword kwd : kwds) {
				System.err.println(kwd.toString());
			}
		}
	}

	public void testAnnotateDocument009() throws Exception {
		TestUtils.setLevelDebug();
		// 固有名詞の扱いを確認する
		String text = "国の一覧";
		String mode = "C";
		SudachiAnnotator ann = new SudachiAnnotator();
		ann.setProperty("systemDict", "/usr/local/sudachi/system_full.dic");
		ann.setProperty("target", "text");
		ann.setProperty("mode", mode);
		try (KeywordParser parser = new KeywordParser(ann);) {
			List<Keyword> kwds = parser.parse(text);
			for (Keyword kwd : kwds) {
				System.err.println(kwd.toString());
			}
		}
	}

	public void testAnnotateDocument010() throws Exception {
		TestUtils.setLevelDebug();
		// 固有名詞の扱いを確認する
		String text = "高橋留美子";
		{
			String mode = "A";
			SudachiAnnotator ann = new SudachiAnnotator();
			ann.setProperty("systemDict", "/usr/local/sudachi/system_full.dic");
			ann.setProperty("target", "text");
			ann.setProperty("mode", mode);
			try (KeywordParser parser = new KeywordParser(ann);) {
				List<Keyword> kwds = parser.parse(text);
				for (Keyword kwd : kwds) {
					System.err.println(kwd.toString());
				}
			}
		}
		System.err.println("---");
		{
			String mode = "C";
			SudachiAnnotator ann = new SudachiAnnotator();
			ann.setProperty("systemDict", "/usr/local/sudachi/system_full.dic");
			ann.setProperty("target", "text");
			ann.setProperty("mode", mode);
			try (KeywordParser parser = new KeywordParser(ann);) {
				List<Keyword> kwds = parser.parse(text);
				for (Keyword kwd : kwds) {
					System.err.println(kwd.toString());
				}
			}
		}
	}

	public void testAnnotateDocument011() throws Exception {
		TestUtils.setLevelDebug();
		// 複合名詞の扱い
		String text = "コケ植物";
		String mode = "C";
		SudachiAnnotator ann = new SudachiAnnotator();
		ann.setProperty("systemDict", "/usr/local/sudachi/system_full.dic");
		ann.setProperty("target", "text");
		ann.setProperty("mode", mode);
		try (KeywordParser parser = new KeywordParser(ann);) {
			List<Keyword> kwds = parser.parse(text);
			for (Keyword kwd : kwds) {
				System.err.println(kwd.toString());
			}
		}
	}

	public void testAnnotateDocument012() throws Exception {
		TestUtils.setLevelDebug();
		// 複合名詞の扱い
		String text = "(カッコ)";
		String mode = "C";
		SudachiAnnotator ann = new SudachiAnnotator();
		ann.setProperty("systemDict", "/usr/local/sudachi/system_full.dic");
		ann.setProperty("target", "text");
		ann.setProperty("mode", mode);
		try (KeywordParser parser = new KeywordParser(ann);) {
			List<Keyword> kwds = parser.parse(text);
			for (Keyword kwd : kwds) {
				System.err.println(kwd.toString());
			}
		}
	}

	public void testAnnotateDocument013() throws Exception {
		TestUtils.setLevelDebug();
		// 固有名詞の扱いを確認する
		String text = "うる星やつら";
		{
			String mode = "A";
			SudachiAnnotator ann = new SudachiAnnotator();
			ann.setProperty("systemDict", "/usr/local/sudachi/system_full.dic");
			ann.setProperty("target", "text");
			ann.setProperty("mode", mode);
			try (KeywordParser parser = new KeywordParser(ann);) {
				List<Keyword> kwds = parser.parse(text);
				for (Keyword kwd : kwds) {
					System.err.println(kwd.toString());
				}
			}
		}
	}

	public void testAnnotateDocument014_StopWords() throws Exception {
		TestUtils.setLevelDebug();
		String text = "私は失敗をすることがある";
		{
			String mode = "A";
			SudachiAnnotator ann = new SudachiAnnotator();
			ann.setProperty("systemDict", "/usr/local/sudachi/system_full.dic");
			ann.setProperty("target", "text");
			ann.setProperty("mode", mode);
			try (KeywordParser parser = new KeywordParser(ann);) {
				List<Keyword> kwds = parser.parse(text);
				for (Keyword kwd : kwds) {
					System.err.println(kwd.toString());
				}
			}
		}
		System.err.println("---");
		{
			String mode = "A";
			SudachiAnnotator ann = new SudachiAnnotator();
			ann.setProperty("systemDict", "/usr/local/sudachi/system_full.dic");
			ann.setProperty("target", "text");
			ann.setProperty("mode", mode);
			ann.setProperty("stopWords", "する,こと,ある");
			Set<String> ss = new HashSet<>();
			try (KeywordParser parser = new KeywordParser(ann);) {
				List<Keyword> kwds = parser.parse(text);
				for (Keyword kwd : kwds) {
					System.err.println(kwd.toString());
					ss.add(kwd.getLex());
				}
			}
			assertEquals(true, ss.contains("私"));
			assertEquals(true, ss.contains("失敗"));
			assertEquals(false, ss.contains("する"));
			assertEquals(false, ss.contains("こと"));
			assertEquals(false, ss.contains("ある"));
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

	public void testAnnotateDocument102() throws Exception {
		String text = "韓国をハングルで書くと한국です。";
		String mode = "C";
		int expected_keyword_size = 9;
		String expected_facet = "補助記号";
		SudachiAnnotator ann = new SudachiAnnotator();
		{
			ann.setProperty("systemDict", "/usr/local/sudachi/system_full.dic");
			ann.setProperty("target", "text");
			ann.setProperty("mode", mode);
		}
		try (KeywordParser parser = new KeywordParser(ann);) {
			List<Keyword> kwds = parser.parse(text);
			for (Keyword kwd : kwds) {
				System.err.println(kwd.toString());
			}
			assertEquals(expected_facet, kwds.get(6).getFacet());
			assertEquals(expected_keyword_size, kwds.size());
		}
	}

	public void testAnnotateDocument103() throws Exception {
		String text = "アイビーエムをアルファベットで書くとIBMです。";
		String mode = "C";
		int expected_keyword_size = 9;
		String expected_facet = "固有名詞";
		SudachiAnnotator ann = new SudachiAnnotator();
		{
			ann.setProperty("systemDict", "/usr/local/sudachi/system_full.dic");
			ann.setProperty("target", "text");
			ann.setProperty("mode", mode);
		}
		try (KeywordParser parser = new KeywordParser(ann);) {
			List<Keyword> kwds = parser.parse(text);
			for (Keyword kwd : kwds) {
				System.err.println(kwd.toString());
			}
			assertEquals(expected_facet, kwds.get(6).getFacet());
			assertEquals(expected_keyword_size, kwds.size());
		}
	}

	public void testAnnotateDocument104() throws Exception {
		String text = "This is test.";
		String mode = "C";
		int expected_keyword_size = 6;
		String expected_facet = "名詞";
		SudachiAnnotator ann = new SudachiAnnotator();
		{
			ann.setProperty("systemDict", "/usr/local/sudachi/system_full.dic");
			ann.setProperty("target", "text");
			ann.setProperty("mode", mode);
		}
		try (KeywordParser parser = new KeywordParser(ann);) {
			List<Keyword> kwds = parser.parse(text);
			for (Keyword kwd : kwds) {
				System.err.println(kwd.toString());
			}
			assertEquals(expected_facet, kwds.get(4).getFacet());
			assertEquals(expected_keyword_size, kwds.size());
		}
	}

}
