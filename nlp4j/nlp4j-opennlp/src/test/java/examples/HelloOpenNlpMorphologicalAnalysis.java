package examples;

import java.io.InputStream;
import java.util.Objects;

import opennlp.tools.lemmatizer.LemmatizerME;
import opennlp.tools.lemmatizer.LemmatizerModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

public class HelloOpenNlpMorphologicalAnalysis {

	public static void main(String[] args) throws Exception {

		String text = "Dogs are running quickly.";

		// -------------------------
		// Load models
		// -------------------------

		TokenizerModel tokenizerModel;

		try (InputStream in = Objects.requireNonNull(HelloOpenNlpMorphologicalAnalysis.class
				.getResourceAsStream("/opennlp-en-ud-ewt-tokens-1.3-2.5.4.bin"))) {

			tokenizerModel = new TokenizerModel(in);
		}

		POSModel posModel;

		try (InputStream in = Objects.requireNonNull(
				HelloOpenNlpMorphologicalAnalysis.class.getResourceAsStream("/opennlp-en-ud-ewt-pos-1.3-2.5.4.bin"))) {

			posModel = new POSModel(in);
		}

		LemmatizerModel lemmatizerModel;

		try (InputStream in = Objects.requireNonNull(HelloOpenNlpMorphologicalAnalysis.class
				.getResourceAsStream("/opennlp-en-ud-ewt-lemmas-1.3-2.5.4.bin"))) {

			lemmatizerModel = new LemmatizerModel(in);
		}

		// -------------------------
		// Create NLP components
		// -------------------------

		TokenizerME tokenizer = new TokenizerME(tokenizerModel);
		POSTaggerME posTagger = new POSTaggerME(posModel);
		LemmatizerME lemmatizer = new LemmatizerME(lemmatizerModel);

		// -------------------------
		// Tokenize
		// -------------------------

		String[] tokens = tokenizer.tokenize(text);

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

		System.out.printf("%-15s %-10s %-15s%n", "TOKEN", "POS", "LEMMA");

		for (int i = 0; i < tokens.length; i++) {

			System.out.printf("%-15s %-10s %-15s%n", tokens[i], posTags[i], lemmas[i]);
		}
	}
}