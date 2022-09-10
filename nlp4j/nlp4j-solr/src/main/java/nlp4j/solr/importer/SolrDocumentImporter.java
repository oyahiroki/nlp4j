package nlp4j.solr.importer;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.common.SolrInputDocument;

import nlp4j.AbstractDocumentImporter;
import nlp4j.Document;
import nlp4j.DocumentImporter;
import nlp4j.Keyword;

/**
 * <pre>
 * Apache Solr にドキュメントを追加する
 * Document Importer for Apache Solr
 * </pre>
 * 
 * @author Hiroki Oya
 * @since 1.3.1.0
 *
 */
public class SolrDocumentImporter extends AbstractDocumentImporter implements DocumentImporter, AutoCloseable {

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	HttpSolrClient solrClient;

	private String endPoint = "http://localhost:8983/solr/";
	private String collection = null;

	// キーワードのファセットをフィールドにマップする Map keyword facet to Document field
	private String keyword_facet_field_mapping = null;
	private String attribute_field_mapping = null;

	@Override
	public void setProperty(String key, String value) {
		super.setProperty(key, value);
		if ("endPoint".equals(key)) {
			this.endPoint = value;
		} //
		else if ("collection".equals(key)) {
			this.collection = value;
		} //
			// キーワードのファセットをフィールドにマップする Map keyword facet to Document field
		else if ("keyword_facet_field_mapping".equals(key)) {
			this.keyword_facet_field_mapping = value;
		} //
		else if ("attribute_field_mapping".equals(key)) {
			this.attribute_field_mapping = value;
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

		logger.info("importing document...");

		// org.apache.solr.common.SolrInputDocument
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
			Map<String, String> map = new HashMap<>();

			// FIELD MAPPING
			if (this.attribute_field_mapping != null) {
				String[] att_field_maps = attribute_field_mapping.split(",");
				for (String att_field_map : att_field_maps) {
					String[] mp = att_field_map.split("->");
					if (mp.length != 2) {
						continue;
					} //
					else {
						String att = mp[0];
						String field = mp[1];
						map.put(att, field);
					}
				}
			}

			// DOCUMENT -> IMPORT DOCUMENT
			for (String key : doc.getAttributeKeys()) {
				String field = key;
				if (map.containsKey(key)) {
					field = map.get(key);
				}
				Object o = doc.getAttribute(key);

				logger.debug(o.getClass().getName());

				if (o instanceof List) {
					@SuppressWarnings({ "rawtypes" })
					List list = (List) o;
					List<String> list2 = new ArrayList<>();
					for (Object x : list) {
						list2.add(x.toString());
					}
					inputDocument.addField(field, list2.toArray(new String[0]));
				} //
				else if (o instanceof String) {
					String s = (String) o;
					inputDocument.addField(field, s);
				} //
				else if (o instanceof Number) {
					Number num = (Number) o;
					inputDocument.addField(field, num);
				} //
				else {
					inputDocument.addField(field, o);
				}

			}
		}

//		{
//			ArrayList<String> kwds = new ArrayList<>();
//			for (Keyword kwd : doc.getKeywords("word")) {
//				kwds.add(kwd.getLex());
//			}
//			inputDocument.addField("word_ss", kwds);
//		}

		// キーワードのファセットをフィールドにマップする Map keyword facet to Document field
		if (this.keyword_facet_field_mapping != null) {
			String[] facet_field_maps = this.keyword_facet_field_mapping.split(",");
			for (String facet_field_map : facet_field_maps) {
				String[] map = facet_field_map.split("->");
				if (map.length != 2) {
					continue;
				} //
				else {
					String facet = map[0];
					String field = map[1];
					ArrayList<String> kwds = new ArrayList<>();
					for (Keyword kwd : doc.getKeywords(facet)) {
						kwds.add(kwd.getLex());
					}
					inputDocument.addField(field, kwds);
				} // END OF IF
			} // END OF FOR
		} // END OF IF

		try {
			solrClient.add(inputDocument); // throws SolrServerException | IOException e
		} //
		catch (SolrServerException | IOException e) {
			e.printStackTrace();
			throw new IOException(e);
		}
	}

	@Override
	public void commit() throws IOException {
		if (solrClient != null) {
			try {
				solrClient.commit();
			} catch (SolrServerException | IOException e) {
				e.printStackTrace();
				throw new IOException(e);
			}
		}

	}

	@Override
	public void close() {

	}

}
