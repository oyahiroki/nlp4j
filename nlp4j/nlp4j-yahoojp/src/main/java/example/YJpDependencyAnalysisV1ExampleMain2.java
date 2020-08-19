package example;

import nlp4j.impl.DefaultNlpServiceResponse;
import nlp4j.yhoo_jp.YJpDaService;

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
public class YJpDependencyAnalysisV1ExampleMain2 {

	/**
	 * メイン関数です。<br>
	 * Main Method
	 * 
	 * @param args 無し
	 * @throws Exception 実行時の例外
	 */
	public static void main(String[] args) throws Exception {
		// 自然文のテキスト
		String text = "今日は走って学校に行きました。明日も学校です。";
		// 係り受け解析
		YJpDaService service = new YJpDaService();
		// 係り受け解析の結果を取得する
		DefaultNlpServiceResponse response = service.process(text);
		System.out.println(response);
		System.out.println(response.getKeywords());
		// 係り受け解析の結果を出力する
		System.out.println(response.getOriginalResponseBody());
	}

}
