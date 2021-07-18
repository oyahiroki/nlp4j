package nlp4j.stanford;

import java.lang.invoke.MethodHandles;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
import edu.stanford.nlp.util.CoreMap;
import nlp4j.AbstractDocumentAnnotator;
import nlp4j.Document;
import nlp4j.Keyword;
import nlp4j.impl.DefaultKeyword;

/**
 * @author Hiroki Oya
 * @created_at 2021-02-12
 */
public abstract class AbstractStanfordAnnotator extends AbstractDocumentAnnotator {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	// Client library instance, or Server instance
	AnnotationPipeline coreNLP;

	boolean runDepParse = false;

	/**
	 * @param annotators to initialize Stranford NLP
	 */
	public void init(String annotators) {

		if (annotators.contains("depparse")) {
			this.runDepParse = true;
		}

		long time1 = System.currentTimeMillis();
		logger.info("initializing ... ");

		Properties pp = new Properties();
		pp.setProperty("annotators", annotators);

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

		long time2 = System.currentTimeMillis();
		logger.info("initializing ... done " + (time2 - time1));

	}

	public int processAnnotation(Document doc, String text) {

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

					logger.info("kwd: " + kwdPOS);

				} // for each labels
			}

			if (runDepParse == true) {
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

//							StringBuilder sbHtml = new StringBuilder();
							SemanticGraphUtils.extractKeywords(doc, root, graph, 0, used, null);

//							System.err.println(sbHtml);

						}
					}
					sentenceCount++;
				} // for each coremap
			}

			startIndex += t.length() + 1;

		} // end of for each t
		return sentenceCount;
	}

	private void annotate(Document doc, IndexedWord word, SemanticGraph graph, int depth, Set<IndexedWord> used,
			String relationName) {

		used.add(word);

		if (relationName != null) {

//			String posChild = word.getString(PartOfSpeechAnnotation.class);
//			IndexedWord wordParent = graph.getParent(word);

//			String lemmaChild = word.getString(LemmaAnnotation.class);
//			String lemmaParent = null;
//			
//			if (wordParent != null) {
//				lemmaParent = wordParent.getString(LemmaAnnotation.class);
//			}

//			if (wordParent != null) {

//				String posParent = wordParent.getString(PartOfSpeechAnnotation.class);
//				String valueParent = wordParent.getString(ValueAnnotation.class);
//				String valueChild = word.get(ValueAnnotation.class);

//				logger.debug("posParent=" + posParent + ",posChild=" + posChild);
//				logger.debug("valueParent=" + valueParent + ",valueChild=" + valueChild);

//			}

		}

//		List<SemanticGraphEdge> edges = graph.outgoingEdgeList(word);
//		{
//			Collections.sort(edges);
//			for (SemanticGraphEdge edge : edges) {
//				IndexedWord targetWord = edge.getTarget();
//				GrammaticalRelation relation = edge.getRelation();
//
//				annotate(doc, targetWord, graph, depth + 1, used, relation.getShortName());
//
//				List<SemanticGraphEdge> edges2 = graph.outgoingEdgeList(targetWord);
//				for (SemanticGraphEdge e2 : edges2) {
//					IndexedWord w2 = e2.getTarget();
//					GrammaticalRelation r2 = edge.getRelation();
//					if (used.contains(w2) == false) {
//						annotate(doc, w2, graph, depth + 1, used, r2.getShortName());
//					}
//				}
//
//			}
//		}

	}

}