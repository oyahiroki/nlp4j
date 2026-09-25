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
import opennlp.tools.postag.POSTagFormat;
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

	// Models are shared across all instances (immutable after loading, thread-safe).
	private static final TokenizerModel TOKENIZER_MODEL;
	private static final POSModel POS_MODEL;
	private static final LemmatizerModel LEMMA_MODEL;

	static {
		try (InputStream in = getModelResource(MODEL_TOKENIZER)) {
			TOKENIZER_MODEL = new TokenizerModel(in);
		} catch (IOException e) {
			throw new ExceptionInInitializerError(e);
		}
		try (InputStream in = getModelResource(MODEL_POS)) {
			POS_MODEL = new POSModel(in);
		} catch (IOException e) {
			throw new ExceptionInInitializerError(e);
		}
		try (InputStream in = getModelResource(MODEL_LEMMAS)) {
			LEMMA_MODEL = new LemmatizerModel(in);
		} catch (IOException e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	// *ME instances are NOT thread-safe. Each instance of this annotator owns its
	// own *ME objects. Use one OpenNLPAnnotator per thread (e.g. via ThreadLocal).
	private final TokenizerME tokenizer;
	private final POSTaggerME posTagger;
	private final LemmatizerME lemmatizer;

	public OpenNLPAnnotator() {
		super.targets.add("text");
		tokenizer = new TokenizerME(TOKENIZER_MODEL);
		// POSTagFormat.UD を明示することで、コンストラクタ内部のモデルフォーマット推定処理をスキップする。
		// 使用モデル opennlp-en-ud-ewt-pos は UD フォーマットであるため変換は不要。
		posTagger = new POSTaggerME(POS_MODEL, POSTagFormat.UD);
		lemmatizer = new LemmatizerME(LEMMA_MODEL);
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
				for (int i = 0; i < spans.length; i++) {
					tokens[i] = spans[i].getCoveredText(text).toString();
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

				for (int i = 0; i < tokens.length; i++) {
					DefaultKeyword kwd = new DefaultKeyword();
					kwd.setLex(lemmas[i]);
					kwd.setStr(tokens[i]);
					kwd.setUPos(posTags[i]);
					kwd.setBegin(spans[i].getStart());
					kwd.setEnd(spans[i].getEnd());
					kwd.setFacet(posTags[i]);
					doc.addKeyword(kwd);
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
