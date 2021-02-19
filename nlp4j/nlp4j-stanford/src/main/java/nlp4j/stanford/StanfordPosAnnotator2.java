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
 * $ cd /usr/local/stanford-corenlp-full-2018-10-05/ 
 * $ java -mx4g -cp "*" edu.stanford.nlp.pipeline.StanfordCoreNLPServer -port 9000 -timeout 15000
 * </pre>
 * 
 * @author Hiroki Oya
 *
 */
public class StanfordPosAnnotator2 extends AbstractStanfordAnnotator implements DocumentAnnotator {

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
	static public final String CORENLP_ANNOTATORS_PROPERTY = "tokenize,ssplit,pos,lemma,depparse";

//	static final String wordConnector = "-";

//	private String mode;

	/**
	 * 
	 * <pre>
	 * Stanford Dependency
	 * https://nlp.stanford.edu/software/stanford-dependencies.html
	 * </pre>
	 */
	public StanfordPosAnnotator2() {
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

		int sentenceCount = processAnnotation(doc, text);

		doc.putAttribute("item.dependency.size", "" + sentenceCount);

	}

	/**
	 * このAnnotatorが付けるAnnotation
	 * 
	 * <pre>
	 * word.[POS]
	 * word.nsubj
	 * word.compound
	 * item.dependencyxml 全文に対するAnnotation mode=all の時にセットする [20191108]
	 * item.dependency.size 文の数
	 * item.dependencyN_html
	 * item.dependencyN_xml Nは数値.各文に対するAnnotation
	 * </pre>
	 * 
	 */
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

//	@Override
//	public void setProperty(String key, String value) {
//
//		super.setProperty(key, value);
//
//		if ("annotators".equals(key)) {
//			Properties properties = new Properties();
//			properties.setProperty(key, value);
//			this.coreNLP = new StanfordCoreNLP(properties);
//		} //
//			// 20200110 else if ("target".equals(key)) {
//			// 20200110 this.target = value;
//			// 20200110 } //
//		else if ("mode".equals(key)) {
//			this.mode = value;
//		}
//	}

}
