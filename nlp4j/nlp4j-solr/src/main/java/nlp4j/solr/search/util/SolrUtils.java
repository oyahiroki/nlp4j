package nlp4j.solr.search.util;

import org.apache.solr.client.solrj.util.ClientUtils;

public class SolrUtils {

	static public String escapeQueryChars(String queryChars) {
		if ("AND".equals(queryChars) || "OR".equals(queryChars) || "NOT".equals(queryChars)) {
			return "\\" + queryChars;
		}
		return ClientUtils.escapeQueryChars(queryChars);
	}

}
