package yhoo_jp;

import java.util.ArrayList;

import junit.framework.TestCase;
import nlp4j.Keyword;
import nlp4j.KeywordWithDependency;
import nlp4j.yhoo_jp.YJpDaAnnotator;
import nlp4j.yhoo_jp.YJpDaService;
import nlp4j.yhoo_jp.YJpMaService;

public class YJpDaAnnotatorTestCase extends TestCase {

	@SuppressWarnings("rawtypes")
	Class target = YJpDaAnnotator.class;

	public void testAnnotateDocument() {
		try {

			// 自然文のテキスト
			String text = "今日は走って学校に行きました。このホテルの設備は良い。あの旅館のお風呂は良くない。私は歩かないです。";

//			text = "行かない";
//
//			text = "お風呂が綺麗でなく、部屋も狭い";
//			text = "お風呂が清潔でない。";
//			text = "お風呂が清潔ではない。";

			text = "お風呂は綺麗で、部屋も広い";

			text = "行けなくなくない";

			// 係り受け解析
			YJpDaService service1 = new YJpDaService();
			// 係り受け解析の結果を取得する
			ArrayList<KeywordWithDependency> kwdsDa = service1.getKeywords(text);
			// 係り受け解析の結果を出力する
			for (KeywordWithDependency kw : kwdsDa) {
				System.err.println(kw.toStringAsXml());
			}

			// 日本語形態素解析
			YJpMaService service2 = new YJpMaService();
			// 形態素解析の結果を取得する
			ArrayList<Keyword> kwdsMa = service2.getKeywords(text);
			// すべてのキーワードを出力する
			for (Keyword kwd : kwdsMa) {
				System.err.println(kwd);
			}

			for (int n = 0; n < kwdsDa.size(); n++) {
				KeywordWithDependency kw = kwdsDa.get(n);
				for (KeywordWithDependency k : kw.asList()) {
					String lex = kwdsMa.get(k.getSequence() - 1).getLex();
					k.setLex(lex);
				}
			}
			System.err.println("変更後");
			for (KeywordWithDependency kw : kwdsDa) {
				System.err.println(kw.toStringAsXml());
			}

			System.err.println("---");

			// 結果をマージする
			for (KeywordWithDependency root : kwdsDa) {
				System.err.println("Root : " + root);
				// FOR EACH Keyword
				for (KeywordWithDependency kw1 : root.asList()) {

					int sq1 = kw1.getSequence();
//					int idxMa = sq1 - 1;
					String lex1 = kw1.getLex();
					String facet1 = kw1.getFacet();

					if (facet1.equals("助詞") || facet1.equals("助動詞") || facet1.equals("特殊")) {
						continue;
					}
					int depth1 = kw1.getDepth();
					for (int n = 1; n <= depth1; n++) {
						KeywordWithDependency kwd2 = kw1.getParent(n);
						int sq2 = kwd2.getSequence();
						String lex2 = kwd2.getLex();
						String str2 = kwd2.getStr();
						String facet2 = kwd2.getFacet();

						String lex = lex1 + "..." + lex2;
						String facet = facet1 + "..." + facet2;

						KeywordWithDependency kwd3 = kw1.getParent(n + 1);
						KeywordWithDependency kwd4 = kw1.getParent(n + 2);

						if (facet2.equals("形容詞") || facet2.equals("動詞")) {
							// 否定されている
							if (kwd3 != null && kwd3.getFacet().equals("助動詞") && kwd3.getLex().equals("ない")) {
								System.err.println(lex1 + "..." + str2 + kwd3.getStr() + "(" + facet + ")");
							} else {
								System.err.println(lex + "(" + facet + ")");
							}
						} //
						else if (facet2.equals("形容動詞")) {
							// 否定されている
							if (kwd4 != null && kwd4.getFacet().equals("助動詞") && kwd4.getLex().equals("ない")) {
								System.err.println(
										lex1 + "..." + str2 + kwd3.getStr() + kwd4.getLex() + "(" + facet + ")");
							} else {
								System.err.println(lex + "(" + facet + ")");
							}
						} //
							// 清潔...だ...ない
						else if (facet1.equals("形容動詞")) {
							// 否定されている
							if (kwd2 != null && facet2 != null && facet2.equals("助詞") && kwd4 != null
									&& kwd4.getLex().equals("ない")) {
								System.err.println(
										lex1 + "..." + str2 + kwd3.getStr() + kwd4.getLex() + "(" + facet + ")");
							} else {
								System.err.println(lex + "(" + facet + ")");
							}
						} //
						else if (facet2.equals("助詞")) {
							if ((sq1 + 1) == sq2) {
								System.err.println(lex + "(" + facet + ")");
							} else {
								System.err.println("XXX");
							}
						} //

						else {
							if (facet1.equals("助詞") == false) {
								System.err.println(lex + "(" + facet + ")");
							} else {
							}
						}

					}

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
