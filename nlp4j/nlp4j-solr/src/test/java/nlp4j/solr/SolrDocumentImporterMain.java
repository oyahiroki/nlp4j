package nlp4j.solr;

import nlp4j.Document;
import nlp4j.impl.DefaultDocument;
import nlp4j.solr.importer.SolrDocumentImporter;

public class SolrDocumentImporterMain {

	public static void main(String[] args) throws Exception {

		Document doc = new DefaultDocument();
		{
//			doc.put
		}

		SolrDocumentImporter importer = new SolrDocumentImporter();

		importer.importDocument(doc);

		importer.commit();

		importer.close();

	}

}
