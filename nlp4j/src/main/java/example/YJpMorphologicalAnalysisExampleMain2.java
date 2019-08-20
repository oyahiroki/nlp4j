package example;

import java.io.IOException;
import java.util.ArrayList;

import nlp4j.Keyword;
import nlp4j.impl.NlpServiceResponseImpl;
import nlp4j.yhoo_jp.YJpMaService;

/**
 * Yahoo! Japan の日本語形態素解析サービスを利用するサンプルです。
 * 
 * @author Hiroki Oya
 *
 */
public class YJpMorphologicalAnalysisExampleMain2 {

	public static void main(String[] args) throws IOException {
		// 自然文のテキスト
		String text = "今日は走って学校に行きました。";
		// 日本語形態素解析
		YJpMaService service = new YJpMaService();
		// 形態素解析の結果を取得する
		NlpServiceResponseImpl response = service.process(text);
		// すべてのキーワードを出力する
		for (Keyword kwd : response.getKeywords()) {
			System.out.println(kwd);
		}
		// レスポンスのXMLを出力する
		System.out.println(response.getOriginalResponseBody());

	}

}
