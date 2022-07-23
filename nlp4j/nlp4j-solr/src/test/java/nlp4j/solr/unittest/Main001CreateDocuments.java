package nlp4j.solr.unittest;

import java.util.ArrayList;
import java.util.List;

import nlp4j.Document;
import nlp4j.impl.DefaultDocument;
import nlp4j.impl.DefaultKeyword;
import nlp4j.solr.importer.SolrDocumentImporter;

public class Main001CreateDocuments {

	/**
	 * <pre>
	 * テスト用のドキュメントを追加する
	 * Add documents for unit test
	 * </pre>
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		List<Document> docs = new ArrayList<>();

		{
			Document doc = new DefaultDocument();
			doc.putAttribute("id", "doc0001");
			// dynamic field
			// *_txt_ja
			doc.putAttribute("text", "今日はいい天気です。"); // text->text_txt_ja
			doc.putAttribute("field1_s", "AAA");
			doc.putAttribute("field2", "BBB");
			doc.putAttribute("field3", 100);
			doc.putAttribute("date_dt", "2022-12-31T23:59:59Z");

			doc.addKeyword(new DefaultKeyword(0, 2, "word.nn", "今日", "今日"));
			doc.addKeyword(new DefaultKeyword(3, 5, "word.adj", "いい", "いい"));
			doc.addKeyword(new DefaultKeyword(4, 6, "word.nn", "天気", "天気"));
			docs.add(doc);
		}
		{
			Document doc = new DefaultDocument();
			doc.putAttribute("id", "doc0002");
			// dynamic field
			// *_txt_ja
			doc.putAttribute("text", "明日はいい天気です。"); // text->text_txt_ja
			doc.putAttribute("field1_s", "AAA");
			doc.putAttribute("field2", "BBB");
			doc.putAttribute("field3", 100);

			doc.addKeyword(new DefaultKeyword(0, 2, "word.nn", "明日", "明日"));
			doc.addKeyword(new DefaultKeyword(3, 5, "word.adj", "いい", "いい"));
			doc.addKeyword(new DefaultKeyword(4, 6, "word.nn", "天気", "天気"));
			docs.add(doc);
		}
		{
			Document doc = new DefaultDocument();
			doc.putAttribute("id", "doc0003");
			// dynamic field
			// *_txt_ja
			doc.putAttribute("text", "昨日もいい天気です。"); // text->text_txt_ja
			doc.putAttribute("field1_s", "AAA");
			doc.putAttribute("field2", "BBB");
			doc.putAttribute("field3", 100);

			doc.addKeyword(new DefaultKeyword(0, 2, "word.nn", "昨日", "昨日"));
			doc.addKeyword(new DefaultKeyword(3, 5, "word.adj", "いい", "いい"));
			doc.addKeyword(new DefaultKeyword(4, 6, "word.nn", "天気", "天気"));
			docs.add(doc);
		}

		String endPoint = "http://localhost:8983/solr/";
		String collection = "unittest";

		try (SolrDocumentImporter importer = new SolrDocumentImporter()) {
			importer.setProperty("endPoint", endPoint);
			importer.setProperty("collection", collection);
			importer.setProperty("keyword_facet_field_mapping", "word->word_ss");
			importer.setProperty("attribute_field_mapping", "text->text_txt_ja,field2->field2_s,field3->field2_i");

			importer.importDocuments(docs);
			importer.commit();
		}
//		{
//			  "responseHeader":{
//			    "status":0,
//			    "QTime":1,
//			    "params":{
//			      "q":"*:*",
//			      "indent":"true",
//			      "q.op":"OR",
//			      "_":"1658454727992"}},
//			  "response":{"numFound":1,"start":0,"numFoundExact":true,"docs":[
//			      {
//			        "id":"doc0001",
//			        "text_txt_ja":"今日はいい天気です。",
//			        "field1_s":"AAA",
//			        "field2_s":"BBB",
//			        "field2_i":100,
//			        "word_ss":["今日",
//			          "いい",
//			          "天気"],
//			        "_version_":1739015817850257408}]
//			  }}
	}

}
