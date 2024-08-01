package hello;

import java.util.ArrayList;
import java.util.List;

import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.impl.DefaultDocument;
import nlp4j.impl.DefaultKeyword;
import nlp4j.llm.embeddings.EmbeddingAnnotator;
import nlp4j.solr.importer.SolrDocumentImporter;

public class Hello002_ImportDocumentWithVector1 {

	public static void main(String[] args) throws Exception {
		String endPoint = "http://localhost:8983/solr/";
		String collection = "sandbox";

		List<Document> docs = new ArrayList<>();
		{
			Document doc = new DefaultDocument();
			{
				doc.putAttribute("id", "doc001");
				// dynamic field
				// *_txt_ja
				doc.putAttribute("text", "今日はいい天気です。");
				doc.putAttribute("field1_s", "AAA");
				doc.putAttribute("field2_i", 100);
				doc.addKeyword(new DefaultKeyword(0, 2, "word.nn", "今日", "今日"));
				doc.addKeyword(new DefaultKeyword(3, 5, "word.adj", "いい", "いい"));
				doc.addKeyword(new DefaultKeyword(4, 6, "word.nn", "天気", "天気"));
			}
			docs.add(doc);
		}
		{
			Document doc = new DefaultDocument();
			{
				doc.putAttribute("id", "doc002");
				// dynamic field
				// *_txt_ja
				doc.putAttribute("text", "明日はいい天気かしら");
				doc.putAttribute("field1_s", "BBB");
				doc.putAttribute("field2_i", 101);
				doc.addKeyword(new DefaultKeyword(0, 2, "word.nn", "明日", "明日"));
				doc.addKeyword(new DefaultKeyword(3, 5, "word.adj", "いい", "いい"));
				doc.addKeyword(new DefaultKeyword(4, 6, "word.nn", "天気", "天気"));
			}
			docs.add(doc);
		}

		{ // Vector
			DocumentAnnotator ann = new EmbeddingAnnotator();
			ann.setProperty("target", "text");
			ann.annotate(docs); // vector size is 1024
		}

		try (SolrDocumentImporter importer = new SolrDocumentImporter()) {
			importer.setProperty("endPoint", endPoint);
			importer.setProperty("collection", collection);
			importer.setProperty("keyword_facet_field_mapping", //
					"word->word_ss" //
			);
			importer.setProperty("attribute_field_mapping", //
					"text->text_txt_ja" //
			);
			importer.importDocuments(docs);
			importer.commit();
		}

	}

}
