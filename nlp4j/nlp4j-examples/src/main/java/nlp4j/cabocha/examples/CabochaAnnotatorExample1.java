package nlp4j.cabocha.examples;

import nlp4j.Document;
import nlp4j.Keyword;
import nlp4j.KeywordWithDependency;
import nlp4j.cabocha.CabochaAnnotator;
import nlp4j.impl.DefaultDocument;

/**
 * @author Hiroki Oya
 * @created_at 2021-07-18
 */
public class CabochaAnnotatorExample1 {

	@SuppressWarnings("javadoc")
	public static void main(String[] args) throws Exception {
		Document doc = new DefaultDocument();
		{
			doc.putAttribute("text", "車が高速道路で急に停止した。エンジンから煙がもくもくと出た。");
		}
		CabochaAnnotator ann = new CabochaAnnotator();
		{
			ann.setProperty("encoding", "MS932");
			ann.setProperty("target", "text");
		}

		ann.annotate(doc); // Annotation

		for (Keyword kwd : doc.getKeywords()) {
			if (kwd instanceof KeywordWithDependency) {
				System.err.println(((KeywordWithDependency) kwd).toStringAsDependencyTree());
			}
		}
	}

}
