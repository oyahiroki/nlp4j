package nlp4j.ginza;

import java.util.List;

import junit.framework.TestCase;
import nlp4j.Document;
import nlp4j.Keyword;
import nlp4j.KeywordWithDependency;
import nlp4j.KeywordWithDependencyParser;
import nlp4j.impl.DefaultDocument;
import nlp4j.impl.DefaultKeyword;
import nlp4j.pattern.UserPatternAnnotator;
import nlp4j.util.DocumentUtil;

public class GinzaPosDependencyAnnotatorTestCase200WithPatternRuleFile extends TestCase {
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

	/**
	 * <pre>
	 * test {0.relation} for pattern value
	 * created on: 2023-05-08
	 * </pre>
	 * 
	 * @throws Exception
	 */
	public void testAnnotateDocument209() throws Exception {
		Document doc = new DefaultDocument("私は学校に行く");
		{
			GinzaPosDependencyAnnotator ann = new GinzaPosDependencyAnnotator();
			ann.setProperty("target", "text");
			ann.annotate(doc);
		}
		{
			UserPatternAnnotator ann = new UserPatternAnnotator();
			ann.setProperty("resource", "nlp4j.pattern/pattern-ud-ja-relation.xml");
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
		System.err.println("---");
		for (Keyword kw : doc.getKeywords(DefaultKeyword.class)) {
			System.err.println(kw.getLex() + "," + kw.getFacet());
		}
	}

	public void testAnnotateDocument210() throws Exception {
		// 否定の抽出

		Document doc = new DefaultDocument( //
				"彼は勉強しない。" //
						+ "私は行かない。" //
						+ "その本は面白くない。" //
						+ "彼女は遅くない。" //
						+ "これは正しくない。" //
						+ "あなたは悲しくない。" //
						+ "私たちは忘れない。" //
						+ "彼らは来ない。" //
						+ "その計画は実現しない。" //
						+ "彼は笑わない。" //
		);
		{
			GinzaPosDependencyAnnotator ann = new GinzaPosDependencyAnnotator();
			ann.setProperty("target", "text");
			ann.annotate(doc);
		}
		{
			UserPatternAnnotator ann = new UserPatternAnnotator();
			ann.setProperty("resource", "nlp4j.pattern/pattern-ud-ja-nsubj-not.xml");
			ann.setProperty("target", "text");
			ann.annotate(doc);

		}
//		System.err.println(DocumentUtil.toJsonPrettyString(doc));
		for (KeywordWithDependency kwd : doc.getKeywords(KeywordWithDependency.class)) {
			System.err.println(kwd.toStringAsXml());
			List<Keyword> kwds = KeywordWithDependencyParser.parse(kwd);
			for (Keyword kw : kwds) {
				System.err.println(kw.getLex() + "," + kw.getFacet());
			}
		}
		System.err.println("---");
		for (Keyword kw : doc.getKeywords(DefaultKeyword.class)) {
			System.err.println(kw.getLex() + "," + kw.getFacet());
		}
	}

}
