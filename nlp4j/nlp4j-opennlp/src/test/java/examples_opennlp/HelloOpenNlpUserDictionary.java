package examples_opennlp;

import java.io.InputStream;
import java.util.Objects;

import opennlp.tools.dictionary.Dictionary;
import opennlp.tools.namefind.DictionaryNameFinder;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;
import opennlp.tools.util.StringList;

public class HelloOpenNlpUserDictionary {

	public static void main(String[] args) throws Exception {

		String text = "Nissan released the new Nissan Leaf and Toyota Prius.";

		// -------------------------
		// Load tokenizer model
		// -------------------------

		TokenizerModel tokenizerModel;

		try (InputStream in = Objects.requireNonNull(
				HelloOpenNlpUserDictionary.class.getResourceAsStream("/opennlp-en-ud-ewt-tokens-1.3-2.5.4.bin"))) {

			tokenizerModel = new TokenizerModel(in);
		}

		// -------------------------
		// Create tokenizer
		// -------------------------

		TokenizerME tokenizer = new TokenizerME(tokenizerModel);

		// -------------------------
		// Tokenize
		// -------------------------

		Span[] tokenSpans = tokenizer.tokenizePos(text);

		String[] tokens = new String[tokenSpans.length];

		for (int i = 0; i < tokenSpans.length; i++) {

			Span span = tokenSpans[i];

			tokens[i] = text.substring(span.getStart(), span.getEnd());
		}

		// -------------------------
		// User dictionary:
		// company
		// -------------------------

		boolean caseSensitive = false;
		Dictionary companyDictionary = new Dictionary(caseSensitive);

		companyDictionary.put(new StringList("Nissan"));
		companyDictionary.put(new StringList("Toyota"));

		DictionaryNameFinder companyFinder = new DictionaryNameFinder(companyDictionary, "company");

		// -------------------------
		// User dictionary:
		// vehicle
		// -------------------------

		Dictionary vehicleDictionary = new Dictionary(false);

		vehicleDictionary.put(new StringList("Nissan", "Leaf"));

		vehicleDictionary.put(new StringList("Toyota", "Prius"));

		DictionaryNameFinder vehicleFinder = new DictionaryNameFinder(vehicleDictionary, "vehicle");

		// -------------------------
		// Find dictionary entries
		// -------------------------

		printMatches(text, tokens, tokenSpans, companyFinder);

		printMatches(text, tokens, tokenSpans, vehicleFinder);
	}

	private static void printMatches(String text, String[] tokens, Span[] tokenSpans, DictionaryNameFinder finder) {

		Span[] matches = finder.find(tokens);

		for (Span match : matches) {

			// DictionaryNameFinder returns token offsets.
			int tokenBegin = match.getStart();
			int tokenEnd = match.getEnd();

			// Convert token offsets to character offsets.
			int begin = tokenSpans[tokenBegin].getStart();
			int end = tokenSpans[tokenEnd - 1].getEnd();

			String str = text.substring(begin, end);

			System.out.printf("str=%-20s facet=%-10s begin=%d end=%d%n", str, match.getType(), begin, end);
		}
	}
}