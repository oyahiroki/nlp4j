package nlp4j.word2vec;

import java.text.Normalizer;
import java.util.ArrayList;

import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.Keyword;
import nlp4j.impl.DefaultDocument;
import nlp4j.krmj.annotator.KuromojiAnnotator;
import nlp4j.util.DocumentUtil;
import nlp4j.util.JsonUtils;

public class HelloWord2VecJa0a {

	public static void main(String[] args) throws Exception {

		String text = "今日はいい天気です。明日はいい天気です。今日は走って学校に行きました。私は元気";

		text = Normalizer.normalize(text, Normalizer.Form.NFKC);

		// ドキュメントの用意（CSVを読み込むなどでも可）
		Document doc = new DefaultDocument();
		doc.setText(text);

		// 形態素解析アノテーター
		DocumentAnnotator annotator = new KuromojiAnnotator(); // 形態素解析
		annotator.setProperty("target", "text");
		{
			annotator.annotate(doc);
		}
		{
			System.err.println(doc);
			System.err.println(JsonUtils.prettyPrint(DocumentUtil.toJsonObject(doc)));
		}

		ArrayList<String> ss = new ArrayList<>();
		StringBuilder sb = new StringBuilder();

		for (int n = 0; n < doc.getKeywords().size(); n++) {
			Keyword kwd = doc.getKeywords().get(n);

			if (kwd.getLex().equals("。") || kwd.getLex().equals("．")) {
				ss.add(sb.toString());
				sb = new StringBuilder();
			} //
			else if ((n == (doc.getKeywords().size() - 1))) {
				if (sb.length() != 0) {
					sb.append(" ");
				}
				sb.append(kwd.getLex());
				ss.add(sb.toString());
				sb = new StringBuilder();
			} //
			else {
				if (sb.length() != 0) {
					sb.append(" ");
				}
				sb.append(kwd.getLex());
			}
		}

		for (String s : ss) {
			System.err.println(s);
		}

	}

}
