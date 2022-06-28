package nlp4j.mecab;

import java.io.File;

import junit.framework.TestCase;
import nlp4j.Document;
import nlp4j.Keyword;
import nlp4j.impl.DefaultDocument;
import nlp4j.test.TestUtils;

/**
 * @author Hiroki Oya
 * @since 1.0.0.0
 *
 */
public class MecabAnnotatorTestCase extends TestCase {

	static {
		TestUtils.setLevelDebug();
	}

	/**
	 * 「私は学校に行きました」の形態素解析結果をテスト
	 * 
	 * @throws Exception on Error
	 */
	public void testAnnotateDocument001() throws Exception {

		// 自然文のテキスト
		String text = "私は学校に行きました。";
		Document doc = new DefaultDocument();
		doc.putAttribute("text", text);
		MecabAnnotator annotator = new MecabAnnotator();
		{
			annotator.setProperty("target", "text");
		}
		annotator.annotate(doc); // throws Exception
		System.err.println("Finished : annotation");

		assertNotNull(doc.getKeywords());
		assertTrue(doc.getKeywords().size() > 0);

		for (Keyword kwd : doc.getKeywords()) {
			System.err.println( //
					"lex=" + kwd.getLex() //
							+ ",facet=" + kwd.getFacet() //
							+ ",upos=" + kwd.getUPos() //
							+ ",reading=" + kwd.getReading() //

			);
		}

		assertEquals("私", doc.getKeywords().get(0).getLex());
		assertEquals("は", doc.getKeywords().get(1).getLex());
		assertEquals("学校", doc.getKeywords().get(2).getLex());
		assertEquals("に", doc.getKeywords().get(3).getLex());
		assertEquals("行く", doc.getKeywords().get(4).getLex());
		assertEquals("ます", doc.getKeywords().get(5).getLex());
		assertEquals("た", doc.getKeywords().get(6).getLex());
		assertEquals("。", doc.getKeywords().get(7).getLex());
		annotator.close();
	}

	/**
	 * 英文字の形態素解析結果をテスト
	 * 
	 * @since 1.2.0.1
	 * @throws Exception on Error
	 */
	public void testAnnotateDocument002() throws Exception {

		// 自然文のテキスト
		String text = "私はEVを買いました。";
		Document doc = new DefaultDocument();
		{
			doc.putAttribute("text", text);
		}
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
		annotator.close();

	}

	/**
	 * 「空気が美味い。」の形態素解析結果をテスト
	 * 
	 * @throws Exception on Error
	 */
	public void testAnnotateDocument003() throws Exception {

		// 自然文のテキスト
		String text = "空気が美味い。";
		Document doc = new DefaultDocument();
		{
			doc.putAttribute("text", text);
		}
		MecabAnnotator annotator = new MecabAnnotator();
		{
			annotator.setProperty("target", "text");
			annotator.annotate(doc); // throws Exception
		}
		System.err.println("Finished : annotation");

		assertNotNull(doc.getKeywords());
		assertTrue(doc.getKeywords().size() > 0);

		for (Keyword kwd : doc.getKeywords()) {
			System.err.println(kwd.getLex() + "," + kwd.getFacet() + "," + kwd.getUPos());
		}

		assertEquals("空気", doc.getKeywords().get(0).getLex());
		assertEquals("が", doc.getKeywords().get(1).getLex());
		assertEquals("美味い", doc.getKeywords().get(2).getLex());
		annotator.close();

	}

	/**
	 * 「希望が託される。」の形態素解析結果をテスト
	 * 
	 * @throws Exception on Error
	 */
	public void testAnnotateDocument004() throws Exception {

		// 自然文のテキスト
		String text = "希望が託される。";
		Document doc = new DefaultDocument();
		{
			doc.putAttribute("text", text);
		}
		MecabAnnotator annotator = new MecabAnnotator();
		{
			annotator.setProperty("target", "text");
		}
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
		annotator.close();

	}

	/**
	 * 「甘えたいと思う。」の形態素解析結果をテスト
	 * 
	 * @throws Exception on Error
	 */
	public void testAnnotateDocument005() throws Exception {

		// 自然文のテキスト
		String text = "甘えたいと思う。";
		Document doc = new DefaultDocument();
		{
			doc.putAttribute("text", text);
		}
		MecabAnnotator annotator = new MecabAnnotator();
		{
			annotator.setProperty("target", "text");
		}
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
		annotator.close();

	}

