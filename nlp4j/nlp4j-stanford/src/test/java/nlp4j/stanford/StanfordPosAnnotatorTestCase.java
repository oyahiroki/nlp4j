package nlp4j.stanford;

import junit.framework.TestCase;
import nlp4j.Document;
import nlp4j.Keyword;
import nlp4j.KeywordWithDependency;
import nlp4j.impl.DefaultDocument;

/**
 * created_at 2021-08-03
 * 
 * @author Hiroki Oya
 * 
 */
public class StanfordPosAnnotatorTestCase extends TestCase {

	@SuppressWarnings("rawtypes")
	Class target = StanfordPosAnnotator.class;

	/**
	 * @throws Exception
	 */
	public void testAnnotateDocument001() throws Exception {
		String text = "I believe that he will come.";
		StanfordPosAnnotator ann = new StanfordPosAnnotator();
		{
			ann.setProperty("target", "text");
		}
		Document doc = new DefaultDocument();
		{
			doc.putAttribute("text", text);
		}
		ann.annotate(doc);
		for (Keyword kwd : doc.getKeywords()) {
			if (kwd instanceof KeywordWithDependency) {
				KeywordWithDependency kd = (KeywordWithDependency) kwd;
				System.err.println(kd.toStringAsXml());
			}
		}
	}

}
