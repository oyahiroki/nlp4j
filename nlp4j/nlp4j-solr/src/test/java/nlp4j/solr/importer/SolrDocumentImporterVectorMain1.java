package nlp4j.solr.importer;

import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.DocumentBuilder;
import nlp4j.annotator.KeywordFacetMappingAnnotator;
import nlp4j.krmj.annotator.KuromojiAnnotator;
import nlp4j.llm.embeddings.EmbeddingAnnotator;

public class SolrDocumentImporterVectorMain1 {

	public static void main(String[] args) throws Exception {

		String endPoint = "http://localhost:8983/solr/";
		String collection = "sandbox";

		// Document
		Document doc = (new DocumentBuilder()).text("今日はいい天気です").build();

		{ // Kuromoji
			KuromojiAnnotator ann = new KuromojiAnnotator();
			ann.setProperty("target", "text");
			ann.annotate(doc);
		}
		{ // Keyword Facet Mapping for Solr
			KeywordFacetMappingAnnotator ann = new KeywordFacetMappingAnnotator();
			ann.setProperty("mapping", KeywordFacetMappingAnnotator.DEFAULT_MAPPING);
			ann.annotate(doc);
		}

		// Annotator for Vector
		{
			DocumentAnnotator ann = new EmbeddingAnnotator();
			ann.setProperty("target", "text");
			ann.annotate(doc);
			System.err.println(doc.getAttributeAsListNumbers("vector").size());
		}

		SolrDocumentImporterVector importer = new SolrDocumentImporterVector();
		{
			importer.setProperty("endPoint", endPoint);
			importer.setProperty("collection", collection);
			importer.setProperty("keyword_facet_field_mapping", //
					"word->word_ss" //
			);
			importer.setProperty("attribute_field_mapping", //
					"text->text_txt_ja" //
			);
		}
		importer.importDocument(doc);

		importer.commit();
		importer.close();

	}

}
