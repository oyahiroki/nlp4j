package nlp4j.lucene;

import junit.framework.TestCase;

public class LocalSearchTestCase extends TestCase {

	public void testSearch() throws Exception {

		try (LocalSearch search = new LocalSearch("ja")) {
			search.add("1", "東京都は日本の都道府県のひとつです");
			search.add("2", "京都は日本の都市です。");
			search.add("3", "京都市には任天堂の本社があります");
			search.addJson("""
					{
					"id":"4",
					"body":"京都府は広いです"
					}
					""");
			search.commit();
			SearchResult[] result = search.search("京都", 10);
			System.out.println("size: " + result.length);
			for (int n = 0; n < result.length; n++) {
				System.out.println("result[" + n + "].id: " + result[n].id);
				System.out.println("result[" + n + "].body: " + result[n].body);
				System.out.println("result[" + n + "].score: " + result[n].score);
			}
		}

// Expected output
//		size: 3
//		result[0].id: 1
//		result[0].body: 京都は日本の都市です。
//		result[0].score: 0.1805949
//		result[1].id: 3
//		result[1].body: 京都府は広いです
//		result[1].score: 0.1805949
//		result[2].id: 2
//		result[2].body: 京都市には任天堂の本社があります
//		result[2].score: 0.16212496


	}

}
