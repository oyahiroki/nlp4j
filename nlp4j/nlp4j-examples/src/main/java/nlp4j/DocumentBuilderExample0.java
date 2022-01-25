package nlp4j;

import nlp4j.util.DocumentUtil;

public class DocumentBuilderExample0 {

	public static void main(String[] args) {

		Document doc = (new DocumentBuilder()).v("text", "これはテストです．").create();

		System.err.println(DocumentUtil.toPrettyJsonString(doc));

	}

}
