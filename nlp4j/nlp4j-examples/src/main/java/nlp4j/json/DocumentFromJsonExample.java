package nlp4j.json;

import nlp4j.Document;
import nlp4j.util.DocumentUtil;

public class DocumentFromJsonExample {

	// Main method where the execution of the program begins
	public static void main(String[] args) throws Exception {

		// Parsing a JSON string to create a Document object.
		// The JSON contains an 'id', 'date', 'item', and 'text' fields.
		// The 'parseFromJson' method is used for this purpose and may throw an
		// Exception if the input is invalid.
		Document doc = DocumentUtil
				.parseFromJson("{'id':'001','date':'2021-04-01T00:00:00Z','item':'aaa','text':'これはテストです．1'}");

		// Printing the 'id' of the Document object to the error output stream.
		System.err.println(doc.getId());

		// Printing the value of the 'item' attribute from the Document object to the
		// error output stream.
		System.err.println(doc.getAttributeAsString("item"));
	}
}
