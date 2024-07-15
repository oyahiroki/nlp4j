package hello;

import java.util.ArrayList;
import java.util.List;

import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.DocumentBuilder;
import nlp4j.annotator.KeywordFacetMapToFieldAnnotator;
import nlp4j.annotator.KeywordFacetMappingAnnotator;
import nlp4j.impl.DefaultDocument;
import nlp4j.impl.DefaultKeyword;
import nlp4j.krmj.annotator.KuromojiAnnotator;
import nlp4j.llm.embeddings.EmbeddingAnnotator;
import nlp4j.solr.importer.SolrDocumentImporter;
import nlp4j.util.DocumentUtil;
import nlp4j.util.DocumentsUtils;

public class Hello002_ImportDocumentWithVector2 {

	public static void main(String[] args) throws Exception {
		String endPoint = "http://localhost:8983/solr/";
		String collection = "sandbox";

		List<Document> docs = new ArrayList<>();
		{
			Document doc = new DefaultDocument();
//			{
//				doc.putAttribute("id", "doc001");
//				// dynamic field
//				// *_txt_ja
//				doc.putAttribute("text", "今日はいい天気です。");
//				doc.putAttribute("field1_s", "AAA");
//				doc.putAttribute("field2_i", 100);
//			}
			doc = (new DocumentBuilder()).id("doc001").text("今日はいい天気です") //
					.put("field1_s", "AAA").put("field2_i", 100).build();
			docs.add(doc);
		}
		{
			Document doc = new DefaultDocument();
//			{
//				doc.putAttribute("id", "doc002");
//				// dynamic field
//				// *_txt_ja
//				doc.putAttribute("text", "明日はいい天気かしら。天気予報を見てみよう。");
//				doc.putAttribute("field1_s", "BBB");
//				doc.putAttribute("field2_i", 101);
//			}
			doc = (new DocumentBuilder()).id("doc002").text("明日はいい天気かしら。天気予報を見てみよう。") //
					.put("field1_s", "BBB").put("field2_i", 101).build();
			docs.add(doc);
		}
		{
			KuromojiAnnotator ann = new KuromojiAnnotator();
			ann.setProperty("target", "text");
			ann.annotate(docs);
		}
		{
			KeywordFacetMappingAnnotator ann = new KeywordFacetMappingAnnotator();
			ann.setProperty("mapping", KeywordFacetMappingAnnotator.DEFAULT_MAPPING);
			ann.annotate(docs);
		}
		{

			System.err.println(DocumentUtil.toJsonPrettyString(docs));

			boolean debug = true;
			if (debug) {
//				return;
			}
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
