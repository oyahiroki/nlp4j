package example;

import nlp4j.Keyword;
import nlp4j.KeywordWithDependency;
import nlp4j.NlpServiceResponse;
import nlp4j.impl.DefaultNlpServiceResponse;
import nlp4j.util.JsonUtils;
import nlp4j.yhoo_jp.YJpDaService;
import nlp4j.yhoo_jp.YJpDaServiceV2;

/**
 * Yahoo! Japan の日本語係り受け解析サービスを利用するサンプルです。<br>
 * Example for Japanese Dependency Analysis with Yahoo! Japan API Service.
 * 
 * <pre>
 * Input: 今日はいい天気です
 * Output:
 * -です
 *     -天気
 *         -は
 *             -今日
 *     -いい
 * </pre>
 * 
 * @author Hiroki Oya
 *
 */
public class YJpDependencyAnalysisV2ExampleMain1 {

	/**
	 * メイン関数です。<br>
	 * Main Method
	 * 
	 * @param args 無し
	 * @throws Exception 実行時の例外
	 */
	public static void main(String[] args) throws Exception {
		// 自然文のテキスト
		String text = "今日は走って学校に行きました。";
		// 係り受け解析
		YJpDaServiceV2 service = new YJpDaServiceV2();
		// 係り受け解析の結果を取得する
		NlpServiceResponse response = service.process(text);
//		System.out.println(response);
//		System.out.println(response.getKeywords());

		// 係り受け解析の結果を出力する
		// <original_response>
		System.err.println("<original_response>");
		System.err.println(JsonUtils.prettyPrint(response.getOriginalResponseBody()));
		System.err.println("</original_response>");
		// </original_response>

		System.err.println("---");
		for (Keyword kwd : response.getKeywords()) {
			if (kwd instanceof KeywordWithDependency) {
				System.err.println(((KeywordWithDependency) kwd).toStringAsXml());
			}
		}
		System.err.println("---");
	}

}
