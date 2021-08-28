package nlp4j.solr;

import nlp4j.Document;
import nlp4j.impl.DefaultDocument;
import nlp4j.impl.DefaultKeyword;
import nlp4j.solr.importer.SolrDocumentImporter;

public class SolrDocumentImporterMain {

	public static void main(String[] args) throws Exception {

		Document doc = new DefaultDocument();
		{
			doc.putAttribute("id", "doc001");
			// dynamic field
			// *_txt_ja
			doc.putAttribute("text", "今日はいい天気です。");
			doc.putAttribute("field1", "AAA");
			doc.putAttribute("field2x", "BBB");
			doc.putAttribute("field_int1", 100);

			doc.addKeyword(new DefaultKeyword(0, 2, "word.nn", "今日", "今日"));
			doc.addKeyword(new DefaultKeyword(3, 5, "word.adj", "いい", "いい"));
			doc.addKeyword(new DefaultKeyword(4, 6, "word.nn", "天気", "天気"));
		}

		String endPoint = "http://localhost:8983/solr/";
		String collection = "unittest";

		try (SolrDocumentImporter importer = new SolrDocumentImporter()) {
			importer.setProperty("endPoint", endPoint);
			importer.setProperty("collection", collection);
			importer.setProperty("keyword_facet_field_mapping", "word->word_ss");
			importer.setProperty("attribute_field_mapping", "text->text_txt_ja,field2x->field2");

			importer.importDocument(doc);
			importer.commit();
		}

	}

}
