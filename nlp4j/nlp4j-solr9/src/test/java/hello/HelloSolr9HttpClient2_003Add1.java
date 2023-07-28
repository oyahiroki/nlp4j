package hello;

import java.util.Arrays;

import org.apache.solr.client.solrj.impl.Http2SolrClient;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;

/**
 * Example of facet search
 *
 */
public class HelloSolr9HttpClient2_003Add1 {

	public static void main(String[] args) throws Exception {

		String endPoint = "http://localhost:8983/solr/";
//		String collection = "gettingstarted";
		String collection = "sandbox";

		try (Http2SolrClient solrClient = new Http2SolrClient.Builder(endPoint + collection) //
				.build();) {

			// org.apache.solr.common.SolrInputDocument
			SolrInputDocument inputDocument = new SolrInputDocument();
			{
				inputDocument.addField("id", "001");
				inputDocument.addField("text_txt_ja", "今日はいい天気です。"); // *_txt_ja
				inputDocument.addField("field1_s", "aaa"); // *_s
				String[] ss = { "aaa", "bbb", "ccc" };
				inputDocument.addField("field2_ss", ss); // *_ss
				inputDocument.setField("vector", Arrays.asList(1.0f, 2.5f, 3.7f, 4.1f));
			}

			UpdateResponse solrResponse = solrClient //
					.add(inputDocument);

			UpdateResponse res = solrClient.commit();

			System.err.println(solrResponse.jsonStr());

			System.err.println(res.jsonStr());

		}

	}

}
