package nlp4j.solr.importer;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.common.SolrInputDocument;

import nlp4j.AbstractDocumentImporter;
import nlp4j.Document;
import nlp4j.DocumentImporter;
import nlp4j.Keyword;

/**
 * @author Hiroki Oya
 * @since 1.3.1.0
 *
 */
public class SolrDocumentImporter extends AbstractDocumentImporter implements DocumentImporter {
	HttpSolrClient solr;

	@Override
	public void importDocument(Document doc) throws IOException {

		if (this.solr == null) {
			String urlString = "http://localhost:8983/solr/sandbox";
			solr = new HttpSolrClient.Builder(urlString).build();
			solr.setParser(new XMLResponseParser());
		}

		SolrInputDocument inputDocument = new SolrInputDocument();
//		{
//			inputDocument.addField("id", "123456");
//			inputDocument.addField("text_en", "This is test");
//			ArrayList<String> kwds = new ArrayList<>();
//			{
//				kwds.add("aaa");
//				kwds.add("aaa");
//				kwds.add("bbb");
//				kwds.add("ccc");
//			}
//			inputDocument.addField("word_noun_ss", kwds);
//		}

		{
			for (String key : doc.getAttributeKeys()) {
				inputDocument.addField(key, doc.getAttribute(key));
			}
		}

		{
			ArrayList<String> kwds = new ArrayList<>();
			for (Keyword kwd : doc.getKeywords("word")) {
				kwds.add(kwd.getLex());
			}
			inputDocument.addField("word_noun_ss", kwds);
		}

		try {
			solr.add(inputDocument);
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
			throw new IOException(e);
		}
	}

	@Override
	public void commit() throws IOException {

		try {
			solr.commit();
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
			throw new IOException(e);
		}

	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

}
