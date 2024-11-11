package nlp4j.krmj.annotator;

import java.lang.invoke.MethodHandles;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.atilika.kuromoji.ipadic.Token;
import com.atilika.kuromoji.ipadic.Tokenizer;

import nlp4j.AbstractDocumentAnnotator;
import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.impl.DefaultKeyword;

/**
 * Kuromoji Annotator
 * 
 * @author Hiroki Oya
 * @since 1.2
 *
 */
public class KuromojiAnnotator extends AbstractDocumentAnnotator implements DocumentAnnotator {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	@Override
	public void annotate(Document doc) throws Exception {

		logger.info("processing document ... ");
		long time1 = System.currentTimeMillis();

		Tokenizer tokenizer = new Tokenizer();

		for (String target : targets) {
			Object obj = doc.getAttribute(target);
			if (obj == null || obj instanceof String == false) {
				continue;
			}

			String text = (String) obj;

			List<Token> tokens = tokenizer.tokenize(text);

			int sequence = 1;

			for (Token token : tokens) {

				logger.debug(token.getAllFeatures());

				DefaultKeyword kwd = new DefaultKeyword();

				kwd.setLex(token.getBaseForm());
				kwd.setStr(token.getSurface());
				kwd.setReading(token.getReading());

				// 英字
				// @since 1.2.0.1
				if (kwd.getLex().equals("*") && kwd.getReading().equals("*") && kwd.getStr().matches("[a-zA-Z]*")) {
					kwd.setLex(kwd.getStr());
				}

				kwd.setBegin(token.getPosition());
				kwd.setEnd(token.getPosition() + token.getSurface().length());
				kwd.setFacet(token.getPartOfSpeechLevel1());
				kwd.setSequence(sequence);

				doc.addKeyword(kwd);

				sequence++;
			}
		}

		long time2 = System.currentTimeMillis();
		logger.info("processing document ... done " + (time2 - time1));

	}
}
