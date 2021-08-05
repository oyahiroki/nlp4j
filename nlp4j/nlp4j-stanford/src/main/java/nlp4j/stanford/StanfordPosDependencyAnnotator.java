package nlp4j.stanford;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.ValueAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.AnnotationPipeline;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.pipeline.StanfordCoreNLPClient;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.BasicDependenciesAnnotation;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.trees.GrammaticalRelation;
import edu.stanford.nlp.util.CoreMap;
import nlp4j.AbstractDocumentAnnotator;
import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.Keyword;
import nlp4j.annotator.DependencyAnnotator;
import nlp4j.impl.DefaultKeyword;

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
 * @author Hiroki Oya
 *
 */
public class StanfordPosDependencyAnnotator extends AbstractDocumentAnnotator
		implements DocumentAnnotator, DependencyAnnotator {

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
	static public final String CORENLP_ANNOTATORS_PROPERTY = "tokenize, ssplit, pos, lemma, depparse";

	static final String wordConnector = "-";

	// Client library instance, or Server instance
	AnnotationPipeline coreNLP;

	// どのフィールドを処理するか、フィールド名を指定
	ArrayList<String> targets = new ArrayList<>();

	private String mode;

	/**
	 * 
	 * <pre>
	 * Stanford Dependency
	 * https://nlp.stanford.edu/software/stanford-dependencies.html
	 * </pre>
	 */
	public StanfordPosDependencyAnnotator() {
		super();
//		Properties properties = new Properties();
//		properties.setProperty("annotators", CORENLP_ANNOTATORS_PROPERTY);
////		properties.put("threads", "1");
//		coreNLP = new StanfordCoreNLP(properties);

		targets = new ArrayList<>();
	}

//	/**
//	 * 008
//	 * 
//	 * @param loadedNLP
//	 */
//	public StanfordPosDependencyAnnotator(AnnotationPipeline loadedNLP) {
//		this.coreNLP = loadedNLP;
//	}

	/**
	 * <pre>
	 * IF (type='server') THEN
	 *   USE StanfordCoreNLPClient (Server) as Client
	 * ELSE
	 *   USE StanfordCoreNLP (Local Java Object) as NLP Library
	 * END IF
	 * </pre>
	 * 
	 * @param properties annotators,server.port,server.threads,server.endpoint
	 */
	public StanfordPosDependencyAnnotator(Properties properties) {
		super();

		this.setProperties(properties);

		Properties pp = new Properties();
		pp.setProperty("annotators", CORENLP_ANNOTATORS_PROPERTY);
//		pp.put("threads", "1");

		if (properties.getProperty("type") != null //
				&& properties.getProperty("type").equals("server")) {
			int port = Integer.parseInt(properties.getProperty("server.port", "9000"));
			int threads = Integer.parseInt(properties.getProperty("server.threads", "2"));
			String endpoint = properties.getProperty("server.endpoint", "http://localhost");
			coreNLP = new StanfordCoreNLPClient(pp, endpoint, port, threads);
		} //
		else {
			coreNLP = new StanfordCoreNLP(pp);
		}
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

		String[] tt = text.split("\n");

		int startIndex = 0;
		int sentenceCount = 0;

		for (int n = 0; n < tt.length; n++) {
			String t = tt[n];

			Annotation annotation = new Annotation(t);
			coreNLP.annotate(annotation);
			{
				List<CoreLabel> labels = annotation.get(TokensAnnotation.class);
				// for each labels
				for (CoreLabel label : labels) { //
					// word.POS
					String str = "" + label.get(TextAnnotation.class);
					String lex = "" + label.get(LemmaAnnotation.class);
					String facet = "word." + label.get(PartOfSpeechAnnotation.class);
					int begin = startIndex + label.beginPosition();
					int end = startIndex + label.endPosition();

					Keyword kwdPOS = new DefaultKeyword("stanford.pos", begin, end, facet, lex, str);

					doc.addKeyword(kwdPOS);

//					System.err.println(kwdPOS);

				} // for each labels
			}

			List<CoreMap> sentenceMap = annotation.get(SentencesAnnotation.class);

			for (CoreMap label : sentenceMap) {

				SemanticGraph graph = label.get(BasicDependenciesAnnotation.class);

				Collection<IndexedWord> roots = graph.getRoots();

				for (IndexedWord root : roots) {
					{
						Set<IndexedWord> used = new HashSet<IndexedWord>();
						annotate(doc, root, graph, 0, used, "root");
					}

					{
						Set<IndexedWord> used = new HashSet<IndexedWord>();

						StringBuilder sbHtml = new StringBuilder();
						SemanticGraphUtils.extractKeywords(doc, root, graph, 0, used, null);

//						System.err.println(sbHtml);

					}
				}
				sentenceCount++;
			} // for each coremap

			startIndex += t.length() + 1;
		} // end of for each t

//		if (mode != null && mode.equals("all")) {
//			doc.putAttribute("item.dependencyxml", sbXml.toString().replace("\n", ""));
//		}

		doc.putAttribute("item.dependency.size", "" + sentenceCount);

	}

	@Override
	public void annotate(Document doc) {

		if (super.targets == null || super.targets.size() == 0) {
			throw new RuntimeException("target is not set");
		}

		if (this.coreNLP == null) {
			Properties pp = new Properties();
			pp.setProperty("annotators", CORENLP_ANNOTATORS_PROPERTY);

			// type=server
			// server.port=9000
			// server.threads=2
			// server.endpoint=http://localhost
			if (super.prop.getProperty("type") != null //
					&& super.prop.getProperty("type").equals("server")) {
				int port = Integer.parseInt(super.prop.getProperty("server.port", "9000"));
				int threads = Integer.parseInt(super.prop.getProperty("server.threads", "2"));
				String endpoint = super.prop.getProperty("server.endpoint", "http://localhost");
				coreNLP = new StanfordCoreNLPClient(pp, endpoint, port, threads);
			} //
			else {
				coreNLP = new StanfordCoreNLP(pp);
			}
		}

		for (String target : super.targets) {
			annotate(doc, target);
		}

	}

	void annotate(Document doc, IndexedWord word, SemanticGraph graph, int depth, Set<IndexedWord> used,
			String relationName) {

		used.add(word);

		if (relationName != null) {

			String posChild = word.getString(PartOfSpeechAnnotation.class);
			IndexedWord wordParent = graph.getParent(word);

			String lemmaChild = word.getString(LemmaAnnotation.class);
			String lemmaParent = null;
			if (wordParent != null) {
				lemmaParent = wordParent.getString(LemmaAnnotation.class);
			}

			if (wordParent != null) {

				String posParent = wordParent.getString(PartOfSpeechAnnotation.class);
				String valueParent = wordParent.getString(ValueAnnotation.class);
				String valueChild = word.get(ValueAnnotation.class);

//				logger.debug("posParent=" + posParent + ",posChild=" + posChild);
//				logger.debug("valueParent=" + valueParent + ",valueChild=" + valueChild);

			}

		}

		List<SemanticGraphEdge> edges = graph.outgoingEdgeList(word);
		{
			Collections.sort(edges);
			for (SemanticGraphEdge edge : edges) {
				IndexedWord targetWord = edge.getTarget();
				GrammaticalRelation relation = edge.getRelation();

				annotate(doc, targetWord, graph, depth + 1, used, relation.getShortName());

				List<SemanticGraphEdge> edges2 = graph.outgoingEdgeList(targetWord);
				for (SemanticGraphEdge e2 : edges2) {
					IndexedWord w2 = e2.getTarget();
					GrammaticalRelation r2 = edge.getRelation();
					if (used.contains(w2) == false) {
						annotate(doc, w2, graph, depth + 1, used, r2.getShortName());
					}
				}

			}
		}

	}

	@Override
	public void setProperty(String key, String value) {

		super.setProperty(key, value);

		if ("annotators".equals(key)) {
			Properties properties = new Properties();
			properties.setProperty(key, value);
			this.coreNLP = new StanfordCoreNLP(properties);
		} //
			// 20200110 else if ("target".equals(key)) {
			// 20200110 this.target = value;
			// 20200110 } //
		else if ("mode".equals(key)) {
			this.mode = value;
		}
	}

}
