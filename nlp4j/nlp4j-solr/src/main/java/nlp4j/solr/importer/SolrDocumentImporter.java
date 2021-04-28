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

	HttpSolrClient solrClient;

	private String endPoint = "http://localhost:8983/solr/";
	private String collection = null;

	@Override
	public void setProperty(String key, String value) {
		super.setProperty(key, value);

		if ("endPoint".equals(key)) {
			this.endPoint = value;
		} //
		else if ("collection".equals(key)) {
			this.collection = value;
		}
	}

	@Override
	public void importDocument(Document doc) throws IOException {

		if (collection == null) {
			throw new NullPointerException("Parameter 'collection' is not set");
		}

		if (this.solrClient == null) {
			solrClient = new HttpSolrClient.Builder(this.endPoint + this.collection).build();
			solrClient.setParser(new XMLResponseParser());
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
			inputDocument.addField("word_ss", kwds);
		}

		try {
			solrClient.add(inputDocument);
		} //
		catch (SolrServerException | IOException e) {
			e.printStackTrace();
			throw new IOException(e);
		}
	}

	@Override
	public void commit() throws IOException {

		try {
			solrClient.commit();
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
			throw new IOException(e);
		}

	}

	@Override
	public void close() {

	}

}
