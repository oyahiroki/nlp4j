package nlp4j.pattern.examples;

import nlp4j.Document;
import nlp4j.Keyword;
import nlp4j.KeywordWithDependency;
import nlp4j.cabocha.CabochaAnnotator;
import nlp4j.impl.DefaultDocument;
import nlp4j.pattern.UserPatternAnnotator;

@SuppressWarnings("javadoc")
public class UserPatternAnnotatorExample {

	@SuppressWarnings("javadoc")
	public static void main(String[] args) throws Exception {

		String text = "車が高速道路で急に停止した。エンジンから煙がもくもくと出た。";

		Document doc = new DefaultDocument();
		{
			doc.putAttribute("text", text);
		}

		{ // 係り受け解析
			CabochaAnnotator ann = new CabochaAnnotator();
			ann.setProperty("encoding", "MS932");
			ann.setProperty("target", "text");
			ann.annotate(doc);
		}

		{ // 係り受けパターン抽出
			UserPatternAnnotator ann = new UserPatternAnnotator();
			ann.setProperty("file", "src/test/resources/nlp4j.pattern.examples/pattern-ja-sv.xml");
			ann.annotate(doc);
		}

//		for (Keyword kwd : doc.getKeywords()) {
//			// 係り受け
//			if (kwd instanceof KeywordWithDependency) {
//				System.err.println(((KeywordWithDependency) kwd).toStringAsXml());
//			} else {
//				System.err.println(kwd.toString());
//			}
//		}

		// Expected Result
		// 車 ... 停止
		// 煙 ... 出る
		for (Keyword kwd : doc.getKeywords("pattern")) {
			System.err.println(kwd.getLex());
		}

	}
}
