package nlp4j.indexer.examples;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.Keyword;
import nlp4j.impl.DefaultDocument;
import nlp4j.indexer.SimpleDocumentIndex;
import nlp4j.krmj.annotator.KuromojiAnnotator;
import nlp4j.util.DocumentUtil;

public class SimpleDocumentIndexExample1 {

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
		DocumentAnnotator annotator = new KuromojiAnnotator();
		annotator.setProperty("target", "text");
		{
			// 形態素解析処理
			annotator.annotate(docs);
		}

		System.err.println(DocumentUtil.toJsonPrettyString(docs.get(0)));

		// キーワードインデックス（統計処理）の用意
		SimpleDocumentIndex index = new SimpleDocumentIndex();
		{
			// キーワードインデックス作成処理
			index.addDocuments(docs);
		}

		System.err.println("Document size: " + index.getDocumentSize());

		{ // 名詞
			List<Keyword> kwds = index.getKeywords("名詞");
			// カウントでソート
			kwds.sort(Comparator.comparing(Keyword::getCount).reversed());
			System.out.println("Keywords(名詞)");
			for (Keyword kwd : kwds) {
				System.out.println(
						String.format("corr=%.1f,lex=%s,count=%d", kwd.getCorrelation(), kwd.getLex(), kwd.getCount()));
			}
		}
		{
			// 共起性の高いキーワードの取得
			List<Keyword> kwds = index.getKeywords("名詞", "item=Nissan");
			// 相関値でソート
			kwds.sort(Comparator.comparing(Keyword::getCorrelation).reversed());
			System.out.println("Keywords(名詞) for Nissan");
			for (Keyword kwd : kwds) {
				System.out.println(String.format("%.1f,%s", kwd.getCorrelation(), kwd.getLex()));
			}
		}
		{
			// 共起性の高いキーワードの取得
			List<Keyword> kwds = index.getKeywords("名詞", "item=Toyota");
			// 相関値でソート
			kwds.sort(Comparator.comparing(Keyword::getCorrelation).reversed());
			System.out.println("Keywords(名詞) for Toyota");
			for (Keyword kwd : kwds) {
				System.out.println(String.format("%.1f,%s", kwd.getCorrelation(), kwd.getLex()));
			}
		}
		{
			// 共起性の高いキーワードの取得
			List<Keyword> kwds = index.getKeywords("名詞", "item=Honda");
			// 相関値でソート
			kwds.sort(Comparator.comparing(Keyword::getCorrelation).reversed());
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
