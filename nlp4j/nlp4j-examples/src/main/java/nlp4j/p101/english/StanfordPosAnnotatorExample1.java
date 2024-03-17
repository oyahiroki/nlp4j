package nlp4j.p101.english;

import java.util.List;

import nlp4j.Keyword;
import nlp4j.KeywordParser;
import nlp4j.stanford.StanfordPosAnnotator;

/**
 * @author Hiroki Oya
 * @created_at 2021-07-18
 */
public class StanfordPosAnnotatorExample1 {

	@SuppressWarnings("javadoc")
	public static void main(String[] args) throws Exception {

		String text = "I eat sushi with chopsticks.";

		try (KeywordParser parser = new KeywordParser(new StanfordPosAnnotator());) {
			List<Keyword> kwds = parser.parse(text);
			for (Keyword kwd : kwds) {
				System.err.println(kwd);
			}
		}

	}

}
