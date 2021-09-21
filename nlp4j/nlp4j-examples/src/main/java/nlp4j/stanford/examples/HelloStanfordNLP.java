package nlp4j.stanford.examples;

import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations.CharacterOffsetBeginAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.CharacterOffsetEndAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.IndexAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.OriginalTextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentenceIndexAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.BasicDependenciesAnnotation;
import edu.stanford.nlp.util.CoreMap;

@SuppressWarnings("javadoc")
public class HelloStanfordNLP {

	public static void main(String[] args) {

		String text = "A natural language parser is a program that works out the grammatical structure of sentences";

		Properties properties = new Properties();
		properties.setProperty("annotators", "tokenize, ssplit, pos, lemma, depparse");
		StanfordCoreNLP coreNLP = new StanfordCoreNLP(properties);
		Annotation annotation = new Annotation(text);
		coreNLP.annotate(annotation);
		List<CoreMap> sentenceMap = annotation.get(SentencesAnnotation.class);
		for (CoreMap label : sentenceMap) {
			SemanticGraph graph = label.get(BasicDependenciesAnnotation.class);
			IndexedWord root = graph.getFirstRoot();
			printWord(root, graph, 0);

			System.err.println("<graph>");
			System.err.println(graph.toString());
			System.err.println("</graph>");
		}
	}

	public static void printWord(IndexedWord word, SemanticGraph graph, int tab) {

		// System.err.println(word.keySet());
		System.err.println("<word>");
		System.err.println("depth:" + tab);
		System.err.println("TextAnnotation:" + word.get(TextAnnotation.class));
		System.err.println("OriginalTextAnnotation:" + word.get(OriginalTextAnnotation.class));
		System.err.println("CharacterOffsetBeginAnnotation:" + word.get(CharacterOffsetBeginAnnotation.class));
		System.err.println("CharacterOffsetEndAnnotation:" + word.get(CharacterOffsetEndAnnotation.class));
//		System.err.println("BeforeAnnotation:" + word.get(BeforeAnnotation.class));
//		System.err.println("AfterAnnotation:" + word.get(AfterAnnotation.class));
		System.err.println("IndexAnnotation:" + word.get(IndexAnnotation.class));
		System.err.println("SentenceIndexAnnotation:" + word.get(SentenceIndexAnnotation.class));
		System.err.println("PartOfSpeechAnnotation:" + word.get(PartOfSpeechAnnotation.class));
//		System.err.println(word.toString());
		System.err.println("</word>");

		List<IndexedWord> list = graph.getChildList(word);

		for (int n = 0; n < list.size(); n++) {
			printWord(list.get(n), graph, tab + 1);
		}

	}

}
