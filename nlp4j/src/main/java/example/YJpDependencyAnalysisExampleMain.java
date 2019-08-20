package example;

import java.io.IOException;

import nlp4j.KeywordWithDependency;
import nlp4j.yhoo_jp.YJpDaService;

/**
 * <pre>
 * Yahoo! Japan の日本語係り受け解析サービスを利用するサンプルです。
 * </pre>
 * Input:
 * <pre>
 * 今日はいい天気です
 * </pre>
 * Output:
 * <pre>
 * -です
 *     -天気
 *         -は
 *             -今日
 *     -いい
 * </pre>
 * @author Hiroki Oya
 *
 */
public class YJpDependencyAnalysisExampleMain {

	public static void main(String[] args) throws IOException {
		// 自然文のテキスト
		String text = "今日はいい天気です";
		// 係り受け解析
		YJpDaService service = new YJpDaService();
		// 係り受け解析の結果を取得する
		KeywordWithDependency kwd = service.getKeywords(text);
		// 係り受け解析の結果を出力する
		System.out.println(kwd.toStringAsDependencyTree());
	}

}
