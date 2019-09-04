package nlp4j.yhoo_jp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nlp4j.Annotator;
import nlp4j.Document;
import nlp4j.Keyword;
import nlp4j.impl.DefaultNlpServiceResponse;

public class YJpDaAnnotator implements Annotator {

	// 構文解析サービス
	YJpDaService service = new YJpDaService();

	@Override
	public void annotate(Document doc) throws IOException {
		String text = doc.getText();
		// 形態素解析の結果を取得する
		DefaultNlpServiceResponse res = service.process(text);

		ArrayList<Keyword> kwds = res.getKeywords();
		doc.addKeywords(kwds);
	}

	@Override
	public void annotate(List<Document> docs) throws IOException {
		for (Document doc : docs) {
			annotate(doc);
		}
	}

}
