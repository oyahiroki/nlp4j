package nlp4j.cotoha;

import nlp4j.AbstractDocumentAnnotator;
import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.impl.DefaultNlpServiceResponse;

/**
 * NTT コミュニケーションズ が提供する日本語形態素解析を利用するアノテーターです。 <br>
 * 
 * @author Hiroki Oya
 * @since 1.0
 *
 */
public class CotohaDaAnnotator extends AbstractDocumentAnnotator implements DocumentAnnotator {

	CotohaNlpService service = new CotohaNlpService();

	@Override
	public void annotate(Document doc) throws Exception {

		if (targets != null && targets.size() > 0) {
			for (String target : targets) {
				String text = doc.getAttribute(target).toString();
				// 形態素解析の結果を取得する
				DefaultNlpServiceResponse response = service.nlpV1Parse(text);
				doc.addKeywords(response.getKeywords());
				
				
				
			}
		}
		// else : default target is 'text'
		else {
			String text = doc.getText();
			// 形態素解析の結果を取得する
			DefaultNlpServiceResponse response = service.nlpV1Parse(text);
			doc.addKeywords(response.getKeywords());
		}

	}

}
