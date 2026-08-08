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
//形態素解析
//処理時間[ms]：1902
//Document [{item=Toyota, text=ハイブリッドカーを作っています。}, keywords=[[begin=0,end=6,facet=名詞,upos=NOUN,lex=ハイブリッド,str=ハイブリッド], [begin=6,end=8,facet=名詞,upos=NOUN,lex=カー,str=カー], [begin=8,end=9,facet=助詞,upos=ADP,lex=を,str=を], [begin=9,end=11,facet=動詞,up
//os=VERB,lex=作る,str=作っ], [begin=11,end=12,facet=助詞,upos=ADP,lex=て,str=て], [begin=12,end=13,facet=動詞,upos=VERB,lex=いる,str=い], [begin=13,end=15,facet=助動詞,upos=AUX,lex=ます,str=ます], [begin=15,end=16,facet=記号,upos=SYM,lex=。,str=。]]]
//Document [{item=Toyota, text=ハイブリッドカーを売っています。}, keywords=[[begin=0,end=6,facet=名詞,upos=NOUN,lex=ハイブリッド,str=ハイブリッド], [begin=6,end=8,facet=名詞,upos=NOUN,lex=カー,str=カー], [begin=8,end=9,facet=助詞,upos=ADP,lex=を,str=を], [begin=9,end=11,facet=動詞,up
//os=VERB,lex=売る,str=売っ], [begin=11,end=12,facet=助詞,upos=ADP,lex=て,str=て], [begin=12,end=13,facet=動詞,upos=VERB,lex=いる,str=い], [begin=13,end=15,facet=助動詞,upos=AUX,lex=ます,str=ます], [begin=15,end=16,facet=記号,upos=SYM,lex=。,str=。]]]
//Document [{item=Toyota, text=自動車を作っています。}, keywords=[[begin=0,end=3,facet=名詞,upos=NOUN,lex=自動車,str=自動車], [begin=3,end=4,facet=助詞,upos=ADP,lex=を,str=を], [begin=4,end=6,facet=動詞,upos=VERB,lex=作る,str=作っ], [begin=6,end=7,facet=助詞,upos=ADP,lex=て
//,str=て], [begin=7,end=8,facet=動詞,upos=VERB,lex=いる,str=い], [begin=8,end=10,facet=助動詞,upos=AUX,lex=ます,str=ます], [begin=10,end=11,facet=記号,upos=SYM,lex=。,str=。]]]
//Document [{item=Toyota, text=自動車を売っています。}, keywords=[[begin=0,end=3,facet=名詞,upos=NOUN,lex=自動車,str=自動車], [begin=3,end=4,facet=助詞,upos=ADP,lex=を,str=を], [begin=4,end=6,facet=動詞,upos=VERB,lex=売る,str=売っ], [begin=6,end=7,facet=助詞,upos=ADP,lex=て
//,str=て], [begin=7,end=8,facet=動詞,upos=VERB,lex=いる,str=い], [begin=8,end=10,facet=助動詞,upos=AUX,lex=ます,str=ます], [begin=10,end=11,facet=記号,upos=SYM,lex=。,str=。]]]
//Document [{item=Nissan, text=EVを作っています。}, keywords=[[begin=0,end=2,facet=名詞,upos=NOUN,lex=EV,str=EV], [begin=2,end=3,facet=助詞,upos=ADP,lex=を,str=を], [begin=3,end=5,facet=動詞,upos=VERB,lex=作る,str=作っ], [begin=5,end=6,facet=助詞,upos=ADP,lex=て,st
//r=て], [begin=6,end=7,facet=動詞,upos=VERB,lex=いる,str=い], [begin=7,end=9,facet=助動詞,upos=AUX,lex=ます,str=ます], [begin=9,end=10,facet=記号,upos=SYM,lex=。,str=。]]]
//Document [{item=Nissan, text=EVを売っています。}, keywords=[[begin=0,end=2,facet=名詞,upos=NOUN,lex=EV,str=EV], [begin=2,end=3,facet=助詞,upos=ADP,lex=を,str=を], [begin=3,end=5,facet=動詞,upos=VERB,lex=売る,str=売っ], [begin=5,end=6,facet=助詞,upos=ADP,lex=て,st
//r=て], [begin=6,end=7,facet=動詞,upos=VERB,lex=いる,str=い], [begin=7,end=9,facet=助動詞,upos=AUX,lex=ます,str=ます], [begin=9,end=10,facet=記号,upos=SYM,lex=。,str=。]]]
//Document [{item=Nissan, text=自動車を売っています。}, keywords=[[begin=0,end=3,facet=名詞,upos=NOUN,lex=自動車,str=自動車], [begin=3,end=4,facet=助詞,upos=ADP,lex=を,str=を], [begin=4,end=6,facet=動詞,upos=VERB,lex=売る,str=売っ], [begin=6,end=7,facet=助詞,upos=ADP,lex=て
//,str=て], [begin=7,end=8,facet=動詞,upos=VERB,lex=いる,str=い], [begin=8,end=10,facet=助動詞,upos=AUX,lex=ます,str=ます], [begin=10,end=11,facet=記号,upos=SYM,lex=。,str=。]]]
//Document [{item=Nissan, text=ルノーと提携しています。}, keywords=[[begin=0,end=3,facet=名詞,upos=NOUN,lex=ルノー,str=ルノー], [begin=3,end=4,facet=助詞,upos=ADP,lex=と,str=と], [begin=4,end=6,facet=名詞,upos=NOUN,lex=提携,str=提携], [begin=6,end=7,facet=動詞,upos=VERB,lex
//=する,str=し], [begin=7,end=8,facet=助詞,upos=ADP,lex=て,str=て], [begin=8,end=9,facet=動詞,upos=VERB,lex=いる,str=い], [begin=9,end=11,facet=助動詞,upos=AUX,lex=ます,str=ます], [begin=11,end=12,facet=記号,upos=SYM,lex=。,str=。]]]
//Document [{item=Nissan, text=軽自動車を売っています。}, keywords=[[begin=0,end=4,facet=名詞,upos=NOUN,lex=軽自動車,str=軽自動車], [begin=4,end=5,facet=助詞,upos=ADP,lex=を,str=を], [begin=5,end=7,facet=動詞,upos=VERB,lex=売る,str=売っ], [begin=7,end=8,facet=助詞,upos=ADP,le
//x=て,str=て], [begin=8,end=9,facet=動詞,upos=VERB,lex=いる,str=い], [begin=9,end=11,facet=助動詞,upos=AUX,lex=ます,str=ます], [begin=11,end=12,facet=記号,upos=SYM,lex=。,str=。]]]
//Document [{item=Honda, text=自動車を作っています。}, keywords=[[begin=0,end=3,facet=名詞,upos=NOUN,lex=自動車,str=自動車], [begin=3,end=4,facet=助詞,upos=ADP,lex=を,str=を], [begin=4,end=6,facet=動詞,upos=VERB,lex=作る,str=作っ], [begin=6,end=7,facet=助詞,upos=ADP,lex=て,
//str=て], [begin=7,end=8,facet=動詞,upos=VERB,lex=いる,str=い], [begin=8,end=10,facet=助動詞,upos=AUX,lex=ます,str=ます], [begin=10,end=11,facet=記号,upos=SYM,lex=。,str=。]]]
//Document [{item=Honda, text=自動車を売っています。}, keywords=[[begin=0,end=3,facet=名詞,upos=NOUN,lex=自動車,str=自動車], [begin=3,end=4,facet=助詞,upos=ADP,lex=を,str=を], [begin=4,end=6,facet=動詞,upos=VERB,lex=売る,str=売っ], [begin=6,end=7,facet=助詞,upos=ADP,lex=て,
//str=て], [begin=7,end=8,facet=動詞,upos=VERB,lex=いる,str=い], [begin=8,end=10,facet=助動詞,upos=AUX,lex=ます,str=ます], [begin=10,end=11,facet=記号,upos=SYM,lex=。,str=。]]]
//Document [{item=Honda, text=バイクを作っています。}, keywords=[[begin=0,end=3,facet=名詞,upos=NOUN,lex=バイク,str=バイク], [begin=3,end=4,facet=助詞,upos=ADP,lex=を,str=を], [begin=4,end=6,facet=動詞,upos=VERB,lex=作る,str=作っ], [begin=6,end=7,facet=助詞,upos=ADP,lex=て,
//str=て], [begin=7,end=8,facet=動詞,upos=VERB,lex=いる,str=い], [begin=8,end=10,facet=助動詞,upos=AUX,lex=ます,str=ます], [begin=10,end=11,facet=記号,upos=SYM,lex=。,str=。]]]
//Document [{item=Honda, text=バイクを売っています。}, keywords=[[begin=0,end=3,facet=名詞,upos=NOUN,lex=バイク,str=バイク], [begin=3,end=4,facet=助詞,upos=ADP,lex=を,str=を], [begin=4,end=6,facet=動詞,upos=VERB,lex=売る,str=売っ], [begin=6,end=7,facet=助詞,upos=ADP,lex=て,
//str=て], [begin=7,end=8,facet=動詞,upos=VERB,lex=いる,str=い], [begin=8,end=10,facet=助動詞,upos=AUX,lex=ます,str=ます], [begin=10,end=11,facet=記号,upos=SYM,lex=。,str=。]]]
//Document [{item=Honda, text=軽自動車を売っています。}, keywords=[[begin=0,end=4,facet=名詞,upos=NOUN,lex=軽自動車,str=軽自動車], [begin=4,end=5,facet=助詞,upos=ADP,lex=を,str=を], [begin=5,end=7,facet=動詞,upos=VERB,lex=売る,str=売っ], [begin=7,end=8,facet=助詞,upos=ADP,lex
//=て,str=て], [begin=8,end=9,facet=動詞,upos=VERB,lex=いる,str=い], [begin=9,end=11,facet=助動詞,upos=AUX,lex=ます,str=ます], [begin=11,end=12,facet=記号,upos=SYM,lex=。,str=。]]]
//Document [{item=Honda, text=軽自動車を作っています。}, keywords=[[begin=0,end=4,facet=名詞,upos=NOUN,lex=軽自動車,str=軽自動車], [begin=4,end=5,facet=助詞,upos=ADP,lex=を,str=を], [begin=5,end=7,facet=動詞,upos=VERB,lex=作る,str=作っ], [begin=7,end=8,facet=助詞,upos=ADP,lex
//=て,str=て], [begin=8,end=9,facet=動詞,upos=VERB,lex=いる,str=い], [begin=9,end=11,facet=助動詞,upos=AUX,lex=ます,str=ます], [begin=11,end=12,facet=記号,upos=SYM,lex=。,str=。]]]
//インデックス作成
//名詞の頻度順
//処理時間[ms]：2
//count=5,facet=名詞,lex=自動車
//count=3,facet=名詞,lex=軽自動車
//count=2,facet=名詞,lex=バイク
//count=2,facet=名詞,lex=EV
//count=2,facet=名詞,lex=カー
//count=2,facet=名詞,lex=ハイブリッド
//名詞 for Nissan
//count=2,correlation=3.0,lex=EV
//count=1,correlation=3.0,lex=ルノー
//count=1,correlation=3.0,lex=提携
//count=1,correlation=1.0,lex=軽自動車
//count=1,correlation=0.6,lex=自動車
//名詞 for Toyota
//count=2,correlation=3.8,lex=ハイブリッド
//count=2,correlation=3.8,lex=カー
//count=2,correlation=1.5,lex=自動車
//名詞 for Honda
//count=2,correlation=2.5,lex=バイク
//count=2,correlation=1.7,lex=軽自動車
//count=2,correlation=1.0,lex=自動車
