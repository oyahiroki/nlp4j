package example;

import java.util.ArrayList;
import java.util.List;

import nlp4j.DocumentAnnotator;
import nlp4j.Document;
import nlp4j.Keyword;
import nlp4j.impl.DefaultDocument;
import nlp4j.indexer.DocumentIndexer;
import nlp4j.indexer.SimpleDocumentIndex;
import nlp4j.yhoo_jp.YJpMaAnnotator;

/**
 * サンプルソースコードです。日本語形態素解析とインデックス処理を利用して、共起性の高いキーワードを抽出します。<br>
 * Example of keyword extraction with Morphological and Dependency Analysis
 * 
 * @author Hiroki Oya
 *
 */
public class HelloTextMiningMain1 {

	/**
	 * メイン関数です。<br>
	 * Main Method
	 * 
	 * @param args 無し
	 * @throws Exception 実行時の例外
	 */
	public static void main(String[] args) throws Exception {

		// ドキュメントの用意（CSVを読み込むなどでも可）
		List<Document> docs = new ArrayList<Document>();
		{
			docs.add(createDocument("Toyota", "ハイブリッドカーを作っています。"));
			docs.add(createDocument("Toyota", "ハイブリッドカーを売っています。"));
			docs.add(createDocument("Toyota", "自動車を作っています。"));
			docs.add(createDocument("Toyota", "自動車を売っています。"));
			docs.add(createDocument("Nissan", "EVを作っています。"));
			docs.add(createDocument("Nissan", "EVを売っています。"));
			docs.add(createDocument("Nissan", "自動車を売っています。"));
			docs.add(createDocument("Nissan", "ルノーと提携しています。"));
			docs.add(createDocument("Nissan", "軽自動車を売っています。"));
			docs.add(createDocument("Honda", "自動車を作っています。"));
			docs.add(createDocument("Honda", "自動車を売っています。"));
			docs.add(createDocument("Honda", "バイクを作っています。"));
			docs.add(createDocument("Honda", "バイクを売っています。"));
			docs.add(createDocument("Honda", "軽自動車を売っています。"));
			docs.add(createDocument("Honda", "軽自動車を作っています。"));
		}

		// 形態素解析アノテーター
		DocumentAnnotator annotator = new YJpMaAnnotator();
		{
			// 形態素解析処理
			annotator.annotate(docs);
		}

		// キーワードインデックス（統計処理）の用意
		DocumentIndexer index = new SimpleDocumentIndex();
		{
			// キーワードインデックス作成処理
			index.addDocuments(docs);
		}

		{
			// 共起性の高いキーワードの取得
			List<Keyword> kwds = index.getKeywords("名詞", "item=Nissan");

			System.out.println("Keywords(名詞) for Nissan");
			for (Keyword kwd : kwds) {
				System.out.println(String.format("%.1f,%s", kwd.getCorrelation(), kwd.getLex()));
			}
		}
		{
			// 共起性の高いキーワードの取得
			List<Keyword> kwds = index.getKeywords("名詞", "item=Toyota");

			System.out.println("Keywords(名詞) for Toyota");
			for (Keyword kwd : kwds) {
				System.out.println(String.format("%.1f,%s", kwd.getCorrelation(), kwd.getLex()));
			}
		}
		{
			// 共起性の高いキーワードの取得
			List<Keyword> kwds = index.getKeywords("名詞", "item=Honda");

			System.out.println("Keywords(名詞) for Honda");
			for (Keyword kwd : kwds) {
				System.out.println(String.format("%.1f,%s", kwd.getCorrelation(), kwd.getLex()));
			}
		}

	}

	static Document createDocument(String item, String text) {
		Document doc = new DefaultDocument();
		doc.putAttribute("item", item);
		doc.setText(text);
		return doc;
	}

}
