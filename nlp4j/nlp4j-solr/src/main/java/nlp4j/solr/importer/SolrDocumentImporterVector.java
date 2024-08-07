package nlp4j.solr.importer;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.Http2SolrClient;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;

import com.google.gson.Gson;

import nlp4j.AbstractDocumentImporter;
import nlp4j.Document;
import nlp4j.DocumentImporter;
import nlp4j.NlpServiceResponse;
import nlp4j.llm.embeddings.EmbeddingResponse;
import nlp4j.llm.embeddings.EmbeddingServiceViaHttp;
import nlp4j.util.DoubleUtils;

public class SolrDocumentImporterVector extends AbstractDocumentImporter implements DocumentImporter, AutoCloseable {

	@Override
	public void importDocument(Document doc) throws IOException {
		{

			{
				String endPoint2 = "http://localhost:8983/solr/";
				String collection = "sandbox";

				try (Http2SolrClient solrClient = new Http2SolrClient.Builder(endPoint2 + collection) //
						.build();) {

					// org.apache.solr.common.SolrInputDocument
					SolrInputDocument inputDocument = new SolrInputDocument();
					{
						inputDocument.addField("id", "001");
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

						System.err.println(solrResponse.jsonStr());

						System.err.println(res2.jsonStr());

					} catch (SolrServerException | IOException e) {
						e.printStackTrace();
					}
				}
			}

		}

	}

	@Override
	public void commit() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

}
