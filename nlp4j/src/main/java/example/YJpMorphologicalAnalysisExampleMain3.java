package example;

import java.io.IOException;
import java.util.ArrayList;

import nlp4j.Keyword;
import nlp4j.yhoo_jp.YJpMaService;

/**
 * Yahoo! Japan の日本語形態素解析サービスを利用するサンプルです。<br>
 * Example for Japanese Language Morphological Analysis with Yahoo! Japan API
 * Service.
 * 
 * <pre>
 * Input:今日は走って学校に行きました。
 * 
 * Output:
Keyword [facet=名詞, lex=今日, str=今日, reading=きょう, begin=0, end=2]
Keyword [facet=助詞, lex=は, str=は, reading=は, begin=2, end=3]
Keyword [facet=動詞, lex=走る, str=走っ, reading=はしっ, begin=3, end=5]
Keyword [facet=助詞, lex=て, str=て, reading=て, begin=5, end=6]
Keyword [facet=名詞, lex=学校, str=学校, reading=がっこう, begin=6, end=8]
Keyword [facet=助詞, lex=に, str=に, reading=に, begin=8, end=9]
Keyword [facet=動詞, lex=行く, str=行き, reading=いき, begin=9, end=11]
Keyword [facet=助動詞, lex=ます, str=まし, reading=まし, begin=11, end=13]
Keyword [facet=助動詞, lex=た, str=た, reading=た, begin=13, end=14]
Keyword [facet=特殊, lex=。, str=。, reading=。, begin=14, end=15]
 * </pre>
 * 
 * @author Hiroki Oya
 *
 */
public class YJpMorphologicalAnalysisExampleMain3 {

	/**
	 * 
	 * @param args 無し
	 * @throws IOException IOでの例外
	 */
	public static void main(String[] args) throws IOException {
		// 自然文のテキスト
		String text = "今日は走って学校に行きました。";
		// 日本語形態素解析
		YJpMaService service = new YJpMaService();
		// 形態素解析の結果を取得する
		ArrayList<Keyword> kwds = service.getKeywords(text);
		// すべてのキーワードを出力する
		for (Keyword kwd : kwds) {
			System.out.println(kwd);
		}

	}

}
