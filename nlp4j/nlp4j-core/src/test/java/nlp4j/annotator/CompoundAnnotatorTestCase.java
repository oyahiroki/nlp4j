package nlp4j.annotator;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;
import nlp4j.Document;
import nlp4j.Keyword;
import nlp4j.impl.DefaultDocument;
import nlp4j.impl.DefaultKeyword;

public class CompoundAnnotatorTestCase extends TestCase {

	public void testAnnotateDocument001() throws Exception {

		Document doc = new DefaultDocument();
		{
			Keyword kwd = new DefaultKeyword();
			kwd.setFacet("word.NN");
			kwd.setLex("プリンタ");
			kwd.setStr("プリンタ");
			doc.addKeyword(kwd);
		}
		{
			Keyword kwd = new DefaultKeyword();
			kwd.setFacet("word.NN");
			kwd.setLex("ドライバ");
			kwd.setStr("ドライバ");
			doc.addKeyword(kwd);
		}

		System.err.println(doc);
		for (Keyword kwd : doc.getKeywords()) {
			System.err.println(kwd.getLex() + "\t" + kwd.getFlag());
		}

		assertEquals(2, doc.getKeywords().size());

		CompoundAnnotator annotator = new CompoundAnnotator();
		annotator.annotate(doc);

		assertEquals(3, doc.getKeywords().size());

		System.err.println(doc);
		for (Keyword kwd : doc.getKeywords()) {
			System.err.println(kwd.getLex() + "\t" + kwd.getFlag());
		}

		// フラグの付いたキーワードを削除する
		doc.removeFlaggedKeyword();

		System.err.println(doc);
		for (Keyword kwd : doc.getKeywords()) {
			System.err.println(kwd.getLex() + "\t" + kwd.getFlag());
		}
		assertEquals(1, doc.getKeywords().size());

	}

	public void testAnnotateDocument002() throws Exception {

		Document doc = new DefaultDocument();
		{
			Keyword kwd = new DefaultKeyword();
			kwd.setFacet("word.NN");
			kwd.setLex("ウィルス");
			kwd.setStr("ウィルス");
			doc.addKeyword(kwd);
		}
		{
			Keyword kwd = new DefaultKeyword();
			kwd.setFacet("word.NN");
			kwd.setLex("パンデ");
			kwd.setStr("パンデ");
			doc.addKeyword(kwd);
		}
		{
			Keyword kwd = new DefaultKeyword();
			kwd.setFacet("word.NN");
			kwd.setLex("ミック");
			kwd.setStr("ミック");
			doc.addKeyword(kwd);
		}

		System.err.println(doc);
		for (Keyword kwd : doc.getKeywords()) {
			System.err.println(kwd.getLex() + "\t" + kwd.getFlag());
		}

		assertEquals(3, doc.getKeywords().size());

		// 複合名詞を抽出する
		CompoundAnnotator annotator = new CompoundAnnotator();
		annotator.annotate(doc);

		System.err.println(doc);
		for (Keyword kwd : doc.getKeywords()) {
			System.err.println(kwd.getLex() + "\t" + kwd.getFlag());
		}
		assertEquals(6, doc.getKeywords().size());

		// フラグの付いたキーワードを削除する
		doc.removeFlaggedKeyword();

		System.err.println(doc);
		for (Keyword kwd : doc.getKeywords()) {
			System.err.println(kwd.getLex() + "\t" + kwd.getFlag());
		}
		assertEquals(3, doc.getKeywords().size());

	}

}
