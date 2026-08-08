package example;

import com.google.gson.JsonArray;

import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.DocumentBuilder;
import nlp4j.impl.DefaultDocument;
import nlp4j.krmj.annotator.KuromojiAnnotator;
import nlp4j.util.DocumentUtil;
import nlp4j.util.JsonObjectUtils;
import nlp4j.util.JsonUtils;
import nlp4j.util.KeywordsUtil;

/**
 * 日本語形態素解析とインデックス処理を利用して、共起性の高いキーワードを抽出するサンプルソースコードです。 <br>
 * Sample for Dependency Analysis and Morphological analysis.
 * 
 * @author Hiroki Oya
 *
 */
public class HelloTextMiningKuromojiMain3 {

	/**
	 * メイン関数です。<br>
	 * Main Method
	 * 
	 * @param args 無し
	 * @throws Exception 実行時の例外
	 */
	public static void main(String[] args) throws Exception {

		// ドキュメントの用意（CSVを読み込むなどでも可）
		Document doc = (new DocumentBuilder()).text("今日はいい天気です。").build();

		// 形態素解析アノテーター
		DocumentAnnotator annotator = new KuromojiAnnotator(); // 形態素解析
		annotator.setProperty("target", "text");
		{
			annotator.annotate(doc);
		}
		{
			System.err.println(JsonUtils.prettyPrint(DocumentUtil.toJsonObject(doc)));
		}

		{
			doc.putAttribute("word", KeywordsUtil.toLexList(doc.getKeywords()));
		}
		{
			System.err.println(JsonUtils.prettyPrint(DocumentUtil.toJsonObject(doc)));
		}
		{
			JsonArray arr = JsonObjectUtils.toJsonArray(KeywordsUtil.toLexList(doc.getKeywords()));
			System.err.println(JsonUtils.prettyPrint(arr));
		}

	}

}
//{
//	  "text": "今日はいい天気です。",
//	  "keywords": [
//	    {
//	      "facet": "名詞",
//	      "upos": "NOUN",
//	      "lex": "今日",
//	      "str": "今日",
//	      "begin": 0,
//	      "end": 2,
//	      "@classname": "nlp4j.impl.DefaultKeyword"
//	    },
//	    {
//	      "facet": "助詞",
//	      "upos": "ADP",
//	      "lex": "は",
//	      "str": "は",
//	      "begin": 2,
//	      "end": 3,
//	      "@classname": "nlp4j.impl.DefaultKeyword"
//	    },
//	    {
//	      "facet": "形容詞",
//	      "upos": "ADJ",
//	      "lex": "いい",
//	      "str": "いい",
//	      "begin": 3,
//	      "end": 5,
//	      "@classname": "nlp4j.impl.DefaultKeyword"
//	    },
//	    {
//	      "facet": "名詞",
//	      "upos": "NOUN",
//	      "lex": "天気",
//	      "str": "天気",
//	      "begin": 5,
//	      "end": 7,
//	      "@classname": "nlp4j.impl.DefaultKeyword"
//	    },
//	    {
//	      "facet": "助動詞",
//	      "upos": "AUX",
//	      "lex": "です",
//	      "str": "です",
//	      "begin": 7,
//	      "end": 9,
//	      "@classname": "nlp4j.impl.DefaultKeyword"
//	    },
//	    {
//	      "facet": "記号",
//	      "upos": "SYM",
//	      "lex": "。",
//	      "str": "。",
//	      "begin": 9,
//	      "end": 10,
//	      "@classname": "nlp4j.impl.DefaultKeyword"
//	    }
//	  ]
//	}
//	{
//	  "text": "今日はいい天気です。",
//	  "word": [
//	    "今日",
//	    "は",
//	    "いい",
//	    "天気",
//	    "です",
//	    "。"
//	  ],
//	  "keywords": [
//	    {
//	      "facet": "名詞",
//	      "upos": "NOUN",
//	      "lex": "今日",
//	      "str": "今日",
//	      "begin": 0,
//	      "end": 2,
//	      "@classname": "nlp4j.impl.DefaultKeyword"
//	    },
//	    {
//	      "facet": "助詞",
//	      "upos": "ADP",
//	      "lex": "は",
//	      "str": "は",
//	      "begin": 2,
//	      "end": 3,
//	      "@classname": "nlp4j.impl.DefaultKeyword"
//	    },
//	    {
//	      "facet": "形容詞",
//	      "upos": "ADJ",
//	      "lex": "いい",
//	      "str": "いい",
//	      "begin": 3,
//	      "end": 5,
//	      "@classname": "nlp4j.impl.DefaultKeyword"
//	    },
//	    {
//	      "facet": "名詞",
//	      "upos": "NOUN",
//	      "lex": "天気",
//	      "str": "天気",
//	      "begin": 5,
//	      "end": 7,
//	      "@classname": "nlp4j.impl.DefaultKeyword"
//	    },
//	    {
//	      "facet": "助動詞",
//	      "upos": "AUX",
//	      "lex": "です",
//	      "str": "です",
//	      "begin": 7,
//	      "end": 9,
//	      "@classname": "nlp4j.impl.DefaultKeyword"
//	    },
//	    {
//	      "facet": "記号",
//	      "upos": "SYM",
//	      "lex": "。",
//	      "str": "。",
//	      "begin": 9,
//	      "end": 10,
//	      "@classname": "nlp4j.impl.DefaultKeyword"
//	    }
//	  ]
//	}
//	[
//	  "今日",
//	  "は",
//	  "いい",
//	  "天気",
//	  "です",
//	  "。"
//	]
