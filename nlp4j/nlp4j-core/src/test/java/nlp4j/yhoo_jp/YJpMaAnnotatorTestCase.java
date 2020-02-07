package nlp4j.yhoo_jp;

import junit.framework.TestCase;
import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.Keyword;
import nlp4j.impl.DefaultDocument;
import nlp4j.yhoo_jp.YJpMaAnnotator;

/**
 * TestCase for YJpMaAnnotator
 *
 */
public class YJpMaAnnotatorTestCase extends TestCase {

	@SuppressWarnings("rawtypes")
	Class target = YJpMaAnnotator.class;

	/**
	 * Test case for "今日はいい天気です。"
	 * 
	 * @throws Exception 例外発生時
	 */
	public void testAnnotateDocument001() throws Exception {
		String text = "今日はいい天気です。";
		Document doc = new DefaultDocument();
		{
			doc.setText(text);
			System.err.println(doc);
		}

		{
			DocumentAnnotator a1 = new YJpMaAnnotator();
			a1.annotate(doc);
			System.err.println(doc);
			for (Keyword kwd : doc.getKeywords()) {
				System.err.println(kwd);
			}
		}
		assertEquals("今日", doc.getKeywords().get(0).getLex());
		assertEquals("は", doc.getKeywords().get(1).getLex());
		assertEquals("いい", doc.getKeywords().get(2).getLex());
		assertEquals("天気", doc.getKeywords().get(3).getLex());
		assertEquals("です", doc.getKeywords().get(4).getLex());
		assertEquals("。", doc.getKeywords().get(5).getLex());

	}

	/**
	 * Test case for "100円拾った。". 数詞 is not extracted.
	 * 
	 * @throws Exception 例外発生時
	 */
	public void testAnnotateDocument002() throws Exception {
		String text = "私は100円を拾いました。";
		Document doc = new DefaultDocument();
		{
			doc.setText(text);
			System.err.println(doc);
		}

		{
			DocumentAnnotator a1 = new YJpMaAnnotator();
			a1.annotate(doc);
			System.err.println(doc);
			for (Keyword kwd : doc.getKeywords()) {
				System.err.println(kwd);
			}
		}

		assertEquals("私", doc.getKeywords().get(0).getLex());
		assertEquals("は", doc.getKeywords().get(1).getLex());
		assertEquals("100", doc.getKeywords().get(2).getLex());
		assertEquals("円", doc.getKeywords().get(3).getLex());
		assertEquals("を", doc.getKeywords().get(4).getLex());
		assertEquals("拾う", doc.getKeywords().get(5).getLex());
		assertEquals("ます", doc.getKeywords().get(6).getLex());
		assertEquals("た", doc.getKeywords().get(7).getLex());
		assertEquals("。", doc.getKeywords().get(8).getLex());
	}

	/**
	 * Test case for "私は学校に行きました。"
	 * 
	 * @throws Exception 例外発生時
	 */
	public void testAnnotateDocument003() throws Exception {
		// 自然文のテキスト
		String text = "私は学校に行きました。";
		Document doc = new DefaultDocument();
		doc.putAttribute("text", text);
		YJpMaAnnotator annotator = new YJpMaAnnotator();
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
	 * 英文字の形態素解析をテストする
	 * 
	 * @throws Exception
	 */
	public void testAnnotateDocument004() throws Exception {
		// 自然文のテキスト
		String text = "私はEVを買いました。";
		Document doc = new DefaultDocument();
		doc.putAttribute("text", text);
		YJpMaAnnotator annotator = new YJpMaAnnotator();
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
