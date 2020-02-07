package nlp4j.krmj.annotator;

import junit.framework.TestCase;
import nlp4j.Document;
import nlp4j.Keyword;
import nlp4j.impl.DefaultDocument;

/**
 * @author Hiroki Oya
 * @since 1.2
 *
 */
public class KuromojiAnnotatorTestCase extends TestCase {

	/**
	 * 「私は学校に行きました」の形態素解析結果をテスト
	 * 
	 * @throws Exception
	 */
	public void testAnnotateDocument001() throws Exception {

		// 自然文のテキスト
		String text = "私は学校に行きました。";
		Document doc = new DefaultDocument();
		doc.putAttribute("text", text);
		KuromojiAnnotator annotator = new KuromojiAnnotator();
		annotator.setProperty("target", "text");
		annotator.annotate(doc); // throws Exception
		System.err.println("Finished : annotation");

		assertNotNull(doc.getKeywords());
		assertTrue(doc.getKeywords().size() > 0);

		for (Keyword kwd : doc.getKeywords()) {
			System.err.println(kwd);
		}

		assertEquals("私", doc.getKeywords().get(0).getLex());
		assertEquals("は", doc.getKeywords().get(1).getLex());
		assertEquals("学校", doc.getKeywords().get(2).getLex());
		assertEquals("に", doc.getKeywords().get(3).getLex());
		assertEquals("行く", doc.getKeywords().get(4).getLex());
		assertEquals("ます", doc.getKeywords().get(5).getLex());
		assertEquals("た", doc.getKeywords().get(6).getLex());
		assertEquals("。", doc.getKeywords().get(7).getLex());

	}

	/**
	 * 英文字の形態素解析結果をテスト
	 * 
	 * @since 1.2.0.1
	 * @throws Exception
	 */
	public void testAnnotateDocument002() throws Exception {

		// 自然文のテキスト
		String text = "私はEVを買いました。";
		Document doc = new DefaultDocument();
		doc.putAttribute("text", text);
		KuromojiAnnotator annotator = new KuromojiAnnotator();
		annotator.setProperty("target", "text");
		annotator.annotate(doc); // throws Exception
		System.err.println("Finished : annotation");

		assertNotNull(doc.getKeywords());
		assertTrue(doc.getKeywords().size() > 0);

		for (Keyword kwd : doc.getKeywords()) {
			System.err.println(kwd);
		}

		assertEquals("私", doc.getKeywords().get(0).getLex());
		assertEquals("は", doc.getKeywords().get(1).getLex());
		assertEquals("EV", doc.getKeywords().get(2).getLex());
		assertEquals("を", doc.getKeywords().get(3).getLex());
		assertEquals("買う", doc.getKeywords().get(4).getLex());
		assertEquals("ます", doc.getKeywords().get(5).getLex());
		assertEquals("た", doc.getKeywords().get(6).getLex());
		assertEquals("。", doc.getKeywords().get(7).getLex());

	}

}
