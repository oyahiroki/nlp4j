package examples;

import java.io.IOException;
import java.util.List;

import nlp4j.Keyword;
import nlp4j.KeywordParser;
import nlp4j.sudachi.SudachiAnnotator;

public class HelloSudachiAnnotator001 {

	public static void main(String[] args) throws Exception {
		String text = "シートのオットマン機構とは何か教えて。";

		{
			SudachiAnnotator ann = new SudachiAnnotator();
			ann.setProperty("systemDict", "/usr/local/sudachi/system_full.dic");
			ann.setProperty("target", "text");
			ann.setProperty("mode", "A");
			try (KeywordParser parser = new KeywordParser(ann);) {
				List<Keyword> kwds = parser.parse(text);
				for (Keyword kwd : kwds) {
					System.err.println(kwd.toString());
				}
			}
		}
		{
			SudachiAnnotator ann = new SudachiAnnotator();
			ann.setProperty("systemDict", "/usr/local/sudachi/system_full.dic");
			ann.setProperty("target", "text");
			ann.setProperty("mode", "C");
			try (KeywordParser parser = new KeywordParser(ann);) {
				List<Keyword> kwds = parser.parse(text);
				for (Keyword kwd : kwds) {
					System.err.println(kwd.toString());
				}
			}
		}

	}

}
