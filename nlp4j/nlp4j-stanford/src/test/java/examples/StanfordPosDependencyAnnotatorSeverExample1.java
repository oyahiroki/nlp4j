package examples;

import nlp4j.Document;
import nlp4j.Keyword;
import nlp4j.KeywordWithDependency;
import nlp4j.impl.DefaultDocument;
import nlp4j.stanford.StanfordPosDependencyAnnotator;

public class StanfordPosDependencyAnnotatorSeverExample1 {

	/**
	 * <pre>
	 * Run nlp4j.stanford.server.StanfordCoreNlpServerStarter.main(String[]) before runing this code
	 * </pre>
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		String text = "We are trying to understand the difference.";

		Document doc = new DefaultDocument();
		doc.putAttribute("text", text);

		StanfordPosDependencyAnnotator ann = new StanfordPosDependencyAnnotator();
		{
			ann.setProperty("target", "text");
			ann.setProperty("type", "server");
			ann.setProperty("server.port", "9000");
			ann.setProperty("server.threads", "2");
			ann.setProperty("server.endpoint", "http://localhost");
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
