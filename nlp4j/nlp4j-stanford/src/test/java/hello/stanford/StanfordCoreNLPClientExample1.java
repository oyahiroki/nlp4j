package hello.stanford;

import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLPClient;

@SuppressWarnings("javadoc")
public class StanfordCoreNLPClientExample1 {

	public static void main(String[] args) throws Exception {

		// creates a StanfordCoreNLP object with POS tagging, lemmatization
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, pos, lemma");

		// edu.stanford.nlp.pipeline.StanfordCoreNLPClient
		StanfordCoreNLPClient pipeline //
				= new StanfordCoreNLPClient(props, "http://localhost", 9000, 2);

		// read some text in the text variable
		String text = "This is sample text for NLP.";

		// create an empty Annotation just with the given text
		Annotation document = new Annotation(text);

		// run all Annotators on this text
		pipeline.annotate(document);

		{
			List<CoreLabel> labels = document.get(TokensAnnotation.class);
			// for each labels
			for (CoreLabel label : labels) { //
				String str = label.get(TextAnnotation.class);
				String lex = label.get(LemmaAnnotation.class);
				String pos = label.get(PartOfSpeechAnnotation.class);
				int begin = label.beginPosition();
				int end = label.endPosition();

				System.err.println("str=" + str + ",lex=" + lex + ",pos=" + pos + ",begin=" + begin + ",end=" + end);

			} // for each labels
		}

	}

}
