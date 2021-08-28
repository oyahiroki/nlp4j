package nlp4j.solr.importer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.common.SolrInputDocument;

import nlp4j.AbstractDocumentImporter;
import nlp4j.Document;
import nlp4j.DocumentImporter;
import nlp4j.Keyword;

/**
 * @author Hiroki Oya
 * @since 1.3.1.0
 *
 */
public class SolrDocumentImporter extends AbstractDocumentImporter implements DocumentImporter, AutoCloseable {

	HttpSolrClient solrClient;

	private String endPoint = "http://localhost:8983/solr/";
	private String collection = null;

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

			for (String key : doc.getAttributeKeys()) {

				String field = key;

				if (map.containsKey(key)) {
					field = map.get(key);
				}

				inputDocument.addField(field, doc.getAttribute(key));
			}
		}

//		{
//			ArrayList<String> kwds = new ArrayList<>();
//			for (Keyword kwd : doc.getKeywords("word")) {
//				kwds.add(kwd.getLex());
//			}
//			inputDocument.addField("word_ss", kwds);
//		}

		if (keyword_facet_field_mapping != null) {
			String[] facet_field_maps = keyword_facet_field_mapping.split(",");
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
				}

			}
		}

		try {
			solrClient.add(inputDocument);
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
