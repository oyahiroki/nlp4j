package nlp4j.annotator;

import junit.framework.TestCase;
import nlp4j.Document;
import nlp4j.Keyword;
import nlp4j.impl.DefaultDocument;
import nlp4j.impl.DefaultKeyword;
import nlp4j.util.DocumentUtil;

public class KeywordFacetMapToFieldAnnotatorTestCase extends TestCase {

	public void testAnnotateDocument001() throws Exception {
		KeywordFacetMapToFieldAnnotator ann = new KeywordFacetMapToFieldAnnotator();
		{
			ann.setProperty("keyword_facet_field_mapping", //
					"" //
							+ "wiki.link->word_ss,"//
							+ "wiki.rel->word_ss,"//
							+ "wiki.yomi->wikiyomi_ss,"//
							+ "名詞->word_ss," //
							+ "動詞->word_ss,"// ■CB202102A
							+ "形容詞->word_ss,"//
							+ "形容動詞->word_ss"// ■CB202102A
							+ "");
		}
		Document doc = new DefaultDocument();
		{
			{
				Keyword kwd = new DefaultKeyword();
				kwd.setFacet("wiki.link");
				kwd.setLex("プリンタ");
				kwd.setStr("プリンタ");
				doc.addKeyword(kwd);
			}
			{
				Keyword kwd = new DefaultKeyword();
				kwd.setFacet("動詞");
				kwd.setLex("使う");
				kwd.setStr("使う");
				doc.addKeyword(kwd);
			}
		}

		System.err.println(doc.getKeywords().size());
		System.err.println(DocumentUtil.toJsonPrettyString(doc));

		ann.annotate(doc);

		System.err.println(doc.getKeywords().size());
		System.err.println(DocumentUtil.toJsonPrettyString(doc));
	}

}
