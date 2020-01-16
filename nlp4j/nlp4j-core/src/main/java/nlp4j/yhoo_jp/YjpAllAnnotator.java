package nlp4j.yhoo_jp;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nlp4j.AbstractDocumentAnnotator;
import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.Keyword;
import nlp4j.KeywordWithDependency;
import nlp4j.impl.DefaultKeyword;

/**
 * 形態素解析結果と構文解析結果をマージする<br>
 * Merge a Morphological analysis response and a Dependency analysis response.
 * 
 * @author Hiroki Oya
 * @since 1.0
 *
 */
public class YjpAllAnnotator extends AbstractDocumentAnnotator implements DocumentAnnotator {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	// 係り受け解析
	YJpDaService service1 = new YJpDaService();

	// 日本語形態素解析
	YJpMaService service2 = new YJpMaService();

	@Override
	public void annotate(Document doc) throws IOException {

		try {
			// 自然文のテキスト
			String text = doc.getText();

			// 係り受け解析の結果を取得する
			ArrayList<KeywordWithDependency> kwdsDa = service1.getKeywords(text); // throws IOException
			// 係り受け解析の結果を出力する
//			for (KeywordWithDependency kw : kwdsDa) {
//				System.err.println(kw.toStringAsXml());
//			}

			// 形態素解析の結果を取得する
			ArrayList<Keyword> kwdsMa = service2.getKeywords(text);
			// すべてのキーワードを出力する
			for (Keyword kwd : kwdsMa) {
				doc.addKeyword(kwd);
			}

			// 構文解析の結果には正規形がセットされていないので形態素解析の結果から補う
			for (int n = 0; n < kwdsDa.size(); n++) {
				KeywordWithDependency kw = kwdsDa.get(n);
				for (KeywordWithDependency k : kw.asList()) {
					// 構文解析の単語の切れ方と、形態素解析の単語の切れ方が違うことがあるので妥協
					for (Keyword kk : kwdsMa) {
						if (kk.getStr().equals(k.getStr())) {
							k.setLex(kk.getLex());
						}
					}
					// 形態素解析の結果と構文解析の切れ方が異なる場合
					if (k.getLex() == null) {
						k.setLex(k.getStr());
					}
				}
			}

			for (KeywordWithDependency kw : kwdsDa) {
				logger.debug("\n" + kw.toStringAsXml());
			}

			// 結果をマージする
			for (KeywordWithDependency root : kwdsDa) {
				// FOR EACH Keyword
				for (KeywordWithDependency kw1 : root.asList()) {

					int sq1 = kw1.getSequence();
					String lex1 = kw1.getLex();
					String facet1 = kw1.getFacet();

					// この後ろに来る語にはあまり意味がないと思われるのでスキップ
					if (facet1.equals("助詞") || facet1.equals("助動詞") || facet1.equals("特殊")) {
						continue;
					}

					int depth1 = kw1.getDepth();
					for (int n = 1; n <= depth1; n++) {
						KeywordWithDependency kw2 = kw1.getParent(n);
						int sq2 = kw2.getSequence();
						String lex2 = kw2.getLex();
						String str2 = kw2.getStr();
						String facet2 = kw2.getFacet();

						String lex = lex1 + "..." + lex2;
						String facet = facet1 + "..." + facet2;

						KeywordWithDependency kw3 = kw1.getParent(n + 1);
						KeywordWithDependency kw4 = kw1.getParent(n + 2);

						// 先頭について
						if ((kw1.getFacet().equals("形容詞") || kw1.getFacet().equals("動詞")) //
								&& kw2 != null && kw2.getStr().equals("ない")) {
							DefaultKeyword k = new DefaultKeyword();
							k.setFacet(kw1.getFacet());
							k.setStr(kw1.getStr() + kw2.getStr());
							k.setLex(kw1.getLex() + "..." + kw2.getLex());
							doc.addKeyword(k);
						}

						if (facet2.equals("形容詞") || facet2.equals("動詞")) {
							// 否定されている
							if (kw3 != null && kw3.getFacet().equals("助動詞") && kw3.getLex().equals("ない")) {
								Keyword k = new DefaultKeyword();
								k.setLex(lex1 + "..." + str2 + kw3.getStr());
								k.setFacet(facet);
								doc.addKeyword(k);
							} //
							else if (kw4 != null && kw4.getFacet().equals("助動詞") && kw4.getLex().equals("ない")) {
								Keyword k = new DefaultKeyword();
								k.setLex(lex1 + "..." + str2 + kw3.getStr() + kw4.getStr());
								k.setFacet(facet);
								doc.addKeyword(k);
							} //
							else if (kw3 != null && kw3.getStr().equals("な") //
									&& kw4 != null && kw4.getStr().equals("い")) {
								Keyword k = new DefaultKeyword();
								k.setLex(lex1 + "..." + str2 + kw3.getStr() + kw4.getStr());
								k.setFacet(facet);
								doc.addKeyword(k);
							} //
							else {
								Keyword k = new DefaultKeyword();
								k.setLex(lex);
								k.setFacet(facet);
								doc.addKeyword(k);
							}
						} //
						else if (facet2.equals("形容動詞")) {
							// 否定されている
							if (kw4 != null && kw4.getFacet().equals("助動詞") && kw4.getLex().equals("ない")) {
								Keyword k = new DefaultKeyword();
								k.setLex(lex1 + "..." + str2 + kw3.getStr() + kw4.getLex());
								k.setFacet(facet);
								doc.addKeyword(k);
							} else {
								Keyword k = new DefaultKeyword();
								k.setLex(lex);
								k.setFacet(facet);
								doc.addKeyword(k);
							}
						} //
							// 清潔...だ...ない
						else if (facet1.equals("形容動詞")) {
							// 否定されている
							if (kw2 != null && facet2 != null && facet2.equals("助詞") && kw4 != null
									&& kw4.getLex().equals("ない")) {
								Keyword k = new DefaultKeyword();
								k.setLex(lex1 + "..." + str2 + kw3.getStr() + kw4.getLex());
								k.setFacet(facet);
								doc.addKeyword(k);
							} else {
								Keyword k = new DefaultKeyword();
								k.setLex(lex);
								k.setFacet(facet);
								doc.addKeyword(k);
							}
						} //
						else if (facet2.equals("助詞")) {
							if ((sq1 + 1) == sq2) {
								Keyword k = new DefaultKeyword();
								k.setLex(lex);
								k.setFacet(facet);
								doc.addKeyword(k);
							} else {
//								System.err.println("XXX");
							}
						} //

						else {
							if (facet1.equals("助詞") == false) {
								Keyword k = new DefaultKeyword();
								k.setLex(lex);
								k.setFacet(facet);
								doc.addKeyword(k);
							} else {
								//
							}
						}

					}

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new IOException(e);
		}

	}

}
