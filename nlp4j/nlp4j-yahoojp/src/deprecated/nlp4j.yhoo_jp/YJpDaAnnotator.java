package nlp4j.yhoo_jp;

import java.io.IOException;
import java.util.List;

import nlp4j.AbstractDocumentAnnotator;
import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.Keyword;
import nlp4j.NlpServiceResponse;
import nlp4j.annotator.DependencyAnnotator;

/**
 * Yahoo! Japan Developer Network の係り受け解析結果をアノテーションするクラスです。<br>
 * Annotation class for Yahoo! Japan Developer Network Dependency Analysis.
 * 
 * @author Hiroki Oya
 * @since 1.0
 * @deprecated V1が廃止された
 */
public class YJpDaAnnotator extends AbstractDocumentAnnotator implements DocumentAnnotator, DependencyAnnotator {

	// 構文解析サービス
	YJpDaService service = new YJpDaService();

	@Override
	public void annotate(Document doc) throws IOException {
		String text = doc.getText();
		// 形態素解析の結果を取得する
		NlpServiceResponse res = service.process(text);

		List<Keyword> kwds = res.getKeywords();

		doc.addKeywords(kwds);
	}

}
