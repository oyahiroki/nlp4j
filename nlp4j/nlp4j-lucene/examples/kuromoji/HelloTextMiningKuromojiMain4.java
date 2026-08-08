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
public class HelloTextMiningKuromojiMain4 {

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
		doc.setText("私は歩いて学校に行きました。");

		// 形態素解析アノテーター
		DocumentAnnotator annotator = new KuromojiAnnotator(); // 形態素解析
		annotator.setProperty("target", "text");
		{
			annotator.annotate(doc);
		}

		{
			doc.getKeywords().forEach(kw -> {
				System.out.println(
						kw.getUPos() + "," + kw.getLex() + "," + kw.getStr() + "," + kw.getBegin() + "," + kw.getEnd());
			});
		}

	}

}
//NOUN,私,私,0,1
//ADP,は,は,1,2
//VERB,歩く,歩い,2,4
//ADP,て,て,4,5
//NOUN,学校,学校,5,7
//ADP,に,に,7,8
//VERB,行く,行き,8,10
//AUX,ます,まし,10,12
//AUX,た,た,12,13
//SYM,。,。,13,14
