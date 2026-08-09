package nlp4j.opennlp;

import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nlp4j.AbstractDocumentAnnotator;
import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.impl.DefaultKeyword;
import opennlp.tools.lemmatizer.LemmatizerME;
import opennlp.tools.lemmatizer.LemmatizerModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;

/**
 * Document annotator for English text using Apache OpenNLP.
 * <p>
 * This annotator performs tokenization, part-of-speech (POS) tagging, and
 * lemmatization on text fields in an NLP4J {@link Document}. The analysis
 * results are added to the document as keywords.
 * </p>
 *
 * <p>
 * For each token, the following information is recorded:
 * </p>
 * <ul>
 * <li>surface form ({@code str})</li>
 * <li>lemma ({@code lex})</li>
 * <li>Universal POS tag ({@code upos})</li>
 * <li>annotator-specific POS value ({@code facet})</li>
 * <li>begin and end character offsets</li>
 * </ul>
 *
 * <p>
 * By default, the {@code text} attribute of the document is analyzed. The
 * OpenNLP tokenizer, POS tagger, and lemmatizer models are loaded from
 * resources on the classpath when this annotator is instantiated.
 * </p>
 *
 * <p>
 * Example:
 * </p>
 *
 * <pre>{@code
 * Document doc = new DefaultDocument("Dogs are running quickly.");
 *
 * OpenNLPAnnotator annotator = new OpenNLPAnnotator();
 * annotator.annotate(doc);
 * }</pre>
 *
 * @see DocumentAnnotator
 * @see AbstractDocumentAnnotator
 */
public class OpenNLPAnnotator extends AbstractDocumentAnnotator implements DocumentAnnotator {

	private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	private static final String MODEL_TOKENIZER = "/opennlp-en-ud-ewt-tokens-1.3-2.5.4.bin";
	private static final String MODEL_LEMMAS = "/opennlp-en-ud-ewt-lemmas-1.3-2.5.4.bin";
	private static final String MODEL_POS = "/opennlp-en-ud-ewt-pos-1.3-2.5.4.bin";

	private final TokenizerME tokenizer;
	private final POSTaggerME posTagger;
	private final LemmatizerME lemmatizer;

	public OpenNLPAnnotator() {
		{
			super.targets.add("text");
		}
		try (InputStream in = getModelResource(MODEL_TOKENIZER)) {
			tokenizer = new TokenizerME(new TokenizerModel(in));
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		try (InputStream in = getModelResource(MODEL_POS)) {
			posTagger = new POSTaggerME(new POSModel(in));
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		try (InputStream in = getModelResource(MODEL_LEMMAS)) {
			lemmatizer = new LemmatizerME(new LemmatizerModel(in));
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
	}

	@Override
	public void annotate(Document doc) throws Exception {

		for (String target : super.targets) {
			Object obj = doc.getAttribute(target);
			if (obj == null || obj instanceof String == false) {
				continue;
			}

			String text = (String) obj;

			{
				// -------------------------
				// Tokenize
				// -------------------------
				Span[] spans = tokenizer.tokenizePos(text);

				String[] tokens = new String[spans.length];
				int[] starts = new int[spans.length];
				int[] ends = new int[spans.length];

				for (int i = 0; i < spans.length; i++) {
					starts[i] = spans[i].getStart();
					ends[i] = spans[i].getEnd();
					tokens[i] = text.substring(spans[i].getStart(), spans[i].getEnd());
				}

				// -------------------------
				// POS tagging
				// -------------------------

				String[] posTags = posTagger.tag(tokens);

				// -------------------------
				// Lemmatization
				// -------------------------

				String[] lemmas = lemmatizer.lemmatize(tokens, posTags);

				// -------------------------
				// Output
				// -------------------------

//				System.out.printf("%-15s %-10s %-15s%n", "TOKEN", "POS", "LEMMA");

				for (int i = 0; i < tokens.length; i++) {

//					System.out.printf("%-15s %-10s %-15s%n", tokens[i], posTags[i], lemmas[i]);

					{
						DefaultKeyword kwd = new DefaultKeyword();
						{
							kwd.setLex(lemmas[i]);
							kwd.setStr(tokens[i]);
							kwd.setUPos(posTags[i]);
							kwd.setBegin(starts[i]);
							kwd.setEnd(ends[i]);
							kwd.setFacet(posTags[i]);
						}
						doc.addKeyword(kwd);
					}

				} // END OF for each token
			} // END OF tokenize
		} // END OF for each target
	} // END OF annotate(Document doc)

	private static InputStream getModelResource(String name) {
		InputStream in = OpenNLPAnnotator.class.getResourceAsStream(name);
		if (in == null) {
			throw new IllegalStateException("OpenNLP model resource not found: " + name);
		}
		return in;
	}
}
