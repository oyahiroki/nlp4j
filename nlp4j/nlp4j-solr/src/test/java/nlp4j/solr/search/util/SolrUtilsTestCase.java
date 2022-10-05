package nlp4j.solr.search.util;

import junit.framework.TestCase;

public class SolrUtilsTestCase extends TestCase {

	public void testEscapeQueryChars101() {
		String q1 = "TEST";
		String q2 = SolrUtils.escapeQueryChars(q1);
		System.err.println(q2);
	}

	public void testEscapeQueryChars102() {
		String q1 = "TEST TEST";
		String q2 = SolrUtils.escapeQueryChars(q1);
		System.err.println(q2);
	}

	public void testEscapeQueryChars103() {
		String q1 = "+チック姉さん";
		String q2 = SolrUtils.escapeQueryChars(q1);
		System.err.println(q2);
	}

}
