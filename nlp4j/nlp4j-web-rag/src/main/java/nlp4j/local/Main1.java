package nlp4j.local;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nlp4j.annotator.KeywordFacetMappingAnnotator;
import nlp4j.krmj.annotator.KuromojiAnnotator;
import nlp4j.servlet.Config;
import nlp4j.solr.importer.SolrDocumentImporterVector;
import nlp4j.util.DocumentUtil;

public class Main1 {
	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	public static void main(String[] args) throws Exception {

		String fileName = "C:/Users/oyahi/git/nlp4j-data/data-ibmjcareer/ibmcareer_json_openai-small.txt";
		File file = new File(fileName);

//		try (JsonFileReader jfr = new JsonFileReader(file);) {
//			jfr.jsonObjectStream().forEach(jo -> {
//				System.err.println("OK");
//			});
//		}

		String solr_endPoint = Config.SOLR_ENDPOINT;
		String solr_collection = Config.SOLR_COLLECTION;

		DocumentUtil.stream(file).forEach(doc -> {
			System.err.println("OK");
			System.err.println(doc.getAttributeAsListNumbers("vector").size());
			{
				{ // Kuromoji
					KuromojiAnnotator ann = new KuromojiAnnotator();
					ann.setProperty("target", "text");
					try {
						ann.annotate(doc);
					} catch (Exception e) {
						e.printStackTrace();
					} // throws Exception
				}
				{ // Keyword Facet Mapping for Solr
					KeywordFacetMappingAnnotator ann = new KeywordFacetMappingAnnotator();
					ann.setProperty("mapping", KeywordFacetMappingAnnotator.DEFAULT_MAPPING);
					try {
						ann.annotate(doc);
					} catch (Exception e) {
						e.printStackTrace();
					} // throws Exception
				}

			}
			logger.info("importing ...");
			{ // IMPORT DOCUMENT
				SolrDocumentImporterVector importer = new SolrDocumentImporterVector();
				{
					importer.setProperty("endPoint", solr_endPoint);
					importer.setProperty("collection", solr_collection);
					importer.setProperty("keyword_facet_field_mapping", //
							"word->word_ss" //
					);
					importer.setProperty("attribute_field_mapping", //
							"text->text_txt_ja" //
					);
				}
				try {
					importer.importDocument(doc);
					importer.commit(); // throws IOException
					importer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} // throws IOException

			}
			logger.info("importing ... done");

		});

	}

}
