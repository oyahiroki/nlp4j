package yhoo_jp;

import junit.framework.TestCase;
import nlp4j.DocumentAnnotator;
import nlp4j.Document;
import nlp4j.Keyword;
import nlp4j.impl.DefaultDocument;
import nlp4j.yhoo_jp.YjpAllAnnotator;

public class YjpAllAnnotatorTestCase extends TestCase {

	@SuppressWarnings("rawtypes")
	Class target = YjpAllAnnotator.class;

	public void testAnnotateDocument001() throws Exception {
		// 自然文のテキスト
		String text = "私は学校に行きました。";
		Document doc = new DefaultDocument();
		doc.putAttribute("description", text);
		DocumentAnnotator annotator = new YjpAllAnnotator();
		annotator.annotate(doc); // throws Exception
		System.err.println("Finished : annotation");
		for (Keyword kwd : doc.getKeywords()) {
			System.err.println(kwd);
		}
		System.err.println("名詞...動詞");
		for (Keyword kwd : doc.getKeywords("名詞...動詞")) {
			System.err.println(kwd);
		}
	}

	public void testAnnotateDocument002() throws Exception {
		// 自然文のテキスト
		String text = "私は急いで走って直接学校に行きました。";
		Document doc = new DefaultDocument();
		doc.putAttribute("description", text);
		DocumentAnnotator annotator = new YjpAllAnnotator();
		System.err.println("Finished : annotation");
		annotator.annotate(doc); // throws Exception
		for (Keyword kwd : doc.getKeywords()) {
			System.err.println(kwd);
		}
		System.err.println("名詞...動詞");
		for (Keyword kwd : doc.getKeywords("名詞...動詞")) {
			System.err.println(kwd);
		}
	}

	public void testAnnotateDocument003() throws Exception {
		// 自然文のテキスト
		String text = "設備が新しい。";
		Document doc = new DefaultDocument();
		doc.putAttribute("description", text);
		DocumentAnnotator annotator = new YjpAllAnnotator();
		System.err.println("Finished : annotation");
		annotator.annotate(doc); // throws Exception
		for (Keyword kwd : doc.getKeywords()) {
			System.err.println(kwd);
		}
		System.err.println("名詞...形容詞");
		for (Keyword kwd : doc.getKeywords("名詞...形容詞")) {
			System.err.println(kwd);
		}
	}

	public void testAnnotateDocument101() throws Exception {
		// 否定(1)
		String text = "私は本を読まない。";
		Document doc = new DefaultDocument();
		doc.setText(text);
		DocumentAnnotator annotator = new YjpAllAnnotator();
		System.err.println("Finished : annotation");
		annotator.annotate(doc); // throws Exception
		for (Keyword kwd : doc.getKeywords()) {
			System.err.println(kwd);
		}
		System.err.println("動詞");
		for (Keyword kwd : doc.getKeywords("動詞")) {
			System.err.println(kwd.getFacet() + ":" + kwd.getLex() + ":" + kwd.getStr());
		}
		System.err.println("名詞...動詞");
		for (Keyword kwd : doc.getKeywords("名詞...動詞")) {
			System.err.println(kwd.getFacet() + ":" + kwd.getLex() + ":" + kwd.getStr());
		}
	}

	public void testAnnotateDocument102() throws Exception {
		// 否定(2)
		String text = "私は本を読みたくない。";
		Document doc = new DefaultDocument();
		doc.setText(text);
		DocumentAnnotator annotator = new YjpAllAnnotator();
		System.err.println("Finished : annotation");
		annotator.annotate(doc); // throws Exception
		for (Keyword kwd : doc.getKeywords()) {
			System.err.println(kwd);
		}
		System.err.println("名詞...動詞");
		for (Keyword kwd : doc.getKeywords("名詞...動詞")) {
			System.err.println(kwd.getFacet() + ":" + kwd.getLex());
		}
	}

	public void testAnnotateDocument103() throws Exception {
		// 否定(3)
		String text = "あの光景は美しくない。";
		Document doc = new DefaultDocument();
		doc.setText(text);
		DocumentAnnotator annotator = new YjpAllAnnotator();
		System.err.println("Finished : annotation");
		annotator.annotate(doc); // throws Exception
		for (Keyword kwd : doc.getKeywords()) {
			System.err.println(kwd);
		}
		System.err.println("名詞...動詞");
		for (Keyword kwd : doc.getKeywords("名詞...動詞")) {
			System.err.println(kwd.getFacet() + ":" + kwd.getLex());
		}
		System.err.println("名詞...形容詞");
		for (Keyword kwd : doc.getKeywords("名詞...形容詞")) {
			System.err.println(kwd.getFacet() + ":" + kwd.getLex());
		}
	}

	public void testAnnotateDocument104() throws Exception {
		// 形容詞の一部が「ない」
		String text = "あのゲームはつまらない。";
		Document doc = new DefaultDocument();
		doc.setText(text);
		DocumentAnnotator annotator = new YjpAllAnnotator();
		System.err.println("Finished : annotation");
		annotator.annotate(doc); // throws Exception
		for (Keyword kwd : doc.getKeywords()) {
			System.err.println(kwd);
		}
		System.err.println("名詞...動詞");
		for (Keyword kwd : doc.getKeywords("名詞...動詞")) {
			System.err.println(kwd.getFacet() + ":" + kwd.getLex());
		}
		System.err.println("名詞...形容詞");
		for (Keyword kwd : doc.getKeywords("名詞...形容詞")) {
			System.err.println(kwd.getFacet() + ":" + kwd.getLex());
		}
	}

	public void testAnnotateDocument105() throws Exception {
		// 係り受けが離れていても認識するか
		String text = "あのゲームはとても面白くない。";
		Document doc = new DefaultDocument();
		doc.setText(text);
		DocumentAnnotator annotator = new YjpAllAnnotator();
		System.err.println("Finished : annotation");
		annotator.annotate(doc); // throws Exception
		for (Keyword kwd : doc.getKeywords()) {
			System.err.println(kwd);
		}
		System.err.println("名詞...動詞");
		for (Keyword kwd : doc.getKeywords("名詞...動詞")) {
			System.err.println(kwd.getFacet() + ":" + kwd.getLex());
		}
		System.err.println("名詞...形容詞");
		for (Keyword kwd : doc.getKeywords("名詞...形容詞")) {
			System.err.println(kwd.getFacet() + ":" + kwd.getLex());
		}
		System.err.println("形容詞");
		for (Keyword kwd : doc.getKeywords("形容詞")) {
			System.err.println(kwd.getFacet() + ":" + kwd.getLex() + ":" + kwd.getStr());
		}
	}

	public void testAnnotateDocument500() throws Exception {
		// 自然文のテキスト
		String text = "設備は新しく、スタッフは親切でした。";
		// 構文解析の形態素解析が以下のように間違えて返すケースがある
		// 設備(名詞)は(助詞)新(接頭辞)しく(動詞)、
		Document doc = new DefaultDocument();
		doc.putAttribute("description", text);
		DocumentAnnotator annotator = new YjpAllAnnotator();
		System.err.println("Finished : annotation");
		annotator.annotate(doc); // throws Exception
		for (Keyword kwd : doc.getKeywords()) {
			System.err.println(kwd);
		}
		System.err.println("名詞...動詞");
		for (Keyword kwd : doc.getKeywords("名詞...動詞")) {
			System.err.println(kwd);
		}
	}

	public void testAnnotateListOfDocument() {
	}

}
