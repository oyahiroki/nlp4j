package hello.opensearch;

import java.util.ArrayList;
import java.util.List;

import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.DocumentImporter;
import nlp4j.impl.DefaultDocument;
import nlp4j.importer.DocumentImporterBuilder;
import nlp4j.llm.embeddings.EmbeddingAnnotator;
import nlp4j.opensearch.importer.OpenSearchDocumentImporter;

public class Hello002A_EmbeddingImportMain {

	public static void main(String[] args) throws Exception {

		String text = "私は読書が好きです。";

		List<Document> docs = new ArrayList<>();
		{
			{
				Document doc = new DefaultDocument();
				{
					doc.putAttribute("id", "1");
					doc.putAttribute("text", text);
				}
				docs.add(doc);
			}

			{ // Vector
				DocumentAnnotator ann = new EmbeddingAnnotator();
				ann.setProperty("target", "text");
				ann.setProperty("vector_name", "vector1024");
				ann.setProperty("endPoint", "http://localhost:8888/");
				ann.annotate(docs); // vector size is 1024
			}

//			System.err.println(DocumentUtil.toJsonPrettyString(docs.get(0)));

		}
		{
			try ( //
					DocumentImporter imt = (new DocumentImporterBuilder(OpenSearchDocumentImporter.class) //
							.p("index", "myindex1") //
							.build()); //
			) {
				imt.importDocuments(docs);
			}

		}
	}

}
