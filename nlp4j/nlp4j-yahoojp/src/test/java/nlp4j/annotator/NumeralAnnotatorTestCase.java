package nlp4j.annotator;

import junit.framework.TestCase;
import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.Keyword;
import nlp4j.impl.DefaultDocument;
import nlp4j.util.DocumentUtil;
import nlp4j.util.JsonUtils;
import nlp4j.yhoo_jp.YJpMaAnnotator;

/**
 * TestCase for NumeralAnnotator
 * 
 * @since 1.1
 */
public class NumeralAnnotatorTestCase extends TestCase {

	@SuppressWarnings("rawtypes")
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
			System.err.println(JsonUtils.prettyPrint(DocumentUtil.toJsonObject(doc)));
		}

		{
			DocumentAnnotator a1 = new YJpMaAnnotator();
			a1.annotate(doc);
			System.err.println(doc);
			for (Keyword kwd : doc.getKeywords()) {
				System.err.println(kwd);
			}
		}
		System.err.println(JsonUtils.prettyPrint(DocumentUtil.toJsonObject(doc)));
		{
			NumeralAnnotator a2 = new NumeralAnnotator();
			a2.annotate(doc);
			System.err.println(doc);
			for (Keyword kwd : doc.getKeywords()) {
				System.err.println(kwd);
			}
		}

	}

	/**
	 * @throws Exception 例外発生時
	 */
	public void testAnnotateDocument002() throws Exception {
		String json = "{" + //
				"  'text': '100円拾った。'," + //
				"  'keywords': [" + //
				"    {" + //
				"      'facet': '名詞'," + //
				"      'lex': '100'," + //
				"      'str': '100'," + //
				"      'reading': '100'," + //
				"      'count': -1," + //
				"      'begin': 0," + //
				"      'end': 3," + //
				"      'correlation': 0.0," + //
				"      'sequence': 1" + //
				"    }," + //
				"    {" + //
				"      'facet': '接尾辞'," + //
				"      'lex': '円'," + //
				"      'str': '円'," + //
				"      'reading': 'えん'," + //
				"      'count': -1," + //
				"      'begin': 3," + //
				"      'end': 4," + //
				"      'correlation': 0.0," + //
				"      'sequence': 2" + //
				"    }," + //
				"    {" + //
				"      'facet': '動詞'," + //
				"      'lex': '拾う'," + //
				"      'str': '拾っ'," + //
				"      'reading': 'ひろっ'," + //
				"      'count': -1," + //
				"      'begin': 4," + //
				"      'end': 6," + //
				"      'correlation': 0.0," + //
				"      'sequence': 3" + //
				"    }," + //
				"    {" + //
				"      'facet': '助動詞'," + //
				"      'lex': 'た'," + //
				"      'str': 'た'," + //
				"      'reading': 'た'," + //
				"      'count': -1," + //
				"      'begin': 6," + //
				"      'end': 7," + //
				"      'correlation': 0.0," + //
				"      'sequence': 4" + //
				"    }," + //
				"    {" + //
				"      'facet': '特殊'," + //
				"      'lex': '。'," + //
				"      'str': '。'," + //
				"      'reading': '。'," + //
				"      'count': -1," + //
				"      'begin': 7," + //
				"      'end': 8," + //
				"      'correlation': 0.0," + //
				"      'sequence': 5" + //
				"    }" + //
				"  ]" + //
				"}" + //
				"";
		Document doc = DocumentUtil.toDocument(json);
		System.err.println(doc);
		for (Keyword kwd : doc.getKeywords()) {
			System.err.println(kwd.getLex() + "," + kwd.getFacet());
		}
		{
			NumeralAnnotator a2 = new NumeralAnnotator();
			a2.annotate(doc);
		}
		System.err.println(doc);
		for (Keyword kwd : doc.getKeywords()) {
			System.err.println(kwd.getLex() + "," + kwd.getFacet());
		}
	}

}
