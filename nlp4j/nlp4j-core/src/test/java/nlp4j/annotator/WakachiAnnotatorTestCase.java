package nlp4j.annotator;

import junit.framework.TestCase;
import nlp4j.Document;
import nlp4j.impl.DefaultDocument;
import nlp4j.impl.DefaultKeyword;

/**
 * @author Hiroki Oya
 * @since 1.2
 */
public class WakachiAnnotatorTestCase extends TestCase {

	/**
	 * @throws Exception 例外発生時
	 */
	public void testAnnotateDocument001() throws Exception {

		// 今日はいい天気です。

		String text = "今日はいい天気です。";

		Document doc = new DefaultDocument();

		doc.putAttribute("text", text);

//今日 [sequence=1, facet=名詞, lex=今日, str=今日, reading=きょう, count=-1, begin=0, end=2, correlation=0.0]
//は [sequence=2, facet=助詞, lex=は, str=は, reading=は, count=-1, begin=2, end=3, correlation=0.0]
//いい [sequence=3, facet=形容詞, lex=いい, str=いい, reading=いい, count=-1, begin=3, end=5, correlation=0.0]
//天気 [sequence=4, facet=名詞, lex=天気, str=天気, reading=てんき, count=-1, begin=5, end=7, correlation=0.0]
//です [sequence=5, facet=助動詞, lex=です, str=です, reading=です, count=-1, begin=7, end=9, correlation=0.0]
//。 [sequence=6, facet=特殊, lex=。, str=。, reading=。, count=-1, begin=9, end=10, correlation=0.0]

		doc.addKeyword(new DefaultKeyword(0, 2, "名詞", "今日", "今日"));
		doc.addKeyword(new DefaultKeyword(2, 3, "助詞", "は", "は"));
		doc.addKeyword(new DefaultKeyword(3, 5, "形容詞", "いい", "いい"));
		doc.addKeyword(new DefaultKeyword(5, 7, "名詞", "天気", "天気"));
		doc.addKeyword(new DefaultKeyword(7, 9, "助動詞", "です", "です"));
		doc.addKeyword(new DefaultKeyword(9, 10, "特殊", "。", "。"));

		System.err.println(doc);

		WakachiAnnotator ann = new WakachiAnnotator();
		ann.setProperty("target", "text");

		ann.annotate(doc);

		System.err.println(doc);

	}

}
