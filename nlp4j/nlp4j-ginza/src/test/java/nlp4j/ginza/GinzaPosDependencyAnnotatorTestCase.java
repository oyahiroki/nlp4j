package nlp4j.ginza;

import java.util.List;

import junit.framework.TestCase;
import nlp4j.Document;
import nlp4j.Keyword;
import nlp4j.KeywordWithDependency;
import nlp4j.KeywordWithDependencyParser;
import nlp4j.impl.DefaultDocument;
import nlp4j.impl.DefaultKeyword;
import nlp4j.pattern.StandardPatternAnnotatorJa;
import nlp4j.util.DocumentUtil;

public class GinzaPosDependencyAnnotatorTestCase extends TestCase {

	public void testAnnotateDocument001() throws Exception {
		Document doc = new DefaultDocument("今日はいい天気です。昨日は雨だったが、明日はいい天気です。");
		GinzaPosDependencyAnnotator ann = new GinzaPosDependencyAnnotator();
		ann.setProperty("target", "text");
		ann.annotate(doc);
		// KeywordWithDependency
		for (KeywordWithDependency kwd : doc.getKeywords(KeywordWithDependency.class)) {
			System.err.println(kwd.toStringAsXml());
		}
	}

	public void testAnnotateDocument002() throws Exception {
		Document doc = new DefaultDocument("私は歩いて学校に行きます。");
		GinzaPosDependencyAnnotator ann = new GinzaPosDependencyAnnotator();
		ann.setProperty("target", "text");
		ann.annotate(doc);
		for (KeywordWithDependency kwd : doc.getKeywords(KeywordWithDependency.class)) {
			System.err.println(kwd.toStringAsXml());
			List<Keyword> kwds = KeywordWithDependencyParser.parse(kwd);
			for (Keyword kw : kwds) {
				System.err.println(kw.getLex() + "," + kw.getFacet());
			}
		}
	}

	public void testAnnotateDocument003() throws Exception {
		Document doc = new DefaultDocument("私は歩いて学校に行きます。");
		GinzaPosDependencyAnnotator ann = new GinzaPosDependencyAnnotator();
		ann.setProperty("target", "text");
		ann.annotate(doc);
		for (KeywordWithDependency kwd : doc.getKeywords(KeywordWithDependency.class)) {
			System.err.println(kwd.toStringAsXml());
			List<Keyword> kwds = KeywordWithDependencyParser.parse(kwd, "nsubj", "obl");
			for (Keyword kw : kwds) {
				System.err.println(kw.getLex() + "," + kw.getFacet());
			}
		}
	}

	public void testAnnotateDocument011() throws Exception {
		Document doc = new DefaultDocument("私は歩いて学校に行きます。");
		GinzaPosDependencyAnnotator ann = new GinzaPosDependencyAnnotator();
		ann.setProperty("target", "text");
		ann.setProperty("extract_keyword", "true");
		ann.annotate(doc);
		for (KeywordWithDependency kwd : doc.getKeywords(KeywordWithDependency.class)) {
			System.err.println(kwd.toStringAsXml());
			List<Keyword> kwds = KeywordWithDependencyParser.parse(kwd, "nsubj", "obl");
			for (Keyword kw : kwds) {
				System.err.println(kw.getLex() + "," + kw.getFacet());
			}
		}
		System.err.println("---");
		for (DefaultKeyword kwd : doc.getKeywords(DefaultKeyword.class)) {
			System.err.println(kwd.toString());
		}
	}

	public void testAnnotateDocument004() throws Exception {
		Document doc = new DefaultDocument("私は箸で寿司を食べます。私は寿司が好きです。");
		GinzaPosDependencyAnnotator ann = new GinzaPosDependencyAnnotator();
		ann.setProperty("target", "text");
		ann.annotate(doc);
		for (KeywordWithDependency kwd : doc.getKeywords(KeywordWithDependency.class)) {
			System.err.println(kwd.toStringAsXml());
			List<Keyword> kwds = KeywordWithDependencyParser.parse(kwd, "nsubj", "obl");
			for (Keyword kw : kwds) {
				System.err.println(kw.getLex() + "," + kw.getFacet());
			}
		}
	}

	public void testAnnotateDocument005() throws Exception {
		// https://www.jstage.jst.go.jp/article/jnlp/26/1/26_3/_pdf
		Document doc = new DefaultDocument("高津さんは朝早く起きるのが苦手だ。");
		GinzaPosDependencyAnnotator ann = new GinzaPosDependencyAnnotator();
		ann.setProperty("target", "text");
		ann.annotate(doc);
		for (KeywordWithDependency kwd : doc.getKeywords(KeywordWithDependency.class)) {
			System.err.println(kwd.toStringAsXml());
			List<Keyword> kwds = KeywordWithDependencyParser.parse(kwd);
			for (Keyword kw : kwds) {
				System.err.println(kw.getLex() + "," + kw.getFacet());
			}
		}
	}

	public void testAnnotateDocument006() throws Exception {
		Document doc = new DefaultDocument("私は日本アイ・ビー・エム株式会社に２００１年に入社しました。");
		GinzaPosDependencyAnnotator ann = new GinzaPosDependencyAnnotator();
		ann.setProperty("target", "text");
		ann.annotate(doc);
		for (KeywordWithDependency kwd : doc.getKeywords(KeywordWithDependency.class)) {
			System.err.println(kwd.toStringAsXml());
			List<Keyword> kwds = KeywordWithDependencyParser.parse(kwd);
			for (Keyword kw : kwds) {
				System.err.println(kw.getLex() + "," + kw.getFacet());
			}
		}
	}

