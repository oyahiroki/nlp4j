package nlp4j.yhoo_jp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nlp4j.Annotator;
import nlp4j.Document;
import nlp4j.Keyword;

public class YJpMaAnnotator implements Annotator {

	// 日本語形態素解析
	YJpMaService service = new YJpMaService();

	@Override
	public void annotate(Document doc) throws IOException {
		String text = doc.getText();
		// 形態素解析の結果を取得する
		ArrayList<Keyword> kwds = service.getKeywords(text);
		doc.setKeywords(kwds);
	}

	@Override
	public void annotate(List<Document> docs) throws IOException {
		for (Document doc : docs) {
			annotate(doc);
		}
	}

}
