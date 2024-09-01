package nlp4j.opensearch.importer;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import nlp4j.AbstractDocumentImporter;
import nlp4j.Document;
import nlp4j.DocumentImporter;
import nlp4j.NlpServiceResponse;
import nlp4j.opensearch.SimpleOpenSearchClient;
import nlp4j.util.DoubleUtils;

public class OpenSearchDocumentImporter extends AbstractDocumentImporter implements DocumentImporter, AutoCloseable {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

//	String index = "myindex1";
	String index = "hello-hybrid";
	String opensearch_endpoint = "https://localhost:9200";
	String openasearch_admin = "admin";

	@Override
	public void setProperty(String key, String value) {
		if ("index".equals(key)) {
			this.index = value;
		} //
		else if ("opensearch_endpoint".equals(key)) {
			this.opensearch_endpoint = value;
		} else if ("openasearch_admin".equals(key)) {
			this.openasearch_admin = value;
		}
	}

	@Override
	public void importDocument(Document d) throws IOException {
		String password = "";

		{
			password = System.getProperty("OPENSEARCH_PASSWORD");
			if (password == null) {
				System.err.println("password is not set.");
			}
		}

		try (SimpleOpenSearchClient client = new SimpleOpenSearchClient(opensearch_endpoint, openasearch_admin,
				password, index);) {

			JsonObject doc = new JsonObject();
			{
				{ // ID
					String field_id = "id";
					String id = d.getId();
					if (id != null) {
						doc.addProperty(field_id, id);
					}
				}
				{
					String field_keyword = "items";
					JsonArray kwds = new JsonArray();
					d.getKeywords().forEach(k -> {
						kwds.add(k.getLex());
					});
					doc.add(field_keyword, kwds);
				}
				{
//					JsonArray items = new JsonArray();
//					items.add("aaa");
//					items.add("bbb");
//					items.add("ccc");
//					doc.add("items", items);
				}
				{
					String field_text = "text_txt_ja";
					String text_txt_ja = d.getText();
					doc.addProperty(field_text, text_txt_ja);
				}
				{
					String doc_attribute_vector = "vector1024";
					String field_vector = "vector1024";
					float[] v = DoubleUtils.toFloatArray(d.getAttributeAsListNumbers(doc_attribute_vector));
					JsonArray vv = new JsonArray();
					for (int n = 0; n < v.length; n++) {
						vv.add(v[n]);
					}
					doc.add(field_vector, vv);
				}
			}
			NlpServiceResponse res = client.postDoc(doc);
			System.err.println(res.getOriginalResponseBody());
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
