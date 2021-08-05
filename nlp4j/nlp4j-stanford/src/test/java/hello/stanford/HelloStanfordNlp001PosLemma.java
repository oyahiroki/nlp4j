package hello.stanford;

import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations.CharacterOffsetBeginAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.CharacterOffsetEndAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentenceIndexAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokenBeginAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokenEndAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
//import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
//import edu.stanford.nlp.semgraph.SemanticGraph;
//import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.BasicDependenciesAnnotation;
import edu.stanford.nlp.util.CoreMap;

@SuppressWarnings("javadoc")
public class HelloStanfordNlp001PosLemma {

	public static void main(String[] args) {
		String text = "This is test. He runs fast.";

		Properties properties = new Properties();
		// "tokenize, ssplit, pos, lemma, depparse"
		// https://stanfordnlp.github.io/CoreNLP/annotators.html
		// tokenize : TokenizerAnnotator
		// ssplit : WordsToSentencesAnnotator : Splits a sequence of tokens into
		// sentences.
		// pos : POSTaggerAnnotator : Labels tokens with their POS tag.
		// lemma : 見出し
		// lemma : MorphaAnnotator : Generates the word lemmas for all tokens in the
		// corpus.

		properties.setProperty("annotators", "tokenize, ssplit, pos, lemma");
		StanfordCoreNLP coreNLP = new StanfordCoreNLP(properties);
		Annotation annotation = new Annotation(text);
		coreNLP.annotate(annotation);

//		Object[] ooo = annotation.keySetNotNull().toArray();
//		System.err.println(Arrays.deepToString(ooo));

//		System.err.println("TokensAnnotation");
		// class edu.stanford.nlp.ling.CoreAnnotations$TokensAnnotation,
		{
			List<CoreLabel> cl = annotation.get(TokensAnnotation.class);
			for (CoreLabel label : cl) {

				System.err.println("<token>");

//				System.err.println(label);
//				System.err.println(Arrays.toString(label.keySet().toArray()));

				// [class edu.stanford.nlp.ling.CoreAnnotations$ValueAnnotation,
				// class edu.stanford.nlp.ling.CoreAnnotations$TextAnnotation,
				// class edu.stanford.nlp.ling.CoreAnnotations$OriginalTextAnnotation,
				// class edu.stanford.nlp.ling.CoreAnnotations$CharacterOffsetBeginAnnotation,
				// class edu.stanford.nlp.ling.CoreAnnotations$CharacterOffsetEndAnnotation,
				// class edu.stanford.nlp.ling.CoreAnnotations$BeforeAnnotation,
				// class edu.stanford.nlp.ling.CoreAnnotations$AfterAnnotation,
				// class edu.stanford.nlp.ling.CoreAnnotations$IsNewlineAnnotation,
				// class edu.stanford.nlp.ling.CoreAnnotations$TokenBeginAnnotation,
				// class edu.stanford.nlp.ling.CoreAnnotations$TokenEndAnnotation,
				// class edu.stanford.nlp.ling.CoreAnnotations$IndexAnnotation,
				// class edu.stanford.nlp.ling.CoreAnnotations$SentenceIndexAnnotation,
				// class edu.stanford.nlp.ling.CoreAnnotations$PartOfSpeechAnnotation,
				// class edu.stanford.nlp.ling.CoreAnnotations$LemmaAnnotation]
				System.err.println("SentenceIndexAnnotation=" + label.get(SentenceIndexAnnotation.class));
				System.err.println("PartOfSpeechAnnotation=" + label.get(PartOfSpeechAnnotation.class));
				System.err.println("LemmaAnnotation=" + label.get(LemmaAnnotation.class));

				System.err.println("</token>");
			}
		}
		System.err.println("---");
		System.err.println("TextAnnotation");
		// class edu.stanford.nlp.ling.CoreAnnotations$TextAnnotation,
		{
			String s = annotation.get(TextAnnotation.class);
			System.err.println(s);
		}
		// class edu.stanford.nlp.ling.CoreAnnotations$SentencesAnnotation
		System.err.println("SentencesAnnotation");

		{
			List<CoreMap> sentenceMap = annotation.get(SentencesAnnotation.class);

			for (CoreMap label : sentenceMap) {
				System.err.println("<sentence>");
//				Object[] oo = label.keySet().toArray();
//				System.err.println(Arrays.toString(oo));
				// [class edu.stanford.nlp.ling.CoreAnnotations$TextAnnotation,
				{
					System.err.println("TextAnnotation=" + label.get(TextAnnotation.class));
				}
				// class edu.stanford.nlp.ling.CoreAnnotations$CharacterOffsetBeginAnnotation,
				{
					System.err.println(
							"CharacterOffsetBeginAnnotation=" + label.get(CharacterOffsetBeginAnnotation.class));
				}
				// class edu.stanford.nlp.ling.CoreAnnotations$CharacterOffsetEndAnnotation,
				{
					System.err.println("CharacterOffsetEndAnnotation=" + label.get(CharacterOffsetEndAnnotation.class));
				}
				// class edu.stanford.nlp.ling.CoreAnnotations$TokensAnnotation,
				{
					System.err.println("TokensAnnotation=" + label.get(TokensAnnotation.class));
				}
				// class edu.stanford.nlp.ling.CoreAnnotations$SentenceIndexAnnotation,
				{
					System.err.println("SentenceIndexAnnotation=" + label.get(SentenceIndexAnnotation.class));
				}
				// class edu.stanford.nlp.ling.CoreAnnotations$TokenBeginAnnotation,
				{
					System.err.println("TokenBeginAnnotation=" + label.get(TokenBeginAnnotation.class));
				}
				// class edu.stanford.nlp.ling.CoreAnnotations$TokenEndAnnotation]
				{
					System.err.println("TokenEndAnnotation=" + label.get(TokenEndAnnotation.class));
				}

//				System.err.println(label);

//				SemanticGraph graph = label.get(BasicDependenciesAnnotation.class);
//
////				System.out.println(graph);
//				System.err.println(graph == null);
//				if (graph != null) {
//					System.err.println(graph.getFirstRoot());
//					IndexedWord root = graph.getFirstRoot();
//					System.err.println(root);
////					printWord(root, graph, 0);
//
////					graph.getChildList(root);
//
//					System.err.println(graph.toString());
//				}

				System.err.println("</sentence>");
			}
		}
	}

}
