package nlp4j.solr.importer;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.Http2SolrClient;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;

import nlp4j.AbstractDocumentImporter;
import nlp4j.Document;
import nlp4j.DocumentImporter;
import nlp4j.util.DoubleUtils;

public class SolrDocumentImporterVector extends AbstractDocumentImporter implements DocumentImporter, AutoCloseable {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	private String solr_endPoint = "http://localhost:8983/solr/";
	private String solr_collection = "sandbox";

	@Override
	public void setProperty(String key, String value) {

		super.setProperty(key, value);

		if ("endpoint".equals(key.toLowerCase())) {
			this.solr_endPoint = value;
		}
		if ("collection".equals(key.toLowerCase())) {
			this.solr_collection = value;
		}

	}

	@Override
	public void importDocument(Document doc) throws IOException {
		{
			{
				try (Http2SolrClient solrClient = new Http2SolrClient.Builder(solr_endPoint + solr_collection) //
						.build();) {
					// org.apache.solr.common.SolrInputDocument
					SolrInputDocument inputDocument = new SolrInputDocument();
					{
						inputDocument.addField("id", doc.getId());
						inputDocument.addField("text_txt_ja", doc.getText()); // *_txt_ja
//						inputDocument.addField("field1_s", "aaa"); // *_s
//						String[] ss = { "aaa", "bbb", "ccc" };
//						inputDocument.addField("field2_ss", ss); // *_ss
						inputDocument.setField("vector",
								DoubleUtils.toFloatList(doc.getAttributeAsListNumbers("vector")));
//						inputDocument.setField("vector1024", DoubleUtils.toPlainString(r.getEmbeddings()));
					}

					UpdateResponse solrResponse;
					try {
						solrResponse = solrClient //
								.add(inputDocument);
						UpdateResponse res2 = solrClient.commit();
						if (logger.isDebugEnabled()) {
							logger.info(solrResponse.jsonStr());
							logger.info(res2.jsonStr());
						}
					} catch (SolrServerException | IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	@Override
	public void commit() throws IOException {
	}

	@Override
	public void close() {
	}

}
