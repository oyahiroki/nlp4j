package hello.opensearch;

import java.util.ArrayList;
import java.util.List;

import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.DocumentBuilder;
import nlp4j.impl.DefaultDocument;
import nlp4j.llm.embeddings.EmbeddingAnnotator;
import nlp4j.opensearch.importer.OpenSearchDocumentImporter;
import nlp4j.util.DateUtils;

public class Hello002_EmbeddingImportMain {

	public static void main(String[] args) throws Exception {

		String text = "私は読書が好きです。";
		String[] texts = { "私はMLBが好きです。", "私はメジャーリーグが好きです。", "私はべーブルースが好きです。" };

		List<Document> docs = new ArrayList<>();
		{
			{
				Document doc = new DefaultDocument();
				{
					doc.putAttribute("id", "doc_" + DateUtils.get_yyyyMMdd_HHmmss());
					doc.putAttribute("text", text);
				}
//				docs.add(doc);
			}

			int idx = 0;
			for (String t : texts) {
				docs.add((new DocumentBuilder()).id("doc_" + DateUtils.get_yyyyMMdd_HHmmss() + "_" + idx).text(t)
						.build());
				idx++;
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
			try (OpenSearchDocumentImporter imt = new OpenSearchDocumentImporter();) {
				imt.importDocuments(docs);

			}
		}
	}

}
