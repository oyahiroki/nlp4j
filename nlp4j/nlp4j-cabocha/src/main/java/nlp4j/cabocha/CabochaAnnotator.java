package nlp4j.cabocha;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nlp4j.AbstractDocumentAnnotator;
import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.Keyword;
import nlp4j.annotator.DependencyAnnotator;
import nlp4j.impl.DefaultNlpServiceResponse;
import nlp4j.util.RegexUtils;
import nlp4j.util.StringUtils;

/**
 * @author Hiroki Oya
 * @created_at 2021-05-05
 * @since 1.0.0.0
 */
public class CabochaAnnotator extends AbstractDocumentAnnotator implements DocumentAnnotator, DependencyAnnotator {

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	private static final String JA_SENTENCE_SPLITTER = "。";
	CabochaNlpService service = new CabochaNlpService();

	@Override
	public void annotate(Document doc) throws Exception {

		for (String target : super.targets) {

			String textOrg = doc.getAttributeAsString(target);

			String text = nlp4j.util.StringUtils.filter(textOrg, "MS932");

			text = text.replaceAll(RegexUtils.REGEX_URL, "");
			text = text.replaceAll(RegexUtils.REGEX_HASHTAG, "");

			int baseBegin = 0;

//			boolean endsWidhSplitter = text.endsWith(JA_SENTENCE_SPLITTER);

			String[] lines = text.split("\n");

			for (String line : lines) {

				String[] ss = line.split("。|．");

				for (String s : ss) {

					logger.debug("NLP target: " + s);

					DefaultNlpServiceResponse response = service.process(s);

					ArrayList<Keyword> kwds = response.getKeywords();

					int idx = textOrg.indexOf(s, baseBegin);

					for (int x = 0; x < kwds.size(); x++) {
						kwds.get(x).addBeginEnd(idx);
						kwds.get(x).setNamespace("nlp4j.cabocha");
					}

					doc.addKeywords(kwds);

					baseBegin = (idx + s.length());

				}

			}

//			String[] tt = text.split("。|\\n", 0);
//
//			for (int n = 0; n < tt.length; n++) {
//				String txt = tt[n];
//
//				if (txt.indexOf("\n") != -1) {
//					txt = txt.substring(0, txt.indexOf("\n"));
//				}
//
//				System.err.println(txt.length());
//				System.err.println(txt);
//				if (txt.trim().isEmpty()) {
//					System.err.println("empty");
//					continue;
//				}
//
//				if (n < (tt.length - 1)) {
//					txt = txt + JA_SENTENCE_SPLITTER;
//				}
//				if (n == (tt.length - 1) && endsWidhSplitter) {
//					txt = txt + JA_SENTENCE_SPLITTER;
//				}
//				DefaultNlpServiceResponse response = service.process(txt);
//				ArrayList<Keyword> kwds = response.getKeywords();
//				for (int x = 0; x < kwds.size(); x++) {
//					kwds.get(x).addBeginEnd(baseBegin);
//				}
//				doc.addKeywords(kwds);
//				baseBegin += txt.length();
//			}

		}
	}

	@Override
	public void setProperty(String key, String value) {
		super.setProperty(key, value);
		service.setProperty("tempDir", super.prop.getProperty("tempDir", System.getProperty("java.io.tmpdir")));
		service.setProperty("encoding", super.prop.getProperty("encoding", "MS932"));
	}

}
