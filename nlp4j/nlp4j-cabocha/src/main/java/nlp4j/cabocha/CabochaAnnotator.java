package nlp4j.cabocha;

import java.util.ArrayList;

import nlp4j.AbstractDocumentAnnotator;
import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.Keyword;
import nlp4j.annotator.DependencyAnnotator;
import nlp4j.impl.DefaultNlpServiceResponse;

/**
 * @author Hiroki Oya
 * @created_at 2021-05-05
 * @since 1.0.0.0
 */
public class CabochaAnnotator extends AbstractDocumentAnnotator implements DocumentAnnotator, DependencyAnnotator {

	private static final String JA_SENTENCE_SPLITTER = "。";
	CabochaNlpService service = new CabochaNlpService();

	@Override
	public void annotate(Document doc) throws Exception {
		for (String target : super.targets) {
			String text = doc.getAttributeAsString(target);
			int baseBegin = 0;

			boolean endsWidhSplitter = text.endsWith(JA_SENTENCE_SPLITTER);
			String[] tt = text.split(JA_SENTENCE_SPLITTER);
			for (int n = 0; n < tt.length; n++) {
				String txt = tt[n];
				if (n < (tt.length - 1)) {
					txt = txt + JA_SENTENCE_SPLITTER;
				}
				if (n == (tt.length - 1) && endsWidhSplitter) {
					txt = txt + JA_SENTENCE_SPLITTER;
				}
				DefaultNlpServiceResponse response = service.process(txt);
				ArrayList<Keyword> kwds = response.getKeywords();
				for (int x = 0; x < kwds.size(); x++) {
					kwds.get(x).addBeginEnd(baseBegin);
				}
				doc.addKeywords(kwds);
				baseBegin += txt.length();
			}

		}
	}

	@Override
	public void setProperty(String key, String value) {
		super.setProperty(key, value);
		service.setProperty("tempDir", super.prop.getProperty("tempDir", System.getProperty("java.io.tmpdir")));
		service.setProperty("encoding", super.prop.getProperty("encoding", "MS932"));
	}

}
