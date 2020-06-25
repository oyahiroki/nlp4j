package nlp4j.cotoha;

import junit.framework.TestCase;
import nlp4j.Document;
import nlp4j.Keyword;
import nlp4j.impl.DefaultDocument;
import nlp4j.impl.DefaultNlpServiceResponse;

public class CotohaAnnotatorTestCase extends TestCase {

	public void testAnnotateDocument() throws Exception {

		// 自然文のテキスト
		String text = "今日はいい天気です。";

		Document doc = new DefaultDocument();
		// 属性「text」としてセットする
		doc.putAttribute("text", text);

		// COTOHA アノテーター
		CotohaDaAnnotator annotator = new CotohaDaAnnotator();
		// 処理対象の属性を指定
		annotator.setProperty("target", "text");
		// 形態素解析処理
		annotator.annotate(doc); // throws Exception

		// キーワードの出力
		for (Keyword kwd : doc.getKeywords()) {
			System.err.println(kwd);
		}

	}

}
