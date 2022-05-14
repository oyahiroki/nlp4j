package nlp4j.yhoo.examples;

import nlp4j.Keyword;
import nlp4j.KeywordWithDependency;
import nlp4j.impl.DefaultDocument;
import nlp4j.pattern.StandardPatternAnnotatorJa;
import nlp4j.yhoo_jp.YJpDaAnnotatorV2;

/**
 * <pre>
 * Yahoo! Japan NLP を使った係り受け解析
 * created at: 2022-05-12
 * </pre>
 * 
 * @author Hiroki Oya
 *
 */
public class YahooDependencyAnnotatorExample {

	public static void main(String[] args) throws Exception {

		DefaultDocument doc = new DefaultDocument();
		{
			doc.putAttribute("text", "これはテストです．今日はいい天気です．ボールが飛ぶ．明日はテストだ．象は鼻が長い．");
		}
		{
			YJpDaAnnotatorV2 ann = new YJpDaAnnotatorV2();
			ann.setProperty("target", "text");
			ann.annotate(doc);
		}
		{
			StandardPatternAnnotatorJa ann = new StandardPatternAnnotatorJa();
			ann.setProperty("target", "text");
			ann.annotate(doc);
		}

		for (Keyword kwd : doc.getKeywords()) {
			if (kwd instanceof KeywordWithDependency) {
				KeywordWithDependency kd = (KeywordWithDependency) kwd;
				System.err.println(kd.toStringAsXml());
			} else {
				System.err.println(kwd.toString());
			}
		}

	}

}
