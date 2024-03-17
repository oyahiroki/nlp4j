package nlp4j.core;

import nlp4j.Document;
import nlp4j.impl.DefaultDocument;
import nlp4j.util.DocumentUtil;

public class HelloDocumentExample001 {

	public static void main(String[] args) {
		Document doc = new DefaultDocument();
		doc.putAttribute("text", "This is test.");
		System.out.println(doc);
		System.out.println(DocumentUtil.toJsonPrettyString(doc));
	}

}
