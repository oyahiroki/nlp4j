package nlp4j.annotator;

import junit.framework.TestCase;
import nlp4j.Document;
import nlp4j.Keyword;
import nlp4j.impl.DefaultDocument;
import nlp4j.impl.DefaultKeyword;

public class KeywordFacetFilteringAnnotatorTestCase extends TestCase {

	public void testAnnotateDocument001() throws Exception {
		KeywordFacetFilteringAnnotator ann = new KeywordFacetFilteringAnnotator();
		{
			ann.setProperty("filter", "word.NN");
		}
		Document doc = new DefaultDocument();
		{
			{
				Keyword kwd = new DefaultKeyword();
				kwd.setFacet("word.NN");
				kwd.setLex("プリンタ");
				kwd.setStr("プリンタ");
				doc.addKeyword(kwd);
			}
			{
				Keyword kwd = new DefaultKeyword();
				kwd.setFacet("word.VB");
				kwd.setLex("使う");
				kwd.setStr("使う");
				doc.addKeyword(kwd);
			}
		}

		System.err.println(doc.getKeywords().size());

		ann.annotate(doc);

		System.err.println(doc.getKeywords().size());
	}

}
