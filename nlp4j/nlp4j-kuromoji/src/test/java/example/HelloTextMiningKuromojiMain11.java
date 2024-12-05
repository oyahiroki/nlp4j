package example;

import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.impl.DefaultDocument;
import nlp4j.krmj.annotator.KuromojiAnnotator;
import nlp4j.util.DocumentUtil;
import nlp4j.util.JsonUtils;

/**
 * 日本語形態素解析とインデックス処理を利用して、共起性の高いキーワードを抽出するサンプルソースコードです。 <br>
 * Sample for Dependency Analysis and Morphological analysis.
 * 
 * @author Hiroki Oya
 *
 */
public class HelloTextMiningKuromojiMain11 {

	/**
	 * メイン関数です。<br>
	 * Main Method
	 * 
	 * @param args 無し
	 * @throws Exception 実行時の例外
	 */
	public static void main(String[] args) throws Exception {

		// ドキュメントの用意（CSVを読み込むなどでも可）
		Document doc = new DefaultDocument();
		doc.setText("今日はいい天気です。日本ＩＢＭ。ドラえもん。");

		// 形態素解析アノテーター
		DocumentAnnotator annotator = new KuromojiAnnotator(); // 形態素解析
		annotator.setProperty("target", "text");
		{
			annotator.annotate(doc);
		}
		{
//			System.err.println(doc);
//			System.err.println(JsonUtils.prettyPrint(DocumentUtil.toJsonObject(doc)));
		}

		doc.getKeywords().forEach(kw -> {
			System.err.println(kw.getFacet() + " " + kw.getLex());
		});

		// Kuromoji は固有名詞の抽出を行わない
//		名詞 今日
//		助詞 は
//		形容詞 いい
//		名詞 天気
//		助動詞 です
//		記号 。
//		名詞 日本ＩＢＭ
//		記号 。
//		名詞 ドラえもん
//		記号 。

	}

}
