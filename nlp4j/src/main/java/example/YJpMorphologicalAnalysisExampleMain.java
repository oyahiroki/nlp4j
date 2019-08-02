package example;

import java.io.IOException;
import java.util.ArrayList;

import nlp4j.Keyword;
import nlp4j.yhoo_jp.YJpMaService;

public class YJpMorphologicalAnalysisExampleMain {

	public static void main(String[] args) throws IOException {
		String text = "今日は走って学校に行きました。";
		YJpMaService service = new YJpMaService();
		ArrayList<Keyword> kwds = service.getKeywords(text);
		for (Keyword kwd : kwds) {
			System.out.println(kwd);
		}

	}

}
