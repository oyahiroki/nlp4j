package example;

import nlp4j.Keyword;
import nlp4j.NlpServiceResponse;
import nlp4j.yhoo_jp.YJpMaServiceV2;

/**
 * Yahoo! Japan の日本語形態素解析サービスを利用するサンプルです。<br>
 * Example for Japanese Language Morphological Analysis with Yahoo! Japan API
 * Service.
 * 
 * <pre>
Input:
日産自動車はEVを作ります。
Ouput:
日産自動車 [sequence=1, facet=名詞, lex=日産自動車, str=日産自動車, reading=にっさんじどうしゃ, count=-1, begin=0, end=5, correlation=0.0]
は [sequence=2, facet=助詞, lex=は, str=は, reading=は, count=-1, begin=5, end=6, correlation=0.0]
EV [sequence=3, facet=名詞, lex=EV, str=EV, reading=EV, count=-1, begin=6, end=8, correlation=0.0]
を [sequence=4, facet=助詞, lex=を, str=を, reading=を, count=-1, begin=8, end=9, correlation=0.0]
作る [sequence=5, facet=動詞, lex=作る, str=作り, reading=つくり, count=-1, begin=9, end=11, correlation=0.0]
ます [sequence=6, facet=助動詞, lex=ます, str=ます, reading=ます, count=-1, begin=11, end=13, correlation=0.0]
。 [sequence=7, facet=特殊, lex=。, str=。, reading=。, count=-1, begin=13, end=14, correlation=0.0]
 * </pre>
 * 
 * @author Hiroki Oya
 *
 */
public class YJpMorphologicalAnalysisExampleMain2 {

	/**
	 * メイン関数です。<br>
	 * Main Method
	 * 
	 * @param args 無し
	 * @throws Exception 実行時の例外
	 */
	public static void main(String[] args) throws Exception {
		// 自然文のテキスト
		String text = "日産自動車はEVを作ります。";
		// 日本語形態素解析
		YJpMaServiceV2 service = new YJpMaServiceV2();
		// 形態素解析の結果を取得する
		NlpServiceResponse response = service.process(text);
		// すべてのキーワードを出力する
		for (Keyword kwd : response.getKeywords()) {
			System.out.println(kwd);
		}
		// レスポンスのXMLを出力する
		System.out.println(response.getOriginalResponseBody());

	}

}