	/**
	 * 「私は学校に行きました\n彼は家で過ごしました。」の形態素解析結果をテスト
	 * 
	 * @throws Exception on Error
	 */
	public void testAnnotateDocument101() throws Exception {

		// 自然文のテキスト
		String text = "私は学校に行きました。\n彼は家で過ごしました。";
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
		annotator.close();

	}

	/**
	 * 「私は学校に行きました\n彼は家で過ごしました。」の形態素解析結果をテスト
	 * 
	 * @throws Exception on Error
	 */
	public void testAnnotateDocument102() throws Exception {

		// 自然文のテキスト
		String text = "# 私は「学校 gakkou 」に行きました。IBM。日産。";
		Document doc = new DefaultDocument();
		doc.putAttribute("text", text);
		MecabAnnotator annotator = new MecabAnnotator();
		{
			annotator.setProperty("facetfilter", "名詞,形容詞");
			annotator.setProperty("target", "text");
			annotator.setProperty("lexregexfilter", "^[^a-zA-Z0-9!-/:-@\\[-`{-~]*$");

			// ^[a-zA-Z0-9!-/:-@¥[-`{-~]*$
		}
		annotator.annotate(doc); // throws Exception
		System.err.println("Finished : annotation");

		assertNotNull(doc.getKeywords());
		assertTrue(doc.getKeywords().size() > 0);

		for (Keyword kwd : doc.getKeywords()) {
			System.err.println(kwd.getLex() + "," + kwd.getFacet() + "," + kwd.getUPos());
		}

//		assertEquals("私", doc.getKeywords().get(0).getLex());
//		assertEquals("は", doc.getKeywords().get(1).getLex());
//		assertEquals("学校", doc.getKeywords().get(2).getLex());
//		assertEquals("に", doc.getKeywords().get(3).getLex());
//		assertEquals("行く", doc.getKeywords().get(4).getLex());
//		assertEquals("ます", doc.getKeywords().get(5).getLex());
//		assertEquals("た", doc.getKeywords().get(6).getLex());
//		assertEquals("。", doc.getKeywords().get(7).getLex());
		annotator.close();

	}

	/**
	 * 「今年は令和４年です．」の形態素解析結果をテスト
	 * 
	 * @throws Exception on Error
	 */
	public void testAnnotateDocument200() throws Exception {

		// 自然文のテキスト
		String text = "令和４年です．";
		Document doc = new DefaultDocument();
		{
			doc.putAttribute("text", text);
		}
		MecabAnnotator annotator = new MecabAnnotator();
		{
			annotator.setProperty("target", "text");
		}
		annotator.annotate(doc); // throws Exception
		System.err.println("Finished : annotation");

		assertNotNull(doc.getKeywords());
		assertTrue(doc.getKeywords().size() > 0);

		for (Keyword kwd : doc.getKeywords()) {
			System.err.println(kwd.getLex() + "," + kwd.getFacet() + "," + kwd.getUPos());
		}

		assertEquals("令", doc.getKeywords().get(0).getLex());
		assertEquals("和", doc.getKeywords().get(1).getLex());
		annotator.close();

	}

	/**
	 * NEologdを使っての形態素解析結果をテスト
	 * 
	 * @throws Exception on Error
	 */
	public void testAnnotateDocumentNEologd201() throws Exception {

		// ■注意■ ファイル区切りはスラッシュ
		String neologdFile = "/usr/local/MeCab/dic/NEologd/NEologd.20200910.dic";
		{
			File dirNeologD = new File(neologdFile.replace("\\", "/"));
			if (dirNeologD.exists() == false) {
				System.err.println("Not exists: " + dirNeologD.getAbsolutePath());
				return;
			}
		}

		// 自然文のテキスト
		String text = "令和４年です．";
		Document doc = new DefaultDocument();
		doc.putAttribute("text", text);
		MecabAnnotator annotator = new MecabAnnotator();
		{
			annotator.setProperty("target", "text");
			annotator.setProperty("option", "-u " + neologdFile);
		}
		annotator.annotate(doc); // throws Exception
		System.err.println("Finished : annotation");

		assertNotNull(doc.getKeywords());
		assertTrue(doc.getKeywords().size() > 0);

		for (Keyword kwd : doc.getKeywords()) {
			System.err.println(kwd.getLex() + "," + kwd.getFacet() + "," + kwd.getUPos());
		}

		assertEquals("令和", doc.getKeywords().get(0).getLex());

		annotator.close();
	}

