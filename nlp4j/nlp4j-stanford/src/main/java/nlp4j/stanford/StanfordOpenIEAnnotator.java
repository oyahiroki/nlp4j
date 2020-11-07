package nlp4j.stanford;

import java.lang.invoke.MethodHandles;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.naturalli.NaturalLogicAnnotations;
import edu.stanford.nlp.naturalli.OpenIE;
import edu.stanford.nlp.naturalli.SentenceFragment;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.PropertiesUtils;
import nlp4j.AbstractDocumentAnnotator;
import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.Keyword;
import nlp4j.impl.DefaultKeyword;

/**
 * pattern.oie_clause<br>
 * pattern.oie_triple<br>
 * pattern.oie_triple_tabseparated<br>
 * 
 * @author Hiroki Oya
 *
 */
public class StanfordOpenIEAnnotator extends AbstractDocumentAnnotator implements DocumentAnnotator {

	/**
	 * pattern.oie_clause
	 */
	public static final String FACET_PATTERN_OIE_CLAUSE = "pattern.oie_clause";

	/**
	 * pattern.oie_triple_tabseparated
	 */
	public static final String FACET_PATTERN_OIE_TRIPLE_TABSEPARATED = "pattern.oie_triple_tabseparated";

	/**
	 * pattern.oie_triple
	 */
	public static final String FACET_PATTERN_OIE_TRIPLE = "pattern.oie_triple";

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	Properties props = PropertiesUtils.asProperties("annotators", "tokenize,ssplit,pos,lemma,depparse,natlog,openie");

	StanfordCoreNLP pipeline;

	OpenIE openie;

	/**
	 * 
	 */
	public StanfordOpenIEAnnotator() {
		super();
		{
			logger.info("initializing core NLP ...");
			pipeline = new StanfordCoreNLP(props);
			logger.info("initializing core NLP done");
		}
		{
			logger.info("initializing OPEN IE ...");
			openie = new OpenIE(props);
			logger.info("initializing OPEN IE done");
		}
	}

	public void annotate(Document doc) {

		for (String target : targets) {

			String text = doc.getAttributeAsString(target);

			Annotation annotation = new Annotation(text);
			pipeline.annotate(annotation);

			// FOR EACH sentence
			for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {

				// Print SemanticGraph : EnhancedDependenciesAnnotation
//				System.err.println("SemanticGraph");
//				System.err.println(sentence.get(SemanticGraphCoreAnnotations.EnhancedDependenciesAnnotation.class)
//						.toString(SemanticGraph.OutputFormat.RECURSIVE));

				// Get the OpenIE triples for the sentence
				Collection<RelationTriple> triples = sentence
						.get(NaturalLogicAnnotations.RelationTriplesAnnotation.class);

				// FOR EACH triples
				for (RelationTriple triple : triples) {
					String lex = triple.subjectGloss() + " , " + triple.relationGloss() + " , " + triple.objectGloss();

					int begin = Integer.MAX_VALUE;
					int end = -1;

					for (CoreLabel label : triple.asSentence()) {
						if (end < label.endPosition()) {
							end = label.endPosition();
						}
						if (begin > label.beginPosition()) {
							begin = label.beginPosition();
						}
					}
					{
						Keyword kwd = new DefaultKeyword();
						kwd.setLex(lex.toLowerCase());
						kwd.setBegin(begin);
						kwd.setEnd(end);
						kwd.setFacet(FACET_PATTERN_OIE_TRIPLE);
						doc.addKeyword(kwd);
					}

				} // END OF FOR EACH triples

				// Alternately, to only run e.g., the clause splitter:
				List<SentenceFragment> clauses = openie.clausesInSentence(sentence);

				// FOR EACH Clause
				for (SentenceFragment clause : clauses) {

					String s = clause.toString();
					Keyword kwd = new DefaultKeyword();
					kwd.setFacet(FACET_PATTERN_OIE_CLAUSE);
					kwd.setLex(s);
					kwd.setBegin(-1);
					kwd.setEnd(-1);
					doc.addKeyword(kwd);
				} // END OF FOR EACH Clause

			}
		}
	}
}
