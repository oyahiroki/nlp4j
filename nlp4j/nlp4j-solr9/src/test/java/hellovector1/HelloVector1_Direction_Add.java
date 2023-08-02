package hellovector1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.solr.client.solrj.impl.Http2SolrClient;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;

/**
 * Example of facet search
 *
 */
public class HelloVector1_Direction_Add {

	public static void main(String[] args) throws Exception {

		String endPoint = "http://localhost:8983/solr/";
		String collection = "hellovector_1_direction";

		try (Http2SolrClient solrClient = new Http2SolrClient.Builder(endPoint + collection) //
				.build();) {

			// org.apache.solr.common.SolrInputDocument
			List<SolrInputDocument> docs = new ArrayList<>();
			{
				SolrInputDocument doc = new SolrInputDocument();
				{
					doc.addField("id", "001");
					doc.addField("text_txt_ja", "右(1.0,0.0)"); // *_txt_ja
					doc.setField("vector", Arrays.asList(1.0f, 0.0f));
				}
				docs.add(doc);
			}
			{
				SolrInputDocument doc = new SolrInputDocument();
				{
					doc.addField("id", "002");
					doc.addField("text_txt_ja", "上(0.0,1.0)"); // *_txt_ja
					doc.setField("vector", Arrays.asList(0.0f, 1.0f));
				}
				docs.add(doc);
			}
			{
				SolrInputDocument doc = new SolrInputDocument();
				{
					doc.addField("id", "003");
					doc.addField("text_txt_ja", "左(-1.0,0.0)"); // *_txt_ja
					doc.setField("vector", Arrays.asList(-1.0f, 0.0f));
				}
				docs.add(doc);
			}
			{
				SolrInputDocument doc = new SolrInputDocument();
				{
					doc.addField("id", "004");
					doc.addField("text_txt_ja", "下(0.0,-1.0)"); // *_txt_ja
					doc.setField("vector", Arrays.asList(0.0f, -1.0f));
				}
				docs.add(doc);
			}

			UpdateResponse solrResponse = solrClient //
					.add(docs);

			UpdateResponse res = solrClient.commit();

			System.err.println(solrResponse.jsonStr());

			System.err.println(res.jsonStr());

		}

	}

}
