package hellovector2;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.apache.solr.client.solrj.impl.Http2SolrClient;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;

import com.google.gson.Gson;

import nlp4j.Document;
import nlp4j.NlpServiceResponse;
import nlp4j.crawler.JsonLineSeparatedCrawler;
import nlp4j.json.JsonFileReader;
import nlp4j.llm.embeddings.EmbeddingResponse;
import nlp4j.llm.embeddings.EmbeddingServiceViaHttp;
import nlp4j.util.DocumentUtil;
import nlp4j.util.DocumentsUtils;
import nlp4j.util.DoubleUtils;
import nlp4j.util.JsonUtils;

public class Hello2_Add1024_2 {

	public static void main(String[] args) throws Exception {

		boolean debug = true;

		File jsonFile = new File("C:/Users/oyahi/git/nlp4j-data/data-mlit/mlit_carinfo-202210_json.txt");
		JsonFileReader jfr = new JsonFileReader(jsonFile);

		JsonLineSeparatedCrawler crawler = new JsonLineSeparatedCrawler();
		crawler.setProperty("file", jsonFile.getAbsolutePath());

		List<Document> docs = crawler.crawlDocuments();
		System.err.println(docs.size());

		Document doc1 = docs.get(0);

		System.err.println(JsonUtils.prettyPrint(DocumentUtil.toJsonObject(doc1)));

		if (debug)
			return;

		String text = "１日はとてもいい天気でした。";
		String endPoint = "http://localhost:8888/";
		EmbeddingServiceViaHttp nlp = new EmbeddingServiceViaHttp(endPoint);
		NlpServiceResponse res = nlp.process(text);
//		System.err.println(res);
//		System.err.println(JsonUtils.prettyPrint(res.getOriginalResponseBody()));
//		JsonObject jo = JsonObjectUtils.fromJson(res.getOriginalResponseBody());
//		System.err.println(JsonUtils.prettyPrint(jo));

		EmbeddingResponse r = (new Gson()).fromJson(res.getOriginalResponseBody(), EmbeddingResponse.class);
//		System.err.println(Arrays.toString(r.getEmbeddings()));
//		System.err.println(r.getEmbeddings().length);

		{
			String endPoint2 = "http://localhost:8983/solr/";
			String collection = "vector1024";

			try (Http2SolrClient solrClient = new Http2SolrClient.Builder(endPoint2 + collection) //
					.build();) {

				// org.apache.solr.common.SolrInputDocument
				SolrInputDocument inputDocument = new SolrInputDocument();
				{
					inputDocument.addField("id", "002");
					inputDocument.addField("text_txt_ja", text); // *_txt_ja
					inputDocument.addField("field1_s", "aaa"); // *_s
					String[] ss = { "aaa", "bbb", "ccc" };
					inputDocument.addField("field2_ss", ss); // *_ss
					inputDocument.setField("vector1024", DoubleUtils.toFloatList(r.getEmbeddings()));
//					inputDocument.setField("vector1024", DoubleUtils.toPlainString(r.getEmbeddings()));
				}

				UpdateResponse solrResponse = solrClient //
						.add(inputDocument);

				UpdateResponse res2 = solrClient.commit();

				System.err.println(solrResponse.jsonStr());

				System.err.println(res2.jsonStr());

			}
		}

	}

}
