package nlp4j.word2vec;

import java.text.Normalizer;
import java.util.ArrayList;

import nlp4j.AbstractDocumentAnnotator;
import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.Keyword;
import nlp4j.impl.DefaultDocument;
import nlp4j.krmj.annotator.KuromojiAnnotator;

public class Word2VecJaAnnotator extends AbstractDocumentAnnotator implements DocumentAnnotator {

	String target = "text";
	String att_to = "text2";

	// 形態素解析アノテーター
	DocumentAnnotator annotator = null;

	@Override
	public void annotate(Document doc) throws Exception {

		if (this.annotator == null) {
			// 形態素解析アノテーター
			this.annotator = new KuromojiAnnotator(); // 形態素解析
			this.annotator.setProperty("target", "text");
		}

		ArrayList<String> ss = new ArrayList<String>();

		String text = doc.getAttributeAsString(this.target);
		text = Normalizer.normalize(text, Normalizer.Form.NFKC);

		// ドキュメントの用意（CSVを読み込むなどでも可）
		Document doc2 = new DefaultDocument();
		doc2.setText(text);

		// 形態素解析アノテーター
//		DocumentAnnotator annotator = new KuromojiAnnotator(); // 形態素解析
//		annotator.setProperty("target", this.target);

		{
			annotator.annotate(doc2);
		}
		{
//			System.err.println(doc2);
//			System.err.println(JsonUtils.prettyPrint(DocumentUtil.toJsonObject(doc2)));
		}

//		ArrayList<String> ss = new ArrayList<>();
		StringBuilder sb = new StringBuilder();

		for (int n = 0; n < doc2.getKeywords().size(); n++) {
			Keyword kwd = doc2.getKeywords().get(n);
			String lex = kwd.getLex();

			if (lex.equals("。") || lex.equals("．")) {
				ss.add(sb.toString());
				sb = new StringBuilder();
			} //
			else if ((n == (doc.getKeywords().size() - 1))) {
				if (sb.length() != 0) {
					sb.append(" ");
				}
				if (lex.equals("*")) {
					sb.append(kwd.getStr());
				} //
				else {
					sb.append(lex);
				}
				ss.add(sb.toString());
				sb = new StringBuilder();
			} //
			else {
				if (sb.length() != 0) {
					sb.append(" ");
				}
				if (lex.equals("*")) {
					sb.append(kwd.getStr());
				} //
				else {
					sb.append(lex);
				}
			}
		}

		doc.putAttribute(att_to, String.join("\n", ss));

//		if (idx > 10) {
//			break;
//		}
//		idx++;

	}

	@Override
	public void setProperty(String key, String value) {

		super.setProperty(key, value);

		if ("target".equals(key)) {
			this.target = value;
		}

	}

}