	public void testAnnotateDocument007() throws Exception {
		Document doc = new DefaultDocument("私は牛丼を食べる");
		GinzaPosDependencyAnnotator ann = new GinzaPosDependencyAnnotator();
		ann.setProperty("target", "text");
		ann.annotate(doc);
		for (KeywordWithDependency kwd : doc.getKeywords(KeywordWithDependency.class)) {
			System.err.println(kwd.toStringAsXml());
			List<Keyword> kwds = KeywordWithDependencyParser.parse(kwd);
			for (Keyword kw : kwds) {
				System.err.println(kw.getLex() + "," + kw.getFacet());
			}
		}
	}

	public void testAnnotateDocument008() throws Exception {
		Document doc = new DefaultDocument("私は学校に行く");
		GinzaPosDependencyAnnotator ann = new GinzaPosDependencyAnnotator();
		ann.setProperty("target", "text");
		ann.annotate(doc);
		for (KeywordWithDependency kwd : doc.getKeywords(KeywordWithDependency.class)) {
			System.err.println(kwd.toStringAsXml());
			List<Keyword> kwds = KeywordWithDependencyParser.parse(kwd);
			for (Keyword kw : kwds) {
				System.err.println(kw.getLex() + "," + kw.getFacet());
			}
		}
	}

	public void testAnnotateDocument009() throws Exception {
		Document doc = new DefaultDocument("立地は悪いが食事が美味しい。");
		GinzaPosDependencyAnnotator ann = new GinzaPosDependencyAnnotator();
		ann.setProperty("target", "text");
		ann.annotate(doc);
		for (KeywordWithDependency kwd : doc.getKeywords(KeywordWithDependency.class)) {
			System.err.println(kwd.toStringAsXml());
			List<Keyword> kwds = KeywordWithDependencyParser.parse(kwd);
			for (Keyword kw : kwds) {
				System.err.println(kw.getLex() + "," + kw.getFacet());
			}
		}
	}

	public void testAnnotateDocument010() throws Exception {
		Document doc = new DefaultDocument("立地は良くなくて食事も美味しくない。");
		GinzaPosDependencyAnnotator ann = new GinzaPosDependencyAnnotator();
		ann.setProperty("target", "text");
		ann.annotate(doc);
		for (KeywordWithDependency kwd : doc.getKeywords(KeywordWithDependency.class)) {
			System.err.println(kwd.toStringAsXml());
			List<Keyword> kwds = KeywordWithDependencyParser.parse(kwd);
			for (Keyword kw : kwds) {
				System.err.println(kw.getLex() + "," + kw.getFacet());
			}
		}
	}

	public void testAnnotateDocument101() throws Exception {
		Document doc = new DefaultDocument("私は学校に行く");
		{
			GinzaPosDependencyAnnotator ann = new GinzaPosDependencyAnnotator();
			ann.setProperty("target", "text");
			ann.annotate(doc);
		}
		{
			StandardPatternAnnotatorJa ann = new StandardPatternAnnotatorJa();
			ann.setProperty("target", "text");
			ann.annotate(doc);
		}

		System.err.println(DocumentUtil.toJsonPrettyString(doc));

	}

	public void testAnnotateDocument301() throws Exception {
		Document doc = new DefaultDocument("私は歩いて学校に行きます。");
		{
			GinzaPosDependencyAnnotator ann = new GinzaPosDependencyAnnotator();
			ann.setProperty("target", "text");
			ann.annotate(doc);
		}
//		System.err.println(DocumentUtil.toJsonPrettyString(doc));
		for (KeywordWithDependency kwd : doc.getKeywords(KeywordWithDependency.class)) {
			System.err.println(kwd.toStringAsXml());
		}
	}

	public void testAnnotateDocument302() throws Exception {
		String text = "2022年の流行語大賞は「村神様」「キーウ」「きつねダンス」「スマホショルダー」「てまえどり」「ヤクルト１０００」など。"
				+ "２０２０年は「３蜜」「あつ森」「アベノマスク」「アマビエ」「鬼滅の刃」「GoToキャンペーン」「ソロキャンプ」「フワちゃん」など。" + "２０１０年は「イクメン」「AKB48」「女子会」など。";
		Document doc = new DefaultDocument(text);
		{
			GinzaPosDependencyAnnotator ann = new GinzaPosDependencyAnnotator();
			ann.setProperty("target", "text");
			ann.annotate(doc);
		}
//		System.err.println(DocumentUtil.toJsonPrettyString(doc));
//		for (KeywordWithDependency kwd : doc.getKeywords(KeywordWithDependency.class)) {
//			System.err.println(kwd.toStringAsXml());
//		}

		System.err.println("text: " + text);

		for (KeywordWithDependency kwd : doc.getKeywords(KeywordWithDependency.class)) {
			for (Keyword k : KeywordWithDependencyParser.flatten(kwd, null)) {
				if (k.getFacet().contains("記号")) {
					continue;
				}
				System.err.println(k.getFacet() + "," + k.getLex());
			}
		}

	}

	public void testSetProperty() {
		GinzaPosDependencyAnnotator ann = new GinzaPosDependencyAnnotator();
		ann.setProperty("target", "text");
	}

}
