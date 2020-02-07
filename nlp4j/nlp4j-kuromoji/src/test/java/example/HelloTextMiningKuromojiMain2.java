package example;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import nlp4j.DocumentAnnotator;
import nlp4j.Document;
import nlp4j.Keyword;
import nlp4j.impl.DefaultDocument;
import nlp4j.indexer.DocumentIndexer;
import nlp4j.indexer.SimpleDocumentIndex;
import nlp4j.krmj.annotator.KuromojiAnnotator;
import nlp4j.yhoo_jp.YjpAllAnnotator;

/**
 * 日本語形態素解析とインデックス処理を利用して、共起性の高いキーワードを抽出するサンプルソースコードです。 <br>
 * Sample for Dependency Analysis and Morphological analysis.
 * 
 * @author Hiroki Oya
 *
 */
public class HelloTextMiningKuromojiMain2 {

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
		DocumentAnnotator annotator = new KuromojiAnnotator(); // 形態素解析
		annotator.setProperty("target", "text");
		{
			System.err.println("形態素解析");
			long time1 = System.currentTimeMillis();
			// 形態素解析＋構文解析
			annotator.annotate(docs);
			long time2 = System.currentTimeMillis();
			System.err.println("処理時間[ms]：" + (time2 - time1));
		}
		{
			for (Document doc : docs) {
				System.err.println(doc);
			}
		}

		// キーワードインデックス（統計処理）の用意
		DocumentIndexer index = new SimpleDocumentIndex();
		{
			System.err.println("インデックス作成");
			long time1 = System.currentTimeMillis();
			// キーワードインデックス作成処理
			index.addDocuments(docs);
			long time2 = System.currentTimeMillis();
			System.err.println("処理時間[ms]：" + (time2 - time1));
		}

		{
			// 頻度の高いキーワードの取得
			System.out.println("名詞の頻度順");
			List<Keyword> kwds = index.getKeywords();
			kwds = kwds.stream() //
					.filter(o -> o.getCount() > 1) // 2件以上
					.filter(o -> o.getFacet().equals("名詞")) // 品詞が名詞
					.collect(Collectors.toList());
			for (Keyword kwd : kwds) {
				System.out.println(
						String.format("count=%d,facet=%s,lex=%s", kwd.getCount(), kwd.getFacet(), kwd.getLex()));
			}
		}
		{
			// 共起性の高いキーワードの取得
			List<Keyword> kwds = index.getKeywords("名詞", "item=Nissan");
			System.out.println("名詞 for Nissan");
			for (Keyword kwd : kwds) {
				System.out.println(String.format("count=%d,correlation=%.1f,lex=%s", kwd.getCount(),
						kwd.getCorrelation(), kwd.getLex()));
			}
		}
		{
			// 共起性の高いキーワードの取得
			List<Keyword> kwds = index.getKeywords("名詞", "item=Toyota");
			System.out.println("名詞 for Toyota");
			for (Keyword kwd : kwds) {
				System.out.println(String.format("count=%d,correlation=%.1f,lex=%s", kwd.getCount(),
						kwd.getCorrelation(), kwd.getLex()));
			}
		}
		{
			// 共起性の高いキーワードの取得
			List<Keyword> kwds = index.getKeywords("名詞", "item=Honda");
			System.out.println("名詞 for Honda");
			for (Keyword kwd : kwds) {
				System.out.println(String.format("count=%d,correlation=%.1f,lex=%s", kwd.getCount(),
						kwd.getCorrelation(), kwd.getLex()));
			}
		}
		{
//			// 共起性の高いキーワードの取得
//			List<Keyword> kwds = index.getKeywords("名詞...動詞", "item=Nissan");
//			System.out.println("名詞...動詞 for Nissan");
//			for (Keyword kwd : kwds) {
//				System.out.println(String.format("count=%d,correlation=%.1f,lex=%s", kwd.getCount(),
//						kwd.getCorrelation(), kwd.getLex()));
//			}
		}
	}

	static Document createDocument(String item, String text) {
		Document doc = new DefaultDocument();
		doc.putAttribute("item", item);
		doc.setText(text);
		return doc;
	}

}
