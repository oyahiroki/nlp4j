package hellovector2;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.solr.client.solrj.SolrRequest.METHOD;
import org.apache.solr.client.solrj.impl.Http2SolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.MultiMapSolrParams;

import com.google.gson.Gson;

import nlp4j.NlpServiceResponse;
import nlp4j.llm.embeddings.LlmClient;
//import nlp4j.llm.embeddings.EmbeddingResponse;
//import nlp4j.llm.embeddings.EmbeddingServiceViaHttp;
import nlp4j.util.DoubleUtils;
import nlp4j.util.FloatUtils;

public class Hello3_Query1024_2 {

	public static void main(String[] args) throws Exception {
		String text = "信号待ちをしていた";
		String endPoint = "http://localhost:8888/";
//		EmbeddingServiceViaHttp nlp = new EmbeddingServiceViaHttp(endPoint);
//		NlpServiceResponse res = nlp.process(text);
//		System.err.println(res);
//		System.err.println(JsonUtils.prettyPrint(res.getOriginalResponseBody()));
//		JsonObject jo = JsonObjectUtils.fromJson(res.getOriginalResponseBody());
//		System.err.println(JsonUtils.prettyPrint(jo));

//		EmbeddingResponse r = (new Gson()).fromJson(res.getOriginalResponseBody(), EmbeddingResponse.class);
//		System.err.println(Arrays.toString(r.getEmbeddings()));
//		System.err.println(DoubleUtils.toPlainString(r.getEmbeddings()));
//		System.err.println(r.getEmbeddings().length);

		{
			String endPoint2 = "http://localhost:8983/solr/";
			String collection = "vector1024";

			try (Http2SolrClient solrClient = new Http2SolrClient.Builder(endPoint2) //
					.withConnectionTimeout(10 * 1000, TimeUnit.MILLISECONDS)//
					.build(); LlmClient nlp = new LlmClient(endPoint);) {

				final Map<String, String[]> requestParamsSolr = new HashMap<>();

//				requestParamsSolr.put("q", new String[] { "{!knn f=vector topK=10}[1.0, 2.4, 3.5, 4.0]" });
				requestParamsSolr.put("q", new String[] { "{!knn f=vector1024 topK=10}"
//				+ DoubleUtils.toPlainString(r.getEmbeddings())
						+ FloatUtils.toPlainString(nlp.embedding(text))

						//
						+ "" });
				requestParamsSolr.put("fl", new String[] { "id", "score", "text_txt_ja" });

				MultiMapSolrParams solrQueryParams = new MultiMapSolrParams(requestParamsSolr);

				QueryResponse solrResponse = solrClient //
						.query(collection, solrQueryParams, METHOD.POST); // throws RemoteSolrException

//				System.err.println(solrResponse.jsonStr());
//				solrResponse.getResponse().forEach(o -> {
//					System.err.println("" + o.getKey() + "=" + o.getValue());
//
//				});

				solrResponse.getResults().forEach(d -> {
					System.err.println(d.toString());

				});

			}
		}

	}

}
