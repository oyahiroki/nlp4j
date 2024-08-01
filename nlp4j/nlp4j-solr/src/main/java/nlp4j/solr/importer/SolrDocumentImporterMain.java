package nlp4j.solr.importer;

import java.util.ArrayList;
import java.util.List;

import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.DocumentBuilder;
import nlp4j.annotator.KeywordFacetMappingAnnotator;
import nlp4j.krmj.annotator.KuromojiAnnotator;
import nlp4j.llm.embeddings.EmbeddingAnnotator;
import nlp4j.util.DateUtils;

public class SolrDocumentImporterMain {

	public static void main(String[] args) throws Exception {

		String endPoint = "http://localhost:8983/solr/";
		String collection = "sandbox";

		String docid = DateUtils.get_yyyyMMdd_HHmmss();
		String text = "今日はいい天気です";

		if (args != null && args.length > 0) {
			text = args[0];
		}

		List<Document> docs = new ArrayList<>();
		{
			docs.add((new DocumentBuilder())//
					.id(docid) //
					.text(text) //
//					.put("field1_s", "AAA")
//					.put("field2_i", 100)
					.build());
		}
		{ // Kuromoji
			KuromojiAnnotator ann = new KuromojiAnnotator();
			ann.setProperty("target", "text");
			ann.annotate(docs);
		}
		{ // Keyword Facet Mapping for Solr
			KeywordFacetMappingAnnotator ann = new KeywordFacetMappingAnnotator();
			ann.setProperty("mapping", KeywordFacetMappingAnnotator.DEFAULT_MAPPING);
			ann.annotate(docs);
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
