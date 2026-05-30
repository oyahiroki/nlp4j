package nlp4j.lucene;

public class LocalSearchExampleMain_Ja {

	public static void main(String[] args)  {

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
	}
}
