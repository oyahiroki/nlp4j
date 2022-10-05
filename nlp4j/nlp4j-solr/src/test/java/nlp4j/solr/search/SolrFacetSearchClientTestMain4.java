package nlp4j.solr.search;

public class SolrFacetSearchClientTestMain4 {

	public static void main(String[] args) throws Exception {

//		String item = "学校";
//		String item = "お婆さん"; // -> NOT FOUND
//		String item = "おばあさん"; // -> NOT FOUND
		String item = "祖母";

		SolrFacetSearchClientTest.check(item);

	}

}
