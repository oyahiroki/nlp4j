package nlp4j.mecab;

import junit.framework.TestCase;
import nlp4j.Document;
import nlp4j.Keyword;
import nlp4j.impl.DefaultDocument;

/**
 * @author Hiroki Oya
 * @since 1.0.0.0
 *
 */
public class MecabAnnotatorTestCase extends TestCase {

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
		MecabAnnotator annotator = new MecabAnnotator();
		annotator.setProperty("target", "text");
		annotator.annotate(doc); // throws Exception
		System.err.println("Finished : annotation");

		assertNotNull(doc.getKeywords());
		assertTrue(doc.getKeywords().size() > 0);

		for (Keyword kwd : doc.getKeywords()) {
			System.err.println(kwd.getLex() + "," + kwd.getFacet() + "," + kwd.getUPos());
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
		MecabAnnotator annotator = new MecabAnnotator();
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

	/**
	 * 「空気が美味い。」の形態素解析結果をテスト
	 * 
	 * @throws Exception
	 */
	public void testAnnotateDocument003() throws Exception {

		// 自然文のテキスト
		String text = "空気が美味い。";
		Document doc = new DefaultDocument();
		doc.putAttribute("text", text);
		MecabAnnotator annotator = new MecabAnnotator();
		annotator.setProperty("target", "text");
		annotator.annotate(doc); // throws Exception
		System.err.println("Finished : annotation");

		assertNotNull(doc.getKeywords());
		assertTrue(doc.getKeywords().size() > 0);

		for (Keyword kwd : doc.getKeywords()) {
			System.err.println(kwd.getLex() + "," + kwd.getFacet() + "," + kwd.getUPos());
		}

		assertEquals("空気", doc.getKeywords().get(0).getLex());
		assertEquals("が", doc.getKeywords().get(1).getLex());
		assertEquals("美味い", doc.getKeywords().get(2).getLex());

	}

	/**
	 * 「希望が託される。」の形態素解析結果をテスト
	 * 
	 * @throws Exception
	 */
	public void testAnnotateDocument004() throws Exception {

		// 自然文のテキスト
		String text = "希望が託される。";
		Document doc = new DefaultDocument();
		doc.putAttribute("text", text);
		MecabAnnotator annotator = new MecabAnnotator();
		annotator.setProperty("target", "text");
		annotator.annotate(doc); // throws Exception
		System.err.println("Finished : annotation");

		assertNotNull(doc.getKeywords());
		assertTrue(doc.getKeywords().size() > 0);

		for (Keyword kwd : doc.getKeywords()) {
			System.err.println(kwd.getLex() + "," + kwd.getFacet() + "," + kwd.getUPos());
		}

		assertEquals("希望", doc.getKeywords().get(0).getLex());
		assertEquals("が", doc.getKeywords().get(1).getLex());
		assertEquals("託す", doc.getKeywords().get(2).getLex());
		assertEquals("れる", doc.getKeywords().get(3).getLex());

	}

	/**
	 * 「甘えたいと思う。」の形態素解析結果をテスト
	 * 
	 * @throws Exception
	 */
	public void testAnnotateDocument005() throws Exception {

		// 自然文のテキスト
		String text = "甘えたいと思う。";
		Document doc = new DefaultDocument();
		doc.putAttribute("text", text);
		MecabAnnotator annotator = new MecabAnnotator();
		annotator.setProperty("target", "text");
		annotator.annotate(doc); // throws Exception
		System.err.println("Finished : annotation");

		assertNotNull(doc.getKeywords());
		assertTrue(doc.getKeywords().size() > 0);

		for (Keyword kwd : doc.getKeywords()) {
			System.err.println(kwd.getLex() + "," + kwd.getFacet() + "," + kwd.getUPos());
		}

		assertEquals("甘える", doc.getKeywords().get(0).getLex());
		assertEquals("たい", doc.getKeywords().get(1).getLex());
		assertEquals("と", doc.getKeywords().get(2).getLex());
		assertEquals("思う", doc.getKeywords().get(3).getLex());

	}

}