	/**
	 * NEologdを使っての形態素解析結果をテスト
	 * 
	 * @throws Exception on Error
	 */
	public void testAnnotateDocumentNEologd202() throws Exception {

		// ■注意■ ファイル区切りはスラッシュ
		String neologdFile = "/usr/local/MeCab/dic/NEologd/NEologd.20200910.dic";
		{
			File dirNeologD = new File(neologdFile.replace("\\", "/"));
			if (dirNeologD.exists() == false) {
				System.err.println("Not exists: " + dirNeologD.getAbsolutePath());
				return;
			}
		}

		// 自然文のテキスト
		String text = "Nintendo Switchです．";
		Document doc = new DefaultDocument();
		doc.putAttribute("text", text);
		MecabAnnotator annotator = new MecabAnnotator();
		annotator.setProperty("target", "text");
		annotator.setProperty("option", "-u " + neologdFile);
		annotator.annotate(doc); // throws Exception
		System.err.println("Finished : annotation");

		assertNotNull(doc.getKeywords());
		assertTrue(doc.getKeywords().size() > 0);

		for (Keyword kwd : doc.getKeywords()) {
			System.err.println(kwd.getLex() + "," + kwd.getFacet() + "," + kwd.getUPos());
		}

		assertEquals("Nintendo Switch", doc.getKeywords().get(0).getLex());
		assertEquals("固有名詞", doc.getKeywords().get(0).getFacet());

		annotator.close();

	}

	/**
	 * NEologdを使っての形態素解析結果をテスト
	 * 
	 * @throws Exception on Error
	 */
	public void testAnnotateDocumentNEologd203() throws Exception {

		// ■注意■ ファイル区切りはスラッシュ
		String neologdFile = "/usr/local/MeCab/dic/NEologd/NEologd.20200910.dic";
		{
			File dirNeologD = new File(neologdFile.replace("\\", "/"));
			if (dirNeologD.exists() == false) {
				System.err.println("Not exists: " + dirNeologD.getAbsolutePath());
				return;
			}
		}

		// 自然文のテキスト
		String text = "サザエさんをテレビで見た．";
		Document doc = new DefaultDocument();
		doc.putAttribute("text", text);
		MecabAnnotator annotator = new MecabAnnotator();
		annotator.setProperty("target", "text");
		annotator.setProperty("option", "-u " + neologdFile);
		annotator.annotate(doc); // throws Exception
		System.err.println("Finished : annotation");

		assertNotNull(doc.getKeywords());
		assertTrue(doc.getKeywords().size() > 0);

		for (Keyword kwd : doc.getKeywords()) {
			System.err.println(kwd.getLex() + "," + kwd.getFacet() + "," + kwd.getUPos());
		}

		assertEquals("サザエさん", doc.getKeywords().get(0).getLex());
		assertEquals("固有名詞", doc.getKeywords().get(0).getFacet());

		annotator.close();

	}

	/**
	 * NEologdを使っての形態素解析結果をテスト
	 * 
	 * @throws Exception on Error
	 */
	public void testAnnotateDocumentNEologd204() throws Exception {

		// ■注意■ ファイル区切りはスラッシュ
		String neologdFile = "/usr/local/MeCab/dic/NEologd/NEologd.20200910.dic";
		{
			File dirNeologD = new File(neologdFile.replace("\\", "/"));
			if (dirNeologD.exists() == false) {
				System.err.println("Not exists: " + dirNeologD.getAbsolutePath());
				return;
			}
		}

		// 自然文のテキスト
		String text = "こち亀を読んだ．";
		Document doc = new DefaultDocument();
		doc.putAttribute("text", text);
		MecabAnnotator annotator = new MecabAnnotator();
		annotator.setProperty("target", "text");
		annotator.setProperty("option", "-u " + neologdFile);
		annotator.annotate(doc); // throws Exception
		System.err.println("Finished : annotation");

		assertNotNull(doc.getKeywords());
		assertTrue(doc.getKeywords().size() > 0);

		for (Keyword kwd : doc.getKeywords()) {
			System.err.println(kwd.getLex() + "," + kwd.getFacet() + "," + kwd.getUPos());
		}

		assertEquals("こち亀", doc.getKeywords().get(0).getLex());
		assertEquals("固有名詞", doc.getKeywords().get(0).getFacet());

		annotator.close();

	}
}
