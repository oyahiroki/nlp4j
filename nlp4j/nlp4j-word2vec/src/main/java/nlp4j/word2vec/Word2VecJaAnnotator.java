package nlp4j.word2vec;

import java.lang.invoke.MethodHandles;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nlp4j.AbstractDocumentAnnotator;
import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.Keyword;
import nlp4j.impl.DefaultDocument;

/**
 * テキストの形態素を半角区切りにする
 * 
 * <pre>
 * {
 *   "text": "今日はいい天気です。",
 *   "keywords": []
 * }
 * {
 *   "text": "今日はいい天気です。",
 *   "text2": "今日 は いい 天気 です",
 *   "keywords": []
 * }
 * </pre>
 * 
 * @author Hiroki Oya
 * @created_at 2021-04-02
 */
public class Word2VecJaAnnotator extends AbstractDocumentAnnotator implements DocumentAnnotator {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	String target = "text";
	String target2 = "text2";

	// 形態素解析アノテーター
	DocumentAnnotator annotator = null;

	List<String> uposFilter = null;

	@Override
	public void annotate(Document doc) throws Exception {

		ArrayList<String> ss = new ArrayList<String>();

		String text = doc.getAttributeAsString(this.target);
		if (text == null) {
			logger.info("Text is null: " + this.target);
			return;
		}

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

//			System.err.println(this.uposFilter);
//			System.err.println(kwd.getUPos());

			if (this.uposFilter != null && this.uposFilter.contains(kwd.getUPos()) == false) {
//				System.err.println("xx");

				if (n == doc2.getKeywords().size() - 1) {
					ss.add(sb.toString());
					sb = new StringBuilder();
				}

				continue;
			}

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

		doc.putAttribute(target2, String.join("\n", ss));

	}

	@Override
	public void setProperty(String key, String value) {

		super.setProperty(key, value);

		// target
		if ("target".equals(key)) {
			this.target = value;
		} //
			// target2
		else if ("target2".equals(key)) {
			this.target2 = value;
		} //
			// annotator
		else if ("annotator".equals(key)) {
			try {
				this.annotator = (DocumentAnnotator) Class.forName(value).newInstance();
				this.annotator.setProperty("target", "text");
				logger.info("annotator: " + this.annotator.getClass().getCanonicalName());
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw new RuntimeException(e);
			}
		} //
			// upos
		else if ("upos".equals(key)) {
			if (this.uposFilter == null) {
				this.uposFilter = new ArrayList<>();
			}
			this.uposFilter.addAll(Arrays.asList(value.split(",")));
		}

	}

}
