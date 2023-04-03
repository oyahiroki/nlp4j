package nlp4j.ginza;

import java.util.List;

import junit.framework.TestCase;
import nlp4j.Document;
import nlp4j.Keyword;
import nlp4j.KeywordWithDependency;
import nlp4j.KeywordWithDependencyParser;
import nlp4j.impl.DefaultDocument;
import nlp4j.pattern.StandardPatternAnnotatorJa;
import nlp4j.pattern.UserPatternAnnotator;
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

	public void testAnnotateDocument201() throws Exception {
		Document doc = new DefaultDocument("私は学校に行く");
		{
			GinzaPosDependencyAnnotator ann = new GinzaPosDependencyAnnotator();
			ann.setProperty("target", "text");
			ann.annotate(doc);
		}
		{
			UserPatternAnnotator ann = new UserPatternAnnotator();
			ann.setProperty("resource", "nlp4j.pattern/pattern-ud-ja-sv.xml");
			ann.setProperty("target", "text");
			ann.annotate(doc);

		}
		System.err.println(DocumentUtil.toJsonPrettyString(doc));
		for (KeywordWithDependency kwd : doc.getKeywords(KeywordWithDependency.class)) {
			System.err.println(kwd.toStringAsXml());
			List<Keyword> kwds = KeywordWithDependencyParser.parse(kwd);
			for (Keyword kw : kwds) {
				System.err.println(kw.getLex() + "," + kw.getFacet());
			}
		}
	}

	public void testAnnotateDocument202() throws Exception {
		Document doc = new DefaultDocument("赤ちゃんはかわいい");
		{
			GinzaPosDependencyAnnotator ann = new GinzaPosDependencyAnnotator();
			ann.setProperty("target", "text");
			ann.annotate(doc);
		}
		{
			UserPatternAnnotator ann = new UserPatternAnnotator();
			ann.setProperty("resource", "nlp4j.pattern/pattern-ud-ja-sv.xml");
			ann.setProperty("target", "text");
			ann.annotate(doc);

		}
		System.err.println(DocumentUtil.toJsonPrettyString(doc));
		for (KeywordWithDependency kwd : doc.getKeywords(KeywordWithDependency.class)) {
			System.err.println(kwd.toStringAsXml());
			List<Keyword> kwds = KeywordWithDependencyParser.parse(kwd);
			for (Keyword kw : kwds) {
				System.err.println(kw.getLex() + "," + kw.getFacet());
			}
		}
	}

	public void testAnnotateDocument203() throws Exception {
		Document doc = new DefaultDocument("かわいい赤ちゃん");
		{
			GinzaPosDependencyAnnotator ann = new GinzaPosDependencyAnnotator();
			ann.setProperty("target", "text");
			ann.annotate(doc);
		}
		{
			UserPatternAnnotator ann = new UserPatternAnnotator();
			ann.setProperty("resource", "nlp4j.pattern/pattern-ud-ja-sv.xml");
			ann.setProperty("resource", "nlp4j.pattern/pattern-ud-ja-noun_adj.xml");
			ann.setProperty("target", "text");
			ann.annotate(doc);

		}
		System.err.println(DocumentUtil.toJsonPrettyString(doc));
		for (KeywordWithDependency kwd : doc.getKeywords(KeywordWithDependency.class)) {
			System.err.println(kwd.toStringAsXml());
			List<Keyword> kwds = KeywordWithDependencyParser.parse(kwd);
			for (Keyword kw : kwds) {
				System.err.println(kw.getLex() + "," + kw.getFacet());
			}
		}
	}

	public void testAnnotateDocument204() throws Exception {
		Document doc = new DefaultDocument("パンダの赤ちゃんかわいいね");
		{
			GinzaPosDependencyAnnotator ann = new GinzaPosDependencyAnnotator();
			ann.setProperty("target", "text");
			ann.annotate(doc);
		}
		{
			UserPatternAnnotator ann = new UserPatternAnnotator();
			ann.setProperty("resource", "nlp4j.pattern/pattern-ud-ja-sv.xml");
			ann.setProperty("resource", "nlp4j.pattern/pattern-ud-ja-noun_adj.xml");
			ann.setProperty("target", "text");
			ann.annotate(doc);

		}
		System.err.println(DocumentUtil.toJsonPrettyString(doc));
		for (KeywordWithDependency kwd : doc.getKeywords(KeywordWithDependency.class)) {
			System.err.println(kwd.toStringAsXml());
			List<Keyword> kwds = KeywordWithDependencyParser.parse(kwd);
			for (Keyword kw : kwds) {
				System.err.println(kw.getLex() + "," + kw.getFacet());
			}
		}
	}

	public void testAnnotateDocument205() throws Exception {
		Document doc = new DefaultDocument("何らかの教育が体系的に行われる組織又はその設備");
		{
			GinzaPosDependencyAnnotator ann = new GinzaPosDependencyAnnotator();
			ann.setProperty("target", "text");
			ann.annotate(doc);
		}
		{
			UserPatternAnnotator ann = new UserPatternAnnotator();
			ann.setProperty("resource", "nlp4j.pattern/pattern-ud-ja-sv.xml");
			ann.setProperty("resource", "nlp4j.pattern/pattern-ud-ja-noun_adj.xml");
			ann.setProperty("resource", "nlp4j.pattern/pattern-ud-ja-root_noun.xml");
			ann.setProperty("target", "text");
			ann.annotate(doc);

		}
		System.err.println(DocumentUtil.toJsonPrettyString(doc));
		for (KeywordWithDependency kwd : doc.getKeywords(KeywordWithDependency.class)) {
			System.err.println(kwd.toStringAsXml());
			List<Keyword> kwds = KeywordWithDependencyParser.parse(kwd);
			for (Keyword kw : kwds) {
				System.err.println(kw.getLex() + "," + kw.getFacet());
			}
		}
	}

	public void testAnnotateDocument206() throws Exception {
		Document doc = new DefaultDocument("何らかの教育が体系的に行われる組織または設備");
		{
			GinzaPosDependencyAnnotator ann = new GinzaPosDependencyAnnotator();
			ann.setProperty("target", "text");
			ann.annotate(doc);
		}
		{
			UserPatternAnnotator ann = new UserPatternAnnotator();
			ann.setProperty("resource", "nlp4j.pattern/pattern-ud-ja-sv.xml");
			ann.setProperty("resource", "nlp4j.pattern/pattern-ud-ja-noun_adj.xml");
			ann.setProperty("resource", "nlp4j.pattern/pattern-ud-ja-root_noun.xml");
			ann.setProperty("target", "text");
			ann.annotate(doc);

		}
		System.err.println(DocumentUtil.toJsonPrettyString(doc));
		for (KeywordWithDependency kwd : doc.getKeywords(KeywordWithDependency.class)) {
			System.err.println(kwd.toStringAsXml());
			List<Keyword> kwds = KeywordWithDependencyParser.parse(kwd);
			for (Keyword kw : kwds) {
				System.err.println(kw.getLex() + "," + kw.getFacet());
			}
		}
	}

	public void testAnnotateDocument207() throws Exception {
		Document doc = new DefaultDocument("何らかの教育が体系的に行われる組織もしくは設備");
		{
			GinzaPosDependencyAnnotator ann = new GinzaPosDependencyAnnotator();
			ann.setProperty("target", "text");
			ann.annotate(doc);
		}
		{
			UserPatternAnnotator ann = new UserPatternAnnotator();
			ann.setProperty("resource", "nlp4j.pattern/pattern-ud-ja-sv.xml");
			ann.setProperty("resource", "nlp4j.pattern/pattern-ud-ja-noun_adj.xml");
			ann.setProperty("resource", "nlp4j.pattern/pattern-ud-ja-root_noun.xml");
			ann.setProperty("resource", "nlp4j.pattern/pattern-ud-ja-nsubj_acl.xml");
			ann.setProperty("target", "text");
			ann.annotate(doc);

		}
		System.err.println(DocumentUtil.toJsonPrettyString(doc));
		for (KeywordWithDependency kwd : doc.getKeywords(KeywordWithDependency.class)) {
			System.err.println(kwd.toStringAsXml());
			List<Keyword> kwds = KeywordWithDependencyParser.parse(kwd);
			for (Keyword kw : kwds) {
				System.err.println(kw.getLex() + "," + kw.getFacet());
			}
		}
	}

	public void testAnnotateDocument208() throws Exception {
		Document doc = new DefaultDocument("何らかの教育が体系的に行われる組織あるいは設備");
		{
			GinzaPosDependencyAnnotator ann = new GinzaPosDependencyAnnotator();
			ann.setProperty("target", "text");
			ann.annotate(doc);
		}
		{
			UserPatternAnnotator ann = new UserPatternAnnotator();
			ann.setProperty("resource", "nlp4j.pattern/pattern-ud-ja-sv.xml");
			ann.setProperty("resource", "nlp4j.pattern/pattern-ud-ja-noun_adj.xml");
			ann.setProperty("resource", "nlp4j.pattern/pattern-ud-ja-root_noun.xml");
			ann.setProperty("target", "text");
			ann.annotate(doc);

		}
		System.err.println(DocumentUtil.toJsonPrettyString(doc));
		for (KeywordWithDependency kwd : doc.getKeywords(KeywordWithDependency.class)) {
			System.err.println(kwd.toStringAsXml());
			List<Keyword> kwds = KeywordWithDependencyParser.parse(kwd);
			for (Keyword kw : kwds) {
				System.err.println(kw.getLex() + "," + kw.getFacet());
			}
		}
	}

	public void testSetProperty() {
		GinzaPosDependencyAnnotator ann = new GinzaPosDependencyAnnotator();
		ann.setProperty("target", "text");
	}

}
