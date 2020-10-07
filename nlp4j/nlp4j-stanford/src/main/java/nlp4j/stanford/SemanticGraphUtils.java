package nlp4j.stanford;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.ValueAnnotation;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.trees.GrammaticalRelation;
import nlp4j.Document;
import nlp4j.KeywordWithDependency;
import nlp4j.impl.DefaultKeywordWithDependency;

public class SemanticGraphUtils {

//	static private final Logger logger = Logger.getLogger(SemanticGraphUtils.class);

	/**
	 * @param doc      Document to be annotated.
	 * @param word     Pointer of Keyword
	 * @param graph    SemanticGraph
	 * @param depth    Depth of node
	 * @param used     Keyword checked in node tree
	 * @param relation Relation of nodes
	 */
	public static void extractKeywords(Document doc, IndexedWord word, SemanticGraph graph, int depth,
			Set<IndexedWord> used, GrammaticalRelation relation) {
		extractKeywords(doc, null, word, graph, depth, used, relation, new SemanticGraphInfo());
	}

	/**
	 * @param doc
	 * @param ptr
	 * @param word     Pointer of Keyword
	 * @param graph    SemanticGraph
	 * @param depth    Depth of node
	 * @param used     Keyword checked in node tree
	 * @param relation Relation of nodes
	 * @param info
	 */
	public static void extractKeywords(Document doc, KeywordWithDependency ptr, IndexedWord word, SemanticGraph graph,
			int depth, Set<IndexedWord> used, GrammaticalRelation relation, SemanticGraphInfo info) {

		used.add(word);

		String str = word.get(ValueAnnotation.class);
		String facet = word.getString(PartOfSpeechAnnotation.class);
		String lex = word.getString(LemmaAnnotation.class);

		String relationValue = null;
		String relationValueLong = null;

		if (relation == null) {
			relationValue = "root";
			relationValueLong = "root";
		} //
		else {
			relationValue = relation.getShortName();
			relationValueLong = relation.getLongName();
		}

		// id of root is 0
		int id = info.getId();

		int begin = word.beginPosition();
		int end = word.endPosition();

		KeywordWithDependency kwd //
				= new DefaultKeywordWithDependency("stanford.dependency", begin, end, facet, lex, str, relationValue);

		if (ptr == null) {
			doc.addKeyword(kwd);
		} else {
			ptr.addChild(kwd);
		}

		info.idPP();

		// 子ノード
		List<SemanticGraphEdge> edges = graph.outgoingEdgeList(word);
		{
			Collections.sort(edges);

			for (SemanticGraphEdge edge : edges) {

				IndexedWord targetWord = edge.getTarget();
				GrammaticalRelation relation2 = edge.getRelation();

				extractKeywords(doc, kwd, targetWord, graph, depth + 1, used, relation2, info);

				List<SemanticGraphEdge> edges2 = graph.outgoingEdgeList(targetWord);
				for (SemanticGraphEdge e2 : edges2) {
					IndexedWord w2 = e2.getTarget();
					GrammaticalRelation r2 = edge.getRelation();
					if (used.contains(w2) == false) {
						extractKeywords(doc, ptr, w2, graph, depth + 1, used, r2, info);
					}
				}

			}
		}

	}

}
