package example;

import java.io.IOException;

import nlp4j.KeywordWithDependency;
import nlp4j.yhoo_jp.YJpDaService;

/**
 * Yahoo! Japan の日本語係り受け解析サービスを利用するサンプルです。
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
public class YJpDependencyAnalysisExampleMain {

	public static void main(String[] args) throws IOException {
		// 自然文のテキスト
		String text = "私は急いでドアを開ける。";
		// 係り受け解析
		YJpDaService service = new YJpDaService();
		// 係り受け解析の結果を取得する
		KeywordWithDependency kwd = service.getKeywords(text);
		System.err.println(kwd.toString());
		// 係り受け解析の結果を出力する
		System.out.println(kwd.toStringAsDependencyTree());
	}

}
