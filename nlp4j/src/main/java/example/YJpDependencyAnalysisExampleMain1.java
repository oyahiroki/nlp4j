package example;

import java.io.IOException;
import java.util.ArrayList;

import nlp4j.Keyword;
import nlp4j.KeywordWithDependency;
import nlp4j.yhoo_jp.YJpDaService;

/**
 * Yahoo! Japan の日本語係り受け解析サービスを利用するサンプルです。<br/>
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
public class YJpDependencyAnalysisExampleMain1 {

	public static void main(String[] args) throws IOException {
		// 自然文のテキスト
		String text = "私は急いでドアを開けた。";
		// 係り受け解析
		YJpDaService service = new YJpDaService();
		// 係り受け解析の結果を取得する
		ArrayList<KeywordWithDependency> kwds = service.getKeywords(text);
		System.err.println(kwds.toString());
		// 係り受け解析の結果を出力する
		for (Keyword kwd : kwds) {
			System.out.println(kwd);
		}
	}

}
