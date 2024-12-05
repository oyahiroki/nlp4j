package examples;

import java.util.List;

import nlp4j.Keyword;
import nlp4j.KeywordParser;
import nlp4j.sudachi.SudachiAnnotator;

public class HelloSudachiAnnotator003 {

	public static void main(String[] args) throws Exception {
		String text = "選挙管理委員会";

		System.err.println("[MODE A]");
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
		System.err.println("[MODE C]");
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

		// 期待される出力 (MODE A は短い単語優先、MODE C は長い単語優先)
//		[MODE A]
//		[facet=名詞 facet2=名詞-普通名詞-サ変可能-*-*-* upos=NOUN lex=選挙 lex=選挙]
//		[facet=名詞 facet2=名詞-普通名詞-サ変可能-*-*-* upos=NOUN lex=管理 lex=管理]
//		[facet=名詞 facet2=名詞-普通名詞-一般-*-*-* upos=NOUN lex=委員 lex=委員]
//		[facet=名詞 facet2=名詞-普通名詞-一般-*-*-* upos=NOUN lex=会 lex=会]
//		[MODE C]
//		[facet=固有名詞 facet2=名詞-固有名詞-一般-*-*-* upos=PROPN lex=選挙管理委員会 lex=選挙管理委員会]

	}

}
