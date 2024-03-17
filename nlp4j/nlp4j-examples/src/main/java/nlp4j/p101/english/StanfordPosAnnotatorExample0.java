package nlp4j.p101.english;

import nlp4j.Document;
import nlp4j.Keyword;
import nlp4j.impl.DefaultDocument;
import nlp4j.stanford.StanfordPosAnnotator;

/**
 * @author Hiroki Oya
 * @created_at 2021-07-18
 */
public class StanfordPosAnnotatorExample0 {

	@SuppressWarnings("javadoc")
	public static void main(String[] args) throws Exception {

		Document doc = new DefaultDocument();
		{
			doc.putAttribute("text", "I eat sushi with chopsticks.");
		}

		StanfordPosAnnotator ann = new StanfordPosAnnotator();
		{
			ann.setProperty("target", "text");
		}

		ann.annotate(doc); // do annotation

		for (Keyword kwd : doc.getKeywords()) {
			System.err.println(kwd);
		}

	}

}
