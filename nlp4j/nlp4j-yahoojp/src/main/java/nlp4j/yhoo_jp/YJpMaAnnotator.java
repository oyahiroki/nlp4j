package nlp4j.yhoo_jp;

import java.io.IOException;
import java.util.ArrayList;

import nlp4j.AbstractDocumentAnnotator;
import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.Keyword;

/**
 * Yahoo! Japan が提供する日本語形態素解析を利用するアノテーターです。 <br>
 * Document annotator for Yahoo! Japan Morphological analysis
 * 
 * @author Hiroki Oya
 * @since 1.0
 *
 */
public class YJpMaAnnotator extends AbstractDocumentAnnotator implements DocumentAnnotator {

	// 日本語形態素解析
	YJpMaService service = new YJpMaService();

	@Override
	public void annotate(Document doc) throws IOException {

		if (targets != null && targets.size() > 0) {
			for (String target : targets) {
				String text = doc.getAttribute(target).toString();
				// 形態素解析の結果を取得する
				ArrayList<Keyword> kwds = service.getKeywords(text);
				doc.setKeywords(kwds);
			}
		}
		// else : default target is 'text'
		else {
			String text = doc.getText();
			// 形態素解析の結果を取得する
			ArrayList<Keyword> kwds = service.getKeywords(text);
			doc.setKeywords(kwds);
		}
	}

}
