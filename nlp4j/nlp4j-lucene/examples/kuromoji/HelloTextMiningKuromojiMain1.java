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
public class HelloTextMiningKuromojiMain1 {

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
		doc.setText("今日はいい天気です。");

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

	}

}

//Document [{text=今日はいい天気です。}, keywords=[[begin=0,end=2,facet=名詞,upos=NOUN,lex=今日,str=今日], [begin=2,end=3,facet=助詞,upos=ADP,lex=は,str=は], [begin=3,end=5,facet=形容詞,upos=ADJ,lex=いい,str=いい], [begin=5,end=7,facet=名詞,upos=NOUN,lex=天気,str=天気], [beg
//in=7,end=9,facet=助動詞,upos=AUX,lex=です,str=です], [begin=9,end=10,facet=記号,upos=SYM,lex=。,str=。]]]
//{
//  "text": "今日はいい天気です。",
//  "keywords": [
//    {
//      "facet": "名詞",
//      "upos": "NOUN",
//      "lex": "今日",
//      "str": "今日",
//      "begin": 0,
//      "end": 2,
//      "@classname": "nlp4j.impl.DefaultKeyword"
//    },
//    {
//      "facet": "助詞",
//      "upos": "ADP",
//      "lex": "は",
//      "str": "は",
//      "begin": 2,
//      "end": 3,
//      "@classname": "nlp4j.impl.DefaultKeyword"
//    },
//    {
//      "facet": "形容詞",
//      "upos": "ADJ",
//      "lex": "いい",
//      "str": "いい",
//      "begin": 3,
//      "end": 5,
//      "@classname": "nlp4j.impl.DefaultKeyword"
//    },
//    {
//      "facet": "名詞",
//      "upos": "NOUN",
//      "lex": "天気",
//      "str": "天気",
//      "begin": 5,
//      "end": 7,
//      "@classname": "nlp4j.impl.DefaultKeyword"
//    },
//    {
//      "facet": "助動詞",
//      "upos": "AUX",
//      "lex": "です",
//      "str": "です",
//      "begin": 7,
//      "end": 9,
//      "@classname": "nlp4j.impl.DefaultKeyword"
//    },
//    {
//      "facet": "記号",
//      "upos": "SYM",
//      "lex": "。",
//      "str": "。",
//      "begin": 9,
//      "end": 10,
//      "@classname": "nlp4j.impl.DefaultKeyword"
//    }
//  ]
//}

