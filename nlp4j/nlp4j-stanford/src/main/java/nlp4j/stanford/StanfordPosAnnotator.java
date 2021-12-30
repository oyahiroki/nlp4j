package nlp4j.stanford;

import nlp4j.Document;
import nlp4j.DocumentAnnotator;

/**
 * <pre>
 * Stanford Dependency
 * https://nlp.stanford.edu/software/stanford-dependencies.html
 * </pre>
 * 
 * <pre>
 * $ java -mx4g -cp "*" edu.stanford.nlp.pipeline.StanfordCoreNLPServer -port 9000 -timeout 15000
 * </pre>
 * 
 * created_at 2021-02-12
 * 
 * @author Hiroki Oya
 *
 */
public class StanfordPosAnnotator extends AbstractStanfordAnnotator implements DocumentAnnotator {

	/**
	 * Property value for Stanford NLP.
	 * 
	 * <pre>
	 * tokenize: Tokenizer
	 * ssplit: Sentence Splitter
	 * pos: Part Of Speech
	 * lemma: Lemmatizer
	 * depparse: Dependency
	 * </pre>
	 */
	static public final String CORENLP_ANNOTATORS_PROPERTY = "tokenize,ssplit,pos,lemma";

	/**
	 * 
	 * <pre>
	 * Stanford Dependency
	 * https://nlp.stanford.edu/software/stanford-dependencies.html
	 * </pre>
	 */
	public StanfordPosAnnotator() {
		super();
	}

	/**
	 * @param doc
	 * @param target
	 */
	private void annotate(Document doc, String target) {

		String text = doc.getAttributeAsString(target);

		if (text == null) {
			return;
		}

		int sentenceCount = super.processAnnotation(doc, text);

		doc.putAttribute("item.dependency.size", "" + sentenceCount);

	}

	@Override
	public void annotate(Document doc) {

		if (super.targets == null || super.targets.size() == 0) {
			throw new RuntimeException("target is not set");
		}

		if (super.coreNLP == null) {
			super.init(CORENLP_ANNOTATORS_PROPERTY);
		}

		for (String target : super.targets) {
			annotate(doc, target);
		}

	}

}
