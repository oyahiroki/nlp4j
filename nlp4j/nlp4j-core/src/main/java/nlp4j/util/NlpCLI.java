package nlp4j.util;

import java.util.Scanner;

import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.KeywordWithDependency;
import nlp4j.impl.DefaultDocument;

/**
 * created on 2021-05-07
 * 
 * @author Hiroki Oya
 * @since 1.3.1.0
 */
public class NlpCLI {

	DocumentAnnotator ann;

	/**
	 * @param ann Instance of Document Annotator
	 */
	public NlpCLI(DocumentAnnotator ann) {
		this.ann = ann;
	}

	private void pipeline(String text) throws Exception {
		Document doc = new DefaultDocument();
		doc.putAttribute("text", text);
		ann.annotate(doc);
		for (KeywordWithDependency kw : doc.getKeywords(KeywordWithDependency.class)) {
			System.out.println(kw.toStringAsXml());
		}
	}

	/**
	 * Run NLP
	 * 
	 * @throws Exception on ERROR
	 */
	public void run() throws Exception {
		try (Scanner scanner = new Scanner(System.in);) {
			while (scanner.hasNext()) {
				String line = scanner.nextLine();
				pipeline(line);
			}
		}

	}

}
