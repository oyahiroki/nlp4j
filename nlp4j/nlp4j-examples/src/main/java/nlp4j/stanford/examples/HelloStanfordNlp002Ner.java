package nlp4j.stanford.examples;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.ling.CoreAnnotations.CharacterOffsetBeginAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.CharacterOffsetEndAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentenceIndexAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokenBeginAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokenEndAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.coref.docreader.CoNLLDocumentReader.NamedEntityAnnotation;
import edu.stanford.nlp.ie.machinereading.structure.MachineReadingAnnotations.RelationMentionsAnnotation;
import edu.stanford.nlp.ie.machinereading.structure.RelationMention;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.NERCombinerAnnotator;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.BasicDependenciesAnnotation;
import edu.stanford.nlp.util.CoreMap;

@SuppressWarnings("javadoc")
public class HelloStanfordNlp002Ner {

	public static void main(String[] args) {
		String text = "Nissan motor is a one of Vehicle company.";

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

		// relation -> ner, tree

		// ner : Named Entity

		properties.setProperty("annotators", "tokenize, ssplit, pos, lemma, parse, ner");
		StanfordCoreNLP coreNLP = new StanfordCoreNLP(properties);
		Annotation annotation = new Annotation(text);
		coreNLP.annotate(annotation);

		Object[] ooo = annotation.keySetNotNull().toArray();
		System.err.println(Arrays.deepToString(ooo));

		System.err.println("TokensAnnotation");
		// class edu.stanford.nlp.ling.CoreAnnotations$TokensAnnotation,
		{
			List<CoreLabel> cl = annotation.get(TokensAnnotation.class);
			for (CoreLabel label : cl) {
				System.err.println(label);
				System.err.println(Arrays.toString(label.keySet().toArray()));

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
				{
					int n = label.get(SentenceIndexAnnotation.class);
					System.err.println("SentenceIndexAnnotation=" + n);
				}
				{
					String s = label.get(PartOfSpeechAnnotation.class);
					System.err.println("PartOfSpeechAnnotation=" + s);
				}
				{
					String l = label.get(LemmaAnnotation.class);
					System.err.println("LemmaAnnotation=" + l);
				}

			}
		}
		System.err.println("TextAnnotation");
		// class edu.stanford.nlp.ling.CoreAnnotations$TextAnnotation,
		{
			String s = annotation.get(TextAnnotation.class);
			System.err.println(s);
		}
		// class edu.stanford.nlp.ling.CoreAnnotations$SentencesAnnotation
		System.err.println("SentencesAnnotation");
		{
		}

		{
			List<CoreMap> sentenceMap = annotation.get(SentencesAnnotation.class);
			for (CoreMap label : sentenceMap) {

				Object[] oo = label.keySet().toArray();
				System.err.println(Arrays.toString(oo));

				// [class edu.stanford.nlp.ling.CoreAnnotations$TextAnnotation,
				{
					String s = label.get(TextAnnotation.class);
					System.err.println("s=" + s);
				}
				// class edu.stanford.nlp.ling.CoreAnnotations$CharacterOffsetBeginAnnotation,
				{
					int n = label.get(CharacterOffsetBeginAnnotation.class);
					System.err.println("CharacterOffsetBeginAnnotation=" + n);
				}
				// class edu.stanford.nlp.ling.CoreAnnotations$CharacterOffsetEndAnnotation,
				{
					int n = label.get(CharacterOffsetEndAnnotation.class);
					System.err.println("CharacterOffsetEndAnnotation=" + n);
				}
				// class edu.stanford.nlp.ling.CoreAnnotations$TokensAnnotation,
				{
					label.get(TokensAnnotation.class);
				}
				// class edu.stanford.nlp.ling.CoreAnnotations$SentenceIndexAnnotation,
				{
					int n = label.get(SentenceIndexAnnotation.class);
					System.err.println("SentenceIndexAnnotation=" + n);
				}
				// class edu.stanford.nlp.ling.CoreAnnotations$TokenBeginAnnotation,
				{
					int n = label.get(TokenBeginAnnotation.class);
					System.err.println("TokenBeginAnnotation=" + n);
				}
				// class edu.stanford.nlp.ling.CoreAnnotations$TokenEndAnnotation]
				{
					int n = label.get(TokenEndAnnotation.class);
					System.err.println("TokenEndAnnotation=" + n);
				}

				{
					String n = label.get(NamedEntityTagAnnotation.class);
					System.err.println("NamedEntityTagAnnotation=" + n);
				}

				System.err.println(label);

//				SemanticGraph graph = label.get(BasicDependenciesAnnotation.class);
//				System.out.println(graph);
//				if (graph != null) {
//					System.err.println(graph.getFirstRoot());
//					IndexedWord root = graph.getFirstRoot();
//
////					printWord(root, graph, 0);
//
////					graph.getChildList(root);
//
//					System.err.println(graph.toString());
//				}

			}
		}
	}

}
