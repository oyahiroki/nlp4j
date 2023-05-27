package nlp4j.annotator;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.DocumentAnnotatorPipeline;
import nlp4j.DocumentBuilder;
import nlp4j.Keyword;
import nlp4j.impl.DefaultDocument;
import nlp4j.impl.DefaultDocumentAnnotatorPipeline;
import nlp4j.impl.DefaultKeyword;
import nlp4j.util.DocumentUtil;
import nlp4j.util.JsonUtils;
import nlp4j.yhoo_jp.YJpMaAnnotator;

/**
 * @author Hiroki Oya
 * @since 1.0
 */
public class KeywordSequencePatternAnnotatorTestCase extends TestCase {

	/**
	 * @throws Exception 例外発生時
	 */
	public void testAnnotateDocument001() throws Exception {
		String rule = "[{facet:'名詞'},{lex:'の'},{facet:'名詞'}]";
		String facet = "word_nn_no_nn";
		String value = "${0.lex}${1.lex}${2.lex}";
		Document doc = new DefaultDocument();
		{
			Keyword kwd = new DefaultKeyword();
			kwd.setLex("君");
			kwd.setFacet("名詞");
			doc.addKeyword(kwd);
		}
		{
			Keyword kwd = new DefaultKeyword();
			kwd.setLex("の");
			kwd.setFacet("助詞");
			doc.addKeyword(kwd);
		}
		{
			Keyword kwd = new DefaultKeyword();
			kwd.setLex("名");
			kwd.setFacet("名詞");
			doc.addKeyword(kwd);
		}
		{
			Keyword kwd = new DefaultKeyword();
			kwd.setLex("は");
			kwd.setFacet("助詞");
			doc.addKeyword(kwd);
		}
		{
			Keyword kwd = new DefaultKeyword();
			kwd.setLex("？");
			kwd.setFacet("記号");
			doc.addKeyword(kwd);
		}
		KeywordSequencePatternAnnotator annotator = new KeywordSequencePatternAnnotator();
		annotator.setProperty("rule[0]", rule);
		annotator.setProperty("facet[0]", facet);
		annotator.setProperty("value[0]", value);
		annotator.annotate(doc);

		System.err.println(JsonUtils.prettyPrint(DocumentUtil.toJsonObject(doc)));
	}

	/**
	 * @throws Exception 例外発生時
	 */
	public void testAnnotateDocument002() throws Exception {

		String rule = "[{facet:'名詞'},{lex:'の'},{facet:'名詞'}]";
		String facet = "word_nn_no_nn";
		String value = "${0.lex}${1.lex}${2.lex}";

		String ss_lex = "君/の/名/の/映画";
		String ss_facet = "名詞/助詞/名詞/助詞/名詞";
		String ss_begin = "0/1/2/3/4";
		String ss_end = "1/2/3/4/6";

		Document doc = new DefaultDocument();

		for (int n = 0; n < ss_lex.split("/").length; n++) {
			String lx = ss_lex.split("/")[n];
			String ft = ss_facet.split("/")[n];
			int b = Integer.parseInt(ss_begin.split("/")[n]);
			int e = Integer.parseInt(ss_end.split("/")[n]);
			Keyword kwd = new DefaultKeyword();
			kwd.setLex(lx);
			kwd.setFacet(ft);
			kwd.setBegin(b);
			kwd.setEnd(e);
			System.err.println("For TEST: " + kwd);
			doc.addKeyword(kwd);
		}

		KeywordSequencePatternAnnotator annotator = new KeywordSequencePatternAnnotator();
		annotator.setProperty("rule[0]", rule);
		annotator.setProperty("facet[0]", facet);
		annotator.setProperty("value[0]", value);
		annotator.annotate(doc);

		assertTrue(doc.getKeywords(facet).size() > 0);

		System.err.println("<抽出されたキーワード>");
		for (Keyword kwd : doc.getKeywords(facet)) {
			System.err.println(kwd);
		}
		System.err.println("</抽出されたキーワード>");
	}

	public void testAnnotateDocument003b() throws Exception {

		String rule = "[{facet:'名詞'},{lex:'の'},{facet:'名詞'}]";
		String facet = "word_nn_no_nn";
		String value = "${0.lex}-${1.lex}-${2.lex}";

		// NLP4Jが提供するテキストファイルのクローラーを利用する

		// ドキュメントのクロール
		List<Document> docs = new ArrayList<>();
		{
			Document doc = (new DocumentBuilder()).text("卵の殻").build();
			docs.add(doc);
		}

		// NLPパイプライン（複数の処理をパイプラインとして連結することで処理する）の定義
		DocumentAnnotatorPipeline pipeline = new DefaultDocumentAnnotatorPipeline();
		{
			// Yahoo! Japan の形態素解析APIを利用するアノテーター
			DocumentAnnotator annotator = new YJpMaAnnotator();
			pipeline.add(annotator);
		}
		{
			KeywordSequencePatternAnnotator annotator = new KeywordSequencePatternAnnotator();
			annotator.setProperty("rule[0]", rule);
			annotator.setProperty("facet[0]", facet);
			annotator.setProperty("value[0]", value);
			pipeline.add(annotator);
		}
		// アノテーション処理の実行
		pipeline.annotate(docs);

		System.err.println("<抽出されたキーワード>");
		for (Document doc : docs) {
			for (Keyword kwd : doc.getKeywords(facet)) {
				System.err.println(kwd);
			}
		}
		System.err.println("</抽出されたキーワード>");

	}

	/**
	 * @throws Exception 例外発生時
	 */
	public void testAnnotateDocument004() throws Exception {
		String rule = "[{facet:'名詞'},{lex:'する'}]";
		String facet = "pattern.sahen";
		String value = "${0.lex}${1.lex}";
		Document doc = new DefaultDocument();
		{
			Keyword kwd = new DefaultKeyword();
			kwd.setLex("発生");
			kwd.setFacet("名詞");
			doc.addKeyword(kwd);
		}
		{
			Keyword kwd = new DefaultKeyword();
			kwd.setLex("する");
			kwd.setFacet("動詞");
			doc.addKeyword(kwd);
		}
		KeywordSequencePatternAnnotator annotator = new KeywordSequencePatternAnnotator();
		annotator.setProperty("rule[0]", rule);
		annotator.setProperty("facet[0]", facet);
		annotator.setProperty("value[0]", value);
		annotator.annotate(doc);

		System.err.println(JsonUtils.prettyPrint(DocumentUtil.toJsonObject(doc)));
	}

}
