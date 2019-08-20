package example;

import java.io.IOException;
import java.util.ArrayList;

import nlp4j.Keyword;
import nlp4j.yhoo_jp.YJpMaService;

/**
 * Yahoo! Japan の日本語形態素解析サービスを利用するサンプルです。
 * 
 * @author Hiroki Oya
 *
 */
public class YJpMorphologicalAnalysisExampleMain {

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
