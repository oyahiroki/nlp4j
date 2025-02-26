package hellovector2;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.apache.solr.client.solrj.impl.Http2SolrClient;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import nlp4j.Document;
import nlp4j.NlpServiceResponse;
import nlp4j.annotator.AttributeNameConverter;
import nlp4j.annotator.FunctionDocumentAnnotator;
import nlp4j.crawler.JsonLineSeparatedCrawler;
import nlp4j.json.JsonFileReader;
import nlp4j.llm.embeddings.LlmClient;
//import nlp4j.llm.embeddings.EmbeddingResponse;
//import nlp4j.llm.embeddings.EmbeddingServiceViaHttp;
import nlp4j.util.DateUtils;
import nlp4j.util.DocumentUtil;
import nlp4j.util.DocumentsUtils;
import nlp4j.util.DoubleUtils;
import nlp4j.util.JsonUtils;
import nlp4j.util.StringUtils;
import nlp4j.util.TextUtils;

public class Hello2_Add1024_3 {

	public static void main(String[] args) throws Exception {

		boolean debug = true;

		File jsonFile = new File("C:/Users/oyahi/git/nlp4j-data/data-mlit/mlit_carinfo-202210_json.txt");
		JsonFileReader jfr = new JsonFileReader(jsonFile);

		JsonLineSeparatedCrawler crawler = new JsonLineSeparatedCrawler();
		crawler.setProperty("file", jsonFile.getAbsolutePath());

		List<Document> docs = crawler.crawlDocuments();
		System.err.println(docs.size());

		for (Document doc : docs) {
			JsonObject jo1 = DocumentUtil.toJsonObject(doc);
			System.err.println(JsonUtils.prettyPrint(jo1));
			{
				AttributeNameConverter c = new AttributeNameConverter();
				{
					String mapping = //
							"番号->id," + //
									"受付日->date," + // Date
//								"性別->gender," + //
									"住所->address," + //
									"申告方法->method," + //
									"車名->brand," + //
									"通称名->vehicle_name," + //
									"初度登録年月->date_reg," + // Date
									"総走行距離->total_milage," + // Integer
									"型式->model_name," + //
									"原動機型式->engine_model," + //
									"不具合装置->parts_defective," + //
									"発生時期->time_emergence," + //
									"申告内容の要約->text" //
					;
					c.setProperty("mapping", mapping);
				}
				c.annotate(doc);
			}

			{
				FunctionDocumentAnnotator ann = new FunctionDocumentAnnotator();
				ann.put("date", v -> {
//					return DateUtils.toISO8601((String) v, "yyyy年MM月dd日");
					return DateUtils.toDate((String) v, "yyyy年MM月dd日");
				});
				ann.put("method", v -> {
					return TextUtils.nfkc((String) v);
				});
				ann.annotate(doc);
			}

			System.err.println(JsonUtils.prettyPrint(DocumentUtil.toJsonObject(doc)));

//			if (debug)
//				return;

//			 String text = "１日はとてもいい天気でした。";
			String text = doc.getText();

			String endPoint = "http://localhost:8888/";
//			EmbeddingServiceViaHttp nlp = new EmbeddingServiceViaHttp(endPoint);
//			NlpServiceResponse res = nlp.process(text);
//			System.err.println(res);
//			System.err.println(JsonUtils.prettyPrint(res.getOriginalResponseBody()));
//			JsonObject jo = JsonObjectUtils.fromJson(res.getOriginalResponseBody());
//			System.err.println(JsonUtils.prettyPrint(jo));

//			EmbeddingResponse r = (new Gson()).fromJson(res.getOriginalResponseBody(), EmbeddingResponse.class);
//			System.err.println(Arrays.toString(r.getEmbeddings()));
//			System.err.println(r.getEmbeddings().length);

			{
				String endPoint2 = "http://localhost:8983/solr/";
				String collection = "vector1024";

				try (Http2SolrClient solrClient = new Http2SolrClient.Builder(endPoint2 + collection) //
						.build(); //
						LlmClient nlp = new LlmClient(endPoint); //

				) {

					// org.apache.solr.common.SolrInputDocument
					SolrInputDocument inputDocument = new SolrInputDocument();
					{
//						inputDocument.addField("id", "002");
//						inputDocument.addField("text_txt_ja", text); // *_txt_ja
//						inputDocument.addField("field1_s", "aaa"); // *_s
//						String[] ss = { "aaa", "bbb", "ccc" };
//						inputDocument.addField("field2_ss", ss); // *_ss

						inputDocument.addField("id", doc.getId());
						inputDocument.addField("address_s", doc.getAttribute("address"));
						inputDocument.addField("brand_s", doc.getAttribute("brand"));
						inputDocument.addField("vehicle_name", doc.getAttribute("vehicle_name"));
						inputDocument.addField("text_txt_ja", doc.getText());
//						inputDocument.setField("vector1024", DoubleUtils.toFloatList(r.getEmbeddings()));
						inputDocument.setField("vector1024", nlp.embedding(text));
//						inputDocument.setField("vector1024", DoubleUtils.toPlainString(r.getEmbeddings()));
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

}
