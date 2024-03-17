package nlp4j.core;

import nlp4j.Document;
import nlp4j.DocumentBuilder;
import nlp4j.util.DocumentUtil;

public class DocumentBuilderExample0 {

	public static void main(String[] args) {

		Document doc = (new DocumentBuilder()).v("text", "This is test.").create();

		System.err.println(DocumentUtil.toPrettyJsonString(doc));

	}

}
