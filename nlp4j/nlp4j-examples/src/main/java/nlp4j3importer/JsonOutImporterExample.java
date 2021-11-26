package nlp4j3importer;

import java.util.ArrayList;
import java.util.List;

import nlp4j.Document;
import nlp4j.impl.DefaultDocument;
import nlp4j.importer.JsonOutImporter;

@SuppressWarnings("javadoc")
public class JsonOutImporterExample {

	public static void main(String[] args) throws Exception {

		List<Document> docs = new ArrayList<>();
		{
			Document doc = new DefaultDocument();
			{
				doc.putAttribute("id", "DOC001");
				doc.putAttribute("item", 1);
				doc.putAttribute("text", "This is test.");
				doc.putAttribute("text_ja", "これはテストです。");
			}
			docs.add(doc);
		}
		{
			Document doc = new DefaultDocument();
			{
				doc.putAttribute("id", "DOC002");
				doc.putAttribute("item", 2);
				doc.putAttribute("text", "This is test.2nd");
				doc.putAttribute("text_ja", "これはテストです。2番目");
			}
			docs.add(doc);
		}

		JsonOutImporter jsonOut = new JsonOutImporter();
		{
			jsonOut.setProperty("file", "src/test/resources/nlp4j/test2.txt");
		}

		jsonOut.importDocuments(docs);

	}

}
