package nlp4j.annotator;

import junit.framework.TestCase;
import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.Keyword;
import nlp4j.impl.DefaultDocument;
import nlp4j.yhoo_jp.YJpMaAnnotator;

/**
 * TestCase for NumeralAnnotator
 * 
 * @since 1.1
 */
public class NumeralAnnotatorTestCase extends TestCase {

	Class target = NumeralAnnotator.class;

	/**
	 * Test case for "100円拾った。". 数詞 is not extracted.
	 * 
	 * @throws Exception 例外発生時
	 */
	public void testAnnotateDocument001() throws Exception {
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
		{
			NumeralAnnotator a2 = new NumeralAnnotator();
			a2.annotate(doc);
			System.err.println(doc);
			for (Keyword kwd : doc.getKeywords()) {
				System.err.println(kwd);
			}
		}

	}

}
