package hello.stanford;

import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations.CharacterOffsetBeginAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.CharacterOffsetEndAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

public class HelloStanfordNlp001Pos1 {

	public static void main(String[] args) {
		String text = "I eat sushi with chopsticks.";

		Properties properties = new Properties();
		properties.setProperty("annotators", "tokenize, ssplit, pos, lemma");
		StanfordCoreNLP coreNLP = new StanfordCoreNLP(properties);
		Annotation annotation = new Annotation(text);
		coreNLP.annotate(annotation);

		List<CoreLabel> cl = annotation.get(TokensAnnotation.class);
		for (CoreLabel label : cl) {
			int begin = label.get(CharacterOffsetBeginAnnotation.class);
			int end = label.get(CharacterOffsetEndAnnotation.class);
			String pos = label.get(PartOfSpeechAnnotation.class);
			String txt = label.get(TextAnnotation.class);
			String lemma = label.get(LemmaAnnotation.class);
			System.err.println(String.format("%d,%d,%s,%s,%s", begin, end, pos, txt, lemma));
		}
	}
}
