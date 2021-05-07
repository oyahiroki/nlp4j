package nlp4j.cabocha;

import nlp4j.Document;
import nlp4j.Keyword;
import nlp4j.impl.DefaultDocument;
import nlp4j.pattern.UserPatternAnnotator;

public class CabochaUserPatternAnnotatorTestMain {

	public static void main(String[] args) throws Exception {

		String text = "マフラーから著しい黒煙が出る";

		Document doc = new DefaultDocument();
		doc.putAttribute("text", text);

		CabochaAnnotator annCabocha = new CabochaAnnotator();
		{
			annCabocha.setProperty("tempDir", "R:/");
			annCabocha.setProperty("encoding", "MS932");
			annCabocha.setProperty("target", "text");
			annCabocha.annotate(doc);
		}

		UserPatternAnnotator ann = new UserPatternAnnotator();
		{
			ann.setProperty("file", "src/test/resources/nlp4j.cabocha/pattern-sample-ja-004.xml");
			ann.annotate(doc);
		}

		// 抽出されたキーワードの取り出し
		for (Keyword kwd : doc.getKeywords("pattern")) {
			System.err.println("lex=" + kwd.getLex() + ",facet=" + kwd.getFacet() + ",begin=" + kwd.getBegin() + ",end="
					+ kwd.getEnd());
		}

	}

}
