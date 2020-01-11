package yhoo_jp;

import junit.framework.TestCase;
import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.Keyword;
import nlp4j.impl.DefaultDocument;
import nlp4j.yhoo_jp.YJpMaAnnotator;

/**
 * TestCase for YJpMaAnnotator
 *
 */
public class YJpMaAnnotatorTestCase extends TestCase {

	@SuppressWarnings("rawtypes")
	Class target = YJpMaAnnotator.class;

	/**
	 * Test case for "今日はいい天気です。"
	 * 
	 * @throws Exception 例外発生時
	 */
	public void testAnnotateDocument001() throws Exception {
		String text = "今日はいい天気です。";
		Document doc = new DefaultDocument();
		{
			doc.setText(text);
			System.err.println(doc);
		}

		{
			DocumentAnnotator a1 = new YJpMaAnnotator();
			a1.annotate(doc);
			System.err.println(doc);
			for (Keyword kwd : doc.getKeywords()) {
				System.err.println(kwd);
			}
		}

	}

	/**
	 * Test case for "100円拾った。". 数詞 is not extracted.
	 * 
	 * @throws Exception 例外発生時
	 */
	public void testAnnotateDocument002() throws Exception {
		String text = "100円拾った。";
		Document doc = new DefaultDocument();
		{
			doc.setText(text);
			System.err.println(doc);
		}

		{
			DocumentAnnotator a1 = new YJpMaAnnotator();
			a1.annotate(doc);
			System.err.println(doc);
			for (Keyword kwd : doc.getKeywords()) {
				System.err.println(kwd);
			}
		}

	}

}
