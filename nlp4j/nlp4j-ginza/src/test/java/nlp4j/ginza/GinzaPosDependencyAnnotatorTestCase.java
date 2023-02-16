package nlp4j.ginza;

import java.util.List;

import junit.framework.TestCase;
import nlp4j.Document;
import nlp4j.Keyword;
import nlp4j.KeywordWithDependency;
import nlp4j.KeywordWithDependencyParser;
import nlp4j.impl.DefaultDocument;

public class GinzaPosDependencyAnnotatorTestCase extends TestCase {

	public void testAnnotateDocument001() throws Exception {
		Document doc = new DefaultDocument("今日はいい天気です。明日はいい天気です。");
		GinzaPosDependencyAnnotator ann = new GinzaPosDependencyAnnotator();
		ann.setProperty("target", "text");
		ann.annotate(doc);
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

	public void testSetProperty() {
		GinzaPosDependencyAnnotator ann = new GinzaPosDependencyAnnotator();
		ann.setProperty("target", "text");

	}

}
